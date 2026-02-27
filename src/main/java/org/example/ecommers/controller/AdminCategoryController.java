package org.example.ecommers.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CategoryDto;
import org.example.ecommers.entity.Category;
import org.example.ecommers.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto categoryDto1 = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto1);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
      List<CategoryDto> categoryDto=  categoryService.getAllCategories();
      return ResponseEntity.ok().body(categoryDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchCategoryByName(@RequestParam String name) {
        List<CategoryDto> categoryDto = categoryService.searchCategory(name);
        return ResponseEntity.ok().body(categoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto updated =
                categoryService.updateCategory(id, categoryDto);

        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }





}
