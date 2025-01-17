package br.com.gpiagentini.api.infraestructure.configuration.web;

import br.com.gpiagentini.api.application.port.in.IAssetAppService;
import br.com.gpiagentini.api.application.port.in.IAssetListAppService;
import br.com.gpiagentini.api.application.port.out.AssetListRepository;
import br.com.gpiagentini.api.application.port.out.AssetPriceFinder;
import br.com.gpiagentini.api.application.port.out.AssetRepository;
import br.com.gpiagentini.api.application.service.AssetAppService;
import br.com.gpiagentini.api.application.service.AssetListAppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public IAssetListAppService assetListAppService(AssetListRepository assetListRepository, AssetPriceFinder assetPriceFinder,
                                                    AssetRepository assetRepository) {
        return new AssetListAppService(assetListRepository, assetPriceFinder, assetRepository);
    }

    @Bean
    public IAssetAppService assetAppService(AssetPriceFinder assetPriceFinder) {
        return new AssetAppService(assetPriceFinder);
    }

}