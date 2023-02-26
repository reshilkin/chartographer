package exceptions;

public class FileNotCreatedException extends Exception {
    public FileNotCreatedException() {
        super();
    }

    public FileNotCreatedException(String message) {
        super(message);
    }

    public FileNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotCreatedException(Throwable cause) {
        super(cause);
    }
}
