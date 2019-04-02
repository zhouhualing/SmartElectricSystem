$(function(){
	if(getURLParam("fromPage") == "0001") {
		$("#submit").addClass("hidden");
		$("#edit").removeClass("hidden");
	}
	if(getURLParam("id") != null) {
		var obj = {
				id:getURLParam("id")
			}
		$("#id").val(getURLParam("id"));
		doJsonRequest("/meet/meetcontentlib/getInfo", obj,function(data){
			if(data.result) {
				$("#meetContent").val(data.data.meetContent);
				$("#meetContent").trigger("keyup")
				$("#contentType").val(data.data.contentType)
                var systemAttachments = data.data.meetContentDocDTOs;
                for(var i=0; i<systemAttachments.length; i++) {
                    doInitSystemAttachment(systemAttachments[i]);
                }
				UM.getEditor('myEditor').setContent(data.data.meetContentDetail);
				 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data.data});
				 $('#dataInputForm').removeClass('fileupload-processing');
			} else {
				$.alert("获取信息失败。")
			}
		});
	}
	
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
	var flag = UM.getEditor('myEditor').hasContents();
	if($("#meetContent").val().length <= 0) {
		$.alert("请输入议题标题");
		return false;
	}
	if($("#meetContent").val().length <= 0) {
		$.alert("请输入议题标题");
		return false;
	}
	if(!flag) {
		$.alert("请输入议题内容");
		return false;
	}
	return true;
}

function uploadSystemFile() {
    var obj = {
        title:'上会文件',
        height:"320px",
        width:"750px",
        url:'meetcontent_attachment_dialog.html',
        fun:true,
        myCallBack:function(data){
            doInitSystemAttachment(data);
        }
    }
    nowSearchRole = new jqueryDialog(obj);
    $(".dialog_div_").parent().addClass("wf_top");
}

function doInitSystemAttachment(data) {
    var type = data.businessType;
    var businessId = data.businessId;
    var href = "";
    if(type == "0001" || type == "0002"  || type == "0005"  || type == "0006" ){
        //发文、发函
        href="../senddoc/new_fwfh_check.html?id="+businessId+"&type="+type+"&showCard=1";
    }else if(type == "0003"){
        //发电
        href="../telegram/new_fd_check.html?id="+businessId+"&type="+type;
    }else if(type == "0009"){
        //拟发文
        href="../senddoc/new_nfw_check.html?id="+businessId+"&type="+type+"&showCard=1";
    }else if(type == "5001"){
        //上级来文
        href="../receivedoc/new_sjlw_check.html?id="+businessId+"&type="+type;
    }else if(type == "5002"){
        //下级来文
        href="../receivedoc/new_xjlw_check.html?id="+businessId+"&type="+type+"&checkFlag=1";
    }else if(type == "5003"){
        //请示报告
        href="../report/new_reqrep_check.html?id="+businessId+"&type="+type+"&checkFlag=1";
    }else if(type == "6001"){
        //来电
        href="../telegram/new_sd_check.html?id="+businessId+"&type="+type;
    }else if(type == "7001"){
        //委办局函文
        href="../letter/bureaus_letter_read.html?id="+businessId+"&type="+type;
    }
    $(".files1").append('<tr ><td><p class="name"><input type="hidden" name="attachmentId" value="'+data.id+'"><a style="color:#428bca" href="'+href+'" target="_blank">'+data.businessTitle+'</a></p></td><td align="right"><button type="button" class="btn_select" role="button" aria-disabled="false" onclick="doDeleteTr(this)"><span class="ui-button-icon-primary ui-icon ui-icon-trash" style="display: none;"></span><span >删除</span></button></td></tr>');
}

function doDeleteTr(_this) {
    $(_this).parent().parent().remove();
}

/**
 * startWorkflow data fun
 */
function getData() {
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});

    var contentDocIds = new Array();
    $("[name='attachmentId']",".files1").each(function(){
        contentDocIds.push($(this).val());
    })
	var dto = {
			id:$("#id").val(),
			meetContent:UM.getEditor('myEditor').getContentTxt(),
			meetContentDetail:UM.getEditor('myEditor').getContent(),
			attachmentDTOs:attachmentDTOs,
			status:'0004',
			contentType:$("#contentType").val(),
			reportType:'0002',
            contentDocIds:contentDocIds
	}
	return dto;
}

/**
 * temp save object with status 0001
 */
function tempSubmitBtn() {
	valideClean("dataInputForm");
	var dto = getData();
	dto.status="0001";
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
	var flag = UM.getEditor('myEditor').hasContents();
//	$("#dataInputForm").attr("validate","");
//	doValideScan();
//	if(!valide("dataInputForm")) {
//		return false;
//	}
//	if($("#meetContent").val().length <= 0) {
//		$.alert("请输入议题标题");
//		return false;
//	}
	if(!flag) {
		$.alert("请输入议题");
		return false;
	}
	
	UM.getEditor('myEditor').setDisabled('fullscreen');
//	disableBtn("enable");
	var dto = getData();
	doJsonRequest("/meet/meetcontentlib/doSave",dto, function(data){
		if(data.result) {
			$("#id").val(data.data.id);
			window.location.href="meetSuccess.html?flag=1&fromPage=0001&btnSource=app";
		} else {
			$.alert("创建失败。")
		}
	},{showWaiting:true})
}

function editBtn() {
	var flag = UM.getEditor('myEditor').hasContents();
//	$("#dataInputForm").attr("validate","");
//	doValideScan();
//	if(!valide("dataInputForm")) {
//		return false;
//	}
//	if($("#meetContent").val().length <= 0) {
//		$.alert("请输入议题标题");
//		return false;
//	}
	if(!flag) {
		$.alert("请输入议题");
		return false;
	}
	
	UM.getEditor('myEditor').setDisabled('fullscreen');
//	disableBtn("enable");
	var dto = getData();
	dto.status="1000";
	doJsonRequest("/meet/meetcontentlib/doSave",dto, function(data){
		if(data.result) {
			$("#id").val(data.data.id);
			window.location.href="meetSuccess.html?flag=1&fromPage=0001&btnSource=app";
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

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0001&btnSource=app";
}


