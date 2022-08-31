package minat0.scdenizenbridge;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import minat0.scdenizenbridge.objects.ClanTag;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class SCBridge extends Bridge {

    @Override
    public void init() {
        registerEvents();
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

    private void registerEvents() {
        Set<Class<? extends BukkitScriptEvent>> events = ReflectionUtils.getSubTypesOf("minat0.scdenizenbridge.events", BukkitScriptEvent.class);
        for (Class<? extends BukkitScriptEvent> event : events) {
            try {
                BukkitScriptEvent bukkitScriptEvent = event.getConstructor().newInstance();
                ScriptEvent.registerScriptEvent(bukkitScriptEvent);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                SCDenizenBridge.getInstance().getLogger().severe("Error registering events: " + ex.getMessage());
            }
        }
    }
}
