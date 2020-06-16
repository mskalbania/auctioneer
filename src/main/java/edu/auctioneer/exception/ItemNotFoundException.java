package edu.auctioneer.exception;

public class ItemNotFoundException extends RuntimeException{

    private final String itemId;

    public ItemNotFoundException(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getMessage() {
        return "Item with id - " + itemId + "not found.";
    }
}
