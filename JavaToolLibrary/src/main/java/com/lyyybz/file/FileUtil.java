package com.lyyybz.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import com.lyyybz.common.MyException;

/**
 * 
 * @author lyyybz
 *
 */
public class FileUtil
{
	
	public static void getFilePath()
	{
		URL url = FileUtil.class.getProtectionDomain().getCodeSource()
				.getLocation();
		String filePath = url.getPath();
		if (filePath.endsWith(".jar"))
		{
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		}
		File file = new File(filePath);
		filePath = file.getAbsolutePath();
		System.out.println(filePath);
	}
	
	public static String readFileContext(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if(!file.exists())
            {
                return null;
            }
            BufferedReader in = new BufferedReader(new FileReader(file));
            String context = "";
            String str;
            while ((str = in.readLine()) != null)
            {
                context += str;
            }
            in.close();
            return context;
        }catch (Exception e)
        {
            return null;
        }
    }
	
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

}
