package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.events.ClanBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BalanceUpdateScriptEvent extends BukkitScriptEvent implements Listener {

    private ClanBalanceUpdateEvent event;

    public BalanceUpdateScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(Bukkit.getPlayer(event.getUpdater().getName()), event.getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "clan" -> new ClanTag(event.getClan());
            case "clan_balance" -> new ElementTag(event.getBalance());
            case "clan_new_balance" -> new ElementTag(event.getNewBalance());
            case "updater_name" -> new ElementTag(event.getUpdater().getName());
            case "updater_balance" -> new ElementTag(event.getUpdater().getBalance());
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onCreate(ClanBalanceUpdateEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clan balance update");
    }
}
