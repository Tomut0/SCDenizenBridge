package minat0.scdenizenbridge;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import minat0.scdenizenbridge.events.ClanCreateScriptEvent;
import minat0.scdenizenbridge.objects.ClanTag;

public class SCBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(ClanCreateScriptEvent.class);
        ObjectFetcher.registerWithObjectFetcher(ClanTag.class, ClanTag.tagProcessor).setAsNOtherCode().generateBaseTag();

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
    }
}
