package br.com.gpiagentini.api.adaper.external;

import br.com.gpiagentini.api.adapters.external.FinancialApi;
import br.com.gpiagentini.api.application.dto.FinancialApi.AssetPriceData;
import br.com.gpiagentini.api.application.dto.FinancialApi.FinancialApiResponseData;
import br.com.gpiagentini.api.application.dto.FinancialApi.QuoteResponse;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiException;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FinancialApiTest {

    @Mock
    private RestTemplate financialRestTemplate;

    @InjectMocks
    private FinancialApi financialApi;

    @Test
    void testGetAssetPricesSuccess() {
        // Arrange
        String assets = "AAPL,GOOGL";
        var mockAssetData = List.of(
                new AssetPriceData("AAPL", "-", "-", 150.00),
                new AssetPriceData("GOGL", "-", "-", 350.00));
        var mockResponseData = new FinancialApiResponseData(new QuoteResponse(null, mockAssetData));

        when(financialRestTemplate.getForObject("/v6/finance/quote?region=US&lang=en&symbols=" + assets, FinancialApiResponseData.class))
                .thenReturn(mockResponseData);

        // Act
        var result = financialApi.getAssetPrices(assets);

        // Assert
        assertEquals(mockAssetData, result);
    }

    @Test
    void testGetAssetPricesWithApiError() {
        // Arrange
        String assets = "AAPL,GOOGL";

        when(financialRestTemplate.getForObject("/v6/finance/quote?region=US&lang=en&symbols=" + assets, FinancialApiResponseData.class))
                .thenThrow(new FinancialApiException("Error"));
        assertThrows(FinancialApiException.class, () -> financialApi.getAssetPrices(assets));
    }

    @Test
    void testGetAssetPricesNotFound() {
        // Arrange
        String assets = "AAPL,GOOGL";
        var mockResponseData = new FinancialApiResponseData(new QuoteResponse(null, List.of()));

        when(financialRestTemplate.getForObject("/v6/finance/quote?region=US&lang=en&symbols=" + assets, FinancialApiResponseData.class))
                .thenReturn(mockResponseData);
        assertThrows(FinancialApiNotFoundException.class, () -> financialApi.getAssetPrices(assets));
    }

    @Test
    void testGetAssetPricesWithErrorNotNull() {
        // Arrange
        String assets = "AAPL,GOOGL";
        var mockResponseData = new FinancialApiResponseData(new QuoteResponse("An error ocurred", List.of()));
        when(financialRestTemplate.getForObject("/v6/finance/quote?region=US&lang=en&symbols=" + assets, FinancialApiResponseData.class))
                .thenReturn(mockResponseData);

        // Act & Assert
        assertThrows(FinancialApiException.class, () -> financialApi.getAssetPrices(assets));
    }

}
