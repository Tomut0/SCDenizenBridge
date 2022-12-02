package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.ObjectType;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.Request;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.minat0.scdenizenbridge.utils.ScriptUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;

import static ru.minat0.scdenizenbridge.utils.ReflectionUtils.getMethodValue;

public class DummyScriptEvent extends BukkitScriptEvent implements Listener {

    private final Set<Method> methods;
    private final String eventName;
    private final Class<? extends Event> event;
    private Event eventObj;

    public DummyScriptEvent(Class<? extends Event> event, Set<Method> methods) {
        this.event = event;
        this.eventName = event.getSimpleName();
        this.methods = methods;
        Bukkit.getPluginManager().registerEvent(event, this, EventPriority.MONITOR, (listener, scEvent) -> {
            eventObj = scEvent;
            fire(scEvent);
        }, SCDenizenBridge.getInstance());
    }

    /**
     * <h2>Retrieve a context of all events</h2>
     * <p>
     * In simply words, this method tries to compare <context.[value]> with existed method name.
     * Example: {@literal <}context.frame{@literal >} -{@literal >} getFrame
     * </p>
     * <p>
     * Then it tries to get a value from that method, gets a type of the objectTag and invoke it.
     * getFrame -{@literal >} new frameTag(getFrame()) -{@literal >} return
     * </p>
     *
     * @param name a value of the context, same as the method name
     * @return {@link ObjectTag} a tag
     */
    @Override
    public ObjectTag getContext(String name) {
        Optional<String> method = methods.stream().
                filter(m -> m.getParameterTypes().length == 0). // Only getters
                map(Method::getName).
                filter(methodName -> methodName.toLowerCase().endsWith(name)).findAny();

        if (method.isEmpty()) {
            return super.getContext(name);
        }

        Object value = Objects.requireNonNull(getEventValue(method.get()));

        if (!ObjectFetcher.objectsByName.containsKey(name)) {
            /*
             * Todo: More support for contexts
             *
             * Currently, it supports methods that are return objectTag:
             * i.e getClan -> objectsByName(clan) -> ClanTagType -> new ClanTag(value)
             *
             * Methods like <context.message> / <context.sender> isn't supported as objectsByName isn't contain sender tag.
             */
            return new ElementTag("Not supported yet!");
        }

        ObjectType<? extends ObjectTag> type = ObjectFetcher.objectsByName.get(name);

        try {
            return type.clazz.getDeclaredConstructor(value.getClass()).newInstance(value);
        } catch (NoSuchMethodException ignored) {
            // will never be thrown since check above
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            SCDenizenBridge.getSCPlugin().getLogger().log(Level.SEVERE, "Can't invoke {} event constructor with value {}: {}", new Object[]{eventName, value, ex.getMessage()});
        }

        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        ScriptUtils.SCHolder holder = getIssuer();
        return new ClanScriptEntryData(holder.player(), holder.clan());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        // Splits and lowers event name
        // Example: DisbandClanEvent -> disband clan
        var split = eventName.replace("Event", "").split("(?=[A-Z])");
        var eventName = String.join(" ", split).toLowerCase();

        // To not confuse with https://github.com/DenizenScript/Denizen/blob/dev/plugin/src/main/java/com/denizenscript/denizen/events/player/ChatScriptEvent.java#L28
        if (eventName.equals("chat")) eventName = "clan player chats";

        return path.eventLower.startsWith(eventName);
    }

    /**
     * Tries to find an issuer of the event
     *
     * @return {@link ru.minat0.scdenizenbridge.utils.ScriptUtils.SCHolder} the issuer
     */
    private @NotNull ScriptUtils.SCHolder getIssuer() {
        Optional<Clan> clanOpt = Optional.ofNullable(getEventValue("getClan"));
        Clan clan = clanOpt.orElse(null);

        Function<?, Player> toPlayer = commandSender -> commandSender instanceof Player pl ? pl : commandSender instanceof ClanPlayer cp ? cp.toPlayer() : null;

        Optional<Player> getSender = mapMethod("getSender", toPlayer);
        Optional<Player> getIssuer = mapMethod("getIssuer", toPlayer);
        Optional<Player> getPlayer = Optional.ofNullable(getEventValue("getPlayer"));

        Optional<Player> eventIssuer = getSender.or(() -> getIssuer.or(() -> getPlayer));
        if (eventIssuer.isPresent()) {
            return new ScriptUtils.SCHolder(eventIssuer.get(), clan);
        }

        Function<Request, Player> requestToPlayer = request -> request.getRequester().toPlayer();
        Function<BankOperator, Player> operatorToPlayer = operator -> Bukkit.getPlayerExact(operator.getName());

        // These methods require a unique way to retrieve issuer
        Player player = switch (eventName) {
            case "CreateClanEvent" -> clanOpt.map(c -> c.getLeaders().get(0).toPlayer()).orElse(null);
            case "FrameOpenEvent" -> mapMethod("getFrame", SCFrame::getViewer).orElse(null);
            case "ClanPlayerTeleportEvent", "PlayerHomeSetEvent" ->
                    mapMethod("getClanPlayer", ClanPlayer::toPlayer).orElse(null);
            case "ClanBalanceUpdateEvent" -> mapMethod("getUpdater", operatorToPlayer).orElse(null);
            case "RequestEvent", "RequestFinishedEvent" -> mapMethod("getRequest", requestToPlayer).orElse(null);
            default -> null;
        };

        return new ScriptUtils.SCHolder(player, clan);
    }

    /**
     * Processes map method on event method value
     *
     * @param name   method name
     * @param mapper map function
     * @param <T>    method value
     * @param <U>    processed value
     * @return Optional of processed method value
     */
    @SuppressWarnings("unchecked")
    private <T, U> Optional<U> mapMethod(@NotNull String name, @NotNull Function<? super T, ? extends U> mapper) {
        return Optional.ofNullable((T) getEventValue(name)).map(mapper);
    }

    /**
     * Retrieves a value from an event method
     *
     * @param name method name
     * @param <T>  method value
     * @return <T> value, can be nullable
     */
    private <T> @Nullable T getEventValue(@NotNull String name) {
        return getMethodValue(event, name, eventObj);
    }
}
