/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.security.token;

/**
 *
 * @author azm
 */
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class JwtKeyGenerator
{

    public static void main(String[] args)
    {
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // Gera uma chave para HS256
        String secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Secret Key: " + secretKey);
    }
}
