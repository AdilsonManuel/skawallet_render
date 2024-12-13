/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService
{

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransactionEvent(String message)
    {
        log.info("Enviando mensagem para o Kafka: {}", message);
        kafkaTemplate.send("transaction-events", message);
    }

    public void sendTransactionToHistory(String transactionMessage)
    {
        log.info("Publicando histórico de transação no Kafka: {}", transactionMessage);
        kafkaTemplate.send("transaction-history", transactionMessage);
    }

    public void sendTransactionError(String errorMessage)
    {
        log.info("Publicando erro de transação no Kafka: {}", errorMessage);
        kafkaTemplate.send("transaction-errors", errorMessage);
    }
}
