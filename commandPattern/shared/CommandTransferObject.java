package shared;

import com.google.gson.Gson;

import shared.commands.ICommand;

/**
 * Created by tjense25 on 1/18/18.
 */

public class CommandTransferObject {

    private static final Gson gson = new Gson();

    //Name of the command to be called
    private String commandName;
    //Json representation of the command class to be called
    private String command;

    public CommandTransferObject(String commandName, ICommand command) {
        this.commandName = commandName;
        this.command = gson.toJson(command);
    }

    public ICommand getCommand() {
        Class<?> klass = null;
        try {
            klass = Class.forName(COMMAND_DIRECTORY + commandName);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: could not find the class " + commandName);
            e.printStackTrace();
        }

        ICommand result = (ICommand) gson.fromJson(command, klass);

        return result;
    }

    private static final String COMMAND_DIRECTORY = "shared.commands.";

}
