package org.example.ecommers.mapper;

import org.example.ecommers.dto.CategoryDto;
import org.example.ecommers.entity.Category;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.id());
        category.setName(categoryDto.name());
        return category;
    }

    @Override
    public CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
