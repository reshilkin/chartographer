package run;

import com.sun.net.httpserver.HttpServer;
import data.DiskManager;
import exceptions.DBException;
import httpserver.ChartasHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Run {
    private final static int PORT = 8080;
    private final static String NAME = "images";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Format for launch: 'java -jar chartographer-1.0.0.jar pathToStoredImages'");
            return;
        }
        DiskManager diskManager;
        try {
            diskManager = new DiskManager(args[0], NAME);
            System.out.println("Storage path set to " + args[0]);
        } catch (DBException | IOException e) {
            System.out.println("Error while initializing storage: " + e.getMessage());
            return;
        }
        try {
            createServer(diskManager);
        } catch (IOException e) {
            System.out.println("Error while initializing Server: " + e.getMessage());
        }

    }

    private static void createServer(DiskManager diskManager) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/chartas", new ChartasHandler(diskManager));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server's running on the port " + PORT);
    }
}
