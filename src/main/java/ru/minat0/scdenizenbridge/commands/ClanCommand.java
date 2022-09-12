package ru.minat0.scdenizenbridge.commands;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import ru.minat0.scdenizenbridge.utils.ScriptUtils;

import java.util.UUID;

public class ClanCommand extends AbstractCommand {

    public ClanCommand() {
        setName("clan");
        setSyntax("clan [invite/promote/demote] (player:<player>) (clan:<clan>)");
        setRequiredArguments(1, 1);
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("clan")
                    && arg.matchesArgumentType(ClanTag.class)
                    && arg.matchesPrefix("clan")) {
                scriptEntry.addObject("clan", arg.asType(ClanTag.class));
            } else if (!scriptEntry.hasObject("action") && arg.matchesEnum(ClanCommand.Action.class)) {
                scriptEntry.addObject("action", arg.asElement());
            }
        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag actionTag = scriptEntry.getElement("action");
        ClanCommand.Action action = ClanCommand.Action.valueOf(actionTag.asString().toUpperCase());
        ScriptUtils.CheckResult checkResult = ScriptUtils.defaultCheck(scriptEntry, getName(), actionTag.asString());
        UUID uniqueId = checkResult.player().getUniqueId();

        switch (action) {
            case PROMOTE -> checkResult.clan().promote(uniqueId);
            case DEMOTE -> checkResult.clan().demote(uniqueId);
            case INVITE -> checkResult.clan().addPlayerToClan(new ClanPlayer(uniqueId));
            default -> throw new InvalidArgumentsRuntimeException("Must specify a valid action!");
        }
    }

    private enum Action {INVITE, DEMOTE, PROMOTE}
}
