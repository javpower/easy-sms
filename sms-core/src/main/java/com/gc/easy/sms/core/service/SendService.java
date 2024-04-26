package com.gc.easy.sms.core.service;

import com.gc.easy.sms.core.exception.SendingException;
import com.gc.easy.sms.core.model.SendResponse;

import java.util.List;
import java.util.Map;

public interface SendService {

    void removeService();

    void send(String to, String message) throws SendingException;

    SendResponse sendWithResponse(String to, String message) throws SendingException;

    void sendTemplate(String to, String templateId, Map<String, String> params) throws SendingException;

    SendResponse sendTemplateWithResponse(String to, String templateId, Map<String, String> params) throws SendingException;

    void sendBulk(List<String> tos, String message) throws SendingException;

    SendResponse sendBulkWithResponse(List<String> tos, String message) throws SendingException;

    void sendBulkWithTemplate(List<String> tos, String templateId, Map<String, String> templateParams) throws SendingException;

    SendResponse sendBulkWithTemplateResponse(List<String> tos, String templateId, Map<String, String> templateParams) throws SendingException;
}