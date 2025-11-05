package com.company.project.controller;

import com.company.project.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(value = "User Management System", tags = "User API")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = userService.createUser(payload);
        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Login user")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = userService.loginUser(payload);
        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Retrieve user(s)")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam Map<String, Object> params) {
        Map<String, Object> response = userService.getUsers(params);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Update a user")
    @PutMapping("/update_user")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = userService.updateUser(payload);
        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Deactivate a user")
    @DeleteMapping("/delete_user")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = userService.deleteUser(payload);
        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}
