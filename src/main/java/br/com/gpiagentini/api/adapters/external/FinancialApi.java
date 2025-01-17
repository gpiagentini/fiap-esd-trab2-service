package br.com.gpiagentini.api.adapters.external;

import br.com.gpiagentini.api.application.dto.FinancialApi.AssetPriceData;
import br.com.gpiagentini.api.application.dto.FinancialApi.FinancialApiResponseData;
import br.com.gpiagentini.api.application.port.out.AssetPriceFinder;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiException;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class FinancialApi implements AssetPriceFinder {

    @Autowired
    private RestTemplate financialRestTemplate;

    @Override
    public List<AssetPriceData> getAssetPrices(String assets) {
        String path = "/v6/finance/quote?region=US&lang=en&symbols=" + assets;
        var response = financialRestTemplate.getForObject(path, FinancialApiResponseData.class);
        checkResponseValidity(response);
        return response.quoteResponse().result();
    }

    private void checkResponseValidity(FinancialApiResponseData response) {
        if (response == null) handleApiError("Error obtaining Financial Api result.");
        var error = response.quoteResponse().error();
        var result = response.quoteResponse().result();
        if (error != null) handleApiError(error);
        if (result == null) handleApiError("Error obtaining Financial Api result.");
        if (result.isEmpty()) handleNotFoundError();
    }

    private void handleApiError(String error) {
        throw new FinancialApiException(error);
    }

    private void handleNotFoundError() {
        throw new FinancialApiNotFoundException("Asset price not found in Financial Api.");
    }

}
