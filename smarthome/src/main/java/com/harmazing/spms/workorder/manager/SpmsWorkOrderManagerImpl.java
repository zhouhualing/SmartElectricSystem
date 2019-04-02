/**
 * 
 */
package com.harmazing.spms.workorder.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.area.dao.AreaDAO;
import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.manager.CodeGeneraterManager;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.CollectionUtil;
import com.harmazing.spms.base.util.DictUtil;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.product.dao.SpmsProductTypeDAO;
import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.workflow.util.WorkFlowUtil;
import com.harmazing.spms.workorder.dao.SpmsWorkOrderDAO;
import com.harmazing.spms.workorder.dto.SpmsWorkOrderDTO;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;
import com.harmazing.spms.workorder.manager.SpmsWorkOrderManager;

/**
 * describe:
 * @author yyx
 * since 2014年12月31日
 */
@Service("spmsWorkOrderManager")
public class SpmsWorkOrderManagerImpl implements SpmsWorkOrderManager {

	@Autowired
    private SpmsWorkOrderDAO spmsWorkOrderDAO;
    @Autowired
	private AreaDAO areaDAO;
	@Autowired
	private CodeGeneraterManager codeGeneraterManager;

    @Autowired
    private SpmsProductTypeDAO spmsProductTypeDAO;

