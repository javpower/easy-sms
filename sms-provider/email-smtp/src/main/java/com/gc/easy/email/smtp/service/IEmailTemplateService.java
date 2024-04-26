package com.gc.easy.email.smtp.service;

public interface IEmailTemplateService {
   default String loadTemplate(String templateId) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Verify Your Email Address</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            border: 1px solid #dddddd;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            margin: 0 0 10px;\n" +
                "            color: #333333;\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .header p {\n" +
                "            margin: 0;\n" +
                "            color: #666666;\n" +
                "        }\n" +
                "        .verification-code {\n" +
                "            margin: 30px 0;\n" +
                "            padding: 20px;\n" +
                "            background-color: #eeeeee;\n" +
                "            border: 2px solid #dddddd;\n" +
                "            text-align: center;\n" +
                "            font-size: 32px;\n" +
                "            font-weight: bold;\n" +
                "            color: #333333;\n" +
                "            border-radius: 6px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            margin-top: 30px;\n" +
                "            color: #888888;\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Verify Your Email</h1>\n" +
                "            <p>We're almost there! Please enter the following verification code to complete the registration process.</p>\n" +
                "        </div>\n" +
                "        <div class=\"verification-code\" th:text=\"${code}\">${code}</div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you didn't request this email, please ignore it. Need help? Contact our support team.</p>\n" +
                "            <p>&copy; 2023 YourCompanyName. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
