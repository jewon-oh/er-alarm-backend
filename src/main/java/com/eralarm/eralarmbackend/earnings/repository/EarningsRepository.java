package com.eralarm.eralarmbackend.earnings.repository;

import com.eralarm.eralarmbackend.earnings.dto.EarningsSingleWithSubResponse;
import com.eralarm.eralarmbackend.earnings.entity.Earnings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EarningsRepository extends JpaRepository<Earnings, Long> {
    List<Earnings> findAllBySymbolIgnoreCase(String symbol);

    Page<Earnings> findAllBySymbolIgnoreCase(String symbol, Pageable pageable);

    List<Earnings> findAllByEarningsDateBetween(OffsetDateTime start, OffsetDateTime end);

    Page<Earnings> findAllPageByEarningsDateBetween(OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    @Query(
            value = """
                      SELECT new com.eralarm.eralarmbackend.earnings.dto.EarningsSingleWithSubResponse(
                        e.id,
                        e.symbol,
                        e.earningsDate,
                        CASE WHEN se.id IS NOT NULL AND se.subscribed=true THEN true ELSE false END
                      )
                      FROM Earnings e
                      LEFT JOIN SubscribedEarnings se
                        ON se.earnings = e
                       AND se.fcmToken = :fcmToken
                      WHERE UPPER(e.symbol) = UPPER(:symbol)
                      ORDER BY e.earningsDate DESC
                    """,
            countQuery = """
                      SELECT COUNT(e)
                      FROM Earnings e
                      WHERE UPPER(e.symbol) = UPPER(:symbol)
                    """
    )
    Page<EarningsSingleWithSubResponse> findAllWithSubscriptionBySymbol(
            @Param("fcmToken") String fcmToken,
            @Param("symbol") String symbol,
            Pageable pageable
    );

    @Query("""
              select e.earningsDate
              from Earnings e
              where lower(e.symbol) = lower(:symbol)
            """)
    List<OffsetDateTime> findEarningsDatesBySymbolIgnoreCase(@Param("symbol") String symbol);
}
