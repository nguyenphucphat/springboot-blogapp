package com.springboot.blog.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String email;
    private String name;
    private String body;
}
