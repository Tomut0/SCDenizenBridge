package minat0.scdenizenbridge.commands;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import minat0.scdenizenbridge.ClanScriptEntryData;
import minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DisbandCommand extends AbstractCommand {

    public DisbandCommand() {
        setName("disband");
        setSyntax("disband (<clan>) (announce:{true}/false}) (force:true/{false})");
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("clan") && arg.matchesArgumentList(ClanTag.class)) {
                scriptEntry.addObject("clan", arg.asType(ClanTag.class));
            } else if (!scriptEntry.hasObject("announce")
                    && arg.matchesPrefix("announce")
                    && arg.matchesBoolean()) {
                scriptEntry.addObject("announce", arg.asElement());
            } else if (!scriptEntry.hasObject("force")
                    && arg.matchesPrefix("force")
                    && arg.matchesBoolean()) {
                scriptEntry.addObject("force", arg.asElement());
            }
        }

        scriptEntry.defaultObject("announce", new ElementTag("true"));
        scriptEntry.defaultObject("force", new ElementTag("false"));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        Clan entryClan = Optional.ofNullable(ClanScriptEntryData.getEntryClan(scriptEntry)).
                orElse(scriptEntry.getObjectTag("clan"));
        if (entryClan == null) {
            throw new InvalidArgumentsRuntimeException("Must have linked or specified clan!");
        }

        ElementTag announce = scriptEntry.getElement("announce");
        ElementTag force = scriptEntry.getElement("force");
        if (!Utilities.entryHasPlayer(scriptEntry)) {
            throw new InvalidArgumentsRuntimeException("Must have linked player!");
        }
        PlayerTag playerTag = Utilities.getEntryPlayer(scriptEntry);
        Player player = playerTag.getPlayerEntity();

        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), getName(), entryClan, announce, force);
        }

        entryClan.disband(player, announce.asBoolean(), force.asBoolean());
    }
}
