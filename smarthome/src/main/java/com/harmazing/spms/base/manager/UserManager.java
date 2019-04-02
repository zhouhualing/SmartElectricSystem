/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dto.TreeDTO;
import com.harmazing.spms.base.util.*;
import com.harmazing.spms.common.manager.IManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.OrgDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dao.RoleDAO;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.OrgEntity;
import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.base.entity.UserEntity;

@Service("userManager")
public class UserManager implements IManager {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private OrgDAO orgDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private QueryDAO queryDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.harmazing.spms.base.manager.UserManager#getUserInfo(com.harmazing.
	 * spms.base.dto.UserDTO)
	 */
	
	public UserDTO getUserInfo(UserDTO userDTO) {
		UserEntity userEntity = userDAO.findOne(userDTO.getId());
		BeanUtils.copyProperties(userEntity, userDTO);
		List<RoleEntity> ll = userEntity.getRoleEntities();
		String ids = "";
		if (ll.size() > 0) {
			for (int i = 0; i < ll.size(); i++) {
				ids = ids + ll.get(i).getId() + ",";
			}
			ids = ids.substring(0, ids.length() - 1);
		}

		if (userEntity.getCompanyEntity() != null) {
			userDTO.setCompanyId(userEntity.getCompanyEntity().getId());
			userDTO.setCompanyName(userEntity.getCompanyEntity().getName());
		}
		if (userEntity.getOrgEntity() != null) {
			userDTO.setOrgId(userEntity.getOrgEntity().getId());
			userDTO.setOrgName(userEntity.getOrgEntity().getName());
		}
		userDTO.setUserTypeText(DictUtil.getDictValue("user_type", userEntity.getUserType().toString()));
		userDTO.setRoleIds(ids);
		return userDTO;
	}

	
	@Transactional
	public UserDTO getUserInfo(String mobile) {
		UserEntity ue = userDAO.getByMobile(mobile);
		UserDTO ud = new UserDTO();
		BeanUtils.copyProperties(ue, ud);
		return getUserInfo(ud);
	}

	
	@Transactional
	public UserDTO doEdit(UserDTO userDTO) {
		UserEntity userEntity = userDAO.findOne(userDTO.getId());
		if (!StringUtil.isNUll(userDTO.getUserName())) {
			userEntity.setUserName(userDTO.getUserName());
		}
		if (!StringUtil.isNUll(userDTO.getPassword())) {
			userEntity.setPassword(userDTO.getPassword());
		}
		if (!StringUtil.isNUll(userDTO.getNo())) {
			userEntity.setNo(userDTO.getNo());
		}
		if (!StringUtil.isNUll(userDTO.getEmail())) {
			userEntity.setEmail(userDTO.getEmail());
		}
		if (!StringUtil.isNUll(userDTO.getPhoneNumber())) {
			userEntity.setPhoneNumber(userDTO.getPhoneNumber());
		}
		if (!StringUtil.isNUll(userDTO.getMobile())) {
			userEntity.setMobile(userDTO.getMobile());
		}
		if (userDTO.getUserType() != null) {
			userEntity.setUserType(userDTO.getUserType());
		}
		if (!StringUtil.isNUll(userDTO.getMark())) {
			userEntity.setMark(userDTO.getMark());
		}
		if (!StringUtil.isNUll(userDTO.getCompanyId())) {
			OrgEntity companyEntity = orgDAO.findOne(userDTO.getCompanyId());
			userEntity.setCompanyEntity(companyEntity);
		}
		if (!StringUtil.isNUll(userDTO.getOrgId())) {
			OrgEntity orgEntity = orgDAO.findOne(userDTO.getOrgId());
			userEntity.setOrgEntity(orgEntity);
		}
		userDAO.save(userEntity);

		/* 角色赋值 */
		if (!StringUtil.isNUll(userDTO.getRoleIds())) {
			assignRoles(userEntity.getId(), userDTO.getRoleIds());
		}
		return userDTO;
	}

	
	@Transactional
	public UserDTO addUser(UserDTO userDTO) {
		SearchFilter searchFilter = new SearchFilter("userCode", userDTO.getUserCode());
		UserEntity userEntityDB = userDAO.findOne(DynamicSpecifications.bySearchFilter(searchFilter, UserEntity.class));
		if (userEntityDB != null) {
			Map<String, Object> infos = Maps.newHashMap();
			infos.put("errorInfo", "登录名已被占用。");
			userDTO.setMessage(infos);
			return userDTO;
		}
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDTO, userEntity);
		userEntity.setUserType(0);
		userEntity.setStatus(0);
		userDAO.save(userEntity);
		/* 角色赋值 */
		assignRoles(userEntity.getId(), userDTO.getRoleIds());
		return userDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.harmazing.spms.base.manager.UserManager#deleteUsers(java.util.List)
	 */
	
	@Transactional
	public boolean deleteUsers(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			if (ids.indexOf(',') > 0) {
				String[] idArra = ids.split(",");
				for (String id : idArra) {
					UserEntity ue = userDAO.findOne(id);
					if (ue != null) {
						ue.setStatus(2);
						userDAO.save(ue);
					}
				}
				return true;
			} else {
				UserEntity ue = userDAO.findOne(ids);
				if (ue != null) {
					ue.setStatus(2);
					userDAO.save(ue);
					return true;
				}
			}
		}
		return false;

	}

	
	@Transactional
	public boolean deleteUser(String id) {
		if (StringUtils.isNotBlank(id)) {
			UserEntity ue = userDAO.findOne(id);
			if (ue != null) {
				if (ue.getStatus() != null && ue.getStatus() == 0) {
					ue.setStatus(2);
				} else {
					ue.setStatus(0);
				}
				userDAO.save(ue);
				return true;
			}
		}
		return false;

	}

	
	public UserEntity getByUserCode(String usercode) {
		return userDAO.getByUserCode(usercode);
	}
	
	public UserEntity getByUserMobile(String mobile) {
		return userDAO.getByMobile(mobile);
	}

	
	public void saveChange(UserEntity ue) {
		userDAO.save(ue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.harmazing.spms.base.manager.UserManager#getCurrentUserInfo()
	 */
	
	public UserDTO getCurrentUserInfo() {
		UserEntity userEntity = UserUtil.getCurrentUser();
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDTO);
		return userDTO;
	}

	public List<TreeDTO> getUsersByUserCods(List<String> userCodes) {
		List<UserEntity> userEntities = userDAO.findAll(DynamicSpecifications
				.bySearchFilter(new SearchFilter("userCode", SearchFilter.Operator.IN, userCodes), UserEntity.class));
		List<TreeDTO> treeDTOs = Lists.newArrayList();
		for (UserEntity user : userEntities) {
			TreeDTO treeDTO = new TreeDTO();
			treeDTO.setName(user.getUserName());
			treeDTO.setId(user.getId());
			treeDTO.setCode(user.getUserCode());
			treeDTOs.add(treeDTO);
		}
		return treeDTOs;
	}

	
	public Map<String, Object> getAllRole() {
		Map<String, Object> result = Maps.newHashMap();
		List<RoleEntity> list = roleDAO.findAll();
		for (int i = 0; i < list.size(); i++) {
			RoleEntity re = list.get(i);
			result.put(re.getId(), re.getRoleName());
		}
		return result;
	}

	
	public Map<String, Object> assignRoles(String userid, String roleids) {
		Map<String, Object> result = Maps.newHashMap();
		String sql = "DELETE FROM user_role WHERE userentities_id = '" + userid + "'";
		queryDAO.doExecuteSql(sql);
		if (!StringUtil.isNUll(roleids)) {
			String[] temp = roleids.split(",");
			List<String> ids = Arrays.asList(temp);
			for (int i = 0; i < ids.size(); i++) {
				String rid = ids.get(i);
				sql = "INSERT INTO user_role (userentities_id,roleentities_id) VALUES ('" + userid + "','" + rid + "')";
				queryDAO.doExecuteSql(sql);
			}
			result.put("success", true);
			result.put("msg", "角色绑定成功");
		}
		result.put("success", true);
		result.put("msg", "没有选择角色");
		return result;
	}

}
