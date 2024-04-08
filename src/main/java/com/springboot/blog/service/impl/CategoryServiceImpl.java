package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.BadRequestException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public DataGetAllRespone findAll(Pageable pageable) {
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        List<CategoryDto> content = categories.stream().map(this::convertToDto).collect(Collectors.toList());

        DataGetAllRespone dataGetAllRespone = new DataGetAllRespone();
        dataGetAllRespone.setContent(content);
        dataGetAllRespone.setPageNo(pageable.getPageNumber());
        dataGetAllRespone.setPageSize(content.size());
        dataGetAllRespone.setTotalElements(categoryRepository.count());
        dataGetAllRespone.setTotalPages(categoryRepository.findAll(pageable).getTotalPages());
        dataGetAllRespone.setLast(categoryRepository.findAll(pageable).isLast());

        return dataGetAllRespone;
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        return convertToDto(category);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        if(categoryDto.getId() != null) {
            throw new BadRequestException("Id is not allowed in creating new category");
        }

        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return convertToDto(savedCategory);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, Long id) {
        if(categoryDto.getId() != null) {
            throw new BadRequestException("Id is not allowed in updating category");
        }

        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id.toString());
        }

        categoryDto.setId(id);
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return convertToDto(savedCategory);
    }

    @Override
    public void deleteById(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id.toString());
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto convertToDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public Category convertToEntity(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }
}
