package com.company.project.service;

import com.company.project.model.Category;
import com.company.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // CREATE
    public Category createCategory(String name, UUID userId) {
        Category category = new Category();
        category.setName(name);
        category.setUserId(userId);
        return categoryRepository.save(category);
    }

    // READ - Get ALL categories (no filter)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // READ - Get categories for specific user
    public List<Category> getCategoriesByUserId(UUID userId) {
        return categoryRepository.findByUserId(userId);
    }

    // READ - Get single category
    public Category getCategoryById(UUID categoryId, UUID userId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            return null;
        }
        return category;
    }

    // READ - Search all categories
    public List<Category> searchCategories(String name) {
        return categoryRepository.searchByName(name);
    }

    // READ - Search categories for specific user
    public List<Category> searchCategories(UUID userId, String name) {
        return categoryRepository.searchByName(userId, name);
    }

    // UPDATE
    public Category updateCategory(UUID categoryId, String name, UUID userId) {
        int updated = categoryRepository.update(categoryId, name, userId);
        if (updated > 0) {
            return categoryRepository.findById(categoryId);
        }
        return null;
    }

    // DELETE
    public boolean deleteCategory(UUID categoryId, UUID userId) {
        int deleted = categoryRepository.delete(categoryId, userId);
        return deleted > 0;
    }

    // Verify ownership (for task validation)
    public boolean verifyCategoryOwnership(UUID categoryId, UUID userId) {
        if (categoryId == null) return true;
        return categoryRepository.belongsToUser(categoryId, userId);
    }
}