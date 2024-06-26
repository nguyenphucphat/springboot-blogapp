package com.springboot.blog.controller;

import com.springboot.blog.payload.ApiRespone;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Get all comments by post id")
    @GetMapping("posts/{postId}/comments/")
    public ResponseEntity<ApiRespone> getAllCommentsByPostId(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT, required = false) String sort,
            @PathVariable(name = "postId") Long postId){

        String[] sortArr = sort.split(",");
        String direction = AppConstants.DEFAULT_SORT_DIRECTION;

        if(sortArr.length == 2)
        {
            direction = sortArr[1];
        }

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable =  PageRequest.of(page, size, Sort.by(dir,sortArr[0]));

        DataGetAllRespone data = commentService.findAll(postId, pageable);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get comment by id")
    @GetMapping("posts/{postId}/comments/{id}/")
    public ResponseEntity<ApiRespone> getCommentById(@PathVariable(name="postId") long postId, @PathVariable(name="id") long id) {
        CommentDto data = commentService.findById(postId, id);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create comment")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("posts/{postId}/comments/")
    public ResponseEntity<ApiRespone> createComment(@PathVariable(name="postId") long postId,@Valid @RequestBody CommentDto commentDto) {
        CommentDto data = commentService.create(postId, commentDto);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update comment")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("posts/{postId}/comments/{id}/")
    public ResponseEntity<ApiRespone> updateComment(@PathVariable(name="postId") long postId, @PathVariable(name="id") long id,@Valid @RequestBody CommentDto commentDto) {
        CommentDto data = commentService.update(postId, id, commentDto);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete comment")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("posts/{postId}/comments/{id}/")
    public ResponseEntity<ApiRespone> deleteComment(@PathVariable(name="postId") long postId, @PathVariable(name="id") long id) {
        commentService.delete(postId, id);
        ApiRespone response = new ApiRespone(true, "success", null);
        return ResponseEntity.ok(response);
    }
}
