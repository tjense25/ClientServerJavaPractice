package clientside;

import shared.IStringProcessor;
import shared.commands.ParseIntegerCommand;
import shared.commands.ToLowerCaseCommand;
import shared.commands.TrimCommand;

/**
 * Created by tjense25 on 1/16/18.
 */

public class StringProcessorProxy implements IStringProcessor {

    private static StringProcessorProxy SINGLETON;
    public static StringProcessorProxy getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new StringProcessorProxy();
        }
        return SINGLETON;
    }

    private StringProcessorProxy() {}

    @Override
    public String toLowerCase(String str) {
        ToLowerCaseCommand command = new ToLowerCaseCommand(str);
        return (String) ClientCommunicator.getInstance().send("ToLowerCaseCommand", command);

    }

    @Override
    public String trim(String str) {
        TrimCommand command = new TrimCommand(str);
        return (String) ClientCommunicator.getInstance().send("TrimCommand", command);
    }

    @Override
    public Integer parseInteger(String str) {
        ParseIntegerCommand command = new ParseIntegerCommand(str);
        Integer result = (Integer) ClientCommunicator.getInstance().send("ParseIntegerCommand", command);
        if(result == null) throw new NumberFormatException();
        return result;
    }
}
