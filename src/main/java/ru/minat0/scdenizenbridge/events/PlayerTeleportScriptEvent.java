package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import net.sacredlabyrinth.phaed.simpleclans.events.ClanPlayerTeleportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerTeleportScriptEvent extends BukkitScriptEvent implements Listener {
    private ClanPlayerTeleportEvent event;

    public PlayerTeleportScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(event.getClanPlayer().toPlayer(), event.getClanPlayer().getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "clanplayer" -> new ClanPlayerTag(event.getClanPlayer());
            case "origin" -> new LocationTag(event.getOrigin());
            case "destination" -> new LocationTag(event.getDestination());
        }

        return super.getContext(name);
    }


    @EventHandler
    public void onWarStart(ClanPlayerTeleportEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clanplayer teleport");
    }
}
