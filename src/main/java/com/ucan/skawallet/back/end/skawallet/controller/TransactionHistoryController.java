/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.model.Transaction;
import com.ucan.skawallet.back.end.skawallet.model.TransactionHistory;
import com.ucan.skawallet.back.end.skawallet.service.TransactionHistoryService;
import com.ucan.skawallet.back.end.skawallet.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction-history")
@RequiredArgsConstructor
public class TransactionHistoryController
{

    private final TransactionService transactionService;
    private final TransactionHistoryService transactionServiceHistory;

    @GetMapping("/")
    public ResponseEntity<List<TransactionHistory>> getAllTransactionHistory()
    {
        List<TransactionHistory> transactionHistorys = transactionService.getAllTransactionHistory();

        return new ResponseEntity<>(transactionHistorys, HttpStatus.OK);
    }

    @GetMapping("/{fk_transactions}")
    public ResponseEntity<List<TransactionHistory>> getTransactionHistory(@PathVariable Long fk_transactions)
    {
        List<TransactionHistory> history = transactionService.getTransactionHistory(fk_transactions);
        if (history.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }
}
