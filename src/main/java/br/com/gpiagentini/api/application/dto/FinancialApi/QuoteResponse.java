package br.com.gpiagentini.api.application.dto.FinancialApi;
import java.util.List;

public record QuoteResponse(String error, List<AssetPriceData> result){
}