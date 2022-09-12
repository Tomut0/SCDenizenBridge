package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.events.BukkitScriptEvent;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import ru.minat0.scdenizenbridge.properties.PlayerClanProperties;
import ru.minat0.scdenizenbridge.utils.ReflectionUtils;

public class SCBridge extends Bridge {

    @Override
    public void init() {
        ReflectionUtils.instantiate("ru.minat0.scdenizenbridge.events", BukkitScriptEvent.class, ScriptEvent::registerScriptEvent);
        ReflectionUtils.instantiate("ru.minat0.scdenizenbridge.commands", AbstractCommand.class,
                command -> DenizenCore.commandRegistry.register(command.getName(), command));

        ObjectFetcher.registerWithObjectFetcher(ClanPlayerTag.class, ClanPlayerTag.tagProcessor).setAsNOtherCode().generateBaseTag();
        ObjectFetcher.registerWithObjectFetcher(ClanTag.class, ClanTag.tagProcessor).setAsNOtherCode().generateBaseTag();

        PropertyParser.registerProperty(PlayerClanProperties.class, PlayerTag.class);

        TagManager.registerTagHandler(ClanTag.class, "clan", attribute ->
                (ClanTag) asType(ClanTag.class, attribute));
        TagManager.registerTagHandler(ClanPlayerTag.class, "clanplayer", attribute ->
                (ClanPlayerTag) asType(ClanPlayerTag.class, attribute));
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
