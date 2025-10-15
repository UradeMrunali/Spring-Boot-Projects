package com.company.project.repository;

import com.company.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Repository for User CRUD operations using JdbcTemplate.
 */
@Repository
public class UserRepository {

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(UUID.fromString(rs.getString("user_id")));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setStatus(rs.getBoolean("status"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            return user;
        }
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createUser(User user) {
        String sql = "INSERT INTO users (user_id, username, email, password, role, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isStatus(),
                user.getCreatedAt()
        );
    }

    public User getUserById(UUID userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND status = true";
        List<User> users = jdbcTemplate.query(sql, new Object[]{userId.toString()}, userRowMapper);
        return users.isEmpty() ? null : users.get(0);
    }

    public int updateUser(UUID userId, User user) {
        String sql = "UPDATE users SET email = ?, password = ?, status = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.isStatus(),
                userId.toString()
        );
    }

    public User getUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND status = true";
        List<User> users = jdbcTemplate.query(sql, new Object[]{email, password}, userRowMapper);
        return users.isEmpty() ? null : users.get(0);
    }

    public int deactivateUser(UUID userId) {
        String sql = "UPDATE users SET status = false WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId.toString());
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users WHERE status = true AND role = 'USER'";
        return jdbcTemplate.query(sql, userRowMapper);
    }
}