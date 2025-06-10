package com.eralarm.eralarmbackend.earnings.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@Getter
public class EarningsSingleWithSubResponse {
    private Long id;
    private String symbol;
    private OffsetDateTime earningsDate;
    private Double epsActual;
    private Double epsEstimate;
    private Double surprisePct;
    private Boolean subscribed;

}
