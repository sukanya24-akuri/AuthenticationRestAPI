package com.project.authify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService
{
    private  final JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromMail;


    public void sendMessage(String toMail,String name)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(toMail);
        message.setSubject("Welcome to our Authify Platform");
        message.setText("Hi, "+name+"\n\nThank you for Registration with us!\n\nBest Regards, \n Authify Team");
        javaMailSender.send(message);
    }

    public void sendOtpMail(String email,String otp)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Hi, "+"\n\n your OTP is "+otp +" you can use this OTP and reset your password");
        javaMailSender.send(message);
    }
    public  void verifyEmailOtp(String toEmail,String otp)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(toEmail);
        message.setSubject("Account verification OTP");
        message.setText("verification code for Authify account :"+otp);
        javaMailSender.send(message);
    }
}
