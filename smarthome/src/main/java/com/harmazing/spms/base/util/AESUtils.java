package com.harmazing.spms.base.util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.google.common.collect.Maps;

public class AESUtils {
    static Charset CHARSET = Charset.forName("utf-8");
    public final static Base64 base64 = new Base64();
    
    public final static String DEFAULT_AES_KEY= "lwbEHLqtsP6xB3g/oSqz7w==";
    public final static String DEFAULT_USER_ID="4649360bdadb11e58f64005056a0a0ed";

    // 随机生成8位字符串
    public static String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String encrypt(String text) {
    	return encrypt(Base64.decodeBase64(DEFAULT_AES_KEY.getBytes()),text,DEFAULT_USER_ID);
    }
    
    public static String encrypt(String text,String userId) {
    	return encrypt(Base64.decodeBase64(DEFAULT_AES_KEY.getBytes()),text,userId);
    }
    
    public static String encrypt(byte[] aesKey, String text, String userId) {

        ByteGroup byteCollector = new ByteGroup();
        //随机�?
        byte[] randomStrBytes = getRandomStr().getBytes(CHARSET);
        //时间�?
        long timespan = System.currentTimeMillis()/1000;
        byte[] timespanBytes = String.valueOf(timespan).getBytes(CHARSET);
        byte[] textBytes = text.getBytes(CHARSET);
        byte[] useridBytes = userId.getBytes(CHARSET);

        // randomStr + networkBytesOrder + text + corpid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(timespanBytes);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(useridBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填�?
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得�?终的字节�?, 未加�?
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            String base64Encrypted = base64.encodeToString(encrypted);

            return base64Encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(String text) {
    	return decrypt(Base64.decodeBase64(DEFAULT_AES_KEY.getBytes()),text);
    }
    
    public static byte[] decrypt(byte[] aesKey, String text) {
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解�?
            byte[] encrypted = Base64.decodeBase64(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String msg, userid;
        try {
            // 去除补位字符
            return PKCS7Encoder.decode(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static Map<String,Object> ParseDecrypt(String encrypt){
    	byte[] decrypt = decrypt(encrypt);
    	if(decrypt==null || decrypt.length==0)
    		return null;
    	return ParseDecrypt(decrypt);
    }
    
    public static Map<String,Object> ParseDecrypt(byte[] decryptBytes){
    	
    	Map<String, Object> ret = Maps.newHashMap();

        byte[] nonceBytes = Arrays.copyOfRange(decryptBytes, 0, 8);
        ret.put(IConstants.PARSE_DECRYPT_RANDOM,new String(nonceBytes));

        byte[] timespanBytes = Arrays.copyOfRange(decryptBytes, 8, 18);
        ret.put(IConstants.PARSE_DECRYPT_TIME,new String(timespanBytes));

        byte[] msgBytes=Arrays.copyOfRange(decryptBytes, 18, decryptBytes.length-32);
        ret.put(IConstants.PARSE_DECRYPT_CONTENT,new String(msgBytes));

        byte[] userIdBytes=Arrays.copyOfRange(decryptBytes, msgBytes.length+18, decryptBytes.length);
        ret.put(IConstants.PARSE_DECRYPT_USERID,new String(userIdBytes));
        return ret;        
    }
    
    public interface IConstants{
    	public final static String PARSE_DECRYPT_RANDOM = "random";
    	public final static String PARSE_DECRYPT_TIME = "time";
    	public final static String PARSE_DECRYPT_CONTENT = "content";
    	public final static String PARSE_DECRYPT_USERID = "userid";
    }
    
}
