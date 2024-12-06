/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.dto.TransactionRequestDTO;
import com.ucan.skawallet.back.end.skawallet.service.TransactionService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/*/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController
{

    private final TransactionService transactionService;

    // Endpoint para obter o saldo de uma conta
    @GetMapping("/balance/{accountNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber)
    {
        return ResponseEntity.ok(transactionService.getBalance(accountNumber));
    }

    // Endpoint para realizar uma transação
    @PostMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> performTransaction(@RequestBody TransactionRequestDTO request)
    {
        return ResponseEntity.ok(transactionService.performTransaction(request));
    }

}
