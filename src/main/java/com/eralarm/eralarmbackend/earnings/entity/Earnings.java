package com.eralarm.eralarmbackend.earnings.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;

import java.time.OffsetDateTime;

// 티커 - 실적 날짜 쌍은 유일
@Entity
@Table(name = "earnings", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"symbol", "earnings_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Earnings implements Serializable {

    //JPA JDBC 배칭 활성화
    //주의: IDENTITY 전략은 배치를 막으므로, SEQUENCE로 바꿈
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "earnings_seq")
    @SequenceGenerator(
            name = "earnings_seq",
            sequenceName = "earnings_seq",
            allocationSize = 50     // 배치 사이즈와 맞춰야 합니다
    )
    private Long id;

    @Column(length = 10, nullable = false, name = "symbol")
    private String symbol;

    @Column(nullable = false, name = "earnings_date")
    private OffsetDateTime earningsDate;

    @Column(nullable = true, name = "eps_actual")
    private Double epsActual;

    @Column(nullable = true, name = "eps_estimate")
    private Double epsEstimate;

    @Column(nullable = true, name = "surprice_pct")
    private Double surprisePct;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name = "last_updated")
    private Timestamp lastUpdated;

    @Builder
    public Earnings(String symbol, OffsetDateTime earningsDate, Double epsActual, Double epsEstimate, Double surprisePct) {
        this.symbol = symbol;
        this.earningsDate = earningsDate;
        this.epsActual = epsActual;
        this.epsEstimate = epsEstimate;
        this.surprisePct = surprisePct;
    }
}
