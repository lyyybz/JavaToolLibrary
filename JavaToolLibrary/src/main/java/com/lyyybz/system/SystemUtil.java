package com.lyyybz.system;

import java.nio.charset.Charset;
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
}
