package com.lyyybz.convert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtil
{
	private static Gson gson = new Gson();
	
	public static String toJson(Object obj)
	{
		if(obj == null)
		{
			return null;
		}
		return gson.toJson(obj);
	}

	public static String toFormatJson(Object obj)
	{
		try
		{
			if(obj == null)
			{
				return null;
			}
			String tmp = gson.toJson(obj);
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(tmp);
			return g.toJson(je);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT)
	{
		try
		{
			return gson.fromJson(json, classOfT);
		} catch (JsonSyntaxException e)
		{
			return null;
		}
	}

}
