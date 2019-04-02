///**
// * 
// */
//package com.harmazing.spms.product.manager;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//import com.harmazing.spms.base.dto.QueryTranDTO;
//import com.harmazing.spms.base.util.*;
//import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
//import com.harmazing.spms.user.entity.SpmsUserProductBinding;
//import com.harmazing.spms.workorder.dto.SpmsWorkOrderDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.google.common.collect.Maps;
//import com.harmazing.spms.base.dao.QueryDAO;
//import com.harmazing.spms.device.dto.SpmsDeviceDTO;
//import com.harmazing.spms.product.dao.SpmsProductDAO;
//import com.harmazing.spms.product.dao.SpmsProductTypeDAO;
//import com.harmazing.spms.product.dto.SpmsProductDTO;
//import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
//import com.harmazing.spms.product.entity.SpmsProduct;
//import com.harmazing.spms.product.entity.SpmsProductType;
//import com.harmazing.spms.product.manager.SpmsProductManager;
//import com.harmazing.spms.user.dao.SpmsUserDAO;
//import com.harmazing.spms.user.entity.SpmsUser;
//import sun.applet.Main;
//
//@Service("spmsProductManager")
//public class SpmsProductManagerImpl implements SpmsProductManager {
//	
//	 @Autowired
//	 private SpmsProductDAO spmsProductDAO;
//	 @Autowired
//	 private SpmsProductTypeDAO spmsProductTypeDAO;
//	 @Autowired
//	 private SpmsUserDAO spmsUserDAO;
//	 @Autowired
//	 private SpmsDeviceDAO spmsDeviceDAO;
//	 @Autowired
//	 private QueryDAO queryDAO;
//	@Autowired
//	private SpmsUserProductBindingDAO spmsUserProductBindingDAO;
//	    /* (non-Javadoc)
//	     * @see com.harmazing.spms.device.manager.SpmsDeviceManager#doSave(com.harmazing.spms.device.dto.SpmsDeviceDTO)
//	     */
//	    @Override
//	    @Transactional(readOnly=false)
//	    public SpmsProductDTO doSave(SpmsProductDTO spmsProductDTO) {
//	    	SpmsProduct spmsProduct = null;
//			if(spmsProductDTO.getId() != null) {
//				spmsProduct = spmsProductDAO.findOne(spmsProductDTO.getId());
//				if(spmsProductDTO.getUpdateFlag()==0){
//					/*更新产品信息*/
//					BeanUtils.copyProperties(spmsProductDTO, spmsProduct);
//				}else{
//					/*绑定设备*/
//				}
//			} else {
//				/*新增产品*/
//				spmsProduct = new SpmsProduct();
//				BeanUtils.copyProperties(spmsProductDTO, spmsProduct);
//				SpmsUser spmsUser=spmsUserDAO.findOne(spmsProductDTO
//						.getSpmsUserDTO().getId());
//				spmsProduct.setSpmsUser(spmsUser);
//			}
//		
//			spmsProductDAO.save(spmsProduct);
//			spmsProductDTO.setId(spmsProduct.getId());
//			return spmsProductDTO;
//	    }
//
//	    
//	    @Transactional
//	    public SpmsProductDTO doSave2(SpmsProductDTO spmsProductDTO) {
//	    	SpmsProduct spmsProduct = null;
//			if(spmsProductDTO.getId() != null) {
//				spmsProduct = spmsProductDAO.findOne(spmsProductDTO.getId());
//			} else {
//				spmsProduct = new SpmsProduct();
//			}
//			BeanUtils.copyProperties(spmsProductDTO, spmsProduct);
//
//			SpmsUser spmsUser=spmsUserDAO.findOne(spmsProductDTO
//					.getSpmsUserDTO().getId());
//			spmsProduct.setSpmsUser(spmsUser);
//			
//			
//			spmsProductDAO.save(spmsProduct);
//			
//			return spmsProductDTO;
//	    }
//
//	    @Override
//		public SpmsProductDTO doQuery(String id)  {
//			
//			   SpmsProductDTO spd = new SpmsProductDTO();
//			   SpmsProduct sp = spmsProductDAO.findOne(id);
//				
//				BeanUtils.copyProperties(sp, spd);
//				
//				SpmsProductType pt=spmsProductTypeDAO.findOne(sp.getSpmsProductType().getId());
//				SpmsProductTypeDTO stp = new SpmsProductTypeDTO();
//				BeanUtils.copyProperties(pt, stp);
//				spd.setSpmsProductTypeDTO(stp);
//				try {
//					if(sp.getSubscribeDate()!=null){
//						spd.setSubDate(DateUtil.parseDateToString(sp.getSubscribeDate(), "yyyy-MM-dd"));
//					}
//					if(sp.getActivateDate()!=null){
//						spd.setActDate(DateUtil.parseDateToString(sp.getActivateDate(), "yyyy-MM-dd"));
//					}
//					if(sp.getExpireDate()!=null){
//						spd.setExpDate(DateUtil.parseDateToString(sp.getExpireDate(), "yyyy-MM-dd"));
//					}
//					spd.setInitialCostRmb(sp.getSpmsProductType().getIndexRmb());
//					
//					//为每个设备计算出用电量
//					String sql = "select t.device_id from spms_user_product_binding t where t.product_id = '" + sp.getId() + "'";
//					List<Object> list = queryDAO.getObjects(sql);
//					Double ydl = 0d;
//					if(list !=null && list.size()>0){
//						for (Object obj : list) {
//							String deviceId = obj.toString();
//							//获取设备用电量
//							Calendar c = Calendar.getInstance();
//							sql = "SELECT (MAX(t.accumulatePower) - MIN(t.accumulatePower)) FROM spms_ac_status t WHERE t.device_id = '" + deviceId + "' and date_format(t.start_time,'%Y-%c') = '" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "' ";
//							List<Object> tmpList = queryDAO.getObjects(sql);
//							if(null != tmpList && tmpList.size() > 0 && null != tmpList.get(0)){
//								ydl = ydl + ((BigInteger)tmpList.get(0)).doubleValue();
//							}
//						}
//					}
//					//通过用电量、用电成本，计算用电费用
//					
//					spd.setElectricityCostRmb(sp.getSpmsProductType().getElectrovalenceRmb() * ydl);
//					
//					spd.setSpmsTypeName(pt.getNames());//类型名称
//					spd.setUserName(sp.getSpmsUser().getFullname());//订户名称
//					spd.setUserId(sp.getSpmsUser().getId());//订户id
//					
//					spd.setStat(DictUtil.getDictValue("product_status", sp.getStatus().toString()));
//					
//					
//				} catch (ParseException e) {
//					
//					e.printStackTrace();
//				}
//				
//			
//				return spd;
//			}
//	    
//		@Override
//		public Boolean doDelete(String id) {
//			try {
//				 SpmsProduct sp = spmsProductDAO.findOne(id);
//				   spmsProductDAO.delete(sp);
//				  
//				return true;
//			} catch (Exception e) {
//				return false;
//			}
//			  
//		}
//
//		@Override
//		@Transactional
//		public SpmsProductDTO doSaveDL(SpmsProductDTO spmsProductDTO) {
//		 	SpmsProduct spmsProduct = null;
//			if(spmsProductDTO.getId() != null) {
//				spmsProduct = spmsProductDAO.findOne(spmsProductDTO.getId());
//			} else {
//				spmsProduct = new SpmsProduct();
//			}
//			spmsProduct.setActivateDate(spmsProductDTO.getActivateDate());
//			
//			//BeanUtils.copyProperties(spmsProductDTO, spmsProduct);
//			SpmsProductType spmsProductType = spmsProductTypeDAO.findOne(spmsProductDTO.getSpmsProductTypeDTO().getId());
//			spmsProduct.setSpmsProductType(spmsProductType);
//			spmsProduct.setStatus(spmsProductDTO.getStatus());
//			SpmsUser spmsUser=spmsUserDAO.findOne(spmsProductDTO.getSpmsUserDTO().getId());
//			spmsProduct.setSpmsUser(spmsUser);
//			spmsProduct.setExpireDate(spmsProductDTO.getExpireDate());
//			spmsProduct.setSubscribeDate(spmsProductDTO.getSubscribeDate());
//			spmsProduct.setInitialCostRmb(spmsProductDTO.getInitialCostRmb());
//			spmsProduct.setElectricityCostRmb(spmsProductDTO.getElectricityCostRmb());
//			spmsProductDAO.save(spmsProduct);
//			
//			return spmsProductDTO;
//		}
//
//		@Override
//		@Transactional
//		public Boolean doDeleteAll(int[] data1) {
//			try {
//				for(int i=0;i<data1.length;i++){
//					
//					spmsProductDAO.doDeleteById(Integer.valueOf(data1[i]).toString());
//				}
//				return true;
//			} catch (Exception e) {
//				return false;
//			}
//			
//			
//			
//		}
//
//		/************************add by hanhao************************/
//		@Override
//		public List<SpmsProductDTO> findProductByUserId(String userId) {
//			List<SpmsProduct> spmsProductList = spmsProductDAO.findAll();
//			List<SpmsProductDTO> spdList = new ArrayList<SpmsProductDTO>();
//			SpmsProductDTO spt = new SpmsProductDTO();
//			for(SpmsProduct sp : spmsProductList){
//				BeanUtils.copyProperties(sp, spt);
//				spdList.add(spt);
//			}
//			return spdList;
//		}
//		@Override
//		public List<SpmsDeviceDTO> findDeviceByProductId(){
//			
//			return null;
//		}
//		/************************end by hanhao************************/
//
//		@Override
//		public Map<String, Object> getCountAmount(String type, String start,String end) {
//			Map<String,Object> result = Maps.newLinkedHashMap();
//			String sql = "SELECT COUNT(*) as num,sp.activateDate as time , sum(spt.countRmb) as money FROM spms_product sp,spms_product_type spt WHERE sp.type_id = spt.id  AND sp.status='1' ";
//			String sqlend = " GROUP BY DATE_FORMAT(sp.activateDate,'%Y-%m') ORDER BY time asc";
//			if(!StringUtil.isNUll(type)){
//				String t = "AND sp.type_id ='"+type+"' ";
//				sql = sql.concat(t);
//			}
//			if(!StringUtil.isNUll(start)){
//				String temp = new String();
//				String a[] = start.split("年");
//				String year = a[0];
//				String month = (a[1].split("月"))[0];
//				int m = Integer.parseInt(month);
//				if(m<10){
//					month = "0"+month;
//				}
//				temp = year+"-"+month;
//				String t = "AND DATE_FORMAT(sp.activateDate,'%Y-%m') >='"+temp+"' ";
//				sql = sql.concat(t);
//			}
//			if(!StringUtil.isNUll(end)){
//				String temp = new String();
//				String a[] = end.split("年");
//				String year = a[0];
//				String month = (a[1].split("月"))[0];
//				int m = Integer.parseInt(month);
//				if(m<10){
//					month = "0"+month;
//				}
//				temp = year+"-"+month;
//				String t = "AND DATE_FORMAT(sp.activateDate,'%Y-%m') <='"+temp+"' ";
//				sql = sql.concat(t);
//			}
//			sql = sql.concat(sqlend);
//			List temp = queryDAO.getMapObjects(sql);
//			for(int i = 0 ; i < temp.size() ; i ++){
//				Map map = (Map) temp.get(i);
//				String time =(((Date)map.get("time")).getYear()+1900)+"年"+ (((Date)map.get("time")).getMonth()+1)+"月";
//				BigInteger count = (BigInteger) map.get("num");
//				Double rmb = (Double) map.get("money");
//				Object[] data = new Object[] {count, rmb};
//				result.put(time, data);
//			}
//			return result;
//		}
//
//
//		@Override
//		public Map<String, Object> getCostEarnings(String type, String start,String end) throws ParseException {
//			Map<String,Object> result = Maps.newLinkedHashMap();
//			String sql = "SELECT sp.activateDate as time ,sum(spt.indexRmb) as cost,sum(spt.countRmb) as allmoney FROM spms_product sp,spms_product_type spt WHERE sp.type_id = spt.id  AND sp.status='1' ";
//			String sqlend = " GROUP BY DATE_FORMAT(sp.activateDate,'%Y-%m') ORDER BY time asc";
//			if(!StringUtil.isNUll(type)){
//				String t = "AND sp.type_id ='"+type+"' ";
//				sql = sql.concat(t);
//			}
//			if(!StringUtil.isNUll(start)){
//				String temp = new String();
//				String a[] = start.split("年");
//				String year = a[0];
//				String month = (a[1].split("月"))[0];
//				int m = Integer.parseInt(month);
//				if(m<10){
//					month = "0"+month;
//				}
//				temp = year+"-"+month;
//				String t = "AND DATE_FORMAT(sp.activateDate,'%Y-%m') >='"+temp+"' ";
//				sql = sql.concat(t);
//			}
//			if(!StringUtil.isNUll(end)){
//				String temp = new String();
//				String a[] = end.split("年");
//				String year = a[0];
//				String month = (a[1].split("月"))[0];
//				int m = Integer.parseInt(month);
//				if(m<10){
//					month = "0"+month;
//				}
//				temp = year+"-"+month;
//				String t = "AND DATE_FORMAT(sp.activateDate,'%Y-%m') <='"+temp+"' ";
//				sql = sql.concat(t);
//			}
//			sql = sql.concat(sqlend);
//			List temp = queryDAO.getMapObjects(sql);
//			for(int i = 0 ; i < temp.size() ; i ++){
//				Map map = (Map) temp.get(i);
//				String time =(((Date)map.get("time")).getYear()+1900)+"年"+ (((Date)map.get("time")).getMonth()+1)+"月";
//				Double cost = (Double) map.get("cost");
//				Double earnings = ((Double) map.get("allmoney"))-cost;
//				Double[] data = new Double[] {cost, earnings};
//				result.put(time, data);
//			}
//			return result;
//		}
//
//
//		@Override
//		public Map<String, Object> getAllProducttype() {
//			Map<String,Object> result = Maps.newHashMap();
//			List<SpmsProductType> list = spmsProductTypeDAO.findAll();
//			for(int i=0;i<list.size();i++){
//				SpmsProductType spt = list.get(i);
//				result.put(spt.getNames(), spt.getId());
//			}
//			return result;
//		}
//
//	public Map<String,Object> getProductInfoByWorkOrder(Map<String,Object> map) {
//		Map<String,Object> result = new HashMap<String, Object>();
//		SearchFilter searchFilter = new SearchFilter("spmsWorkOrder.id",map.get("id").toString());
//		List <SpmsProduct> spmsProducts = spmsProductDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter, SpmsProduct.class));
//		List <SpmsProductDTO> spmsProductDTOs = Lists.newArrayList();
//		for(SpmsProduct spmsProduct : spmsProducts) {
//			SpmsProductDTO spmsProductDTO = new SpmsProductDTO();
//			spmsProductDTO.setId(spmsProduct.getId());
//			spmsProductDTO.setSpmsTypeName(spmsProduct.getSpmsProductType().getNames());
//			try {
//				spmsProductDTO.setGwMac(spmsProduct.getSpmsUser().getSpmsDevice().getMac());
//			} catch (Exception e) {
//			}
//			spmsProductDTOs.add(spmsProductDTO);
//		}
//		result.put("spmsProductDTOs", spmsProductDTOs);
//		String sql = "select t.device_id,t1.mac,t1.type from spms_user_product_binding t,spms_device t1 where" +
//				" t.device_id = t1.id and  t.product_id = '" + spmsProductDTOs.get(0).getId() + "'";
//		result.put("devices", queryDAO.getMapObjects(sql));
//		return result;
//	}
//
//	@Transactional
//	@Override
//	public SpmsProductDTO bindDeviceToProduct(SpmsProductDTO spmsProductDTO) throws Exception{
//		List<SpmsDevice> listDevices = new ArrayList<SpmsDevice>();
//		// 订户
//		SpmsUser spmsUser = spmsUserDAO.findOne(spmsProductDTO.getSpmsUserDTO().getId());
//		SpmsDevice sd = spmsUser.getSpmsDevice();
//		if(null != sd){
//			listDevices.add(sd);
//		}
//		spmsUser.setSpmsDevice(null);
//		spmsUserDAO.saveAndFlush(spmsUser);
//		// 产品
//		SpmsProduct spmsProduct = spmsProductDAO.findOne(spmsProductDTO.getId());
//		String sql = "select id from spms_user_product_binding where product_id = '" + spmsProduct.getId() + "'";
//		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
//		
//		for (Map<String, Object> map : list) {
//			SpmsUserProductBinding supb = spmsUserProductBindingDAO.findOne(map.get("id").toString());
//			listDevices.add(supb.getSpmsDevice());
//			spmsUserProductBindingDAO.delete(supb);
//		}
//		
//		for (SpmsDevice spmsDevice : listDevices) {
//			spmsDevice.setOperStatus(0);
//			spmsDevice.setStatus(1);
//			spmsDevice.setOnOff(0);
//			spmsDevice.setStorage(1);
//			spmsDeviceDAO.saveAndFlush(spmsDevice);
//		}
//		
//		//网关
//		SpmsDevice spmsDeviceGW = spmsDeviceDAO.findByMac(spmsProductDTO.getGwMac());
//		//设备
//		List <String> deviceMacs = Arrays.asList(spmsProductDTO.getSpmsDeviceMac().split(","));
//		List <SpmsDevice> spmsDevices = spmsDeviceDAO.findByMacs(deviceMacs);
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		if(spmsDeviceGW == null) {
////			throw new RuntimeException("库存中不存在该设备。");
//			map.put("result", "库存中不存在该设备。");
//			spmsProductDTO.setMessage(map);
//			return spmsProductDTO;
//		}
//		if((spmsDevices == null) || (spmsDevices.size() !=deviceMacs.size()) ) {
////			throw new RuntimeException("库存中不存在该设备。");
//			map.put("result", "库存中不存在该设备。");
//			spmsProductDTO.setMessage(map);
//			return spmsProductDTO;
//		}
//		
//		//绑定网关
//		spmsUser.setSpmsDevice(spmsDeviceGW);
//		spmsDeviceGW.setStatus(2);
//		spmsDeviceGW.setStorage(5);
//
//		//绑定设备
//		for(SpmsDevice spmsDevice : spmsDevices) {
//			SpmsUserProductBinding spmsUserProductBinding = new SpmsUserProductBinding();
//			spmsUserProductBinding.setSpmsUser(spmsUser);
//			spmsUserProductBinding.setSpmsDevice(spmsDevice);
//			spmsUserProductBinding.setSpmsProduct(spmsProduct);
//			spmsUserProductBinding.setSpmsProductType(spmsProduct.getSpmsProductType());
//			spmsUserProductBinding.setDeviceType(spmsDevice.getType());
//			spmsUserProductBinding.setGwId(spmsDeviceGW.getId());
//			spmsUserProductBinding.setVersion(1L);
//			spmsUserProductBindingDAO.save(spmsUserProductBinding);
//
//			spmsDevice.setStatus(2);
//			spmsDevice.setStorage(5);
// 		}
//		Date date = new Date();
//		Date dateN = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		spmsProduct.setActivateDate(df.parse(df.format(dateN)));
//        spmsProduct.setStatus(1);
//        
//		// 给产品到期时间赋值
//		int validPeriod = Integer.parseInt(spmsProduct.getSpmsProductType()
//				.getValidPeriod());
////		if (validPeriod == 1) {
//			Calendar c = Calendar.getInstance();
//			c.setTime(spmsProduct.getSubscribeDate());
//			c.add(Calendar.MONTH, validPeriod);
//			spmsProduct.setExpireDate(df.parse(df.format(c.getTime())));
////		} else if (validPeriod == 2) {
////			Calendar c = Calendar.getInstance();
////			c.setTime(spmsProduct.getSubscribeDate());
////			c.add(Calendar.MONTH, 6);
////			spmsProduct.setExpireDate(df.parse(df.format(c.getTime())));
////		} else if (validPeriod == 3) {
////			Calendar c = Calendar.getInstance();
////			c.setTime(spmsProduct.getSubscribeDate());
////			c.add(Calendar.YEAR, 1);
////			spmsProduct.setExpireDate(df.parse(df.format(c.getTime())));
////		}
//		spmsProductDAO.save(spmsProduct);
//		
//		/* 给netty发送更新命令 */
//		Map m=new HashMap();
//		m.put("userId", spmsUser.getId());
//		m.put("commandType",0);
//		m.put("messageType","SERVICEUPDATE");
//		CommandUtil.asyncSendMessage(m);
//		
//		return spmsProductDTO;
//	}
//
//
//	@Override
//	public Map<String, Object> getOldEarnings(String type, String start,String end) throws ParseException {
//		Map<String,Object> result = Maps.newLinkedHashMap();
//		String sql = "SELECT sp.activateDate as time ,sum(spt.indexRmb) as cost,sum(spt.countRmb) as allmoney FROM spms_product sp,spms_product_type spt WHERE sp.type_id = spt.id  AND sp.status='1' ";
//		String sqlend = " GROUP BY DATE_FORMAT(sp.activateDate,'%Y-%m') ORDER BY time desc";
//		if(!StringUtil.isNUll(type)){
//			String t = "AND sp.type_id ='"+type+"' ";
//			sql = sql.concat(t);
//		}
//		if(!StringUtil.isNUll(start)){
//			String temp = new String();
//			String a[] = start.split("年");
//			String year = a[0];
//			String month = (a[1].split("月"))[0];
//			int m = Integer.parseInt(month);
//			if(m<10){
//				month = "0"+month;
//			}
//			temp = year+"-"+month;
//			String t = "AND DATE_FORMAT(sp.activateDate,'%Y-%m') >='"+temp+"' ";
//			sql = sql.concat(t);
//		}
//		if(!StringUtil.isNUll(end)){
//			String temp = new String();
//			String a[] = end.split("年");
//			String year = a[0];
//			String month = (a[1].split("月"))[0];
//			int m = Integer.parseInt(month);
//			if(m<10){
//				month = "0"+month;
//			}
//			temp = year+"-"+month;			
//			String t = "AND DATE_FORMAT(sp.activateDate,'%Y-%m') <='"+temp+"' ";
//			sql = sql.concat(t);
//		}
//		sql = sql.concat(sqlend);
//		List temp = queryDAO.getMapObjects(sql);
//		for(int i = 0 ; i < temp.size() ; i ++){
//			Map map = (Map) temp.get(i);
//			Date t = (Date)map.get("time");
//			Date oldt = (Date) t.clone();
//			oldt.setYear(oldt.getYear()-1);
//			String sqlt = "SELECT CASE WHEN sp.activateDate IS NULL THEN 0 ELSE sp.activateDate END AS oldtime, CASE WHEN sum(spt.indexRmb) IS NULL THEN 0 ELSE sum(spt.indexRmb) END AS oldcost, CASE WHEN sum(spt.countRmb) IS NULL THEN 0 ELSE sum(spt.countRmb) END AS oldallmoney FROM spms_product sp, spms_product_type spt WHERE sp.type_id = spt.id AND sp. STATUS = '1' AND DATE_FORMAT(sp.activateDate,'%Y-%m') = '"+DateUtil.parseDateToString(oldt, "yyyy-MM")+"' GROUP BY DATE_FORMAT(sp.activateDate, '%Y-%m')";
//			List l = queryDAO.getMapObjects(sqlt);
//			if(l.size() > 0){
//				DecimalFormat df = new DecimalFormat("######000.0000"); 
//				Map m = (Map) l.get(0);
//				//String time = t.getMonth()+1+"月";
//				String time = (t.getYear()+1900)+"年"+ (t.getMonth()+1)+"月";
//				Double cost = (Double) map.get("cost");
//				Double earnings = ((Double) map.get("allmoney"))-cost;
//
//				Double oldcost = (Double) m.get("oldcost");
//				Double oldearnings = ((Double) m.get("oldallmoney"))-oldcost;
//
//				Double[] data = new Double[] {Double.valueOf(df.format(((cost-oldcost)/oldcost)))*100, Double.valueOf(df.format(((earnings-oldearnings)/oldearnings)))*100};
//				result.put(time, data);
//			}
//		}
//		return result;
//	}
//
//
//	@Override
//	public List<SpmsDeviceDTO> getProductDeviceList(String id) {
//		SpmsProduct sp = spmsProductDAO.findOne(id);
//		SpmsUser su = sp.getSpmsUser();
//		List<SpmsDeviceDTO> result = Lists.newArrayList();
//		List<SpmsUserProductBinding> bindList= spmsUserProductBindingDAO.getUserBinding(su.getId(), sp.getId());
//		for(SpmsUserProductBinding spmsUserProductBinding : bindList) {
//			SpmsDeviceDTO spmsDeviceDTO = new SpmsDeviceDTO();
//			BeanUtils.copyProperties(spmsUserProductBinding.getSpmsDevice(), spmsDeviceDTO);
//			spmsDeviceDTO.setTypeText(DictUtil.getDictValue("device_type", spmsUserProductBinding.getSpmsDevice().getType().toString()));
//			result.add(spmsDeviceDTO);
//		}
//		if(su.getSpmsDevice()!=null){
//			SpmsDeviceDTO spmsDeviceDTO = new SpmsDeviceDTO();
//			SpmsDevice gw = su.getSpmsDevice();
//			BeanUtils.copyProperties(gw, spmsDeviceDTO);
//			spmsDeviceDTO.setTypeText(DictUtil.getDictValue("device_type", gw.getType().toString()));
//			result.add(spmsDeviceDTO);
//		}
//		return result;
//	}
//
//	public QueryTranDTO getProductCont(QueryTranDTO queryTranDTO) throws ParseException {
//		String startDate= null;
//		String endDate=null;
//		//产品类型
//		String names = null;
//		for(SearchFilter searchFilter : queryTranDTO.getSearchFilters()) {
//			if("createDate".equals(searchFilter.getFieldName())) {
//				startDate = searchFilter.getValue().toString().split(",")[0];
//				endDate = searchFilter.getValue().toString().split(",")[1];
//			} else if ("names".equals(searchFilter.getFieldName())) {
//				if(searchFilter.getValue() != null) {
//					names = searchFilter.getValue().toString();
//				}
//			}
//		}
//		//图标1 销售数量金额
//		Map<String, Object> infos1 = getCountAmount(names, startDate, endDate);
//		//图标2 成本收益
//		Map<String, Object> infos2 = getCostEarnings(names, startDate, endDate);
//		//图表3 成本同比增长 收益同比增长
//		Map<String, Object> infos3 = getOldEarnings(names, startDate, endDate);
//
//		//获取所有产品类型
//		Map<String, Object> productTypes = getAllProducttype();
//
//		Map<String,Object> returnInfos = Maps.newHashMap();
//		returnInfos.put("chart1",infos1);
//		returnInfos.put("chart2",infos2);
//		returnInfos.put("chart3",infos3);
//		returnInfos.put("productTypes",productTypes);
//		queryTranDTO.setReturnInfos(returnInfos);
//
//		Set <String> keys = Sets.newHashSet();
//		keys.addAll(infos1.keySet());
//		keys.addAll(infos2.keySet());
//		keys.addAll(infos3.keySet());
//		List <String> keysL = Lists.newArrayList();
//		keysL.addAll(keys);
//		Collections.sort(keysL);
//
//
//		List <Map<String,Object>> result = Lists.newArrayList();
//		for(String key : keysL) {
//			Map <String, Object> map = Maps.newHashMap();
//			map.put("time",key);
//			map.put("productType",names==null?"全部":names);
//			List <BigInteger> infosL1 = handleDoubleObject(infos1.get(key));
//			List <Double> infosL2 = handleDoubleObject(infos2.get(key));
//			List <Double> infosL3 = handleDoubleObject(infos3.get(key));
//
//			map.put("count",infosL1==null?0:infosL1.get(0));
//			map.put("mount",infosL1==null?0:infosL1.get(1));
//			map.put("cost",infosL2==null?0:infosL2.get(0));
//			map.put("profit",infosL2==null?0:infosL2.get(1));
//			map.put("tCost",infosL3==null?0:infosL3.get(0));
//			map.put("tProfit",infosL3==null?0:infosL3.get(1));
//			result.add(map);
//		}
//		queryTranDTO.setCustomDatas(result);
//		queryTranDTO.setAllCount(Long.valueOf(result.size()));
//		queryTranDTO.setShowColumnDTOs(queryTranDTO.getQueryDTO().getColumn());
//
//		return queryTranDTO;
//	}
//
//	private <T> List <T> handleDoubleObject(Object objects) {
//		if(objects == null) {
//			return null;
//		}
//		List <T> numbers = Lists.newArrayList();
//		for(Object object : (Object[])objects) {
//			numbers.add((T)object);
//		}
//		return numbers;
//	}
//
//
//	@Override
//	public Map<String, Object> checkGWandDevice(SpmsProductDTO spmsProductDTO) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		// 网关
//		SpmsDevice spmsDeviceGW = null;
//		List<SpmsDevice> listgw =  spmsDeviceDAO.findByMacAndSn(spmsProductDTO.getGwMac(),spmsProductDTO.getGwMac());
//		if(listgw != null && listgw.size() == 1){
//			spmsDeviceGW = listgw.get(0);
//			result.put("gw", spmsDeviceGW.getMac());
//		}
//		// 设备
//		List<String> deviceMacs = Arrays.asList(spmsProductDTO.getSpmsDeviceMac().split(","));
//		List<SpmsDevice> spmsDevices = spmsDeviceDAO.findByMacs(deviceMacs);
//
//		result.put("result", false);
//		// 订户
//		SpmsUser spmsUser = spmsUserDAO.findOne(spmsProductDTO.getSpmsUserDTO().getId());
//		SpmsDevice sd = spmsUser.getSpmsDevice();
//		if(null != sd && spmsProductDTO.getGwMac().equals(sd.getMac())){
//
//		}else{
//			if (spmsDeviceGW == null || spmsDeviceGW.getType() != 1) {
//				result.put("result", true);
//				result.put("message","库存中不存在该网关。");
//			}
//		}
//		
//		// 产品
//		SpmsProduct spmsProduct = spmsProductDAO.findOne(spmsProductDTO.getId());
//		String sql = "select id from spms_user_product_binding where product_id = '"
//				+ spmsProduct.getId() + "'";
//		List<Map<String, Object>> list = queryDAO.getMapObjects(sql);
//
//		int i = 0;
//		for (Map<String, Object> map : list) {
//			SpmsUserProductBinding supb = spmsUserProductBindingDAO.findOne(map
//					.get("id").toString());
//			if(deviceMacs.contains(supb.getSpmsDevice().getMac())){
//				i++;
//			}
//		}
//		
//		if ((spmsDevices == null) || ((spmsDevices.size() + i) != deviceMacs.size())) {
//			result.put("result", true);
//			result.put("message","库存中不存在该设备。");
//		}
//		return result;
//	}
//
//
//	@Override
//	public List<SpmsProduct> getProductList(SpmsProductDTO spmsProductDTO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
