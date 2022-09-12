package ru.minat0.scdenizenbridge.commands;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import ru.minat0.scdenizenbridge.ScriptUtils;
import ru.minat0.scdenizenbridge.objects.ClanTag;

public class PromoteCommand extends AbstractCommand {

    public PromoteCommand() {
        setName("promote");
        setSyntax("promote (<clan>) (player:<player>)");
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
        checkResult.clan().promote(checkResult.player().getUniqueId());
    }
}
