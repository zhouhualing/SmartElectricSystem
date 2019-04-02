/**
 * 收文分发页面
 */

var id =getURLParam("id");
var fromPage = getURLParam("fromPage");
$("#businessKey").val(id);
$("#receiveId").val(id);

$("#businessId").val(id);

function queryEnd3 () {
	clickmedTables.actionListQuery.hideFoot();
}


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

$("#sendUserBtn").on("click",function(){
	doSearchOrg();
})

$("#sendBtn").on("click",function(){
	doSendReceive();
})

function queryEnd () {
	clickmedTables.reportInfoQuery.hideFoot();
}

function queryEnd1 () {
	clickmedTables.reportInfoQuery1.hideFoot();
}

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
		});
	}

})


function doSearchOrg() {
	var obj = {
	    title:'选择分发人',
	    height:"500px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?selectType=multi',
	    myCallBack:"initFollowUserInfo"
	}
	new jqueryDialog(obj);
}

function initFollowUserInfo(data) {
	var userCode = data.userCode.join(",");
	var userName = data.userName.join(",");
	$("#sendUserName").val(userName);
	$("#sendUserCode").val(userCode);
	sendReceive();
}

function sendReceive() {
		var dto = {
				id:id,
				sendUserCode:$("#sendUserCode").val()
		}
		doJsonRequest("/receivedoc/sendReceive",dto,function(data){
			doQuery("reportInfoQuery1");
			if(data.result) {
				var data = data.data;
/*				$.alert("分发完成");
				window.location.href="mecreate_reports_list.html";*/
			} else {
				$.alert("添加报告出错。");
			}
		},{showWaiting:true})
}

function doSendReceive() {
	var dto = {
			id:id
	}
	doJsonRequest("/receivedoc/confirmSendReceive",dto,function(data){
		doQuery("reportInfoQuery1");
		if(data.result) {
			var data = data.data;
			$.alert({
	    	    title:'提示信息',
	    	    msg:'分发成功。',
	    	    height:180,
	    	    confirmClick:"goSuccess"
	    	});
			
		} else {
			$.alert("添加报告出错。");
		}
	},{showWaiting:true})
}

function goSuccess() {
	window.location.href=window.location.href;
//	if(fromPage == "0001") {
//		window.location.href="inbox_reports_list.html";
//	} else {
//		window.location.href="mecreate_reports_list.html";
//	}
	
}

$("#submitBtn").on("click",function(){
	addReport("0002");
} )

$("#tempSubmitBtn").on("click",function(){
	addReport("0001");
} )

function goBack() {
	window.history.back();
//	if(fromPage == "0001") {
//		window.location.href="inbox_reports_list.html";
//	} else {
//		window.history.back();
//	}
}

