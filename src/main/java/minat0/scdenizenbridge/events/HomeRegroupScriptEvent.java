package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import minat0.scdenizenbridge.objects.ClanPlayerTag;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.HomeRegroupEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HomeRegroupScriptEvent extends BukkitScriptEvent implements Listener {

    private HomeRegroupEvent event;

    public HomeRegroupScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getIssuer().toPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "clan" -> new ClanTag(event.getClan());
            case "issuer" -> new ClanPlayerTag(event.getIssuer());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onHomeSet(HomeRegroupEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clanplayer home regroup");
    }
}
