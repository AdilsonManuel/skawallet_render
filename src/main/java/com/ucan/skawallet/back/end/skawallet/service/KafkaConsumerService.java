/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucan.skawallet.back.end.skawallet.model.TransactionHistory;
import com.ucan.skawallet.back.end.skawallet.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService
{

    @Autowired
    private final TransactionHistoryRepository transactionHistoryRepository;

    @KafkaListener(topics = "transaction-events", groupId = "skawallet-group")
    public void consumeTransactionEvent(String message)
    {
        log.info("Mensagem consumida do Kafka: {}", message);
        // Processar e registrar a mensagem no banco de dados, se necessário
    }

//    @KafkaListener(topics = "transaction-history", groupId = "transaction-group")
//    public void consumeTransactionHistory(String message)
//    {
//        log.info("Mensagem consumida do tópico 'transaction-history': {}", message);
//        // Processar e armazenar no banco de dados, se necessário
//    }

    @KafkaListener(topics = "transaction-errors", groupId = "transaction-group")
    public void consumeTransactionErrors(String errorMessage)
    {
        log.error("Erro consumido do tópico 'transaction-errors': {}", errorMessage);
        // Registrar logs ou notificar a equipe de suporte
    }

    @KafkaListener(topics = "transaction-history", groupId = "skawallet-group")
    public void consumeTransactionHistory(String message)
    {
        log.info("Mensagem consumida do tópico 'transaction-history': {}", message);

        try
        {
            // Converter mensagem para objeto TransactionHistory
            ObjectMapper objectMapper = new ObjectMapper();
            TransactionHistory transactionHistory = objectMapper.readValue(message, TransactionHistory.class);

            // Persistir no banco de dados
            transactionHistoryRepository.save(transactionHistory);
            log.info("Histórico de transação salvo no banco de dados: {}", transactionHistory);

        }
        catch (Exception e)
        {
            log.error("Erro ao processar mensagem consumida: {}", e.getMessage());
        }
    }
}
    