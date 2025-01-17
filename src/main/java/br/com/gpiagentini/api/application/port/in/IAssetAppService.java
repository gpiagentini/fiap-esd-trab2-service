package br.com.gpiagentini.api.application.port.in;

import br.com.gpiagentini.api.application.dto.AssetListResponseData;

import java.util.List;

public interface IAssetAppService {
    List<AssetListResponseData> getPrices(String assets);
}
