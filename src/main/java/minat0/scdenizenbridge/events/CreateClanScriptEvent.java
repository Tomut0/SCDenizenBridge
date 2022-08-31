package minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.CreateClanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class CreateClanScriptEvent extends BukkitScriptEvent implements Listener {

    private CreateClanEvent event;

    public CreateClanScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(Objects.requireNonNull(event.getClan().getLeaders().get(0).toPlayer()));
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("clan")) {
            return new ClanTag(event.getClan());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onCreate(CreateClanEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan create");
    }
}
