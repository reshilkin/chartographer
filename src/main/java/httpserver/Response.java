package httpserver;

import java.net.HttpURLConnection;

public class Response {
    public static final Response NOT_FOUND =
            new Response(StringResponseStream.EMPTY_STRING, HttpURLConnection.HTTP_NOT_FOUND);

    public static final Response OK =
            new Response(StringResponseStream.EMPTY_STRING, HttpURLConnection.HTTP_OK);

    public static final Response NOT_IMPLEMENTED =
            new Response(StringResponseStream.EMPTY_STRING, HttpURLConnection.HTTP_NOT_IMPLEMENTED);

    public static final Response BAD_REQUEST =
            new Response(StringResponseStream.EMPTY_STRING, HttpURLConnection.HTTP_BAD_REQUEST);

    public static final Response SERVER_ERROR =
            new Response(StringResponseStream.EMPTY_STRING, HttpURLConnection.HTTP_SERVER_ERROR);

    private final ResponseStream responseStream;
    private final int rcode;

    public Response(ResponseStream responseStream, int rcode) {
        this.responseStream = responseStream;
        this.rcode = rcode;
    }

    public ResponseStream getResponseStream() {
        return responseStream;
    }

    public int getRCode() {
        return rcode;
    }
}
