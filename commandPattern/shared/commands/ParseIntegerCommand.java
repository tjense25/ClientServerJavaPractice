package shared.commands;

import server.StringProcessor;

/**
 * Created by tjense25 on 1/18/18.
 */

public class ParseIntegerCommand implements ICommand {

    private String str;

    public ParseIntegerCommand(String str) {
        this.str = str;
    }


    @Override
    public Object execute() {
        Object result = null;
        try {
            result = StringProcessor.getInstance().parseInteger(str);
        } catch(NumberFormatException e) {
            result = "NumberFormatException";
        }
        return result;
    }
}
