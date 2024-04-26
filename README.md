# 📚 Java 通信套件 (easy-sms)

## 🌟 项目介绍

`Java 通信套件` 是一个为企业级应用和开发者设计的多功能通信服务库，旨在通过Java应用程序简化短信和电子邮件的发送流程。本套件目前支持阿里云短信服务、腾讯短信服务以及SMTP电子邮件服务，未来将扩展支持更多服务商，以满足不断增长的市场需求。

## 🚀 快速开始

在您的 `pom.xml` 文件中添加以下依赖项，以将 `Java 通信套件` 集成到您的项目中：

```xml
<dependencies>
    <!-- 添加阿里云短信服务依赖 -->
    <dependency>
        <groupId>io.github.javpower</groupId>
        <artifactId>sms-ali</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    
    <!-- 添加腾讯短信服务依赖 -->
    <dependency>
        <groupId>io.github.javpower</groupId>
        <artifactId>sms-tencent</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    
    <!-- 添加SMTP电子邮件服务依赖 -->
    <dependency>
        <groupId>io.github.javpower</groupId>
        <artifactId>email-smtp</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 🎯 核心优势

- **服务商中立**：套件设计为服务商中立，允许轻松切换不同的短信和邮件服务商。
- **易于集成**：通过简单的API调用，快速集成短信和邮件发送功能。
- **实时配置更新**：监听器模式支持实时更新配置，无需重启应用，提高应用的灵活性和响应速度。
- **扩展性强**：模块化设计，方便添加新的服务商支持和功能扩展。
- **异常处理**：提供详细的异常处理机制，确保通信的可靠性。

## 🛠️ 使用指南

以下是如何在Spring Boot应用程序中使用 `Java 通信套件` 的示例：

```java
@Component
@Order(value = 1) // 指定执行顺序
public class TestRun implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String tenant = "1号";
        // 测试电子邮件发送
        testEmail(tenant);
        // 测试阿里云短信发送
        testAli(tenant);
        // 测试腾讯短信发送
        testTen(tenant);
    }

    // 测试电子邮件发送方法
    public void testEmail(String tenant) throws SendingException {
        EmailConfiguration emailConfiguration = EmailConfiguration.builder()
                .vendor(SmsVendor.SMTP_EMAIL.getVendorCode())
                .username("xxxx")
                .password("xxxxx")
                .host("smtp.qq.com")
                .port(465).build();
        
        // 配置监听器以处理配置变更
        EmailBuilder.addListener(new EmailConfigurationListener() {
            @Override
            public void onConfigurationChange(EmailConfiguration newConfig) {
                // 处理电子邮件配置变更...
            }
        });
        
        // 使用构建器发送邮件
        EmailBuilder.factory(tenant, emailConfiguration)
                .subject("测试邮件主题")
                .to("recipient@example.com")
                .param("code", "123456")
                .onSend(response -> {
                    if (response.isSuccess()) {
                        System.out.println("邮件发送成功。");
                    } else {
                        System.out.println("邮件发送失败：" + response.getMessage());
                    }
                })
                .send();
    }

    // 测试阿里云短信发送方法
    public void testAli(String tenant) throws SendingException {
        // 配置和发送逻辑...
        SmsConfiguration aliConfig = SmsConfiguration.builder().
                apiKey("hello").
                apiSecret("world").
                vendor(SmsVendor.ALIYUN.getVendorCode()).build();
        SmsBuilder.addListener(new SmsConfigurationListener() {
            @Override
            public void onConfigurationChange(SmsConfiguration newConfig) {
                // 处理配置变化，如重新初始化服务等
                System.out.println(tenant+"配置变化了:"+newConfig.toString());
            }
        });
        SmsBuilder.factory(tenant,aliConfig)
                .signName("xxxxx")
                .to("13988888888")
                .templateId("template123")
                .param("name", "John")
                .onSend(response -> {
                    if (response.isSuccess()) {
                        System.out.println("SMS sent successfully."+response.getMessage());
                    } else {
                        System.out.println("Failed to send SMS: " + response.getMessage());
                    }
                })
                .send();
    }

    // 测试腾讯短信发送方法
    public void testTen(String tenant) throws SendingException {
        // 配置和发送逻辑...
        SmsConfiguration tenConfig = SmsConfiguration.builder().
                apiKey("hello").
                apiSecret("world").
                vendor(SmsVendor.TENCENT.getVendorCode()).build();
        SmsBuilder.addListener(new SmsConfigurationListener() {
            @Override
            public void onConfigurationChange(SmsConfiguration newConfig) {
                // 处理配置变化，如重新初始化服务等
                System.out.println(tenant+"配置变化了:"+newConfig.toString());
            }
        });
        SmsBuilder.factory(tenant,tenConfig)
                .signName("xxxxx")
                .appId("xxxx")
                .senderid("")//国际专用
                .to("13988888888")
                .templateId("template123")
                .param("name", "John")
                .onSend(response -> {
                    if (response.isSuccess()) {
                        System.out.println("SMS sent successfully."+response.getMessage());
                    } else {
                        System.out.println("Failed to send SMS: " + response.getMessage());
                    }
                })
                .send();
    }
}
```

## 📜 注意事项

- **版本兼容性**：确保您添加的依赖版本与您的项目兼容。
- **敏感信息管理**：不要在代码中硬编码敏感信息，如API密钥和密码。使用配置文件或环境变量来管理这些信息。
- **服务商限制**：了解并遵守您选择的短信或电子邮件服务商的使用限制和条款。
- **异常处理**：在使用套件发送短信或电子邮件时，确保您的应用程序能够妥善处理可能发生的异常。
- **更新日志**：在升级套件版本时，查阅更新日志以了解新特性、改进和可能的破坏性变更。
- **文档参考**：对于详细的API文档和高级使用案例，请参考文档。
- **社区参与**：积极参与社区讨论，帮助解决问题并分享您的经验。

## 🔍 项目路线图

- **服务商扩展**：未来版本将增加对更多短信和邮件服务商的支持。
- **功能增强**：计划引入更多高级功能，如邮件模板管理、短信签名管理等。
- **性能优化**：持续优化套件性能，提供更快的发送速度和更高的吞吐量。

## 🤝 贡献指南

我们欢迎任何形式的贡献，包括但不限于：

- **代码提交**：提交pull request以修复bug或添加新功能。
- **文档改进**：帮助我们改进和完善文档。
- **社区支持**：在论坛和社区中帮助其他用户解决问题。

## 📜 许可证

本项目遵循 [Apache License 2.0](LICENSE)。

## 🙏 致谢

感谢所有开源贡献者和社区成员的支持。

---