    @Component
    @Order(value = 1)//执行顺序
    public class TestRun implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		String tenant="1号";
		testEmail(tenant);
	}


	public void testEmail(String tenant) throws SendingException {
		EmailConfiguration emailConfiguration = EmailConfiguration.builder()
				.vendor(SmsVendor.SMTP_EMAIL.getVendorCode())
				.username("xxxx")
				.password("xxxxx")
				.host("smtp.qq.com")
				.port(465).build();
		EmailBuilder.addListener(new EmailConfigurationListener() {
			@Override
			public void onConfigurationChange(EmailConfiguration newConfig) {
				// 处理配置变化，如重新初始化服务等
				System.out.println(tenant+"配置变化了:"+newConfig.toString());
			}
		});
		EmailBuilder.factory(tenant,emailConfiguration)
				.templateId("111")
				.subject("测试")
				.to("javpower@163.com")
				.param("code","Look Look")
				.onSend(response -> {
					if (response.isSuccess()) {
						System.out.println("Email sent successfully."+response.getMessage());
					} else {
						System.out.println("Failed to send Email: " + response.getMessage());
					}
				})
				.send();
	}
	public void testAli(String tenant) throws SendingException {
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
	public void testTen(String tenant) throws SendingException {
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