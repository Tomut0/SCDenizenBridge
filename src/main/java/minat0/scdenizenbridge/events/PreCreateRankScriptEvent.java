package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.PreCreateRankEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PreCreateRankScriptEvent extends BukkitScriptEvent implements Listener {

    private PreCreateRankEvent event;

    public PreCreateRankScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "clan" -> new ClanTag(event.getClan());
            case "rank_name" -> new ElementTag(event.getRankName(), true);
        }

        return super.getContext(name);
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
