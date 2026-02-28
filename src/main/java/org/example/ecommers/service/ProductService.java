package org.example.ecommers.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CategoryDto;
import org.example.ecommers.dto.ProductDto;
import org.example.ecommers.entity.Category;
import org.example.ecommers.entity.Product;
import org.example.ecommers.exception.product.ProductAlreadyExistsException;
import org.example.ecommers.exception.product.ProductNotFoundException;
import org.example.ecommers.mapper.ProductMapperImpl;
import org.example.ecommers.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapperImpl productMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream().map(productMapper::toDto)
                .toList();
    }

    public ProductDto addProduct(ProductDto productDto) {

        if (productRepository.existsByName(productDto.name())) {
            throw new ProductAlreadyExistsException(productDto.name());
        }

        Category category = categoryService.getCategoryEntityById(productDto.CategoryId());
        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);

        Product save = productRepository.save(product);
        return productMapper.toDto(save);

    }

    public void deleteProduct(Long productId) {
       Product product = productRepository.findById(productId)
               .orElseThrow(() -> new ProductNotFoundException(productId));
       productRepository.delete(product);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (productRepository.existsByName(productDto.name())
        && !product.getName().equalsIgnoreCase(productDto.name())) {
            throw new ProductAlreadyExistsException(productDto.name());
        }
        Category category = categoryService.getCategoryEntityById(productDto.CategoryId());
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setStock(productDto.stock());
        product.setCategory(category);

        Product save = productRepository.save(product);
        return productMapper.toDto(save);

    }
    public ProductDto findById(Long id) {
      Product product= productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
      return productMapper.toDto(product);

    }

    public List<ProductDto> searchByName(String name) {
        List<ProductDto> products = productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(productMapper::toDto).toList();
        return products;

    }

    public List<ProductDto> searchByCategory(String categoryName) {
        List<ProductDto> products=productRepository.findByCategoryNameContainingIgnoreCase(categoryName)
                .stream().map(productMapper::toDto).toList();

        return products;

    }


}
