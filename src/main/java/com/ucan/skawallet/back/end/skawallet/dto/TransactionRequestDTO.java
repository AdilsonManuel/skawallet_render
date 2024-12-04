/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.dto;

import com.ucan.skawallet.back.end.skawallet.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author azm
 */
@Data
public class TransactionRequestDTO
{

    @NotBlank(message = "Conta de origem é obrigatória")
    private String sourceAccount;

    private String destinationAccount; // Opcional para depósitos e retiradas

    @NotNull(message = "Valor da transação é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor da transação deve ser positivo")
    private BigDecimal amount;

    @NotNull(message = "Tipo de transação é obrigatório")
    private TransactionType transactionType;
}   
