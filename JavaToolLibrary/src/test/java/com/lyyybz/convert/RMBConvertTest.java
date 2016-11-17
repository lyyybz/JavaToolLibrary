package com.lyyybz.convert;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lyyybz.common.LogUtil;

public class RMBConvertTest
{

	@Test
	public void test()
	{
		String money = "12.53";
		String str = RMBConvert.chinese(money);
		assertEquals("壹拾贰元伍角叁分", str);
		LogUtil.debug(money + "\t" + str);
		
		money = "12.0";
		str = RMBConvert.chinese(money);
		assertEquals("壹拾贰元整", str);
		LogUtil.debug(money + "\t" + str);
		
		money = "12";
		str = RMBConvert.chinese(money);
		assertEquals("壹拾贰元整", str);
		LogUtil.debug(money + "\t" + str);
		
		money = "0.12";
		str = RMBConvert.chinese(money);
		assertEquals("壹角贰分", str);
		LogUtil.debug(money + "\t" + str);
		
		money = "0.0";
		str = RMBConvert.chinese(money);
		assertEquals("零元", str);
		LogUtil.debug(money + "\t" + str);
		
		money = "0";
		str = RMBConvert.chinese(money);
		assertEquals("零元", str);
		LogUtil.debug(money + "\t" + str);
	}

}
