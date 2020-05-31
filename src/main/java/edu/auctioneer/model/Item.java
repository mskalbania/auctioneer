package edu.auctioneer.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Item {

    private UUID id;

    @NotNull
    @Length(min = 3, max = 30)
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal initialPrice;

    private BigDecimal highestBid;

    private List<Bid> bids;
}
