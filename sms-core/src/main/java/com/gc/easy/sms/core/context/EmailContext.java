package com.gc.easy.sms.core.context;

import com.gc.easy.sms.core.factory.EmailServiceFactory;
import com.gc.easy.sms.core.manager.EmailConfigurationManager;

public class EmailContext {
    public static final EmailConfigurationManager configurationManager = new EmailConfigurationManager();
    public static final EmailServiceFactory serviceFactory = new EmailServiceFactory(configurationManager);

}
