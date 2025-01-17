package br.com.gpiagentini.api.adapters.controllers;

import br.com.gpiagentini.api.application.dto.*;
import br.com.gpiagentini.api.application.port.in.IAssetListAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController

@RequestMapping("/api/v1/asset-list")
@Tag(name = "Asset List Controller", description = "Handles all asset list operations.")
public class AssetListController {

    private final IAssetListAppService assetListAppService;

    public AssetListController(IAssetListAppService assetListAppService) {
        this.assetListAppService = assetListAppService;
    }

    @Operation(summary = "Create new list entry", description = "Intended to create a new entry for the asset list.")
    @PostMapping
    @Transactional
    public void createAssetListEntry(@RequestBody @Valid CreateAssetListEntryData assetListEntryData, UriComponentsBuilder uriBuilder) {
        assetListAppService.createAssetListEntry(assetListEntryData.asset());
    }

    @Operation(summary = "Get your asset list", description = "Intended to retrieve the asset list for the user.")
    @GetMapping()
    public ResponseEntity<List<AssetListResponseData>> getAssetList(
            @Parameter(description = "Sort options for the Asset List", example = "SYMBOL") @RequestParam(required = false)
            SortOption sortOption,
            @Parameter(description = "Sort direction for the Asset List", example = "ASC") @RequestParam(required = false)
            SortDirection sortDirection) {
        if (sortOption == null) sortOption = SortOption.DEFAULT;
        if (sortDirection == null) sortDirection = SortDirection.ASC;
        var assetListRequest = assetListAppService.getAssetList(sortOption, sortDirection);
        return ResponseEntity.ok(assetListRequest);
    }

    @Operation(summary = "Sort your asset list", description = "Intended to alter the priority of items to be displayed")
    @PatchMapping("priority/{symbol}")
    @Transactional
    public void updateAssetListPriority(
            @Parameter(description = "Symbol to be ordered in your asset list", example = "AAPL")
            @NotBlank(message = "Symbol cannot be blank.") @PathVariable
            String symbol,
            @RequestBody @Valid UpdatePriorityRequestData updatePriorityRequestData) {
        assetListAppService.updateListPriority(symbol, updatePriorityRequestData);
    }

    @Operation(summary = "Delete asset from list", description = "Intended to remove an asset from your asset list")
    @DeleteMapping("/{symbol}")
    @Transactional
    public void deleteAssetListEntry(
            @Parameter(description = "Symbol to be deleted from your asset list", example = "AAPL")
            @NotBlank(message = "Symbol cannot be blank.") @PathVariable
            String symbol) {
        assetListAppService.removeListEntry(symbol);
    }
}