    @Autowired
    private SpmsUserDAO spmsUserDAO;
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
	private QueryDAO queryDAO;	
    /* (non-Javadoc)
     * @see com.harmazing.spms.device.manager.SpmsDeviceManager#doSave(com.harmazing.spms.device.dto.SpmsDeviceDTO)
     */
    @Override
    @Transactional
    public SpmsWorkOrderDTO doSave(SpmsWorkOrderDTO spmsWorkOrderDTO) {
    	SpmsWorkOrder spmsWorkOrder = null;
        //mobile
        String mobile = spmsWorkOrderDTO.getSpmsUserMobile();
        String idNumber = spmsWorkOrderDTO.getIdNumber();
        String email = spmsWorkOrderDTO.getEmail();
        String meterNumber = spmsWorkOrderDTO.getMeterNumber();
        String userId=spmsWorkOrderDTO.getUserId();
        if(!StringUtils.isBlank(spmsWorkOrderDTO.getId())) {
			spmsWorkOrder = spmsWorkOrderDAO.findOne(spmsWorkOrderDTO.getId());
		} else {
			spmsWorkOrder = new SpmsWorkOrder();
			if(userId!=null&&userId!=""){
		    	   spmsWorkOrder.setUserId(userId);
		       }
		}
        //校验工单是否有校验信息
        SearchFilter searchFilter = new SearchFilter("spmsUserMobile",mobile);
        if(null != idNumber && !"".equals(idNumber)){
        	searchFilter.appendOrSearchFilter(new SearchFilter("idNumber",idNumber));
        }
        if(null != email && !"".equals(email)){
        	searchFilter.appendOrSearchFilter(new SearchFilter("email",email));
        }
        if(null != meterNumber && !"".equals(meterNumber)){
        	searchFilter.appendOrSearchFilter(new SearchFilter("meterNumber",meterNumber));
        }
        List <String> userMobileL = Lists.newArrayList();
        List <String> idNumberL = Lists.newArrayList();
        List <String> emailL = Lists.newArrayList();
        List <String> meterNumberL = Lists.newArrayList();

        List <SpmsWorkOrder> spmsWorkOrders = spmsWorkOrderDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilter,SpmsWorkOrder.class));
        Boolean flag = true;
        if((spmsWorkOrders != null)&& (spmsWorkOrders.size()>0)) {
            flag = false;
            //有唯一性校验报错
            userMobileL = CollectionUtil.extractToList(spmsWorkOrders,"spmsUserMobile");
            idNumberL = CollectionUtil.extractToList(spmsWorkOrders,"idNumber");
            emailL = CollectionUtil.extractToList(spmsWorkOrders,"email");
            meterNumberL = CollectionUtil.extractToList(spmsWorkOrders,"meterNumber");
        }
		if (spmsWorkOrderDTO.getType() == 1
				&& spmsWorkOrderDTO.getExist().equals("isExist")) {
			SpmsUser spmsUser = spmsUserDAO.getByMobile(mobile);
			StringBuilder stringBuilder = new StringBuilder();
			if (spmsUser != null) {
				if(spmsUser.getEmail()!=null && spmsUser.getEmail()!=""){
				if (!email.equals(spmsUser.getEmail())) {
					if(spmsUserDAO.getByEmail(email)!=null){
						flag=false;
						stringBuilder.append("邮箱：").append(email)
						.append(" 已注册。<br/>");
					}

				}
				}
				if(spmsUser.getIdNumber()!=null && spmsUser.getIdNumber()!=""){
				if (!idNumber.equals(spmsUser.getIdNumber())) {
					if(spmsUserDAO.getByIdNumber(idNumber)!=null){
						flag=false;
						stringBuilder.append("身份证：").append(idNumber)
						.append(" 已注册。<br/>");
					}
				}
				}
//				if(spmsUser.getAmmeter()!=null && spmsUser.getAmmeter()!=""){
//				if (!meterNumber.equals(spmsUser.getAmmeter())) {
//					if(spmsUserDAO.getByAmmeter(meterNumber)!=null){
//						flag=false;
//						stringBuilder.append("电表号：").append(meterNumber)
//						.append(" 已注册。");
//					}
//
//				}
//				}
				if (!flag) {
					if (userMobileL.contains(mobile)) {
						stringBuilder.append("手机号：").append(mobile)
								.append(" 已注册。<br/>");
					}
					if (idNumberL.contains(idNumber)) {
						stringBuilder.append("身份证：").append(idNumber)
								.append(" 已注册。<br/>");
					}
					if (emailL.contains(email)) {
						stringBuilder.append("邮箱：").append(email)
								.append(" 已注册。<br/>");
					}
					if (meterNumberL.contains(meterNumber)) {
						stringBuilder.append("电表号：").append(meterNumber)
								.append(" 已注册。");
					}
					Map<String, Object> map = Maps.newHashMap();
					map.put("errorMsg", stringBuilder.toString());
					spmsWorkOrderDTO.setMessage(map);
					spmsWorkOrderDTO.getWorkFlowDTO().setIsContinue(false);
					spmsWorkOrderDTO.getWorkFlowDTO().setMessage(map);
					return spmsWorkOrderDTO;
				}
			}
		}
       
        //判断是否是开户,开户校验以下信息
        if(spmsWorkOrderDTO.getType() == 1 && (!spmsWorkOrderDTO.getExist().equals("isExist"))){
        	//校验鼎湖是否有校验信息
            SearchFilter searchFilterU = new SearchFilter("mobile",mobile);
            if(null != idNumber && !"".equals(idNumber)){
            	searchFilterU.appendOrSearchFilter(new SearchFilter("idNumber",idNumber));
            }
            if(null != email && !"".equals(email)){
            	searchFilterU.appendOrSearchFilter(new SearchFilter("email",email));
            }
            if(null != meterNumber && !"".equals(meterNumber)){
            	searchFilterU.appendOrSearchFilter(new SearchFilter("ammeter",meterNumber));
            }
            List <SpmsUser> spmsUsers = spmsUserDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilterU,SpmsUser.class));
            if((spmsUsers != null)&& (spmsUsers.size()>0)) {
                flag = false;
                //有唯一性校验报错
                userMobileL.addAll(CollectionUtil.extractToList(spmsUsers,"mobile"));
                idNumberL.addAll(CollectionUtil.extractToList(spmsUsers,"idNumber"));
                emailL.addAll(CollectionUtil.extractToList(spmsUsers, "email"));
                meterNumberL.addAll(CollectionUtil.extractToList(spmsUsers, "ammeter"));
            }

            if(!flag) {
                StringBuilder stringBuilder = new StringBuilder();
                if(userMobileL.contains(mobile)) {
                    stringBuilder.append("手机号：").append(mobile).append(" 已注册。<br/>");
                }
                if(idNumberL.contains(idNumber)) {
                    stringBuilder.append("身份证：").append(idNumber).append(" 已注册。<br/>");
                }
                if (emailL.contains(email)) {
                    stringBuilder.append("邮箱：").append(email).append(" 已注册。<br/>");
                }
                if (meterNumberL.contains(meterNumber)) {
                    stringBuilder.append("电表号：").append(meterNumber).append(" 已注册。");
                }
                Map<String,Object> map = Maps.newHashMap();
                map.put("errorMsg", stringBuilder.toString());
                spmsWorkOrderDTO.setMessage(map);
                spmsWorkOrderDTO.getWorkFlowDTO().setIsContinue(false);
                spmsWorkOrderDTO.getWorkFlowDTO().setMessage(map);
                return spmsWorkOrderDTO;
            }
        }

		
		BeanUtils.copyProperties(spmsWorkOrderDTO, spmsWorkOrder);

		Area a = areaDAO.findOne(spmsWorkOrderDTO.getArea().getId());
		Area eleArea = areaDAO.findOne(spmsWorkOrderDTO.getEleArea().getId());
		UserEntity ue = UserUtil.getCurrentUser();
		spmsWorkOrder.setSupervisor(ue); // 供电管理员
		spmsWorkOrder.setOwner(ue);// 当前所有者
		spmsWorkOrder.setCreationdate(new java.util.Date());// 工单创建时间
		spmsWorkOrder.setActivitycount(0); // 工单内存在的活动数
		spmsWorkOrder.setActivityindex(0);// 现在activity 的 index
		spmsWorkOrder.setArea(a);
		spmsWorkOrder.setEleArea(eleArea);
		spmsWorkOrder.setStatus(WorkFlowUtil.STATUS_WORKORDER_PREHAND);

        //添加产品类型
		//增加判断 未选择产品类型的情况
		if(null != spmsWorkOrderDTO.getSpmsProductTypeDTO().getId() && !"".equals(spmsWorkOrderDTO.getSpmsProductTypeDTO().getId())){
			SpmsProductType spmsProductType = spmsProductTypeDAO.findOne(spmsWorkOrderDTO.getSpmsProductTypeDTO().getId());
			spmsWorkOrder.setSpmsProductType(spmsProductType);
		}
		//setCode
		spmsWorkOrder.setWorkOrderCode(codeGeneraterManager.getCode(1));
		
		//存储工单
		spmsWorkOrderDAO.save(spmsWorkOrder);
		
