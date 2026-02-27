package org.example.ecommers.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.ProductDto;
import org.example.ecommers.mapper.ProductMapper;
import org.example.ecommers.service.CategoryService;
import org.example.ecommers.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;


    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
       List<ProductDto> productDto= productService.findAll();
       return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto productDto1 = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                    @RequestBody @Valid ProductDto productDto) {
         productService.updateProduct(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }
}
