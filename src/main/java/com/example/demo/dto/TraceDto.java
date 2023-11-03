package com.example.demo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record TraceDto(
        @JsonProperty("order_key") String orderKey,
        @JsonProperty("user_id") String userId,
        @JsonProperty("paymentId") String paymentId,
        @JsonProperty("payload") String payload
) {
    public TraceDto {
        Objects.requireNonNull(userId);

        if(Objects.isNull(orderKey)) orderKey = "";
        if(Objects.isNull(paymentId)) paymentId = "";
    }
    public TraceDto() {
        this(null, "", null, null);
    }
}
