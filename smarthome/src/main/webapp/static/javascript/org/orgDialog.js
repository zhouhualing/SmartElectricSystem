var checkType = "radio";
if(getURLParam("checkType") != null) {
	checkType = getURLParam("checkType");
}

var setting = {
	async:{
		autoParam:["id=pId","checked","singleCheck"],
		enable:true,
		contentType :"application/json",
		url:baseUrl+"/org/getOrgTree"
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
//		beforeClick:function(id,node,flag){
//			node.singleCheck = true;
//			setting.checkNode(node);
//		},
//		beforeCheck:function(id,node,flag){
//			node.singleCheck = false;
//		}
	}

};

var nowTree = {};


$(document).ready(function(){
	doJsonRequest("/org/getOrgTree",{},function(data,status){
		if(data.result) {
			var data = data.data;
			nowTree = $.fn.zTree.init($("#treeDemo"), setting, data);					
		} else {
			$.alert("获取组织机构出错。");
		}
	},{showWaiting:true})
	
});

function doCallBack() {
	if(checkType == "radio") {
		var arr = nowTree.getCheckedNodes();
		if(arr.length <=0) {
			$.alert("请选择");
			return false;
		}
		var obj = {
				id:arr[0].id,
				name:arr[0].name,
				code:arr[0].code
		}
		return obj;
	} else {
		var nodeArr = nowTree.getNodesByParam("check_Child_State","1");
		var codes = new Array();
		var names = new Array();
		var singleCodes = new Array();
		var singleNames = new Array();
		var levelObj = {};
		if(nodeArr.length > 0) {
			for(var i=0; i<nodeArr.length; i++) {
				if(levelObj[nodeArr[i].level] == undefined) {
					var tempArr = new Array();
					tempArr.push(nodeArr[i].tId)
					levelObj[nodeArr[i].level] = tempArr;
					
				} else {
					levelObj[nodeArr[i].level].push(nodeArr[i].tId);
				}
			}
			for(var key in levelObj) {
				for(var i=0;i<levelObj[key].length;i++) {
					var nodes = nowTree.getNodesByParam("parentTId",levelObj[key][i]);
					for(var j=0; j<nodes.length;j++) {
						if(nodes[j].checked) {
							if($.inArray(nodes[j].tId,levelObj[parseInt(key)+1])==-1) {
								if(nodes[j].singleCheck) {
									singleCodes.push(nodes[j].code);
									singleNames.push(nodes[j].realName);								
								} else {
									codes.push(nodes[j].code);
									names.push(nodes[j].realName);
								}
							}
						}
					}			
				}
			}
		} else {
			var nowArr = nowTree.getNodesByParam("level","0");
			for(var i=0; i<nowArr.length; i++) {
				if(nowArr[i].checked) {
					codes.push(nowArr[i].code);
					names.push(nowArr[i].realName);
				}			
			}
		}
		
		if((codes.length <=0)&&(singleCodes.length <=0)) {
			$.alert("请选择")
			return false;
		}
		
		var obj = {
			codes:codes,
			names:names,
			singleCodes:singleCodes,
			singleNames:singleNames
		}
		return obj;
	}
}