package br.com.gpiagentini.api.domain.exception;

public class InvalidAssetException extends RuntimeException {
    public InvalidAssetException(String message) {
        super(message);
    }
}
