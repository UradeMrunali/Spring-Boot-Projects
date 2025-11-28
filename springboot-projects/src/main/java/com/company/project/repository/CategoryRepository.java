package com.company.project.repository;
import com.company.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class CategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper
    private final RowMapper<Category> rowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setCategoryId(UUID.fromString(rs.getString("category_id")));
        category.setName(rs.getString("name"));
        category.setUserId(UUID.fromString(rs.getString("user_id")));
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            category.setCreatedAt(timestamp.toLocalDateTime());
        }
        return category;
    };

    // CREATE
    public Category save(Category category) {
        String sql = "INSERT INTO categories (category_id, name, user_id) VALUES (?, ?, ?)";

        if (category.getCategoryId() == null) {
            category.setCategoryId(UUID.randomUUID());
        }
        jdbcTemplate.update(sql,
                category.getCategoryId(),
                category.getName(),
                category.getUserId()
        );
        return findById(category.getCategoryId());
    }
    // READ - Find ALL categories (no user filter)
    public List<Category> findAll() {
        String sql = "SELECT * FROM categories ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // READ - Find all by user
    public List<Category> findByUserId(UUID userId) {
        String sql = "SELECT * FROM categories WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    // READ - Find by ID
    public Category findById(UUID categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        List<Category> categories = jdbcTemplate.query(sql, rowMapper, categoryId);
        return categories.isEmpty() ? null : categories.get(0);
    }

    // READ - Search by name (all users)
    public List<Category> searchByName(String name) {
        String sql = "SELECT * FROM categories WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, rowMapper, "%" + name + "%");
    }

    // READ - Search by name (specific user)
    public List<Category> searchByName(UUID userId, String name) {
        String sql = "SELECT * FROM categories WHERE user_id = ? AND LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, rowMapper, userId, "%" + name + "%");
    }

    // UPDATE
    public int update(UUID categoryId, String name, UUID userId) {
        String sql = "UPDATE categories SET name = ? WHERE category_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, name, categoryId, userId);
    }

    // DELETE
    public int delete(UUID categoryId, UUID userId) {
        String sql = "DELETE FROM categories WHERE category_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, categoryId, userId);
    }

    // Check if belongs to user
    public boolean belongsToUser(UUID categoryId, UUID userId) {
        String sql = "SELECT COUNT(*) FROM categories WHERE category_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId, userId);
        return count != null && count > 0;
    }
}

