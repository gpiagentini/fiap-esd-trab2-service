package br.com.gpiagentini.api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAssetListEntryData(
        @NotBlank(message = "Asset is required.") String asset) {
}
