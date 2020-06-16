package edu.auctioneer.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@Builder
public class User {

    @JsonProperty(access = READ_ONLY)
    private UUID id;

    @NotNull
    @Length(min = 3, max = 10)
    private String name;

    @JsonProperty(access = READ_ONLY)
    private List<Bid> bids;

    @JsonProperty(access = READ_ONLY)
    private List<Item> items;
}
