package com.harmazing.spms.base.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.harmazing.spms.base.dto.AttachmentDTO;
import com.harmazing.spms.base.entity.AttachmentEntity;
import com.harmazing.spms.base.manager.AttachmentManager;
import com.harmazing.spms.base.util.ConstantUtil;
import com.harmazing.spms.base.util.PropertyUtil;
import com.harmazing.spms.base.util.UserUtil;

@Controller
public class AttachmentController {
	
	@Autowired
	private AttachmentManager attachmentManager;
	
	@RequestMapping("/attachment/upload")
	@ResponseBody
	public Map<String,List<Map<String, Object>>> doUploadFiles(AttachmentDTO attachmentDTOd, HttpServletRequest httpServletRequest) throws IOException {
		String uploadPath = PropertyUtil.getPropertyInfo(ConstantUtil.UPLOADFOLDER);
		File fileFolder = new File(uploadPath);
		if(!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		Map <String,List<Map <String, Object>>> maps = new HashMap<String,List<Map<String, Object>>>();
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		if(attachmentDTOd.getFiles() == null) {
		    if(httpServletRequest instanceof DefaultMultipartHttpServletRequest) {
			DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = (DefaultMultipartHttpServletRequest)httpServletRequest;
			Collection<List<MultipartFile>> theFiles = defaultMultipartHttpServletRequest.getMultiFileMap().values();
			for(List<MultipartFile> multipartFiles : theFiles) {
			    MultipartFile multipartFile = multipartFiles.get(0);
    			Map <String,Object> map = new HashMap<String, Object>();
    			AttachmentDTO attachmentDTO = new AttachmentDTO();
    			AttachmentEntity attachmentEntity = new AttachmentEntity();
    			int index = multipartFile.getOriginalFilename().lastIndexOf(".");
    			if(index != -1) {
    				attachmentEntity.setFilePostfix(multipartFile.getOriginalFilename().substring(index+1));
    				attachmentEntity.setFileName(multipartFile.getOriginalFilename().substring(0,index));
    			} else {
    				attachmentEntity.setFileName(multipartFile.getOriginalFilename());
    			}
    			
    			attachmentEntity.setId(UUID.randomUUID().toString());
    			Date createDate = new Date();
    			attachmentEntity.setCreateDate(createDate);
    			attachmentEntity.setLastModifyDate(createDate);
    			attachmentEntity.setCreateUser(UserUtil.getCurrentUser());
    			attachmentEntity.setLastModifyUser(UserUtil.getCurrentUser());
    			attachmentEntity.setFileSize(multipartFile.getSize());
    			attachmentEntity.setFilePath(fileFolder+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
    			File file = new File (fileFolder+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
    			FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
    			attachmentManager.doSaveAttachment(attachmentEntity);
    			attachmentDTO.setId(attachmentEntity.getId().toString());
    			attachmentDTO.setFiles(attachmentDTOd.getFiles());
    			map.put("id", attachmentEntity.getId().toString());
    			map.put("name", attachmentEntity.getFileName());
    			map.put("type", attachmentEntity.getFilePostfix());
    			map.put("size", attachmentEntity.getFileSize());
    			map.put("url", "/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
    			map.put("thumbnailUrl", "/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
    			map.put("deleteUrl", attachmentEntity.getFilePath()+"/delete");
    			map.put("pdfUrl", httpServletRequest.getContextPath()+"/static/pdf"+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
    			map.put("deleteType", "DELETE");
    			lists.add(map);
			}
		    }
		} else {
        		for(MultipartFile multipartFile : attachmentDTOd.getFiles()) {
        			Map <String,Object> map = new HashMap<String, Object>();
        			AttachmentDTO attachmentDTO = new AttachmentDTO();
        			AttachmentEntity attachmentEntity = new AttachmentEntity();
        			int index = multipartFile.getOriginalFilename().lastIndexOf(".");
        			if(index != -1) {
        				attachmentEntity.setFilePostfix(multipartFile.getOriginalFilename().substring(index+1));
        				attachmentEntity.setFileName(multipartFile.getOriginalFilename().substring(0,index));
        			} else {
        				attachmentEntity.setFileName(multipartFile.getOriginalFilename());
        			}
        			
        			attachmentEntity.setId(UUID.randomUUID().toString());
        			Date createDate = new Date();
        			attachmentEntity.setCreateDate(createDate);
        			attachmentEntity.setLastModifyDate(createDate);
        			attachmentEntity.setCreateUser(UserUtil.getCurrentUser());
        			attachmentEntity.setLastModifyUser(UserUtil.getCurrentUser());
        			attachmentEntity.setFileSize(multipartFile.getSize());
        			attachmentEntity.setFilePath(fileFolder+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
        			File file = new File (fileFolder+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
        			FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
        			attachmentManager.doSaveAttachment(attachmentEntity);
        			attachmentDTO.setId(attachmentEntity.getId().toString());
        			attachmentDTO.setFiles(attachmentDTOd.getFiles());
        			map.put("id", attachmentEntity.getId().toString());
        			map.put("name", attachmentEntity.getFileName());
        			map.put("type", attachmentEntity.getFilePostfix());
        			map.put("size", attachmentEntity.getFileSize());
        			map.put("url", "/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
        			map.put("thumbnailUrl", "/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
        			map.put("deleteUrl", attachmentEntity.getFilePath()+"/delete");
        			map.put("pdfUrl", httpServletRequest.getContextPath()+"/static/pdf"+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
        			map.put("deleteType", "DELETE");
        			lists.add(map);
        		}
		}
		maps.put("files", lists);
		
		return maps;
	}
	
	@RequestMapping("/attachment/edUpload")
	@ResponseBody
	public Map<String,String> doEdUploadFiles(AttachmentDTO attachmentDTOd, HttpServletRequest httpServletRequest) throws IOException {
		String uploadPath = PropertyUtil.getPropertyInfo(ConstantUtil.UPLOADFOLDER);
		File fileFolder = new File(uploadPath);
		if(!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		Map <String,String> map = new HashMap<String,String>();
		if(attachmentDTOd.getFiles() == null) {
		    if(httpServletRequest instanceof DefaultMultipartHttpServletRequest) {
			DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = (DefaultMultipartHttpServletRequest)httpServletRequest;
			Collection<List<MultipartFile>> theFiles = defaultMultipartHttpServletRequest.getMultiFileMap().values();
			for(List<MultipartFile> multipartFiles : theFiles) {
			    MultipartFile multipartFile = multipartFiles.get(0);
    			AttachmentDTO attachmentDTO = new AttachmentDTO();
    			AttachmentEntity attachmentEntity = new AttachmentEntity();
    			int index = multipartFile.getOriginalFilename().lastIndexOf(".");
    			if(index != -1) {
    				attachmentEntity.setFilePostfix(multipartFile.getOriginalFilename().substring(index+1));
    				attachmentEntity.setFileName(multipartFile.getOriginalFilename().substring(0,index));
    			} else {
    				attachmentEntity.setFileName(multipartFile.getOriginalFilename());
    			}
    			
    			attachmentEntity.setId(UUID.randomUUID().toString());
    			Date createDate = new Date();
    			attachmentEntity.setCreateDate(createDate);
    			attachmentEntity.setLastModifyDate(createDate);
    			attachmentEntity.setCreateUser(UserUtil.getCurrentUser());
    			attachmentEntity.setLastModifyUser(UserUtil.getCurrentUser());
    			attachmentEntity.setFileSize(multipartFile.getSize());
    			attachmentEntity.setFilePath(fileFolder+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
    			File file = new File (fileFolder+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
    			FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
    			attachmentManager.doSaveAttachment(attachmentEntity);
    			attachmentDTO.setId(attachmentEntity.getId().toString());
    			attachmentDTO.setFiles(attachmentDTOd.getFiles());
    			map.put("id", attachmentEntity.getId().toString());
    			map.put("name", attachmentEntity.getFileName());
    			map.put("type", attachmentEntity.getFilePostfix());
    			map.put("size", attachmentEntity.getFileSize().toString());
    			map.put("url", "/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
    			map.put("thumbnailUrl", "/cmcp/attachment/downloadFile/"+attachmentEntity.getId());
    			map.put("deleteUrl", attachmentEntity.getFilePath()+"/delete");
    			map.put("pdfUrl", httpServletRequest.getContextPath()+"/static/pdf"+"/"+attachmentEntity.getId().toString()+"."+attachmentEntity.getFilePostfix());
    			map.put("deleteType", "DELETE");
    			map.put("state", "SUCCESS");
			}
		    }
		} 
		
		return map;
	}
	
	@RequestMapping("/attachment/downloadFile/{fileId}")
	public void download(@PathVariable("fileId") String fileId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {  
		AttachmentEntity attachmentEntity = attachmentManager.findByAttachmentEntity(fileId);
//		
//		
//	    HttpHeaders headers = new HttpHeaders();  
//	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
//	     String headerValue = String.format("attachment; filename=\"%s\"",
//	    		 new String(uploadFileEntity.getRealFileName().getBytes(),"ISO8859-1"));
//	    headers.setContentDispositionFormData("attachment", headerValue);  
//	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(uploadFileEntity.getFilePath())),  
//	                                      headers, HttpStatus.CREATED);  
		
		InputStream inStream = new FileInputStream(attachmentEntity.getFilePath());// 文件的存放路径
		httpServletResponse.reset();
		httpServletResponse.setContentType("bin");
//		httpServletResponse.setContentType("application/pdf");
		String fileName = attachmentEntity.getFileName()+"."+attachmentEntity.getFilePostfix();
	    String agent = httpServletRequest.getHeader("USER-AGENT");
	    if(agent != null && (agent.indexOf("MSIE") != -1 ||agent.indexOf("Windows") != -1)) {
	        fileName = URLEncoder.encode(fileName, "UTF8");
	        fileName = fileName.replaceAll("\\+", "%20");
	    } else {
	        fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
	    }
		httpServletResponse.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		byte[] b = new byte[100];
		int len;
		try {
		while ((len = inStream.read(b)) > 0)
			httpServletResponse.getOutputStream().write(b, 0, len);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  
}
