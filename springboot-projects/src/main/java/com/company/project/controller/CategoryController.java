package com.company.project.controller;

import com.company.project.model.Category;
import com.company.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Default user ID (fallback)
    private static final String DEFAULT_USER_ID = "4a7e6c8d-9f2b-4e1a-8c3d-5f9a2b7e4c1d"; // CHANGE THIS

    // Helper method to get userId (optional)
    private UUID getUserIdOptional(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.isEmpty()) {
            return UUID.fromString(DEFAULT_USER_ID);
        }
        return UUID.fromString(userIdHeader);
    }

    // CREATE - POST /api/categories
    @PostMapping
    public ResponseEntity<?> createCategory(
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "User-Id", required = false) String userIdHeader) {

        String name = request.get("name");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required");
        }

        try {
            UUID userId = getUserIdOptional(userIdHeader);
            Category category = categoryService.createCategory(name, userId);
            return new ResponseEntity<>(category, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid User-Id format");
        }
    }

    // READ - GET /api/categories (ALL categories or filtered by user)
    @GetMapping
    public ResponseEntity<?> getAllCategories(
            @RequestHeader(value = "User-Id", required = false) String userIdHeader) {

        try {
            List<Category> categories;

            // If User-Id header provided, filter by user
            if (userIdHeader != null && !userIdHeader.isEmpty()) {
                UUID userId = UUID.fromString(userIdHeader);
                categories = categoryService.getCategoriesByUserId(userId);
            } else {
                // No header = get ALL categories from all users
                categories = categoryService.getAllCategories();
            }

            return ResponseEntity.ok(categories);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid User-Id format");
        }
    }

    // READ - GET /api/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable String id,
            @RequestHeader(value = "User-Id", required = false) String userIdHeader) {

        try {
            UUID categoryId = UUID.fromString(id);
            UUID userId = getUserIdOptional(userIdHeader);

            Category category = categoryService.getCategoryById(categoryId, userId);

            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category not found or access denied");
            }

            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format");
        }
    }

    // READ - GET /api/categories/search?name=work
    @GetMapping("/search")
    public ResponseEntity<?> searchCategories(
            @RequestParam String name,
            @RequestHeader(value = "User-Id", required = false) String userIdHeader) {

        try {
            List<Category> categories;

            // If User-Id header provided, filter by user
            if (userIdHeader != null && !userIdHeader.isEmpty()) {
                UUID userId = UUID.fromString(userIdHeader);
                categories = categoryService.searchCategories(userId, name);
            } else {
                // No header = search ALL categories
                categories = categoryService.searchCategories(name);
            }

            return ResponseEntity.ok(categories);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid User-Id format");
        }
    }

    // UPDATE - PUT /api/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable String id,
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "User-Id", required = false) String userIdHeader) {

        String name = request.get("name");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required");
        }

        try {
            UUID categoryId = UUID.fromString(id);
            UUID userId = getUserIdOptional(userIdHeader);

            Category updated = categoryService.updateCategory(categoryId, name, userId);

            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category not found or access denied");
            }

            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format");
        }
    }

    // DELETE - DELETE /api/categories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable String id,
            @RequestHeader(value = "User-Id", required = false) String userIdHeader) {

        try {
            UUID categoryId = UUID.fromString(id);
            UUID userId = getUserIdOptional(userIdHeader);

            boolean deleted = categoryService.deleteCategory(categoryId, userId);

            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category not found or access denied");
            }

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format");
        }
    }
}