/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.dto.TransactionRequestDTO;
import com.ucan.skawallet.back.end.skawallet.model.BankAccount;
import com.ucan.skawallet.back.end.skawallet.model.Transaction;
import com.ucan.skawallet.back.end.skawallet.repository.BankAccountRepository;
import com.ucan.skawallet.back.end.skawallet.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author azm
 */
@Service
@RequiredArgsConstructor
public class PaymentService
{

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public BigDecimal getBalance(String accountNumber)
    {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        return account.getBalance();
    }

    public String performTransaction(TransactionRequestDTO request)
    {
        BankAccount sourceAccount = bankAccountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        if (request.getTransactionType().equalsIgnoreCase("DEPOSIT"))
        {
            sourceAccount.setBalance(sourceAccount.getBalance().add(request.getAmount()));
        }
        else if (request.getTransactionType().equalsIgnoreCase("WITHDRAWAL"))
        {
            if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0)
            {
                throw new RuntimeException("Saldo insuficiente");
            }
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        }
        else if (request.getTransactionType().equalsIgnoreCase("TRANSFER"))
        {
            BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(request.getDestinationAccount())
                    .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

            if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0)
            {
                throw new RuntimeException("Saldo insuficiente");
            }
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
            destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
            bankAccountRepository.save(destinationAccount);
        }
        else
        {
            throw new IllegalArgumentException("Tipo de transação inválido");
        }

        bankAccountRepository.save(sourceAccount);
        return "Transação realizada com sucesso";
    }

    public List<Transaction> getAllTransaction()
    {
        return transactionRepository.findAll();
    }
}
