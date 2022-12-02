package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import ru.minat0.scdenizenbridge.objects.FrameTag;
import ru.minat0.scdenizenbridge.properties.PlayerClanProperties;
import ru.minat0.scdenizenbridge.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class SCBridge extends Bridge {

    private static final List<String> filteredMethods = Arrays.asList("getHandlers", "getHandlerList", "isCancelled", "setCancelled");

    @Override
    public void init() {
        registerEvents();

        ReflectionUtils.instantiate("ru.minat0.scdenizenbridge.commands", AbstractCommand.class,
                command -> DenizenCore.commandRegistry.register(command.getName(), command));

        ObjectFetcher.registerWithObjectFetcher(ClanPlayerTag.class, ClanPlayerTag.tagProcessor).setAsNOtherCode().setCanConvertStatic().generateBaseTag();
        ObjectFetcher.registerWithObjectFetcher(ClanTag.class, ClanTag.tagProcessor).setAsNOtherCode().setCanConvertStatic().generateBaseTag();
        ObjectFetcher.registerWithObjectFetcher(FrameTag.class, FrameTag.tagProcessor).setAsNOtherCode().setCanConvertStatic().generateBaseTag();

        PropertyParser.registerProperty(PlayerClanProperties.class, PlayerTag.class);

        TagManager.registerTagHandler(ClanTag.class, "clan", attribute ->
                (ClanTag) asType(ClanTag.class, attribute));
        TagManager.registerTagHandler(ClanPlayerTag.class, "clanplayer", attribute ->
                (ClanPlayerTag) asType(ClanPlayerTag.class, attribute));
        TagManager.registerTagHandler(FrameTag.class, "frame", attribute ->
                (FrameTag) asType(FrameTag.class, attribute));
    }

    private static void registerEvents() {
        Set<Class<? extends Event>> events = ReflectionUtils.getSubTypesOf(SCDenizenBridge.getSCPlugin().getClass(), "net.sacredlabyrinth.phaed.simpleclans.events", Event.class);

        HashMap<Class<?>, Set<Method>> scEvents = new HashMap<>();
        for (Class<? extends Event> event : events) {
            var methods = Arrays.stream(event.getDeclaredMethods()).
                    filter(method -> !filteredMethods.contains(method.getName())).
                    collect(Collectors.toSet());
            scEvents.put(event, methods);
        }

        for (Map.Entry<Class<?>, Set<Method>> entry : scEvents.entrySet()) {
            //noinspection unchecked
            ScriptEvent.registerScriptEvent(new DummyScriptEvent((Class<? extends Event>) entry.getKey(), entry.getValue()));
        }
    }

    private @Nullable <T extends ObjectTag> ObjectTag asType(@NotNull Class<T> clazz, Attribute attribute) {
        T type = null;
        if (attribute.hasParam()) {
            type = attribute.paramAsType(clazz);
            if (type == null) {
                attribute.echoError("Type invocation failure: " + attribute.getParam() + "' does not exist.");
                return null;
            }
        }

        return type;
    }
}
