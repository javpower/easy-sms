package com.gc.easy.sms.core.config;

import com.gc.easy.sms.core.model.SmsVendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfiguration {

    private String vendor= SmsVendor.SMTP_EMAIL.getVendorCode();
    private String host;
    private int port;
    private String username;
    private String password;
    private String subject;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailConfiguration)) return false;
        EmailConfiguration that = (EmailConfiguration) o;
        return Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(username, that.username)&&
                Objects.equals(password, that.password);
        // 检查其他配置信息...
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port,username,password);
        // 计算其他配置信息的哈希值...
    }
}