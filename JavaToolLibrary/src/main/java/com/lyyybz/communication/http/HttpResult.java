package com.lyyybz.communication.http;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpResult
{
	public final Map<String, List<String>> headerFields;
	public final int responseCode;
	public final byte[] content;

	public HttpResult(Map<String, List<String>> headerFields, int responseCode, byte[] content)
	{
		this.headerFields = headerFields;
		this.responseCode = responseCode;
		this.content = content;
	}

	public boolean isSuccess()
	{
		return (responseCode == 200);
	}

	public String getHanderElement(String elementName) throws Exception
	{
		List<String> tmpList = headerFields.get(elementName);
		if (tmpList == null || tmpList.size() != 1)
		{
			throw new Exception("HttpResult does not contain " + elementName);
		}
		return tmpList.get(0);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("HttpResult[");
		sb.append(responseCode + ",");
		sb.append(headerFields.toString() + ",");
		String str = new String(content);
		if (str.length() > 500)
		{
			str = str.substring(0, 500);
		}
		sb.append(str);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpResult other = (HttpResult) obj;
		if (responseCode != other.responseCode)
			return false;
		if (Arrays.equals(content, other.content) != true)
			return false;
		if (headerFields.equals(other.headerFields) == false)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + responseCode;
		result = result * 31 + headerFields.hashCode();
		if (content == null)
		{
			return result;
		}
		for (int i = 0; i < content.length; i++)
		{
			result = result * 31 + (int) content[i];
		}
		return result;
	}
}