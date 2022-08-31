package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import minat0.scdenizenbridge.objects.ClanPlayerTag;
import net.sacredlabyrinth.phaed.simpleclans.events.AddKillEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerKillScriptEvent extends BukkitScriptEvent implements Listener {

    private AddKillEvent event;

    public PlayerKillScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "attacker" -> new ClanPlayerTag(event.getAttacker());
            case "victim" -> new ClanPlayerTag(event.getVictim());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onKill(AddKillEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clanplayer kill");
    }
}
