var selectType = getURLParam("selectType");
var markType = getURLParam("markType");
var roleCodes = getURLParam("roleCodes");
var checkType = getURLParam("checkType");
var isShowDate = getURLParam("isShowDate");
var operaterId = getURLParam("operaterId");
var printType = getURLParam("printType");
var businessType = getURLParam("businessType");


function doCallBack() {
	
	if(markType != "0002") {
		if($("#mark").val().length <= 0) {
			$.alert("请输入处理信息");
			return false;
		}		
	}
//	var arr1 = clickmedTables.orgTargetDiv1.selectObjs;
	var arr2 = new Array();
	if(checkType=="single") {
		
//		arr2.push($.parseJSON(clickmedTables.orgTargetDiv.getCheckedRadioValue()));
		arr2 = zTree_Menu.getCheckedNodes();
	} else {
//		arr2 = clickmedTables.orgTargetDiv.selectObjs;
		arr2 = zTree_Menu.getCheckedNodes()
	}

//	var arr3 = nowTree.getCheckedNodes(true);
	var roleCodes = new Array();
	var roleNames = new Array();
	var counts = new Array();
//	for(var i=0; i< arr1.length; i++) {
//		roleCodes.push(arr1[i].contactUser.roleCode);
//		roleNames.push(arr1[i].contactUser.roleName);
//	}
	for(var i=0; i< arr2.length; i++) {
		if(arr2[i].level == 1) {
			if(printType =="0002") {
				var aObj = $("#" + arr2[i].tId+"_span");
				if($("input",aObj.next("span")).val().length <=0) {
					$.alert("请输入分发份数.");
				}	
				counts.push($("input",aObj.next("span")).val());
			}

			roleCodes.push(arr2[i].code);
			roleNames.push(arr2[i].name);
			
		}

	}
//	for(var i=0; i<arr3.length; i++) {
//		roleCodes.push(arr3[i].code);
//		roleNames.push(arr3[i].realName);
//	}
	if(selectType!="0002") {
		if(roleCodes.length <= 0) {
			$.alert("请至少选择一个角色。");
			return false;
		}
	}
//	if(checkType=="multi") {
		var roleDTO =  {
				roleCode:roleCodes,
				roleName:roleNames,
				counts:counts,
				mark:$("#mark").val(),
				completeDate:$("#inputDate").val()
			}	
		return roleDTO
//	} else {
//		alert(roleCodes);
//		
//		return false;
//		if(roleCodes.length > 1) {
//			$.alert("只能选一个人。");
//			return false;
//		}
//		var roleDTO =  {
//			roleCode:roleCodes[0],
//			roleName:roleNames[0],
//			mark:$("#mark").val(),
//			completeDate:$("#inputDate").val()
//		}
//		
//		
//		return roleDTO;		
//	}
		
}

$("#inputDate").datetimepicker({
	showSecond: false, //显示秒
	timeFormat: 'HH:mm',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-mm-dd",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间'
});

$(function(){
	
	if(parent.wf_getMark) {
		$("#mark").val(parent.wf_getMark(operaterId));
	}
	if(parent.wf_getDate) {
		$("#inputDate").val(parent.wf_getDate(operaterId));
	} else {
		$("#inputDate").val(getTheDate(86400));
	}
	$("#roleCode").val(roleCodes.length==0?-1:roleCodes);
	if(selectType=="0002") {
		$("#roleDiv").addClass("hidden")
	} 
	if(markType=="0002") {
		$("#markDiv").addClass("hidden")
	}
	if(isShowDate=="0002") {
		$("#dateDiv").addClass("hidden")
	}
	if(selectType == "0003") {
		checkType="single";
	}
	if(checkType=="single") {
//		$("#orgTargetDiv").attr("conf","{showTitle:false,onQueryEnd:queryEnd,createDelOpp:false,showSelect:true,createBtn:false,selectText:'选择',selectType:'RADIO',autoLoad:false,searchBtn:false}")
	}
//	clickmedCommonTableToScan();
//	doQuery("orgTargetDiv");
	
})

