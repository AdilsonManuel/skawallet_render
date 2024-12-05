/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.dto;

import com.ucan.skawallet.back.end.skawallet.model.BankAccount;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankAccountDTO
{

    private String accountNumber;  // Número da conta bancária
    private BigDecimal balance;    // Saldo da conta bancária
    private String bankName;       // Nome do banco
    private Long pk_users;        // ID do usuário
    private Long pk_banks;        // ID do banco

    public BankAccountDTO(BankAccount bankAccount)
    {
        this.accountNumber = bankAccount.getAccountNumber();
        this.balance = bankAccount.getBalance();
        this.bankName = bankAccount.getBank().getName();  // Supondo que o banco tenha um nome
        this.pk_users = bankAccount.getUser().getPk_users();  // ID do usuário dono da conta
        this.pk_banks = bankAccount.getBank().getPk_banks();  // ID do banco da conta
    }
}
