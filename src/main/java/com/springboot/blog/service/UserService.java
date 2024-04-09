package com.springboot.blog.service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    DataGetAllRespone findAll(Pageable pageable);

    List<UserDto> findAllByRoleId(long roleId);

    UserDto findById(long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long id);

    void deleteById(long id);

    UserDto addRoleToUser(long userId, long roleId);

    UserDto removeRoleFromUser(long userId, long roleId);

    UserDto convertToDto(User user);

    User convertToEntity(UserDto userDto);
}
