package ru.minat0.scdenizenbridge.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.sacredlabyrinth.phaed.simpleclans.events.FrameOpenEvent;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.minat0.scdenizenbridge.SCDenizenBridge;
import ru.minat0.scdenizenbridge.objects.FrameTag;
import ru.minat0.scdenizenbridge.utils.ReflectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class FrameOpenScriptEvent extends BukkitScriptEvent implements Listener {

    private static final Set<String> frames = ReflectionUtils.getSubTypesOf(SCDenizenBridge.getSCPlugin().getClass(), "net.sacredlabyrinth.phaed.simpleclans.ui.frames", SCFrame.class).
            stream().
            map(Class::getSimpleName).
            map(s -> s.replace("Frame", "")).
            map(String::toLowerCase).
            collect(Collectors.toSet());

    private FrameOpenEvent event;

    public FrameOpenScriptEvent() {
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("frame")) {
            return new FrameTag(event.getFrame());
        }

        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @EventHandler
    public void onFrameOpen(FrameOpenEvent event) {
        this.event = event;
        fire(event);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("frame") && (frames.contains(path.eventArgLowerAt(1)) || path.eventLower.endsWith("open"));
    }

    @Override
    public boolean matches(ScriptPath path) {
        String frame = event.getFrame().getClass().getSimpleName().replace("Frame", "");
        if (!frame.equalsIgnoreCase(path.eventArgLowerAt(1)) && !path.eventArgLowerAt(1).equals("open")) {
            return false;
        }

        return super.matches(path);
    }
}
