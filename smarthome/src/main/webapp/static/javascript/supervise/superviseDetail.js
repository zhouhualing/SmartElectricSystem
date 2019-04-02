var id = getURLParam("id");
var title = getURLParam("title");
var businessName = getURLParam("businessName");
var deadLineTime = getURLParam("deadLineTime");
var userNames = getURLParam("userNames");
var businessType = getURLParam("businessType");
var businessId = getURLParam("businessId");




/**
 * document ready
 */
$(function(){
	if(businessId == null) {
		$("#businessTitle").val(title);
		$("#businessName").val(businessName);
		if(deadLineTime != "null") {
			$("#deadLineTime").val(new Date(parseInt(deadLineTime)).format("yyyy-MM-dd hh:mm"));
		} else {
			$("#deadLineDiv").addClass("hidden");
		}
		$("#userNames").val(userNames).trigger("onkeyup");
		$("#businessId").val(id);
		$("#businessType").val(businessType);
		doQuery("actionListSuperviseMessageQuery");
	} else {
		$("#superviseBtn").addClass("hidden");
		$("#superviseInfoDiv").addClass("hidden");
		var dto = {
				businessKey:businessId,
				businessType:businessType
		}
		doJsonRequest("/super/getSuperviseApprovalByTaskId",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#businessTitle").val(data.businessTitle);
				$("#businessName").val(data.businessName);
				if(data.deadLineTime != "null") {
					$("#deadLineTime").val(new Date(parseInt(data.deadLineTime)).format("yyyy-MM-dd hh:mm"));
				} else {
					$("#deadLineDiv").addClass("hidden");
				}
				$("#userNames").val(data.userNames).trigger("onkeyup");
				$("#businessId").val(data.businessId);
				$("#businessType").val(data.businessType);	
				doQuery("actionListSuperviseMessageQuery");
			}
		})
	}
	

})


function queryEnd1 () {
	clickmedTables.actionListSuperviseMessageQuery.hideFoot();
	if(clickmedTables.actionListSuperviseMessageQuery.getCount() == 0) {
		$("#hidDiv1").hide();
	}
}

function doSupervise() {
	if($("#userCode").val().length == 0) {
		$.alert("请选择被督办人。");
		return false;
	}
	if($("#message").val().length <= 0) {
		$.alert("请输入督办信息。");
		return false;	
	}
	var dto = {
			id:id,
			message:$("#message").val(),
			businessType:businessType,
			type:'0002',
			userCodes:$("#userCode").val(),
			userNames:$("#userName").val(),
			title:title
	}
	doJsonRequest("/report/doSuperviseActionList",dto,function(data){
		if(data.result) {
			doQuery("actionListSuperviseMessageQuery");
			$("#message").val("");
			$.alert("已督办。");
		}
	},{showWaiting:true})	
}



function goBack() {
	history.back();
//	window.location.href = "actionlist_me.html";
}

function initUserInfo(data,srcData) {
	$("#removeBtn1").html("修改");
	$("#userName").val(data.userName.join(","));
	$("#userCode").val(data.userCode.join(","));
	$("#message").focus();
}

function doSearchUser(_this) {
	var obj = {
	    title:'选择责任人',
	    height:"500px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?checkType=multi',
	    myCallBack:initUserInfo,
	    fun:true,
	    srcData:_this
	}
	new jqueryDialog(obj);
}