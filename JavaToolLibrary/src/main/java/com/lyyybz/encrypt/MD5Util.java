package com.lyyybz.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class MD5Util
{
	public final static String generateMD5(byte[] data)
	{
		try
		{
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(data);
			byte[] md = mdInst.digest();
			return hex2String(md);
		} catch (Exception e)
		{
			return null;
		}
	}

	public static String generateFileMD5(File file)
	{
		if (file.exists() == false)
		{
			return null;
		}
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(file);
			byte[] fileData = new byte[in.available()];
			in.read(fileData);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(fileData);
			byte[] data = digest.digest();
			String str = hex2String(data);
			return str;
		} catch (Exception e)
		{
			return null;
		} finally
		{
			try
			{
				in.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static String hex2String(byte[] data)
	{
		if (data == null)
		{
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++)
		{
			sb.append(String.format("%02X", data[i]));
		}

		return sb.toString();
	}
}
