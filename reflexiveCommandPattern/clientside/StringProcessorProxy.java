package clientside;

import shared.IStringProcessor;

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
        String[] parameterTypeNames = {String.class.getName()};
        Object[] parameters = {str};
        Object result = ClientCommunicator.getInstance().send("toLowerCase", parameterTypeNames, parameters);
        return (String) result;

    }

    @Override
    public String trim(String str) {
        String[] parameterTypeNames = {String.class.getName()};
        Object[] parameters = {str};
        Object result = ClientCommunicator.getInstance().send("trim", parameterTypeNames, parameters);
        return (String) result;
    }

    @Override
    public Integer parseInteger(String str) {
        String[] parameterTypenames = {String.class.getName()};
        Object[] parameters = {str};
        Object result = ClientCommunicator.getInstance().send("parseInteger", parameterTypenames, parameters);
        if(result == null) throw new NumberFormatException();
        return (Integer) result;
    }
}
