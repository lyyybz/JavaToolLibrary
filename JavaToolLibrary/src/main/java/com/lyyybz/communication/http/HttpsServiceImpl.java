package com.lyyybz.communication.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpsServiceImpl implements IHttpService
{
	private final String url;
	private final int readTimeout;
	private final int connectTimeout;
	private HttpRequestMethodEnum requestMethod;
	private HttpsURLConnection conn;
	private Map<String, String> requestProperties;
	private final HttpsContext context;

	private HttpsServiceImpl(Builder builder)
	{
		this.url = builder.url;
		this.context = builder.ctx;
		this.readTimeout = builder.readTimeout;
		this.connectTimeout = builder.connectTimeout;
		this.requestMethod = builder.requestMethod;
		this.requestProperties = builder.requestProperties;
	}

	public HttpResult getHttpResult(byte[] content) throws Exception
	{
		initConnection();
		if (content != null)
		{
			writeContent(content);
		}

		return recvResult();
	}

	private void initConnection() throws Exception
	{
		URL url = new URL(this.url);
		conn = (HttpsURLConnection) url.openConnection();
		initHttpsURLConnection();
		conn.setReadTimeout(this.readTimeout);
		conn.setConnectTimeout(this.connectTimeout);
		conn.setRequestMethod(this.requestMethod.getValue());
		conn.setDoInput(true);
		conn.setDoOutput(true);

		for (String key : requestProperties.keySet())
		{
			conn.setRequestProperty(key, requestProperties.get(key));
		}

		conn.connect();
	}

	private void writeContent(byte[] content) throws IOException
	{
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content);
		out.flush();
		out.close();
	}

	private HttpResult recvResult() throws IOException
	{
		byte[] buff = new byte[0x1000];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int responseCode = conn.getResponseCode();
		while (true)
		{
			int len = 0;
			if (responseCode == 200)
			{
				len = conn.getInputStream().read(buff);
			} else
			{
				len = conn.getErrorStream().read(buff);
			}
			if (len < 0)
			{
				break;
			}
			baos.write(buff, 0, len);
		}
		baos.close();
		conn.disconnect();
		HttpResult httpResult = new HttpResult(conn.getHeaderFields(), responseCode, baos.toByteArray());
		return httpResult;
	}

	private void initHttpsURLConnection() throws Exception
	{
		if (context == null)
		{
			return;
		}
		SSLContext sslContext = context.getSSLContext();
		if (sslContext != null)
		{
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
		}
		conn.setHostnameVerifier(context.getHostnameVerifier());
	}

	public static class Builder
	{
		private String url;
		private int readTimeout = 3000;
		private int connectTimeout = 5000;
		private HttpRequestMethodEnum requestMethod;
		private Map<String, String> requestProperties;
		private final HttpsContext ctx;

		public Builder(String url, HttpsContext ctx)
		{
			this.url = url;
			this.ctx = ctx;
			requestProperties = new HashMap<String, String>();
			requestProperties.put("connection", "Keep-Alive");
			requestProperties.put("Content-Type", "application/json; charset=utf-8");

		}

		public Builder readTimeout(int readTimeout)
		{
			this.readTimeout = readTimeout;
			return this;
		}

		public Builder connectTimeout(int connectTimeout)
		{
			this.connectTimeout = connectTimeout;
			return this;
		}

		public Builder requestMethod(HttpRequestMethodEnum requestMethod)
		{
			this.requestMethod = requestMethod;
			return this;
		}

		public Builder requestPropertyies(String key, String value)
		{
			requestProperties.put(key, value);
			return this;
		}

		public HttpsServiceImpl build()
		{
			return new HttpsServiceImpl(this);
		}
	}
}
