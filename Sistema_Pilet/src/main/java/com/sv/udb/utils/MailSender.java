/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
/**
 *
 * @author aleso
 */
public class MailSender {
    
    public static void mthEnviar(String destino, String asunto, String mensaje) {
        try {
            Properties Propiedades = new Properties();
            Session Sesión;
            Propiedades.put("mail.smtp.host", "smtp.gmail.com");
            Propiedades.put("mail.smtp.starttls.enable", "true");
            Propiedades.put("mail.smtp.port", 587);
            Propiedades.put("mail.smtp.mail.sender", "smartmuseumsystem@gmail.com");
            Propiedades.put("mail.smtp.password", "expotecnica2014");
            Propiedades.put("mail.smtp.user", "smartmuseumsystem@gmail.com");
            Propiedades.put("mail.smtp.auth", "true");
            Sesión = Session.getDefaultInstance(Propiedades);
            MimeMessage message = new MimeMessage(Sesión);
            message.setFrom(new InternetAddress((String) Propiedades.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport Cuenta = Sesión.getTransport("smtp");
            Cuenta.connect((String) Propiedades.get("mail.smtp.user"), (String) Propiedades.get("mail.smtp.password"));
            Cuenta.sendMessage(message, message.getAllRecipients());
            Cuenta.close();
        } catch (MessagingException e) {
            System.out.println(""+e.getLocalizedMessage());
        }
    }
}
