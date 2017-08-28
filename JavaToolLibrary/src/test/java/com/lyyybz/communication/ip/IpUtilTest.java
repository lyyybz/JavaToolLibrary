package com.lyyybz.communication.ip;

import org.junit.Test;

public class IpUtilTest
{

	@Test
	public void testGetPublicIp()
	{
		IpUtil.getPublicIp();
	}

	@Test
	public void testGetPrivateIp()
	{
		IpUtil.getPrivateIp();
	}

	@Test
	public void testGetAllLocalHost()
	{
		IpUtil.getAllLocalHost();
	}

}
