package com.jobs.jobportal.controller;

import com.jobs.jobportal.DTO.ThresholdDTO;
import com.jobs.jobportal.model.User;
import com.jobs.jobportal.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User user) {
        User saved = userService.signup(user);
        String token = userService.generateToken(saved);

        return Map.of(
            "token", token,
            "userId", saved.getId(),
            "username", saved.getUserName()
        );
    }


    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        User found = userService.login(user);
        String token = userService.generateToken(found);

        return Map.of(
            "token", token,
            "userId", found.getId(),
            "username", found.getUserName()
        );
    }

    @GetMapping("/{userId}/threshold")
    public Map<String, Integer> fetchThreshold(@PathVariable Long userId) {
        Integer threshold = userService.getatsThreshold(userId);
        return Map.of("threshold", threshold);
    }
    

    @PutMapping("/{userId}/threshold")
    public Integer putThreshold(@PathVariable Long userId, @RequestBody ThresholdDTO dto) {
        return userService.putThreshold(userId, dto.getThreshold());
    }
}
