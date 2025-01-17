package br.com.gpiagentini.api.adaper.controller;

import br.com.gpiagentini.api.adapters.controllers.AssetListController;
import br.com.gpiagentini.api.adapters.controllers.handler.FinancialApiExceptionHandler;
import br.com.gpiagentini.api.adapters.controllers.handler.InvalidAssetListActionHandler;
import br.com.gpiagentini.api.application.dto.*;
import br.com.gpiagentini.api.application.port.in.IAssetAppService;
import br.com.gpiagentini.api.application.port.in.IAssetListAppService;
import br.com.gpiagentini.api.adapters.external.authentication.TokenService;
import br.com.gpiagentini.api.domain.exception.AssetEntryListNotFoundException;
import br.com.gpiagentini.api.domain.exception.DuplicatedAssetListEntryException;
import br.com.gpiagentini.api.domain.exception.InvalidAssetException;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiException;
import br.com.gpiagentini.api.infraestructure.exception.FinancialApiNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AssetListController.class, TokenService.class, FinancialApiExceptionHandler.class, InvalidAssetListActionHandler.class})
public class AssetListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAssetAppService assetAppService;
    @MockBean
    private IAssetListAppService assetListAppService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateNewListEntryWithValidAsset() throws Exception {
        // Arrange
        var assetEntryData = new CreateAssetListEntryData("AAPL");
        var mockResponse = List.of(new AssetListResponseData("AAPL", 150.0));
        var mockResponseJson = objectMapper.writeValueAsString(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/asset-list")
                        .content(objectMapper.writeValueAsString(assetEntryData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateNewListEntryWithInvalidAsset() throws Exception {
        // Arrange
        var assetEntryData = new CreateAssetListEntryData("INVALID_ASSET");
        Mockito.doThrow(new InvalidAssetException(assetEntryData.asset() + " is not a valid asset."))
                        .when(assetListAppService).createAssetListEntry(assetEntryData.asset());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/asset-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateNewListEntryWithDuplicatedAsset() throws Exception {
        // Arrange
        var assetEntryData = new CreateAssetListEntryData("DUPLICATED_ASSET");
        Mockito.doThrow(new DuplicatedAssetListEntryException("Asset " + assetEntryData.asset() + " already exists in user list."))
                .when(assetListAppService).createAssetListEntry(assetEntryData.asset());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/asset-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAssetListWithoutSorting() throws Exception {
        // Arrange
        var mockResponse = List.of(
                new AssetListResponseData("GOGL", 2800.0),
                new AssetListResponseData("AAPL", 150.0)
        );
        var mockResponseJson = objectMapper.writeValueAsString(mockResponse);
        when(assetListAppService.getAssetList(SortOption.DEFAULT, SortDirection.ASC)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/asset-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResponseJson));
    }

    @Test
    void testGetAssetListWithSpecifiedSorting() throws Exception {
        // Arrange: Mock response for specific sort options
        var mockResponse = Arrays.asList(
                new AssetListResponseData("GOGL", 2800.0),
                new AssetListResponseData("AAPL", 150.0)
        );
        var mockResponseJson = objectMapper.writeValueAsString(mockResponse);
        when(assetListAppService.getAssetList(SortOption.SYMBOL, SortDirection.DESC)).thenReturn(mockResponse);

        // Act & Assert: Provide specific sorting parameters
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/asset-list")
                        .param("sortOption", "SYMBOL")
                        .param("sortDirection", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResponseJson));
    }

    @Test
    void testGetEmptyAssetList() throws Exception {
        // Arrange
        when(assetListAppService.getAssetList(SortOption.DEFAULT, SortDirection.ASC)).thenReturn(List.of());

        // Act & Assert: Provide specific sorting parameters
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/asset-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetAssetListWhenApiExceptionOccurs() throws Exception {
        // Arrange
        var error = "Api error.";
        when(assetListAppService.getAssetList(SortOption.DEFAULT, SortDirection.ASC)).thenReturn(List.of());
        Mockito.doThrow(new FinancialApiException(error))
                .when(assetListAppService).getAssetList(SortOption.DEFAULT, SortDirection.ASC);

        // Act & Assert: Provide specific sorting parameters
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/asset-list")
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(error));
    }

    @Test
    void testGetAssetListWhenAssetNotFound() throws Exception {
        // Arrange
        when(assetListAppService.getAssetList(SortOption.DEFAULT, SortDirection.ASC)).thenReturn(List.of());
        Mockito.doThrow(new FinancialApiNotFoundException("Asset price not found in Financial Api."))
                .when(assetListAppService).getAssetList(SortOption.DEFAULT, SortDirection.ASC);

        // Act & Assert: Provide specific sorting parameters
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/asset-list")
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Asset price not found in Financial Api."));
    }

    @Test
    void testSortAssetList() throws Exception {
        // Arrange
        var symbol = "AAPL";
        var updatePriorityRequestData = new UpdatePriorityRequestData(1);
        Mockito.doNothing().when(assetListAppService).updateListPriority(symbol, updatePriorityRequestData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/asset-list/priority/" + symbol)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePriorityRequestData)))
                .andExpect(status().isOk());
    }

    @Test
    void testSortAssetListWhenAssetNotFound() throws Exception {
        // Arrange
        var symbol = "AAPL";
        var updatePriorityRequestData = new UpdatePriorityRequestData(1);
        Mockito.doThrow(new AssetEntryListNotFoundException("User entry list does not contains symbol " + symbol))
                        .when(assetListAppService).updateListPriority(symbol, updatePriorityRequestData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/asset-list/priority/" + symbol)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePriorityRequestData)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSortAssetListWithoutParams() throws Exception {
        // Arrange
        var symbol = "AAPL";
        var updatePriorityRequestData = new UpdatePriorityRequestData(1);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/asset-list/priority/" + symbol)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteListEntry() throws Exception {
        // Arrange
        var symbol = "AAPL";
        Mockito.doNothing().when(assetListAppService).removeListEntry(symbol);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/asset-list/" + symbol)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
