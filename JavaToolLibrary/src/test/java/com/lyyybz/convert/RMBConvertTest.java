package com.lyyybz.convert;

import static org.junit.Assert.*;

import org.junit.Test;

public class RMBConvertTest
{

	@Test
	public void test()
	{
		String str = RMBConvert.chinese("12.53");
		assertEquals("壹拾贰元伍角叁分", str);
		str = RMBConvert.chinese("12.0");
		assertEquals("壹拾贰元整", str);
		str = RMBConvert.chinese("12");
		assertEquals("壹拾贰元整", str);
		str = RMBConvert.chinese("0.12");
		assertEquals("壹角贰分", str);
		str = RMBConvert.chinese("0.0");
		assertEquals("零元", str);
		str = RMBConvert.chinese("0");
		assertEquals("零元", str);
	}

}
