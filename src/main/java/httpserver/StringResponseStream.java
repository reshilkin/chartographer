package httpserver;

import java.nio.charset.StandardCharsets;

public class StringResponseStream extends ResponseStream {
    public final static StringResponseStream EMPTY_STRING = new StringResponseStream("");
    private final byte[] response;
    private int curPos;

    public StringResponseStream(String response) {
        this.response = response.getBytes(StandardCharsets.UTF_8);
        curPos = 0;
    }

    @Override
    public long responseLength() {
        return response.length;
    }

    @Override
    public int read() {
        return response[curPos++];
    }

    @Override
    public int read(byte[] buffer) {
        int resLen = Math.min(buffer.length, response.length - curPos);
        System.arraycopy(response, curPos, buffer, 0, resLen);
        return resLen;
    }
}
