package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.ProductDto;
import org.example.ecommers.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDto= productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<List<ProductDto>> getProductsByName(@RequestParam String name) {
       List<ProductDto> search= productService.searchByName(name);
        return ResponseEntity.ok().body(search);

    }

    @GetMapping("/search/by-category")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@RequestParam String category) {
        List<ProductDto> search= productService.searchByCategory(category);
        return ResponseEntity.ok().body(search);
    }

}
