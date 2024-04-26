package com.gc.easy.sms.core.config;


//配置变化监听器
public interface SmsConfigurationListener {
    default void onConfigurationChange(SmsConfiguration newConfig){

    }
}
