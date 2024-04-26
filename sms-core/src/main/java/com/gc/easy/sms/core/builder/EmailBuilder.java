package com.gc.easy.sms.core.builder;

import com.gc.easy.sms.core.config.EmailConfiguration;
import com.gc.easy.sms.core.config.EmailConfigurationListener;
import com.gc.easy.sms.core.context.EmailContext;
import com.gc.easy.sms.core.exception.SendingException;
import com.gc.easy.sms.core.model.SendResponse;
import com.gc.easy.sms.core.model.StatusConfig;
import com.gc.easy.sms.core.service.SendService;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EmailBuilder {
    public static EmailBuilder factory(String tenantId, EmailConfiguration config) {
        return new EmailBuilder(tenantId, config);
    }
    public static EmailBuilder factory(String tenantId) {
        return new EmailBuilder(tenantId, null);
    }
    public EmailBuilder onSend(Consumer<SendResponse> onSendCallback) {
        // 存储回调函数
        this.onSendCallback = onSendCallback;
        return this;
    }
    private Consumer<SendResponse> onSendCallback;
    private SendService service;
    private String to;
    private List<String> toEmails;
    private String templateId;
    private String message;
    private String tenantId;
    private final Map<String, String> templateParams;

    public static void addListener(EmailConfigurationListener listener) {
        EmailContext.configurationManager.addListener(listener);
    }
    public static void remove(String tenantId) {
        EmailContext.serviceFactory.removeService(tenantId);
        EmailContext.configurationManager.removeConfiguration(tenantId);

    }
    private EmailBuilder(String tenantId, EmailConfiguration config) {
        StatusConfig status = EmailContext.configurationManager.addOrUpdateConfiguration(tenantId, config);
        switch (status){
            case NONE:
                this.service=EmailContext.serviceFactory.getService(tenantId);
                break;
            case CREATE:
                this.service=EmailContext.serviceFactory.createService(tenantId);
                break;
            case UPDATE:
                this.service =EmailContext.serviceFactory.updateService(tenantId);
                break;
        }
        this.templateParams = new HashMap<>();
        this.tenantId=tenantId;
    }
    public EmailBuilder to(String to) {
        this.to = to;
        return this;
    }
    public EmailBuilder message(String message) {
        this.message = message;
        return this;
    }

    public EmailBuilder subject(String subject) {
        EmailConfiguration configuration = EmailContext.configurationManager.getConfiguration(tenantId);
        if(configuration!=null){
            configuration.setSubject(subject);
        }
        return this;
    }
    public EmailBuilder templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public EmailBuilder param(String key, String value) {
        this.templateParams.put(key, value);
        return this;
    }
    public EmailBuilder param(Map<String,String> templateParams) {
        this.templateParams.putAll(templateParams);
        return this;
    }

    public void send() throws SendingException {
        SendResponse response;
        if (this.templateId != null) {
            if(!CollectionUtils.isEmpty(toEmails)){
                response = this.service.sendBulkWithTemplateResponse(toEmails, templateId, templateParams);
            }else {
                response = this.service.sendTemplateWithResponse(to, templateId, templateParams);
            }
        } else {
            response = this.service.sendWithResponse(to, message);
        }
        if (this.onSendCallback != null) {
            this.onSendCallback.accept(response);
        }
    }
    public EmailBuilder toEmails(List<String> toEmails) {
        this.toEmails = toEmails;
        return this;
    }

    // 批量发送的链式调用方法
    public void sendBulk() throws SendingException {
        SendResponse response;
        if (templateId != null) {
            response = service.sendBulkWithTemplateResponse(toEmails, templateId, templateParams);
        } else {
            response = service.sendBulkWithResponse(toEmails, message);
        }
        if (this.onSendCallback != null) {
            this.onSendCallback.accept(response);
        }
    }
    // 链式调用的其他方法...
}