package com.harmazing.spms.area.manager;

import java.io.IOException;
import java.util.*;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.base.manager.QueryManager;
import com.harmazing.spms.base.manager.QueryManagerImpl;
import com.harmazing.spms.base.util.*;
import com.harmazing.spms.common.entity.IEntity;
import com.harmazing.spms.device.dao.SpmsWarningSettingDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.harmazing.spms.area.dao.AreaDAO;
import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.area.dto.AreaTreeDTO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.area.manager.AreaManager;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.SearchFilter.Operator;


@Service("areaManager")
public class AreaManagerImpl implements AreaManager{

	@Autowired
	private AreaDAO areaDAO;
	@Autowired
	private QueryDAO queryDAO;	
	@Override
	public AreaDTO doSave(AreaDTO areaDTO) {
		Area area =null;
		if(areaDTO.getId()!=null && !areaDTO.getId().equals("")  && !StringUtil.isNUll(areaDTO.getId())){
			area = areaDAO.findOne(areaDTO.getId());
			areaDTO.setSort(area.getSort());
			BeanUtils.copyProperties(areaDTO, area);
		} else{
			area = new Area();
			BeanUtils.copyProperties(areaDTO, area);
			area.setSort(getMaxSort()+1);
		}
		
		if(areaDTO.getParent()!=null && areaDTO.getParent().getId()!=null && !StringUtil.isNUll(areaDTO.getParent().getId())){
			Area areaP = areaDAO.findOne(areaDTO.getParent().getId());
			area.setParent(areaP);
			areaDAO.save(area);
//			area.setParentIds(areaP.getParentIds()==null?areaP.getParentIds():"0,1"+","+areaP.getId().toString()+","+area.getId()+",");
			area.setParentIds(areaP.getParentIds()==null?"0,1,":areaP.getParentIds()+areaP.getId().toString()+","+area.getId()+",");
		}else{
			if(null != area.getId()){
				area.setParentIds("0,1"+","+area.getId()+",");
			}else{
				area.setParentIds("0,1"+",");
			}
		}
		areaDAO.save(area);
		BeanUtils.copyProperties(areaDTO, area);
		areaDTO.setId(area.getId());
		areaDTO.setSort(area.getSort());
		return areaDTO;
	}
	@Override
	public Area findById(String id) {
		return areaDAO.findOne(id);
	}
	@Override
	public Integer hasName(String myParent, String myName) {
		return areaDAO.hasName(myParent, myName).size();
	}
	@Override
	public List<AreaTreeDTO> findFirstArea(AreaTreeDTO areaTreeDTO) {
		List <SearchFilter> sfs = Lists.newArrayList();
		sfs.add(new SearchFilter("parent.id",Operator.ISNULL,"null"));
		sfs.add(new SearchFilter("classification",Operator.EQ,areaTreeDTO.getClassification()));
		List <Area> areaL = areaDAO.findAll(DynamicSpecifications.bySearchFilter(sfs, Area.class));
		List<AreaTreeDTO> areaTreeDTOs = Lists.newArrayList();
		for(Area area : areaL) {
			AreaTreeDTO tempAreaDTO = new AreaTreeDTO();
			tempAreaDTO.setName(area.getName());
			tempAreaDTO.setId(area.getId());
			tempAreaDTO.setIsParent(true);
			tempAreaDTO.setType(area.getType());
			tempAreaDTO.setClassification(area.getClassification());
			tempAreaDTO.setParentIds(area.getParentIds());
			tempAreaDTO.setOpen(true);
			areaTreeDTOs.add(tempAreaDTO);
		}
		return areaTreeDTOs;
	}
	@Override
	public List<AreaTreeDTO> findChildrenByParent(AreaTreeDTO areaTreeDTO) {
		List <SearchFilter> sfs = Lists.newArrayList();
		sfs.add(new SearchFilter("parent.id",Operator.EQ,areaTreeDTO.getId()));
		sfs.add(new SearchFilter("classification",Operator.EQ,areaTreeDTO.getClassification()));
		List <Area> areaL = areaDAO.findAll(DynamicSpecifications.bySearchFilter(sfs, Area.class));
		List<AreaTreeDTO> areaTreeDTOs = Lists.newArrayList();
		for(Area area: areaL){
			AreaTreeDTO tempAreaDTO = new AreaTreeDTO();
			tempAreaDTO.setType(area.getType());
			tempAreaDTO.setName(area.getName());
			tempAreaDTO.setId(area.getId());
			if (area.getChildList() != null && area.getChildList().size() > 0) {
				tempAreaDTO.setIsParent(true);
			}
			tempAreaDTO.setClassification(area.getClassification());
			tempAreaDTO.setParentIds(area.getParentIds());
			areaTreeDTOs.add(tempAreaDTO);
		}
		return areaTreeDTOs;
	}
	@Override
	public AreaDTO getById(String id) {
		Area area = areaDAO.findOne(id);
		
		AreaDTO ad = new AreaDTO();
		BeanUtils.copyProperties(area, ad);
		
		if(area.getParent()!=null && area.getParent().getId()!=null){
			Area areaP = area.getParent();
			AreaDTO adp = new AreaDTO();
			BeanUtils.copyProperties(areaP, adp);
			ad.setParent(adp);
			
		}
		return ad;
	}
	
