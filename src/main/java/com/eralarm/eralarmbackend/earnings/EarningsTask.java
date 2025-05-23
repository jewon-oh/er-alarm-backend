package com.eralarm.eralarmbackend.earnings;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@NoArgsConstructor
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "earnings_task")
public class EarningsTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private String taskId;

    @Enumerated(EnumType.STRING)
    private EarningsTaskStatus status; // SUCCESS, FAILURE

    private String message;

    @CreatedDate
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public EarningsTask(String symbol, String taskId, EarningsTaskStatus status ) {
        this.symbol = symbol;
        this.taskId = taskId;
        this.status = status;
    }

    public void update(EarningsTaskStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
