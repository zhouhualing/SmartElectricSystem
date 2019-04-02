package com.harmazing.spms.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	/**
	* Description: 向FTP服务器上传文件
	* @Version1.0 Jul 27, 2008 4:31:09 PM by 崔红保（cuihongbao@d-heaven.com）创建
	* @param url FTP服务器hostname
	* @param port FTP服务器端口
	* @param username FTP登录账号
	* @param password FTP登录密码
	* @param path FTP服务器保存目录
	* @param filename 上传到FTP服务器上的文件名
	* @param input 输入流
	* @return 成功返回true，否则返回false
	*/ 
	public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}

			ftp.setBufferSize(1024); 
//			ftp.setControlEncoding("GBK"); 
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
			ftp.enterLocalPassiveMode();
            
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
	public static void main(String[] args) throws IOException {
		uploadFile("dsm-innolinks.com", 21, "ftpuser", "ftpuser", "", "a.txt", new FileInputStream(new File("d:/a.txt")));
	}
}
