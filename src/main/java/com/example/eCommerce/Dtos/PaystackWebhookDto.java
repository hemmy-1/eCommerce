package com.example.eCommerce.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackWebhookDto {

    private String event;
    private DataPayload data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataPayload {

        private String reference;

        private String status;

        private Long amount; // Sent in minor units (kobo)

        private String channel;

        @JsonProperty("paid_at")
        private String paidAt;
    }
}