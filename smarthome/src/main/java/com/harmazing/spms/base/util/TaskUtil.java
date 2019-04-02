package com.harmazing.spms.base.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * task uitl.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@Component
public class TaskUtil {
	
	/**
	 * 需要执行的task需要添加到该map中
	 * 值为 bean名称.方法名称
	 */
	public static final List <String> executeTasks = new ArrayList<String>();
	
	static {
//		executeTasks.add("accountBillTask#executeAccountBill");
	}
	
	/**
	 * execute task.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static final void doExecuteTask() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		for(String task : executeTasks) {
			String [] tempArr = task.split("#");
			SpringUtil.getBeanByName(tempArr[0]);
		}
	}
	
	

}
