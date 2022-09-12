package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ScriptUtils {
    private ScriptUtils() {
    }

    public static CheckResult defaultCheck(ScriptEntry entry, String commandName, Object... debugValues) {
        Clan entryClan = Optional.ofNullable(ClanScriptEntryData.getEntryClan(entry)).
                orElse(entry.getObjectTag("clan"));
        if (entryClan == null) {
            throw new InvalidArgumentsRuntimeException("Must have linked or specified clan!");
        }

        if (!Utilities.entryHasPlayer(entry)) {
            throw new InvalidArgumentsRuntimeException("Must have linked or specified player!");
        }

        PlayerTag playerTag = Utilities.getEntryPlayer(entry);
        Player player = playerTag.getPlayerEntity();

        if (entry.dbCallShouldDebug()) {
            Debug.report(entry, commandName, commandName, entryClan, player, debugValues);
        }

        return new CheckResult(entryClan, player);
    }

    public record CheckResult(Clan clan, Player player) {
        public CheckResult(@NotNull Clan clan, @NotNull Player player) {
            this.clan = clan;
            this.player = player;
        }
    }
}
