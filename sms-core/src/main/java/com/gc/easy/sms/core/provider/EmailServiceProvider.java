package com.gc.easy.sms.core.provider;

import com.gc.easy.sms.core.config.EmailConfiguration;
import com.gc.easy.sms.core.service.SendService;

//服务提供者接口
public interface EmailServiceProvider {
    SendService createService(EmailConfiguration config);
}