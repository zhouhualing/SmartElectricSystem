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

function initFlag(data) {
		if(data.flag == '1') {
			if(data.urgency=="0001") {
				return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
			} else if(data.urgency=="0002"){
				return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!</span>";
			} else if(data.urgency=="0003"){
				return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!</span>";
			} else {
			}
			
		} else if(data.flag == '0'){
			if(data.urgency=="0001") {
				return "<span style='color:red'>!!!</span>";
			} else if(data.urgency=="0002"){
				return "<span style='color:red'>!!</span>";
			} else if(data.urgency=="0003"){
				return "<span style='color:red'>!</span>";
			} else {
			}
		} 
}

function initFun(data, key) {
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,35,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reportTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	//+"&fromPage=szms" : 当前页面为市长秘书点击市长待批文件列表 处理按钮后跳转的,小纸条可编辑,其余情况小纸条不可编辑
	if(data.reportType=='0001' || data.reportType=='0002' || data.reportType=='0005' || data.reportType=='0006'){
		//政府发文、政府发函、办公厅发文，办公厅发函
		window.location.href="../senddoc/new_fwfh_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.reportType=='0003'){
		//政府发电报
		window.location.href="../telegram/new_fd_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.reportType=='0009'){
		//拟政府发文
		window.location.href="../senddoc/new_nfw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.reportType=='5002'){
		window.location.href="../receivedoc/new_xjlw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.reportType=='5001'){
		window.location.href="../receivedoc/new_sjlw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.reportType=='6001'){
		window.location.href="../telegram/new_sd_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.reportType=='7001'){
		window.location.href="../letter/bureaus_letter_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}
}

var oppObj = [editObj];

