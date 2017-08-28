package com.lyyybz.communication.ip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.lyyybz.common.LogUtil;
import com.lyyybz.communication.http.HttpRequestMethodEnum;
import com.lyyybz.communication.http.HttpResult;
import com.lyyybz.communication.http.HttpServiceImpl;
import com.lyyybz.communication.http.IHttpService;

public class IpUtil
{
	public static String getPublicIp()
	{
		try
		{
			String url = "http://www.ip138.com/ip2city.asp";

			IHttpService service = new HttpServiceImpl.Builder(url)
					.requestPropertyies("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15")
					.requestPropertyies("Content-Type", "text/html")
					.requestPropertyies("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.requestMethod(HttpRequestMethodEnum.GET).build();

			HttpResult result = service.getHttpResult(null);
			if (result.isSuccess() == false)
			{
				return null;
			}
			String content = new String(result.content);
			int start = content.indexOf('[') + 1;
			int end = content.indexOf(']');

			if (start > end || start < 0 || end < 0)
			{
				return null;
			}

			String ip = content.substring(start, end);
			LogUtil.info(ip);
			return ip;
		} catch (Exception e)
		{
			LogUtil.exception(e);
		}
		return null;
	}

	public static String getPrivateIp()
	{
		try
		{
			String ip = InetAddress.getLocalHost().getHostAddress();
			LogUtil.info(ip);
			return ip;
		} catch (Exception e)
		{
			LogUtil.exception(e);
		}
		return null;
	}

	public static List<InetAddress> getAllLocalHost()
	{
		List<InetAddress> addrs = new ArrayList<InetAddress>();
		Enumeration<NetworkInterface> netInterfaces = null;
		try
		{
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			Enumeration<InetAddress> ips = null;
			while (netInterfaces.hasMoreElements())
			{
				NetworkInterface ni = netInterfaces.nextElement();
				ips = ni.getInetAddresses();
				while (ips.hasMoreElements())
				{
					InetAddress addr = ips.nextElement();
					if (addr instanceof Inet4Address)
					{
						addrs.add(addr);
					}
				}
			}
			LogUtil.info(addrs.toString());
			return addrs;
		} catch (Exception e)
		{
			LogUtil.exception(e);
		}
		return null;
	}

}
