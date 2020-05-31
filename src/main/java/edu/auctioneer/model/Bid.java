package edu.auctioneer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.*;

@Data
@Builder
public class Bid {

    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;

    @JsonProperty(access = Access.READ_ONLY)
    private LocalDateTime createdOn;
}
