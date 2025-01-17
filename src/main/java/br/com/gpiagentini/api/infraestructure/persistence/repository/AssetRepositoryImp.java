package br.com.gpiagentini.api.infraestructure.persistence.repository;

import br.com.gpiagentini.api.application.port.out.AssetRepository;
import br.com.gpiagentini.api.infraestructure.persistence.entity.AssetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRepositoryImp implements AssetRepository {

    @Autowired
    private AssetJpaRepository assetJpaRepository;


    @Override
    public boolean isAssetValid(String asset) {
        return assetJpaRepository.existsBySymbolAndIsValid(asset, true);
    }
}

interface AssetJpaRepository extends JpaRepository<AssetEntity, Long> {
    boolean existsBySymbolAndIsValid(String symbol, boolean isValid);
}
