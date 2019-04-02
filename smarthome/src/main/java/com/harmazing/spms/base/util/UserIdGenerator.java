package com.harmazing.spms.base.util;

public class UserIdGenerator {
	  private static synchronized Long next(){
		    return System.currentTimeMillis();
		  }
		  
		  public static String getNext()
		  {
			  return next().toString();
		  }
	}
