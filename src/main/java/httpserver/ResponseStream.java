package httpserver;

import java.io.InputStream;

public abstract class ResponseStream extends InputStream {
    public abstract long responseLength();
}
