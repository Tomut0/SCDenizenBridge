package ru.minat0.scdenizenbridge.utils;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.SCDenizenBridge;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class ScriptUtils {
    public static final Set<String> frames = ReflectionUtils.getSubTypesOf(SCDenizenBridge.getSCPlugin().getClass(), "net.sacredlabyrinth.phaed.simpleclans.ui.frames", SCFrame.class).
            stream().
            map(Class::getSimpleName).
            map(s -> s.replace("Frame", "")).
            map(String::toLowerCase).
            collect(Collectors.toSet());

    private ScriptUtils() {
    }

    public static SCHolder defaultCheck(@NotNull ScriptEntry entry, @NotNull String commandName, Object... debugValue) {
        Clan entryClan = Optional.ofNullable(ClanScriptEntryData.getEntryClan(entry)).orElse(entry.getObjectTag("clan"));
        if (entryClan == null) {
            throw new InvalidArgumentsRuntimeException("Must have linked or specified clan!");
        }

        if (!Utilities.entryHasPlayer(entry)) {
            throw new InvalidArgumentsRuntimeException("Must have linked or specified player!");
        }

        PlayerTag playerTag = Utilities.getEntryPlayer(entry);
        Player player = playerTag.getPlayerEntity();

        if (entry.dbCallShouldDebug()) {
            Debug.report(entry, commandName, entryClan, player, debugValue);
        }

        return new SCHolder(player, entryClan);
    }

    public record SCHolder(@Nullable Player player, @Nullable Clan clan) {
    }
}
