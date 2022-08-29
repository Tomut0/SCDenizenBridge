package minat0.scdenizenbridge.objects;

import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.TagContext;
import minat0.scdenizenbridge.SCDenizenBridge;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.jetbrains.annotations.NotNull;

public class ClanPlayerTag implements ObjectTag {

    public ClanPlayer getClanPlayer() {
        return cp;
    }

    public static ClanTag valueOf(@NotNull String string) {
        return valueOf(string, null);
    }

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

    private final ClanPlayer cp;
    private String prefix = "cp";

    public ClanPlayerTag(ClanPlayer cp) {
        this.cp = cp;
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
