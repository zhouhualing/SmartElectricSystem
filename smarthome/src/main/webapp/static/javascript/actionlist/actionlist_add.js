function doCallBack() {
	return true;
}

function doCancelCallBack() {
	return true;
}

$(function(){
	var id = $("#id", parent.document).val();
	$("#id").val((id.length==0)?-1:id)
//	doQuery();
	$("#reportDate").html(new Date().format("yyyy-MM-dd"))
	getCurrenUserInfo(function(data){
		$("#createUserName").html(data.userName);
		$("#createUserOrgName").html(data.orgName);
		$("#phoneNumber").html(data.phoneNumber)
	})
	
});

function doSearchOrg() {
	var obj = {
	    title:'选择跟件人',
	    height:"500px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?checkType=radio',
	    myCallBack:"initFollowUserInfo"
	}
	new jqueryDialog(obj);
}

function initFollowUserInfo(data) {
	$("#reportUserName").val(data.userName);
	$("#reportUserCode").val(data.userCode);
}

function addActionList() {
	parent.addTempReport("0001");
	var id = $("#id", parent.document).val();
	$("#id").val(id);
	var flag = true;
	if(valide("dataInputForm")) {
		var dto = {
			actionContent:$("#actionContent").val(),
			completeDate:$("#completeDate").val(),
			businessType:'0001',
			businessId:id
		}
		doJsonRequest("/actionList/saveActionList",dto,function(data){
			if(data.result) {
				$.alert("计划添加成功");
				$("#actionContent").val("");
				$("#completeDate").val("");
				doQuery();
			} else {
				alert("添加报告出错。");
			}
		},{showWaiting:true})
	}
}

$("#submitBtn").on("click",function(){
	addReport("0002");
} )

$("#tempSubmitBtn").on("click",function(){
	addReport("0001");
} )


$("#completeDate").datepicker({
	dateFormat:"yy-mm-dd",
});

//$("#completeDate").datetimepicker({
//	showSecond: true, //显示秒
//	timeFormat: 'HH:mm:ss',//格式化时间
//	stepHour: 1,//设置步长
//	stepMinute: 5,
//	stepSecond: 10,
//	dateFormat:"yy-mm-dd",
//	currentText:'现在',
//	closeText:'确定',
//	hourMax:'23',
//	hourText:'时',
//	minuteText:'分',
//	secondText:'秒',
//	timeText:'时间'
//});

function onChangePage(_this) {
	var value = $(_this).val();
	if(value=="0001") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").addClass("hidden");
		$("#proposedAdviceDiv").addClass("hidden");
		$("#text2").html("报告内容");
	}
	
	if(value=="0002") {
		$("#fromNumberDiv").removeClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
		$("#text1").html("来电单位");
		$("#text2").html("来电摘要");
	}
	
	if(value=="0003") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
		$("#text1").html("来文单位");
		$("#text2").html("来文摘要");
	}
}

function goSuccess() {
	window.location.href = "mecreate_reports_list.html";
}

function goBack() {
	window.location.href = "drafting_reports_list.html";
}