function doDeleteContactUser() {
	var ids = clickmedTables.orgTargetDiv1.selectIds;
	if(ids.length <= 0) {
		$.alert("请至少选择一个。");
		return false;
	}
	var dto = {
			ids:ids
	}
	doJsonRequest("/contactUser/deleteContactUser", dto,function(data){
		if(data.result) {
			$.alert("删除联系人成功。");
			doQuery("orgTargetDiv1");
			doQuery("orgTargetDiv2");
		} else {
			$.alert("删除联系人出错。");
		}
	},{noFrame:true})	
}

function queryEnd() {
	if(clickmedTables.orgTargetDiv.allCount ==1) {
		if(checkType=="single") {
			$("#orgTargetDiv").find("td[name='check']").children().trigger("click");
		} else {
			$("#orgTargetDiv").find("td[name='check']").children().trigger("click");
		}
	}
	if(clickmedTables.orgTargetDiv.allCount > clickmedTables.orgTargetDiv.defaultPageSize) {
		
	} else {
		clickmedTables.orgTargetDiv.hideFoot();
	}
}

var zTree_Menu = null;
var checkType = "checkbox";
if(selectType == '0003') {
	checkType="radio"
}
var setting = {
		async:{
			autoParam:["id","type"],
			enable:true,
			contentType :"application/json",
			url:baseUrl+"/org/getUserByOrgTree"
		},
		check:{
			chkStyle:checkType,
			enable:true,
			radioType :"all"
		},
		view: {
			nameIsHTML: true,
			showIcon:false
		},
		callback:{
			beforeClick:function(id,node,flag){
				node.singleCheck = true;
				setting.checkNode(node);

			},
			beforeCheck:function(id,node){
				var parentNode = node.getParentNode();
				
				if(printType =="0002") {
					var num1 = "";
					var num2 = "";
					if("getDocFromLower" == businessType) {
						num1 = "2";
						num2 = "2";
					} else {
						num1 = "1"
						num2 = "3"
					}
					if(checkType=="radio" || checkType=="single") {
						$("input",$("#treeDemo")).parent().remove();
						var aObj = $("#" + node.tId+"_span");
						if(aObj.next("span").length <=0) {
							aObj.after("<span style='vertical-align:middle;font-size:15px'><label style='vertical-align:middle;font-size:15px'>&nbsp;&nbsp;打印</label><input type='text'style='width:30px;line-height:20px;vertical-align:middle;text-align:center;' class='theNumber' maxlength='3' value='"+(parentNode.id==5?num2:num1)+"'/><label style='vertical-align:middle;font-size:15px'>份</lable></span>");
						} 
						$(".theNumber").each(function(){
							$(this).attr("onkeyup","value=value.replace(/[^\\d]/g,'')");
							$(this).attr("onbeforepaste","clipboardData.setData('text',clipboardData.getData('text').replace(/[^\\d]/g,''))");
	
						});
					} else if(checkType=="checkbox" || checkType=="multi") {
						var aObj = $("#" + node.tId+"_span");
						if(!node.checked) {
							aObj.after("<span style='vertical-align:middle;font-size:15px'><label style='vertical-align:middle;font-size:15px'>&nbsp;&nbsp;打印</label><input type='text'style='width:30px;line-height:20px;vertical-align:middle;text-align:center;' class='theNumber' maxlength='3' value='"+(parentNode.id==5?num2:num1)+"'/><label style='vertical-align:middle;font-size:15px'>份</lable></span>");
						} else {
							aObj.next("span").remove();
						}
					}
				}
			}
		}

	};

function addHoverDom(treeId,treeNode){
	
	if(aObj.next("input").length>0) {
		aObj.next("input").removeClass("hidden")
	} else {
		aObj.append("<input type='text' width='20px'/>");
	}
	
}

function removeHoverDom(treeId,treeNode) {
	var aObj = $("#" + treeNode.tId+"_span");
	aObj.next().addClass("hidden")
}

var treeObj;
$(document).ready(function(){
		treeObj = $("#treeDemo");
		var dto = {
				roleCodes:roleCodes,
				mark:"0001"
		}
	 
		doJsonRequest("/workflow/initWorkFlowGroup",dto,function(data,status){
			if(data.result) {
				var data = data.data
				zTree_Menu = $.fn.zTree.init(treeObj, setting, data);

			} else {
				$.alert("获取组织机构出错。");
			}
		},function(){
	           $.alert("获取组织机构出错。");
	    },{showWaiting:true})		
});
