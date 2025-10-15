package com.company.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Model class representing a User.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID userId;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean status;
    private Timestamp createdAt;

    public boolean isIsActive() {
        return status;
    }

    public void setIsActive(boolean isActive) {
        this.status = isActive;
    }
}