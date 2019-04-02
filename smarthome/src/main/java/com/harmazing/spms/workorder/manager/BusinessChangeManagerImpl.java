package com.harmazing.spms.workorder.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.device.dao.*;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.jszc.dao.JSZCDao;
import com.harmazing.spms.jszc.entity.JSZCEntity;
import com.harmazing.spms.product.dao.SpmsProductDAO;
import com.harmazing.spms.product.dao.SpmsProductTypeDAO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.usersRairconSetting.dao.SpmsRairconCurveDAO;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;
import com.harmazing.spms.workorder.dto.SpmsChangeProductDTO;
import com.harmazing.spms.workorder.manager.BusinessChangeManager;

@Service("businessChangeManager")
public class BusinessChangeManagerImpl implements BusinessChangeManager{
	@Autowired
	public QueryDAO queryDao;
	@Autowired
	public SpmsProductTypeDAO productTypeDao;
	@Autowired
	public SpmsUserDAO spmsUserDao;
	@Autowired
	public SpmsProductDAO spmsProductDAO;
	@Autowired
	public JSZCDao jszcDao;
	@Autowired
	private SpmsAirConditionDAO spmsDeviceDAO;
	@Autowired
	private SpmsRairconCurveDAO rairconCurveDAO;
	@Override
	public Double getUserBanlance(@RequestBody Map<String, Object> para) {
		// Map para=request.getParameterMap();
		String pid = para.get("processInstanceId").toString();
		String sql = "select u.balance from spms_workorder o,spms_user u where o.spmsUserMobile=u.mobile and o.processInstanceId='"
				+ pid + "'";
		List lst = queryDao.getMapObjects(sql);
		Double balance = 0d;
		if (lst.size() > 0) {
			Object obj = ((Map) lst.get(0)).get("balance");
			if (obj != null) {
				balance = Double.parseDouble(obj.toString());
			}
		}
		return balance;
	}

	@Override
	public List getUserProduct(@RequestBody Map<String, Object> para) {
		String mobile=para.get("mobile").toString();
		String sql="select product.id,type.`names`,type.id typeId from spms_user usr,spms_product product,spms_product_type type"+
				" where usr.mobile='"+mobile+"' and product.user_id=usr.id and product.status <> 4 and product.status <> 5 and product.type_id=type.id";
		return queryDao.getMapObjects(sql);
	}

	@Override
	public List getDevices(HttpServletRequest request) {
		String sql="select id,type.`names` from spms_product_type type where deleteStauts <> 1";
		return queryDao.getMapObjects(sql);
	}

	@Override
	public Map<String, Object> getUserDetail(
			@RequestBody Map<String, Object> para) {
		String mobile=para.get("mobile").toString();
		String sql="SELECT usr.*,bizarea.`name` bizAreaName,elearea.`name` eleAreaName FROM `spms_area` bizarea, spms_area elearea,spms_user usr "+
				" where usr.ele_area_id=elearea.id and usr.biz_area_id=bizarea.id and usr.mobile='"+mobile+"'";
		List lst=queryDao.getMapObjects(sql);
		Map<String,Object> m=new HashMap<String,Object>();
		if(lst.size()>0){
			m=(Map<String, Object>) lst.get(0);
			m.put("msg", true);
		}
		return m;
	}

	@Override
	public List getUserDevices(@RequestBody Map<String, Object> para) {
		String pid=para.get("processInstanceId").toString();
		String sql="select product.device_id,device.type typeid ,device.mac ,type.value typename "+
				" from spms_user usr,spms_workorder workorder,spms_user_product_binding product,spms_device device,tb_workflow_variable var,"+
				"	dict_device_type type where "+
				"	workorder.processInstanceId='" + para.get("processInstanceId").toString() + "'  "+
				"	and workorder.spmsUserMobile=usr.mobile and usr.id=product.user_id and "+
				"	product.product_id=var.iValue and var.iKey='orderProduct' and workorder.processInstanceId=var.processId and "+
				"	device.type=type.code and product.device_id=device.id ";
				
		return queryDao.getMapObjects(sql);		
	}

