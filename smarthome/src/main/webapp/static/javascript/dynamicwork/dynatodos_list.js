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

/*function initFlag(data) {
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
}*/

function initFun(data, key) {
	if(key == "dynaTitle") {
		var title = handleLongString(data.dynaTitle,135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.dynaTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	//市长督办单
	window.location.href="dynamicwork_examine.html?id="+data.dynaId+"&mark=005&fromPage=0001&taskId="+data.taskId;
}

var oppObj = [editObj];

