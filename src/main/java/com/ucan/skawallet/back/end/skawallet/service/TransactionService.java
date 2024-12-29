/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.dto.TransactionRequestDTO;
import com.ucan.skawallet.back.end.skawallet.model.BankAccount;
import com.ucan.skawallet.back.end.skawallet.model.EventType;
import com.ucan.skawallet.back.end.skawallet.model.Transaction;
import com.ucan.skawallet.back.end.skawallet.model.TransactionHistory;
import com.ucan.skawallet.back.end.skawallet.model.TransactionStatus;
import com.ucan.skawallet.back.end.skawallet.model.UserTypeAccount;
import com.ucan.skawallet.back.end.skawallet.repository.BankAccountRepository;
import com.ucan.skawallet.back.end.skawallet.repository.TransactionHistoryRepository;
import com.ucan.skawallet.back.end.skawallet.repository.TransactionRepository;
import com.ucan.skawallet.back.end.skawallet.security.AuthenticationFacade;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService
{

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionHistoryRepository transactionHistoryRepository; // Repositório para o histórico
    private final AuthenticationFacade authenticationFacade;
//    private final KafkaProducerService kafkaProducerService;

    public BigDecimal getBalance(String accountNumber)
    {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

//        verifyUserPermission(account);
        return account.getBalance();
    }

    @Transactional
    public String performTransaction(TransactionRequestDTO request)
    {
        BankAccount sourceAccount = bankAccountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

//        verifyUserPermission(sourceAccount);
        logger.info("Transação iniciada: {}", request);

        validateTransactionLimits(request.getAmount(), sourceAccount.getUserTypeAccount());

        if (request.getTransactionType() == null)
        {
            throw new IllegalArgumentException("Tipo de transação inválido");
        }

        BankAccount destinationAccount = null;

        switch (request.getTransactionType())
        {
            case DEPOSIT ->
                sourceAccount.setBalance(sourceAccount.getBalance().add(request.getAmount()));
            case WITHDRAWAL ->
            {
                validateSufficientBalance(sourceAccount, request.getAmount());
                sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
            }
            case TRANSFER ->
            {
                destinationAccount = bankAccountRepository.findByAccountNumber(request.getDestinationAccount())
                        .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));
                validateSufficientBalance(sourceAccount, request.getAmount());
                sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
                destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
                bankAccountRepository.save(destinationAccount);
            }
            default ->
                throw new IllegalArgumentException("Tipo de transação inválido");
        }

        bankAccountRepository.save(sourceAccount);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setStatus(TransactionStatus.COMPLETED);

        transaction = transactionRepository.save(transaction);

        saveTransactionHistory(transaction, EventType.CREATED);

        return "Transação realizada com sucesso";
    }

    public List<TransactionHistory> getTransactionHistory(Long transactionId)
    {
        return transactionHistoryRepository.findByTransactionHistory(transactionId);
    }

    public List<TransactionHistory> getAllTransactionHistory()
    {
        return transactionHistoryRepository.findAll();
    }

    private void saveTransactionHistory(Transaction transaction, EventType eventType)
    {
        TransactionHistory history = new TransactionHistory();
        history.setTransaction(transaction);
        history.setEventType(eventType);
        history.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
        transactionHistoryRepository.save(history);
        logger.info("Histórico de transação salvo: TransactionId={}, EventType={}", transaction.getPk_transactions(), eventType);
    }

    private void validateSufficientBalance(BankAccount account, BigDecimal amount)
    {
        if (account.getBalance().compareTo(amount) < 0)
        {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private void validateTransactionLimits(BigDecimal amount, UserTypeAccount userTypeAccount)
    {
        if (UserTypeAccount.STANDARD.equals(userTypeAccount) && amount.compareTo(BigDecimal.valueOf(10000)) > 0)
        {
            throw new RuntimeException("Limite diário excedido para contas padrão");
        }
        if (UserTypeAccount.PREMIUM.equals(userTypeAccount) && amount.compareTo(BigDecimal.valueOf(50000)) > 0)
        {
            throw new RuntimeException("Limite diário excedido para contas premium");
        }
    }

    private void verifyUserPermission(BankAccount account)
    {
        String currentUser = authenticationFacade.getCurrentUsername();
        if (!account.getUser().getEmail().equals(currentUser) && !authenticationFacade.hasRole("ADMIN"))
        {
            throw new RuntimeException("Permissão negada");
        }
    }

//    public void registerTransaction(Transaction transaction)
//    {
//        // Processar transação (lógica existente)
//
//        // Publicar evento no Kafka
//        String message = String.format("Transação registrada: %s", transaction.toString());
//        kafkaProducerService.sendTransactionEvent(message);
//    }
//
//    public void processTransaction(Transaction transaction)
//    {
//        try
//        {
//            // Criar mensagem com dados da transação
//            String message = String.format("Transação registrada: ID=%s, Valor=%s, Status=%s",
//                                           transaction.getPk_transactions(), transaction.getAmount(), transaction.getStatus());
//
//            // Enviar para o Kafka
//            kafkaProducerService.sendTransactionEvent(message);
//            log.info("Evento de transação enviado ao Kafka");
//
//        }
//        catch (Exception e)
//        {
//            log.error("Erro ao processar transação: {}", e.getMessage());
//        }
//    }

}
