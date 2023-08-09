package com.pokemon.ao.api.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ExchangeStatus {
    SUCCESS(200),
    INVALID(400),
    ERROR(500);
    @JsonProperty("status_code")
    private final int statusCode;

    ExchangeStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
