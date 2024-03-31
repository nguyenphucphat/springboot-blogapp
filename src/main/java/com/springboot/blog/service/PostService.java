package com.springboot.blog.service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.PostDto;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostService {
    DataGetAllRespone findAll(Pageable pageable);
    PostDto findById(long id);

    PostDto create(PostDto postDto);

    PostDto update(PostDto postDto, long id);

    void deleteById(long id);

    PostDto convertToDto(Post post);

    Post convertToEntity(PostDto postDto);
}
