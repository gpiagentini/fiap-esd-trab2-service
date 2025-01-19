package br.com.gpiagentini.api.adaper.controller;

import br.com.gpiagentini.api.adapters.controllers.AssetController;
import br.com.gpiagentini.api.adapters.controllers.handler.FinancialApiExceptionHandler;
import br.com.gpiagentini.api.application.dto.AssetListResponseData;
import br.com.gpiagentini.api.application.port.in.IAssetAppService;
import br.com.gpiagentini.api.adapters.external.authentication.TokenService;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AssetController.class, TokenService.class, FinancialApiExceptionHandler.class})
public class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAssetAppService assetAppService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAssetListPriceWithValidAssets() throws Exception {
        // Arrange
        String assets = "AAPL,GOGL";
        var mockResponse = Arrays.asList(
                new AssetListResponseData("AAPL", 150.0),
                new AssetListResponseData("GOGL", 2800.0)
        );
        var mockResponseJson = objectMapper.writeValueAsString(mockResponse);
        when(assetAppService.getPrices(assets)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assets/price/" + assets)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResponseJson));
    }

    @Test
    void testGetAssetListPriceWithNoFoundAsset() throws Exception {
        // Arrange
        String assets = "NOT_FOUND_ASSET";
        when(assetAppService.getPrices(assets)).thenThrow(new FinancialApiNotFoundException("Asset price not found in Financial Api."));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assets/price/" + assets)
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Asset price not found in Financial Api."));
    }
}
