package br.com.gpiagentini.api.application.port.out;

import br.com.gpiagentini.api.application.dto.FinancialApi.AssetPriceData;

import java.util.List;

public interface AssetPriceFinder {
    List<AssetPriceData> getAssetPrices(String assets);
}
