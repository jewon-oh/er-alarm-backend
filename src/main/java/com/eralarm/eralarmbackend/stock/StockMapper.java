package com.eralarm.eralarmbackend.stock;

import com.eralarm.eralarmbackend.stock.dto.StockInfoResponse;

public class StockMapper {
    public static Stock toEntity(StockInfoResponse response){
        return Stock.builder()
                .symbol(response.getSymbol().trim())
                .name(response.getName())
                .country(response.getCountry())
                .ipoYear(response.getIpoYear())
                .sector(response.getSector())
                .industry(response.getIndustry())
                .marketCap(response.getMarketCap())
                .lastSale(response.getLastSale())
                .netChange(response.getNetChange())
                .pctChange(response.getPctChange())
                .volume(response.getVolume())
                .build();
    }
}
