/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import java.io.Serializable;
import java.util.Properties;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Carlos
 */
@Named(value = "envioCorreosBean")
@ViewScoped
public class EnvioCorreosBean implements Serializable{
    static Properties mailServProp;
    static Session mailSess;
    static MimeMessage geneMailMess;

    public EnvioCorreosBean() {
    }
    
    /**
     * Método que envía correos eletrónicos desde cualquier parte del sistema
     * @param para Es el destinatario del correo.
     * @param asun Es el asunto del correo.
     * @param mens Es el mensaje del correo.
     * @throws AddressException cuando se produce una excepción en la dirección del destinatario
     * @throws  MessagingException cuando se produce una excepción en el cuerpo del correo
     */
    public void sendMail(String para, String asun, String mens) throws AddressException, MessagingException{
        mailServProp = System.getProperties();
        mailServProp.put("mail.smtp.port", "587");
        mailServProp.put("mail.smtp.auth", "true");
        mailServProp.put("mail.smtp.starttls.enable", "true");
        
        mailSess = Session.getDefaultInstance(mailServProp, null);
        geneMailMess = new MimeMessage(mailSess);
        geneMailMess.addRecipient(Message.RecipientType.TO, new InternetAddress(para));
        //geneMailMess.addRecipient(Message.RecipientType.CC, new InternetAddress("another.receiver@service.com"));
        geneMailMess.setSubject(asun);
        geneMailMess.setContent(mens, "text/html");
        
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        String user = facsCtxt.getExternalContext().getInitParameter("sendmail.USER");
        String pass = facsCtxt.getExternalContext().getInitParameter("sendmail.PASSWORD"); 
        Transport transport = mailSess.getTransport("smtp");
        transport.connect("smtp.gmail.com", user, pass);
        transport.sendMessage(geneMailMess, geneMailMess.getAllRecipients());
        transport.close();
    }
}
