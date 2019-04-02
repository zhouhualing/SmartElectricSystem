package com.harmazing.spms.base.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.dto.DictDTO;

/**
 * dictDao impl.
 * @author Zhaocaipeng
 * since 2013年12月12日
 */
@Repository("dictDAO")
public class DictDAOImpl implements DictDAO  {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	/* (non-Javadoc)
	 * @see cn.clickmed.cmcp.dao.org.DictDAO#loadDict(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<DictDTO> loadDict(String tableName) {
		Query query = entityManagerFactory.createEntityManager().createNativeQuery("select code code, value value, iOrder iOrder from "+tableName);
		List <Object []>objects = query.getResultList();
		List <DictDTO> dictDTOs = new ArrayList<DictDTO>();
		for(Object [] object : objects) {
			DictDTO dictDTO = new DictDTO();
			dictDTO.setCode(object[0].toString());
			dictDTO.setValue(object[1].toString());
			dictDTO.setiOrder(object[2].toString());
			dictDTOs.add(dictDTO);
		}
		
		return dictDTOs;
	}
}