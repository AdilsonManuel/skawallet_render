/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.service.KafkaProducerService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
@Slf4j
public class KafkaController
{

    private final KafkaProducerService kafkaProducerService;

    /**
     * Endpoint para enviar mensagens para o Kafka
     *
     * @param message Mensagem enviada pelo cliente
     * @return Resposta indicando sucesso
     */
    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody String message)
    {
        log.info("Recebendo mensagem para publicar: {}", message);
        kafkaProducerService.sendTransactionEvent(message);
        return ResponseEntity.ok("Mensagem enviada ao Kafka com sucesso!");
    }
}
