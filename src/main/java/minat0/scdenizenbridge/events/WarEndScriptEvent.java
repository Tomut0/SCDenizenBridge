package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.WarEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarEndScriptEvent extends BukkitScriptEvent implements Listener {
    private WarEndEvent event;

    public WarEndScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "first_clan" -> new ClanTag(event.getWar().getClans().get(0));
            case "second_clan" -> new ClanTag(event.getWar().getClans().get(1));
            case "reason" -> new ElementTag(event.getReason().toString());
        }

        return super.getContext(name);
    }


    @EventHandler
    public void onWarStart(WarEndEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan war end");
    }
}
