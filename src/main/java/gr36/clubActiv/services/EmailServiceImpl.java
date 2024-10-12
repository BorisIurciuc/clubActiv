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

  // Constructor injection
  public EmailServiceImpl(JavaMailSender sender, Configuration mailConfig, ConfirmationService confirmationService) {
    this.sender = sender;
    this.mailConfig = mailConfig;
    this.confirmationService = confirmationService;

    // Set mail configuration
    mailConfig.setDefaultEncoding("UTF-8");
    mailConfig.setTemplateLoader(new ClassTemplateLoader(EmailServiceImpl.class, "/mail"));
  }

  @Override
  public void sendConfirmationEmail(User user) {
    MimeMessage message = sender.createMimeMessage(); // Create MimeMessage object
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

      // Generate the email content using the FreeMarker template
      String emailText = generateEmailText(user);

      // Set email metadata
      helper.setFrom("nejelschi@gmail.com");
      helper.setTo(user.getEmail());
      helper.setSubject("Registration Confirmation");
      helper.setText(emailText, true); // Set true for HTML content

      // Send email
      sender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Error while sending confirmation email", e);
    }
  }

  private String generateEmailText(User user) {
    try {
      Template template = mailConfig.getTemplate("confirm_reg_mail.ftlh");

      // Generate confirmation code and URL
      String code = confirmationService.generateConfirmationCode(user);
      String url = "http://localhost:8080/api/register?code=" + code;

      // Prepare template data
      Map<String, Object> templateData = new HashMap<>();
      templateData.put("name", user.getName());
      templateData.put("link", url);

      // Process template into a String
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateData);
    } catch (Exception e) {
      throw new RuntimeException("Error while generating email text", e);
    }
  }
}
