/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.oauth2;

import com.ucan.skawallet.back.end.skawallet.model.UserType;
import com.ucan.skawallet.back.end.skawallet.model.Users;
import com.ucan.skawallet.back.end.skawallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
    
    @Autowired
    private UserRepository userRepository;

    // Aqui, o 'delegate' é a implementação padrão de OAuth2UserService que realiza o carregamento do usuário
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate;
    
    public CustomOAuth2UserService(OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate)
    {
        this.delegate = delegate;
    }
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        // Chama o delegate para carregar o usuário do provedor OAuth2
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Extraindo informações do usuário
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Verificando se o usuário já existe
        Users user = userRepository.findByEmail(email)
                .orElseGet(() ->
                {
                    Users newUser = new Users();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setPassword(""); // ou um valor padrão
                    newUser.setType(UserType.valueOf("USER")); // ou o tipo correspondente
                    return newUser;
                });

        // Salvando/atualizando no banco de dados
        userRepository.save(user);
        
        return oAuth2User;
    }
    
}
