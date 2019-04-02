package com.harmazing.spms.helper;

import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserWraperDTO extends WraperDTO {
	private UserDTO user;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public UserWraperDTO(){}
	public UserWraperDTO(UserDTO user){
		this.user = user;
	}
	
	public static UserWraperDTO getDefaultWraperDTO(){
		
		UserWraperDTO userWraperDTO = new UserWraperDTO(new UserDTO());		
		userWraperDTO.setUserResult(ErrorCodeConsts.Success);
		return userWraperDTO;
	}
}
