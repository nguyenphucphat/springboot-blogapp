package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BadRequestException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private PostService postService;

    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(PostService postService, PostRepository postRepository, CommentRepository commentRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto create(Long postId, CommentDto commentDto) {
        throwPostNotExist(postId);
        throwCommentExist(commentDto.getId());

        Comment comment = convertToEntity(postId, commentDto);

        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    @Override
    public CommentDto update(Long postId, Long commentId, CommentDto commentDto) {
        throwPostNotExist(postId);
        throwCommentNotExist(commentId);

        Comment comment = convertToEntity(postId, commentDto);

        Comment existingComment = commentRepository.findById(commentId).get();

        if(comment.getName() != null)
        {
            existingComment.setName(comment.getName());
        }

        if(comment.getEmail() != null)
        {
            existingComment.setEmail(comment.getEmail());
        }

        if(comment.getBody() != null)
        {
            existingComment.setBody(comment.getBody());
        }

        Comment savedComment = commentRepository.save(existingComment);

        return convertToDto(savedComment);
    }

    @Override
    public void delete(Long postId, Long commentId) {
        throwPostNotExist(postId);
        throwCommentNotExist(commentId);

        commentRepository.deleteById(commentId);
    }

    @Override
    public DataGetAllRespone findAll(Long postId, Pageable pageable) {
        throwPostNotExist(postId);

        List<Comment> comments = commentRepository.findAll(pageable).getContent();

        List<CommentDto> commentDtos = comments.stream().map(this::convertToDto).toList();

        DataGetAllRespone dataGetAllRespone = new DataGetAllRespone();

        dataGetAllRespone.setContent(commentDtos);
        dataGetAllRespone.setPageNo(pageable.getPageNumber());
        dataGetAllRespone.setPageSize(commentDtos.size());
        dataGetAllRespone.setTotalElements(commentRepository.count());
        dataGetAllRespone.setTotalPages(commentRepository.findAll(pageable).getTotalPages());
        dataGetAllRespone.setLast(commentRepository.findAll(pageable).isLast());

        return dataGetAllRespone;
    }

    @Override
    public CommentDto findById(Long postId, Long commentId) {
        throwPostNotExist(postId);
        throwCommentNotExist(commentId);

        Comment comment = commentRepository.findById(commentId).get();

        return convertToDto(comment);
    }

    @Override
    public CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    @Override
    public Comment convertToEntity(Long postId, CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Optional<Post> post = postRepository.findById(postId);

        comment.setPost(post.get());

        return comment;
    }

    @Override
    public void throwPostNotExist(Long postId) {
        if(!postRepository.existsById(postId))
        {
            throw new ResourceNotFoundException("Post", "id", Long.toString(postId));
        }
    }

    @Override
    public void throwCommentExist(Long commentId) {
        if(commentRepository.existsById(commentId))
        {
            throw new BadRequestException("Comment with id " + commentId + " already exists");
        }
    }

    @Override
    public void throwCommentNotExist(Long commentId) {
        if(!commentRepository.existsById(commentId))
        {
            throw new ResourceNotFoundException("Comment", "id", Long.toString(commentId));
        }
    }

}
