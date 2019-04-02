/**
 * 
 */
package com.harmazing.spms.ifttt.entity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.entity.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="ifttt_stragegy_test")
public class SpmsIftttStrategy extends GenericEntity{

    private static final long serialVersionUID = 1L;
    
	public static long getSerialversionuid() {
	    return serialVersionUID;
	}

	private String ifConditionDTO; //DTO of input
	
	private String ifCondition; //follow entity name, but time is exception
	
	private String action; //method name of IftttService
	private String actionPara;

	public String getIfConditionDTO() {
		return ifConditionDTO;
	}
	public void setIfConditionDTO(String ifConditionDTO) {
		this.ifConditionDTO = ifConditionDTO;
	}
	public String getIfCondition() {
		return ifCondition;
	}
	public void setIfCondition(String ifCondition) {
		this.ifCondition = ifCondition;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionPara() {
		return actionPara;
	}
	public void setActionPara(String actionPara) {
		this.actionPara = actionPara;
	}
}
