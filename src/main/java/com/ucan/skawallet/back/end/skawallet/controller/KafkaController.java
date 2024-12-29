///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
// */
//package com.ucan.skawallet.back.end.skawallet.controller;
//
//import com.ucan.skawallet.back.end.skawallet.model.Transaction;
//import com.ucan.skawallet.back.end.skawallet.model.TransactionHistory;
//import com.ucan.skawallet.back.end.skawallet.repository.TransactionHistoryRepository;
//import com.ucan.skawallet.back.end.skawallet.service.KafkaProducerService;
//import com.ucan.skawallet.back.end.skawallet.service.TransactionService;
//import java.util.List;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@RestController
//@RequestMapping("/api/v1/kafka")
//@RequiredArgsConstructor
//@Slf4j
//public class KafkaController
//{
//
////    private final KafkaProducerService kafkaProducerService;
//    private final TransactionService transactionService;
//    private final TransactionHistoryRepository transactionHistoryRepository;
//
//    /**
//     * Endpoint para enviar mensagens para o Kafka
//     *
//     * @param message Mensagem enviada pelo cliente
//     * @return Resposta indicando sucesso
//     */
//    @PostMapping("/publish")
//    public ResponseEntity<String> publishMessage(@RequestBody String message)
//    {
//        log.info("Recebendo mensagem para publicar: {}", message);
//        kafkaProducerService.sendTransactionEvent(message);
//        return ResponseEntity.ok("Mensagem enviada ao Kafka com sucesso!");
//    }
//
//    /**
//     * Endpoint para monitorar o status do consumidor.
//     *
//     * Este é apenas um exemplo para verificar se o consumidor está ativo.
//     *
//     * @return Resposta com uma mensagem de status.
//     */
//    @GetMapping("/status")
//    public ResponseEntity<String> checkConsumerStatus()
//    {
//        log.info("Verificando o status do consumidor Kafka...");
//        return ResponseEntity.ok("Consumidor Kafka está ativo.");
//    }
//
//    @PostMapping("/process")
//    public ResponseEntity<String> processTransaction(@RequestBody Transaction transaction)
//    {
//        transactionService.processTransaction(transaction);
//        return ResponseEntity.ok("Transação processada com sucesso!");
//    }
//
//    /**
//     * Endpoint para buscar histórico de mensagens Kafka no banco de dados.
//     *
//     * @return Lista de mensagens consumidas e salvas.
//     */
//    @GetMapping("/history")
//    public ResponseEntity<List<TransactionHistory>> getTransactionHistory()
//    {
//        List<TransactionHistory> history = transactionHistoryRepository.findAll();
//        if (history.isEmpty())
//        {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(history);
//    }
//}
