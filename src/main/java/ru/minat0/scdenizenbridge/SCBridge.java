package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import ru.minat0.scdenizenbridge.utils.ReflectionUtils;

public class SCBridge extends Bridge {

    @Override
    public void init() {
        ReflectionUtils.instantiate("ru.minat0.scdenizenbridge.events", BukkitScriptEvent.class, ScriptEvent::registerScriptEvent);
        ReflectionUtils.instantiate("ru.minat0.scdenizenbridge.commands", AbstractCommand.class,
                command -> DenizenCore.commandRegistry.register(command.getName(), command));
        ObjectFetcher.registerWithObjectFetcher(ClanTag.class, ClanTag.tagProcessor).setAsNOtherCode().generateBaseTag();
        ObjectFetcher.registerWithObjectFetcher(ClanPlayerTag.class, ClanPlayerTag.tagProcessor).setAsNOtherCode().generateBaseTag();

        TagManager.registerTagHandler(ClanTag.class, "clan", attribute -> {
            ClanTag clanTag = null;

            if (attribute.hasParam()) {
                clanTag = attribute.paramAsType(ClanTag.class);
                if (clanTag == null) {
                    attribute.echoError("Clan '" + attribute.getParam() + "' does not exist.");
                    return null;
                }
            }

            return clanTag;
        });
        TagManager.registerTagHandler(ClanPlayerTag.class, "clanplayer", attribute -> {
            ClanPlayerTag clanPlayerTag = null;

            if (attribute.hasParam()) {
                clanPlayerTag = attribute.paramAsType(ClanPlayerTag.class);
                if (clanPlayerTag == null) {
                    attribute.echoError("Clanplayer '" + attribute.getParam() + "' does not exist.");
                    return null;
                }
            }

            return clanPlayerTag;
        });
    }
}
