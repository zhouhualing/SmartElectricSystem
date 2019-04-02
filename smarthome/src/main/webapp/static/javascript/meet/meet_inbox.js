$(function(){
	getCurrenUserInfo(function(data){
		$("#assignUserId").val(data.userId);
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
	var flag = '0';
	if((new Date().getTime() - data.theDeadLineTime) > 0){
		flag = '1';
	}
	if(flag == '1') {
		if(data.urgency=="0001") {
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else if(data.urgency=="0002"){
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!</span>";
		} else if(data.urgency=="0003"){
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!</span>";
		} else {
			
		}
		
	} else if(flag == '0'){
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
		var title = handleLongString(data.reportTitle,135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reportTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	if(data.businessType == "0016") {
		window.location.href = "meet_content_lib_approval.html?id="+data.businessId+"&taskId="+data.taskId;
	} else if(data.businessType == "0020") {
		window.location.href = "meet_plan_approval.html?id="+data.businessId+"&taskId="+data.taskId;
	} else if(data.businessType == "0021") {
		window.location.href = "meet_tell_approval.html?id="+data.businessId+"&taskId="+data.taskId;
	} else if(data.businessType == "0023") {
		window.location.href = "meet_mark_approval.html?id="+data.businessId+"&taskId="+data.taskId;
	}
}

var oppObj = [editObj];

