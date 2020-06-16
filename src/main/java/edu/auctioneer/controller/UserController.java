package edu.auctioneer.controller;

import edu.auctioneer.model.User;
import edu.auctioneer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<String> saveUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userService.saveUser(user));
    }
}
