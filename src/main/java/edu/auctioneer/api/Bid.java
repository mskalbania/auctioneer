package edu.auctioneer.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@Builder
public class Bid {

    @JsonProperty(access = READ_ONLY)
    private UUID id;

    private UUID bidderId;

    @JsonProperty(access = READ_ONLY)
    private UUID targetItemId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;

    @JsonProperty(access = READ_ONLY)
    private LocalDateTime createdOn;
}
