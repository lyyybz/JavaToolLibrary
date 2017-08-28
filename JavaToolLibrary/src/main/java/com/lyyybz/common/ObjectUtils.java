package com.lyyybz.common;

public class ObjectUtils
{

	public static boolean equals(Object object1, Object object2)
	{
		return object1 == object2 ? true : (object1 != null && object2 != null ? object1.equals(object2) : false);
	}

	public static boolean notEqual(Object object1, Object object2)
	{
		return !equals(object1, object2);
	}
}
