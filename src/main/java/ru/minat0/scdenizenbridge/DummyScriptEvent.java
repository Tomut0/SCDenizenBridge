package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.events.ChatEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.ClanBalanceUpdateEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.FrameOpenEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.WarEndEvent;
import net.sacredlabyrinth.phaed.simpleclans.loggers.BankOperator;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCComponent;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import ru.minat0.scdenizenbridge.objects.FrameTag;
import ru.minat0.scdenizenbridge.utils.ScriptUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public ObjectTag getContext(String name) {
        Optional<String> method = methods.stream().
                filter(m -> m.getParameterTypes().length == 0). // Only getters
                        map(Method::getName).
                filter(methodName -> methodName.startsWith("get") && methodName.toLowerCase().endsWith(name)).findAny();

        if (method.isEmpty()) {
            return super.getContext(name);
        }

        Object value = Objects.requireNonNull(getEventValue(method.get()));

        if (value instanceof SCFrame frame) {
            return new FrameTag(frame);
        }

        return switch (value.getClass().getSimpleName()) {
            case "Clan" -> new ClanTag(((Clan) value));
            case "ClanPlayer" -> new ClanPlayerTag(((ClanPlayer) value));
            // List of unsupported tags, possibly I will take care about them later
            case "SCComponent" -> new ItemTag(((SCComponent) value).getItem());
            case "Rank" -> new ElementTag(((Rank) value).getName());
            case "Request" -> new ElementTag(((Request) value).getType().name());
            case "WarEndEvent.Reason" -> new ElementTag(((WarEndEvent.Reason) value).name());
            case "BankOperator" -> new ElementTag(((BankOperator) value).getName());
            case "ClanBalanceUpdateEvent.Cause" -> new ElementTag(((ClanBalanceUpdateEvent.Cause) value).name());
            case "War" ->
                    new ListTag((Collection<? extends ObjectTag>) ((War) value).getClans().stream().map(ClanTag::new).collect(Collectors.toSet()));
            default -> CoreUtilities.objectToTagForm(value, getTagContext(tryingToBuildPath), false, true);
        };
    }

    @Override
    public boolean matches(ScriptPath path) {
        // SCDenizenBridge 2.1 compatibility
        if (!(event == FrameOpenEvent.class)) {
            return super.matches(path);
        }

        String frame = ((FrameOpenEvent) eventObj).getFrame().getClass().getSimpleName().replace("Frame", "");
        return frame.equalsIgnoreCase(path.eventArgLowerAt(1)) || path.eventArgLowerAt(1).equals("open");
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
        if (event == ChatEvent.class) {
            eventName = "clan player chats";
        } else if (event == FrameOpenEvent.class) {
            // SCDenizenBridge 2.1 compatibility
            return path.eventLower.startsWith("frame") && (ScriptUtils.frames.contains(path.eventArgLowerAt(1)) || path.eventLower.endsWith("open"));
        }

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

        // todo: make a function that takes any parameter and returns player, use it in switch (eventName) below
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
            case "FrameOpenEvent", "ComponentClickEvent" -> mapMethod("getFrame", SCFrame::getViewer).orElse(null);
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
