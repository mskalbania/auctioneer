package edu.auctioneer.exception;

public class UserNotFoundException extends RuntimeException {

    private final String userId;

    public UserNotFoundException(String userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "User with id - " + userId + " not found.";
    }
}
