package br.com.gpiagentini.api.infraestructure.persistence.repository;

import br.com.gpiagentini.api.application.port.out.AssetListRepository;
import br.com.gpiagentini.api.domain.exception.AssetEntryListNotFoundException;
import br.com.gpiagentini.api.domain.exception.DuplicatedAssetListEntryException;
import br.com.gpiagentini.api.infraestructure.persistence.entity.UserAssetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAssetRepository implements AssetListRepository {

    @Autowired
    private UserAssetJpaRepository userAssetJpaRepository;

    @Override
    public List<String> getUnsortedListEntries() {
        var entries = userAssetJpaRepository.findAll();
        return entries.stream().map(UserAssetEntity::getSymbol).toList();
    }

    @Override
    public List<String> getSymbolSortedListEntries() {
        var entries = userAssetJpaRepository.findAll(Sort.by(Sort.Direction.ASC, "symbol"));
        return entries.stream().map(UserAssetEntity::getSymbol).toList();
    }

    @Override
    public List<String> getDisplaySortedListEntries() {
        var entries = userAssetJpaRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"));
        return entries.stream().map(UserAssetEntity::getSymbol).toList();
    }

    @Override
    public void addListEntry(String symbol) {
        var lastDisplayNumber = userAssetJpaRepository.getLastDisplayNumber() + 1;
        var newUserAsset = new UserAssetEntity(symbol, lastDisplayNumber);
        try {
            userAssetJpaRepository.save(newUserAsset);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatedAssetListEntryException("Asset " + symbol + " already exists in user list.");
        }
    }

    @Override
    public void updateEntryListPriority(String symbol, Integer priority) {
        var entryList = userAssetJpaRepository.findBySymbol(symbol);
        if (entryList == null)
            throw new AssetEntryListNotFoundException("User entry list does not contains symbol " + symbol);
        var originalPosition = entryList.getDisplayOrder();
        var decrease = originalPosition < priority;
        userAssetJpaRepository.findAll().forEach(entry -> {
            if(entry.getSymbol().equalsIgnoreCase(symbol))
                entry.setDisplayOrder(priority);
            else if(decrease) {
                if(entry.getDisplayOrder() > originalPosition && entry.getDisplayOrder() <= priority)
                    entry.setDisplayOrder(entry.getDisplayOrder() - 1);
            }
            else {
                if(entry.getDisplayOrder() < originalPosition && entry.getDisplayOrder() >= priority)
                    entry.setDisplayOrder(entry.getDisplayOrder() + 1);
            }
            userAssetJpaRepository.save(entry);
        });
    }

    public void removeEntryList(String symbol) {
        userAssetJpaRepository.deleteBySymbol(symbol);
    }
}

interface UserAssetJpaRepository extends JpaRepository<UserAssetEntity, Long> {
    UserAssetEntity findBySymbol(String symbol);
    @Modifying
    @Query(value = "UPDATE UserAssetEntity SET displayOrder = displayOrder + :value WHERE displayOrder > :start AND displayOrder < :end")
    void updateEntriesPositionBetweenRange(Integer value, Integer start, Integer end);
    @Query(value = "SELECT COALESCE(MAX(displayOrder), 0) from UserAssetEntity")
    Integer getLastDisplayNumber();
    @Modifying
    @Query(value="DELETE FROM UserAssetEntity WHERE symbol = :symbol")
    void deleteBySymbol(String symbol);
}
