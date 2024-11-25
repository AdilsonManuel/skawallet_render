/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ucan.skawallet.back.end.skawallet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author azm
 */
@Entity
@Table(name = "banks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_banks;

    @NotBlank(message = "Bank name cannot be blank")
    @Size(max = 100, message = "Bank name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Bank code cannot be blank")
    @Pattern(regexp = "\\d{3}", message = "Bank code must be exactly 3 digits")
    private String code;

    @NotNull(message = "Created date cannot be null")
    @PastOrPresent(message = "Created date must be in the past or present")
    @Column(name = "created_date")
    private LocalDate createdDate;
}
