package com.gc.easy.email.smtp.service;

import java.util.Map;

public class TemplateEngine {
    public static String renderTemplate(String templateContent, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            templateContent = templateContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return templateContent;
    }
}