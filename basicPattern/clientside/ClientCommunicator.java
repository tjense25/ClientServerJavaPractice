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

    public String toLowerCase(String str) {
        HttpURLConnection connection = openConnection("/toLowerCase");
        send(connection, str);
        String result = (String) getResult(connection, String.class);
        return result;
    }

    public String trim(String str) {
        HttpURLConnection connection = openConnection("/trim");
        send(connection, str);
        String result = (String) getResult(connection, String.class);
        return result;
    }

    public Integer parseInteger(String str) {
        HttpURLConnection connection = openConnection("/parseInteger");
        send(connection, str);
        Integer result = (Integer) getResult(connection, Integer.class);
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

    private void send(HttpURLConnection connection, Object objectToSend) {
        try {
            String json = gson.toJson(objectToSend);
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

    private Object getResult(HttpURLConnection connection, Class<?> klass) {
        Object result = null;
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                result = gson.fromJson(new InputStreamReader(respBody), klass);
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
