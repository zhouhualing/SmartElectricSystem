package com.harmazing.spms.base.util;

import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.Lists;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Demo {
    //AES密钥，以Base64方式存储
    final static String AES_KEY= "lwbEHLqtsP6xB3g/oSqz7w==";
    final static String USER_ID="4649360bdadb11e58f64005056a0a0ed";

    public static void main(String[] args) throws UnsupportedEncodingException {
    	
    	String s11 = MD5EncryptUtil.MD5("zhl1010122312aafdasfsdfioreuroqewpuroqewprewuoruqewoprjfskafkldsajfasdiofuiufuadsfjdffffffffffffffffffffffffffffffffffff___d_&");
    	String s12 = MD5EncryptUtil.MD5("zhl1010122312aafdasfsdfioreuroqewpuroqewprewuoruqewoprjfskafkldsajfasdiofuiufuadsfjdffffffffffffffffffffffffffffffffffff___d_&");
    	System.out.println(s11);
    	System.out.println(s12);
        //原文
        //String msg="{\"userId\":\"changan\",\"tel\":\"13888888888\"}";
        String msg = "{\"userId\":\"xx12312312222\",\"username\":\"xxx\",\"carid\": \"dsfasdfasdfasdf\",\"vin\": \"s9371723dd2222\",\"tel\":\"13888888888\",\"speed\":\"20\"}";
        //用户Id
        String userId= USER_ID ;

        byte[] aeskeyBytes= Base64.decodeBase64(AES_KEY.getBytes());

        userId = UUID.randomUUID().toString().replace("-", "");
        //加密
        String encryptStr=AESUtils.encrypt(aeskeyBytes,msg,userId);
//        String encryptStr = 
//        		"kJmbT5cA8YE7IgM9A6ATS3xwWjyBkjMUmyf+NAR+31v9Sbzm84hAPDiD7wpf58bMrINSQ//ODMnR4D0z/h+fhw==";
        encryptStr = AESUtils.encrypt("",userId);
        List<String> l = Lists.newArrayList();
        //l.add("6uBXVysFEoFNcrMxrcJBLuL9hXA7VBPzvjVGXv9D9T/NmXZuWy2T9EyvJdsibyeS95cOxE6dGSo4/nwaa7U82A==");
        l.add("vzkVi7IBrOZJFFs09L9iu6l5kXiKxGIdCrwJjA1vGxhL/1O2SzY25yLGPdKsNpI/DkB8OWwT6admaUhXmS7GQsl0wO3ej5Mmc8LHlfVubl+zu7JtHjFKrXvxJZR6FtPgGFwwhX1Vx1IiPtAVIliPepsBx+YAtUD/95zDlHUJr2Sun7E4YeFpjiOb4SkIdBwGwLm5cXoxnA531us6GQD1qcqwveLCq8tqa43yTgcpt9CU/Q7rDkDdj0I2b2A/AD7kNNoQgalcGAJnirVkpGXJfzZDwgkL9H0l7bdJ+ffraZLQ4U6T+cactunCjhHmE56VBI0gtp1hPpQu/TGs51WuoV7vIW9riBCSRySSXXS9sEeqVIWGpqOxN/qmrH9frmILP/CvJmcwhPCqW1ar6o8o5KncQ0hZEHuhS1WHBKv7RtEJldoIHyGW4fP2/s5zSItvEwwpEN8DlVlz6JjdOGwDBCJrNkLKCf1jrdzD76xnpoN3VYoQo94LWs/d0iOQii3EQUs3978fqJwX0OuH9X+35bwoaHhtz0kAUFA+fhA2NHonPeea+rC31gFHrSfJ+pJ8+HdlpvZFYCEV2W3MBMDIyTuKIo+eZj+mD+nRdoPjM9oLAc9XAzjr+UrO3h1HJO2O");
        //l.add("2dXBFOcn8X412MivViNYAR3HnoKnTP2n6p7VFJuEjMCtZ9K9M5pZSQ/7kJymm4IpmTk8Vkmap+l4m83lloBriUTeD2w0uGcx8H0/thi9Oqtk/0QeERVFQBw3F07DIj1K");
        //l.add("k6PtsO0KLbdPdaCMH94v0Nx82DZXjjlJvrTxCZlZFj5Txgh2+UUOgP4qw+KwWVY5t0CHfHZSxgPdwGSM+XJLYQ==");
        
        for(String s : l){
    	   encryptStr = s;
        
        System.out.printf("\n密文：\n"+encryptStr);
        
//        String e1 = URLEncoder.encode(encryptStr,"utf-8");
//        System.out.printf("\n密文encoder：\n"+e1);
//        String d1 = URLDecoder.decode(encryptStr, "utf-8");

        // 解密
        byte[] decryptBytes=AESUtils.decrypt(aeskeyBytes,encryptStr);


        //随机�?
        byte[] nonceBytes = Arrays.copyOfRange(decryptBytes, 0, 8);
        System.out.printf("\n解密出随机数为：\n"+new String(nonceBytes));
        //时间�?
        byte[] timespanBytes = Arrays.copyOfRange(decryptBytes, 8, 18);
        System.out.printf("\n解密出时间戳为：\n"+new String(timespanBytes));
        //消息原文
        byte[] msgBytes=Arrays.copyOfRange(decryptBytes, 18, decryptBytes.length-32);
        System.out.printf("\n解密出消息原文为：\n"+new String(msgBytes));
        //userid
        byte[] userIdBytes=Arrays.copyOfRange(decryptBytes, msgBytes.length+18, decryptBytes.length);
        System.out.printf("\n解密出UserID为：\n"+new String(userIdBytes));
       }
    }
}
