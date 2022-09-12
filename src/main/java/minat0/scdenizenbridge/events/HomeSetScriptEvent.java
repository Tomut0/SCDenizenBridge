package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import minat0.scdenizenbridge.ClanScriptEntryData;
import minat0.scdenizenbridge.objects.ClanPlayerTag;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerHomeSetEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HomeSetScriptEvent extends BukkitScriptEvent implements Listener {

    private PlayerHomeSetEvent event;

    public HomeSetScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(event.getClanPlayer().toPlayer(), event.getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "location" -> new LocationTag(event.getLocation());
            case "clan" -> new ClanTag(event.getClan());
            case "clanplayer" -> new ClanPlayerTag(event.getClanPlayer());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onHomeSet(PlayerHomeSetEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clanplayer home set");
    }
}
