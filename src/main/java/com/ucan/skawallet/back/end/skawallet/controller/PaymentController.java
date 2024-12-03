/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.dto.TransactionRequestDTO;
import com.ucan.skawallet.back.end.skawallet.model.BankAccount;
import com.ucan.skawallet.back.end.skawallet.service.PaymentService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author azm
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController
{

    private final PaymentService paymentService;

    // Apenas ADMIN pode acessar (Exemplo: consulta global de contas)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<BankAccount>> getAll()
    {
        List<BankAccount> transaction = paymentService.getAllTransaction();
        return ResponseEntity.ok(transaction);
    }

    // ADMIN e USER podem acessar (Exemplo: consulta de saldo de conta específica)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber)
    {
        return ResponseEntity.ok(paymentService.getBalance(accountNumber));
    }

    // Apenas USER pode realizar transações
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/transaction")
    public ResponseEntity<String> performTransaction(@RequestBody TransactionRequestDTO request)
    {
        String response = paymentService.performTransaction(request);
        return ResponseEntity.ok(response);
    }
}
