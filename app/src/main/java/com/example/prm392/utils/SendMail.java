package com.example.prm392.utils;

import android.os.AsyncTask;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    private static final String EMAIL = "qchi20112003@gmail.com";
    private static final String PASSWORD = "ftqgdwsmkhvfhrxy";

    public static String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(10000)); // 6-digit OTP
    }

    public static boolean sendMail(String recipientEmail, String otp) {
        AsyncTask.execute(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Verification Code");
                message.setText("Your OTP code is: " + otp);

                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
