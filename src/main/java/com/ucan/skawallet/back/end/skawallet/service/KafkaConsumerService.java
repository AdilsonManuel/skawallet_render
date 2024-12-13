/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService
{

    @KafkaListener(topics = "transaction-events", groupId = "skawallet-group")
    public void consumeTransactionEvent(String message)
    {
        log.info("Mensagem consumida do Kafka: {}", message);
        // Processar e registrar a mensagem no banco de dados, se necessário
    }
}
