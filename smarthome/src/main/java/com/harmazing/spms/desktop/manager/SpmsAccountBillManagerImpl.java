package com.harmazing.spms.desktop.manager;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.base.util.MailBean;
import com.harmazing.spms.base.util.MailUtils;
import com.harmazing.spms.base.util.PropertyUtil;
import com.harmazing.spms.desktop.dao.SpmsAccountBillDAO;
import com.harmazing.spms.desktop.dao.SpmsProductMFeeDAO;
import com.harmazing.spms.desktop.dto.SpmsAccointBillDTO;
import com.harmazing.spms.desktop.dto.SpmsProductMFeeDTO;
import com.harmazing.spms.desktop.entity.SpmsAccountBill;
import com.harmazing.spms.desktop.entity.SpmsProductMFee;
import com.harmazing.spms.desktop.manager.SpmsAccountBillManager;
import com.harmazing.spms.desktop.manager.SpmsProductMFeeManager;
import com.harmazing.spms.product.dao.SpmsProductDAO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.user.dto.SpmsUserDTO;
import com.harmazing.spms.user.entity.SpmsUser;

/**
 * billSERVICE
 * @author hanhao
 * @version v1.0
 */
@Component("spmsAccountBillManagerImpl")
public class SpmsAccountBillManagerImpl implements SpmsAccountBillManager {

	@Autowired
	private SpmsAccountBillDAO spmsAccountBillDao;
	@Autowired
	private SpmsProductMFeeManager spmsProductMFeeManager;
	
	@Autowired 
	private SpmsUserDAO spmsUserDAO;
	@Autowired
	private SpmsProductDAO spmsProductDAO;
	@Autowired
	private SpmsProductMFeeDAO spmsProductMFeeDAO;
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private SpmsUserProductBindingDAO spmsUserProductBindingDAO;
	
	@Override
	public List<SpmsAccountBill> findAllByUser(String userId) {
		return spmsAccountBillDao.findAllByUser(userId);
	}

	@Override
	public SpmsAccointBillDTO getInfo(String id) {
		SpmsAccointBillDTO sabd = new SpmsAccointBillDTO();
		SpmsAccountBill spmsAccointBill = spmsAccountBillDao.findOne(id);
		BeanUtils.copyProperties(spmsAccointBill, sabd);
		SpmsUserDTO spmsUserDTO = new SpmsUserDTO();
		BeanUtils.copyProperties(spmsAccointBill.getSpmsUser(), spmsUserDTO);
		sabd.setSpmsUserDTO(spmsUserDTO);
		sabd.setSpmdList(spmsProductMFeeManager.findAllByUser((spmsAccointBill
				.getSpmsUser().getId()),sabd.getMonth(),sabd.getYear()));
		return sabd;
	}
	
