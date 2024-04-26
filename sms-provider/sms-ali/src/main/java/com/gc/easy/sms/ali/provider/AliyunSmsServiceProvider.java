package com.gc.easy.sms.ali.provider;

import com.gc.easy.sms.ali.service.AliyunSmsService;
import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.provider.SmsServiceProvider;
import com.gc.easy.sms.core.model.SmsVendor;
import com.gc.easy.sms.core.service.SendService;
import org.springframework.stereotype.Service;

@Service
public class AliyunSmsServiceProvider implements SmsServiceProvider {

    @Override
    public SendService createService(SmsConfiguration config) {
        if (SmsVendor.ALIYUN.getVendorCode().equals(config.getVendor())) {
            return new AliyunSmsService(config);
        }
        return null;
    }

}