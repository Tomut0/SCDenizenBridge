package ru.minat0.scdenizenbridge.commands;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import ru.minat0.scdenizenbridge.ClanScriptEntryData;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import net.sacredlabyrinth.phaed.simpleclans.Clan;

import java.util.Optional;

public class BbCommand extends AbstractCommand {

    public BbCommand() {
        setName("bb");
        setSyntax("bb [add/show/clear] (player:<player>) (<clan>) [message]");
        setRequiredArguments(1, 2);
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("clan") && arg.matchesArgumentType(ClanTag.class)) {
                scriptEntry.addObject("clan", arg.asType(ClanTag.class));
            } else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.class)) {
                scriptEntry.addObject("action", arg.asElement());
            } else if (!scriptEntry.hasObject("message")
                    || arg.matchesPrefix("message")) {
                scriptEntry.addObject("message", arg.asElement());
            }
        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag actionTag = scriptEntry.getElement("action");
        ElementTag message = scriptEntry.getElement("message");
        PlayerTag playerTag = Utilities.getEntryPlayer(scriptEntry);
        Clan entryClan = Optional.ofNullable(ClanScriptEntryData.getEntryClan(scriptEntry)).
                orElse(scriptEntry.getObjectTag("clan"));

        if (entryClan == null) {
            throw new InvalidArgumentsRuntimeException("Must have linked or specified clan!");
        }

        Action action = Action.valueOf(actionTag.asString().toUpperCase());
        if ((message == null || message.asString().isEmpty()) && action.equals(Action.ADD)) {
            throw new InvalidArgumentsRuntimeException("Message must not be empty!");
        }

        switch (action) {
            case ADD:
                if (playerTag != null) {
                    entryClan.addBb(playerTag.getName(), message.asString());
                } else {
                    entryClan.addBb(null, message.asString());
                }

                break;
            case SHOW:
                if (playerTag == null) {
                    throw new InvalidArgumentsRuntimeException("Must have linked or specified player!");
                }

                entryClan.displayBb(playerTag.getPlayerEntity());
                break;
            case CLEAR:
                entryClan.clearBb();
                break;
            default:
                throw new InvalidArgumentsRuntimeException("Must specify a valid action!");
        }
    }

    private enum Action {ADD, SHOW, CLEAR}
}
