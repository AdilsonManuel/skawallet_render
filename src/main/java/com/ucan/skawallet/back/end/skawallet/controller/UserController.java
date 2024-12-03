/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.dto.LoginRequestDTO;
import com.ucan.skawallet.back.end.skawallet.model.Users;
import com.ucan.skawallet.back.end.skawallet.service.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/*/users")
@AllArgsConstructor
public class UserController
{

    @Autowired
    private final UserService userService;

    // 1. Criar um novo utilizador
    @PostMapping("/registration")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode criar utilizadores
    public ResponseEntity<?> registUser(@RequestBody Users user)
    {
        if (user.getName() == null || user.getName().isEmpty())
        {
            return new ResponseEntity<>("Username is required", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty())
        {
            return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
        }

        try
        {
            Users newUser = userService.saveUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Obter utilizador por ID
    @GetMapping("/{pk_users}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #pk_users == authentication.principal.id)")
    public ResponseEntity<?> getUserById(@PathVariable("pk_users") Long pk_users)
    {
        Optional<Users> user = userService.getUserById(pk_users);
        if (user.isPresent())
        {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // 3. Obter todos os utilizadores
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode listar todos os utilizadores
    public ResponseEntity<List<Users>> getAllUsers()
    {
        List<Users> users = userService.ListUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 4. Actualizar utilizador
    @PatchMapping("/{pk_users}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #pk_users == authentication.principal.id)")
    public ResponseEntity<?> updateUser(@PathVariable Long pk_users, @RequestBody Users user)
    {
        Optional<Users> existingUser = userService.getUserById(pk_users);
        if (existingUser.isPresent())
        {
            Users updatedUser = existingUser.get();
            updatedUser.setName(user.getName() != null ? user.getName() : updatedUser.getName());
            updatedUser.setPassword(user.getPassword() != null ? user.getPassword() : updatedUser.getPassword());

            try
            {
                userService.saveUser(updatedUser);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            }
            catch (Exception e)
            {
                return new ResponseEntity<>("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
        {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // 5. Deletar utilizador
    @DeleteMapping("/{pk_users}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode deletar utilizadores
    public ResponseEntity<?> deleteUser(@PathVariable Long pk_users)
    {
        Optional<Users> user = userService.getUserById(pk_users);
        if (user.isPresent())
        {
            userService.deleteUser(pk_users);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}
