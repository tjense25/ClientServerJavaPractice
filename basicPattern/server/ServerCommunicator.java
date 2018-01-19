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
        server.createContext(PARSE_INTEGER_DESIGNATOR, parseIntegerHandler);
        server.createContext(TRIM_DESIGNATOR, trimHandler);
        server.createContext(TO_LOWER_CASE_DESIGNATOR, toLowerCaseHandler);

        System.out.println("Starting server . . . ");
        server.start();

        System.out.println("Server started and waiting for connections!");
    }

    private HttpHandler parseIntegerHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                InputStream reqBody = exchange.getRequestBody();
                String str = gson.fromJson(new InputStreamReader(reqBody), String.class);
                Integer result = 0;
                try {
                    result = StringProcessor.getInstance().parseInteger(str);

                } catch (NumberFormatException e) {
                    result = null;
                }
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                String json = gson.toJson(result);
                writeString(json, respBody);
                respBody.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private HttpHandler trimHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                InputStream reqBody = exchange.getRequestBody();
                String str = gson.fromJson(new InputStreamReader(reqBody), String.class);
                String result = StringProcessor.getInstance().trim(str);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                String json = gson.toJson(result);
                writeString(json, respBody);
                respBody.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private HttpHandler toLowerCaseHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("In to Lower Case Handler!");
            try {
                InputStream reqBody = exchange.getRequestBody();
                String str = gson.fromJson(new InputStreamReader(reqBody), String.class);
                String result = StringProcessor.getInstance().toLowerCase(str);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                String json = gson.toJson(result);
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

    public static final String PARSE_INTEGER_DESIGNATOR = "/parseInteger";
    public static final String TRIM_DESIGNATOR = "/trim";
    public static final String TO_LOWER_CASE_DESIGNATOR = "/toLowerCase";
}
