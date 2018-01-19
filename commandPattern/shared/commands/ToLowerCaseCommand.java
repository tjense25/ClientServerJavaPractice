package shared.commands;
import server.StringProcessor;

/**
 * Created by tjense25 on 1/18/18.
 */

public class ToLowerCaseCommand implements ICommand {

    private String str;

    public ToLowerCaseCommand(String str) {
        this.str = str;
    }
    @Override
    public Object execute() {
        return StringProcessor.getInstance().toLowerCase(str);
    }
}
