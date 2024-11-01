package gr36.clubActiv.services;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import gr36.clubActiv.services.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender sender;
  private final Configuration mailConfig;
  private final ConfirmationService confirmationService;


  public EmailServiceImpl(JavaMailSender sender, Configuration mailConfig,
      ConfirmationService confirmationService) {
    this.sender = sender;
    this.mailConfig = mailConfig;
    this.confirmationService = confirmationService;

    mailConfig.setDefaultEncoding("UTF-8");
    mailConfig.setTemplateLoader(new ClassTemplateLoader(EmailServiceImpl.class, "/mail"));
  }

  @Override
  public void sendConfirmationEmail(User user) {
    MimeMessage message = sender.createMimeMessage(); // Create MimeMessage object
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message,
          MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

      String emailText = generateEmailText(user);

      helper.setFrom("boris.iurciuc@gmail.com");
      helper.setTo(user.getEmail());
      helper.setSubject("Registration Confirmation");
      helper.setText(emailText, true);

      sender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Error while sending confirmation email", e);
    }
  }

  @Override
  public void sendPasswordResetEmail(String email, String resetUrl) {
    MimeMessage message = sender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message,
          MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
      String emailText = generatePasswordResetEmailText(resetUrl);
      helper.setFrom("boris.iurciuc@gmail.com");
      helper.setTo(email);
      helper.setSubject("Password Reset Request");
      helper.setText(emailText, true);

      sender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Error while sending password reset email", e);
    }
  }

  private String generatePasswordResetEmailText(String resetUrl) {
    try {
      Template template = mailConfig.getTemplate("password_reset_mail.ftlh");
      Map<String, Object> templateData = new HashMap<>();
      templateData.put("link", resetUrl);
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateData);
    } catch (Exception e) {
      throw new RuntimeException("Error while generating password reset email text", e);
    }
  }

  private String generateEmailText(User user) {
    try {
      Template template = mailConfig.getTemplate("confirm_reg_mail.ftlh");
      String code = confirmationService.generateConfirmationCode(user);
      String url = "http://localhost:8080/api/register?code=" + code;
      Map<String, Object> templateData = new HashMap<>();
      templateData.put("name", user.getName());
      templateData.put("link", url);
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateData);
    } catch (Exception e) {
      throw new RuntimeException("Error while generating email text", e);
    }
  }
}
