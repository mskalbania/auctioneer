package edu.auctioneer.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@Table(name = "bid")
public class BidEntity {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "uuid_generator")
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne(fetch = LAZY)
    private ItemEntity item;

    @ManyToOne(fetch = LAZY)
    private UserEntity owner;
}
