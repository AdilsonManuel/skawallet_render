/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.model.Users;
import com.ucan.skawallet.back.end.skawallet.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author azm
 */
@Service
public class UserService
{

    @Autowired
    private UserRepository userRepository;

    public Users saveUser(Users user)
    {
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
}
