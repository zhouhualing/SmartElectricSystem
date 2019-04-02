package com.harmazing.spms.desktop.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harmazing.spms.desktop.dto.SpmsAccointBillDTO;
import com.harmazing.spms.desktop.entity.SpmsAccountBill;
import com.harmazing.spms.desktop.manager.SpmsAccountBillManager;
import com.harmazing.spms.desktop.manager.SpmsAccountBillManagerImpl;
/**
 * billCONTROLLER
 * @author hanhao
 * @version v1.0
 */
@Controller
@RequestMapping(value = "/spmsAccountBill")
public class SpmsAccountBillController  {

	@Autowired
	private SpmsAccountBillManagerImpl spmsAccountBillService;
	@Autowired
	private SpmsAccountBillManager spmsAccountBillManager;
	@ModelAttribute
	public SpmsAccountBill get(@RequestParam(required=false) String id) {
			return null;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(SpmsAccountBill spmsAccountBill, HttpServletRequest request, HttpServletResponse response, Model model) {
		return null;
	}


	@RequestMapping(value = "form")
	public String form(SpmsAccountBill spmsAccountBill, Model model) {
		model.addAttribute("spmsAccountBill", spmsAccountBill);
		return "modules/spms/spmsAccountBillForm";
	}
	

	@RequestMapping(value = "detail")
	public String detail(SpmsAccountBill spmsAccountBill, Model model) {
		model.addAttribute("spmsAccountBill", spmsAccountBill);
		return "modules/spms/spmsAccountBillDetail";
	}


	@RequestMapping(value = "save")
	public String save(SpmsAccountBill spmsAccountBill, Model model, RedirectAttributes redirectAttributes) {
		return null;
	}
	

	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		return null;
	}
	@RequestMapping("/getInfo")
    @ResponseBody
    public SpmsAccointBillDTO getInfo(String id) {
		//SpmsAccointBillDTO sabd = spmsAccountBillManager.getInfo(id);
    	return spmsAccountBillManager.getInfo(id);
    }

	/**
	 * 发送账单邮件
	 * @param id
	 * @return
	 */
	@RequestMapping("/sendAccountEmail")
	@ResponseBody
	public Map<String, Object> sendAccountEmail(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", spmsAccountBillManager.sendAccountEmail(id));
		return map;
	}

	/**
	 * 发送账单邮件
	 * @param id
	 * @return
	 */
	@RequestMapping("/createChart1")
	@ResponseBody
	public Map<String, Object> createChart1(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = spmsAccountBillManager.createChart1(id, map);
		return map;
	}

	/**
	 * 发送账单邮件
	 * @param id
	 * @return
	 */
	@RequestMapping("/createChart2")
	@ResponseBody
	public Map<String, Object> createChart2(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = spmsAccountBillManager.createChart2(id, map);
		return map;
	}
}
