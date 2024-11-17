/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.repository.UserRepository;
import com.ucan.skawallet.back.end.skawallet.model.Users;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author azm
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService
{

    @Autowired
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG = "User With email %s not found";
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Users> ListUsers()
    {
        return userRepository.findAll();
    }

    public Users saveUser(Users user)
    {

        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<Users> getUserById(Long pkUsers)
    {
        return userRepository.findById(pkUsers);
    }

    public Optional<Users> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long pkUsers)
    {
        userRepository.deleteById(pkUsers);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public Optional<Users> findByUsername(String username)
    {
        // Lógica para buscar o usuário
        return Optional.ofNullable(userRepository.findByName(username));
    }

}