	@Override
	@Transactional
	public Map<String, Object> doDelete(String id) {
		Map<String , Object> result = new HashMap<String, Object>();
		Area area = areaDAO.findOne(id);
		if(area == null || area.getId() == null){
			result.put("success", false);
			result.put("msg", "没有找到对应区域");
			return result;
		}
		try{
			String ids=null;
			StringBuilder sb = new StringBuilder();
			sb.append("'"+area.getId()+"'").append(",");
			List <SearchFilter> sfs = Lists.newArrayList();
			sfs.add(new SearchFilter("parent.id",Operator.EQ,id));
			List <Area> areaL = areaDAO.findAll(DynamicSpecifications.bySearchFilter(sfs, Area.class));
			if(areaL!=null&&areaL.size()>0){
				for (Area area2 : areaL) {
					sb.append("'"+area2.getId()+"'").append(",");
				}
			}
			ids=sb.toString().substring(0,sb.toString().length()-1);
			String sql="select * from spms_user where ele_area_id in ("+ids+") or biz_area_id in ("+ids+")";
			List<Object> list = queryDAO.getObjects(sql);
			if(list.size()>0){
				result.put("success", false);
				result.put("msg", "对应区域或子区域存在用户不能删除!");
				return result;
			}
			String sql2="select * from  spms_product_type where area_id in ("+ids+")";
			List<Object> listType = queryDAO.getObjects(sql2);
			if(listType.size()>0){
				result.put("success", false);
				result.put("msg", "对应区域或子区域存在产品类型不能删除!");
				return result;
			}
			String sql1="select *  from spms_workorder where service_id in ("+ids+") or ele_id in ("+ids+")";
			List<Object> listOrder = queryDAO.getObjects(sql1);
			if(listOrder.size()>0){
				result.put("success", false);
				result.put("msg", "对应区域或子区域存在工单不能删除!");
				return result;
			}
			String sql3="delete from spms_warning_setting where area_id in ("+ids+") ";
			queryDAO.doExecuteSql(sql3);
			//areaDAO.deleteAllChild(area.getId());			
			areaDAO.deleteAllChildren(","+area.getId()+"," , area.getId());			
			//areaDAO.delete(area);
		}catch(Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "删除失败");
			return result;
		}
		result.put("success", true);
		result.put("msg", "删除成功");
		return result;
	}
	
	public List<Map<String,Object>> findAllAreaCurrentElectro(){
		List<Map<String,Object>> result = Lists.newArrayList();
		
//		String sql = "select areaid,case when sum(power) is null then 0 else sum(power) end as power from (select DISTINCT sa.id as areaid,surb.device_id as id from sys_area sa,spms_user su,spms_user_rule_binding surb where (instr(sa.parent_ids,',11,') > 0 or sa.id = '11') and su.ele_area_id = sa.id and surb.user_id = su.id and surb.device_type = 2) decount left join spms_device sd on sd.id = decount.id and sd.on_off = 1 and sd.oper_status=1 and sd.status=2 group by areaid";
//		List list = jdbcTemplate.queryForList(sql);
//
//		for(int i=0 ;i<list.size();i++){
//			Map<String,Object> data = Maps.newHashMap();
//			Map map = (Map)list.get(i);
//			String areaId = map.get("areaid").toString();
//			Float power = Float.parseFloat(map.get("power").toString());
//			data.put("areaId", areaId);
//			data.put("power", power);
//			result.add(data);
//		}
		return result;
	}

	public static void main (String [] args) {
		
	}
	@Override
	public Integer getMaxSort() {
		String sql ="SELECT MAX(sort) FROM spms_area";
		int max_sort = 0;
		if(queryDAO.getObjects(sql).get(0) != null) {
			max_sort = (int)queryDAO.getObjects(sql).get(0);
		}

		return max_sort;
	}

	@Override
	public QueryTranDTO getAreaTree(QueryTranDTO queryTranDTO) throws IOException {
		List<Map <String,Object>> infos = Lists.newArrayList();
		QueryManager queryManager = SpringUtil.getBeanByName("queryManager");
		if(queryTranDTO.getSearchFilters().size() >1) {
			for(IEntity iEntity : queryTranDTO.getPage().getContent()) {
				Area area = (Area)iEntity;
				Map map = Maps.newHashMap();
				map.put("id",area.getId());
				map.put("code",area.getCode());
				map.put("name",area.getName());
				map.put("remarks",area.getRemarks());
				map.put("classification",area.getClassification());
				map.put("type", area.getType());
				map.put("parentId",area.getParent()==null?null:area.getParent().getId());
				infos.add(map);
			}
		} else {
			((QueryManagerImpl)queryManager).generateSearchFilter(queryTranDTO);
			List <Area> areas = areaDAO.findAll(DynamicSpecifications.bySearchFilter(queryTranDTO.getSearchFilters(),Area.class),new Sort(Sort.Direction.ASC, "sort"));
			Set <Area> parentAreas = Sets.newHashSet();
			List <Area> result = Lists.newArrayList();
			Iterator <Area> iterator =  areas.iterator();
			//get root area
			while(iterator.hasNext()) {
				Area area = iterator.next();
				parentAreas.add(area.getParent());
			}
			if(areas.size() == 0) {
			} else {
				//对根节点进行判断
				List<Integer> list = new ArrayList<Integer>();
				for (int i = 0; i < areas.size(); i++) {
					if( null == areas.get(i).getParent()){
						list.add(i);
					}
				}
				for (Integer i : list) {
					getTheSortedArea(areas,parentAreas,areas.get(i),result);
				}
				
				for(Area area : result) {
					Map map = Maps.newHashMap();
					map.put("id",area.getId());
					map.put("code",area.getCode());
					map.put("name",area.getName());
					map.put("remarks",area.getRemarks());
					map.put("type", area.getType());
					map.put("classification",area.getClassification());
					map.put("parentId",area.getParent()==null?null:area.getParent().getId());
					infos.add(map);
				}
			}
		}
		queryTranDTO.setCustomDatas(infos);
		queryTranDTO.setShowColumnDTOs(queryTranDTO.getQueryDTO().getColumn());
		((QueryManagerImpl)queryManager).doCustomFilter(queryTranDTO);
		((QueryManagerImpl) queryManager).enhanceData(queryTranDTO);
		return queryTranDTO;
	}

	//根据传入的area找到对应的子区域
	private void getTheSortedArea(List<Area> allArea,Set <Area> parentArea,Area area,List <Area> result) {
		result.add(area);
		//是父区域的话再找子区域
		if(parentArea.contains(area)) {
			for (int i = allArea.indexOf(area) + 1; i < allArea.size(); i++) {
				if (allArea.get(i).getParent() != null && allArea.get(i).getParent().equals(area)) {
					getTheSortedArea(allArea, parentArea, allArea.get(i), result);
				}
			}
		}
	}
}
