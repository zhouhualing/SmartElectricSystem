package com.harmazing.spms.usersRairconSetting.toolsClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class JDomXml
{
	public static Map<String,Object> cityMap = new HashMap<String,Object>();
	/**
	 * 加载城市代码
	 */
	@SuppressWarnings("unchecked")
	public static void getCityCode(){
		try {
			JDomXml jDomXml = new JDomXml();
			//String xmlPath = jDomXml.getClass().getClassLoader().getResource("/").getPath();
			Resource resources = new PathMatchingResourcePatternResolver()
					.getResource("/cityCode.xml");
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(resources.getInputStream());
			Element root = doc.getRootElement();//获得根节点<bookstore>  
			List<Element> list=root.getChildren();//获得所有根节点<bookstore>的子节点
			for(Element e:list){  
				String name = e.getChildText("name");
				String code = e.getChildText("code");
				cityMap.put(name, code);
			}
			cityMap.put("bo", "true");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public static void main(String[] args) {
		try {
			getCityCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
