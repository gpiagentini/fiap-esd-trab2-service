package br.com.gpiagentini.api.adapters.controllers.handler;

import br.com.gpiagentini.api.domain.exception.DuplicatedAssetListEntryException;
import br.com.gpiagentini.api.domain.exception.InvalidAssetException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidAssetHandler {

    @ExceptionHandler(InvalidAssetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestBody
    public String handleInvalidAsset(InvalidAssetException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicatedAssetListEntryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestBody
    public String handleInvalidAsset(DuplicatedAssetListEntryException ex) {
        return ex.getMessage();
    }

}
