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
        message.setSubject("Welcome To Our Authify Platform");
        message.setText("Hi, "+name+"\n\nThank you for registering with us!\n\nYour account has been successfully created.\n\nBest Regards, \n Authify Team");
        javaMailSender.send(message);
    }

    public void sendOtpMail(String email,String otp)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Hi, "+"\n\nYour One-Time Password (OTP) is:  "+otp+"\n\nPlease use this OTP to reset your password"+"\n\nThank you,\n Authify Team");
        javaMailSender.send(message);
    }
    public  void verifyEmailOtp(String toEmail,String otp)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(toEmail);
        message.setSubject("Account verification Code");
        message.setText("Hi, \n\nYour Authify Account Verification Code is :  "+otp+"\n\n Thank you.");
        javaMailSender.send(message);
    }
}
