package com.gc.easy.sms.core.factory;

import com.gc.easy.sms.core.config.EmailConfiguration;
import com.gc.easy.sms.core.manager.EmailConfigurationManager;
import com.gc.easy.sms.core.manager.EmailServiceProviderRegistry;
import com.gc.easy.sms.core.service.SendService;

import java.util.concurrent.ConcurrentHashMap;

public class EmailServiceFactory {
    private final EmailConfigurationManager configurationManager;
    private final ConcurrentHashMap<String, SendService> sendServiceConcurrentHashMap = new ConcurrentHashMap<>();


    public EmailServiceFactory(EmailConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }
    public SendService createService(String tenantId) {
        EmailConfiguration config = configurationManager.getConfiguration(tenantId);
        if (config == null) {
            throw new IllegalArgumentException("No configuration found for tenant: " + tenantId);
        }
        // 根据配置信息动态创建短信服务实例
        SendService service = EmailServiceProviderRegistry.createService(tenantId, configurationManager);
        sendServiceConcurrentHashMap.put(tenantId,service);
        return service;
    }

    public SendService getService(String tenantId) {
        EmailConfiguration config = configurationManager.getConfiguration(tenantId);
        if (config == null) {
            throw new IllegalArgumentException("No configuration found for tenant: " + tenantId);
        }
        // 根据配置信息动态创建短信服务实例
        return sendServiceConcurrentHashMap.get(tenantId);
    }

    public SendService updateService(String tenantId) {
        removeService(tenantId);
        return createService(tenantId);
    }
    public void removeService(String tenantId){
        sendServiceConcurrentHashMap.get(tenantId).removeService();
        sendServiceConcurrentHashMap.remove(tenantId);
    }
}