package com.eralarm.eralarmbackend.earnings.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class EarningsSingleWithSubResponse {
    private Long id;
    private String symbol;
    private OffsetDateTime earningsDate;
    private Boolean subscribed;
}
