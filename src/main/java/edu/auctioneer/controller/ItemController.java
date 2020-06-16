package edu.auctioneer.controller;

import edu.auctioneer.api.Bid;
import edu.auctioneer.api.Item;
import edu.auctioneer.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAll(@RequestParam(required = false, defaultValue = "") String like) {
        LOG.debug("Get all items requested with pattern {}", like);
        return ResponseEntity.ok(itemService.getAll(like));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable String id) {
        LOG.debug("Specific item requested requested with pattern {}", id);
        return ResponseEntity.ok(itemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody @Valid Item item) {
        LOG.debug("Add item requested for item {}", item.toString());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(itemService.addItem(item));
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<?> bid(@PathVariable String id,
                                 @RequestBody @Valid Bid bid) {
        LOG.debug("Add new bid {}, requested at item {}", bid, id);
        itemService.placeBid(id, bid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}