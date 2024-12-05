/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.dto;

import java.util.Map;
import lombok.Data;


@Data
public class PaymentResponseDTO
{

    private boolean success;
    private String message;
    private Map<String, Object> response;
}
