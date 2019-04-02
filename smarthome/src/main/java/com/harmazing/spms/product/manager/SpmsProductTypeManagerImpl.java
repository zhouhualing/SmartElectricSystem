package com.harmazing.spms.product.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.area.dao.AreaDAO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.DictUtil;
import com.harmazing.spms.product.dao.SpmsProductTypeDAO;
import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.product.manager.SpmsProductTypeManager;

@Service
public class SpmsProductTypeManagerImpl implements SpmsProductTypeManager {
		
	
	
		@Autowired
	 	private SpmsProductTypeDAO spmsProductTypeDAO;
		@Autowired
	 	private AreaDAO areaDAO;
		@Autowired
		private QueryDAO queryDAO;
	    
		SpmsProductType spmsProductType = null;
	    /* (non-Javadoc)
	     * @see com.harmazing.spms.device.manager.SpmsDeviceManager#doSave(com.harmazing.spms.device.dto.SpmsDeviceDTO)
	     */
	    @Override
	    @Transactional
	    public SpmsProductTypeDTO doSave(SpmsProductTypeDTO spmsProductTypeDTO) {
	    	
		if(StringUtils.isNotBlank(spmsProductTypeDTO.getId())) {
			spmsProductType = spmsProductTypeDAO.findOne(spmsProductTypeDTO.getId());
		} else {
			spmsProductType = new SpmsProductType();
		}
		Area area=areaDAO.findOne(spmsProductTypeDTO.getAreaId());
		spmsProductType.setArea(area);
		BeanUtils.copyProperties(spmsProductTypeDTO, spmsProductType);
		spmsProductType.setDeleteStauts(0);
		spmsProductTypeDAO.save(spmsProductType);
		
		return spmsProductTypeDTO;
	    	
	    	
	    }
	    @Transactional
		@Override
		public SpmsProductTypeDTO doUpdate(SpmsProductTypeDTO spmsProductTypeDTO) {
			spmsProductType = spmsProductTypeDAO.findOne(spmsProductTypeDTO.getId());
			BeanUtils.copyProperties(spmsProductTypeDTO, spmsProductType);
			Area area = new Area();
			area = areaDAO.findOne(spmsProductTypeDTO.getAreaDTO().getId());
			spmsProductType.setArea(area);
			
			spmsProductTypeDAO.save(spmsProductType);
			return spmsProductTypeDTO;
		}
	    @Transactional
		@Override
		public SpmsProductTypeDTO doQuery(String id) {
	    	SpmsProductTypeDTO spd = new SpmsProductTypeDTO();
			SpmsProductType sp = spmsProductTypeDAO.findOne(id);
			BeanUtils.copyProperties(sp, spd);
			spd.setMuluIdText(DictUtil.getDictValue("product_mulu", sp.getMuluId().toString()));
			spd.setTypeText(DictUtil.getDictValue("producttype_rmb", sp.getRmbType().toString()));
			spd.setConfigurationInformationText(DictUtil.getDictValue("producttype_config", sp.getConfigurationInformation().toString()));
			spd.setStatus(sp.getStatus());
			spd.setStatusText(DictUtil.getDictValue("producttype_status", sp.getStatus().toString()));
			spd.setValidPeriodText(DictUtil.getDictValue("producttype_validperiod", sp.getValidPeriod().toString()));
			spd.setLianDongText(DictUtil.getDictValue("producttype_liandong",sp.getLianDong().toString()));
			spd.setRmbTypeText(DictUtil.getDictValue("producttype_rmb", sp.getRmbType().toString()));
			//spd.setDeviceType(DictUtil.getDictValue("device_type", sp.getDeviceType().toString()));
			spd.setAreaName(sp.getArea().getName());
			spd.setAreaId(sp.getArea().getId());
			return spd;
		}
		@Transactional
		@Override
		public Map<String, Object>  doDelete(String id) {
			Map<String , Object> result = new HashMap<String, Object>();
			SpmsProductType spt = spmsProductTypeDAO.findOne(id);
			
			if(spt == null || spt.getId() == null){
				result.put("success", false);
				result.put("msg", "没有找到对应类型");
				return result;
			}
			String sql = "SELECT * FROM spms_product WHERE type_id = '"+spt.getId()+"'";
			List list = queryDAO.getMapObjects(sql);
			if(list.size()>0){
				result.put("success", false);
				result.put("msg", "该产品类型已有订户使用，无法删除");
				return result;
			}
			
			try{
				spmsProductTypeDAO.delete(spt);
			}catch(Exception e){
				result.put("success", false);
				result.put("msg", "删除失败");
				return result;
			}
			result.put("success", true);
			result.put("msg", "删除成功");
			return result;
		}
		@Transactional
		@Override
		public Boolean  doDeleteAll(int[] data1) {
			try{
			
				for(int i=0;i<data1.length;i++){
					spmsProductTypeDAO.doDeleteById(Integer.valueOf(data1[i]).toString());
				}
				return true;
			}catch(Exception e){
				
				return false;
				}
			
			}
			
		
		@Override
		public List<SpmsProductType> doFindAll(int[] data1) {
			
			return spmsProductTypeDAO.findAll();
		}
		
	    @Override
	    public List<SpmsProductTypeDTO> getAll(){
	    	List<SpmsProductType> sptList = spmsProductTypeDAO.getAll();
	    	List<SpmsProductTypeDTO> sptDTOList = new ArrayList<SpmsProductTypeDTO>();
//	    	if(ListUtils.isNotBlank(sptList)){
	 	    	for(SpmsProductType spt : sptList){
	 	    		SpmsProductTypeDTO sptDTO = new SpmsProductTypeDTO();
	 	    		BeanUtils.copyProperties(spt, sptDTO);
                    sptDTO.setValidPeriodText(DictUtil.getDictValue("producttype_validperiod",spt.getValidPeriod()));
                    sptDTO.setConfig(DictUtil.getDictValue("producttype_config",spt.getConfigurationInformation().toString()));
                    sptDTO.setAreaName(spt.getArea().getName());
                    sptDTO.setMulu(DictUtil.getDictValue("product_mulu",spt.getMuluId().toString()));
	 	    		sptDTOList.add(sptDTO);
		    	}
//		    }
 	    	return sptDTOList;
	    }
		@Override
		@Transactional
		public Map<String, Object> deleteProductAll(List<String> ids) {
			Map<String , Object> result = new HashMap<String, Object>();
			if(ids != null && ids.size() >0 ){
				for(int i=0;i<ids.size();i++) {
					SpmsProductType spt = spmsProductTypeDAO.findOne(ids.get(i));
					if(spt == null || spt.getId() == null){
						result.put("success", false);
						result.put("msg", "没有找到对应类型");
						return result;
					}
					String sql = "SELECT * FROM spms_product WHERE type_id = '"+ids.get(i)+"'";
					List list = queryDAO.getMapObjects(sql);
					if(list.size()>0){
						result.put("success", false);
						result.put("msg", "选择的产品类型"+spt.getNames()+"...已有订户使用，无法删除");
						return result;
					}

				}
				spmsProductTypeDAO.doDeleteProductTypes(ids);
			}
			result.put("success", true);
			result.put("msg", "删除成功");
			return result;
			
		}
}


