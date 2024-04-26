package com.gc.easy.sms.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsConfiguration {
    private String vendor;
    private String apiKey;
    private String apiSecret;
    // 其他配置信息...
    private String signName;
    private String endpoint;
    private String appId;
    private String senderid;//腾讯国际短信专用



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmsConfiguration)) return false;
        SmsConfiguration that = (SmsConfiguration) o;
        return Objects.equals(vendor, that.vendor) &&
               Objects.equals(apiKey, that.apiKey) &&
               Objects.equals(apiSecret, that.apiSecret);
        // 检查其他配置信息...
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendor, apiKey, apiSecret);
        // 计算其他配置信息的哈希值...
    }
}