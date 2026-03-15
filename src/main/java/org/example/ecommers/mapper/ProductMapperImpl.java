package org.example.ecommers.mapper;

import lombok.*;
import org.example.ecommers.dto.request.ProductRequest;
import org.example.ecommers.dto.response.ProductResponse;
import org.example.ecommers.entity.Product;
import org.example.ecommers.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements ProductMapper {

    private final CategoryRepository categoryRepository;

    @Override
    public Product toEntity(ProductRequest productRequest) {

//        Category category = categoryRepository.findById(productDto.CategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));


            Product product = new Product();
            product.setName(productRequest.name());
            product.setDescription(productRequest.description());
            product.setStock(productRequest.stock());
            product.setPrice(productRequest.price());
            product.setImage(productRequest.image());
        return product;


    }

    @Override
    public ProductResponse toDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getImage(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getCreatedAt()


        );
    }
}
