package edu.auctioneer.exception;

import java.math.BigDecimal;

public class PriceToLowException extends RuntimeException {

    private final String itemId;
    private final BigDecimal requestedPrice;

    public PriceToLowException(String itemId, BigDecimal requestedPrice) {
        this.itemId = itemId;
        this.requestedPrice = requestedPrice;
    }

    @Override
    public String getMessage() {
        return String.format("Unable to place bid for item - %s, price too low - %s", itemId, requestedPrice.toString());
    }
}
