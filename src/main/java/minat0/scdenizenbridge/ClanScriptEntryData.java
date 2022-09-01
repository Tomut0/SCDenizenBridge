package minat0.scdenizenbridge;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClanScriptEntryData extends BukkitScriptEntryData {

    @Nullable
    private final Clan clan;

    public ClanScriptEntryData(@NotNull Entity entity, @Nullable Clan clan) {
        super(entity);
        this.clan = clan;
    }

    public static ClanScriptEntryData getEntryData(ScriptEntry entry) {
        return ((ClanScriptEntryData) entry.entryData);
    }

    @Nullable
    public static Clan getEntryClan(ScriptEntry entry) {
        return getEntryData(entry).getClan();
    }

    @Nullable
    public Clan getClan() {
        return clan;
    }
}
