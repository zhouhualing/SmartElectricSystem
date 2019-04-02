package com.harmazing.spms.base.manager;

import com.harmazing.spms.base.dao.CodeGeneraterDAO;
import com.harmazing.spms.base.entity.CodeGeneraterEntity;
import com.harmazing.spms.base.manager.CodeGeneraterManager;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("codeGeneraterManager")
public class CodeGeneraterManagerImpl implements CodeGeneraterManager {

	public static final String FILE_PREFIX_PART_2 = "0000000000";

	@ Autowired
	private CodeGeneraterDAO codeGeneraterDAO;
	
	@Override
	@Transactional
	public synchronized String getCode(Integer type) {
		SearchFilter searchFilter = new SearchFilter("type",type);
		CodeGeneraterEntity generaterEntity = codeGeneraterDAO.findOne(DynamicSpecifications.bySearchFilter(searchFilter, CodeGeneraterEntity.class));
		if(generaterEntity == null) {
			generaterEntity = new CodeGeneraterEntity();
			generaterEntity.setType(type);
			generaterEntity.setCode(FILE_PREFIX_PART_2+1);
			codeGeneraterDAO.save(generaterEntity);
		} else {
			Integer codeI = Integer.valueOf(generaterEntity.getCode())+1;
			String codeStr = FILE_PREFIX_PART_2+codeI.toString();
			generaterEntity.setCode(codeStr.substring(codeStr.length()-(FILE_PREFIX_PART_2.length()+1)));
			codeGeneraterDAO.save(generaterEntity);
		}
		return generaterEntity.getCode();
	}
	
	
}
