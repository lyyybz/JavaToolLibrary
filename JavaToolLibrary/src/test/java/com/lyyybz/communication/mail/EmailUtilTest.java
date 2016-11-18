package com.lyyybz.communication.mail;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EmailUtilTest
{

	@Test
	public void test()
	{
		EmailUtil emailUtil = new EmailUtil.Builder(
				"954111430@qq.com")
				.mailServerHost("smtp.ehomeclouds.com.cn")
				.mailServerPort("25")
				.sourceAddr("warning_notification@ehomeclouds.com.cn")
				.userName("warning_notification@ehomeclouds.com.cn")
				.password("Eastsoft666").build();
		boolean result = emailUtil.sendTextMail("Test", "Don't worry, this is a test email - -!");
		assertTrue(result);
	}

}
