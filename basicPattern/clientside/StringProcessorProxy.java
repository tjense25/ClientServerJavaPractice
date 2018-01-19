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
        String result = ClientCommunicator.getInstance().toLowerCase(str);
        return result;
    }

    @Override
    public String trim(String str) {
        String result = ClientCommunicator.getInstance().trim(str);
        return result;
    }

    @Override
    public Integer parseInteger(String str) {
        Integer result = ClientCommunicator.getInstance().parseInteger(str);
        if(result == null) throw new NumberFormatException();
        return result;
    }
}
