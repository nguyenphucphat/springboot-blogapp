package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private Long id;
    @NotEmpty(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;
    @NotEmpty(message = "Description is required")
    @Size(min = 2, max = 250, message = "Description must be between 2 and 250 characters")
    private String description;
    @NotEmpty(message = "Content is required")
    private String content;
    private Set<CommentDto> comments;
    private Long categoryId;
}