//		//判断用户是否存在
//		SearchFilter searchFilterU = new SearchFilter("mobile",spmsWorkOrder.getSpmsUserMobile());
//        List <SpmsUser> users = spmsUserDAO.findAll(DynamicSpecifications.bySearchFilter(searchFilterU,SpmsUser.class));
//        if((users == null || users.size() == 0)) {
//        	//存储用户tb_user_user
//    		UserEntity user = new UserEntity();
//    		user.setUserName(spmsWorkOrder.getSpmsUserName());
//    		user.setUserCode(spmsWorkOrder.getSpmsUserMobile());
//    		user.setMobilePhone(spmsWorkOrder.getSpmsUserMobile());
//    		user.setPhoneNumber(spmsWorkOrder.getSpmsUserMobile());
//    		user.setEmail(spmsWorkOrder.getEmail());
//    		user.setCreateDate(spmsWorkOrder.getCreationdate());
//    		user.setCreateUser(ue);
//    		user.setVersion(spmsWorkOrder.getVersion());
//    		user.setUserType(spmsWorkOrder.getUserType());
//    		user.setStatus(0);
//    		user.setPassword("123456");
//    		user = userDAO.save(user);
//    		
//    		//存储订户spms_user
//    		SpmsUser su = new SpmsUser();
//    		su.setUser(user);
//    		su.setId(user.getId());
//    		su.setAddress(spmsWorkOrder.getAddress());
//    		su.setAmmeter(spmsWorkOrder.getMeterNumber());
//    		su.setEmail(user.getEmail());
//    		su.setFullname(user.getUserName());
//    		su.setMobile(user.getMobilePhone());
//    		su.setType(user.getUserType());
//    		su.setBizArea(spmsWorkOrder.getArea());
//    		su.setEleArea(spmsWorkOrder.getEleArea());
//    		su.setIdNumber(spmsWorkOrder.getIdNumber());
//    		spmsUserDAO.save(su);
//        }
        
		spmsWorkOrderDTO.setId(spmsWorkOrder.getId());

		//start workflow
