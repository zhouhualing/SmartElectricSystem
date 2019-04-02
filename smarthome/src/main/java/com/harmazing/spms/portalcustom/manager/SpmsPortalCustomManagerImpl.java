package com.harmazing.spms.portalcustom.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.portalcustom.dao.SpmsPortalCustomDAO;
import com.harmazing.spms.portalcustom.entity.SpmsPortalCustom;
import com.harmazing.spms.portalcustom.manager.SpmsPortalCustomManager;
import com.harmazing.spms.portalmodules.dao.SpmsPortalModulesDAO;
import com.harmazing.spms.portalmodules.entity.SpmsPortalModules;
import com.harmazing.spms.potaldefault.dao.SpmsPortalDefaultDAO;
import com.harmazing.spms.potaldefault.entity.SpmsPortalDefault;

@Service
public class SpmsPortalCustomManagerImpl implements SpmsPortalCustomManager {
	@Autowired
	private SpmsPortalCustomDAO spmsPortalCustomDAO;
	@Autowired
	private SpmsPortalDefaultDAO spmsPortalDefaultDAO;
	@Autowired
	private SpmsPortalModulesDAO spmsPortalModulesDAO;

	/**
	 * 取得首页显示顺序
	 * 
	 * @param user
	 *            当前用户
	 */
	public Map<Integer, HashMap<String, String>> getSort(UserEntity user) {
		Map<Integer, HashMap<String, String>> result = new HashMap<Integer, HashMap<String, String>>();
		List<SpmsPortalCustom> cList = spmsPortalCustomDAO.getByUserId(user
				.getId());
		List<SpmsPortalCustom> cAllList = spmsPortalCustomDAO.getByUserIdAll(user
				.getId());
		if(cAllList == null || cAllList.size() == 0){
			beCustomSort(user);
		}
		if (cList == null || cList.size() == 0) {
			/*List<RoleEntity> rList = user.getRoleEntities();
			List<SpmsPortalModules> dList = new ArrayList<SpmsPortalModules>();
			for (int i = 0; i < rList.size(); i++) {
				List<SpmsPortalDefault> t = spmsPortalDefaultDAO
						.getByRoleCode(rList.get(i).getRoleCode());
				for (int j = 0; j < t.size(); j++) {
					SpmsPortalDefault spd = t.get(j);
					SpmsPortalModules spm = spd.getModules();
					if (!dList.contains(spm)) {
						dList.add(spm);
					}
				}
			}
			for (int i = 0; i < dList.size(); i++) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put(dList.get(i).getDivName(), dList.get(i).getDivTitle());
				result.put(i + 1, temp);
			}
*/
			return result;
		} else {
			for (int i = 0; i < cList.size(); i++) {
				HashMap<String, String> temp = new HashMap<String, String>();
				SpmsPortalCustom spc = cList.get(i);
				SpmsPortalModules spm = spc.getSpmsPortalModules();
				int sort = spc.getSort();
				temp.put(spm.getDivName(), spm.getDivTitle());
				result.put(sort, temp);
			}
			return result;
		}
	}

	/**
	 * 将已有portaldefault 转化成 自定义的
	 * 
	 * @param user
	 *            当前用户
	 */
	public void beCustomSort(UserEntity user) {
		List<RoleEntity> rList = user.getRoleEntities();
		List<SpmsPortalModules> dList = new ArrayList<SpmsPortalModules>();
		for (int i = 0; i < rList.size(); i++) {
			List<SpmsPortalDefault> t = spmsPortalDefaultDAO
					.getByRoleCode(rList.get(i).getRoleCode());
			for (int j = 0; j < t.size(); j++) {
				SpmsPortalDefault spd = t.get(j);
				SpmsPortalModules spm = spd.getModules();
				if (!dList.contains(spm)) {
					dList.add(spm);
				}
			}
		}
		for (int i = 0; i < dList.size(); i++) {
			SpmsPortalCustom spc = new SpmsPortalCustom();
			spc.setUser(user);
			spc.setSpmsPortalModules(dList.get(i));
			spc.setSort(i + 1);
			spc.setRolecode(user.getRoleEntities().get(0).getRoleCode());
			spc.setIsdisPlay(0);
			spmsPortalCustomDAO.save(spc);
		}
	}

