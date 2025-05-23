package com.eralarm.eralarmbackend.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "stock",  schema = "public")
@Getter
@NoArgsConstructor
public class Stock implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastSale;

    @Column(nullable = false)
    private String netChange;

    @Column(nullable = false)
    private String pctChange;

    @Column(nullable = false)
    private String marketCap;

    @Column(nullable = false)
    private String volume;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String ipoYear;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private String sector;

    @Column(nullable = false)
    private Boolean enabled;

    @Builder
    public Stock(String symbol,
                 String name,
                 String lastSale,
                 String netChange,
                 String pctChange,
                 String marketCap,
                 String volume,
                 String country,
                 String ipoYear,
                 String industry,
                 String sector) {
        this.symbol = symbol;
        this.name = name;
        this.lastSale = lastSale;
        this.netChange = netChange;
        this.pctChange = pctChange;
        this.marketCap = marketCap;
        this.volume = volume;
        this.country = country;
        this.ipoYear = ipoYear;
        this.industry = industry;
        this.sector = sector;
        enabled = true;
    }

    public void updateEnabled(boolean b) {
        this.enabled = b;
    }
}
