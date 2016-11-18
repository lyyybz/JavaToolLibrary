package com.lyyybz.communication.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.lyyybz.common.LogUtil;

public class MailUtil
{
	private final String mailServerHost;
	private final String mailServerPort;
	private final String targetAddr;
	private final String sourceAddr;
	private final String userName;
	private final String password;

	public MailUtil(Builder builder)
	{
		this.targetAddr = builder.targetAddr;
		
		this.mailServerHost = builder.mailServerHost;
		this.mailServerPort = builder.mailServerPort;
		this.sourceAddr = builder.sourceAddr;
		this.userName = builder.userName;
		this.password = builder.password;
	}

	public boolean sendTextMail(String mailSubject, String mailContent)
	{
		Properties pro = getProperties();
		MyAuthenticator authenticator = new MyAuthenticator(userName, password);
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try
		{
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(sourceAddr);
			mailMessage.setFrom(from);
			Address to = new InternetAddress(targetAddr);
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailSubject);
			mailMessage.setSentDate(new Date());
			mailMessage.setText(mailContent);
			Transport.send(mailMessage);
			return true;
		} catch (Exception e)
		{
			LogUtil.exception(e);
			return false;
		}
	}

	private Properties getProperties()
	{
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", "true");
		return p;
	}

	public class MyAuthenticator extends Authenticator
	{
		String userName;
		String password;

		public MyAuthenticator(String username, String password)
		{
			this.userName = username;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(userName, password);
		}
	}
	
	public static class Builder
	{
		private String mailServerHost;
		private String mailServerPort;
		private String targetAddr;
		private String sourceAddr;
		private String userName;
		private String password;
		
		public Builder(String targetAddr)
		{
			this.mailServerHost = "";
			this.mailServerPort = "25";
			this.targetAddr = targetAddr;
			this.sourceAddr = "";
			this.userName = "";
			this.password = "";
		}

		public Builder mailServerHost(String mailServerHost)
		{
			this.mailServerHost = mailServerHost;
			return this;
		}

		public Builder mailServerPort(String mailServerPort)
		{
			this.mailServerPort = mailServerPort;
			return this;
		}

		public Builder sourceAddr(String sourceAddr)
		{
			this.sourceAddr = sourceAddr;
			return this;
		}

		public Builder userName(String userName)
		{
			this.userName = userName;
			return this;
		}

		public Builder password(String password)
		{
			this.password = password;
			return this;
		}
		
		public MailUtil build()
		{
			return new MailUtil(this);
		}
	}
	
}
