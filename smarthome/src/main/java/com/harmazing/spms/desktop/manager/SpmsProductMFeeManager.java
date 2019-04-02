package com.harmazing.spms.desktop.manager;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.desktop.dto.SpmsProductMFeeDTO;
import com.harmazing.spms.desktop.entity.SpmsAccountBill;
import com.harmazing.spms.desktop.entity.SpmsProductMFee;

public interface SpmsProductMFeeManager extends IManager{
	public List<SpmsProductMFeeDTO> findAllByUser(String userId,Integer month,Integer year);
}
