package com.harmazing.util;

import com.google.protobuf.ByteString;

public class ProtobufUtil {
	public static String convertByteString2String( ByteString _str){
		String result = "";
		
		if( _str == null || _str.size() == 0)
			return "";
		
		for( int i=0; i<_str.size(); ++i)
			result += (char)(_str.byteAt(i) + '0');
		
		return result;
	}
	
	public static ByteString covertMain2SBytetring(String _main){

		int size = _main.length();
		byte[] bytes = new byte[size];
		for( int i=0; i<size; ++i)
			bytes[i] = (byte)(_main.charAt(i) - '0');
		
		return ByteString.copyFrom(bytes);
	}
}