//		spmsWorkOrderDTO.getWorkFlowDTO().setWorkFlowTypeId(WorkFlowUtil.WF_TYPE_OPENSERVICE);
		spmsWorkOrderDTO.getWorkFlowDTO().setBusinessKey(spmsWorkOrder.getId().toString());
		spmsWorkOrderDTO.getWorkFlowDTO().setObject(spmsWorkOrder);
		return spmsWorkOrderDTO;
    }

	@Override
	public SpmsWorkOrderDTO getById(String id) {
		SpmsWorkOrderDTO swod = new SpmsWorkOrderDTO();
		SpmsWorkOrder swo = spmsWorkOrderDAO.findOne(id);
		BeanUtils.copyProperties(swo, swod);
		AreaDTO ad = new AreaDTO();
		AreaDTO eleArea = new AreaDTO();
		BeanUtils.copyProperties(swo.getArea(), ad);
		BeanUtils.copyProperties(swo.getEleArea(), eleArea);
		swod.setArea(ad);
        swod.setEleArea(eleArea);
		swod.setCreateUserName(swo.getCreateUser().getUserName());
		swod.setCreateUserMobile(swo.getCreateUser().getMobile());
		swod.setSpmsUserName(swod.getSpmsUserName());
		swod.setSpmsUserMobile(swod.getSpmsUserMobile());
        SpmsProductTypeDTO spmsProductTypeDTO = new SpmsProductTypeDTO();
        //增加判断，如果为选择产品类型
        if(null != swo.getSpmsProductType()){
        	spmsProductTypeDTO.setId(swo.getSpmsProductType().getId());
        	spmsProductTypeDTO.setNames(swo.getSpmsProductType().getNames());
        	spmsProductTypeDTO.setKongTiaoCount(swo.getSpmsProductType().getKongTiaoCount());
        	spmsProductTypeDTO.setChuangGanCount(swo.getSpmsProductType().getChuangGanCount());
        }
        swod.setSpmsProductTypeDTO(spmsProductTypeDTO);
		swod.setTypeText(DictUtil.getDictValue("workorder_type", swod.getType().toString()));
		return swod;
	}
	
	@Override
	public Map<String, Object> doDelete(String id) {
		Map<String , Object> result = new HashMap<String, Object>();
		
		SpmsWorkOrder swo = spmsWorkOrderDAO.findOne(id);
		
		if(swo == null || swo.getId() == null){
			result.put("success", false);
			result.put("msg", "没有找到对应工单");
			return result;
		}
		
		try{
			spmsWorkOrderDAO.delete(swo);
		}catch(Exception e){
			result.put("success", false);
			result.put("msg", "删除失败");
			return result;
		}
		result.put("success", true);
		result.put("msg", "删除成功");
		return result;

	}

	@Override
	public Map<String, Object> batchDelete(String ids) {
		Map<String , Object> result = new HashMap<String, Object>();
		String[] id = ids.split(",");
		for(int i = 0 ; i < id.length ;i++){
			SpmsWorkOrder swo = spmsWorkOrderDAO.findOne(id[i]);
			try{
				spmsWorkOrderDAO.delete(swo);
			}catch(Exception e){
				result.put("success", false);
				result.put("msg", "删除失败");
				return result;
			}
		}
		result.put("success", true);
		result.put("msg", "删除成功");
		return result;
	}

	@Override
	public List<SpmsWorkOrder> findBySpe(Specification specification) {
		return spmsWorkOrderDAO.findAll(specification);
	}

    @Transactional
    @Override
    public SpmsWorkOrderDTO doModifyDelete(SpmsWorkOrderDTO spmsWorkOrderDTO) {
        SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(spmsWorkOrderDTO.getId());
        if(spmsWorkOrder.getStatus() == WorkFlowUtil.STATUS_WORKORDER_COMPLETED) {
            Map <String,Object> msg = Maps.newHashMap();
            msg.put("errorInfo","该工单状态为已完成，不可删除。");
            spmsWorkOrderDTO.setMessage(msg);
            return spmsWorkOrderDTO;
        }
        spmsWorkOrder.setStatus(WorkFlowUtil.STATUS_WORKORDER_VOID);
        //对业务开通的订单做特殊处理
        if(spmsWorkOrder.getType() == 1){
        	// TODO
        }
        spmsWorkOrderDAO.save(spmsWorkOrder);
        return spmsWorkOrderDTO;
    }
    public Map<String,Object> validateUser(String processInstanceId){
    	Map<String, Object> map = new HashMap<String, Object>();
		String sql="select spmsUserMobile from spms_workorder where processInstanceId = '"+processInstanceId+"'";
		List<Object> list = queryDAO.getObjects(sql);
		if(list!=null&&list.size()>0){
		String spmsUserMobile =	list.get(0).toString();
		SpmsUser su = spmsUserDAO.getByMobile(spmsUserMobile);
		if(su != null && su.getUser() != null && su.getUser().getStatus() != null && su.getUser().getStatus() == 0){
			map.put("isValidate", true);
		}else if(su != null && su.getUser() != null && su.getUser().getStatus() != null && su.getUser().getStatus() != 0){
			map.put("isValidate", false);
		}else{
			map.put("isValidate", "true");
		}
		}else{
			map.put("isValidate", false);
		}
		
		return map;
	
    }
}
