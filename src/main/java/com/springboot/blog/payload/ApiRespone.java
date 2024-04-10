package com.springboot.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiRespone {
    private Boolean success;

    private String message;

    private Object data;
}
