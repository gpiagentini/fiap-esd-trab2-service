package br.com.gpiagentini.api.application.service;

import br.com.gpiagentini.api.application.dto.AssetListResponseData;
import br.com.gpiagentini.api.application.port.in.IAssetAppService;
import br.com.gpiagentini.api.application.port.out.AssetPriceFinder;

import java.util.List;

public class AssetAppService implements IAssetAppService {

    private final AssetPriceFinder assetPriceFinder;

    public AssetAppService(AssetPriceFinder assetPriceFinder) {
        this.assetPriceFinder = assetPriceFinder;
    }

    @Override
    public List<AssetListResponseData> getPrices(String assets) {
        var assetPriceList = assetPriceFinder.getAssetPrices(assets);
        return assetPriceList.stream().map(data -> new AssetListResponseData(data.symbol(), data.regularMarketPrice())).toList();
    }
}
