package com.harmazing.spms.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.device.entity.SpmsDeviceBase;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.DeviceDAOAccessor;
import com.harmazing.spms.ifttt.dao.SpmsIftttStrategyDAO;
import com.harmazing.spms.ifttt.entity.SpmsIftttStrategy;
import com.harmazing.spms.ifttt.service.IftttService;

import net.sf.json.JSONArray;

@Component("spmsIftttTask")
@Lazy(false)
public class SpmsIftttTask {
	@Autowired
	private IftttService iftttService;
//	
//	@Scheduled(cron="0 0-59/1 0-23 * * ?")
//	public void iftttUpdate() {
//		iftttService.ifThisThen();
//	}	
}
