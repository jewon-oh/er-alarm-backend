package com.eralarm.eralarmbackend.earnings;

import com.eralarm.eralarmbackend.earnings.entity.Earnings;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "subscribed_earnings",  schema = "public",
uniqueConstraints = @UniqueConstraint(columnNames = {"earnings_id", "fcm_token"}))
@EntityListeners(AuditingEntityListener.class)
public class SubscribedEarnings implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "earnings_id", nullable = false)
    private Earnings earnings;

//    @Column(length = 10, nullable = false, name="symbol")
//    private String symbol;
//
    @Column(nullable = false, name="fcm_token")
    private String fcmToken;

    @Column(nullable = false, name="subscribed")
    private Boolean subscribed = true;

    @CreatedDate
    private LocalDateTime createdAt;


    @Builder
    public SubscribedEarnings(Earnings earnings, String fcmToken) {
        this.earnings = earnings;
        this.fcmToken = fcmToken;
        this.subscribed = true;
    }

    public void subscribe() {
        this.subscribed = true;
    }

    public void unsubscribe() {
        this.subscribed = false;
    }
}
