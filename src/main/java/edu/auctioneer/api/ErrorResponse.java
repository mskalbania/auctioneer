package edu.auctioneer.api;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorResponse {

    LocalDateTime timestamp;
    int status;
    String message;
}