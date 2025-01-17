package br.com.gpiagentini.api.adapters.controllers;

import br.com.gpiagentini.api.application.dto.AssetListResponseData;
import br.com.gpiagentini.api.application.port.in.IAssetAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/v1/assets")
@Tag(name = "Asset Controller", description = "Handles all asset operations.")
public class AssetController {

    private final IAssetAppService assetAppService;

    public AssetController(IAssetAppService assetAppService) {
        this.assetAppService = assetAppService;
    }

    @Operation(summary = "Get an asset price", description = "Intended to retrieve the asset price.")
    @GetMapping("/price/{assets}")
    public ResponseEntity<List<AssetListResponseData>> getAssetList(
            @Parameter(description = "Use comma separation to fetch multiple assets at a time", example = "AAPL,GOGL,AAP")
            @PathVariable @NotBlank(message = "Assets cannot be blank.") String assets) {
        var assetPrice = assetAppService.getPrices(assets);
        return ResponseEntity.ok(assetPrice);
    }

//    @Operation(summary = "Get all assets", description = "Intended to retrieve all assets available.")
//    @GetMapping()
//    public ResponseEntity<List<String>> getAvailableAssets(
//            @Parameter(description = "Use comma separation to fetch multiple assets at a time", example = "AAPL,GOGL,AAP")
//            @PathVariable @NotBlank(message = "Assets cannot be blank.") String assets) {
//        var assetPrice = assetAppService.getPrices(assets);
//        return ResponseEntity.ok(assetPrice);
//    }


}
