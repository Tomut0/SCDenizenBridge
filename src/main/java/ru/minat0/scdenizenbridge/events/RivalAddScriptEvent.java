package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.RivalClanAddEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RivalAddScriptEvent extends BukkitScriptEvent implements Listener {
    private RivalClanAddEvent event;

    public RivalAddScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "first_clan" -> new ClanTag(event.getClanFirst());
            case "second_clan" -> new ClanTag(event.getClanSecond());
            default -> super.getContext(name);
        };
    }


    @EventHandler
    public void onRivalAdd(RivalClanAddEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan rival add");
    }
}
