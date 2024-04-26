package com.gc.easy.sms.core.provider;

import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.service.SendService;
//服务提供者接口
public interface SmsServiceProvider {
    SendService createService(SmsConfiguration config);
}