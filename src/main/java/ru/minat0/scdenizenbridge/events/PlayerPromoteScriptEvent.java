package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerPromoteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPromoteScriptEvent extends BukkitScriptEvent implements Listener {
    private PlayerPromoteEvent event;

    public PlayerPromoteScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(event.getClanPlayer().toPlayer(), event.getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "clan" -> new ClanTag(event.getClan());
            case "clanplayer" -> new ClanPlayerTag(event.getClanPlayer());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPromote(PlayerPromoteEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan player promote");
    }
}
