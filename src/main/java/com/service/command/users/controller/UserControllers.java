package com.service.command.users.controller;

import com.service.command.config.ConfigAcces;
import com.service.command.products.models.ProductsCategory;
import com.service.command.users.dto.UserCreateDto;
import com.service.command.users.models.Users;
import com.service.command.users.models.UsersRol;
import com.service.command.users.repository.UsersRepository;
import com.service.command.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllers {

    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ConfigAcces setting;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String raw){
        ResponseEntity<String> respuesta = userService.Login(username, raw);
        if (respuesta == null) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }
        return respuesta;
    }
    @PostMapping("/create")
    public ResponseEntity<?> CreateUser(@RequestBody UserCreateDto create) {
        Users now = new Users();
        now.setName(create.getName());
        now.setPassword(create.getPassword());
        now.setUsername(create.getUsername());
        now.setLastName(create.getLastName());
        now.setRol(create.getRol());

        Users nowUser = userService.EmployeeRegistration(now);
        return ResponseEntity.ok(nowUser);
    }
    @GetMapping("/list")
    public List<Users> ListAll(@CookieValue(name = "HttpsOnly", required = false) String validation){
        return userService.UserList(validation);
    }
    @GetMapping("/{id}")
    public Users getId(@PathVariable Long id,@CookieValue(name = "HttpsOnly", required = false) String validation){
        return userService.GetUserForId(id,validation);
    }
    @PutMapping("rol/{id}")
    public ResponseEntity<?> ChangeRol(@PathVariable Long id, @RequestBody UsersRol newRol,@CookieValue(name = "HttpsOnly", required = false) String validation){
        userService.ChangeRol(id,newRol,validation);
        return ResponseEntity.ok("Change the rol ok.");
    }
}
