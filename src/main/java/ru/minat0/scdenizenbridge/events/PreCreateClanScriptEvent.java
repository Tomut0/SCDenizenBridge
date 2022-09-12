package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.sacredlabyrinth.phaed.simpleclans.events.PreCreateClanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PreCreateClanScriptEvent extends BukkitScriptEvent implements Listener {

    private PreCreateClanEvent event;

    public PreCreateClanScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "tag" -> new ElementTag(event.getTag());
            case "name" -> new ElementTag(event.getName());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onPreCreate(PreCreateClanEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("pre clan create");
    }
}
