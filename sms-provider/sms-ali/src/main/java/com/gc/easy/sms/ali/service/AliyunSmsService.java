package com.gc.easy.sms.ali.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.exception.SendingException;
import com.gc.easy.sms.core.model.SendResponse;
import com.gc.easy.sms.core.service.SendService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class AliyunSmsService implements SendService {
    private SmsConfiguration config;
    private com.aliyun.dysmsapi20170525.Client client; // 阿里云短信服务客户端

    public AliyunSmsService(SmsConfiguration config) {
        this.config = config;
        initializeClient(config.getApiKey(),config.getApiSecret());
    }

    private void initializeClient(String apiKey, String apiSecret) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(apiKey)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(apiSecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = StringUtils.isEmpty(config.endpoint)? "dysmsapi.aliyuncs.com":config.endpoint;
        try {
            this.client=new com.aliyun.dysmsapi20170525.Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void sendTemplate(String to, String templateId, Map<String, String> params) throws SendingException {
        // 实现阿里云短信模板发送逻辑
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName(config.getSignName())
                .setTemplateCode(templateId)
                .setTemplateParam(JSONUtil.toJsonStr(params))
                .setPhoneNumbers(to);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = null;
        try {
            response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if(response.getBody().getCode()!=null  && !response.getBody().getCode().equals("OK")) {
                throw new Exception("Failed to send SMS: " + response.getBody().getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SendResponse sendTemplateWithResponse(String to, String templateId, Map<String, String> params) {
        // 实现阿里云短信模板发送逻辑，并返回响应
        try {
            sendTemplate(to,templateId,params);
            return new SendResponse(true, "Template message sent successfully");
        } catch (SendingException e) {
            return new SendResponse(false, e.getMessage());
        }
    }


    @Override
    public SendResponse sendBulkWithResponse(List<String> toNumbers, String message) throws SendingException {
        // 实现阿里云短信批量发送逻辑，并返回响应
        return new SendResponse(true, "Bulk message sent successfully");
    }

    @Override
    public void sendBulkWithTemplate(List<String> toNumbers, String templateId, Map<String, String> templateParams) throws SendingException {
        // 实现阿里云短信模板批量发送逻辑
        String join = StrUtil.join(",", toNumbers);
        sendTemplate(join,templateId,templateParams);
    }

    @Override
    public SendResponse sendBulkWithTemplateResponse(List<String> toNumbers, String templateId, Map<String, String> templateParams) throws SendingException {
        // 实现阿里云短信模板批量发送逻辑，并返回响应
        String join = StrUtil.join(",", toNumbers);
        return sendTemplateWithResponse(join,templateId,templateParams);
    }


    @Override
    public void removeService() {
        client=null;
    }

    @Override
    public void send(String to, String message) throws SendingException {
        // 构建发送短信的请求

    }

    @Override
    public SendResponse sendWithResponse(String to, String message) throws SendingException {
        // 实现阿里云短信发送逻辑，并返回响应
        return new SendResponse(true, "Message sent successfully");
    }
    @Override
    public void sendBulk(List<String> toNumbers, String message) throws SendingException {
        // 实现阿里云短信批量发送逻辑

    }
}