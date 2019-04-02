$(function(){
	//wf_showProcessOnTable("reportInfoQuery","查看流程图");
	getCurrenUserInfo(function(data){
		$("#roleCode").val(data.roleCodes)
		$("#createUserCode").val(data.userCode);
		$("#userId").val(data.userId);
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
			goPage(data);
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
	if(key == "todoTheme") {
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title="+data[key]+">"+data[key]+"</div>";
	}
}

function goPage(data) {
	var url = data.todoURL;
	var url1 = url.substring(0,url.indexOf("?")+1);
	var url2 = url.substring(url.indexOf("?")+1);
	window.location.href = "/cmcp"+url1+"fromPage=todolist&"+url2;
}

var oppObj = [handleObj];


