package clientside;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import shared.CommandTransferObject;
import shared.commands.ICommand;
import shared.commands.ResultTransferObject;

/**
 * Created by tjense25 on 1/16/18.
 */

public class ClientCommunicator {

    private static ClientCommunicator SINGLETON;
    public static ClientCommunicator getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new ClientCommunicator();
        }
        return SINGLETON;
    }

    private static Gson gson = new Gson();

    private ClientCommunicator() {}

    public Object send(String commandName, ICommand command) {
        HttpURLConnection connection = openConnection("/command");
        sendToServer(connection, commandName, command);
        Object result = getResult(connection);

        return result;
    }

    private HttpURLConnection openConnection(String contextDesignator) {
        HttpURLConnection result = null;
        try {
            URL url = new URL(URL_PREFIX + contextDesignator);
            result = (HttpURLConnection) url.openConnection();
            result.setRequestMethod("POST");
            result.setDoOutput(true);
            result.connect();
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL Error");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void sendToServer(HttpURLConnection connection, String commandName, ICommand command) {
        try {
            CommandTransferObject commandTransferObject = new CommandTransferObject(commandName, command);
            String json = gson.toJson(commandTransferObject);
            OutputStream reqBody = connection.getOutputStream();
            writeString(json, reqBody);
            reqBody.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private Object getResult(HttpURLConnection connection) {
        Object result = null;
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                ResultTransferObject transferObject = (ResultTransferObject)
                        gson.fromJson(new InputStreamReader(respBody), ResultTransferObject.class);
                result = transferObject.getResult();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
}
