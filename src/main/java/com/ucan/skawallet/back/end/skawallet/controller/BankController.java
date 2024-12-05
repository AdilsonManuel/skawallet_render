/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.controller;

import com.ucan.skawallet.back.end.skawallet.model.Bank;
import com.ucan.skawallet.back.end.skawallet.service.BankService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/api/v1/banks")
public class BankController
{

    @Autowired
    private BankService bankService;

    // Apenas usuários com role ADMIN podem acessar
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Bank>> getAllBanks()
    {
        List<Bank> banks = bankService.getAllBanks();
        return ResponseEntity.ok(banks);
    }

    // Apenas usuários com role ADMIN ou USER podem acessar
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{pk_banks}")
    public ResponseEntity<Bank> getBankByCode(@PathVariable String pk_banks)
    {
        Bank bank = bankService.getBankByCode(pk_banks);
        if (bank != null)
        {
            return ResponseEntity.ok(bank);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    // Apenas usuários com role ADMIN podem acessar
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Bank> createBank(@RequestBody Bank bank)
    {
        Bank createdBank = bankService.createBank(bank);
        return ResponseEntity.status(201).body(createdBank);
    }

    // Apenas usuários com role ADMIN podem acessar
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{pk_banks}")
    public ResponseEntity<Bank> updateBankPartially(@PathVariable Long pk_banks, @RequestBody Map<String, Object> updates)
    {
        try
        {
            Bank updatedBank = bankService.updateBankPartially(pk_banks, updates);
            return ResponseEntity.ok(updatedBank);
        }
        catch (EntityNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Apenas usuários com role ADMIN podem acessar
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{pk_banks}")
    public ResponseEntity<?> deleteBank(@PathVariable Long pk_banks)
    {
        Optional<Bank> bank = bankService.getBankById(pk_banks);
        if (bank.isPresent())
        {
            bankService.deleteBank(pk_banks);
            return new ResponseEntity<>("Bank deleted successfully", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Bank not found", HttpStatus.NOT_FOUND);
        }
    }
}
