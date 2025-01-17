package br.com.gpiagentini.api.application.port.out;

import java.util.List;

public interface AssetListRepository {
    List<String> getSymbolSortedListEntries();
    List<String> getDisplaySortedListEntries();
    List<String> getUnsortedListEntries();
    void addListEntry(String symbol);
    void updateEntryListPriority(String symbol, Integer priority);
    void removeEntryList(String symbol);
}
