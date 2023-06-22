package com.springboot.blog.service;

import com.springboot.blog.dto.CategoryDto;
import com.springboot.blog.dto.requests.CreateCategoryRequest;
import com.springboot.blog.dto.requests.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CreateCategoryRequest categoryRequest);
    CategoryDto getCategory(Long id);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(UpdateCategoryRequest categoryRequest, Long id);
    void deleteCategory(Long id);
}
