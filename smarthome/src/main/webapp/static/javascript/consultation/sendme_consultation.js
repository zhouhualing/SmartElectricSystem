$(function(){
	//wf_showProcessOnTable("reportInfoQuery","查看流程图");
	getCurrenUserInfo(function(data){
		$("#roleCode").val(data.roleCodes)
		$("#createUserCode").val(data.userCode);
//		doJsonRequest("/consultation/getCanSeeId",null,function(data){
//			if(data.result) {
//				$("#id").val(data.data);
//				if(data.data.length <=0) {
//					$("#id").val("-1")
//				}
				doQuery();
//			} else {
//				$.alert("获取信息失败。")
//			}
//		})
		
	});
});
var handleObj = {
		text:"处理",
		fun:function(data) {
			var url = data.todoURL;
			var url1 = url.substring(0,url.indexOf("?")+1);
			var url2 = url.substring(url.indexOf("?")+1);
			window.location.href = "/cmcp"+url1+"fromPage=todolist&"+url2;
		}
}


function initFlag(data) {
		
//	86 400 000
	if(data.flag == '1') {
		
		if(data.urgency=="0001") {
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else if (data.urgency=="0002"){
			return "<span style='color:red'>!!</span>";
		} else if(data.urgency=="0003") {
			return "<span style='color:red'>!</span>";
		} else {
			return "";
		}
		
	} else {
		if(data.urgency=="0001") {
			return "<span style='color:red'>!!!</span>";
		} else if (data.urgency=="0002"){
			return "<span style='color:red'>!!</span>";
		} else if(data.urgency=="0003") {
			return "<span style='color:red'>!</span>";
		} else {
			return "";
		}
	} 
}

function initFun(data, key) {
	if(key == "title") {
		return "<a style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")'>"+data.title+"</a>";
	}
}

function goPage(data) {
	if(data.reportType=='0001' || data.reportType=='0002' || data.reportType=='0005' || data.reportType=='0006'){
		window.location.href="new_fwfh_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId;
	}else if(data.reportType=='0003'){
		
	}else if(data.reportType=='0009'){
		window.location.href="new_nfw_examine.html?id="+data.reportId+"&type="+data.reportType+"&fromPage=0001&taskId="+data.taskId;
	}
}

var oppObj = [handleObj];


function goPage(data) {
	window.location.href="show_consultation.html?id="+data.id;
}