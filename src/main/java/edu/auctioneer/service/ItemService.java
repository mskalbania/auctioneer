package edu.auctioneer.service;

import edu.auctioneer.entity.BidEntity;
import edu.auctioneer.entity.ItemEntity;
import edu.auctioneer.model.Bid;
import edu.auctioneer.model.Item;
import edu.auctioneer.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAll(String pattern) {
        return itemRepository.findAllBy(pattern).stream()
                             .map(entity -> Item.builder()
                                                .id(entity.getId())
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
        ItemEntity entity = itemRepository.findById(UUID.fromString(id));
        BigDecimal highestBid = highestBid(entity);
        if (bid.getAmount().compareTo(highestBid) > 0) {
            BidEntity newBid = new BidEntity();
            newBid.setAmount(bid.getAmount());
            entity.getBids().add(newBid);
        }
    }

    private Item mapToItem(ItemEntity entity) {
        return Item.builder()
                   .id(entity.getId())
                   .name(entity.getName())
                   .initialPrice(entity.getInitialPrice())
                   .highestBid(highestBid(entity))
                   .bids(entity.getBids().stream()
                               .map(bidEntity -> Bid.builder()
                                                    .id(bidEntity.getId())
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
}
