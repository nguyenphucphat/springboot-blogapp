package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BadRequestException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DataGetAllRespone findAll(Pageable pageable) {
        List<User> content = userRepository.findAll(pageable).getContent();

        DataGetAllRespone respone = new DataGetAllRespone();
        respone.setContent(content);
        respone.setPageNo(pageable.getPageNumber());
        respone.setPageSize(content.size());
        respone.setTotalElements(userRepository.count());
        respone.setTotalPages(userRepository.findAll(pageable).getTotalPages());
        respone.setLast(userRepository.findAll(pageable).isLast());

        return respone;
    }

    @Override
    public List<UserDto> findAllByRoleId(long roleId) {
        if(!roleRepository.existsById(roleId))
        {
            throw new ResourceNotFoundException("Role", "id", Long.toString(roleId));
        }

        List<User> users = userRepository.findAllByRolesId(roleId);
        List<UserDto> result = users.stream().map(this::convertToDto).collect(Collectors.toList());

        return result;
    }

    @Override
    public UserDto findById(long id) {
        if(!userRepository.existsById(id))
        {
            throw new ResourceNotFoundException("User", "id", Long.toString(id));
        }

        User user = userRepository.findById(id).get();
        return convertToDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        if(userDto.getId() != null)
        {
            throw new BadRequestException("Id must be null");
        }

        User user = convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role roleDefault = roleRepository.findByName("ROLE_USER").get();
        user.getRoles().add(roleDefault);

        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        if(!userRepository.existsById(id))
        {
            throw new ResourceNotFoundException("User", "id", Long.toString(id));
        }

        if(userDto.getId() != null)
        {
            throw new BadRequestException("Cannot change user id");
        }

        User existedUser = userRepository.findById(id).get();
        if(userDto.getName() != null)
        {
            existedUser.setName(userDto.getName());
        }

        if(userDto.getEmail() != null)
        {
            existedUser.setEmail(userDto.getEmail());
        }

        if(userDto.getUsername() != null)
        {
            existedUser.setUsername(userDto.getUsername());
        }

        if(userDto.getPassword() != null)
        {
            existedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User savedUser = userRepository.save(existedUser);

        return convertToDto(savedUser);
    }

    @Override
    public void deleteById(long id) {
        if(!userRepository.existsById(id))
        {
            throw new ResourceNotFoundException("User", "id", Long.toString(id));
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserDto addRoleToUser(long userId, long roleId) {
        if(!userRepository.existsById(userId))
        {
            throw new ResourceNotFoundException("User", "id", Long.toString(userId));
        }

        if(!roleRepository.existsById(roleId))
        {
            throw new ResourceNotFoundException("Role", "id", Long.toString(roleId));
        }

        User user = userRepository.findById(userId).get();
        Role role = roleRepository.findById(roleId).get();

        if(user.getRoles().contains(role))
        {
            throw new BadRequestException("User already has this role");
        }

        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    @Override
    public UserDto removeRoleFromUser(long userId, long roleId) {
        if(!userRepository.existsById(userId))
        {
            throw new ResourceNotFoundException("User", "id", Long.toString(userId));
        }

        if(!roleRepository.existsById(roleId))
        {
            throw new ResourceNotFoundException("Role", "id", Long.toString(roleId));
        }

        User user = userRepository.findById(userId).get();
        Role role = roleRepository.findById(roleId).get();

        if(!user.getRoles().contains(role))
        {
            throw new BadRequestException("User does not have this role");
        }

        user.getRoles().remove(role);

        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    @Override
    public UserDto convertToDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }
}
