/**
 * init
 */
$(function(){
	//初始化流程按钮
	var dto = {
			taskId:getURLParam("taskId");
	}
	wf_getOperator(dto,function(data){
		if("usertask1"==data.userTask) {
			$("#fileDiv").addClass("hidden")
			var obj = {
					id:getURLParam("id")
				}
			doJsonRequest("/meet/meetcontentlib/getInfo", obj,function(data){
				if(data.result) {
					 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data.data});
					 $('#dataInputForm').removeClass('fileupload-processing');
					 $(".hideTd").hide();
				} else {
					$.alert("获取信息出错。");
				}
			})
		}
	})
	var obj = {
		id:getURLParam("id")
	}
	doJsonRequest("/meet/meetcontentlib/getInfo", obj,function(data){
		if(data.result) {
			$("#meetContent").val(data.data.meetContent);
		} else {
			$.alert("获取信息出错。");
		}
	})
	$("#businessKey").val(getURLParam("id"));
	doQueryWF("reportInfo","approvalDiv");
	
    'use strict';

    // Initialize the jQuery File Upload widget:
    $('#dataInputForm').fileupload({
        disableImageResize: false,
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: '/cmcp/attachment/upload'
    });

    // Enable iframe cross-domain access via redirect option:
    $('#dataInputForm').fileupload('option', {
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
            maxFileSize: attachment_maxSize,
            acceptFileTypes:attachment_regixType
    });
})

/**
 * doValide
 */
function wf_beforeValid() {
	$("#dataInputForm").attr("validate","");
	doValideScan();
	if(!valide("dataInputForm")) {
		return false;
	} else {
		return true;
	}
}

/**
 * startWorkflow data fun
 */
function getData() {
	var dto = {
			id:getURLParam("id"),
			meetContent:$("#meetContent").val(),
			status:'0002'
	}
	return dto;
}

/**
 * startWorkflow data fun
 */
function getDataA() {
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});
	var dto = {
			id:getURLParam("id"),
			attachmentDTOs:attachmentDTOs
	}
	return dto;
}

/**
 * completeFun
 */
function getCData() {
	var dto = {
			id:getURLParam("id"),
			meetContent:$("#meetContent").val(),
			status:"0004"
	}
	return dto;
}

/**
 * temp save object with status 0001
 */
function tempSubmitBtn() {
	valideClean("dataInputForm");
	var dto = {
			id:getURLParam("id"),
			meetContent:$("#meetContent").val(),
			status:'0001'
	}
	doJsonRequest("/meet/meetcontentlib/doSave",dto, function(data){
		if(data.result) {
			$("#id").val(data.data.id);
			$.alert("暂存成功。")
		} else {
			$.alert("暂存失败。")
		}
	},{showWaiting:true})
}

/**
 * save object with status 0004
 */
function submitBtn() {
	$("#dataInputForm").attr("validate","");
	doValideScan();
	if(!valide("dataInputForm")) {
		return false;
	}
	var dto = {
			id:getURLParam("id"),
			meetContent:$("#meetContent").val(),
			status:'0004'
	}
	doJsonRequest("/meet/meetcontentlib/doSave",dto, function(data){
		if(data.result) {
			$("#id").val(data.data.id);
			$.alert("创建成功。",function(){
				goSkip();
			})
		} else {
			$.alert("创建失败。")
		}
	},{showWaiting:true})
}

/**
 * do url skip
 */
function goSkip() {
	window.location.href = "meet_content_lib_list.html";
}

/**
 * go Success page
 * @param data
 */
function goSuccess(data) {
	var flagStr = "&flag="+4;
	if(data.operaterId == "flow3") {
		flagStr = "&flag="+1;
	}
	if(data.operaterId == "flow2") {
		flagStr = "";
	}
	var roleName = data.assignRoleName;
	if(getURLParam("fromPage") =="pc") {
		window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0001&btnSource=pc"+flagStr;
	} else {
		window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0001&btnSource=app"+flagStr;
	}
	
}


