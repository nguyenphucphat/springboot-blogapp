package com.springboot.blog.controller;

import com.springboot.blog.payload.ApiRespone;
import com.springboot.blog.payload.DataGetAllRespone;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.service.UserService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
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

    @Operation(summary = "Get all users by role id")
    @GetMapping("role/{id}/")
    public ResponseEntity<ApiRespone> getAllUsersByRoleId(@PathVariable(name="id") long roleId) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.findAllByRoleId(roleId)));
    }

    @Operation(summary = "Get user by id")
    @GetMapping("{id}/")
    public ResponseEntity<ApiRespone> getUserById(@PathVariable(name="id") long id) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.findById(id)));
    }

    @Operation(summary = "Create user")
    @PostMapping()
    public ResponseEntity<ApiRespone> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.create(userDto)));
    }

    @Operation(summary = "Update user")
    @PutMapping("{id}/")
    public ResponseEntity<ApiRespone> updateUser(@RequestBody UserDto userDto, @PathVariable(name="id") long id) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.update(userDto, id)));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("{id}/")
    public ResponseEntity<ApiRespone> deleteUser(@PathVariable(name="id") long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(new ApiRespone(true, "success", null));
    }

    @Operation(summary = "Add role to user")
    @PutMapping("{userId}/role/{roleId}/")
    public ResponseEntity<ApiRespone> addRoleToUser(@PathVariable(name="userId") long userId, @PathVariable(name="roleId") long roleId) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.addRoleToUser(userId, roleId)));
    }

    @Operation(summary = "Remove role from user")
    @DeleteMapping("{userId}/role/{roleId}/")
    public ResponseEntity<ApiRespone> removeRoleFromUser(@PathVariable(name="userId") long userId, @PathVariable(name="roleId") long roleId) {
        return ResponseEntity.ok(new ApiRespone(true, "success", userService.removeRoleFromUser(userId, roleId)));
    }

}
