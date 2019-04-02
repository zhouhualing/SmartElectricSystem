package com.harmazing.spms.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.desktop.dao.SpmsAccountBillDAO;
import com.harmazing.spms.desktop.dao.SpmsProductMFeeDAO;
import com.harmazing.spms.desktop.entity.SpmsAccountBill;
import com.harmazing.spms.desktop.entity.SpmsProductMFee;
import com.harmazing.spms.desktop.manager.SpmsAccountBillManagerImpl;
import com.harmazing.spms.product.dao.SpmsProductDAO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.user.entity.SpmsUser;

/**
 * 账单定时任务
 * 
 * @author Administrator
 * 
 */
@Component("accountBillTask")
@Lazy(false)
public class AccountBillTask {
	private SpmsAccountBillManagerImpl sabm = (SpmsAccountBillManagerImpl) SpringUtil
			.getBeanByName("spmsAccountBillManagerImpl");
	@Scheduled(cron="0 0 3 1 * ?")
//	@Scheduled(cron="0 37 15 * * ?")
	public void executeAccountBill() {
		if(sabm.executeAccountBill()){
			System.out.println("成功生成账单！");
		}else{
			System.out.println("生成账单失败！");
		}
	}

	// @Scheduled(cron = "0 19 22 * * ?")
	// @Scheduled(fixedDelay = 2000)
	public void initAccountBill() {
		if(sabm.initAccountBill()){
			System.out.println("成功生成账单！");
		}else{
			System.out.println("生成账单失败！");
		}
	}

}