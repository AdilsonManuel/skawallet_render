/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.dto;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author azm
 */
@Data
public class TransactionRequestDTO
{

    private String sourceAccount;        // Conta de origem
    private String destinationAccount;   // Conta de destino (aplicável para TRANSFER)
    private BigDecimal amount;           // Valor da transação
    private String transactionType;      // Tipo de transação (DEPOSIT, WITHDRAWAL, TRANSFER)
}
