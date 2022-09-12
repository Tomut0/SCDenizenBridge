package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.PreCreateRankEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PreCreateRankScriptEvent extends BukkitScriptEvent implements Listener {

    private PreCreateRankEvent event;

    public PreCreateRankScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(event.getPlayer(), event.getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "clan" -> new ClanTag(event.getClan());
            case "rank_name" -> new ElementTag(event.getRankName(), true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPreCreate(PreCreateRankEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("pre rank create");
    }
}
