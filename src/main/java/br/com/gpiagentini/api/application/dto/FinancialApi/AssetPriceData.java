package br.com.gpiagentini.api.application.dto.FinancialApi;

public record AssetPriceData(String symbol, String displayName, String quoteSourceName, Double regularMarketPrice) {
}

