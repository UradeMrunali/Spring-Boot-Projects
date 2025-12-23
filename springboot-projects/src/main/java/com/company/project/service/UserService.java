package com.company.project.service;
import com.company.project.model.User;
import com.company.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Register a new user
    public Map<String, Object> createUser(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setUserId(UUID.randomUUID());
            user.setUsername((String) payload.get("username"));
            user.setEmail((String) payload.get("email"));
            user.setPassword((String) payload.get("password"));
            user.setRole(payload.get("role") != null ? (String) payload.get("role") : "USER");
            user.setStatus(true);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            int result = userRepository.createUser(user);
            response.put("success", result > 0);
            response.put("userId", user.getUserId());
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
    // Login user
    public Map<String, Object> loginUser(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = (String) payload.get("email");
            String password = (String) payload.get("password");

            // Validate input
            if (email == null || password == null) {
                response.put("error", "Email and password are required");
                return response;
            }

            User user = userRepository.getUserByEmailAndPassword(email, password);

            if (user == null) {
                response.put("error", "Invalid email or password");
            } else {
                response.put("message", "Login successful");
                response.put("user", user);
            }

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    // Get all active users
    public Map<String, Object> getUsers(Map<String, Object> params) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userRepository.getAllUsers();
            response.put("users", users);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    // Update user details
    public Map<String, Object> updateUser(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            UUID userId = UUID.fromString((String) payload.get("user_id"));
            User user = new User();
            user.setEmail((String) payload.get("email"));
            user.setPassword((String) payload.get("password"));
            user.setStatus((Boolean) payload.getOrDefault("status", true));

            int result = userRepository.updateUser(userId, user);
            response.put("success", result > 0);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    // Deactivate a user
    public Map<String, Object> deleteUser(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            UUID userId = UUID.fromString((String) payload.get("user_id"));
            int result = userRepository.deactivateUser(userId);
            response.put("success", result > 0);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}