	@Transactional(readOnly=false)
	@Override
	public boolean initAccountBill() {
		List<String> spmsUserList = spmsUserDAO.findAllUser();
		try {
			if(spmsUserList!=null&&spmsUserList.size()>0){
				int year = DateUtil.getNowYear();
				int month = DateUtil.getNowMonth()-1;
				SpmsAccountBill sab =null;
				for(String userId : spmsUserList){
					//计算用电费用(用户下所有产品的用电费用)
					Map<String, Double> ydlMap = countYDL(userId, year, month);
					//账单总费用(后期可能将用电费用统计进去)
					double countMonthRMB = 0;//TODO
					SpmsProductMFee spmf = null;
					List<SpmsProduct> spmsProductList = spmsProductDAO.getSpmsProductByUserid(userId);
					
					boolean isJanFlag = false;
					if(spmsProductList!=null&&spmsProductList.size()>0){
						for(SpmsProduct sp : spmsProductList){
							isJanFlag = isJan(sp.getSubscribeDate());
							if(isJanFlag){
								spmf = new SpmsProductMFee();
								spmf.setProductId(sp);
								spmf.setElecFee(null);//TODO
								String t = "";
								if(sp.getSpmsProductType() != null) {
									t = sp.getSpmsProductType().getValidPeriod();//产品周期
								}
								
								//设置用电费用
								Double elecFee = null;
								elecFee = ydlMap.get(sp.getId());
								spmf.setElecFee(elecFee);
								
								int cycle = 0 ;
//								if("1".equals(t)){
//									cycle = 4; 
//								}else if ("2".equals(t)){
//									cycle = 6; 
//								}else{
//									cycle = 12; 
//								}
								//修改产品周期
								cycle = Integer.parseInt(t);
								
								double productMFee = 0.0;
								if(sp.getSpmsProductType() != null) {
									productMFee = sp.getSpmsProductType().getCountRmb() / cycle;
								}
								spmf.setYear(year);
								spmf.setMonth(month);
								countMonthRMB+=productMFee;
								spmf.setProductFee(productMFee);
								spmf.setSpmsUser(sp.getSpmsUser());
								//spmfList.add(spmf);
								
								spmsProductMFeeDAO.saveAndFlush(spmf);
							}
						}
//						if(isJanFlag){
//							sab = new SpmsAccountBill();
//							sab.setBillDate(new Date(new Date().getTime()-1000*3600*24*9));
//							sab.setCountMonthRMB(countMonthRMB);
//							sab.setBillCycle(DateUtil.getMonthBothEnds(DateUtil.chinaSdf,new Date()));
//							SpmsUser su = spmsUserDAO.findOne(userId);
//							su.setBalance(su.getBalance()-countMonthRMB);
//							sab.setSpmsUser(su);
//							sab.setYear(year);
//							sab.setMonth(month);
//							sab.setAccountMonthRest(su.getBalance());
//							sab.setMonthCredit(su.getCredit());
//							spmsAccountBillDao.saveAndFlush(sab);
//							/*账户金额费用扣除*/
//							spmsUserDAO.saveAndFlush(su);
//						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 是否为1月
	 * @param date
	 * @return
	 */
	public  boolean isJan(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(date!=null){
			if("2015-01".equals(sdf.format(date))){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean executeAccountBill() {
		List<String> spmsUserList = spmsUserDAO.findAllUser();
		try {
			if (spmsUserList != null && spmsUserList.size() > 0) {
				int year = DateUtil.getNowYear();
				int month = DateUtil.getNowMonth() - 1;
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -1);
				
				SpmsAccountBill sab = null;
				a:for (String userId : spmsUserList) {
					try {
						//计算用电费用(用户下所有产品的用电费用)
						Map<String, Double> ydlMap = countYDL(userId, year, month);
						// 账单总费用(后期可能将用电费用统计进去)
						double countMonthRMB = 0;// TODO
						SpmsProductMFee spmf = null;
						List<SpmsProduct> spmsProductList = spmsProductDAO
								.getSpmsProductByUserid(userId);
						if (spmsProductList != null && spmsProductList.size() > 0) {
							for (SpmsProduct sp : spmsProductList) {
								if(DateUtil.getLastMonthNewestDayDate().getTime()<sp.getExpireDate().getTime()
										&&sp.getExpireDate().getTime()<DateUtil.getLastMonthLastDayDate().getTime()){
									spmf = new SpmsProductMFee();
									spmf.setProductId(sp);
									
									//设置用电费用
									Double elecFee = null;
									elecFee = ydlMap.get(sp.getId());
									spmf.setElecFee(elecFee);
									
									String t = "";
									if (sp.getSpmsProductType() != null) {
										t = sp.getSpmsProductType().getValidPeriod();// 产品周期
									}
									int cycle = 0;
//									if ("1".equals(t)) {
//										cycle = 4;
//									} else if ("2".equals(t)) {
//										cycle = 6;
//									} else {
//										cycle = 12;
//									}
									//修改产品周期
									cycle = Integer.parseInt(t);
									
									double productMFee = 0.0;
									if (sp.getSpmsProductType() != null) {
										productMFee = sp.getSpmsProductType().getCountRmb()
												/ cycle;
									}
									spmf.setYear(year);
									spmf.setMonth(month);
									countMonthRMB += productMFee;
									spmf.setProductFee(productMFee);
									spmf.setSpmsUser(sp.getSpmsUser());
									// spmfList.add(spmf);

									spmsProductMFeeDAO.saveAndFlush(spmf);
								}
							}
						}
						sab = new SpmsAccountBill();
						sab.setBillDate(new Date());
						sab.setCountMonthRMB(countMonthRMB);
						
						c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
						SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
						String t1 = sdf.format(c.getTime());
						c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
						String t2 = sdf.format(c.getTime());
						
						sab.setBillCycle(t1 + "-" + t2);
						SpmsUser su = spmsUserDAO.findOne(userId);
//						su.setBalance((su.getBalance() == null ?0:su.getBalance()) - countMonthRMB);
//						sab.setSpmsUser(su);
//						sab.setYear(year);
//						sab.setMonth(month);
//						sab.setAccountMonthRest(su.getBalance());
//						sab.setMonthCredit(su.getCredit());
						sab = spmsAccountBillDao.saveAndFlush(sab);
						/* 账户金额费用扣除 */
						spmsUserDAO.saveAndFlush(su);
						
						/* 发送账单到用户的邮箱 */
						sab.setSendMsgFlag(sendMail(sab));
						spmsAccountBillDao.saveAndFlush(sab);
					} catch (Exception e) {
						e.printStackTrace();
						continue a;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 计算用户产品用电量
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	private Map<String, Double> countYDL(String userId, int year, int month){
		Map<String, Double> map = new HashMap<String, Double>();
		//获取用户产品绑定
//		List<SpmsUserProductBinding> list = spmsUserProductBindingDAO.getListByUserId(userId);
//		for (SpmsUserProductBinding supb : list) {
//			//为每个设备计算出用电量
//			//String deviceId = supb.getSpmsDevice().getId();
//			
//			String deviceId = "";
//			
//			Double ydl = null;
//			//获取设备用电量
//			String sql = "SELECT t.accumulatePower FROM spms_ac_status_year t WHERE t.device_id = '" + deviceId + "' and t.date = '" + year + "-" + month + "' ";
//			List<Object> tmpList = queryDAO.getObjects(sql);
//			if(null != tmpList && tmpList.size() > 0 && null != tmpList.get(0)){
//				ydl = ((BigInteger)tmpList.get(0)).doubleValue();
//			}
//			//通过用电量、用电成本，计算用电费用
//			if(null != ydl){
//				if(null != map.get(supb.getSpmsProduct().getId())){
//					map.put(supb.getSpmsProduct().getId(), map.get(supb.getSpmsProduct().getId() + (ydl * supb.getSpmsProductType().getElectrovalenceRmb())));
//				}else{
//					map.put(supb.getSpmsProduct().getId(), ydl * supb.getSpmsProductType().getElectrovalenceRmb());
//				}
//			}
//		}
		return map;
	}

	/**
	 * 发送账单到用户邮箱
	 * @param sab
	 * @return
	 */
	private int sendMail(SpmsAccountBill sab) {
		try {
			//开始处理图表
			Map<String, Object> chart1 = new HashMap<String, Object>();
			chart1 = createChart1(sab.getId(),chart1);
			Map<String, Object> chart2 = new HashMap<String, Object>();
			chart2 = createChart2(sab.getId(),chart2);
			byte[] jfreechart1 = createJFreeChart1(chart1);
			byte[] jfreechart2 = createJFreeChart2(chart2);

			List<byte[]> list = new ArrayList<byte[]>();
			list.add(jfreechart1);
			list.add(jfreechart2);
			
			String html = createHTML(sab);
			
			SpmsUser ue = sab.getSpmsUser();
			MailBean mb = new MailBean();
			mb.setTo(ue.getEmail());
			System.out.println(ue.getEmail());
			mb.setFrom(PropertyUtil.getPropertyInfo("sendemail.from"));
			mb.setUsername(PropertyUtil.getPropertyInfo("sendemail.user"));
			mb.setPassword(PropertyUtil.getPropertyInfo("sendemail.password"));
			mb.setHost(PropertyUtil.getPropertyInfo("sendemail.smtp"));
			mb.setSubject("智能用电系统客户账单");
			mb.setContent(html);
//			if(MailUtils.sendMailWithPic(mb, list)){
//				return 1;
//			}else{
//				return 0;
//			}
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 拼接网页
	 * @param sab
	 * @return
	 */
	private String createHTML(SpmsAccountBill sab) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html> ");
		sb.append("<head> ");
		sb.append("<style> ");
		sb.append("td{ ");
		sb.append("width:200px; ");
		sb.append("} ");
		sb.append("div{ ");
		sb.append("background-color:#F2F2F2; ");
		sb.append("} ");
		sb.append("</style> ");
		sb.append("</head> ");
		sb.append("<body> ");
		sb.append("<div style='background-color:#B4E1F7;text-align:center;padding:10px 0px 10px 0px;'> ");
		sb.append("<font size='18px;'>智能用电系统客户账单</font> ");
		sb.append("</div> ");
		sb.append("<div style='padding:20px 20px 20px 20px;'> ");
		sb.append("<table> ");
		sb.append("<tr> ");
		sb.append("<td nowrap>姓名</td> ");
		sb.append("<td nowrap>" + nullToString(sab.getSpmsUser().getFullname()) + "</td> ");
		sb.append("<td nowrap>手机号</td> ");
		sb.append("<td nowrap>" + nullToString(sab.getSpmsUser().getMobile()) + "</td> ");
		sb.append("</tr> ");
		sb.append("<tr> ");
		sb.append("<td nowrap>账单日</td> ");
		sb.append("<td nowrap>" + dateToString(sab.getBillDate()) + "</td> ");
		sb.append("<td nowrap>账单周期</td> ");
		sb.append("<td nowrap>" + nullToString(sab.getBillCycle()) + "</td> ");
		sb.append("</tr> ");
		sb.append("<tr> ");
		sb.append("<td nowrap>费用合计</td> ");
		sb.append("<td nowrap>" + doubleToString(sab.getCountMonthRMB()) + "</td> ");
		sb.append("<td nowrap>打印日期</td> ");
		sb.append("<td nowrap>" + dateToString(sab.getPrintDate()) + "</td> ");
		sb.append("</tr> ");
		sb.append("</table> ");
		sb.append("<table style='margin:30px 0 30px 0;'> ");
		List<SpmsProductMFeeDTO>  list = spmsProductMFeeManager.findAllByUser((sab.getSpmsUser().getId()),sab.getMonth(),sab.getYear());
		for (SpmsProductMFeeDTO supb : list) {
			sb.append("<tr>");
			sb.append("<td nowrap>" + nullToString(supb.getProdductTypeName()) + "</td> ");
			sb.append("<td nowrap>套餐费用</td> ");
			sb.append("<td nowrap>" + doubleToString(supb.getProductFee()) + "</td> ");
			sb.append("<td nowrap>用电费用</td> ");
			sb.append("<td nowrap>");
			sb.append(doubleToString(supb.getElecFee()));
			sb.append("</td> ");
			sb.append("</tr>");
		}
		sb.append("</table> ");
		sb.append("<hr/> ");
		sb.append("<table style='margin:30px 0 30px 0;'> ");
		sb.append("<tr>");
		sb.append("<td nowrap>账户余额</td> ");
		sb.append("<td nowrap>" + doubleToString(sab.getAccountMonthRest()) + "</td> ");
		sb.append("<td nowrap>应缴费用</td> ");
		sb.append("<td nowrap>");
		if(null != sab.getAccountMonthRest()){
			sb.append((sab.getAccountMonthRest()>=0?0:Math.abs(sab.getAccountMonthRest())));
		}else{
			sb.append("");
		}
		sb.append("</td> ");
		sb.append("<td nowrap>信用额度</td> ");
		sb.append("<td nowrap>" + doubleToString(sab.getMonthCredit()) + "</td> ");
		sb.append("</tr>");
		sb.append("</table> ");
		sb.append("<hr /> ");
		sb.append("<div> ");
		sb.append("<div style='width=100%'> ");
		sb.append("<img src='cid:pic0'/> ");
		sb.append("</div> ");
		sb.append("<div style='width=100%'> ");
		sb.append("<img src='cid:pic1'/> ");
		sb.append("</div> ");
		sb.append("</div> ");
		sb.append("<div style='clear:both;float:none'></div> ");
		sb.append("</div> ");
		sb.append("</body> ");
		sb.append("</html> ");
		return sb.toString();
	}

	private String dateToString(Date date){
		if(null != date){
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}else{
			return "";
		}
	}

	private String doubleToString(Double d){
		if(null != d){
			return new DecimalFormat("0").format(d);
		}else{
			return "";
		}
	}
	
	private String nullToString(String str){
		if(null != str){
			return str;
		}else{
			return "";
		}
	}
	
	/**
	 * 创建jfreechart柱状图
	 * @param chart1
	 * @return
	 * @throws IOException 
	 */
	private byte[] createJFreeChart1(Map<String, Object> chart1) throws IOException {
		Object[] x = (Object[]) chart1.get("xAxis");
		Long[] data = (Long[]) chart1.get("data");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < data.length; i++) {
			dataset.addValue(data[i], "", x[i].toString());
		}
		 //创建主题样式         
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");   
		//设置标题字体        
		standardChartTheme.setExtraLargeFont(new Font("宋体",Font.BOLD,20));         
		//设置图例的字体        
		standardChartTheme.setRegularFont(new Font("宋体",Font.PLAIN,15));         
		//设置轴向的字体         
		standardChartTheme.setLargeFont(new Font("宋体",Font.PLAIN,15));  
		standardChartTheme.setLegendBackgroundPaint(new Color(242, 242, 242));
		//应用主题样式        
		ChartFactory.setChartTheme(standardChartTheme);
		
		JFreeChart chart = ChartFactory.createBarChart3D("用电量信息统计图", 
                "日期",
                "用电量(千瓦时)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
		chart.setBackgroundPaint(new Color(242, 242, 242));
		
		BarRenderer3D renderer=(BarRenderer3D) chart.getCategoryPlot().getRenderer();
		//显示每个柱的数值，并修改该数值的字体属性
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		 //设置bar的最小宽度，以保证能显示数值
//		renderer.setMinimumBarLength(0.02);
		 //最大宽度
//		renderer.setMaximumBarWidth(0.07); 

		 //设置不能在柱子上正常显示的那些数值的显示方式，将这些数值显示在柱子外面
		ItemLabelPosition itemLabelPositionFallback=new ItemLabelPosition(
		          ItemLabelAnchor.OUTSIDE6,TextAnchor.BASELINE_LEFT,
		          TextAnchor.HALF_ASCENT_LEFT,-1.57D);
		renderer.setPositiveItemLabelPosition(itemLabelPositionFallback);
		renderer.setItemLabelsVisible(true); 
		renderer.setItemMargin(10);
		//设置颜色
		renderer.setSeriesPaint(0, new Color(47,126,216));
		
		chart.getCategoryPlot().setRenderer(renderer);
		
		((CategoryPlot) chart.getPlot()).getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45); 
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsPNG(bos, chart, 1045, 500);
		byte[] bytes = bos.toByteArray();
		bos.close();
		return bytes;
	}

	/**
	 * 创建jfreechart饼状图
	 * @param chart2
	 * @return
	 * @throws IOException 
	 */
	private byte[] createJFreeChart2(Map<String, Object> chart2) throws IOException {
		DefaultPieDataset  dataset = new DefaultPieDataset ();
		List<String> keys = (List<String>) chart2.get("keys");
		List<Long> values = (List<Long>) chart2.get("values");
		for (int i = 0; i < keys.size(); i++) {
			dataset.setValue(keys.get(i), values.get(i));
		}
		JFreeChart chart = ChartFactory.createPieChart("设备用电量占比图", dataset, true, true, false);  
		chart.setBackgroundPaint(new Color(242, 242, 242));
		PiePlot pieplot = (PiePlot) chart.getPlot();   
		pieplot.setLabelFont(new Font("宋体", 0, 11));   
		//设置饼图是圆的（true），还是椭圆的（false）；默认为true   
		pieplot.setCircular(true);   
		//没有数据的时候显示的内容   
		pieplot.setNoDataMessage("无数据显示");   
		pieplot.setLabelGap(0.02D);
		//设置百分比显示
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(                  
				  "设备：{0}， \r\n 用电：{1}千瓦时，\r\n 占比{2}",                  
				  NumberFormat.getNumberInstance(),                  
				  new DecimalFormat("0.00%")));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsPNG(bos, chart, 1045, 500);
		byte[] bytes = bos.toByteArray();
		bos.close();
		return bytes;
	}

	@Override
	public int sendAccountEmail(String spmsAccountEmailId) {
		int flag = 0;
		try {
			SpmsAccountBill sab = spmsAccountBillDao.findOne(spmsAccountEmailId);
			flag = sendMail(sab);
			sab.setSendMsgFlag(flag);
			spmsAccountBillDao.saveAndFlush(sab);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
	}

	@Override
	public Map<String, Object> createChart1(String id , Map<String, Object> map) {
		SpmsAccountBill sab = spmsAccountBillDao.findOne(id);
		SpmsUser su = sab.getSpmsUser();
		Map<String, Long> result = new HashMap<String, Long>();
		String findDeviceSql = "SELECT t.device_id FROM spms_user_product_binding t WHERE t.user_id = '" + su.getId() + "'";
		List<Object> devices = queryDAO.getObjects(findDeviceSql);
		for (Object device : devices) {
			StringBuffer sb = new StringBuffer();
//			sb.append(" SELECT  date_format(t.start_time,'%Y-%m-%d') time,(MAX(t.accumulatePower) - MIN(t.accumulatePower)) usedEle ");
//			sb.append(" FROM spms_ac_status t ");
//			sb.append(" WHERE t.device_id = '" + device.toString() + "' and date_format(t.start_time,'%Y-%c') = '" + sab.getYear() + "-" + sab.getMonth() + "' ");
//			sb.append(" GROUP BY  date_format(t.start_time,'%Y-%m-%d') ");
//			sb.append(" ORDER BY  date_format(t.start_time,'%Y-%m-%d') ");
			sb.append(" select t.date as time,t.accumulatePower as usedEle from spms_ac_status_year t where t.device_id = '" + device.toString() + "' and t.date = '" + sab.getYear() + "-" + (sab.getMonth()<10?"0"+sab.getMonth():sab.getMonth()) + "'");
			List<Map<String, Object>> tmpList = queryDAO.getMapObjects(sb.toString());
			for (Map<String, Object> tmpMap : tmpList) {
				if(null == result.get(tmpMap.get("time").toString())){
					result.put(tmpMap.get("time").toString(), ((BigInteger)tmpMap.get("usedEle")).longValue());
				}else{
					result.put(tmpMap.get("time").toString(), result.get(tmpMap.get("time").toString()) + ((BigInteger)tmpMap.get("usedEle")).longValue());
				}
			}
		}
		Object[] xAxis = result.keySet().toArray();
		Arrays.sort(xAxis, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		Long[] data = new Long[xAxis.length];
		for (int i = 0; i < xAxis.length; i++) {
			data[i] = result.get(xAxis[i]);
		}
		map.put("xAxis", xAxis);
		map.put("data", data);
		map.put("billCycle", sab.getBillCycle());
		return map;
	}

	@Override
	public Map<String, Object> createChart2(String id, Map<String, Object> map) {
		SpmsAccountBill sab = spmsAccountBillDao.findOne(id);
		SpmsUser su = sab.getSpmsUser();
		String findDeviceSql = "SELECT t.device_id as id,t1.sn as name FROM spms_user_product_binding t left join spms_device t1 on t.device_id = t1.id WHERE t.user_id = '" + su.getId() + "'";
		List<Map<String, Object>> devices = queryDAO.getMapObjects(findDeviceSql);
		List<String> keys = new ArrayList<String>();
		List<Long> values = new ArrayList<Long>();
		for (Map<String, Object> device : devices) {
			String did = device.get("id").toString();
			String name = device.get("name").toString();
//			String sql = "SELECT (MAX(t.accumulatePower) - MIN(t.accumulatePower)) FROM spms_ac_status t WHERE t.device_id = '" + did + "' and date_format(t.start_time,'%Y-%c') = '" + sab.getYear() + "-" + sab.getMonth() + "' ";
			String sql = "SELECT accumulatePower FROM spms_ac_status_year t WHERE t.device_id = '" + did + "' and t.date  = '" + sab.getYear() + "-" + (sab.getMonth()<10?"0"+sab.getMonth():sab.getMonth()) + "' ";
			List<Object> tmpList = queryDAO.getObjects(sql);
			if(null != tmpList && tmpList.size() > 0 && null != tmpList.get(0)){
				values.add(((BigInteger)tmpList.get(0)).longValue());
			}else{
				values.add(0L);
			}
			keys.add(name);
		}
		map.put("keys", keys);
		map.put("values", values);
		map.put("billCycle", sab.getBillCycle());
		return map;
	}
	
	
}
