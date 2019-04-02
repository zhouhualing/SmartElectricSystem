package com.harmazing.spms.base.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class AttachmentDTO  {
	
	private String id;
	
	private List<MultipartFile> files;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
