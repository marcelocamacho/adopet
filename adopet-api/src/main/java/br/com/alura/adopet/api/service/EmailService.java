package br.com.alura.adopet.api.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private final String FROM = "adopet@localhost";

    private JavaMailSender emailSender;
    
    public void enviarEmail(String to, String subject, String message){


        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(FROM);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        try{
            emailSender.send(email);
        } catch(Exception e){
            System.out.println("|> Disparando erro!!!");
        }
        


    }
}