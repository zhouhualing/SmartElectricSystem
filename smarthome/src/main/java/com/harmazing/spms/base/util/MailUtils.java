package com.harmazing.spms.base.util;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

public class MailUtils {
	public static String toChinese(String text){
        try {
            text = MimeUtility.encodeText(new String(text.getBytes(), "UTF-8"), "UTF-8", "B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

	public static boolean sendMail(String userName,String password,String smtpHost,String from,String to,String subject,String content){
		MailBean mb = new MailBean();
        mb.setHost(smtpHost);                        // 设置SMTP主机(163)，若用126，则设为：smtp.126.com
        mb.setUsername(userName);                // 设置发件人邮箱的用户名
        mb.setPassword(password);                        // 设置发件人邮箱的密码，需将*号改成正确的密码
        mb.setFrom(from);            // 设置发件人的邮箱
        mb.setTo(to);                // 设置收件人的邮箱
        mb.setSubject(subject);                    // 设置邮件的主题
        mb.setContent(content);        // 设置邮件的正文

//        mb.attachFile("E:/工作报告(林乙腾).doc");            // 往邮件中添加附件
//        mb.attachFile("E:/test.txt");
//        mb.attachFile("E:/test.xls");
        
        System.out.println("正在发送邮件...");
        
        if(sendMail(mb)){
        	// 发送邮件
        	System.out.println("发送成功!");
        	return true;
        }
        else{
        	System.out.println("发送失败!");
        	return false;
        }
	}
	
	public static boolean sendMail(MailBean mb) {
        String host = mb.getHost();
        final String username = mb.getUsername();
        final String password = mb.getPassword();
        String from = mb.getFrom();
        String to = mb.getTo();
        String subject = mb.getSubject();
        String content = mb.getContent();
        String fileName = mb.getFilename();
        Vector<String> file = mb.getFile();
        
        
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);                // 设置SMTP的主机
        props.put("mail.smtp.auth", "true");            // 需要经过验证
        
        Session session = Session.getInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(toChinese(subject));

            Multipart mp = new MimeMultipart();
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setContent(content, "text/html;charset=gb2312");
//            mbpContent.setText(content);
            mp.addBodyPart(mbpContent);

            /**//*    往邮件中添加附件    */
            if(null != file && file.size() > 0){
            	Enumeration<String> efile = file.elements();
            	while (efile.hasMoreElements()){
            		MimeBodyPart mbpFile = new MimeBodyPart();
            		fileName = efile.nextElement().toString();
            		FileDataSource fds = new FileDataSource(fileName);
            		mbpFile.setDataHandler(new DataHandler(fds));
            		mbpFile.setFileName(toChinese(fds.getName()));
            		mp.addBodyPart(mbpFile);
            	}
            }
            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
            
        } catch (MessagingException me) {
            me.printStackTrace();
            return false;
        }
        return true;
	}
	
	public static boolean sendMailWithPic(MailBean mb, List<byte[]> list) {
		String host = mb.getHost();
		final String username = mb.getUsername();
		final String password = mb.getPassword();
		String from = mb.getFrom();
		String to = mb.getTo();
		String subject = mb.getSubject();
		String content = mb.getContent();
		
		
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);                // 设置SMTP的主机
		props.put("mail.smtp.auth", "true");            // 需要经过验证
		
		Session session = Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(toChinese(subject));
			
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setContent(content, "text/html;charset=gb2312");
//            mbpContent.setText(content);
			mp.addBodyPart(mbpContent);
			
			for (int i = 0; i < list.size(); i++) {
				mbpContent = new MimeBodyPart(); 
				DataHandler dh = new DataHandler(new ByteArrayDataSource(list.get(i), "application/octet-stream"));
				mbpContent.setDataHandler(dh); 
				// 加上这句将作为附件发送,否则将作为信件的文本内容
				mbpContent.setFileName("pic" + i + ".png"); 
				mbpContent.setHeader("Content-ID", "pic" + i);
				// 将含有附件的BodyPart加入到MimeMultipart对象中
				mp.addBodyPart(mbpContent); 
			}
				
			msg.setContent(mp);
			msg.setSentDate(new Date());
			Transport.send(msg);
			
		} catch (MessagingException me) {
			me.printStackTrace();
			return false;
		}
		return true;
	}
}
