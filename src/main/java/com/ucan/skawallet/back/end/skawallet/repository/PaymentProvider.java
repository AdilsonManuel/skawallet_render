/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.repository;

import com.ucan.skawallet.back.end.skawallet.dto.PaymentRequestDTO;
import com.ucan.skawallet.back.end.skawallet.dto.PaymentResponseDTO;
import java.math.BigDecimal;


public interface PaymentProvider
{

    PaymentResponseDTO initiatePayment(PaymentRequestDTO request);

    PaymentResponseDTO checkPaymentStatus(String orderId);

    public String createProduct(String productName, String productDescription, BigDecimal price, String currency, String billingType, String frequency);
}
