/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.repository;

import com.ucan.skawallet.back.end.skawallet.model.Transaction;
import com.ucan.skawallet.back.end.skawallet.utils.TransactionDetailsDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>
{

    @Query(value = "SELECT \n"
            + "    t.*,\n"
            + "    COALESCE(ba_source.account_number, 'N/A') AS source_account_number,\n"
            + "    COALESCE(ba_dest.account_number, 'N/A') AS destination_account_number,\n"
            + "    u.name AS user_nme\n"
            + "FROM \n"
            + "    transactions t\n"
            + "LEFT JOIN \n"
            + "    bank_accounts ba_source ON t.fk_source_account = ba_source.pk_bank_accounts\n"
            + "LEFT JOIN \n"
            + "    bank_accounts ba_dest ON t.fk_destination_account = ba_dest.pk_bank_accounts\n"
            + "INNER JOIN \n"
            + "    users u ON (\n"
            + "        ba_source.fk_users = u.pk_users OR \n"
            + "        ba_dest.fk_users = u.pk_users OR \n"
            + "        t.fk_users = u.pk_users\n"
            + "    )\n"
            + "WHERE \n"
            + "    u.pk_users = :fk_users", nativeQuery = true)
    List<TransactionDetailsDTO> findTransactionsByUser(
            @Param("fk_users") Long fk_users);
}
