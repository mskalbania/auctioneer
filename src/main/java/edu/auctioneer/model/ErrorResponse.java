package edu.auctioneer.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorResponse {

    LocalDateTime timestamp;
    int status;
    String message;
}