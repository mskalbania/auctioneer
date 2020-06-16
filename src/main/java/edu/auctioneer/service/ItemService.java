package edu.auctioneer.service;

import edu.auctioneer.entity.BidEntity;
import edu.auctioneer.entity.ItemEntity;
import edu.auctioneer.entity.UserEntity;
import edu.auctioneer.exception.PriceToLowException;
import edu.auctioneer.api.Bid;
import edu.auctioneer.api.Item;
import edu.auctioneer.repository.ItemRepository;
import edu.auctioneer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<Item> getAll(String pattern) {
        return itemRepository.findAllBy(pattern).stream()
                             .map(entity -> Item.builder()
                                                .id(entity.getId())
                                                .ownerId(entity.getOwner().getId())
                                                .name(entity.getName())
                                                .initialPrice(entity.getInitialPrice())
                                                .highestBid(highestBid(entity))
                                                .build())
                             .collect(Collectors.toList());
    }

    public Item getById(String id) {
        return mapToItem(itemRepository.findById(UUID.fromString(id)));
    }

    public void placeBid(String id, Bid bid) {
        UserEntity userEntity = userRepository.getById(UUID.fromString(bid.getBidderId().toString()));
        ItemEntity itemEntity = itemRepository.findById(UUID.fromString(id));
        BigDecimal highestBid = highestBid(itemEntity);
        if (bid.getAmount().compareTo(highestBid) > 0) {
            BidEntity newBid = new BidEntity();
            newBid.setAmount(bid.getAmount());
            newBid.setOwner(userEntity);
            newBid.setItem(itemEntity);
            itemEntity.getBids().add(newBid);
        } else {
            LOG.error("Unable to place bid {} for item {}", bid.toString(), id);
            throw new PriceToLowException(id, bid.getAmount());
        }
    }

    private Item mapToItem(ItemEntity entity) {
        return Item.builder()
                   .id(entity.getId())
                   .ownerId(entity.getOwner().getId())
                   .name(entity.getName())
                   .initialPrice(entity.getInitialPrice())
                   .highestBid(highestBid(entity))
                   .bids(entity.getBids().stream()
                               .map(bidEntity -> Bid.builder()
                                                    .id(bidEntity.getId())
                                                    .bidderId(bidEntity.getOwner().getId())
                                                    .createdOn(bidEntity.getCreatedOn())
                                                    .amount(bidEntity.getAmount())
                                                    .build())
                               .collect(Collectors.toList()))
                   .build();
    }

    private BigDecimal highestBid(ItemEntity itemEntity) {
        return itemEntity.getBids().stream()
                         .findFirst()
                         .map(BidEntity::getAmount)
                         .orElse(null);
    }

    public String addItem(Item item) {
        UserEntity userEntity = userRepository.getById(UUID.fromString(item.getOwnerId().toString()));
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(item.getName());
        itemEntity.setInitialPrice(item.getInitialPrice());
        itemEntity.setOwner(userEntity);
        UUID itemId = itemRepository.save(itemEntity);
        return itemId.toString();
    }
}
