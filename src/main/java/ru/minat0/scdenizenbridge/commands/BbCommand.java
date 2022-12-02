package ru.minat0.scdenizenbridge.commands;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import ru.minat0.scdenizenbridge.objects.ClanTag;
import ru.minat0.scdenizenbridge.utils.ScriptUtils;

public class BbCommand extends AbstractCommand {

    public BbCommand() {
        setName("bb");
        setSyntax("bb [add/show/clear] (player:<player>) (clan:<clan>) [message]");
        setRequiredArguments(1, 2);
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("clan")
                    && arg.matchesArgumentType(ClanTag.class)
                    && arg.matchesPrefix("clan")) {
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
        ScriptUtils.SCHolder SCHolder = ScriptUtils.defaultCheck(scriptEntry, getName(), actionTag.asString());

        Action action = Action.valueOf(actionTag.asString().toUpperCase());
        if ((message == null || message.asString().isEmpty()) && action.equals(Action.ADD)) {
            throw new InvalidArgumentsRuntimeException("Message must not be empty!");
        }

        switch (action) {
            case ADD -> SCHolder.clan().addBb(SCHolder.player().getName(), message.asString());
            case SHOW -> SCHolder.clan().displayBb(SCHolder.player());
            case CLEAR -> SCHolder.clan().clearBb();
            default -> throw new InvalidArgumentsRuntimeException("Must specify a valid action!");
        }
    }

    private enum Action {ADD, SHOW, CLEAR}
}
