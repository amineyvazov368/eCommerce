package org.example.ecommers.mapper;

import lombok.*;
import org.example.ecommers.dto.ProductDto;
import org.example.ecommers.entity.Category;
import org.example.ecommers.entity.Product;
import org.example.ecommers.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements ProductMapper {

    private final CategoryRepository categoryRepository;

    @Override
    public Product toEntity(ProductDto productDto) {

//        Category category = categoryRepository.findById(productDto.CategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));


        Product product = new Product();
        product.setId(productDto.id());
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setStock(productDto.stock());
        product.setPrice(productDto.price());
        product.setImage(productDto.image());
        return product;


    }

    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getImage(),
                product.getCategory() != null ? product.getCategory().getId() : null


        );
    }
}
