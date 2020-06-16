package edu.auctioneer.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "uuid_generator")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "owner")
    private List<BidEntity> bids = new ArrayList<>();

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "owner")
    private List<ItemEntity> items = new ArrayList<>();
}
