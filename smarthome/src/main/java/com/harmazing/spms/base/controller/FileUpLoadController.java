package com.harmazing.spms.base.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;





@Controller
public class FileUpLoadController {
	
	
	@RequestMapping("/doSave")
	@ResponseBody
	public Map<String,Object> deviceImport(@RequestParam MultipartFile[] myfiles, HttpServletRequest request) throws IOException{
		//文件解析完事了， 就删了。
		Map<String,Object> result = Maps.newHashMap();
		for(MultipartFile myfile : myfiles){  
			 if(myfile.isEmpty()){
				 result.put("success", false);
				 result.put("msg", "导入时，必须选择正确的数据文件");
				 return result;
			 }
			 String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
			 File dir=new File(realPath);
			 if  (!dir .exists()  && !dir .isDirectory())      
			 {        
				 dir .mkdir();    //不存在文件夹则创建
			 }
			 FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));//copy
			 
			 File file = new File(realPath+"/"+ myfile.getOriginalFilename()); //找到刚刚copy的文件
			 
			 if(!file.exists()){  
				 result.put("success", false);
				 result.put("msg", "导入文件失败");
				 return result;
		     } 
			 
			 try{
				 InputStream inputStream = new FileInputStream(file);
				 String fileName = file.getName();
				 Workbook wb = null;
				 if(fileName.endsWith("xls")){  
		                wb = new HSSFWorkbook(inputStream);//解析xls格式  
		         }else if(fileName.endsWith("xlsx")){  
		                wb = new XSSFWorkbook(inputStream);//解析xlsx格式  
		         }
				 
				 
				 
				 
				 
				 
			 }catch(Exception e){
				 e.printStackTrace();
				 result.put("success", false);
				 result.put("msg", "导入文件解析失败");
				 return result;
			 }
			 
		}
		return result;
	}
}
