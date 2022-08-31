package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.WarStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarStartScriptEvent extends BukkitScriptEvent implements Listener {
    private WarStartEvent event;

    public WarStartScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "first_clan" -> new ClanTag(event.getWar().getClans().get(0));
            case "second_clan" -> new ClanTag(event.getWar().getClans().get(1));
        }

        return super.getContext(name);
    }


    @EventHandler
    public void onWarStart(WarStartEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan war start");
    }
}
