package com.gc.easy.sms.core.manager;

import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.provider.SmsServiceProvider;
import com.gc.easy.sms.core.service.SendService;
import com.gc.easy.sms.core.util.ProviderUtil;

import java.util.Map;

//务提供者注册表，用于存储和查找短信服务提供者

public class SmsServiceProviderRegistry {

    //根据配置信息动态创建短信服务实例
    public static SendService createService(String tenantId, SmsConfigurationManager configurationManager) {
        SmsConfiguration config = configurationManager.getConfiguration(tenantId);
        // 获取所有短信服务提供者
        Map<String, SmsServiceProvider> providers = ProviderUtil.getSmsServiceProviders();
        for (SmsServiceProvider provider : providers.values()) {
            SendService service = provider.createService(config);
            if (service != null) {
                return service;
            }
        }
        throw new IllegalArgumentException("No SmsService implementation found for tenant: " + tenantId);
    }
}