package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import shared.CommandTransferObject;
import shared.commands.ICommand;
import shared.commands.ResultTransferObject;

/**
 * Created by tjense25 on 1/16/18.
 */

public class ServerCommunicator {

    private static ServerCommunicator SINGLETON;
    public static ServerCommunicator getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new ServerCommunicator();
        }
        return SINGLETON;
    }

    private static final int SERVER_PORT_NUMBER = 8080;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    private static Gson gson = new Gson();

    private HttpServer server;

    private ServerCommunicator() {}

    private void run() {
        try {
            server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
        }
        catch(IOException e) {
            System.out.println("Could not create HTTP Server: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating Contexts . . . ");
        server.createContext(COMMAND_DESIGNATOR, commandHandler);

        System.out.println("Starting server . . . ");
        server.start();

        System.out.println("Server started and waiting for connections!");
    }

    private HttpHandler commandHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                InputStream reqBody = exchange.getRequestBody();
                CommandTransferObject commandTransferObject =
                        gson.fromJson(new InputStreamReader(reqBody), CommandTransferObject.class);
                ICommand command = commandTransferObject.getCommand();

                Object result = command.execute();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                ResultTransferObject resultTransferObject = null;
                if(result != null && result.equals("NumberFormatException")) {
                    resultTransferObject = new ResultTransferObject("java.lang.Integer", null);
                } else {
                    resultTransferObject = new ResultTransferObject(result.getClass().getName(), result);
                }
                String json = gson.toJson(resultTransferObject);
                writeString(json, respBody);
                respBody.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public static int getServerPortNumber() {
        return SERVER_PORT_NUMBER;
    }

    public static void main(String[] args) {
        new ServerCommunicator().run();
    }

    public static final String COMMAND_DESIGNATOR = "/command";
}
