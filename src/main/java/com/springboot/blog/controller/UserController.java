package com.springboot.blog.controller;

import com.springboot.blog.payload.ApiRespone;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.service.UserService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<ApiRespone> getAllUsers(
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
        DataGetAllRespone data = userService.findAll(pageable);
        ApiRespone response = new ApiRespone(true, "success", (Object) data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<ApiRespone> getAllUsersByRoleId(@PathVariable(name="id") long roleId) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.findAllByRoleId(roleId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiRespone> getUserById(@PathVariable(name="id") long id) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.findById(id)));
    }

    @PostMapping()
    public ResponseEntity<ApiRespone> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.create(userDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiRespone> updateUser(@RequestBody UserDto userDto, @PathVariable(name="id") long id) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.update(userDto, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiRespone> deleteUser(@PathVariable(name="id") long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(new ApiRespone(true, "success", null));
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<ApiRespone> addRoleToUser(@PathVariable(name="userId") long userId, @PathVariable(name="roleId") long roleId) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.addRoleToUser(userId, roleId)));
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    public ResponseEntity<ApiRespone> removeRoleFromUser(@PathVariable(name="userId") long userId, @PathVariable(name="roleId") long roleId) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.removeRoleFromUser(userId, roleId)));
    }

}
