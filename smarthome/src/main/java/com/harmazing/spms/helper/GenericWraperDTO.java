package com.harmazing.spms.helper;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.util.ErrorCodeConsts;

public class GenericWraperDTO extends WraperDTO {
	private List<Map<String, Object>> data;

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	
	public GenericWraperDTO(){}
	public GenericWraperDTO(List<Map<String, Object>>  data){
		this.data = data;
	}
	
	public static GenericWraperDTO getDefaultWraperDTO(){
		List<Map<String,Object>> list = Lists.newArrayList();
		GenericWraperDTO userWraperDTO = new GenericWraperDTO(list);		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
