$(function(){
	//wf_showProcessOnTable("reportInfoQuery","查看流程图");
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	});
});
//var scanObj = {
//		text:"查看",
//		fun:function(data) {
//			window.location.href="";
//		}
//}
var editObj = {
		text:"处理",
		fun:function(data) {
			goPage(data);
		}
};

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				};
				doJsonRequest("/senddoc/deleteReport", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true});				
			},data:"1"});

		}
};

function initFlag(data) {
		
//	86 400 000
	if(data.flag == '1') {
//		return "<span style ='color:blue'>!</span>";
		if(data.urgency=="0001") {
			return "<img src='../../static/images/green.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else {
			return "<img src='../../static/images/green.png' style='vertical-align:baseline;'/>";
		}
		
	} else if(data.flag == '2'){
		if(data.urgency=="0001") {
			return "<img src='../../static/images/yellow.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else {
//		return "<span style ='color:red'>!</span>";
			return "<img src='../../static/images/yellow.png' style='vertical-align:baseline;'/>";
		}
	} else if(data.flag == '3'){
		if(data.urgency=="0001") {
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else {
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/>";
		}
	}
}

function initFun(data, key) {
	if(key == "reportTitle") {
		return "<a style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")'>"+data.reportTitle+"</a>";
	}
}

function goPage(data) {
	//+"&fromPage=szms" : 当前页面为市长秘书点击市长待批文件列表 处理按钮后跳转的,小纸条可编辑,其余情况小纸条不可编辑
	if(data.reportType=='5001'){
		//上级来文
		window.location.href="new_sjlw_examine.html?id="+data.receiveId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms";
	}else if(data.reportType=='5002'){
		//下级来文
		window.location.href="new_xjlw_examine.html?id="+data.receiveId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms";
	}
}

var oppObj = [editObj];

