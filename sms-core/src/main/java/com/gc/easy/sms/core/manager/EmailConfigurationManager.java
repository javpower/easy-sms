package com.gc.easy.sms.core.manager;

import com.gc.easy.sms.core.config.EmailConfiguration;
import com.gc.easy.sms.core.config.EmailConfigurationListener;
import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.model.StatusConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EmailConfigurationManager {

    private final ConcurrentHashMap<String, EmailConfiguration> configurations = new ConcurrentHashMap<>();
    private List<EmailConfigurationListener> listeners = new ArrayList<>();
    public void addListener(EmailConfigurationListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(EmailConfiguration newConfig) {
        listeners.forEach(listener -> listener.onConfigurationChange(newConfig));
    }
    public EmailConfiguration getConfiguration(String tenantId) {
        return configurations.get(tenantId);
    }

    public StatusConfig addOrUpdateConfiguration(String tenantId, EmailConfiguration config) {
        if(config==null){
            return StatusConfig.NONE;
        }
        EmailConfiguration existingConfig = configurations.get(tenantId);
        //新增
        if(existingConfig==null){
            configurations.put(tenantId, config);
            notifyListeners(config);
            return StatusConfig.CREATE;
        }else if(config!=null&&!existingConfig.equals(config)){
            //更新
            configurations.put(tenantId, config);
            notifyListeners(config);
            return StatusConfig.UPDATE;
        }
        return StatusConfig.NONE;
    }

    public void removeConfiguration(String tenantId) {
        configurations.remove(tenantId);
    }
}