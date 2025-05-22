package com.eralarm.eralarmbackend.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockInfoResponse {
    private String symbol;
    private String name;
    @JsonProperty("lastsale")
    private String lastSale;
    @JsonProperty("netchange")
    private String netChange;
    @JsonProperty("pctchange")
    private String pctChange;
    private String marketCap;
    private String volume;
    private String country;
    @JsonProperty("ipoyear")
    private String ipoYear;
    private String industry;
    private String sector;
    private String url;
}
