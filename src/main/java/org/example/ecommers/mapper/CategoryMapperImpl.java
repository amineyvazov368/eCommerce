package org.example.ecommers.mapper;

import org.example.ecommers.dto.response.CategoryDto;
import org.example.ecommers.entity.Category;
import org.springframework.stereotype.Component;

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
