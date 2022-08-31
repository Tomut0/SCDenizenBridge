package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.RivalClanRemoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RivalRemoveScriptEvent extends BukkitScriptEvent implements Listener {
    private RivalClanRemoveEvent event;

    public RivalRemoveScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "first_clan" -> new ClanTag(event.getClanFirst());
            case "second_clan" -> new ClanTag(event.getClanSecond());
        }

        return super.getContext(name);
    }


    @EventHandler
    public void onRivalRemove(RivalClanRemoveEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan rival remove");
    }
}
