package com.service.command.users.controller;

import com.service.command.users.dto.UserCreateDto;
import com.service.command.users.models.Users;
import com.service.command.users.models.UsersRol;
import com.service.command.users.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllers {

    @Autowired
    private UserService userService;

    @GetMapping
    private List<Users> ListAll(){
        return userService.UserList();
    }
    @PostMapping("/create")
    private ResponseEntity<?> CreateUser(@RequestBody UserCreateDto create){
        Users now = new Users();
        now.setName(create.getName());
        now.setPassword(create.getPassword());
        now.setUsername(create.getUsername());
        now.setLastName(create.getLastName());
        now.setRol(create.getRol());

        Users nowUser = userService.EmployeeRegistration(now);
        return ResponseEntity.ok(nowUser);
    }
    @GetMapping("/{id}")
    public Users getId(@PathVariable Long id){
        return userService.GetUserForId(id);
    }
    @PutMapping("rol/{id}")
    public ResponseEntity<?> ChangeRol(@PathVariable Long id, @RequestBody UsersRol newRol){
        userService.ChangeRol(id,newRol);
        return ResponseEntity.ok("Change the rol ok.");
    }
}
