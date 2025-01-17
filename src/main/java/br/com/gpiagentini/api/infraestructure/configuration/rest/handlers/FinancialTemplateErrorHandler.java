package br.com.gpiagentini.api.infraestructure.configuration.rest.handlers;

import br.com.gpiagentini.api.infraestructure.exception.FinancialApiException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.logging.Logger;

public class FinancialTemplateErrorHandler extends DefaultResponseErrorHandler {

    private Logger logger = Logger.getLogger(FinancialTemplateErrorHandler.class.getName());

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        this.logger.severe(response.getStatusText());
        var responseStatusCode = response.getStatusCode();
        if (responseStatusCode.is4xxClientError() && responseStatusCode.isSameCodeAs(HttpStatusCode.valueOf(403))) {
            throw new FinancialApiException("Service without access to Asset Price API.");
        }
        throw new FinancialApiException("Error using Asset Price API.");
    }
}
