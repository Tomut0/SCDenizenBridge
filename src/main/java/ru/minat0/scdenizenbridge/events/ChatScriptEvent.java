package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.objects.ClanPlayerTag;
import net.sacredlabyrinth.phaed.simpleclans.events.ChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatScriptEvent extends BukkitScriptEvent implements Listener {
    private ChatEvent event;

    public ChatScriptEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new ClanScriptEntryData(Objects.requireNonNull(event.getSender().toPlayer()), event.getSender().getClan());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "sender" -> new ClanPlayerTag(event.getSender());
            case "message" -> new ElementTag(event.getMessage());
            case "receivers" -> new ListTag((Collection<? extends ObjectTag>)
                            event.getReceivers().stream().map(ClanPlayerTag::new).collect(Collectors.toSet()));
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("clanplayer chat");
    }
}
