package com.myicpc.service.email;

import com.myicpc.model.contest.Contest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author Roman Smetana
 */
@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String MYICPC_EMAIL = "myicpc.baylor@gmail.com";

    /**
     * Send feedback on email
     *
     * @param subject
     *            subject of the email
     * @param msg
     *            body of the email
     */
    @Async
    public void sendFeedbackEmail(final Contest contest, final String subject, final String msg) {
        getEmailSender().send(getEmailMessage(MYICPC_EMAIL, contest.getContestSettings().getEmail(), subject, msg));
        logger.info("Sent feedback email 'Feedback " + subject + "' to " + contest.getContestSettings().getEmail() + ":\n" + msg);
    }

    private MailSender getEmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // TODO load info from database
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("myicpc2@gmail.com");
        mailSender.setPassword("eDc.4rFv");

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", "true");
        mailProperties.setProperty("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }

    /**
     * Constructs email message from email fields
     *
     * @param from
     *            from email address
     * @param to
     *            to email address
     * @param subject
     *            email subject
     * @param text
     *            test of the email
     * @return email representation
     */
    private SimpleMailMessage getEmailMessage(String from, String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        return mailMessage;
    }
}
