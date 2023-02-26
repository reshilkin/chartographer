package exceptions;

public class IllegalFragmentException extends Exception{
    public IllegalFragmentException() {
        super();
    }

    public IllegalFragmentException(String message) {
        super(message);
    }

    public IllegalFragmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFragmentException(Throwable cause) {
        super(cause);
    }
}
