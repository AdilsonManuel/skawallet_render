/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.dto.PaymentRequestDTO;
import com.ucan.skawallet.back.end.skawallet.dto.PaymentResponseDTO;
import com.ucan.skawallet.back.end.skawallet.dto.ProductRequestDTO;
import com.ucan.skawallet.back.end.skawallet.service.PaymentProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController
{

    private final PaymentProviderService paymentProvider;

    @PostMapping("/")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO request)
    {
        return ResponseEntity.ok(paymentProvider.initiatePayment(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponseDTO> checkPaymentStatus(@PathVariable String orderId)
    {
        return ResponseEntity.ok(paymentProvider.checkPaymentStatus(orderId));
    }

    /**
     * Endpoint para criar um novo produto no Paddle
     *
     * @param request O DTO com as informações do produto
     * @return Resposta indicando o sucesso ou falha na criação do produto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")  // Apenas administradores podem criar produtos
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestDTO request)
    {
        log.info("Tentativa de criar produto no Paddle: {}", request);

        try
        {
            String result = paymentProvider.createProduct(
                    request.getProductName(),
                    request.getProductDescription(),
                    request.getPrice(),
                    request.getCurrency(),
                    request.getBillingType(),
                    request.getFrequency()
            );

            return ResponseEntity.ok(result);  // Retorna uma mensagem de sucesso

        }
        catch (Exception e)
        {
            log.error("Erro ao criar produto no Paddle: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar produto: " + e.getMessage());
        }
    }
}
