package com.eralarm.eralarmbackend.earnings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class EarningsMultipleResponse {
    private String symbol;

    private List<OffsetDateTime> earningsDates;
}
