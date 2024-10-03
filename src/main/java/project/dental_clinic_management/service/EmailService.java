package project.dental_clinic_management.service;

import project.dental_clinic_management.dto.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //Config mail là gửi từ tk tuan6a1thcstv@gmail.com và nội dung lấy từ mail body
    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("tuan6a1thcstv@gmail.com");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        mailSender.send(message);

    }
}
