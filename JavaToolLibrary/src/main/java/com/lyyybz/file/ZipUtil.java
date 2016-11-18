package com.lyyybz.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.lyyybz.common.LogUtil;

public class ZipUtil
{
	public static boolean compress(String zipFileName, String sourceFilePath)
	{
		try
		{
			File inputFile = new File(sourceFilePath);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
			BufferedOutputStream bos = new BufferedOutputStream(zos);
			compress(zos, inputFile, inputFile.getName(), bos);
			bos.close();
			zos.close();
			return true;
		} catch (Exception e)
		{
			LogUtil.exception(e);
			return false;
		}
	}

	private static void compress(ZipOutputStream zos, File file, String folder, BufferedOutputStream bos)
			throws Exception
	{
		if (file.isDirectory() == true)
		{
			File[] files = file.listFiles();
			if (files.length == 0)
			{
				zos.putNextEntry(new ZipEntry(folder + "/"));
			}
			for (int i = 0; i < files.length; i++)
			{
				compress(zos, files[i], folder + "/" + files[i].getName(), bos);
			}
		} else
		{
			zos.putNextEntry(new ZipEntry(folder));
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int b;
			while ((b = bis.read()) != -1)
			{
				bos.write(b);
			}
			bis.close();
			fis.close();
		}
	}

	public static boolean decompress(String zipFileName, String targetFilePath) throws IOException
	{
		try
		{
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
			BufferedInputStream bis = new BufferedInputStream(zis);
			File file = null;
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null && !entry.isDirectory())
			{
				file = new File(targetFilePath, entry.getName());
				if (!file.exists())
				{
					(new File(file.getParent())).mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int b;
				while ((b = bis.read()) != -1)
				{
					bos.write(b);
				}
				bos.close();
				fos.close();
			}
			bis.close();
			zis.close();
			return true;
		} catch (Exception e)
		{
			LogUtil.exception(e);
			return false;
		}
	}
}
