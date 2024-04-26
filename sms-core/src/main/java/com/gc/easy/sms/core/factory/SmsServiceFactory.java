package com.gc.easy.sms.core.factory;

import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.manager.SmsConfigurationManager;
import com.gc.easy.sms.core.manager.SmsServiceProviderRegistry;
import com.gc.easy.sms.core.service.SendService;

import java.util.concurrent.ConcurrentHashMap;

public class SmsServiceFactory {
    private final SmsConfigurationManager configurationManager;
    private final ConcurrentHashMap<String, SendService> sendServiceConcurrentHashMap = new ConcurrentHashMap<>();

    public SmsServiceFactory(SmsConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public SendService createService(String tenantId) {
        SmsConfiguration config = configurationManager.getConfiguration(tenantId);
        if (config == null) {
            throw new IllegalArgumentException("No configuration found for tenant: " + tenantId);
        }
        // 根据配置信息动态创建短信服务实例
        SendService service = SmsServiceProviderRegistry.createService(tenantId, configurationManager);
        sendServiceConcurrentHashMap.put(tenantId,service);
        return service;
    }
    public void removeService(String tenantId){
        sendServiceConcurrentHashMap.get(tenantId).removeService();
        sendServiceConcurrentHashMap.remove(tenantId);
    }

    public SendService getService(String tenantId) {
        SmsConfiguration config = configurationManager.getConfiguration(tenantId);
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
}