package ru.minat0.scdenizenbridge.commands;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import ru.minat0.scdenizenbridge.ScriptUtils;
import ru.minat0.scdenizenbridge.objects.ClanTag;

public class DemoteCommand extends AbstractCommand {

    public DemoteCommand() {
        setName("demote");
        setSyntax("demote (<clan>) (player:<player>)");
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("clan") && arg.matchesArgumentType(ClanTag.class)) {
                scriptEntry.addObject("clan", arg.asType(ClanTag.class));
            }
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ScriptUtils.CheckResult checkResult = ScriptUtils.defaultCheck(scriptEntry, getName());
        checkResult.clan().demote(checkResult.player().getUniqueId());
    }
}
