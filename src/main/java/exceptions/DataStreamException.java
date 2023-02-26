package exceptions;

public class DataStreamException extends Exception {
    public DataStreamException() {
        super();
    }

    public DataStreamException(String message) {
        super(message);
    }

    public DataStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataStreamException(Throwable cause) {
        super(cause);
    }
}
