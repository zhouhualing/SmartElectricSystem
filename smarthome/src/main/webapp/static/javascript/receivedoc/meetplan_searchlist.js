$(function(){
	//wf_showProcessOnTable("reportInfoQuery","查看流程图");
	getCurrenUserInfo(function(data){
		$("#roleCode").val(data.roleCodes)
		$("#userCode").val(data.userCode);
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
		text:"查看",
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
	if(key == "title") {
		var title = handleLongString(data[key],135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href = "meet_plan_show.html?id="+data.businessKey;
}

var oppObj = [handleObj];


