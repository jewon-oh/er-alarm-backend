package com.eralarm.eralarmbackend.earnings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EarningsUpdateRequest {
    private String symbol;
    private List<EarningsDateDto> earningsDates;
}
