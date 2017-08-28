package com.lyyybz.communication.ftp;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpConnection
{
	public static final String SEP = "/";
	public static final String USERNAME = "ecloud_upgrade";
	public static final String PASSWD = "who1attack2who3sb";

	private FTPClient ftpClient;

	public FtpConnection()
	{
		super();
	}
	
	public FTPClient getFtpClient()
	{
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient)
	{
		this.ftpClient = ftpClient;
	}

	public FtpConnection(String ip, int port, String username,
			String password) throws Exception
	{
		if (login(ip, port, username, password) == false)
		{
			throw new Exception("Login Server Fail!");
		}
	}

	public FtpConnection(String ip, int port) throws Exception
	{
		if (!login(ip, port, USERNAME, PASSWD))
		{
			throw new Exception("Login Server Fail!!");
		}
	}

	private boolean login(String ip, int port, String username,
			String password) throws Exception
	{
		ftpClient = FtpUtil.login(ip, port, username, password);
		return ftpClient == null ? false : true;
	}

	public boolean logout() throws Exception
	{
		return FtpUtil.logout(ftpClient);
	}

	public boolean upload(String local, String remote) throws Exception
	{
		return upload(null, local, remote);
	}

	public boolean upload(String source, String target, int maxSize, int count) throws Exception
	{
		String folder = target.substring(0, target.lastIndexOf(SEP));
		int size = getFoldersSize(folder);
		if(size > maxSize)
		{
			deleteHistoryFile(folder, count);
		}
		return upload(null, source, target);
	}
	
	public boolean upload(InputStream local, String remote) throws Exception
	{
		return upload(local, null, remote);
	}

	private boolean upload(InputStream localInputStream, String local,
			String remote) throws Exception
	{
		ftpClient.enterLocalPassiveMode();
		remote = new String(remote.getBytes("UTF-8"), "ISO-8859-1");
		String folder = "";
		if (remote.lastIndexOf(SEP) != -1)
		{
			folder = remote.substring(0, remote.lastIndexOf(SEP));
		}
		String remoteName = "";
		if (remote.lastIndexOf(SEP) != -1)
		{
			remoteName = remote.substring(remote.lastIndexOf(SEP) + 1);
		} else
		{
			remoteName = remote;
		}
		boolean b = FtpUtil.mkdir(ftpClient, folder);
		if (!b)
		{
			throw new Exception("Create Folder Fail In Ftp Server!!");
		}
		boolean ub = false;
		try
		{
			if (null != local)
			{
				ub = FtpUtil.upload(ftpClient, local, remoteName);
			} else if (null != localInputStream)
			{
				ub = FtpUtil.upload(ftpClient, localInputStream, remoteName);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ub;
	}

	public boolean download(String remote, String local) throws Exception
	{
		remote = new String(remote.getBytes("UTF-8"), "ISO-8859-1");
		boolean ib = FtpUtil.isFileExist(ftpClient, remote);
		if (!ib)
		{
			return ib;
		}
		local = new String(local.getBytes("UTF-8"), "ISO-8859-1");
		boolean isDirExist = isDirExist(local);
		boolean isDownload = false;
		try
		{
			if (isDirExist)
				isDownload = FtpUtil.download(ftpClient, remote, local);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return isDownload;
	}

	public InputStream download(String remote) throws Exception
	{
		remote = new String(remote.getBytes("UTF-8"), "ISO-8859-1");
		boolean ib = FtpUtil.isFileExist(ftpClient, remote);
		if (!ib)
		{
			return null;
		}
		InputStream inputStream = FtpUtil.download(ftpClient, remote);
		if (inputStream.available() == 0)
		{
			return null;
		}
		return inputStream;
	}

	public boolean copy(String sourceRemote, String targetRemote)
			throws Exception
	{
		sourceRemote = new String(sourceRemote.getBytes("UTF-8"), "ISO-8859-1");
		targetRemote = new String(targetRemote.getBytes("UTF-8"), "ISO-8859-1");
		boolean b = FtpUtil.copy(ftpClient, sourceRemote, targetRemote);
		return b;
	}

	public boolean delete(String remote) throws Exception
	{
		remote = new String(remote.getBytes("UTF-8"), "ISO-8859-1");
		return FtpUtil.delete(ftpClient, remote);
	}

	public boolean rename(String sourceRemote, String targetRemote)
			throws Exception
	{
		sourceRemote = new String(sourceRemote.getBytes("UTF-8"), "ISO-8859-1");
		targetRemote = new String(targetRemote.getBytes("UTF-8"), "ISO-8859-1");
		boolean b = FtpUtil.rename(ftpClient, sourceRemote, targetRemote);
		return b;
	}

	public boolean mkdir(String remote) throws Exception
	{
		remote = new String(remote.getBytes("UTF-8"), "ISO-8859-1");
		boolean b = FtpUtil.mkdir(ftpClient, remote);
		return b;
	}

	public boolean isFileExist(String fileRemote) throws Exception
	{
		return FtpUtil.isFileExist(ftpClient, fileRemote);
	}
	
	private boolean isDirExist(String local)
	{
		String folder = "";
		boolean isDirExist = false;
		if (local.lastIndexOf(SEP) != -1)
		{
			folder = local.substring(0, local.lastIndexOf(SEP));
			if (folder == "")
			{
				isDirExist = true;
			} else
			{
				isDirExist = isDir(folder);
			}
		} else
		{
			isDirExist = true;
		}
		return isDirExist;
	}

	private boolean isDir(String folder)
	{
		File localFile = new File(folder);
		try
		{
			if (!localFile.isDirectory())
			{
				return localFile.mkdirs();
			}
		} catch (SecurityException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getFoldersSize(String path)
	{
		try
		{
			ftpClient.enterLocalPassiveMode();
			FTPFile[] files = ftpClient.listFiles(path);
			int size = 0;
			for(FTPFile file : files)
			{
				if(file.isDirectory() == true)
				{
					size += getFoldersSize(path+"/"+file.getName());
				}else
				{
					size += file.getSize();
				}
			}
			return size;
		} catch (Exception e)
		{
			return 0;
		}
	}
	
	public boolean deleteHistoryFile(String path, int count)
	{
		try
		{
			ftpClient.enterLocalPassiveMode();
			FTPFile[] files = ftpClient.listFiles(path);
			int num = (files.length>count) ? count : files.length;
			for(int i=0; i<num; i++)
			{
				String filePath = path + files[i].getName();
				ftpClient.deleteFile(filePath);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception
	{
		FtpConnection connection = new FtpConnection("115.28.176.106", 21,
				"ecloud_upgrade", "who1attack2who3sb");
		String source = "./lib/gson-2.3.1.jar";
		String filePath = "test/test";
		boolean isExist = connection.isFileExist(filePath);
		System.out.println(isExist);
		connection.upload(source, filePath);
		connection.getFtpClient().changeToParentDirectory();
		isExist = connection.isFileExist(filePath);
		System.out.println(isExist);
		connection.delete(filePath);
		isExist = connection.isFileExist(filePath);
		System.out.println(isExist);
		connection.upload(source, filePath);
		connection.getFtpClient().changeToParentDirectory();
		isExist = connection.isFileExist(filePath);
		System.out.println(isExist);
		
		System.out.println("size : " + connection.getFoldersSize("ecloudcamera/video/20851/5f5a69c2-e0ae-504f-829b-00E0F80226EA/"));
		connection.upload(null, "ecloudcamera/video/20851/5f5a69c2-e0ae-504f-829b-00E0F80226EA/", 1024, 1);
		connection.logout();
	}
}
