var id =getURLParam("id");
var fromPage = getURLParam("fromPage");
$("#reportId").val(id)

$("#businessId").val(id);

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

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
})

$("#nextPage1").on("click",function(){
	$(".nav-tabs a").eq(2).tab("show");
})

$("#beforePage1").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

/**
 * document ready
 */
$(function(){
	$("input[name='reportType']").on("change",function(){
		onChangePage(this);
	})
	if(id != null) {
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		}
		doJsonRequest("/report/getReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(data.status == "0006" || data.status == "0007") {
					$("#nextPage1").removeClass("hidden");
					$("#tab3Li").removeClass("hidden");
				}
				$("[name='reportType']").initRadio(data.reportType);
				onChangePage($("[name='reportType']:checked"));
				var value = $("[name='reportType']:checked").next("span").html();
				$("[name='reportType']").parent().empty().append("<span class='input-group-addon' ></label>起草方式：</span><input type='text' name='reportType' disabled class='form-control' id='reportType' value='"+value+"'/>");
				$("#businessType").val(data.businessType)
				$("#businessType").replaceWith("<input type='text' name='businessType' disabled class='form-control' id='businessType' value='"+$("#businessType option:selected").text()+"'/>");
				
				$("#fromNumber").val(data.fromNumber);
				$("#reportDate").html(new Date(data.reportDate).format("yyyy-MM-dd"));
				$("#completeDate").val(new Date(data.completeDate).format("yyyy-MM-dd hh:mm:ss"));
				$("#fromOrgName").val(data.fromOrgName);
				$("#reportTitle").val(data.reportTitle);
				$("#reportCode").html(data.reportCode);
				$("#reportSummary").val(data.reportSummary).trigger("keyup");
				$("#proposedAdvice").val(data.proposedAdvice).trigger("keyup");
				$("#reportUserName").val(data.reportUserName);
				$("#reportUserCode").val(data.reportUserCode);
				$("#reportUserCode").val(data.reportUserCode);
				$("#userCode").val(data.createUserCode);
				$("#createUserName").html(data.createUserName);
				$("#createUserOrgName").html(data.createUserOrgName);
				$("#phoneNumber").html(data.phoneNumber)
				doQuery("reportInfoQuery");
				doQuery("actionListQuery");
			} else {
				$.alert("获取信息失败。");
			}
		}) 		
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

function addReport(status) {
	var flag = true;
	if(status == '0002') {
		flag = valide("dataInputForm");
	}
	if(flag) {
		var dto = $("#dataInputForm").serialize();
		dto = dto+"&status="+status;
		doRequest("/report/addReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#id").val(data.id);
				if(status == "0001") {
					$.alert("暂存成功。");
				}
				if(status == "0002") {
					$.alert("提交成功。");
				}	
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

$("#reportType").on("change",function(){
	onChangePage(this);
})


function onChangePage(_this) {
	var value = $(_this).val();
	if(value=="0001") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").addClass("hidden");
		$("#proposedAdviceDiv").addClass("hidden");
	}
	
	if(value=="0002") {
		$("#fromNumberDiv").removeClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
	}
	
	if(value=="0003") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
	}
}

function goBack() {
	window.history.back();
}

var completeObj = {
		text:"完成",
		fun:function(data) {
			if(data.status == '0002') {
				$.alert("已经完成。");
				return false;
			}
			var dto = {
					id:data.id,
					status:'0002'
			}
			doJsonRequest("/actionList/modifyStatus",dto,function(data){
				if(data.result) {
					doQuery("actionListQuery");
				} else {
					$.alert("服务器内部错误，请联系管理员。");
				}
			})
		}
}


var oppObj = [completeObj];