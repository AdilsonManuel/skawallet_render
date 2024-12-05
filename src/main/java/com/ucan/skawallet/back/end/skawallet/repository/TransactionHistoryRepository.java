/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.repository;

import com.ucan.skawallet.back.end.skawallet.model.TransactionHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>
{

    @Query(value = "SELECT * FROM transaction_history WHERE pk_transaction_history = ?", nativeQuery = true)
    List<TransactionHistory> findByTransactionHistory(@Param("pk_transaction_history") Long pk_transaction_history);
}
