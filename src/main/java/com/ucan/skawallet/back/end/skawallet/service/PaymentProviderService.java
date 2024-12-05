/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.dto.PaymentRequestDTO;
import com.ucan.skawallet.back.end.skawallet.dto.PaymentResponseDTO;
import com.ucan.skawallet.back.end.skawallet.repository.PaymentProvider;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProviderService implements PaymentProvider
{

    private final RestTemplate restTemplate;

    @Value("${paddle.api.base-url}")
    private String baseUrl;

    @Value("${paddle.api.vendor-id}")
    private String vendorId;

    @Value("${paddle.api.api-key}")
    private String apiKey;

    @Override
    public PaymentResponseDTO initiatePayment(PaymentRequestDTO request)
    {
        String url = baseUrl + "/order";
        log.info("Iniciando pagamento no Paddle: {}", request);

        // Criação do corpo da requisição
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vendor_id", vendorId);
        requestBody.put("vendor_auth_code", apiKey);
        requestBody.put("amount", request.getAmount());
        requestBody.put("currency", request.getCurrency());
        requestBody.put("customer_email", request.getCustomerEmail());
        requestBody.put("product_id", request.getProductId());

        try
        {
            ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(url, requestBody, PaymentResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful())
            {
                PaymentResponseDTO responseBody = response.getBody();
                log.info("Pagamento iniciado com sucesso: {}", responseBody);
                return responseBody;
            }
            else
            {
                log.error("Erro ao iniciar pagamento no Paddle: HTTP Status={}", response.getStatusCode());
                throw new RuntimeException("Erro ao iniciar pagamento: " + response.getStatusCode());
            }
        }
        catch (HttpClientErrorException e)
        {
            log.error("Erro de cliente na API Paddle: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Erro de cliente: " + e.getMessage());
        }
        catch (HttpServerErrorException e)
        {
            log.error("Erro de servidor na API Paddle: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Erro de servidor: " + e.getMessage());
        }
        catch (RuntimeException e)
        {
            log.error("Erro geral ao conectar com a API Paddle: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar pagamento: " + e.getMessage());
        }

    }

    @Override
    public PaymentResponseDTO checkPaymentStatus(String orderId)
    {
        String url = baseUrl + "/order/" + orderId;
        log.info("Consultando status do pagamento: OrderId={}", orderId);

        // Headers e autenticação
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("vendor_id", vendorId);
        queryParams.put("vendor_auth_code", apiKey);
        queryParams.put("order_id", orderId);

        try
        {
            ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(url, queryParams, PaymentResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful())
            {
                log.info("Status do pagamento recuperado: {}", response.getBody());
                return response.getBody();
            }
            else
            {
                log.error("Erro ao consultar status do pagamento no Paddle: {}", response.getStatusCode());
                throw new RuntimeException("Erro ao consultar status: " + response.getStatusCode());
            }
        }
        catch (RuntimeException e)
        {
            log.error("Erro ao conectar com a API Paddle para consultar status: {}", e.getMessage());
            throw new RuntimeException("Erro ao consultar status do pagamento: " + e.getMessage());
        }
    }

    @Override
    public String createProduct(String productName, String productDescription, BigDecimal price, String currency, String billingType, String frequency)
    {
        String url = baseUrl + "https://vendors.paddle.com/products-v2/";

        // Corpo da requisição
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vendor_id", vendorId);
        requestBody.put("vendor_auth_code", apiKey);
        requestBody.put("product_name", productName);
        requestBody.put("product_description", productDescription);
        requestBody.put("price", price);
        requestBody.put("currency", currency);
        requestBody.put("billing_type", billingType);
        requestBody.put("frequency", frequency);

        // Enviar requisição POST para criar o produto
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try
        {
            // Enviar a requisição POST
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful())
            {
                log.info("Produto criado com sucesso no Paddle: {}", response.getBody());
                return "Produto criado com sucesso!";
            }
            else
            {
                log.error("Erro ao criar produto no Paddle: {}", response.getStatusCode());
                throw new RuntimeException("Erro ao criar produto: " + response.getStatusCode());
            }
        }
        catch (RuntimeException e)
        {
            log.error("Erro ao conectar com a API Paddle: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar criação do produto: " + e.getMessage());
        }
    }

}
