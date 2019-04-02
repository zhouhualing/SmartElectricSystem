var zTree_Menu = null;
var checkType = "radio";


function doCallBack() {
	return zTree_Menu.getCheckedNodes();
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
			beforeClick:function(id,node,flag){


			},
			
			beforeCheck:function(id,node){

			}
		}

	};



var treeObj;
$(document).ready(function(){
	var classification = 1;
	/**
	 * 处理url参数
	 */
	if (getURLParam("classification") != null) {
		classification = getURLParam("classification");
		//doReQuery(classification)
	}
	treeObj = $("#treeDemo");
		var dto = {"classification":classification};
		doJsonRequest("/area/getFirstLevelArea",dto,function(data,status){
			if(data.result) {
				//alert($.toJSON(data.data))
				zTree_Menu = $.fn.zTree.init(treeObj, setting, data.data);
			} else {
				$.alert("获取顶级区域出错。");
			}
		});

});
