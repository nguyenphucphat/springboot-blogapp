package com.springboot.blog.controller;

import com.springboot.blog.payload.ApiRespone;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.service.CategoryService;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<ApiRespone> getAllCategories(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT, required = false) String sort
    ) {
        String[] sortArr = sort.split(",");
        String direction = AppConstants.DEFAULT_SORT_DIRECTION;

        if(sortArr.length == 2)
        {
            direction = sortArr[1];
        }

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable =  PageRequest.of(page, size, Sort.by(dir,sortArr[0]));

        DataGetAllRespone data = categoryService.findAll(pageable);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiRespone> getCategoryById(@PathVariable(name = "id") Long id) {
        CategoryDto category = categoryService.findById(id);
        ApiRespone response = new ApiRespone(true, "success", (Object) category);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ApiRespone> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.create(categoryDto);
        ApiRespone response = new ApiRespone(true, "success", (Object) category);

        ResponseEntity<ApiRespone> responseEntity = new ResponseEntity<>(response, HttpStatus.CREATED);

        return responseEntity;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiRespone> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable(name = "id") Long id) {
        CategoryDto category = categoryService.update(categoryDto, id);
        ApiRespone response = new ApiRespone(true, "success", (Object) category);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
