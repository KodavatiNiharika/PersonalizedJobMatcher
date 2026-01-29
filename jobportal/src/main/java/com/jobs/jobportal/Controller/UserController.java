package com.jobs.jobportal.Controller;
import com.jobs.jobportal.Security.JwtUtil;
import org.springframework.web.bind.annotation.*;
import com.jobs.jobportal.User;
import com.jobs.jobportal.UserRepo;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    public UserController(UserRepo userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userRepo.save(user);  // save user to DB
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        // simple login logic
        User found = userRepo.findAll()
            .stream()
            .filter(u -> u.getEmail().equals(user.getEmail()) && u.getPassword().equals(user.getPassword()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        String token = jwtUtil.generateToken(found.getEmail());
        return Map.of("token", token);
    }
}
