package com.service.command.users.service;

import com.service.command.config.ConfigAcces;
import com.service.command.users.models.Users;
import com.service.command.users.models.UsersRol;
import com.service.command.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ConfigAcces setting;


    public Users EmployeeRegistration(Users user){

        if (repository.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("The username already exist " + user.getUsername());
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        return repository.save(user);
    }

    public ResponseEntity<String> Login(String username, String rawPaswrd){
        if (repository.existsUsersByUsername(username)){
            Users intro = repository.getUsersByUsername(username);
            if (passwordEncoder.matches(rawPaswrd,intro.getPassword())){
                String token = setting.generateToken(intro);
                ResponseCookie jwtCookie = ResponseCookie.from("HttpsOnly", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(60 * 60)
                        .sameSite("Lax")
                        .build();

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body("¡Login exitoso! Revisa las cookies de tu navegador.");
            }
        }else{
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        return null;
    }

    public List<Users> UserList(String validation){
        if(setting.validateToken(validation)){
            return repository.findAll();
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }


    public Users GetUserForId(Long id,String validation){
        if(setting.validateToken(validation)){
            return repository.findById(id)
                    .orElseThrow(()-> new ResolutionException("User not fount "+ id));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

    public void ChangeRol(Long id,UsersRol new_rol,String validation){
        if(setting.validateToken(validation)){
            Users employee = GetUserForId(id,validation);
            employee.setRol(new_rol);
            repository.save(employee);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }

}
