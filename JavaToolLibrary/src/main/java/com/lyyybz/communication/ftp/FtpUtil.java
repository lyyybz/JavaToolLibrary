package com.lyyybz.communication.ftp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
/**
 * need commons-net.jar
 */
public class FtpUtil
{
	public static final String SEP = "/";

	public static FTPClient login(String server, int port, String username,
			String password) throws Exception
	{
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(server, port);

		ftpClient.setControlEncoding("UTF-8");
		ftpClient.setBufferSize(1024 * 10);
		FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_NT);
		config.setServerLanguageCode("zh");

		boolean lb = ftpClient.login(username, password);
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
		{
			ftpClient.disconnect();
			throw new Exception("UserName Or Passwd Error!!");
		}
		if (lb)
		{
			return ftpClient;
		}
		throw new Exception("Login ftp server fail, check para!!");
	}

	public static boolean upload(FTPClient ftpClient, String local,
			String remoteName) throws Exception
	{
		InputStream input = new FileInputStream(local);
		return upload(ftpClient, input, remoteName);
	}

	public static boolean upload(FTPClient ftpClient, InputStream input,
			String remoteName) throws Exception
	{
		try
		{
			boolean b1 = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			boolean b2 = ftpClient.storeFile(remoteName, input);
			return b1 && b2;
		} finally
		{
			if (null != input)
			{
				input.close();
			}
		}
	}

	public static boolean isFileExist(FTPClient ftpClient, String fileRemote)
			throws Exception
	{
		FTPFile[] files = ftpClient.listFiles(new String(fileRemote
				.getBytes("UTF-8"), "ISO-8859-1"));
		if (files.length != 1)
		{
			return false;
		}
		return true;
	}

	public static boolean download(FTPClient ftpClient, String remote,
			String local) throws Exception
	{
		OutputStream output = new FileOutputStream(local);
		try
		{
			boolean b1 = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			boolean b2 = ftpClient.retrieveFile(remote, output);
			return b1 && b2;
		} finally
		{
			if (null != output)
			{
				output.close();
			}
		}
	}

	public static InputStream download(FTPClient ftpClient, String remote)
			throws Exception
	{
		InputStream inputStream = null;
		inputStream = ftpClient.retrieveFileStream(remote);
		return inputStream;
	}

	public static boolean copy(FTPClient ftpClient, String sourceRemote,
			String targetRemote) throws Exception
	{
		InputStream input = download(ftpClient, sourceRemote);
		boolean ub = upload(ftpClient, input, targetRemote);
		return ub;
	}

	public static boolean delete(FTPClient ftpClient, String remote)
			throws Exception
	{
		FTPFile[] files = ftpClient.listFiles(new String(remote
				.getBytes("UTF-8"), "ISO-8859-1"));
		if (files.length != 1)
		{
			throw new Exception("Remote File  " + remote + " Not Exist!");
		}
		return ftpClient.deleteFile(remote);
	}

	public static boolean rename(FTPClient ftpClient, String sourceRemote,
			String targetRemote) throws Exception
	{
		return ftpClient.rename(sourceRemote, targetRemote);
	}

	public synchronized static boolean mkdir(FTPClient ftpClient, String remote)
			throws Exception
	{
		String[] folders = remote.split(SEP);
		for (int i = 0; i < folders.length; i++)
		{
			String folder = folders[i];
			if ("".equals(folder))
			{
				continue;
			}
			if (!ftpClient.changeWorkingDirectory(folder))
			{
				boolean mb = ftpClient.makeDirectory(folder);
				if (!mb)
				{
					return false;
				} else
				{
					ftpClient.changeWorkingDirectory(folder);
				}
			}
		}
		return true;
	}

	public static boolean rmdir(FTPClient ftpClient, String pathname)
			throws Exception
	{
		return recurrence(ftpClient, pathname);
	}

	private static boolean recurrence(FTPClient ftpClient, String pathname)
			throws Exception
	{
		if (!pathname.startsWith(SEP))
		{
			pathname = SEP + pathname;
		}
		if (pathname.endsWith(SEP))
		{
			pathname = pathname.substring(0, pathname.length() - 1);
		}
		int sepIndex = pathname.lastIndexOf(SEP);
		if (sepIndex != -1)
		{
			String workDir = pathname.substring(0, sepIndex);
			boolean cb = ftpClient.changeWorkingDirectory(workDir);
			if (cb)
			{
				String deleDir = pathname.substring(1 + sepIndex);
				boolean rb = ftpClient.removeDirectory(deleDir);
				if (rb)
				{
					recurrence(ftpClient, workDir);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean logout(FTPClient ftpClient) throws Exception
	{
		boolean b = ftpClient.logout();
		ftpClient.disconnect();
		return b;
	}
}
