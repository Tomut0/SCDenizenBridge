package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import minat0.scdenizenbridge.ClanScriptEntryData;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.TagChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TagChangeScriptEvent extends BukkitScriptEvent implements Listener {

    private TagChangeEvent event;

    public TagChangeScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(event.getPlayer(), event.getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "tag" -> new ElementTag(event.getNewTag(), true);
            case "clan" -> new ClanTag(event.getClan());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onTagChange(TagChangeEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan tag change");
    }
}
