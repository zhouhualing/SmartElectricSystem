var id =getURLParam("id");
var sendId = getURLParam("sendId");
var fromPage = getURLParam("fromPage");
var status = getURLParam("status");
$("#receiveId").val(id);
$("#businessId").val(id);

function queryEnd3 () {
	clickmedTables.actionListQuery.hideFoot();
}

function init() {
	if(fromPage == "0001") {
		$("ul").remove()
		$("#tab2").remove();
		$("#nextPage").remove();
	}
	
	if(status == "0003") {
		$("#sendDiv").removeClass("hidden");
		$("#applyDiv").addClass("hidden");
		$("#reApplyBtn").addClass("hidden");
	}
}
init();

function queryEnd () {
	clickmedTables.reportInfoQuery.hideFoot();
}

function queryEnd1 () {
	clickmedTables.reportInfoQuery1.hideFoot();
}

$("#businessKey").val(id);

$(".nav-tabs a:first").tab("show");

$("#nextPage").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
})

$("#nextPage1").on("click",function(){
	$(".nav-tabs a").eq(2).tab("show");
})

$("#beforePage1").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

$("#reApplyBtn").on("click",function(){
	doApplayReceive();
})

/**
 * document ready
 */
$(function(){
	if(id != null) {
		$("#backBtn").removeClass("hidden");
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
	}
	getCurrenUserInfo(function(data){
		$("#applyUserCode").val(data.userCode);
		doQuery("reportInfoQuery1");
	}) 
})

function initFollowUserInfo(data) {
	$("#reportUserName").val(data.userName);
	$("#reportUserCode").val(data.userCode);
}

function goBack() {
	window.history.back();
}

function doApplayReceive() {
	var dto = {
			sendId : sendId,
			message:$("#message").val()
	}
	doJsonRequest("/receivedoc/applyReceive",dto,function(data){
		if(data.result) {
			$.alert({
	    	    title:'提示信息',
	    	    msg:'回执成功。',
	    	    height:180,
	    	    confirmClick:"goSuccess"
	    	});
		} else {
			$.alert("回执失败。");
		}
	})
}

function goSuccess() {
	window.location.href=window.location.href;
}