package org.example.ecommers.mapper;

import org.example.ecommers.dto.request.ProductRequest;
import org.example.ecommers.dto.response.ProductResponse;
import org.example.ecommers.entity.Product;



public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);

    ProductResponse toDto(Product product);
}
