package com.harmazing.spms.base.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import com.harmazing.spms.common.entity.IEntity;
import com.harmazing.spms.jszc.entity.JSZCEntityId;

@SuppressWarnings("serial")
@Entity
@Table(name = "spms_ndr_dsm")
@IdClass(_DSMEntity.class)
public class DSMEntity implements IEntity {
	
	@Id
	@Column(name = "faId")
	private String faId;
	@Id
	@Column(name = "id")
	private String id;
	@Id
	@Column(name = "infoType")
	private Integer infoType;
	
	
	@Column(name = "releaseDate")
	private Date releaseDate;
	
	@Column(name = "abateDate")
	private Date abateDate;
	@Column(name = "confirmDate")
	private Date confirmDate;
	@Column(name = "msgDes")
	private String msgDes;
	@Column(name = "releaseUser")
	private String releaseUser;
	@Column(name = "schemeCode")
	private String schemeCode;
	@Column(name = "schemeName")
	private String schemeName;
	@Column(name = "schemeType")
	private String schemeType;
	@Column(name = "resType")
	private String resType;
	@Column(name = "impDate")
	private Date impDate;
	@Column(name = "startDate")
	private Date startDate;
	@Column(name = "endDate")
	private Date endDate;
	@Column(name = "planReduceTargetP")
	private Integer planReduceTargetP;
	@Column(name = "planReduceTargetQ")
	private Integer planReduceTargetQ;
	@Column(name = "promisedReduceTargetP")
	private Integer promisedReduceTargetP;
	@Column(name = "promisedReduceTargetQ")
	private Integer promisedReduceTargetQ;
	@Column(name = "actualReduceP")
	private Integer actualReduceP;
	@Column(name = "actualReduceQ")
	private Integer actualReduceQ;
	@Column(name = "startConditions")
	private String startConditions;
	@Column(name = "contact")
	private String contact;
	@Column(name = "contactPhone")
	private String contactPhone;
	@Column(name = "schemePeople")
	private String schemePeople;
	@Column(name = "schemeDate")
	private String schemeDate;
	@Column(name = "schemeIntroduction")
	private String schemeIntroduction;
	@Column(name = "status")
	private String status;
	public String getFaId() {
		return faId;
	}
	public void setFaId(String faId) {
		this.faId = faId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getInfoType() {
		return infoType;
	}
	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Date getAbateDate() {
		return abateDate;
	}
	public void setAbateDate(Date abateDate) {
		this.abateDate = abateDate;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getMsgDes() {
		return msgDes;
	}
	public void setMsgDes(String msgDes) {
		this.msgDes = msgDes;
	}
	public String getReleaseUser() {
		return releaseUser;
	}
	public void setReleaseUser(String releaseUser) {
		this.releaseUser = releaseUser;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public Date getImpDate() {
		return impDate;
	}
	public void setImpDate(Date impDate) {
		this.impDate = impDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getPlanReduceTargetP() {
		return planReduceTargetP;
	}
	public void setPlanReduceTargetP(Integer planReduceTargetP) {
		this.planReduceTargetP = planReduceTargetP;
	}
	public Integer getPlanReduceTargetQ() {
		return planReduceTargetQ;
	}
	public void setPlanReduceTargetQ(Integer planReduceTargetQ) {
		this.planReduceTargetQ = planReduceTargetQ;
	}
	public Integer getPromisedReduceTargetP() {
		return promisedReduceTargetP;
	}
	public void setPromisedReduceTargetP(Integer promisedReduceTargetP) {
		this.promisedReduceTargetP = promisedReduceTargetP;
	}
	public Integer getPromisedReduceTargetQ() {
		return promisedReduceTargetQ;
	}
	public void setPromisedReduceTargetQ(Integer promisedReduceTargetQ) {
		this.promisedReduceTargetQ = promisedReduceTargetQ;
	}
	public Integer getActualReduceP() {
		return actualReduceP;
	}
	public void setActualReduceP(Integer actualReduceP) {
		this.actualReduceP = actualReduceP;
	}
	public Integer getActualReduceQ() {
		return actualReduceQ;
	}
	public void setActualReduceQ(Integer actualReduceQ) {
		this.actualReduceQ = actualReduceQ;
	}
	public String getStartConditions() {
		return startConditions;
	}
	public void setStartConditions(String startConditions) {
		this.startConditions = startConditions;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getSchemePeople() {
		return schemePeople;
	}
	public void setSchemePeople(String schemePeople) {
		this.schemePeople = schemePeople;
	}
	public String getSchemeDate() {
		return schemeDate;
	}
	public void setSchemeDate(String schemeDate) {
		this.schemeDate = schemeDate;
	}
	public String getSchemeIntroduction() {
		return schemeIntroduction;
	}
	public void setSchemeIntroduction(String schemeIntroduction) {
		this.schemeIntroduction = schemeIntroduction;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
