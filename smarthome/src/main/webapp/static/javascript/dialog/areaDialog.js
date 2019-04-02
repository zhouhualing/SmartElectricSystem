var checkType = "radio";
if(getURLParam("checkType") != null) {
	checkType = getURLParam("checkType");
}

var setting = {
	async:{
		autoParam:["id","classification"],
		enable:true,
		contentType :"application/json",
		url:"/spms/area/findChildrenByParent"
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
	var classification = 1;
	/**
	 * 处理url参数
	 */
	if (getURLParam("classification") != null) {
		classification = getURLParam("classification");
	}
	treeObj = $("#treeDemo");
	var dto = {"classification":classification};
	var url = "/area/getFirstLevelArea";
	

	doJsonRequest(url,dto,function(data,status){
		if(data.result) {
			nowTree = $.fn.zTree.init(treeObj, setting, data.data);
			var nodes = nowTree.getNodes();
			nowTree.expandNode(nodes[0],true);
		} else {
			$.alert("获取顶级区域出错。");
		}
	});
	
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
				code:arr[0].code,
				parentIds:arr[0].parentIds
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