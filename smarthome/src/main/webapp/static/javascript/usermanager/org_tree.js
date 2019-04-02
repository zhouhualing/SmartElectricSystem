var nowTree = {};
var checkType = "radio";
var setting = {
	async:{
		autoParam:["id","checked","singleCheck"],
		enable:true,
		contentType :"application/json",
		url:baseUrl+"/org/getOrgOrgTree"
	},
	check:{
		chkStyle:checkType||"checkbox",
		enable:true,
		radioType :"all"
	},
	view: {
		nameIsHTML: true,
		showIcon:false
	},
	callback:{
		beforeCheck:function(id,node,flag){
			$.dialog.data('orgCode',node.code);
			$.dialog.data('orgName',node.realName);
		}
	}
};

$(document).ready(function(){
	doJsonRequest("/org/getTheTopLevelOrgTree",null,function(data,status){
		if(data.result) {
			var data = data.data;
			nowTree = $.fn.zTree.init($("#treeDemo"), setting, data);	
			var data = nowTree.getCheckedNodes();
		} else {
			$.alert("获取组织机构出错。");
		}
	},{showWaiting:false});
});