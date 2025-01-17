package br.com.gpiagentini.api.domain.exception;

public class DuplicatedAssetListEntryException extends RuntimeException {

    public DuplicatedAssetListEntryException(String message) {
        super(message);
    }
}
