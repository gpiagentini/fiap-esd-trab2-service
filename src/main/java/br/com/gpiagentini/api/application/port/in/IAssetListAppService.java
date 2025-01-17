package br.com.gpiagentini.api.application.port.in;

import br.com.gpiagentini.api.application.dto.AssetListResponseData;
import br.com.gpiagentini.api.application.dto.SortDirection;
import br.com.gpiagentini.api.application.dto.SortOption;
import br.com.gpiagentini.api.application.dto.UpdatePriorityRequestData;

import java.util.List;

public interface IAssetListAppService {
    void createAssetListEntry(String asset);
    List<AssetListResponseData> getAssetList(SortOption sortOption, SortDirection sortDirection);
    void updateListPriority(String symbol, UpdatePriorityRequestData updatePriorityData);
    void removeListEntry(String symbol);
}
