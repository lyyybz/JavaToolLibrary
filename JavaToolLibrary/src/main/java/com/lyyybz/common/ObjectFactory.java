package com.lyyybz.common;

public class ObjectFactory
{
	public static Object create(ClassLoader loader, String classname)
	{
		Object object = null;
		if(classname  == null || loader == null)
		{
			return null;
		}
		try
		{
			object = loader.loadClass(classname).newInstance();
		} catch (Exception e)
		{
			LogUtil.exception(e);
		} 
		return object;
	}
}