package com.eralarm.eralarmbackend.stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock,String> {
    List<Stock> findAllByEnabledTrue();

    Page<Stock> findAllBySymbolStartsWithIgnoreCaseOrNameStartsWithIgnoreCase(
            String symbolPart,
            String namePart,
            Pageable pageable
    );

    Page<Stock> findAllByEnabledTrue(Pageable pageable);
}
