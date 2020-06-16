package edu.auctioneer.controller;

import edu.auctioneer.model.Bid;
import edu.auctioneer.model.Item;
import edu.auctioneer.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAll(@RequestParam(required = false, defaultValue = "") String like) {
        return ResponseEntity.ok(itemService.getAll(like));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody @Valid Item item) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(itemService.addItem(item));
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<?> bid(@PathVariable String id,
                                 @RequestBody @Valid Bid bid) {
        itemService.placeBid(id, bid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}