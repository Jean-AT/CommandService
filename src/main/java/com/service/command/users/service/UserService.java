package com.service.command.users.service;

import com.service.command.users.models.Users;
import com.service.command.users.models.UsersRol;
import com.service.command.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;


    public Users EmployeeRegistration(Users user){

        if (repository.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("The username already exist " + user.getUsername());
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        return repository.save(user);
    }


    public List<Users> UserList(){
        return repository.findAll();
    }


    public Users GetUserForId(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new ResolutionException("User not fount "+ id));
    }

    public void ChangeRol(Long id,UsersRol new_rol){
        Users employee = GetUserForId(id);
        employee.setRol(new_rol);
        repository.save(employee);
    }
}
