package com.lyyybz.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.lyyybz.exception.MyException;

/**
 * 
 * @author lyyybz
 *
 */
public class FileUtil
{
	/**
	 * write File, you can select append or not 
	 */
	public static boolean writeFile(String fileName, String message, boolean append) throws MyException
	{
		if (fileName == null || message == null)
		{
			throw new MyException("para is null");
		}
	    File file = new File(fileName);
	    if (!file.exists())
	    {
	    	try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				throw new MyException("file not exist and createNewFile fail");
			}
	    }
	    FileWriter fw = null;
	    try
		{
	    	fw = new FileWriter(file, append);
	 	    fw.write(message);
	 	    return true;
		} catch (IOException e)
		{
			return false;
		}finally {
			try
			{
				fw.flush();
				fw.close();
			} catch (IOException e)
			{
				throw new MyException("FileWriter close fail");
			}
		}
	}

	public enum BatchDeleteMode { ALL, FILE, DIRECTORY; }
	/**
	 * Batch delete (all, files, directories), you can specify the suffix, default is ""
	 */
	public static boolean batchDeleteFile(String directory, BatchDeleteMode mode, String suffix) throws MyException
	{
		if (directory == null || mode == null || suffix == null)
		{
			throw new MyException("para is null");
		}
		File file = new File(directory);
		if (!file.isDirectory())
		{
			throw new MyException("first para is not a directory");
		}
		boolean result = true;
		File[] filelist = file.listFiles();
		for (File tmp : filelist)
		{
			if ((mode == BatchDeleteMode.FILE && tmp.isDirectory())
					|| (mode == BatchDeleteMode.DIRECTORY) && !tmp.isDirectory())
			{
				continue;
			}
			if (tmp.getName().endsWith(suffix))
			{
				boolean res = tmp.delete();
				result = result & res;
			}
		}
		return result;
	}

	public static void main(String[] args) throws MyException
	{
//		System.out.println(batchDeleteFile("e:\\tmp", BatchDeleteMode.FILE, ""));
		System.out.println(writeFile("e:\\tmp\\1.txt", "test", true));
		
	}
}
