package com.gc.easy.email.smtp.service;

import com.gc.easy.sms.core.config.EmailConfiguration;
import com.gc.easy.sms.core.exception.SendingException;
import com.gc.easy.sms.core.model.SendResponse;
import com.gc.easy.sms.core.service.SendService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SmtpEmailService implements SendService {
    private final EmailConfiguration config;
    private Session session;
    private IEmailTemplateService templateService;
    public SmtpEmailService(EmailConfiguration config,IEmailTemplateService templateService)
    {
        this.config = config;
        this.templateService=templateService;
        Properties properties = new Properties();
        properties.put("mail.smtp.host", config.getHost());
        properties.put("mail.smtp.port", config.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true"); // 使用SSL安全连接
        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
    }

    @Override
    public void removeService() {
        this.session=null;
    }

    @Override
    public void send(String to, String text) throws SendingException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getUsername()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(config.getSubject());
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new SendingException("Failed to send email", e);
        }
    }
    private void sendHtml(String to, String htmlMessage) throws MessagingException {
        // 创建一个包含HTML内容的MimeBodyPart
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlMessage, "text/html; charset=utf-8");
        // 创建一个Multipart对象，并设置其为mixed类型
        Multipart multipart = new MimeMultipart("mixed");
        // 将HTML部分添加到Multipart中
        multipart.addBodyPart(htmlPart);
        // 创建邮件消息
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getUsername()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(config.getSubject());
        // 设置邮件的主要内容为multipart对象
        message.setContent(multipart);
        // 发送邮件
        Transport.send(message);
    }

    @Override
    public SendResponse sendWithResponse(String to, String message) throws SendingException {
        try {
            send(to,message);
            return new SendResponse(true, "Message sent successfully");
        }catch (Exception e){
            return new SendResponse(false, e.getMessage());

        }
    }

    @Override
    public void sendTemplate(String to, String templateId, Map<String, String> params) throws SendingException {
        String templateContent = templateService.loadTemplate(templateId); // 假设这个方法可以加载模板内容
        String htmlMessage = TemplateEngine.renderTemplate(templateContent, params);
        try {
            sendHtml(to, htmlMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SendResponse sendTemplateWithResponse(String to, String templateId, Map<String, String> params) throws SendingException {
        try {
            sendTemplate(to, templateId, params);
            return new SendResponse(true, "Template message sent successfully: " + params.toString());
        } catch (Exception e) {
            return new SendResponse(false, e.getMessage());
        }
    }
    @Override
    public void sendBulk(List<String> toNumbers, String message) {
        // 批量发送逻辑
        toNumbers.parallelStream().forEach(to -> {
            try {
                send(to,message);
            } catch (SendingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public SendResponse sendBulkWithResponse(List<String> toNumbers, String message) throws SendingException {
        try {
            sendBulk(toNumbers,message);
            return new SendResponse(true, "Bulk email sent successfully");
        } catch (Exception e) {
            return new SendResponse(false, "Failed to send bulk email: " + e.getMessage());
        }
    }

    @Override
    public void sendBulkWithTemplate(List<String> toNumbers, String templateId, Map<String, String> templateParams) throws SendingException {
        // 模板批量发送逻辑
        // 根据每个收件人地址处理模板参数
        toNumbers.parallelStream().forEach(to -> {
            try {
                sendTemplate(to, templateId, templateParams);
            } catch (SendingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public SendResponse sendBulkWithTemplateResponse(List<String> toNumbers, String templateId, Map<String, String> templateParams) throws SendingException {
        // 模板批量发送逻辑，并返回响应
        try {
            sendBulkWithTemplate(toNumbers, templateId, templateParams);
            return new SendResponse(true, "Bulk template email sent successfully");
        } catch (Exception e) {
            return new SendResponse(false, "Failed to send bulk template email: " + e.getMessage());
        }
    }
}