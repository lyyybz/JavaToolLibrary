package com.lyyybz.communication.http;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class HttpsContext
{
	private SSLContext sslContext;
	private HostnameVerifier verifier;
	private final KeyStoreData data;

	public HttpsContext(KeyStoreData data)
	{
		this.data = data;
	}

	public SSLContext getSSLContext() throws Exception
	{
		if (sslContext != null)
		{
			return sslContext;
		}

		sslContext = generateSSLContext();
		return sslContext;
	}

	public HostnameVerifier getHostnameVerifier()
	{
		if (verifier != null)
		{
			return verifier;
		}

		verifier = new HostnameVerifier()
		{
			public boolean verify(String hostname, SSLSession session)
			{
				if (data.hostname.equals(hostname))
				{
					return true;
				}
				return false;
			}
		};
		return verifier;
	}

	private SSLContext generateSSLContext() throws Exception
	{
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore keyStore = generateJksKeyStore();
		keyManagerFactory.init(keyStore, data.password.toCharArray());
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore trustStore = keyStore;
		trustManagerFactory.init(trustStore);
		SSLContext ctx = SSLContext.getInstance("SSL");
		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		return ctx;
	}

	private KeyStore generateJksKeyStore() throws Exception
	{
		KeyStore keyStore = KeyStore.getInstance("JKS");
		ByteArrayInputStream bais = new ByteArrayInputStream(data.content);
		keyStore.load(bais, data.password.toCharArray());
		bais.close();
		return keyStore;
	}

	public static class KeyStoreData
	{
		public final byte[] content;
		public final String password;
		public final String hostname;

		public KeyStoreData(byte[] content, String password, String hostname)
		{
			if (content == null || password == null || hostname == null)
				throw new IllegalArgumentException("para is null");
			this.content = content;
			this.password = password;
			this.hostname = hostname;
		}
	}
}
