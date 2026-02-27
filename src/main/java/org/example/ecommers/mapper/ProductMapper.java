package org.example.ecommers.mapper;

import org.example.ecommers.dto.ProductDto;
import org.example.ecommers.entity.Product;



public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);
}
