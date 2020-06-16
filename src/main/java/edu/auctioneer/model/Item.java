package edu.auctioneer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@Builder
public class Item {

    @JsonProperty(access = READ_ONLY)
    private UUID id;

    private UUID ownerId;

    @NotNull
    @Length(min = 3, max = 30)
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal initialPrice;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal highestBid;

    @JsonProperty(access = READ_ONLY)
    private List<Bid> bids;
}
