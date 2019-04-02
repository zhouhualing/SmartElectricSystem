package com.harmazing.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.python.antlr.PythonParser.return_stmt_return;

import com.harmazing.Config;

public class UdpLogger {
	// { "0 - Debug","1 - General","2 - Important","3 - Serious","4 - Error","5 - No"};

	public static void mtDebug(DatagramSocket sendSocket, String ipAddress, String message) {
		mtPrn(sendSocket, ipAddress, message, 0);
	}

	public static void mtDebug(DatagramSocket sendSocket, String message) {
		mtDebug(sendSocket, "255.255.255.255", message);
	}

	public static void mtDebug(String ipAddress, String message) {
		mtDebug(null, ipAddress, message);
	}

	public static void mtDebug(String message) {
		mtDebug(null, "255.255.255.255", message);
	}

	public static void mtInfo(DatagramSocket sendSocket, String ipAddress, String message) {
		mtPrn(sendSocket, ipAddress, message, 1);
	}

	public static void mtInfo(DatagramSocket sendSocket, String message) {
		mtInfo(sendSocket, "255.255.255.255", message);
	}

	public static void mtInfo(String ipAddress, String message) {
		mtInfo(null, ipAddress, message);
	}

	public static void mtInfo(String message) {
		mtInfo(null, "255.255.255.255", message);
	}

	public static void mtImportant(DatagramSocket sendSocket, String ipAddress, String message) {
		mtPrn(sendSocket, ipAddress, message, 2);
	}

	public static void mtImportant(DatagramSocket sendSocket, String message) {
		mtImportant(sendSocket, "255.255.255.255", message);
	}

	public static void mtImportant(String ipAddress, String message) {
		mtImportant(null, ipAddress, message);
	}

	public static void mtImportant(String message) {
		mtImportant(null, "255.255.255.255", message);
	}

	public static void mtSerious(DatagramSocket sendSocket, String ipAddress, String message) {
		mtPrn(sendSocket, ipAddress, message, 3);
	}

	public static void mtSerious(DatagramSocket sendSocket, String message) {
		mtSerious(sendSocket, "255.255.255.255", message);
	}

	public static void mtSerious(String ipAddress, String message) {
		mtSerious(null, ipAddress, message);
	}

	public static void mtSerious(String message) {
		mtSerious(null, "255.255.255.255", message);
	}

	public static void mtError(DatagramSocket sendSocket, String ipAddress, String message) {
		mtPrn(sendSocket, ipAddress, message, 4);
	}

	public static void mtError(DatagramSocket sendSocket, String message) {
		mtError(sendSocket, "255.255.255.255", message);
	}

	public static void mtError(String ipAddress, String message) {
		mtError(null, ipAddress, message);
	}

	public static void mtError(String message) {
		mtError(null, "255.255.255.255", message);
	}
	
	public static void mtError(Throwable t) {
		mtError(getStackTrace(t));
		mtError(t.getMessage());
	}

	public static void mtPrn(DatagramSocket sendSocket, String ipAddress, String message, Integer level) {
		if(!Config.getInstance().UDP_WATCH)
			return;
		DatagramSocket sendSocket1 = null;
		try {
			if (sendSocket == null)
				sendSocket1 = new DatagramSocket();
			byte[] buf = message.getBytes();

			int port = 0x7777;
			InetAddress ip = InetAddress.getByName(ipAddress);

			int len = 6 + buf.length;
			byte[] data = new byte[len];

			data[2] = 3;
			data[3] = level.byteValue();
			data[1] = (byte) (len & 0xff);
			data[0] = (byte) ((len >> 8) & 0xff);

			for (int i = 0; i < buf.length; ++i) {
				data[i + 6] = buf[i];
			}

			DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, port);

			if (sendSocket != null)
				sendSocket.send(sendPacket);
			else
				sendSocket1.send(sendPacket);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sendSocket1 != null) {
				sendSocket1.close();
				sendSocket1 = null;
			}
		}
	}
	
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	
}
