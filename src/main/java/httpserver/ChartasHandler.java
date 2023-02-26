package httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.DiskManager;
import data.Rectangle;
import data.Shape;
import exceptions.*;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ChartasHandler implements HttpHandler {
    DiskManager diskManager;

    public ChartasHandler(DiskManager diskManager) {
        this.diskManager = diskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> args = parseQuery(exchange);
        Response response;
        switch (exchange.getRequestMethod()) {
            case "GET": {
                response = processGet(exchange, args);
                break;
            }
            case "POST": {
                response = processPost(exchange, args);
                break;
            }
            case "DELETE": {
                response = processDelete(exchange);
                break;
            }
            default: {
                response = Response.NOT_IMPLEMENTED;
            }
        }
        try {
            ResponseStream is = response.getResponseStream();
            exchange.sendResponseHeaders(response.getRCode(), is.responseLength());
            OutputStream os = exchange.getResponseBody();
            byte[] buffer = new byte[4096];
            System.out.println("Response code: " + response.getRCode());
            System.out.println("Response length: " + is.responseLength());
            for (long i = 0; i < is.responseLength(); ) {
                int len = is.read(buffer);
                os.write(buffer, 0, len);
                i += len;
            }
            os.close();
            exchange.close();
        } catch (Exception e) {
            System.out.println("Error while sending the response: " + e.getMessage());
        }
    }

    private Map<String, String> parseQuery(HttpExchange exchange) {
        Map<String, String> res = new HashMap<>();
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            return res;
        }
        for (String q : query.split("&")) {
            String[] temp = q.split("=");
            if (temp.length != 2) {
                continue;
            }
            res.put(temp[0], temp[1]);
        }
        System.out.println("Query parameters:");
        for (String s : res.keySet()) {
            System.out.println('\t' + s + " : " + res.get(s));
        }
        return res;
    }

    private Response processGet(HttpExchange exchange, Map<String, String> query) {
        System.out.println("Handling Get request");
        String path = exchange.getRequestURI().getPath();
        if (path == null || path.split("/").length != 3) {
            return Response.NOT_FOUND;
        }
        int id;
        try {
            id = Integer.parseInt(path.split("/")[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error handling GET: " + e.getMessage());
            return Response.NOT_FOUND;
        }
        try {
            ResponseStream responseStream = diskManager.getImageFragment(id, new Rectangle(query));
            return new Response(responseStream, HttpURLConnection.HTTP_OK);
        } catch (RectangleException | IllegalFragmentException | ShapeSizeException e) {
            System.out.println("Error handling GET: " + e.getMessage());
            return Response.BAD_REQUEST;
        } catch (RowMissingException | FileNotFoundException e) {
            System.out.println("Error handling GET: " + e.getMessage());
            return Response.NOT_FOUND;
        } catch (DBException | DataStreamException e) {
            System.out.println("Error handling GET: " + e.getMessage());
            return Response.SERVER_ERROR;
        }
    }

    private Response processPost(HttpExchange exchange, Map<String, String> map) {
        System.out.println("Handling Post request");
        String path = exchange.getRequestURI().getPath();
        if (path == null) {
            return Response.NOT_FOUND;
        }
        if (path.split("/").length == 2) {
            try {
                int id = diskManager.createImage(new Shape(map));
                return new Response(new StringResponseStream(Integer.toString(id)), HttpURLConnection.HTTP_CREATED);
            } catch (DBException | FileNotCreatedException e) {
                System.out.println("Error handling POST new image: " + e.getMessage());
                return Response.SERVER_ERROR;
            } catch (RectangleException | ShapeSizeException e) {
                System.out.println("Error handling POST new image: " + e.getMessage());
                return Response.BAD_REQUEST;
            }
        } else if (path.split("/").length == 3) {
            System.out.println("Posting image fragment");
            int id;
            try {
                id = Integer.parseInt(path.split("/")[2]);
            } catch (NumberFormatException e) {
                System.out.println("Error handling GET: " + e.getMessage());
                return Response.NOT_FOUND;
            }
            try {
                diskManager.setImageFragment(id, new Rectangle(map), exchange.getRequestBody());
                return Response.OK;
            } catch (DBException | DataStreamException e) {
                System.out.println("Error handling POST image fragment: " + e.getMessage());
                return Response.SERVER_ERROR;
            } catch (IllegalFragmentException | RectangleException | ShapeSizeException e) {
                System.out.println("Error handling POST image fragment: " + e.getMessage());
                return Response.BAD_REQUEST;
            } catch (FileNotFoundException | RowMissingException e) {
                System.out.println("Error handling POST image fragment: " + e.getMessage());
                return Response.NOT_FOUND;
            }
        }
        return Response.NOT_FOUND;
    }

    private Response processDelete(HttpExchange exchange) {
        System.out.println("Handling Delete request");
        String path = exchange.getRequestURI().getPath();
        if (path == null || path.split("/").length != 3) {
            return Response.NOT_FOUND;
        }
        int id;
        try {
            id = Integer.parseInt(path.split("/")[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error handling GET: " + e.getMessage());
            return Response.NOT_FOUND;
        }
        try {
            diskManager.deleteImage(id);
            return Response.OK;
        } catch (DBException e) {
            System.out.println("Error handling DELETE: " + e.getMessage());
            return Response.SERVER_ERROR;
        } catch (RowMissingException | FileNotFoundException e) {
            System.out.println("Error handling DELETE: " + e.getMessage());
            return Response.NOT_FOUND;
        }
    }
}
