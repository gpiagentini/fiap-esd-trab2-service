package br.com.gpiagentini.api.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdatePriorityRequestData(
        @NotNull(message = "Priority number is required.") @Positive(message = "Priority must be greater than zero.")
        Integer priority
) {
}
