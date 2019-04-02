package com.harmazing.spms.helper;

import com.harmazing.spms.base.util.ErrorCodeConsts;

public class ObjectWraperDTO extends WraperDTO {
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public ObjectWraperDTO(){}
	
	public static ObjectWraperDTO getDefaultWraperDTO(){
		ObjectWraperDTO userWraperDTO = new ObjectWraperDTO();		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
