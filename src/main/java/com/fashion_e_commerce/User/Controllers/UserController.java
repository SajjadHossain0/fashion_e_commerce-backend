package com.fashion_e_commerce.User.Controllers;

import com.fashion_e_commerce.User.Entities.User;
import com.fashion_e_commerce.User.Services.UserDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserDataService userDataService;

    public UserController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userDataService.getUserById(userId));
    }


}
