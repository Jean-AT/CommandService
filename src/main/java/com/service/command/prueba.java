package com.service.command;

import com.service.command.users.models.Users;
import com.service.command.users.repository.UsersRepository;
import com.service.command.users.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.service.command.config.ConfigAcces;
@RestController
public class prueba {

    private ConfigAcces setting = new ConfigAcces();
    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;
    @GetMapping("/test-db")
    public String probarConexion() {
        // Esto lanzará una consulta SQL real: "SELECT COUNT(*) FROM users"
        long cantidad = usersRepository.count();
        return "¡Conexión Exitosa! He encontrado " + cantidad + " usuarios en la base de datos.";
    }

    @GetMapping("/generarToker/{cadena}")
    public String probarToken(@PathVariable Users cadena){
        return setting.generateToken(cadena);
    }

    @GetMapping("/get/{cadena}")
    public Claims verdatos(@PathVariable String cadena){
        return setting.getAllClaimsFromToken(cadena);
    }
}
