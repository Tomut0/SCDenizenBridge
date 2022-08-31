package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.DisbandClanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisbandClanScriptEvent extends BukkitScriptEvent implements Listener {

    private DisbandClanEvent event;

    public DisbandClanScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("clan")) {
            return new ClanTag(event.getClan());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onDisband(DisbandClanEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan disband");
    }
}
