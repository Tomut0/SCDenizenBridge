package ru.minat0.scdenizenbridge.commands;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import ru.minat0.scdenizenbridge.utils.ScriptUtils;
import ru.minat0.scdenizenbridge.objects.ClanTag;

public class DisbandCommand extends AbstractCommand {

    public DisbandCommand() {
        setName("disband");
        setSyntax("disband (clan:<clan>) (announce:{true}/false}) (force:true/{false})");
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("clan")
                    && arg.matchesArgumentType(ClanTag.class)
                    && arg.matchesPrefix("clan")) {
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
        ElementTag announce = scriptEntry.getElement("announce");
        ElementTag force = scriptEntry.getElement("force");
        ScriptUtils.CheckResult checkResult = ScriptUtils.defaultCheck(scriptEntry, getName(), announce, force);
        checkResult.clan().disband(checkResult.player(), announce.asBoolean(), force.asBoolean());
    }
}
