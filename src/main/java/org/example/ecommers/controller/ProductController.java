package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.response.ProductResponse;
import org.example.ecommers.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productDto = productService.findAll();
        return ResponseEntity.ok().body(productDto);
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@RequestParam String name) {
        List<ProductResponse> search = productService.searchByName(name);
        return ResponseEntity.ok().body(search);

    }


    @GetMapping("/search/by-category")
    @CrossOrigin(origins = "http://localhost:63342")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@RequestParam String category) {
        List<ProductResponse> search = productService.searchByCategory(category);
        return ResponseEntity.ok().body(search);
    }

}
