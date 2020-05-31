package edu.auctioneer.entity;

import lombok.Data;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "uuid_generator")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "initial_price", nullable = false)
    private BigDecimal initialPrice;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    @OrderBy(clause = "created_on desc")
    private List<BidEntity> bids = new ArrayList<>();
}
