package com.harmazing.spms.user.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;

import com.harmazing.spms.common.entity.GenericEntity;

@Entity
@Table(name = "spms_user_async")
public class SpmsUserAsyncEntity extends GenericEntity{
	private static final long serialVersionUID = 1L;

	@Length(min=0, max=200)
	@Column(name = "mobile")
	private String mobile;	
	
	
	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public Byte[] getImage() {
		return image;
	}


	public void setImage(Byte[] image) {
		this.image = image;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Lob @Basic(fetch=FetchType.LAZY) 
    private Byte[] image;

}
