package br.com.gpiagentini.api.adapters.controllers.handler;

import br.com.gpiagentini.api.infraestructure.exception.FinancialApiException;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiNotFoundException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FinancialApiExceptionHandler {

    @ExceptionHandler(FinancialApiException.class)
    @RequestBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleFinancialApiException(FinancialApiException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(FinancialApiNotFoundException.class)
    @RequestBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFinancialApiException(FinancialApiNotFoundException ex) {
        return ex.getMessage();
    }
}
