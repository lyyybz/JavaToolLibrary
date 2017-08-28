package com.lyyybz.communication.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Arrays;

public class UdpClient
{
	private int localPort;
	private final int soTimeout;
	private final SocketAddress serverAddr;
	private DatagramSocket docket;
	private DatagramPacket recvPacket;
	
	private UdpClient(Builder builder) throws IOException
	{
		this.serverAddr = builder.serverAddr;
		this.localPort = builder.localPort;
		this.soTimeout = builder.soTimeout;
		initDatagramSocket();
	}
	
	private void initDatagramSocket() throws IOException
	{
		if(localPort != 0)
		{
			docket = new DatagramSocket(localPort);
		}else
		{
			docket = new DatagramSocket();
			localPort = docket.getLocalPort();
		}
		if (soTimeout != 0)
		{
			docket.setSoTimeout(soTimeout);
		}
		docket.setReceiveBufferSize(128 * 1024);
		byte[] recvbuff = new byte[1500];
		recvPacket = new DatagramPacket(recvbuff, recvbuff.length);
	}
	
	public byte[] receive()
	{
		try
		{	
			docket.receive(recvPacket);
			byte[] data = Arrays.copyOf(recvPacket.getData(), recvPacket.getLength());
			return data;
		} catch (IOException e)
		{
			return null;
		}
	}
	
	public void send(byte[] data)
	{
		try
		{	
			DatagramPacket outPacket = new DatagramPacket(data, data.length);
			outPacket.setSocketAddress(serverAddr);
			docket.send(outPacket);
		} catch (IOException e)
		{
			return;
		}
	}
	
	public void close()
	{
		docket.close();
	}
	
	public static class Builder
	{
		private SocketAddress serverAddr;
		private int localPort;
		private int soTimeout;
		
		public Builder(SocketAddress serverAddr)
		{
			this.serverAddr = serverAddr;
			this.localPort = 0;
			this.soTimeout = 0;
		}
		
		public Builder localPort(int localPort)
		{
			this.localPort = localPort;
			return this;
		}
		
		public Builder soTimeout(int soTimeout)
		{
			this.soTimeout = soTimeout;
			return this;
		}
		
		public UdpClient build() throws IOException
		{
			return new UdpClient(this);
		}
	}
}
