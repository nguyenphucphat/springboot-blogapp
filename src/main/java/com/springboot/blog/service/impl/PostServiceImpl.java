package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BadRequestException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    CategoryRepository categoryRepository;
    ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository,
                           ModelMapper mapper,
                           CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DataGetAllRespone findAll(Pageable pageable) {
        List<Post> posts = postRepository.findAll(pageable).getContent();
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());

        DataGetAllRespone dataGetAllRespone = new DataGetAllRespone();
        dataGetAllRespone.setContent(postDtos);
        dataGetAllRespone.setPageNo(pageable.getPageNumber());
        dataGetAllRespone.setPageSize(postDtos.size());
        dataGetAllRespone.setTotalElements(postRepository.count());
        dataGetAllRespone.setTotalPages(postRepository.findAll(pageable).getTotalPages());
        dataGetAllRespone.setLast(postRepository.findAll(pageable).isLast());

        return dataGetAllRespone;
    }

    @Override
    public List<PostDto> findAllByCategoryId(long categoryId) {
        if(!categoryRepository.existsById(categoryId))
        {
            throw new ResourceNotFoundException("Category", "id", Long.toString(categoryId));
        }

        List<Post> posts = postRepository.findAllByCategoryId(categoryId);
        List<PostDto> result = posts.stream().map(this::convertToDto).collect(Collectors.toList());

        return result;
    }

    @Override
    public PostDto findById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("Post", "id", Long.toString(id));
        });

        return convertToDto(post);
    }

    @Override
    public PostDto create(PostDto postDto) {
        if(postDto.getId() != null) {
            throw new BadRequestException("Id is not allowed in creating new post");
        }

        if(!categoryRepository.existsById(postDto.getCategoryId()))
        {
            throw new ResourceNotFoundException("Category", "id", Long.toString(postDto.getCategoryId()));
        }

        Post post = convertToEntity(postDto);
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    @Override
    public PostDto update(PostDto postDto, long id) {

        if(postDto.getId() != null){
            throw new BadRequestException("Id is not allowed in updating post");
        }

        Post existingPost = postRepository.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("Post", "id", Long.toString(id));
        });

        if(postDto.getCategoryId() != null && !categoryRepository.existsById(postDto.getCategoryId()))
        {
            throw new ResourceNotFoundException("Category", "id", Long.toString(postDto.getCategoryId()));
        }
        else{
            existingPost.setCategory(categoryRepository.findById(postDto.getCategoryId()).get());
        }

        Post post = convertToEntity(postDto);

        // update with fields not null
        if(post.getTitle() != null)
        {
            existingPost.setTitle(post.getTitle());
        }
        if(post.getDescription() != null)
        {
            existingPost.setDescription(post.getDescription());
        }
        if(post.getContent() != null)
        {
            existingPost.setContent(post.getContent());
        }

        Post savedPost = postRepository.save(existingPost);

        return convertToDto(savedPost);
    }

    @Override
    public void deleteById(long id) {
        if(!postRepository.existsById(id))
        {
            throw new ResourceNotFoundException("Post", "id", Long.toString(id));
        }
        else
        {
            postRepository.deleteById(id);
        }
    }

    @Override
    public PostDto convertToDto(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());

        return postDto;
    }

    @Override
    public Post convertToEntity(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
//        post.setId(postDto.getId());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        return post;
    }
}
