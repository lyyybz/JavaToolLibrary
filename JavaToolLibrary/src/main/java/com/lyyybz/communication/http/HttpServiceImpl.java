package com.lyyybz.communication.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpServiceImpl implements IHttpService
{
	private final String url;
	private final int readTimeout;
	private final int connectTimeout;
	private HttpRequestMethodEnum requestMethod;
	private HttpURLConnection conn;
	private final Map<String, String> requestProperties;

	private HttpServiceImpl(Builder builder)
	{
		this.url = builder.url;
		this.readTimeout = builder.readTimeout;
		this.connectTimeout = builder.connectTimeout;
		this.requestMethod = builder.requestMethod;
		requestProperties = builder.requestProperties;

	}

	public HttpResult getHttpResult(byte[] content) throws IOException
	{
		initConnection();
		if (content != null)
		{
			writeContent(content);
		}

		return recvResult();
	}

	private void initConnection() throws IOException
	{
		URL url = new URL(this.url);
		conn = (HttpURLConnection) url.openConnection();
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

	public static class Builder
	{
		private String url;
		private int readTimeout = 3000;
		private int connectTimeout = 5000;
		private HttpRequestMethodEnum requestMethod;
		private Map<String, String> requestProperties;

		public Builder(String url)
		{
			this.url = url;
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

		public HttpServiceImpl build()
		{
			return new HttpServiceImpl(this);
		}
	}

}
