package com.eralarm.eralarmbackend.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StockInvalidateRequest {
    private String symbol;
    private String message;
}
