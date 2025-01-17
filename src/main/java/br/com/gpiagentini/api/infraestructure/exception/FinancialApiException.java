package br.com.gpiagentini.api.infraestructure.exception;

public class FinancialApiException extends RuntimeException {

    public FinancialApiException(String message) {
        super(message);
    }
}
