package com.hubert.momoservice.email;

import com.hubert.momoservice.email.token.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender {

  private final JavaMailSender mailSender;

  @Value("${mail.sent.from}")
  private String sentFrom;
  @Value("${mail.sent.subject}")
  private String subject;

  @Autowired
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async
  @Override
  public void send(String to, String email) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper =
          new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setText(email, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setFrom(sentFrom);
      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new IllegalStateException("failed to send email");
    }
  }
}