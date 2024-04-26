package com.gc.easy.sms.core.context;

import com.gc.easy.sms.core.factory.SmsServiceFactory;
import com.gc.easy.sms.core.manager.SmsConfigurationManager;

public class SmsContext {
    public static final SmsConfigurationManager configurationManager = new SmsConfigurationManager();
    public static final SmsServiceFactory serviceFactory = new SmsServiceFactory(configurationManager);

}
