/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.service;

import com.ucan.skawallet.back.end.skawallet.model.Bank;
import com.ucan.skawallet.back.end.skawallet.repository.BankRepository;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author azm
 */
@Service
public class BankService
{

    @Autowired
    private BankRepository bankRepository;

    public List<Bank> getAllBanks()
    {
        return bankRepository.findAll();
    }

    public Bank getBankByCode(String pk_banks)
    {
        return bankRepository.findByCode(pk_banks);
    }

    public Bank createBank(Bank bank)
    {
        return bankRepository.save(bank);
    }

    public Bank updateBankPartially(Long id, Map<String, Object> updates)
    {
        // Verifica se o banco existe
        Bank existingBank = bankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + id));

        // Itera pelas atualizações e aplica as mudanças
        updates.forEach((key, value) ->
        {
            Field field = ReflectionUtils.findField(Bank.class, key);
            if (field != null)
            {
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingBank, value);
            }
        });

        // Salva as mudanças no banco de dados
        return bankRepository.save(existingBank);
    }

    public void deleteBank(Long pk_banks)
    {
        bankRepository.deleteById(pk_banks);
    }

    public Optional<Bank> getBankById(Long pk_banks)
    {
        return bankRepository.findById(pk_banks);
    }
}
