package exceptions;

public class RowMissingException extends Exception {
    public RowMissingException() {
        super();
    }

    public RowMissingException(String message) {
        super(message);
    }

    public RowMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RowMissingException(Throwable cause) {
        super(cause);
    }
}
