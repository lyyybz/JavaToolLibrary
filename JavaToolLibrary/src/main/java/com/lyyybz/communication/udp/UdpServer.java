package com.lyyybz.communication.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer
{
	private final int port;
	private final int soTimeout;
	private DatagramSocket docket;

	public UdpServer(int port) throws IOException
	{
		this.port = port;
		this.soTimeout = 0;
		initDatagramSocket();
	}

	public UdpServer(int port, int soTimeout) throws IOException
	{
		this.port = port;
		this.soTimeout = soTimeout;
		initDatagramSocket();
	}

	private void initDatagramSocket() throws IOException
	{
		docket = new DatagramSocket(port);
		if (soTimeout != 0)
		{
			docket.setSoTimeout(soTimeout);
		}
		docket.setReceiveBufferSize(128 * 1024);
	}

	public DatagramPacket receive()
	{
		try
		{
			byte[] recvbuff = new byte[1500];
			DatagramPacket packet = new DatagramPacket(recvbuff, recvbuff.length);
			docket.receive(packet);
			return packet;
		} catch (IOException e)
		{
			return null;
		}
	}

	public void send(DatagramPacket packet)
	{
		try
		{
			if (packet == null)
			{
				return;
			}
			docket.send(packet);
		} catch (IOException e)
		{
			return;
		}
	}

	public void close()
	{
		docket.close();
	}

	public int getPort()
	{
		return port;
	}

	public int getSoTimeout()
	{
		return soTimeout;
	}

}
