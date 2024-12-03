/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.dto.TransactionRequestDTO;
import com.ucan.skawallet.back.end.skawallet.model.BankAccount;
import com.ucan.skawallet.back.end.skawallet.model.Transaction;
import com.ucan.skawallet.back.end.skawallet.model.TransactionStatus;
import com.ucan.skawallet.back.end.skawallet.model.TransactionType;
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

    private void validateTransactionRequest(TransactionRequestDTO request)
    {
        if (request.getSourceAccount() == null || request.getSourceAccount().isEmpty())
        {
            throw new IllegalArgumentException("O número da conta de origem é obrigatório");
        }

        if (request.getTransactionType() == null || request.getTransactionType().isEmpty())
        {
            throw new IllegalArgumentException("O tipo de transação é obrigatório");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero");
        }
    }

    public String performTransaction(TransactionRequestDTO request)
    {
        validateTransactionRequest(request);

        Transaction transaction = createPendingTransaction(request);

        try
        {
            switch (TransactionType.valueOf(request.getTransactionType().toUpperCase()))
            {
                case DEPOSIT -> processDeposit(request, transaction);
                case WITHDRAWAL -> processWithdrawal(request, transaction);
                case TRANSFER -> processTransfer(request, transaction);
                default -> throw new IllegalArgumentException("Tipo de transação inválido");
            }

            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            return "Transação realizada com sucesso";

        }
        catch (IllegalArgumentException e)
        {
            transaction.setStatus(TransactionStatus.FAILED);
//            transaction.setDescription(e.getMessage());
            transactionRepository.save(transaction);

            throw new RuntimeException("Erro na transação: " + e.getMessage(), e);
        }
    }

    private Transaction createPendingTransaction(TransactionRequestDTO request)
    {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.valueOf(request.getTransactionType().toUpperCase()));
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);

        if (request.getSourceAccount() != null)
        {
            BankAccount sourceAccount = bankAccountRepository.findByAccountNumber(request.getSourceAccount())
                    .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));
            transaction.setSourceAccount(sourceAccount);
        }

        if (request.getDestinationAccount() != null)
        {
            BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(request.getDestinationAccount())
                    .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));
            transaction.setDestinationAccount(destinationAccount);
        }

        return transactionRepository.save(transaction);
    }
    
        private void processDeposit(TransactionRequestDTO request, Transaction transaction) {
        BankAccount sourceAccount = transaction.getSourceAccount();
        sourceAccount.setBalance(sourceAccount.getBalance().add(request.getAmount()));
        bankAccountRepository.save(sourceAccount);
    }

    private void processWithdrawal(TransactionRequestDTO request, Transaction transaction) {
        BankAccount sourceAccount = transaction.getSourceAccount();

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        bankAccountRepository.save(sourceAccount);
    }

    private void processTransfer(TransactionRequestDTO request, Transaction transaction) {
        BankAccount sourceAccount = transaction.getSourceAccount();
        BankAccount destinationAccount = transaction.getDestinationAccount();

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(destinationAccount);
    }

//    public String performTransaction(TransactionRequestDTO request)
//    {
//        BankAccount sourceAccount = bankAccountRepository.findByAccountNumber(request.getSourceAccount())
//                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));
//
//        // Criar uma nova transação e definir o estado inicial como PENDING
//        Transaction transaction = new Transaction();
//        transaction.setTransactionType(TransactionType.valueOf(request.getTransactionType().toUpperCase()));
//        transaction.setAmount(request.getAmount());
//        transaction.setSourceAccount(sourceAccount);
//
//        try
//        {
//            if (null != transaction.getTransactionType())
//            {
//                switch (transaction.getTransactionType())
//                {
//                    case DEPOSIT ->
//                        sourceAccount.setBalance(sourceAccount.getBalance().add(request.getAmount()));
//                    case WITHDRAWAL ->
//                    {
//                        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0)
//                        {
//                            transaction.setStatus(TransactionStatus.FAILED);
//                            throw new RuntimeException("Saldo insuficiente");
//                        }
//                        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
//                    }
//                    case TRANSFER ->
//                    {
//                        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(request.getDestinationAccount())
//                                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));
//                        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0)
//                        {
//                            transaction.setStatus(TransactionStatus.FAILED);
//                            throw new RuntimeException("Saldo insuficiente");
//                        }
//                        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
//                        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
//                        bankAccountRepository.save(destinationAccount);
//                    }
//                    default ->
//                    {
//                    }
//                }
//            }
//
//            transaction.setStatus(TransactionStatus.COMPLETED);
//            bankAccountRepository.save(sourceAccount);
//            transactionRepository.save(transaction); // Salvar a transação no banco de dados
//            return "Transação realizada com sucesso";
//        }
//        catch (Exception e)
//        {
//            transaction.setStatus(TransactionStatus.FAILED);
////            transaction.setDescription(e.getMessage());
//            transactionRepository.save(transaction); // Salvar a falha da transação
//            return "Erro na transação: " + e.getMessage();
//        }
//    }
    public List<BankAccount> getAllTransaction()
    {
        return bankAccountRepository.findAll();
    }
}
