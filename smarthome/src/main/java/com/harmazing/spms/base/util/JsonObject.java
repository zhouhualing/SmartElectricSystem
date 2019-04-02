package com.harmazing.spms.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * bean util.
 * @author Zhaocaipeng
 * since 2013-9-16
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class JsonObject {
	
	private Map <String, Object> map = new HashMap<String,Object>();
	
	public void setValue(String path, Object value) {
		setValue(path, value, false);
	}
	
	public void setValue(String path, Object value, Boolean isArray) {
		String [] pathArr = path.split("\\.");
		Object currentPathObj = map;
		for(int i=0; i<pathArr.length-1; i++) {
			Object tempObject = null;
			if(pathArr[i].endsWith("]")) {
				Integer index = 0;
				if(currentPathObj instanceof Map) {
					index = Integer.valueOf(pathArr[i].substring(pathArr[i].indexOf("[")+1,pathArr[i].lastIndexOf("]")));
					pathArr[i] = pathArr[i].substring(0, pathArr[i].indexOf("["));
					tempObject = ((Map)currentPathObj).get(pathArr[i]);
				} else {
					
				}
				if(tempObject == null) {
					tempObject = Lists.newArrayListWithExpectedSize(index);
					for(int j=0; j<=index; j++) {
						((List)tempObject).add(Maps.newHashMap());
					}
					((Map)currentPathObj).put(pathArr[i],tempObject);
					tempObject = ((List)tempObject).get(index);
				} else {
					if(index >= ((List)tempObject).size()) {
					    	Integer theSize = ((List)tempObject).size();
						for(int j=0; j<=theSize-index; j++) {
						    	
							((List)tempObject).add(Maps.newHashMap());
						}	    
					}	    
				        tempObject = ((List)tempObject).get(index);
				}
			} else {
				
				if(currentPathObj instanceof Map) {
					tempObject = ((Map)currentPathObj).get(pathArr[i]);
				}  else {
				    //todo
				}
				
				if(tempObject == null) {
					tempObject = Maps.newHashMap();
					((Map)currentPathObj).put(pathArr[i],tempObject);
					
				}

			}
			currentPathObj = tempObject;
		}
		if(pathArr[pathArr.length-1].endsWith("]")) {
			Integer index = Integer.valueOf(pathArr[pathArr.length-1].substring(pathArr[pathArr.length-1].indexOf("[")+1,pathArr[pathArr.length-1].lastIndexOf("]")));
			String currentPath = pathArr[pathArr.length-1].substring(0, pathArr[pathArr.length-1].indexOf("["));
			List tempObject = (List)((Map)currentPathObj).get(currentPath);
			
			if(tempObject == null) {
				tempObject = Lists.newArrayListWithExpectedSize(index);
				for(int j=0; j<=index; j++) {
					((List)tempObject).add(Maps.newHashMap());
				}
				((Map)currentPathObj).put(currentPath, tempObject);
			} 
			if(index >= tempObject.size()) {
			    	Integer theSize = tempObject.size();
				for(int j=0; j<=theSize-index; j++) {
				    	
					((List)tempObject).add(Maps.newHashMap());
				}
				((Map)currentPathObj).put(currentPath, tempObject);		    
			}
			tempObject.set(index, value);
			
		}  else {
		    ((Map)currentPathObj).put(pathArr[pathArr.length-1],value);
		}
		
	}
	
	public Object getValue(String path) {
		String [] pathArr = path.split("\\.");
		Object currentPathObj = map;
		for(int i=0; i<pathArr.length-1; i++) {
			Object tempObject = null;
			if(pathArr[i].endsWith("]")) {
				Integer index = 0;
				if(currentPathObj instanceof Map) {
					index = Integer.valueOf(pathArr[i].substring(pathArr[i].indexOf("[")+1,pathArr[i].lastIndexOf("]")));
					pathArr[i] = pathArr[i].substring(0, pathArr[i].indexOf("["));
					tempObject = ((Map)currentPathObj).get(pathArr[i]);
				} else {
					
				}
				if(tempObject == null) {
					tempObject = Lists.newArrayListWithExpectedSize(index);
					for(int j=0; j<=index; j++) {
						((List)tempObject).add(Maps.newHashMap());
					}
					((Map)currentPathObj).put(pathArr[i],tempObject);
					tempObject = ((List)tempObject).get(index);
				} else {
					if(index >= ((List)tempObject).size()) {
					    	Integer theSize = ((List)tempObject).size();
						for(int j=0; j<=theSize-index; j++) {
						    	
							((List)tempObject).add(Maps.newHashMap());
						}	    
					}	    
				        tempObject = ((List)tempObject).get(index);
				}
			} else {
				
				if(currentPathObj instanceof Map) {
					tempObject = ((Map)currentPathObj).get(pathArr[i]);
				}  else {
				    //todo
				}
				
				if(tempObject == null) {
					tempObject = Maps.newHashMap();
					((Map)currentPathObj).put(pathArr[i],tempObject);
					
				}

			}
			currentPathObj = tempObject;
		}
		if(pathArr[pathArr.length-1].endsWith("]")) {
			Integer index = Integer.valueOf(pathArr[pathArr.length-1].substring(pathArr[pathArr.length-1].indexOf("[")+1,pathArr[pathArr.length-1].lastIndexOf("]")));
			String currentPath = pathArr[pathArr.length-1].substring(0, pathArr[pathArr.length-1].indexOf("["));
			List tempObject = (List)((Map)currentPathObj).get(currentPath);
			
			if(tempObject == null) {
				tempObject = Lists.newArrayListWithExpectedSize(index);
				for(int j=0; j<=index; j++) {
					((List)tempObject).add(Maps.newHashMap());
				}
				((Map)currentPathObj).put(currentPath, tempObject);
			} 
			if(index >= tempObject.size()) {
			    	Integer theSize = tempObject.size();
				for(int j=0; j<=theSize-index; j++) {
				    	
					((List)tempObject).add(Maps.newHashMap());
				}
				((Map)currentPathObj).put(currentPath, tempObject);		    
			}
			return tempObject.get(index);
			
		}  else {
		    return ((Map)currentPathObj).get(pathArr[pathArr.length-1]);
		}	    
	}
	
	public void addValue(String path, Object value) {
		String [] pathArr = path.split("\\.");
		Object currentPathObj = map;
		for(int i=0; i<pathArr.length-1; i++) {
			Object tempObject = null;
			if(pathArr[i].endsWith("]")) {
				Integer index = 0;
				if(currentPathObj instanceof Map) {
					index = Integer.valueOf(pathArr[i].substring(pathArr[i].indexOf("[")+1,pathArr[i].lastIndexOf("]")));
					pathArr[i] = pathArr[i].substring(0, pathArr[i].indexOf("["));
					tempObject = ((Map)currentPathObj).get(pathArr[i]);
				} else {
					
				}
				if(tempObject == null) {
					tempObject = Lists.newArrayListWithExpectedSize(index);
					for(int j=0; j<=index; j++) {
						((List)tempObject).add(Maps.newHashMap());
					}
					((Map)currentPathObj).put(pathArr[i],tempObject);
					tempObject = ((List)tempObject).get(index);
				} else {
					if(index >= ((List)tempObject).size()) {
					    	Integer theSize = ((List)tempObject).size();
						for(int j=0; j<=theSize-index; j++) {
						    	
							((List)tempObject).add(Maps.newHashMap());
						}	    
					}	    
				        tempObject = ((List)tempObject).get(index);
				}
			} else {
				
				if(currentPathObj instanceof Map) {
					tempObject = ((Map)currentPathObj).get(pathArr[i]);
				}  else {
				    //todo
				}
				
				if(tempObject == null) {
					tempObject = Maps.newHashMap();
					((Map)currentPathObj).put(pathArr[i],tempObject);
					
				}

			}
			currentPathObj = tempObject;
		}
		if(pathArr[pathArr.length-1].endsWith("]")) {
			Integer index = Integer.valueOf(pathArr[pathArr.length-1].substring(pathArr[pathArr.length-1].indexOf("[")+1,pathArr[pathArr.length-1].lastIndexOf("]")));
			String currentPath = pathArr[pathArr.length-1].substring(0, pathArr[pathArr.length-1].indexOf("["));
			List tempObject = (List)((Map)currentPathObj).get(currentPath);
			
			if(tempObject == null) {
				tempObject = Lists.newArrayListWithExpectedSize(index);
				for(int j=0; j<=index; j++) {
					((List)tempObject).add(Maps.newHashMap());
				}
				((Map)currentPathObj).put(currentPath, tempObject);
			} 
			if(index >= tempObject.size()) {
			    	Integer theSize = tempObject.size();
				for(int j=0; j<=theSize-index; j++) {
				    	
					((List)tempObject).add(Maps.newHashMap());
				}
				((Map)currentPathObj).put(currentPath, tempObject);		    
			}
			tempObject.set(index, value);
			
		}  else {
			if(((Map)currentPathObj).get(pathArr[pathArr.length-1]) == null) {
			    List lists = Lists.newArrayList();
			    if(value instanceof List) {
				 lists.addAll((List)value);
			    } else {
				 lists.add(value);
			    }
			   
			    ((Map)currentPathObj).put(pathArr[pathArr.length-1],lists);
			} else {
			    ((List)((Map)currentPathObj).get(pathArr[pathArr.length-1])).add(value);
			}
		}
		
		
	}
	
	public Map <String, Object> getData() {
		return map;
	}
	
	
	public String getJsonData() {
		return new JSONObject(this.map).toString();
	}
	
	public void setData(Map map) {
	    this.map = map;
	}
}
