package com.lyyybz.convert;

public class ByteUtil
{
	/*
	 * 反转字节数组
	 */
	public static byte[] invertOrder(byte[] data)
	{
	     if(data == null)
	          return null;
	     for(int i = 0; i < data.length/2; i++)
	     {
	          byte tmp = data[i];
	          data[i] = data[data.length - i - 1];
	          data[data.length - i - 1] = tmp;
	     }
	     return data;
	}
}
