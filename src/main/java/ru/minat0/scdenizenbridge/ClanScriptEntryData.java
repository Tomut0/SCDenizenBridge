package ru.minat0.scdenizenbridge;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ClanScriptEntryData extends BukkitScriptEntryData {

    @Nullable
    private final Clan clan;

    public ClanScriptEntryData(@Nullable Entity entity, @Nullable Clan clan) {
        super(entity);
        this.clan = clan;
    }

    public static ClanScriptEntryData getEntryData(ScriptEntry entry) {
        if (entry.entryData instanceof ClanScriptEntryData) {
            return ((ClanScriptEntryData) entry.entryData);
        }

        PlayerTag player = ((BukkitScriptEntryData) entry.entryData).getPlayer();
        if (player != null) {
            Clan clan = SCDenizenBridge.getSCPlugin().getClanManager().getClanByPlayerUniqueId(player.getPlayerEntity().getUniqueId());
            return new ClanScriptEntryData(player.getPlayerEntity(), clan);
        }

        return null;
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
