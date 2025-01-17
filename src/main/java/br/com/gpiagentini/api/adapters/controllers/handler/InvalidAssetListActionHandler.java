package br.com.gpiagentini.api.adapters.controllers.handler;

import br.com.gpiagentini.api.domain.exception.AssetEntryListNotFoundException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class InvalidAssetListActionHandler {

    @ExceptionHandler(AssetEntryListNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestBody
    public String handleInvalidAsset(AssetEntryListNotFoundException ex) {
        return ex.getMessage();
    }
}
