var selectType = getURLParam("selectType");
var markType = getURLParam("markType");
var roleCodes = getURLParam("roleCodes");
var checkType = getURLParam("checkType");
var isShowDate = getURLParam("isShowDate");
var operaterId = getURLParam("operaterId");
function doCallBack() {
	
		if($("#inputDate").val().length <= 0) {
			$.alert("请输入截止日期。");
			return false;
		}		
//	var arr1 = clickmedTables.orgTargetDiv1.selectObjs;
	var arr2 = new Array();
	if(checkType=="single") {
		arr2.push($.parseJSON(clickmedTables.orgTargetDiv.getCheckedRadioValue()));
	} else {
		arr2 = clickmedTables.orgTargetDiv.selectObjs;
	}
//	var arr3 = nowTree.getCheckedNodes(true);
	var roleCodes = new Array();
	var roleNames = new Array();
//	for(var i=0; i< arr1.length; i++) {
//		roleCodes.push(arr1[i].contactUser.roleCode);
//		roleNames.push(arr1[i].contactUser.roleName);
//	}
	for(var i=0; i< arr2.length; i++) {
		roleCodes.push(arr2[i].roleCode);
		roleNames.push(arr2[i].roleName);
	}
//	for(var i=0; i<arr3.length; i++) {
//		roleCodes.push(arr3[i].code);
//		roleNames.push(arr3[i].realName);
//	}
	if(roleCodes.length <= 0) {
		$.alert("请至少选择一个角色。");
		return false;
	}
//	if(checkType=="multi") {
		var roleDTO =  {
				roleCode:roleCodes,
				roleName:roleNames,
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

$("#inputDate").datepicker({
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
//	if(parent.wf_getMark) {
//		$("#mark").val(parent.wf_getMark(operaterId));
//	}
	if(parent.wf_getDate) {
		$("#inputDate").val(parent.wf_getDate(operaterId));
	}
	$("#roleCode").val(roleCodes.length==0?-1:roleCodes);
//	$("#inputDate").val(getTheDate(604800));
	doQuery("orgTargetDiv");
	
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
	if(clickmedTables.orgTargetDiv.allCount > clickmedTables.orgTargetDiv.defaultPageSize) {
		
	} else {
		clickmedTables.orgTargetDiv.hideFoot();
	}
}

//function doAddContactUser() {
//	var ids = clickmedTables.orgTargetDiv2.selectIds;
//	if(ids.length <= 0) {
//		$.alert("请至少选择一个。");
//		return false;
//	}
//	var dto = {
//			ids:ids
//	}
//	doJsonRequest("/contactUser/addContactUser", dto,function(data){
//		if(data.result) {
//			$.alert("添加联系人成功。");
//			doQuery("orgTargetDiv1");
//			doQuery("orgTargetDiv2");
//		} else {
//			$.alert("添加联系人出错。");
//		}
//	},{noFrame:true})
//} 



//var setting = {
//		async:{
//			autoParam:["id","type"],
//			enable:true,
//			contentType :"application/json",
//			url:baseUrl+"/org/getUserByOrgTree"
//		},
//		check:{
//			chkStyle:"checkbox",
//			enable:true,
//			radioType :"all"
//		},
//		view: {
//			nameIsHTML: true,
//			showIcon:false
//		},
//		callback:{
//			beforeClick:function(id,node,flag){
//				node.singleCheck = true;
//				nowTree.checkNode(node);
//			}
////			beforeCheck:function(id,node,flag){
////				node.singleCheck = false;
////				alert(node.singleCheck)
////			}
//		}
//
//	};

//	var nowTree = {};


//	$(document).ready(function(){
//		doJsonRequest("/org/getUserByOrgTree",null,function(data,status){
//			if(data.result) {
//				var data = data.data
//				nowTree = $.fn.zTree.init($("#treeDemo"), setting, data);					
//			} else {
//				$.alert("获取组织机构出错。");
//			}
//		},{showWaiting:true})
//		
//	});
