///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
// */
//package com.ucan.skawallet.back.end.skawallet.service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class EmailService
//{
//
//    @Autowired
//    private final JavaMailSender mailSender;
//
//    public void sendEmail(String to, String subject, String text) throws MessagingException
//    {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(text, true);
//        mailSender.send(message);
//    }
//}
