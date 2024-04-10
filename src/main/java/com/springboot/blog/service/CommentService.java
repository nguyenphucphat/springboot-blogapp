package com.springboot.blog.service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.DataGetAllRespone;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto create(Long postId, CommentDto commentDto);
    CommentDto update(Long postId, Long commentId, CommentDto commentDto);
    void delete(Long postId, Long commentId);
    DataGetAllRespone findAll(Long postId, Pageable pageable);
    CommentDto findById(Long postId, Long commentId);
    CommentDto convertToDto(Comment comment);
    Comment convertToEntity(Long postId,CommentDto commentDto);
    void throwPostNotExist(Long postId);
    void throwCommentExist(Long commentId);
    void throwCommentNotExist(Long commentId);

}
