package org.example.ecommers.mapper;

import org.example.ecommers.dto.CategoryDto;
import org.example.ecommers.entity.Category;
import org.springframework.stereotype.Component;

@Component
public interface CategoryMapper {

    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);
}
