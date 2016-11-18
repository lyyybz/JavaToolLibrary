package com.lyyybz.convert;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lyyybz.common.LogUtil;

public class JsonUtil
{
	private static Gson gson = new Gson();
	
	public static String toString(Object obj)
	{
		if(obj == null)
		{
			return null;
		}
		return gson.toJson(obj);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT)
	{
		try
		{
			return gson.fromJson(json, classOfT);
		} catch (JsonSyntaxException e)
		{
			LogUtil.exception(e);
			return null;
		}
	}

}
