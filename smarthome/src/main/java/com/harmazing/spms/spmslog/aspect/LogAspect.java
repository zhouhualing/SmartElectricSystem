package com.harmazing.spms.spmslog.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.spmslog.dao.DeviceLogsDao;
import com.harmazing.spms.spmslog.dao.UserLogsDao;
import com.harmazing.spms.user.dao.SpmsUserDAO;


/**
 * 
* @ClassName: LogAspect 
* @Description: 日志记录AOP实现 
* @author shaojian.yu
* @date 2014年11月3日 下午1:51:59 
*
 */
//@Component("LogAspect")
//@Aspect
public class LogAspect{
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

//  @Autowired
  private UserLogsDao userLogsDao ;

//  @Autowired
  private DeviceLogsDao deviceLogsDao ;
  
//  @Autowired
  private SpmsUserDAO spmsUserDAO;
  
//  @Autowired
//  private SpmsDeviceDAO spmsDeviceDAO;
  
  String[] cmUser = new String[]{"spmsUser/doSave","spmsUser/doDels","spmsUser/doDel"};
  
  String[] cmDevice = new String[]{"spmsUser/bindGetWay","spmsUser/addDeviceToProduct",
		  "spmsUser/reBindDevice","spmsUser/reBindDevice1",
//  		"spmsUser/cancelDeviceBind","spmsProduct/bindDeviceToProduct",
  		"businessChange/saveChangeDevices","businessChange/saveTdDevices"};
  
  @Autowired
  private QueryDAO queryDao; 
  /**
   * @Title：doBeforeInServiceLayer
   * @Description: 方法调用前触发 
   *  记录开始时间 
   * @param joinPoint
   */
//  @Before("execution(* com.harmazing.spms.*.manager.*.*(..))")
  public void doBeforeInServiceLayer(JoinPoint joinPoint){

  }

  /**
   * @Title：doAfterInServiceLayer
   * @Description: 方法调用后触发 
   *  记录结束时间
   * @param joinPoint
   */
//  @After("execution(* com.harmazing.spms.*.manager.*.*(..))")
  public void doAfterInServiceLayer(JoinPoint joinPoint) {

  }
  
	/**
	 * @Title：doAround
	 * @Description: 环绕触发
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
//	@Around("execution(public * com.harmazing.spms.*.manager.*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//		/**
//		 * 1.获取request信息 2.根据request获取session 3.从session中取出登录用户信息
//		 */
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//		HttpServletRequest request = sra.getRequest();
//		String url = request.getServletPath();
//		String ip = request.getRemoteAddr();
//		String id = request.getParameter("id") != null ? request.getParameter(
//				"id").toString() : "";
//		String time = sendMessage.getTime();
//		Object before = null;
//		Object after = null;
//
//		Boolean flag = null;
//		for (String str : cmUser) {
//			if (url.contains(str)) {
//				flag = true;
//				break;
//			}
//		}
//		for (String str : cmDevice) {
//			if (url.contains(str)) {
//				flag = false;
//				break;
//			}
//		}
//		if (null != flag && flag) {
//			before = spmsUserDAO.findOne(id);
//		} else {
//			before = spmsDeviceDAO.findOne(id);
//		}
		Object obj = pjp.proceed();
//		if(null == flag){
//			return obj;
//		}
//		if (null != flag && flag) {
//			after = spmsUserDAO.findOne(id);
//		} else {
//			after = spmsDeviceDAO.findOne(id);
//		}
//		JsonConfig jsonConfig = new JsonConfig();
//		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
//		jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer"}); 
//		
////		before = collections2Null(before);
////		after = collections2Null(after);
//		
//		if (null != before && null != after && flag) {
//			userLogsDao.saveUserLog(url, id, time, ip,
//					JSONObject.fromObject(before,jsonConfig).toString(),
//					JSONObject.fromObject(after,jsonConfig).toString(),UserUtil.getCurrentUser().getId());
//		} else if (null != before && null != after && !flag) {
//			deviceLogsDao.saveDeviceLog(url, id, time, ip,
//					JSONObject.fromObject(before,jsonConfig).toString(),
//					JSONObject.fromObject(after,jsonConfig).toString(),UserUtil.getCurrentUser().getId());
//		}
		return obj;
	}
	
	private Object collections2Null(Object obj){
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Method[] methods = obj.getClass().getDeclaredMethods();
			for (Field field : fields) {
				if(!(field.getType().getName().toLowerCase().contains("int") ||
						field.getType().getName().toLowerCase().contains("long") ||
						field.getType().getName().toLowerCase().contains("double") ||
						field.getType().getName().toLowerCase().contains("string") ||
						field.getType().getName().toLowerCase().contains("float") ||
						field.getType().getName().toLowerCase().contains("char") ||
						field.getType().getName().toLowerCase().contains("integer")
						)){
					field.getName();
					for (Method m : methods) {
						if(m.getName().toLowerCase().equals(("set" + field.getName()).toLowerCase())){
							System.out.println(m.getName());
							m.invoke(obj, null);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}