	/**
	 * 取得可以添加的列表
	 */
	@Override
	public Map<String, String> getCanAddSort(UserEntity user) {
		Map<String, String> result = new HashMap<String, String>();
		List<SpmsPortalCustom> cList = spmsPortalCustomDAO.getByUserId(user
				.getId());
		if (cList == null || cList.size() == 0) {
			//beCustomSort(user);
			cList = spmsPortalCustomDAO.getByUserId(user.getId());
		}
		List<RoleEntity> rList = user.getRoleEntities();
		List<SpmsPortalModules> dList = new ArrayList<SpmsPortalModules>(); // 所有可以显示的div
		for (int i = 0; i < rList.size(); i++) {
			List<SpmsPortalDefault> t = spmsPortalDefaultDAO
					.getByRoleCode(rList.get(i).getRoleCode());
			for (int j = 0; j < t.size(); j++) {
				SpmsPortalDefault spd = t.get(j);
				SpmsPortalModules spm = spd.getModules();
				if (!dList.contains(spm)) {
					dList.add(spm);
				}
			}
		}
		List<SpmsPortalModules> c = new ArrayList<SpmsPortalModules>(); // 用户所有显示的div
		for (int i = 0; i < cList.size(); i++) {
			c.add(cList.get(i).getSpmsPortalModules());
		}
		for (int i = 0; i < dList.size(); i++) {
			if (!c.contains(dList.get(i))) {// 如果不存在以显示的，却存在可显示的
				result.put(dList.get(i).getDivName(), dList.get(i)
						.getDivTitle());
			}
		}
		return result;
	}

	/**
	 * 按 divName为用户添加显示
	 */
	@Transactional
	public synchronized Map<String, Object> addSort(UserEntity user, String divName) {
		Map<String, Object> result = new HashMap<String, Object>();
		SpmsPortalModules spm = spmsPortalModulesDAO.getByDivname(divName);
		if (spm == null || spm.getId() == null) {
			result.put("msg", "没有找到对应的模块");
			result.put("succcess", false);
		} else {
			//SpmsPortalCustom spc = new SpmsPortalCustom();getByUserIdDivName
			SpmsPortalCustom spc = spmsPortalCustomDAO.getByUserIdDivName(user.getId(), divName);
			List<SpmsPortalCustom> cList = spmsPortalCustomDAO.getByUserId(user
					.getId());
			List<SpmsPortalCustom> cAllList = spmsPortalCustomDAO.getByUserIdAll(user
					.getId());
			if(cAllList == null || cAllList.size() == 0){
				beCustomSort(user);
			}
			if (cList == null || cList.size() == 0) {
				//beCustomSort(user);
				cList = spmsPortalCustomDAO.getByUserId(user.getId());
			}
			int maxSort = 0;
			for (int i = 0; i < cAllList.size(); i++) {
				if (maxSort < cAllList.get(i).getSort()) {
					maxSort = cAllList.get(i).getSort();
				}
			}
			if(spc == null){
				spc = new SpmsPortalCustom();
			}
			spc.setUser(user);
			spc.setSpmsPortalModules(spm);
			spc.setSort(maxSort + 1);
			spc.setIsdisPlay(1);
			spc.setRolecode(user.getRoleEntities().get(0).getRoleCode());
			spmsPortalCustomDAO.save(spc);

			result.put("msg", "添加成功");
			result.put("succcess", true);
		}
		return result;
	}

	@Override
	public Map<String, Object> deleteSort(UserEntity user, String divName) {
		Map<String, Object> result = new HashMap<String, Object>();
		SpmsPortalModules spm = spmsPortalModulesDAO.getByDivname(divName);

		if (spm == null || spm.getId() == null) {
			result.put("msg", "没有找到对应的模块");
			result.put("succcess", false);
		} else {
			SpmsPortalCustom spc = spmsPortalCustomDAO.getByUserIdDivName(
					user.getId(), divName);
			List<SpmsPortalCustom> cList = spmsPortalCustomDAO.getByUserId(user
					.getId());
			if (cList == null || cList.size() == 0) {
				beCustomSort(user);
				spc = spmsPortalCustomDAO.getByUserIdDivName(user.getId(),
						divName);
			}
			spc.setIsdisPlay(0);
			spmsPortalCustomDAO.save(spc);
			int sort = spc.getSort();
			for (int i = 0; i < cList.size(); i++) {
				if (cList.get(i).getSort() > sort) {
					cList.get(i).setSort(cList.get(i).getSort() - 1);
				}
			}
			result.put("msg", "移除成功");
			result.put("succcess", true);
		}
		return result;
	}

	/**
	 * 更改顺序
	 */
	@Override
	public Map<String, Object> changeSort(UserEntity user,
			Map<String, String> sort) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<SpmsPortalCustom> cList = spmsPortalCustomDAO
					.getByUserId(user.getId());
			if (cList == null || cList.size() == 0) {
				beCustomSort(user);
			}
			Set<String> key = sort.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				String k = (String) it.next();
				int s = Integer.parseInt(k); // 传来的序号
				String divname = sort.get(k);
				SpmsPortalCustom spc = spmsPortalCustomDAO.getByUserIdDivName(
						user.getId(), divname);

				spc.setSort(s);

				spmsPortalCustomDAO.save(spc);
			}

		} catch (Exception e) {
			result.put("msg", "拖拽失败");
			result.put("succcess", false);
			return result;
		}
		result.put("msg", "排序成功");
		result.put("succcess", true);
		return result;
	}
}
