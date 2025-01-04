/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.model.Transaction;
import com.ucan.skawallet.back.end.skawallet.model.TransactionHistory;
import com.ucan.skawallet.back.end.skawallet.repository.TransactionHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService
{

    private final TransactionHistoryRepository transactionHistoryRepository;

    public List<TransactionHistory> getAllHistory()
    {
        return transactionHistoryRepository.findAll();
    }

    public List<TransactionHistory> getHistoryByTransactionId(String transactionId)
    {
        return transactionHistoryRepository.findByTransactionPkTransactions(transactionId);
    }

}
