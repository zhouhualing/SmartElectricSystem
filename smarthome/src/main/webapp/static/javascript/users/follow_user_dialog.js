var selectType = getURLParam("checkType");

function doCallBack() {
	var arr1 = clickmedTables.orgTargetDiv1.selectObjs;
	var arr2 = clickmedTables.orgTargetDiv2.selectObjs;
	var arr3 = nowTree.getCheckedNodes(true);
	

	var userCodes = new Array();
	var userNames = new Array();
	for(var i=0; i< arr1.length; i++) {
		userCodes.push(arr1[i].contactUser.userCode);
		userNames.push(arr1[i].contactUser.userName);
	}
	for(var i=0; i< arr2.length; i++) {
		userCodes.push(arr2[i].userCode);
		userNames.push(arr2[i].userName);
	}
	for(var i=0; i<arr3.length; i++) {
		userCodes.push(arr3[i].code);
		userNames.push(arr3[i].realName);
	}
	if(userCodes.length <= 0) {
		$.alert("请至少选择一个人。");
		return false;
	}
	if(selectType=="multi") {
		
		var userDTO =  {
				userCode:userCodes,
				userName:userNames
			}		
		return userDTO
	} else {
		
		if(userCodes.length > 1) {
			$.alert("只能选一个人。");
			return false;
		}
		var userDTO =  {
			userCode:userCodes[0],
			userName:userNames[0]
		}
		return userDTO;		
	}
}

$(function(){
	$(".nav-tabs a").eq(2).trigger("click");
	$(".nav-tabs a").eq(1).trigger("click");
	doQuery("orgTargetDiv1");
	doQuery("orgTargetDiv2");
	
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

function doAddContactUser() {
	var ids = clickmedTables.orgTargetDiv2.selectIds;
	if(ids.length <= 0) {
		$.alert("请至少选择一个。");
		return false;
	}
	var dto = {
			ids:ids
	}
	doJsonRequest("/contactUser/addContactUser", dto,function(data){
		if(data.result) {
			$.alert("添加联系人成功。");
			doQuery("orgTargetDiv1");
			doQuery("orgTargetDiv2");
		} else {
			$.alert("添加联系人出错。");
		}
	},{noFrame:true})
} 



var setting = {
		async:{
			autoParam:["id","type"],
			enable:true,
			contentType :"application/json",
			url:baseUrl+"/org/getUserByOrgTree"
		},
		check:{
			chkStyle:"checkbox",
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
				nowTree.checkNode(node);
			}
//			beforeCheck:function(id,node,flag){
//				node.singleCheck = false;
//				alert(node.singleCheck)
//			}
		}

	};

	var nowTree = {};


	$(document).ready(function(){
		doJsonRequest("/org/getUserByOrgTree",null,function(data,status){
			if(data.result) {
				var data = data.data
				nowTree = $.fn.zTree.init($("#treeDemo"), setting, data);					
			} else {
				$.alert("获取组织机构出错。");
			}
		},{showWaiting:true})
		
	});
