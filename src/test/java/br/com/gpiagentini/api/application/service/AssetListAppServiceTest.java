package br.com.gpiagentini.api.application.service;

import br.com.gpiagentini.api.application.dto.FinancialApi.AssetPriceData;
import br.com.gpiagentini.api.application.dto.SortDirection;
import br.com.gpiagentini.api.application.dto.SortOption;
import br.com.gpiagentini.api.application.port.out.AssetListRepository;
import br.com.gpiagentini.api.application.port.out.AssetPriceFinder;
import br.com.gpiagentini.api.application.port.out.AssetRepository;
import br.com.gpiagentini.api.domain.exception.InvalidAssetException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetListAppServiceTest {

    @Mock
    private AssetListRepository assetListRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetPriceFinder assetPriceFinder;

    @InjectMocks
    private AssetListAppService assetListAppService;

    // Test cases for createAssetListEntry
    @Test
    void testCreateAssetListEntryValidAsset() {
        // Arrange
        String validAsset = "AAPL";
        when(assetRepository.isAssetValid(validAsset)).thenReturn(true);
        assertDoesNotThrow(() -> assetListAppService.createAssetListEntry(validAsset));
    }

    @Test
    void testCreateAssetListEntryInvalidAsset() {
        // Arrange
        String invalidAsset = "INVALID_ASSET";
        when(assetRepository.isAssetValid(invalidAsset)).thenReturn(false);

        assertThrows(InvalidAssetException.class, () -> assetListAppService.createAssetListEntry(invalidAsset));
    }

    // Test cases for getAssetList
    @Test
    void testGetAssetListEmptyList() {
        // Arrange
        when(assetListRepository.getDisplaySortedListEntries()).thenReturn(List.of());

        var result = assetListAppService.getAssetList(SortOption.DEFAULT, SortDirection.ASC);
        assertEquals(List.of(), result);
    }

    @Test
    void testGetAssetListSortBySymbolAscending() {
        // Arrange
        var assets = List.of("GOGL", "AAPL", "AAP");
        var assetPrices = Arrays.asList(
                new AssetPriceData("GOGL", "-", "-", 150.0),
                new AssetPriceData("AAPL", "-", "-", 2800.0),
                new AssetPriceData("AAP", "-", "-", 2800.0)
        );
        when(assetListRepository.getDisplaySortedListEntries()).thenReturn(assets);
        when(assetPriceFinder.getAssetPrices("GOGL,AAPL,AAP")).thenReturn(assetPrices);

        var result = assetListAppService.getAssetList(SortOption.SYMBOL, SortDirection.ASC);

        // Assert: Sorted by symbol in ascending order
        assertEquals("AAP", result.get(0).asset());
        assertEquals("AAPL", result.get(1).asset());
        assertEquals("GOGL", result.get(2).asset());
    }

    @Test
    void testGetAssetListSortByPriceDescending() {
        // Arrange
        var assets = List.of("GOGL", "AAPL", "AAP");
        var assetPrices = Arrays.asList(
                new AssetPriceData("GOGL", "-", "-", 150.0),
                new AssetPriceData("AAPL", "-", "-", 3800.0),
                new AssetPriceData("AAP", "-", "-", 2800.0)
        );
        when(assetListRepository.getDisplaySortedListEntries()).thenReturn(assets);
        when(assetPriceFinder.getAssetPrices("GOGL,AAPL,AAP")).thenReturn(assetPrices);

        // Act
        var result = assetListAppService.getAssetList(SortOption.PRICE, SortDirection.DESC);

        // Assert: Sorted by price in descending order
        assertEquals("AAPL", result.get(0).asset());
        assertEquals("AAP", result.get(1).asset());
        assertEquals("GOGL", result.get(2).asset());
    }
}
