package com.myicpc.service.email;

import com.myicpc.model.contest.Contest;
import com.myicpc.service.dto.GlobalSettings;
import com.myicpc.service.settings.GlobalSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Email service responsible for sending emails from MyICPC
 *
 * @author Roman Smetana
 */
@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private GlobalSettingsService globalSettingsService;

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
        GlobalSettings globalSettings = globalSettingsService.getGlobalSettings();
        getEmailSender(globalSettings).send(getEmailMessage(globalSettings.getSmtpUsername(), contest.getContestSettings().getEmail(), subject, msg));
        logger.info("Sent feedback email 'Feedback " + subject + "' to " + contest.getContestSettings().getEmail() + ":\n" + msg);
    }

    private MailSender getEmailSender(GlobalSettings globalSettings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(globalSettings.getSmtpHost());
        mailSender.setPort(Integer.parseInt(globalSettings.getSmtpPort()));
        mailSender.setUsername(globalSettings.getSmtpUsername());
        mailSender.setPassword(globalSettings.getSmtpPassword());

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
