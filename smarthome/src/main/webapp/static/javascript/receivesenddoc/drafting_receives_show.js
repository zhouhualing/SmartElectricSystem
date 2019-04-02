var id =getURLParam("id");
var taskId =getURLParam("taskId");
/* 通过登录用户角色判断上下级来文
 * @report_type
 * 0001 上级来文
 * 0002 下级来文
 */
var report_type="0001";
$(function(){
	if(report_type==="0001"){
		$("#hq").css("display","none");
	}
})
var fromPage = getURLParam("fromPage");
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

$("#nextPage2").on("click",function(){
	$(".nav-tabs a").eq(2).tab("show")
})

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
})

$("#nextPage1").on("click",function(){
	$(".nav-tabs a").eq(3).tab("show");
})

$("#beforePage1").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

$("#beforePage2").on("click",function(){
	$(".nav-tabs a").eq(2).tab("show");
})

/**
 * document ready
 */
$(function(){
	//初始化流程按钮
	var dto = {
			taskId:taskId
	}
	wf_getOperator(dto,function(data){
	});
	if(id != null) {
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				 $("#fileDiv").hide()
				 $(".hideTd").hide()
				if(data.status == "0006" || data.status == "0007") {
					$("#nextPage1").removeClass("hidden");
					$("#tab3Li").removeClass("hidden");
				}
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
				$("#createUserName").val(data.createUserName);
				$("#createUserOrgName").val(data.createUserOrgName);
				$("#phoneNumber").val(data.phoneNumber);
				$("#zmzj").val(data.zmzj);
				$("#huiqian").html(data.zmzj);
				$("#cb_time").val(new Date().format("yyyy-MM-dd hh:mm:ss")),
				$("#createUserCode").val(data.createUserCode)
				doQuery("reportInfoQuery");
			} else {
				$.alert("获取信息失败。");
			}
		});	
	}

})


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


function goBack() {
	window.history.back();
}