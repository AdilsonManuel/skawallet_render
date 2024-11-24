/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.config;

import com.ucan.skawallet.back.end.skawallet.security.token.JwtRequestFilter;
import com.ucan.skawallet.back.end.skawallet.service.UserService;
import lombok.AllArgsConstructor;
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


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig
{

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Configuração de autorização
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable()) // Desabilitar CSRF, se necessário
                .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/*/registration/**").permitAll() // Permitir acesso a essas rotas
                .requestMatchers("/api/*/banks/**").permitAll() // Permitir acesso a essas rotas
                .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição
                )
                .formLogin(form -> form
                .loginPage("/login") // Define explicitamente a URL para a página de login
                .permitAll() // Permite acesso à página de login sem autenticação
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll() // Permite logout sem autenticação
                )
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Para segurança sem estado (Stateless)
                )
                .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Página de login personalizada (se necessário)
                .defaultSuccessUrl("/home", true) // Redirecionamento após sucesso no login
                .permitAll() // Permite acesso ao login via OAuth2 sem autenticação
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT antes do filtro de autenticação padrão

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
