package clientside;

/**
 * Created by tjense25 on 1/16/18.
 */

public class Main {

    public static void main(String[] args) {
        //System.out.println(StringProcessorProxy.getInstance().toLowerCase("HELLO WORLD!!"));
        //System.out.println(StringProcessorProxy.getInstance().trim("  hello world!!!    "));
        System.out.println(StringProcessorProxy.getInstance().parseInteger("-12"));
    }
}
