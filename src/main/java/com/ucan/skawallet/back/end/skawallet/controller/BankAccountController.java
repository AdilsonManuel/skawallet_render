/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.dto.BankAccountDTO;
import com.ucan.skawallet.back.end.skawallet.model.Bank;
import com.ucan.skawallet.back.end.skawallet.model.BankAccount;
import com.ucan.skawallet.back.end.skawallet.model.Users;
import com.ucan.skawallet.back.end.skawallet.repository.BankAccountRepository;
import com.ucan.skawallet.back.end.skawallet.repository.BankRepository;
import com.ucan.skawallet.back.end.skawallet.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author azm
 */
@RestController
@RequestMapping("/api/*/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController
{

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    // Endpoint para consultar conta bancária pelo número da conta (USER ou ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable String accountNumber)
    {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        BankAccountDTO bankAccountDTO = new BankAccountDTO(bankAccount);
        return ResponseEntity.ok(bankAccountDTO);
    }

    // Endpoint para consultar todas as contas bancárias de um usuário (Apenas ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{fk_users}")
    public ResponseEntity<List<BankAccountDTO>> getBankAccountsByUser(@PathVariable Long fk_users)
    {
        Users user = userRepository.findById(fk_users)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<BankAccount> bankAccounts = bankAccountRepository.findByUser(user);
        List<BankAccountDTO> bankAccountDTOs = bankAccounts.stream()
                .map(BankAccountDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bankAccountDTOs);
    }

    // Endpoint para criar uma nova conta bancária (Apenas ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO)
    {
        // Verifica se o usuário existe
        Users user = userRepository.findById(bankAccountDTO.getPk_banks())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o banco existe
        Bank bank = bankRepository.findById(bankAccountDTO.getPk_banks())
                .orElseThrow(() -> new RuntimeException("Banco não encontrado"));

        // Criação de uma nova instância de BankAccount
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankAccountDTO.getAccountNumber());
        bankAccount.setBalance(bankAccountDTO.getBalance());
        bankAccount.setUser(user);
        bankAccount.setBank(bank);

        // Salvando no banco de dados
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        // Retorna a conta bancária criada
        BankAccountDTO savedBankAccountDTO = new BankAccountDTO(savedBankAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBankAccountDTO);
    }

    // Endpoint para listar todas as contas bancárias (Apenas ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<BankAccount>> getAllBankAccounts()
    {
        List<BankAccount> banksAccountDTOs = bankAccountRepository.findAll();
        return ResponseEntity.ok(banksAccountDTOs);
    }
}
