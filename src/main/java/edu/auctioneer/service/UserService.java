package edu.auctioneer.service;

import edu.auctioneer.entity.BidEntity;
import edu.auctioneer.entity.ItemEntity;
import edu.auctioneer.entity.UserEntity;
import edu.auctioneer.api.Bid;
import edu.auctioneer.api.Item;
import edu.auctioneer.api.User;
import edu.auctioneer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String saveUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        UUID id = userRepository.saveUser(userEntity);
        return id.toString();
    }

    public User getById(String id) {
        UserEntity userEntity = userRepository.getWithDetailsById(UUID.fromString(id));
        return User.builder()
                   .id(userEntity.getId())
                   .name(userEntity.getName())
                   .bids(bids(userEntity.getBids()))
                   .items(items(userEntity.getItems()))
                   .build();

    }

    private List<Item> items(List<ItemEntity> itemEntities) {
        return itemEntities.stream()
                           .map(itemEntity -> Item.builder()
                                                  .name(itemEntity.getName())
                                                  .initialPrice(itemEntity.getInitialPrice())
                                                  .highestBid(highestBid(itemEntity))
                                                  .build())
                           .collect(Collectors.toList());
    }

    private BigDecimal highestBid(ItemEntity itemEntity) {
        return itemEntity.getBids().stream()
                         .findFirst()
                         .map(BidEntity::getAmount)
                         .orElse(null);
    }

    private List<Bid> bids(List<BidEntity> bidEntities) {
        return bidEntities.stream()
                          .map(bidEntity -> Bid.builder()
                                               .id(bidEntity.getId())
                                               .targetItemId(bidEntity.getItem().getId())
                                               .amount(bidEntity.getAmount())
                                               .createdOn(bidEntity.getCreatedOn())
                                               .build())
                          .collect(Collectors.toList());
    }
}
