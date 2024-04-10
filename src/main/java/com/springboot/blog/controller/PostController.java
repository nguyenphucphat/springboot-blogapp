package com.springboot.blog.controller;

import com.springboot.blog.payload.ApiRespone;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Get all posts")
    @GetMapping()
    public ResponseEntity<ApiRespone> getAllPosts(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT, required = false) String sort
    )
    {
        String[] sortArr = sort.split(",");
        String direction = AppConstants.DEFAULT_SORT_DIRECTION;

        if(sortArr.length == 2)
        {
            direction = sortArr[1];
        }

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable =  PageRequest.of(page, size, Sort.by(dir,sortArr[0]));
        DataGetAllRespone data = postService.findAll(pageable);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all posts by category id")
    @GetMapping("category/{id}/")
    public ResponseEntity<ApiRespone> getAllPostsByCategoryId(@PathVariable(name="id") long categoryId) {
        List<PostDto> data = postService.findAllByCategoryId(categoryId);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get post by id")
    @GetMapping("{id}/")
    public ResponseEntity<ApiRespone> getPostById(@PathVariable(name="id") long id) {
        PostDto data = postService.findById(id);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create post")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, WebRequest request) {
        ResponseEntity<?> response;

        PostDto data = postService.create(postDto);
        ApiRespone apiRespone = new ApiRespone(true, "success", (Object) data);
        response = new ResponseEntity<>(apiRespone, HttpStatus.CREATED);

        return response;
    }

    @Operation(summary = "Update post")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}/")
    public ResponseEntity<?> updatePost(@PathVariable(name="id") long id,@RequestBody PostDto postDto, WebRequest request) {
        ResponseEntity<?> response;

        PostDto data = postService.update(postDto, id);
        ApiRespone apiRespone = new ApiRespone(true, "success", (Object) data);
        response = new ResponseEntity<>(apiRespone, HttpStatus.OK);

        return response;
    }

    @Operation(summary = "Delete post")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}/")
    public ResponseEntity<Void> deletePost(@PathVariable(name="id") long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
