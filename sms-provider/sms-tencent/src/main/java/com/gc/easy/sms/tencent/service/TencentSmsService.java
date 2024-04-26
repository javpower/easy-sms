package com.gc.easy.sms.tencent.service;

import cn.hutool.core.util.StrUtil;
import com.gc.easy.sms.core.config.SmsConfiguration;
import com.gc.easy.sms.core.exception.SendingException;
import com.gc.easy.sms.core.model.SendResponse;
import com.gc.easy.sms.core.service.SendService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class TencentSmsService implements SendService {
    private SmsConfiguration config;
    private com.tencentcloudapi.sms.v20190711.SmsClient client; // 腾讯云短信服务客户端

    public TencentSmsService(SmsConfiguration config) {
        this.config = config;
        initializeClient(config.getApiKey(),config.getApiSecret());
    }

    private void initializeClient(String apiKey, String apiSecret) {
        Credential cred = new Credential(apiKey, apiSecret);
        String endpoint = StringUtils.isEmpty(config.getEndpoint())? "sms.tencentcloudapi.com":config.getEndpoint();
        // 实例化一个http选项，可选，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        /* SDK有默认的超时时间，非必要请不要进行调整
         * 如有需要请在代码中查阅以获取最新的默认值 */
        httpProfile.setConnTimeout(60);
        /* 指定接入地域域名，默认就近地域接入域名为 sms.tencentcloudapi.com ，也支持指定地域域名访问，例如广州地域的域名为 sms.ap-guangzhou.tencentcloudapi.com */
        httpProfile.setEndpoint(endpoint);
        /* 非必要步骤:
         * 实例化一个客户端配置对象，可以指定超时时间等配置 */
        ClientProfile clientProfile = new ClientProfile();
        /* SDK默认用TC3-HMAC-SHA256进行签名
         * 非必要请不要修改这个字段 */
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        /* 实例化要请求产品(以sms为例)的client对象
         * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，支持的地域列表参考 https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8 */
        this.client = new SmsClient(cred, "ap-guangzhou",clientProfile);
    }
    @Override
    public void sendTemplate(String to, String templateId, Map<String, String> params) throws SendingException {
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(config.getAppId());
        req.setSign(config.getSignName());
        req.setTemplateID(templateId);
        /* 模板参数: 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空 */
        String[] templateParamSet = params.values().toArray(new String[]{});
        req.setTemplateParamSet(templateParamSet);
        /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
         * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号 */
        String[] phoneNumberSet = to.split(",");
        req.setPhoneNumberSet(phoneNumberSet);
        /* 用户的 session 内容（无需要可忽略）: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
        String sessionContext = "";
        req.setSessionContext(sessionContext);
        /* 国内短信无需填写该项；国际/港澳台短信已申请独立 SenderId 需要填写该字段，默认使用公共 SenderId，无需填写该字段。注：月度使用量达到指定量级可申请独立 SenderId 使用，详情请联系 [腾讯云短信小助手](https://cloud.tencent.com/document/product/382/3773#.E6.8A.80.E6.9C.AF.E4.BA.A4.E6.B5.81)。*/
        req.setSenderId(config.getSenderid());
        /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
         * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
        try {
            SendSmsResponse res = client.SendSms(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public SendResponse sendTemplateWithResponse(String to, String templateId, Map<String, String> params) {
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