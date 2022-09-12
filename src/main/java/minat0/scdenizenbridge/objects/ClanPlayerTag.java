package minat0.scdenizenbridge.objects;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.TimeTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import minat0.scdenizenbridge.SCDenizenBridge;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

public class ClanPlayerTag implements ObjectTag {

    public static ObjectTagProcessor<ClanPlayerTag> tagProcessor = new ObjectTagProcessor<>();
    private final ClanPlayer cp;
    private String prefix = "cp";

    public ClanPlayerTag(ClanPlayer cp) {
        this.cp = cp;
    }

    public static ClanPlayerTag valueOf(@NotNull String string) {
        return valueOf(string, null);
    }

    public static boolean matches(String arg) {
        return valueOf(arg) != null;
    }

    @Fetchable("clan")
    public static ClanPlayerTag valueOf(@NotNull String str, TagContext context) {
        str = str.replace("cp@", "");
        ClanPlayer cp = SCDenizenBridge.getSCPlugin().getClanManager().getClanPlayer(str);
        if (cp == null) {
            try {
                cp = SCDenizenBridge.getSCPlugin().getClanManager().getClanPlayer(UUID.fromString(str));
            } catch (IllegalArgumentException ignored) {}
        }

        return cp != null ? new ClanPlayerTag(cp) : null;
    }

    public static void registerTags() {

        tagProcessor.registerTag(ElementTag.class, "civilian_kills", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getCivilianKills())
        );

        tagProcessor.registerTag(ElementTag.class, "rival_kills", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getRivalKills())
        );

        tagProcessor.registerTag(ElementTag.class, "neutral_kills", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getNeutralKills())
        );

        tagProcessor.registerTag(ElementTag.class, "rank_id", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getRankId())
        );

        tagProcessor.registerTag(ElementTag.class, "rank_display_name", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getRankDisplayName())
        );

        tagProcessor.registerTag(ElementTag.class, "ally_kills", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getAllyKills())
        );

        tagProcessor.registerTag(ElementTag.class, "muted", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isMuted())
        );

        tagProcessor.registerTag(ElementTag.class, "muted_ally", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isMutedAlly())
        );

        tagProcessor.registerTag(ElementTag.class, "trusted", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isTrusted())
        );

        tagProcessor.registerTag(ElementTag.class, "leader", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isLeader())
        );

        tagProcessor.registerTag(ElementTag.class, "friendly_fire", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isFriendlyFire())
        );

        tagProcessor.registerTag(ElementTag.class, "bb_enabled", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isBbEnabled())
        );

        tagProcessor.registerTag(ElementTag.class, "invite_enabled", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().isInviteEnabled())
        );

        tagProcessor.registerTag(ListTag.class, "past_clans", (attribute, clanPlayerTag) ->
                new ListTag(clanPlayerTag.getClanPlayer().getPastClans())
        );

        tagProcessor.registerTag(ElementTag.class, "flags", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getFlags())
        );

        tagProcessor.registerTag(ElementTag.class, "channel", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getChannel().toString(), true)
        );

        tagProcessor.registerTag(TimeTag.class, "last_seen", (attribute, clanPlayerTag) ->
                new TimeTag(clanPlayerTag.getClanPlayer().getLastSeen())
        );

        tagProcessor.registerTag(ElementTag.class, "inactive_days", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getInactiveDays())
        );

        tagProcessor.registerTag(TimeTag.class, "join_date", (attribute, clanPlayerTag) ->
                new TimeTag(clanPlayerTag.getClanPlayer().getJoinDate())
        );

        tagProcessor.registerTag(ElementTag.class, "deaths", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getDeaths())
        );

        tagProcessor.registerTag(ElementTag.class, "kdr", (attribute, clanPlayerTag) ->
                new ElementTag(clanPlayerTag.getClanPlayer().getKDR())
        );

        tagProcessor.registerTag(ClanTag.class, "clan", (attribute, clanPlayerTag) -> {
            Clan clan = clanPlayerTag.getClanPlayer().getClan();
            return clan != null ? new ClanTag(clan) : null;
        });

        tagProcessor.registerTag(ElementTag.class, "locale", (attribute, clanPlayerTag) -> {
            Locale locale = clanPlayerTag.getClanPlayer().getLocale();
            return locale != null ? new ElementTag(locale.toString(), true) : null;
        });

        tagProcessor.registerTag(PlayerTag.class, "to_player", (attribute, clanPlayerTag) -> {
            Player player = clanPlayerTag.getClanPlayer().toPlayer();
            return player != null ? new PlayerTag(player) : null;
        });
    }

    public ClanPlayer getClanPlayer() {
        return cp;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "cp@";
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public ObjectTag setPrefix(String s) {
        this.prefix = s;
        return this;
    }

    @Override
    public String toString() {
        return identify();
    }
}
