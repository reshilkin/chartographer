package exceptions;

public class ShapeSizeException extends Exception {
    public ShapeSizeException() {
        super();
    }

    public ShapeSizeException(String message) {
        super(message);
    }

    public ShapeSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShapeSizeException(Throwable cause) {
        super(cause);
    }
}
