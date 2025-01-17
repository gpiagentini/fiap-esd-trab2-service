package br.com.gpiagentini.api.application.service;

import br.com.gpiagentini.api.application.dto.AssetListResponseData;
import br.com.gpiagentini.api.application.dto.SortDirection;
import br.com.gpiagentini.api.application.dto.SortOption;
import br.com.gpiagentini.api.application.dto.UpdatePriorityRequestData;
import br.com.gpiagentini.api.application.port.in.IAssetListAppService;
import br.com.gpiagentini.api.application.port.out.AssetListRepository;
import br.com.gpiagentini.api.application.port.out.AssetPriceFinder;
import br.com.gpiagentini.api.application.port.out.AssetRepository;
import br.com.gpiagentini.api.domain.exception.InvalidAssetException;

import java.util.Comparator;
import java.util.List;

public class AssetListAppService implements IAssetListAppService {

    private final AssetListRepository assetListRepository;
    private final AssetRepository assetRepository;
    private final AssetPriceFinder assetPriceFinder;

    public AssetListAppService(AssetListRepository assetListRepository, AssetPriceFinder assetPriceFinder, AssetRepository assetRepository) {
        this.assetListRepository = assetListRepository;
        this.assetPriceFinder = assetPriceFinder;
        this.assetRepository = assetRepository;
    }

    @Override
    public void createAssetListEntry(String asset) {
        if (assetRepository.isAssetValid(asset))
            assetListRepository.addListEntry(asset);
        else
            throw new InvalidAssetException(asset + " is not a valid asset.");
    }

    @Override
    public List<AssetListResponseData> getAssetList(SortOption sortOption, SortDirection sortDirection) {
        var assetList = assetListRepository.getDisplaySortedListEntries();
        if(assetList.isEmpty()) return List.of();
        var assetPriceList = assetPriceFinder.getAssetPrices(String.join(",", assetList));
        var responseList = assetPriceList.stream().map(data -> new AssetListResponseData(data.symbol(), data.regularMarketPrice())).toList();
        switch (sortOption) {
            case SYMBOL ->
                    responseList = responseList.stream().sorted(Comparator.comparing(AssetListResponseData::asset)).toList();
            case PRICE ->
                    responseList = responseList.stream().sorted(Comparator.comparing(AssetListResponseData::price)).toList();
        }
        return sortDirection.equals(SortDirection.ASC) ? responseList : responseList.reversed();
    }

    @Override
    public void updateListPriority(String symbol, UpdatePriorityRequestData updatePriorityData) {
        assetListRepository.updateEntryListPriority(symbol, updatePriorityData.priority());
    }

    @Override
    public void removeListEntry(String symbol) {
        assetListRepository.removeEntryList(symbol);
    }


}
