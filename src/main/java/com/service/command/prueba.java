package com.service.command;

import com.service.command.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class prueba {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/test-db")
    public String probarConexion() {
        // Esto lanzará una consulta SQL real: "SELECT COUNT(*) FROM users"
        long cantidad = usersRepository.count();
        return "¡Conexión Exitosa! He encontrado " + cantidad + " usuarios en la base de datos.";
    }
}
