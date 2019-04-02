package com.harmazing.spms.desktop.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.desktop.dao.SpmsProductMFeeDAO;
import com.harmazing.spms.desktop.dto.SpmsProductMFeeDTO;
import com.harmazing.spms.desktop.entity.SpmsProductMFee;
import com.harmazing.spms.desktop.manager.SpmsProductMFeeManager;
@Component
@Transactional(readOnly = true)
public class SpmsProductMFeeManagerImpl implements SpmsProductMFeeManager{
	@Autowired
	private SpmsProductMFeeDAO spmsProductMFeeDAO;

	@Override
	public List<SpmsProductMFeeDTO> findAllByUser(String userId,Integer month,Integer year) {
		List<SpmsProductMFee> spmList = spmsProductMFeeDAO.findAllByUser(userId,month,year);
		List<SpmsProductMFeeDTO> spmdList = new ArrayList<SpmsProductMFeeDTO>();
		if(spmList!=null&&spmList.size()>0){
			SpmsProductMFeeDTO spmd = new SpmsProductMFeeDTO();
			for(SpmsProductMFee spm: spmList){
				BeanUtils.copyProperties(spm, spmd);
				spmd.setProdductTypeName(spm.getProductId().getSpmsProductType().getNames());
				spmdList.add(spmd);
			}
		}
		return spmdList;
	}
}
