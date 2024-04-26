package com.gc.easy.sms.core.model;

public enum SmsVendor {
    ALIYUN("ALI"),
    TENCENT("TENCENT"),
    SMTP_EMAIL("SMTP_EMAIL");

    private final String vendorCode;

    SmsVendor(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }
}