package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.DataGetAllRespone;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    DataGetAllRespone findAll(Pageable pageable);
    CategoryDto findById(Long id);
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto, Long id);
    void deleteById(Long id);
    CategoryDto convertToDto(Category category);
    Category convertToEntity(CategoryDto categoryDto);
}
