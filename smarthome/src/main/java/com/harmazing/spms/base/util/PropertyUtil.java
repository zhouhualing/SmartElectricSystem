package com.harmazing.spms.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.harmazing.spms.common.log.ILog;
/**
 * properties util class.
 * 
 * @author Zhaocaipeng since TODO
 */
public class PropertyUtil implements ILog {

	private final static PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

	private static Properties properties;

	private static final String propertiesSearchPath = "prop/*.properties";

	public static void loadProperties() throws IOException {
		Resource[] resources = pathMatchingResourcePatternResolver
				.getResources(propertiesSearchPath);
		properties = new Properties();
		for (Resource resource : resources) {
			InputStream temp = resource.getInputStream();
			properties.load(temp);
			temp.close();
		}
	}

	public static String getPropertyInfo(String propertyKey) throws IOException {
		List<String> list = new ArrayList<String>();
		list.add(propertyKey);
		Map<String, String> resultmap = getPropertyInfo(list);
		return resultmap.get(propertyKey);
	}

	public static Map<String, String> getPropertyInfo(List<String> propertyKeys)
			throws IOException {
		Resource[] resources = pathMatchingResourcePatternResolver
				.getResources(propertiesSearchPath);
		properties = new Properties();
		for (Resource resource : resources) {
			InputStream temp = resource.getInputStream();
			properties.load(temp);
			temp.close();
		}
		Map<String, String> resultInfos = new HashMap<String, String>();
		for (String propertyKey : propertyKeys) {
			resultInfos
					.put(propertyKey, properties.get(propertyKey).toString());
		}

		return resultInfos;

	}
}