	@Transactional
	@Override
	public SpmsChangeProductDTO saveChangeDevices(
			@RequestBody SpmsChangeProductDTO productDto) {
		String pid=productDto.getProcessInstanceId();
		String taskId=productDto.getTaskId();
		List addDevices=productDto.getAddDevices();
		List delDevices=productDto.getDelDevices();
		//验证mac地址是否存在和是否超出最大绑定数
		String error="";
		for(int i=0;i<addDevices.size();i++){
			Map device=(Map) addDevices.get(i);
			String deviceId=setDeviceId(device);
			if(deviceId.equals("")){
				error+=device.get("deviceMac")+"";
			}
			device.put("deviceId", deviceId);
		}		
//		if(!error.equals("")){
//			error+="不存在";
//			return error;
//		}
		//切换用户产品状态
		String sql="select * from tb_workflow_variable  where  ikey in('changeProduct','orderProduct') and processId='"+pid+"'";
		List products=queryDao.getMapObjects(sql);
		String orderId="";
		String changeId="";
		for(int i=0;i<products.size();i++){
			Map m=(Map) products.get(i);
			if(m.get("iKey").equals("changeProduct")){
				if(null != m.get("iValue")){
					changeId=m.get("iValue").toString();
				}
			}
			if(m.get("iKey").equals("orderProduct")){
				if(null != m.get("iValue")){
					orderId=m.get("iValue").toString();
				}
			}
		}
		//获取订户ID
		sql="select usr.id ,usr.gw_id gwid from spms_user usr,spms_workorder workorder where "
				+ "workorder.spmsUserMobile=usr.mobile and workorder.processInstanceId='"+pid+"'";
		List lst=queryDao.getMapObjects(sql);
		String dhid="";
		String gwid="";
		if(lst.size()>0){
			dhid=((Map)lst.get(0)).get("id").toString();
			gwid=((Map)lst.get(0)).get("gwid").toString();
		}
		//获取当前时间
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String currentDate=df.format(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		
		/* 如果选择了已有产品，未选择变更产品 视为对已订购产品设备的变更 */
		if (null != orderId && !"".equals(orderId) && (null == changeId || "".equals(changeId))) {
			// 删除原有产品绑定
			sql = "delete from spms_user_product_binding where product_id='"
					+ orderId + "'";
			queryDao.doExecuteSql(sql);
			//增加设备回收记录
			String delLog = "";
			// 修改回收设备状态，并删除订户绑定
			for (int i = 0; i < delDevices.size(); i++) {
				Map m = (Map) delDevices.get(i);
				String mac = m.get("deviceMac").toString();
				String deviceId = m.get("deviceId").toString();
				sql = "update spms_device set onOff=0,operStatus=0,status=1,storage="
						+ m.get("storage").toString() + " where id='"
						+ deviceId + "'";
				queryDao.doExecuteSql(sql);
				sql = "delete from spms_user_product_binding where device_id='"
						+ deviceId + "'";
				delDeviceSetting(deviceId);
				delLog = delLog + "设备" + (i + 1) + ":" + mac + " ";
				queryDao.doExecuteSql(sql);
				
			}
			JSZCEntity entity = new JSZCEntity();
			entity.setProcessId(pid);
			entity.setTaskId(taskId);
			entity.setiKey("delLog");
			entity.setiValue(delLog);
			jszcDao.save(entity);

			//获取订户已有产品
			SpmsProduct spmsProduct = spmsProductDAO.findOne(orderId);
			//创建产品与新设备的绑定
			for(int i=0;i<addDevices.size();i++){
				Map device=(Map) addDevices.get(i);
				sql="insert into spms_user_product_binding "
						+ "(id,createDate,deviceType,displayHomepage,gwId,device_id,product_id,producttype_id,user_id,device_type,version)"
						+ "values ("
						+ "'"+UUID.randomUUID().toString()+"',"
						+ "'"+currentDate+"',"
						+ "'"+device.get("deviceTypeId").toString()+"',"
						+ "'"+1+"',"
						+ "'"+gwid+"',"
						+ "'"+device.get("deviceId").toString()+"',"
						+ "'"+orderId+"',"
						+ "'"+spmsProduct.getSpmsProductType().getId()+"',"
						+ "'"+dhid+"',"
						+ "'"+device.get("deviceTypeId").toString()+"',"
						+ "0"
						+ ")";
				queryDao.doExecuteSql(sql);
				// 更新新添加设备的状态
				sql = "update spms_device set status=2,storage=5 where id='"
						+ device.get("deviceId").toString() + "'";
				queryDao.doExecuteSql(sql);
			}
		}
		
		/* 如果选择了已有产品  并且选择了变更产品 */
		if (null != orderId && !"".equals(orderId) && null != changeId && !"".equals(changeId)) {
			// 删除原有产品绑定
			sql = "delete from spms_user_product_binding where product_id='"
					+ orderId + "'";
			queryDao.doExecuteSql(sql);
			// 更新原有产品状态
			sql = "update spms_product set status = 4 where id ='" + orderId
					+ "'";
			queryDao.doExecuteSql(sql);
			String delLog = "";
			// 修改回收设备状态，并删除订户绑定
			for (int i = 0; i < delDevices.size(); i++) {
				Map m = (Map) delDevices.get(i);
				String mac = m.get("deviceMac").toString();
				String deviceId = m.get("deviceId").toString();
				sql = "update spms_device set onOff=0,operStatus=0,status=1,storage="
						+ m.get("storage").toString() + " where id='"
						+ deviceId + "'";
				queryDao.doExecuteSql(sql);
				sql = "delete from spms_user_product_binding where device_id='"
						+ deviceId + "'";
				queryDao.doExecuteSql(sql);
				delDeviceSetting(deviceId);
				delLog = delLog + "设备" + (i + 1) + "mac:" + mac + " ";
			}
			JSZCEntity entity = new JSZCEntity();
			entity.setProcessId(pid);
			entity.setTaskId(taskId);
			entity.setiKey("delLog");
			entity.setiValue(delLog);
			jszcDao.save(entity);
		}
		
		/* 如果选择了变更产品 */
		if(null != changeId && !"".equals(changeId)){
			//创建新产品
			SpmsProductType spt = productTypeDao.findOne(changeId);
//			if(spt.getValidPeriod().equals("1")){
				c.add(Calendar.MONTH, Integer.parseInt(spt.getValidPeriod()));
//			}else if(spt.getValidPeriod().equals("2")){
//				c.add(Calendar.MONTH, 6);
//			}else if(spt.getValidPeriod().equals("3")){
//				c.add(Calendar.YEAR, 1);
//			}
			
			String expireDate = df.format(c.getTime());
			String productId=UUID.randomUUID().toString();
			sql="insert into spms_product ("
					+"id,createDate,activateDate,electricityCostRmb,expireDate,initialCostRmb,status,subscribeDate,type_id,user_id,version"
					+ ")"
					+ "values ("
					+ "'"+productId+"',"
					+ "'"+currentDate+"',"
					+ "'"+currentDate+"',"
					+ "'"+0+"',"
					+ "'"+expireDate+"',"
					+ "'"+0+"',"
					+ "'"+1+"',"
					+ "'"+currentDate+"',"
					+ "'"+changeId+"',"
					+ "'"+dhid+"',"
					+ "0"
					+ ")";
			queryDao.doExecuteSql(sql);
			
			//创建用户与新产品的绑定		
			for(int i=0;i<addDevices.size();i++){
				Map device=(Map) addDevices.get(i);
				sql="insert into spms_user_product_binding "
						+ "(id,createDate,deviceType,displayHomepage,gwId,device_id,product_id,producttype_id,user_id,device_type,version)"
						+ "values ("
						+ "'"+UUID.randomUUID().toString()+"',"
						+ "'"+currentDate+"',"
						+ "'"+device.get("deviceTypeId").toString()+"',"
						+ "'"+1+"',"
						+ "'"+gwid+"',"
						+ "'"+device.get("deviceId").toString()+"',"
						+ "'"+productId+"',"
						+ "'"+changeId+"',"
						+ "'"+dhid+"',"
						+ "'"+device.get("deviceTypeId").toString()+"',"
						+ "0"
						+ ")";
				queryDao.doExecuteSql(sql);
				
			}
			//保存用户替换的产品id
			sql="insert into tb_workflow_variable(processId,taskId,iKey,iValue) values ("
					+ "'"+pid+"',"
					+ "'"+taskId+"',"
					+ "'changeproductId',"
					+ "'"+productId+"'"
					+")";
			queryDao.doExecuteSql(sql);
			
			// 更新新添加设备的状态
			for(int i=0;i<addDevices.size();i++){
				Map device=(Map) addDevices.get(i);
				sql="update spms_device set status=2,storage=5 where id='"+device.get("deviceId").toString()+"'";
				queryDao.doExecuteSql(sql);			
			}
		}
		
		/* 给netty发送更新命令 */
		
		Map m=new HashMap();
		m.put("userId", dhid);
		m.put("commandType",0);
		m.put("messageType","SERVICEUPDATE");
		CommandUtil.asyncSendMessage(m);
		
		return productDto;
	}

	@Transactional
	@Override
	public SpmsChangeProductDTO saveTdDevices(
			@RequestBody SpmsChangeProductDTO productDto) {
		String pid=productDto.getProcessInstanceId();
		String taskId=productDto.getTaskId();
		List delDevices=productDto.getDelDevices();
		//验证mac地址是否存在和是否超出最大绑定数
		String error="";
		//切换用户产品状态
		String sql="select * from tb_workflow_variable  where  ikey in('changeProduct','orderProduct') and processId='"+pid+"'";
		List products=queryDao.getMapObjects(sql);
		String orderId="";
		for(int i=0;i<products.size();i++){
			Map m=(Map) products.get(i);
			if(m.get("iKey").equals("orderProduct")){
				if(null != m.get("iValue")){
					orderId=m.get("iValue").toString();
				}
			}
		}
		//获取订户ID
		sql="select usr.id ,usr.gw_id gwid from spms_user usr,spms_workorder workorder where "
				+ "workorder.spmsUserMobile=usr.mobile and workorder.processInstanceId='"+pid+"'";
		List lst=queryDao.getMapObjects(sql);
		String dhid="";
		String gwid="";
		if(lst.size()>0){
			dhid=((Map)lst.get(0)).get("id").toString();
			gwid=((Map)lst.get(0)).get("gwid").toString();
		}		
		//修改未回收的设备状态
		sql = "update spms_device set onOff=0,status=5 where id in (select device_id from spms_user_product_binding where product_id = '" + orderId + "')";
		queryDao.doExecuteSql(sql);
		
		//删除原有产品绑定(经需求讨论 不删除未回收设备与产品的绑定了)
//		sql="delete from spms_user_product_binding where product_id='"+orderId+"'";
//		queryDao.doExecuteSql(sql);
		//更新原有产品状态
		sql="update spms_product set status = 5 where id ='" + orderId + "'";
		queryDao.doExecuteSql(sql);
		String delLog = "";
		//修改回收设备状态，并删除订户绑定
		for(int i=0;i<delDevices.size();i++){
			Map m=(Map) delDevices.get(i);
			String mac=m.get("deviceMac").toString();
			String deviceId=m.get("deviceId").toString();
			sql="update spms_device set onOff=0,operStatus=0,status=1,storage=" + m.get("storage").toString() + " where id='"+deviceId+"'";
			queryDao.doExecuteSql(sql);
			sql="delete from spms_user_product_binding where device_id='"+deviceId+"'";
			queryDao.doExecuteSql(sql);
			delDeviceSetting(deviceId);
			delLog = delLog + "设备" + (i + 1) + ":" + mac + " ";
		}
		JSZCEntity entity = new JSZCEntity();
		entity.setProcessId(pid);
		entity.setTaskId(taskId);
		entity.setiKey("delLog");
		entity.setiValue(delLog);
		jszcDao.save(entity);
		
		/* 给netty发送更新命令 */
		Map m=new HashMap();
		m.put("userId", dhid);
		m.put("commandType",0);
		m.put("messageType","SERVICEUPDATE");
		CommandUtil.asyncSendMessage(m);
		
		return productDto;
	}

	@Override
	public String checkDevice(@RequestBody Map<String, Object> para) {
		String mac=para.get("deviceMac").toString();
		String typeId=para.get("deviceTypeId").toString();
		String sql="select id from spms_device where mac='"+mac+"' and type='"+typeId+"' and status=1";
		List lst=queryDao.getMapObjects(sql);
		String deviceId="";
		if(lst.size()>0){
			Map m=(Map) lst.get(0);
			deviceId=m.get("id").toString();
			m.put("deviceId", deviceId);
		}
		return deviceId;
	}

	private String setDeviceId(Map<String, Object> para) {
		String mac=para.get("deviceMac").toString();
		String typeId=para.get("deviceTypeId").toString();
		String sql="select id from spms_device where mac='"+mac+"' and type='"+typeId+"'";
		List lst=queryDao.getMapObjects(sql);
		String deviceId="";
		if(lst.size()>0){
			Map m=(Map) lst.get(0);
			deviceId=m.get("id").toString();
			m.put("deviceId", deviceId);
		}
		return deviceId;
	}

	@Override
	public List checkDevices(@RequestBody Map<String, Object> para) {
		List devices=(List) para.get("devices");
		for(int i=0;i<devices.size();i++){
			Map device=(Map) devices.get(i);
			device.put("deviceId",checkDevice(device));
		}
		return devices;
	}

	@Override
	public Map<String, Object> countUserDevices(SpmsChangeProductDTO productDto) {
		String pid=productDto.getProcessInstanceId();
		String taskId=productDto.getTaskId();
		List addDevices=productDto.getAddDevices();
		Map<String, Object> result = new HashMap<String, Object>();
		//验证mac地址是否存在和是否超出最大绑定数
		for(int i=0;i<addDevices.size();i++){
			Map device=(Map) addDevices.get(i);
			String deviceId=setDeviceId(device);
			device.put("deviceId", deviceId);
		}
		String sql="select * from tb_workflow_variable  where  ikey in('changeProduct','orderProduct') and processId='"+pid+"'";
		List products=queryDao.getMapObjects(sql);
		String orderId="";
		String changeId="";
		for(int i=0;i<products.size();i++){
			Map m=(Map) products.get(i);
			if(m.get("iKey").equals("changeProduct")){
				if(null != m.get("iValue")){
					changeId=m.get("iValue").toString();
				}
			}
			if(m.get("iKey").equals("orderProduct")){
				if(null != m.get("iValue")){
					orderId=m.get("iValue").toString();
				}
			}
		}
		//获取订户ID
		sql="select usr.id ,usr.gw_id gwid from spms_user usr,spms_workorder workorder where "
				+ "workorder.spmsUserMobile=usr.mobile and workorder.processInstanceId='"+pid+"'";
		List lst=queryDao.getMapObjects(sql);
		String dhid="";
		String gwid="";
		if(lst.size()>0){
			dhid=((Map)lst.get(0)).get("id").toString();
			gwid=((Map)lst.get(0)).get("gwid").toString();
		}
		//如果未选变更产品
		if (null != orderId && !"".equals(orderId) && (null == changeId || "".equals(changeId))) {
			SpmsProduct sp = spmsProductDAO.getOne(orderId);
			SpmsProductType spt = sp.getSpmsProductType();
			result = countResult(spt,addDevices,result);
		}
		//如果选变更产品
		if (null != changeId && !"".equals(changeId)) {
			SpmsProductType spt = productTypeDao.getOne(changeId);
			result = countResult(spt,addDevices,result);
		}
		return result;
	}
	
	/**
	 * 计算设备绑定结果是否符合
	 * @param spt
	 * @param addDevices
	 * @param result
	 * @return
	 */
	private Map<String,Object> countResult(SpmsProductType spt, List addDevices, Map<String,Object> result){
		boolean success = true;
		String message = "";
		int kongTiaoCount = spt.getKongTiaoCount();
		int chuanGanCount = spt.getChuangGanCount();
		int kongTiao = 0;
		int chuanGan = 0;
		for (Object obj : addDevices) {
			Map device = (Map)obj;
			SpmsAirCondition sd = spmsDeviceDAO.getOne(device.get("deviceId").toString());
			if(sd.getType() == 2){
				kongTiao++;
			}else if(sd.getType() == 3){
				chuanGan++;
			}else{
				success = false;
				message = "(mac:" + sd.getMac() + ")设备类型不对";
				break;
			}
		}
		if(kongTiao > kongTiaoCount){
			success = false;
			message = "空调数量超过绑定最大数量";
		}
		if(chuanGan > chuanGanCount){
			success = false;
			message = "门窗传感数量超过绑定最大数量";
		}
		result.put("success", success);
		result.put("msg", message);
		return result;
	}
//删除设备定时设置
	private void delDeviceSetting(String deviceId) {
		List<RairconSetting> rairconCurveList = rairconCurveDAO
				.getSpmsRairconCurveByDeviceId(deviceId);
		if (rairconCurveList != null && rairconCurveList.size() > 0) {
			rairconCurveDAO.delete(rairconCurveList);
		}
		String sql = "delete from rairconcurve_clocksetting where spmsDevice_id='"
				+ deviceId + "'";
		queryDao.doExecuteSql(sql);
	}

	@Override
	public Map<String,Object> getUserByWorkOrder(Map<String, Object> para) {
		String mobile=para.get("mobile").toString();
		String sql="select spmsUserMobile from spms_workorder where spmsUserMobile ='"+mobile+"'";
		List mobileList=queryDao.getMapObjects(sql);
		Map<String,Object> m=new HashMap<String,Object>();
		if(mobileList!=null && mobileList.size()>0){
			m.put("msg", true);
		}
		return m;
	}

	@Override
	public Map<String, Object> listStorage(Map<String, Object> para) {
		String sql = "select t.code,t.value from dict_device_storage t where t.code != '5'";
		List<Map<String, Object>> list=queryDao.getMapObjects(sql);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("storage", list);
		return result;
	}
}
