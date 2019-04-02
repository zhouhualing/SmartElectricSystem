package com.harmazing.cache;

import java.util.ArrayList;
import java.util.List;

import com.harmazing.mq.ACLogConsumer;

public class ACLogConsumerCache {
	public static List cache=new ArrayList<ACLogConsumer>();
	public static void add(ACLogConsumer obj){
		cache.add(obj);
	}
	
}
