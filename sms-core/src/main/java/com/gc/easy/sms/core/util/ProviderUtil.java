package com.gc.easy.sms.core.util;

import com.gc.easy.sms.core.provider.EmailServiceProvider;
import com.gc.easy.sms.core.provider.SmsServiceProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProviderUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    // 缓存所有短信服务提供者的引用
    private static Map<String, SmsServiceProvider> smsServiceProviders;
    private static Map<String, EmailServiceProvider> emailServiceProviders;


    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
        // 缓存所有短信服务提供者
        smsServiceProviders = applicationContext.getBeansOfType(SmsServiceProvider.class);
        emailServiceProviders = applicationContext.getBeansOfType(EmailServiceProvider.class);

    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static Map<String, SmsServiceProvider> getSmsServiceProviders() {
        if (smsServiceProviders == null) {
            throw new IllegalStateException("SpringUtil has not been initialized. Ensure the application context is set.");
        }
        return smsServiceProviders;
    }
    public static Map<String, EmailServiceProvider> getEmailServiceProviders() {
        if (emailServiceProviders == null) {
            throw new IllegalStateException("SpringUtil has not been initialized. Ensure the application context is set.");
        }
        return emailServiceProviders;
    }

}