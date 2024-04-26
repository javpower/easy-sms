package com.gc.easy.sms.core.builder;

import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.config.SmsConfigurationListener;
import com.gc.easy.sms.core.context.SmsContext;
import com.gc.easy.sms.core.exception.SendingException;
import com.gc.easy.sms.core.model.SendResponse;
import com.gc.easy.sms.core.model.StatusConfig;
import com.gc.easy.sms.core.service.SendService;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SmsBuilder {
    public static SmsBuilder factory(String tenantId, SmsConfiguration config) {
        return new SmsBuilder(tenantId, config);
    }
    public static SmsBuilder factory(String tenantId) {
        return new SmsBuilder(tenantId, null);
    }
    public SmsBuilder onSend(Consumer<SendResponse> onSendCallback) {
        // 存储回调函数
        this.onSendCallback = onSendCallback;
        return this;
    }
    private Consumer<SendResponse> onSendCallback;
    private SendService service;
    private String to;
    private List<String> toNumbers;
    private String templateId;
    private String message;
    private String tenantId;
    private final Map<String, String> templateParams;

    public static void addListener(SmsConfigurationListener listener) {
        SmsContext.configurationManager.addListener(listener);
    }
    public static void remove(String tenantId) {
        SmsContext.serviceFactory.removeService(tenantId);
        SmsContext.configurationManager.removeConfiguration(tenantId);
    }
    private SmsBuilder(String tenantId, SmsConfiguration config) {
        StatusConfig status = SmsContext.configurationManager.addOrUpdateConfiguration(tenantId, config);
        switch (status){
            case NONE:
                this.service=SmsContext.serviceFactory.getService(tenantId);
                break;
            case CREATE:
                this.service=SmsContext.serviceFactory.createService(tenantId);
                break;
            case UPDATE:
                this.service =SmsContext.serviceFactory.updateService(tenantId);
                break;
        }
        this.templateParams = new HashMap<>();
        this.tenantId=tenantId;
    }

    public SmsBuilder to(String to) {
        this.to = to;
        return this;
    }
    public SmsBuilder message(String message) {
        this.message = message;
        return this;
    }
    public SmsBuilder signName(String signName) {
        SmsConfiguration configuration =SmsContext.configurationManager.getConfiguration(tenantId);
        if(configuration!=null){
            configuration.setSignName(signName);
        }
        return this;
    }
    public SmsBuilder appId(String appId) {
        SmsConfiguration configuration =SmsContext.configurationManager.getConfiguration(tenantId);
        if(configuration!=null){
            configuration.setAppId(appId);
        }
        return this;
    }
    public SmsBuilder senderid(String senderid) {
        SmsConfiguration configuration =SmsContext.configurationManager.getConfiguration(tenantId);
        if(configuration!=null){
            configuration.setSenderid(senderid);
        }
        return this;
    }
    public SmsBuilder endpoint(String endpoint) {
        SmsConfiguration configuration =SmsContext.configurationManager.getConfiguration(tenantId);
        if(configuration!=null){
            configuration.setSignName(endpoint);
        }
        return this;
    }

    public SmsBuilder templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public SmsBuilder param(String key, String value) {
        this.templateParams.put(key, value);
        return this;
    }
    public SmsBuilder param(Map<String,String> templateParams) {
        this.templateParams.putAll(templateParams);
        return this;
    }

    public void send() throws SendingException {
        SendResponse response;
        if (this.templateId != null) {
            if(!CollectionUtils.isEmpty(toNumbers)){
                response = this.service.sendBulkWithTemplateResponse(toNumbers, templateId, templateParams);
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
    public SmsBuilder toNumbers(List<String> toNumbers) {
        this.toNumbers = toNumbers;
        return this;
    }

    // 批量发送的链式调用方法
    public void sendBulk() throws SendingException {
        SendResponse response;
        if (templateId != null) {
            response = service.sendBulkWithTemplateResponse(toNumbers, templateId, templateParams);
        } else {
            response = service.sendBulkWithResponse(toNumbers, message);
        }
        if (this.onSendCallback != null) {
            this.onSendCallback.accept(response);
        }
    }
    // 链式调用的其他方法...
}