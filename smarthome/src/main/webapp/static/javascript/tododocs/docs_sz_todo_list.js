$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	});
});
var editObj = {
		text:"处理",
		fun:function(data) {
			goPage(data);
		}
};


function initFlag(data) {
		
//	86 400 000
	if(data.flag == '1') {
//		return "<span style ='color:blue'>!</span>";
		if(data.urgency=="0001") {
			return "<img src='../../static/images/green.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else {
			return "<img src='../../static/images/green.png'/>";
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
	if(data.reportType=='0001' || data.reportType=='0002' || data.reportType=='0005' || data.reportType=='0006'){
		//政府发文、政府发函、办公厅发文，办公厅发函
		window.location.href="../senddoc/new_fwfh_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms&mark=002";
	}else if(data.reportType=='0003'){
		//政府发电报
		window.location.href="../telegram/new_fd_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms&mark=002";
	}else if(data.reportType=='0009'){
		//拟政府发文
		window.location.href="../senddoc/new_nfw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms&mark=002";
	}else if(data.reportType=='5002'){
		window.location.href="../receivedoc/new_xjlw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms&mark=002";
	}else if(data.reportType=='5001'){
		window.location.href="../receivedoc/new_sjlw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms&mark=002";
	}else if(data.reportType=='6001'){
		window.location.href="../telegram/new_sd_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId+"&ltPage=szms&mark=002";
	}
}

var oppObj = [editObj];

