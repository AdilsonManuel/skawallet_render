/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.oauth2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OAuthController
{

    @GetMapping("/success")
    public ResponseEntity<String> success(OAuth2AuthenticationToken authentication)
    {
        return ResponseEntity.ok("Login successful for: " + authentication.getName());
    }
}
