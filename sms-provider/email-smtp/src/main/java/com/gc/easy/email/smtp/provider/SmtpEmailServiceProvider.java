package com.gc.easy.email.smtp.provider;

import com.gc.easy.email.smtp.service.IEmailTemplateService;
import com.gc.easy.email.smtp.service.SmtpEmailService;
import com.gc.easy.email.smtp.service.impl.DefaultEmailTemplateService;
import com.gc.easy.sms.core.config.EmailConfiguration;
import com.gc.easy.sms.core.provider.EmailServiceProvider;
import com.gc.easy.sms.core.model.SmsVendor;
import com.gc.easy.sms.core.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmtpEmailServiceProvider implements EmailServiceProvider {
    @Autowired(required = false)
    private IEmailTemplateService templateService;
    @Override
    public SendService createService(EmailConfiguration config) {
        if (SmsVendor.SMTP_EMAIL.getVendorCode().equals(config.getVendor())) {
            return new SmtpEmailService(config,templateService==null?new DefaultEmailTemplateService():templateService);
        }
        return null;
    }
}