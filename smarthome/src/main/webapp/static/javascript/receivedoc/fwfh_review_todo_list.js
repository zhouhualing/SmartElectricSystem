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
	}else if(key == "receiveTitle"){
		return "<a style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")'>"+data.receiveTitle+"</a>";
	}
}

function goPage(data) {
	if(data.reportType=='5002'){
		window.location.href="new_xjlw_examine.html?id="+data.receiveId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId;
	}
	if(data.reportType=='5001'){
		window.location.href="new_sjlw_examine.html?id="+data.receiveId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId;
	}
	if(data.reportType=='5003'){
		window.location.href="../report/new_reqrep_examine.html?id="+data.receiveId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId;
	}
	if(data.reportType=='6001'){
		window.location.href="../telegram/new_sd_examine.html?id="+data.receiveId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId;
	}
}

var oppObj = [editObj];

