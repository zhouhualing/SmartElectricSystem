/**
 * 跟件人代办列表点击办理页面
 */

var id =getURLParam("id");
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
$("#businessKey").val(id);
$("#businessId").val(id);

$(".nav-tabs a:first").tab("show");

$("#nextPage").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
})

$("#submitBtn").on("click",function(){
	submitReceive();
})

function queryEnd3 () {
	clickmedTables.actionListQuery.hideFoot();
}

$("[name='showPlan']").on("change",function(){
	if($(this).val()=="0001") {
		$("#hidDiv").removeClass("hidden")
	} else {
		$("#hidDiv").addClass("hidden")
	}
})

/**
 * document ready
 */
$(function(){
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#receiveDate").html(new Date(data.receiveDate).format("yyyy-MM-dd"));
				$("#receiveCode").html(data.receiveCode);
				$("#receiveTitle").val(data.receiveTitle);
				$("#docCameOrgan").val(data.docCameOrgan);
				$("#docCameDate").val(new Date(data.docCameDate).format("yyyy-MM-dd"));
				$("#docCameNum").val(data.docCameNum);
				$("#docCameCode").val(data.docCameCode);
				$("#attachments").val();
				$("#completeDate").val(new Date(data.completeDate).format("yyyy-MM-dd hh:mm:ss"));
				$("#docCameSummary").val(data.docCameSummary).trigger("keyup");
				$("#remark").val(data.remark).trigger("keyup");
				$("#reportUserName").val(data.reportUserName);
				$("#reportUserCode").val(data.reportUserCode);
				$("#createUserName").html(data.createUserName);
				$("#createUserOrgName").html(data.createUserOrgName);
				$("#phoneNumber").html(data.phoneNumber);
				doQuery("reportInfoQuery");
			} else {
				$.alert("获取信息失败。");
			}
		}) 		

})

function queryEnd () {
	clickmedTables.reportInfoQuery.hideFoot();
}

function submitReceive() {
	var sendFlag = $("input[name='resendFlag']:checked");
	if(sendFlag.length == 0) {
		$.alert("请选择重发、完成、作废。");
		return false;
	}
	if(sendFlag.val() == "0001") {
		if($("#reportUserCode").val().length <= 0) {
			$.alert("请选择签批人。");
			return false;
		}
	}
		var dto =  {
			id:id,
			reportUserCode:$("#reportUserCode").val(),
			completeDate:$("#completeDate1").val(),
			workFlowDTO:{
				taskId:taskId,
				mark:$("#mark").val(),
				reSendFlag:sendFlag.val()
			}
		}
		var msg = "报告已上报给："+$("#reportUserName").val()+"。";
		if(sendFlag.val() == "0002") {
			msg = "该报告已完成。"
		}
		if(sendFlag.val() == "0003") {
			msg = "该报告已作废。"
		}		
		doJsonRequest("/receivedoc/reSendReceive",dto,function(data){
			if(data.result) {
				if(sendFlag.val() == '0001') {
					$.alert({
			    	    title:'提示信息',
			    	    msg:msg,
			    	    height:180,
			    	    confirmClick:"goSuccess"
			    	});
				} else {
					$.confirm({
			    	    title:'提示信息',
			    	    msg:"是否开始分发?",
			    	    height:180,
			    	    confirmClick:"goSend",
			    	    cancelClick:"goSuccess"
			    	});					
				}
			} else {
				alert("审批出错。");
			}
		},{showWaiting:true})
}

function goSuccess() {
	if(fromPage == "pc") {
		//window.location.href="../receivesenddoc/rsdocIndex.html";
		window.history.back();
	} else {
		window.location.href="inbox_receives_melist.html";
	}

}

function goSend() {
	window.location.href='send_receives.html?fromPage=0001&id='+id;
//	if(fromPage == "pc") {
//		$.alert("请在收文的分发功能分发。");
//		//window.location.href="../receivesenddoc/rsdocIndex.html";
//		window.history.back();
//	} else{
//		window.location.href='send_reports.html?fromPage=0001&id='+id;
//	}
}

function goBack() {
	window.history.back();
//	if(fromPage == "pc") {
//		window.location.href="../receivesenddoc/rsdocIndex.html";
//	} else {
//		window.history.back();
//	}
}

function doSearchOrg() {
	var obj = {
	    title:'选择签批人',
	    height:"500px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?checkType=radio',
	    myCallBack:"initFollowUserInfo"
	}
	new jqueryDialog(obj);
}
$("#completeDate1").datetimepicker({
	showSecond: true, //显示秒
	timeFormat: 'HH:mm:ss',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-mm-dd",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间'
});

function initFollowUserInfo(data) {
	$("#reportUserName").val(data.userName);
	$("#reportUserCode").val(data.userCode);
}

function doChange(_this) {
	if($(_this).val() == "0001") {
		$("#hideDiv").removeClass("hidden");
		$("#hideDiv1").removeClass("hidden");
		
	} else {
		$("#hideDiv").addClass("hidden");
		$("#hideDiv1").addClass("hidden");
	}
}