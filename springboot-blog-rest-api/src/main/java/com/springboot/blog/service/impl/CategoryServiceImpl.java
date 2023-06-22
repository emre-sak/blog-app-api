package com.springboot.blog.service.impl;

import com.springboot.blog.dto.requests.CreateCategoryRequest;
import com.springboot.blog.dto.requests.UpdateCategoryRequest;
import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.dto.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import com.springboot.blog.utils.mappers.ModelMapperService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapperService modelMapper;

    @Override
    public CategoryDto addCategory(CreateCategoryRequest categoryRequest) {

        Category category = modelMapper.forRequest().map(categoryRequest, Category.class);

        Category savedCategory = this.categoryRepository.save(category);

        CategoryDto responseCategory = modelMapper.forResponse().map(savedCategory, CategoryDto.class);

        return responseCategory;
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id)
        );

        CategoryDto responseCategory = modelMapper.forResponse().map(category, CategoryDto.class);

        return responseCategory;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryDto> categoriesDto =  categories.stream()
                .map(category -> modelMapper.forResponse().map(category, CategoryDto.class))
                .toList();
        return categoriesDto;
    }

    @Override
    public CategoryDto updateCategory(UpdateCategoryRequest categoryRequest, Long id) {

        Category category = this.categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id)
        );

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category updatedCategory = this.categoryRepository.save(category);

        CategoryDto responseCategory = modelMapper.forResponse().map(updatedCategory, CategoryDto.class);

        return responseCategory;
    }

    @Override
    public void deleteCategory(Long id) {

        CategoryDto categoryDto = getCategory(id);

        Category category = modelMapper.forResponse().map(categoryDto, Category.class);

        categoryRepository.delete(category);
    }


}
