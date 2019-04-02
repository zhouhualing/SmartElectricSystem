//收文待办-->点击签批后的签批页面js
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

$("#approvalBtn").on("click",function(){
	approvalReport();
})

$("[name='showPlan']").on("change",function(){
	if($(this).val()=="0001") {
		$("#hidDiv").removeClass("hidden")
	} else {
		$("#hidDiv").addClass("hidden")
	}
})

function queryEnd () {
	clickmedTables.reportInfoQuery.hideFoot();
}

function queryEnd3 () {
	clickmedTables.actionListQuery.hideFoot();
}

/**
 * document ready
 */
$(function(){
		var dto = {
				id:id
		};
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

});


function approvalReport() {
	var approvalObj = $("input[name='approvalFlag']:checked");
	if(approvalObj.length == 0) {
		$.alert("请选择通过或拒绝。");
		return false;
	}
		var dto =  {
			id:id,
			workFlowDTO:{
				taskId:taskId,
				mark:$("#mark").val(),
				approvalFlag:approvalObj.val()
			}
		}
		
		var msg = "拒绝";
		if(approvalObj.val() == "0001")  {
			msg = "通过";
		}
		doJsonRequest("/receivedoc/approvalReceiveDoc",dto,function(data){
			if(data.result) {
				$.alert({
		    	    title:'提示信息',
		    	    msg:'已审批'+msg+"该报告。",
		    	    height:180,
		    	    confirmClick:"goSuccess"
		    	});
			} else {
				alert("审批出错。");
			}
		},{showWaiting:true});
}

function goSuccess() {
	if(fromPage == "pc") {
		//window.location.href="../receivesenddoc/rsdocIndex.html";
		window.history.back();
	} else {
		window.location.href="inbox_receives_list.html";
	}
	
}

function goBack() {
	window.history.back();
//	if(fromPage == "pc") {
//		window.location.href="../receivesenddoc/rsdocIndex.html";
//	} else {
//		window.history.back();
//	}
}

function doResize() {
	if($("frameset", window.parent.document).eq(1).attr("cols") == "225,*") {
		$("frameset", window.parent.document).eq(1).attr("cols","0,*");
	} else  if($("frameset", window.parent.document).eq(1).attr("cols") == "0,*") {
		$("frameset", window.parent.document).eq(1).attr("cols","225,*");
	}
}