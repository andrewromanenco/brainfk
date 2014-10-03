package bfk;

public class BfkError extends RuntimeException {

    private static final long serialVersionUID = -217102134939772773L;

    public BfkError(String message) {
        super(message);
    }

    public BfkError(Throwable cause) {
        super(cause);
    }

    public BfkError(String message, Throwable cause) {
        super(message, cause);
    }

}
