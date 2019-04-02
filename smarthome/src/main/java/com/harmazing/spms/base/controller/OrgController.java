/**
 * 
 */
package com.harmazing.spms.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.dto.TreeDTO;
import com.harmazing.spms.base.manager.OrgManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月7日
 */
@Controller
@RequestMapping("/org")
public class OrgController {
    
    @Autowired
    private OrgManager orgManager;
    
    @RequestMapping("/getOrgTree")
    @ResponseBody
    public List<TreeDTO> getOrgTree(@RequestBody TreeDTO treeDTO) {
	return orgManager.getOrgTree(treeDTO);
    }
}
