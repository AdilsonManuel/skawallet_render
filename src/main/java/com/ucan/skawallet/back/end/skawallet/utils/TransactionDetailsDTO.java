/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.utils;

import com.ucan.skawallet.back.end.skawallet.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author azm
 */
public interface TransactionDetailsDTO
{

    Long getPk_transactions(); // Campo t.id

    BigDecimal getAmount(); // Campo t.amount

    String gettransaction_type(); // Campo t.transactionType

    String getStatus(); // Campo t.status

    LocalDateTime getCreated_at(); // Campo t.createdAt

    String getfk_source_account(); // Campo ba_source.account_number

    String getfk_destination_account(); // Campo ba_dest.account_number

    String getsource_account_number();

    String getuser_name(); // Campo u.name
}
