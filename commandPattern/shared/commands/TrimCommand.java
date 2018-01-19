package shared.commands;

import server.StringProcessor;

/**
 * Created by tjense25 on 1/18/18.
 */

public class TrimCommand implements ICommand {

    private String str;

    public TrimCommand(String str) {
        this.str = str;
    }


    @Override
    public Object execute() {
        return StringProcessor.getInstance().trim(str);
    }
}
