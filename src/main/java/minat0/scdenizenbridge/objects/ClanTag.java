package minat0.scdenizenbridge.objects;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import minat0.scdenizenbridge.SCDenizenBridge;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

public class ClanTag implements ObjectTag {
    public static ObjectTagProcessor<ClanTag> tagProcessor = new ObjectTagProcessor<>();
    private final Clan clan;
    private String prefix = "clan";

    public ClanTag(@NotNull Clan clan) {
        this.clan = clan;
    }

    public static ClanTag valueOf(@NotNull String string) {
        return valueOf(string, null);
    }

    // Called in Denizen internally
    @SuppressWarnings("unused")
    public static boolean matches(String arg) {
        return valueOf(arg) != null;
    }

    @Fetchable("clan")
    public static ClanTag valueOf(@NotNull String tag, TagContext context) {
        tag = tag.replace("clan@", "");
        Clan clan = SCDenizenBridge.getSCPlugin().getClanManager().getClan(tag);
        if (clan != null) {
            return new ClanTag(clan);
        }

        return null;
    }

    // Todo: members
    // Called in Denizen internally
    @SuppressWarnings("unused")
    public static void registerTags() {

        tagProcessor.registerTag(ElementTag.class, "tag", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getTag(), true)
        );

        tagProcessor.registerTag(ElementTag.class, "color_tag", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getColorTag())
        );

        tagProcessor.registerTag(ElementTag.class, "name", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getName(), true)
        );

        tagProcessor.registerTag(ElementTag.class, "description", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getDescription(), true)
        );

        tagProcessor.registerTag(ElementTag.class, "balance", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getBalance())
        );

        tagProcessor.registerTag(ElementTag.class, "founded", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getFounded())
        );

        tagProcessor.registerTag(ElementTag.class, "friendly_fire", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().isFriendlyFire())
        );

        tagProcessor.registerTag(ElementTag.class, "members_fee", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getMemberFee())
        );

        tagProcessor.registerTag(ElementTag.class, "inactive_days", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getInactiveDays())
        );

        tagProcessor.registerTag(LocationTag.class, "home_location", (attribute, clanTag) ->
                clanTag.getClan().getHomeLocation() != null ? new LocationTag(clanTag.getClan().getHomeLocation()) : null
        );

        tagProcessor.registerTag(ElementTag.class, "size", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getSize())
        );

        tagProcessor.registerTag(ListTag.class, "allies", (attribute, clanTag) ->
                new ListTag((Collection<? extends ObjectTag>) clanTag.getClan().getAllies().stream().
                        map(ClanTag::valueOf).
                        collect(Collectors.toSet())
                ));

        tagProcessor.registerTag(ElementTag.class, "total_kdr", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().getTotalKDR())
        );

        tagProcessor.registerTag(ElementTag.class, "can_deposit", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().isAllowDeposit())
        );

        tagProcessor.registerTag(ElementTag.class, "can_withdraw", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().isAllowWithdraw())
        );

        tagProcessor.registerTag(ElementTag.class, "is_verified", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().isVerified())
        );

        tagProcessor.registerTag(ElementTag.class, "is_permanent", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().isPermanent())
        );

        tagProcessor.registerTag(ElementTag.class, "is_member", (attribute, clanTag) ->
                attribute.hasParam() ? new ElementTag(clanTag.getClan().isMember(attribute.getParam())) : null
        );

        tagProcessor.registerTag(ElementTag.class, "is_leader", (attribute, clanTag) ->
                attribute.hasParam() ? new ElementTag(clanTag.getClan().isLeader(attribute.getParam())) : null
        );

        tagProcessor.registerTag(ElementTag.class, "is_rival", (attribute, clanTag) ->
                attribute.hasParam() ? new ElementTag(clanTag.getClan().isRival(attribute.getParam())) : null
        );

        tagProcessor.registerTag(ElementTag.class, "is_ally", (attribute, clanTag) ->
                attribute.hasParam() ? new ElementTag(clanTag.getClan().isAlly(attribute.getParam())) : null
        );

        tagProcessor.registerTag(ElementTag.class, "is_warring", (attribute, clanTag) ->
                attribute.hasParam() ? new ElementTag(clanTag.getClan().isWarring(attribute.getParam())) : null
        );

        tagProcessor.registerTag(ElementTag.class, "is_unrivable", (attribute, clanTag) ->
                new ElementTag(clanTag.getClan().isUnrivable())
        );
    }

    public Clan getClan() {
        return clan;
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
        return "clan@" + clan.getTag();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public String toString() {
        return identify();
    }
}
