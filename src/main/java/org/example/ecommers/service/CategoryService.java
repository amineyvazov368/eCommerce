package org.example.ecommers.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CategoryDto;
import org.example.ecommers.entity.Category;
import org.example.ecommers.exception.category.CategoryAlreadyExistsException;
import org.example.ecommers.exception.category.CategoryNotFoundException;
import org.example.ecommers.mapper.CategoryMapper;
import org.example.ecommers.mapper.CategoryMapperImpl;
import org.example.ecommers.repository.CategoryRepository;
import org.example.ecommers.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapperImpl categoryMapper;
    private final ProductRepository productRepository;

    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.name())) {
            throw new CategoryAlreadyExistsException(categoryDto.name());
        }

        Category category = categoryMapper.toEntity(categoryDto);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(categoryMapper::toDto)
                .toList();
    }


    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.toDto(category);


    }

    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<CategoryDto> searchCategory(String categoryName) {
        return categoryRepository.findByNameContainingIgnoreCase(categoryName)
                .stream().map(categoryMapper::toDto)
                .toList();



    }

    public CategoryDto updateCategory(Long id,CategoryDto categoryDto) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        if (categoryRepository.existsByName(categoryDto.name())
                && !category.getName()
                .equalsIgnoreCase(categoryDto.name())) {
            throw new CategoryAlreadyExistsException(categoryDto.name());
        }
        category.setName(categoryDto.name());
        return categoryMapper.toDto(categoryRepository.save(category));

    }

    public void deleteCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        boolean hasProducts =
                productRepository.existsByCategoryId(id);

        if (hasProducts) {
            throw new RuntimeException(
                    "Category cannot be deleted because it has products");
        }

        categoryRepository.delete(category);
    }





}
