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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author azm
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_transactions;

    @Column(nullable = false)
    private BigDecimal amount;  // Quantia da transação (ex: 100.50)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;  // Tipo da transação (DEPOSIT, WITHDRAWAL, TRANSFER)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;  // Status da transação (PENDING, COMPLETED, FAILED)

    @ManyToOne
    @JoinColumn(name = "fk_users")
    private Users user;  // Usuário que está realizando a transação

    @ManyToOne
    @JoinColumn(name = "fk_source_account")
    private BankAccount sourceAccount;  // Conta de origem da transação (de onde o dinheiro sai)

    @ManyToOne
    @JoinColumn(name = "fk_destination_account")
    private BankAccount destinationAccount;  // Conta de destino da transação (para onde o dinheiro vai)

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;  // Data de criação da transação
}
