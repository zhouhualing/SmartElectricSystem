package com.harmazing.spms.base.dto;

public class AttachmentInfoDTO {
	
	private String id;
	
	private String order;
	
	private String name;
	
	private Long size;
	
	private String type;
	private String url;
	
	private String thumbnailUrl;
	
	private String deleUrl;
	
	private String deleteType = "DELETE";
	
	private String pdfUrl;
	
	private String flagName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlagName() {
	    return flagName;
	}

	public void setFlagName(String flagName) {
	    this.flagName = flagName;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleUrl() {
		return deleUrl;
	}

	public void setDeleUrl(String deleUrl) {
		this.deleUrl = deleUrl;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}
	
}
