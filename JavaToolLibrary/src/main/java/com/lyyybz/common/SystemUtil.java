package com.lyyybz.common;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.SortedMap;

public class SystemUtil
{
	/**
	 *	获取当前系统支持的编码 
	 */
	public SortedMap<String, Charset> getAvailableCharsets()
	{
	    return Charset.availableCharsets();
	}
	
	public static void getProperties()
	{
		Properties properties = System.getProperties();
	    System.out.println(properties.toString());
	}
	
	public static String getJdkVersion()
	{
	    String arch = System.getProperty("sun.arch.data.model");
	    System.out.println(arch);
	    return arch;
	}
	
	public static void main(String[] args)
	{
		getJdkVersion();
		getProperties();
	}
}
