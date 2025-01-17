package br.com.gpiagentini.api.infraestructure.exception;

public class FinancialApiNotFoundException extends RuntimeException {

    public FinancialApiNotFoundException(String message) {
        super(message);
    }
}
