package bknd.JavaMain.Verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service // Add this annotation
public class EmailService {
    @Autowired // Add this annotation
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("informarcialaditya@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public void emailGenerate(String generatedOTP, String email){
        String statement = generatedOTP.equals("null") ? "Fail to generate OTP"
                :  "hello from java. " + generatedOTP + "is your OTP."
                + new SimpleDateFormat("HH:mm:ss").format(new Date());

        sendMail(
            email,
            "sample subject",
            statement
        );
        System.out.print(generatedOTP);
    }
}