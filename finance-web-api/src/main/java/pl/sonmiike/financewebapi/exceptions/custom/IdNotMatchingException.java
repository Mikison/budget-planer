package pl.sonmiike.financewebapi.exceptions.custom;

public class IdNotMatchingException extends RuntimeException {

    public IdNotMatchingException(String message) {
        super(message);
    }
}
