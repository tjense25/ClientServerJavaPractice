package server;

import shared.IStringProcessor;

/**
 * Created by tjense25 on 1/16/18.
 */

public class StringProcessor implements IStringProcessor {

    private static StringProcessor SINGLETON;
    public static StringProcessor getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new StringProcessor();
        }
        return SINGLETON;
    }

    private StringProcessor() {}


    @Override
    public String toLowerCase(String str) {
        return str.toLowerCase();
    }

    @Override
    public String trim(String str) {
        return str.trim();
    }

    @Override
    public Integer parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}

