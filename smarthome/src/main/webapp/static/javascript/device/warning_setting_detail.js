if(getURLParam("classification") == 1) {
    $("#the_name").html("服务区域列表");
} else {
    $("#the_name").html("用电区域列表");
}

$("#id").val(getURLParam("id"));
$("#basicEdit").on("click",function(){
	$("#searchCompanyBtn").show();
	$("#level").select2("val",$("#levelVal").val());
});

$("#basicCancel").on("click",function(){
	$("#searchCompanyBtn").hide();
	$("#searchOrgBtn").hide();
});


//初始化信息
$(function(){
		var dto = {
				areaId:getURLParam("id")
		};
		doJsonRequest("/warningSetting/getAreaSetInfo",dto,function(data){
			if(data.result) {
				 var levels=["一级","二级"];
					$("#nameS").html(data.data.name);
					$("#maxLoadS").html(data.data.maxLoad);
					$("#avgLoadS").html(data.data.avgLoad);	
					$("#runDurationS").html(data.data.runDuration);	
					$("#gwJoinNumS").html(data.data.gwJoinNum);	
					$("#acJoinNumS").html(data.data.acJoinNum);	
					$("#acAvgTempS").html(data.data.acAvgTemp);
					$("#maxLoad").val(data.data.maxLoad);
					$("#avgLoad").val(data.data.avgLoad);	
					$("#runDuration").val(data.data.runDuration);	
					$("#gwJoinNum").val(data.data.gwJoinNum);	
					$("#acJoinNum").val(data.data.acJoinNum);	
					$("#acAvgTemp").val(data.data.acAvgTemp);
					$("#areaId").val(data.data.areaId);
					$("#id").val(data.data.id);
					$("#levelS").html(levels[data.data.level]);
					$("#levelVal").val(data.data.level);
				} 
			else {
			}
		},{showWaiting:true});
});

//validate
$("#inputForm").validate({
	submitHandler : function(form) {
		finishEditText($(".edit_complete")[0],'basic','/user/editUser',function(){
			$("#searchCompanyBtn").hide();
			$("#searchOrgBtn").hide();
		});
	}
});

function doEdit() {
	
	//方式一
	//$("#inputForm").submit();
	//方式二
	if(!$("#inputForm").valid()){
	$.alert("请填入正确信息");
		return false;
	}
	var data = $("#inputForm").serialize();
	doRequest("/warningSetting/doSaveAreaSet", data, function(data) {
		window.location.href = "../device/warning_setting_detail.html?id="+data.data.areaId; 
	},{showWaiting:true});
	
}

function doDelete() {
	var wid = $("#id").val();
	var dto = {
			id:wid
	};
	$.confirm({
        title: '提示信息',
        msg: '确定要删除么？',
        confirmClick: function(){
        	doRequest("/warningSetting/deleteAreaSet",dto,function(data){
        		if(data.result) {
        			history.go(-1);
        		} else {
        			
        		}
        	}, {showWaiting: true, successInfo: '删除成功', failtureInfo: '删除失败'})
        },
    });
}

function searchCompany() {
	var nowCompanyId = $("#companyId").val();
	C_doOrgSelect(function(data){
		$("#companyName").val(data.name);
		$("#companyId").val(data.id);
		if(nowCompanyId !=  $("#companyId").val()) {
			$("#searchOrgBtn").show();
			$("#orgName").val(null);
			$("#orgId").val(null);	
		}
	});
}

function searchOrg() {
	C_doOrgSelect({
		params:{pId:$("#companyId").val()},
		myCallBack:function(data){
			$("#orgName").val(data.name);
			$("#orgId").val(data.id);			
		}
	});
}