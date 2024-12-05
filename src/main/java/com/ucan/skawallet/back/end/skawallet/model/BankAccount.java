/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "bank_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_bank_accounts;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "fk_users", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "fk_banks", nullable = false)
    private Bank bank;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserTypeAccount userTypeAccount;

}
