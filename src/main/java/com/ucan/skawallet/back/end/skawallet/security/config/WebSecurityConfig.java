/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.config;

import com.ucan.skawallet.back.end.skawallet.security.token.JwtRequestFilter;
import com.ucan.skawallet.back.end.skawallet.security.token.JwtUtil;
import com.ucan.skawallet.back.end.skawallet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author azm
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig
{

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final JwtUtil JwtUtil;

    // Configuração de autorização
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable()) // Desabilitar CSRF, se necessário
                .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/*/registration/**").permitAll() // Permitir acesso a essas rotas
                .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição
                )
                .formLogin(form -> form
                .loginPage("/login") // Define explicitamente a URL para a página de login
                .permitAll() // Permite acesso à página de login sem autenticação
                )
                .securityContext(securityContext -> securityContext.requireExplicitSave(true)) // Para segurança sem estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Alternativa para sessionManagement()
                .addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT

        return http.build();
    }

    // Configuração do AuthenticationManagerBuilder
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    // Configuração do DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder); // Configura o encoder de senha
        daoAuthenticationProvider.setUserDetailsService(userService); // Configura o UserDetailsService
        return daoAuthenticationProvider;
    }

}
