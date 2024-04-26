package com.gc.easy.sms.tencent.provider;

import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.provider.SmsServiceProvider;
import com.gc.easy.sms.core.model.SmsVendor;
import com.gc.easy.sms.core.service.SendService;
import com.gc.easy.sms.tencent.service.TencentSmsService;
import org.springframework.stereotype.Service;

@Service
public class TencentSmsServiceProvider implements SmsServiceProvider {

    @Override
    public SendService createService(SmsConfiguration config) {
        if (SmsVendor.TENCENT.getVendorCode().equals(config.getVendor())) {
            return new TencentSmsService(config);
        }
        return null;
    }

}