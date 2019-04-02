package com.harmazing.spms.base.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.harmazing.spms.base.dto.QueryDTO;
import com.harmazing.spms.base.dto.QuerysDTO;
import com.harmazing.spms.common.log.ILog;

/**
 * query util.
 * @author Zhaocaipeng
 * since 2013-9-17
 */
public class QueryUtil implements ILog {
	
	/**
	 * use to cache the java object of querys.
	 */
	public static final Map <String, QueryDTO> queryCaches = new HashMap<String, QueryDTO>();
	
	public static final String QUERYSEARCHPATH = "classpath:query/**/*query.xml";
	
	/**
	 * load query
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static final void loadQuerys() throws FileNotFoundException, IOException, JAXBException {
		if( queryCaches.size() > 0)
			return;
		
		JAXBContext context = JAXBContext.newInstance(QuerysDTO.class);
		PathMatchingResourcePatternResolver prpr = new PathMatchingResourcePatternResolver();
		Resource [] resources;
		try{
		    resources = prpr.getResources(QUERYSEARCHPATH);
		} catch(FileNotFoundException fileNotFoundException) {
		    logger.error("can not found the query conf folder.");
		    return;
		}
		for(int i=0; i<resources.length; i++) {
			logger.info("start parse :"+resources[i].getURI());
	        Unmarshaller um = context.createUnmarshaller();
	        QuerysDTO querysDTO = (QuerysDTO)um.unmarshal(resources[i].getInputStream());
	        for(QueryDTO queryDTO : querysDTO.getQuery()) {
	        	queryCaches.put(queryDTO.getQueryId(), queryDTO);
	        }
	        logger.info("end parse :"+resources[i].getURI());
		}
	}
	
	public static QueryDTO getQueryDTOByQueryId(String queryId) {
	    	if(ConstantUtil.MODEL_KEY_DEV.equals(SpringUtil.getApplicationContext().getEnvironment().getProperty(ConstantUtil.MODEL_KEY))) {
	    	    try {
	    		loadQuerys();
	    	    }catch(Exception exception) {
	    		logger.error("reload query error:"+exception.getMessage());
	    	    }
	    	}
		return (QueryDTO)queryCaches.get(queryId);
	}
	
}
