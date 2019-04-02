$(function(){
	getCurrenUserInfo(function(data){
		$("#userId").val(data.userId);
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
		if(data.theFlag == '1') {
			if(data.speed=="0001") {
				return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
			} else if(data.speed=="0002"){
				return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!</span>";
			} else if(data.speed=="0003"){
				return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!</span>";
			} else {
			}
			
		} else if(data.theFlag == '0'){
			if(data.speed=="0001") {
				return "<span style='color:red'>!!!</span>";
			} else if(data.speed=="0002"){
				return "<span style='color:red'>!!</span>";
			} else if(data.speed=="0003"){
				return "<span style='color:red'>!</span>";
			} else {
			}
		} 
}

function initFun(data, key) {
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,1000,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
}


function goPage(data) {
	window.location.href=clickmedBaseUrl + data.todoURL + "&appFlag=1";
	//+"&fromPage=szms" : 当前页面为市长秘书点击市长待批文件列表 处理按钮后跳转的,小纸条可编辑,其余情况小纸条不可编辑
	/*if(data.businessType=='0006' || data.businessType=='0007' || data.businessType=='0009' || data.businessType=='0010'){
		//政府发文、政府发函、办公厅发文，办公厅发函
		window.location.href="../senddoc/new_fwfh_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.businessType=='0008'){
		//政府发电报
		window.location.href="../telegram/new_fd_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.businessType=='0001' || data.businessType=='0002' || data.businessType=='0003' || data.businessType=='0004' || data.businessType=='0005'){
		//拟政府发文
		window.location.href="../senddoc/new_nfw_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.businessType=='0012'){
		//下级来文
		window.location.href="../receivedoc/new_xjlw_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.businessType=='0011'){
		//上级来文
		window.location.href="../receivedoc/new_sjlw_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.businessType=='0014'){
		//收电
		window.location.href="../telegram/new_sd_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}else if(data.businessType=='0015'){
		//委办局函文
		window.location.href="../letter/bureaus_letter_examine.html?id="+data.businessId+"&type="+data.reportType+"&fromPage=0001&mark=001&taskId="+data.taskId;
	}*/
}

var oppObj = [editObj];

