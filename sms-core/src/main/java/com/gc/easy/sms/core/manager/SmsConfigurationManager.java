package com.gc.easy.sms.core.manager;

import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.config.SmsConfigurationListener;
import com.gc.easy.sms.core.model.StatusConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SmsConfigurationManager {

    private final ConcurrentHashMap<String, SmsConfiguration> configurations = new ConcurrentHashMap<>();
    private List<SmsConfigurationListener> listeners = new ArrayList<>();
    public void addListener(SmsConfigurationListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(SmsConfiguration newConfig) {
        listeners.forEach(listener -> listener.onConfigurationChange(newConfig));
    }
    public SmsConfiguration getConfiguration(String tenantId) {
        return configurations.get(tenantId);
    }

    public StatusConfig addOrUpdateConfiguration(String tenantId, SmsConfiguration config) {
       if(config==null){
           return StatusConfig.NONE;
       }
        SmsConfiguration existingConfig = configurations.get(tenantId);
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