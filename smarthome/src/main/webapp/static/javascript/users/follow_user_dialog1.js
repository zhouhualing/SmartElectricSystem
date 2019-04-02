var checkType = getURLParam("checkType");




function doCallBack() {
	

	var arr2 = new Array();
	if(checkType=="single") {
		
		arr2 = zTree_Menu.getCheckedNodes();
	} else {
		arr2 = zTree_Menu.getCheckedNodes()
	}

	var roleCodes = new Array();
	var roleNames = new Array();
	for(var i=0; i< arr2.length; i++) {
		if(arr2[i].level == 1) {
			roleCodes.push(arr2[i].code);
			roleNames.push(arr2[i].name);			
		}

	}
		var roleDTO =  {
				roleCode:roleCodes,
				roleName:roleNames
			}	
		return roleDTO
		
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


var zTree_Menu = null;
var checkType = "checkbox";

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
			}
//			beforeCheck:function(id,node,flag){
//				node.singleCheck = false;
//				alert(node.singleCheck)
//			}
		}

	};

$(document).ready(function(){

		var treeObj = $("#treeDemo");
		
		var dto = {
				roleCodes:"WF_ALL",
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
