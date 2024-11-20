/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Component
public class JwtRequestFilter extends OncePerRequestFilter
{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil)
    {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
// Obter o cabeçalho de autorização
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Verificar se o cabeçalho de autorização está presente e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            jwt = authorizationHeader.substring(7);  // Extrai o token JWT
            username = jwtUtil.extractUsername(jwt);  // Extraí o username do JWT
        }

        // Verificar se o username é válido e se a autenticação não foi configurada ainda
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            // Carregar os detalhes do usuário usando o UserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validar o token JWT
            if (jwtUtil.validateToken(jwt, username))
            {
                // Criar um objeto de autenticação com os detalhes do usuário e as autoridades
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Adicionar detalhes adicionais à autenticação
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Definir o contexto de autenticação
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Prosseguir com o filtro
        chain.doFilter(request, response);
    }
}
