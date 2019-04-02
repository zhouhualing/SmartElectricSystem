var pdfUrl = "";
$(function () {
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
    // Initialize the jQuery File Upload widget:
    $('#dataInputForm1').fileupload({
        disableImageResize: false,
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: '/cmcp/attachment/upload'
    });

    // Enable iframe cross-domain access via redirect option:
    $('#dataInputForm1').fileupload('option', {
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
            maxFileSize: attachment_maxSize,
            acceptFileTypes:attachment_regixType
    });
});




$(function(){
	var id =null;
	var fileName = "";
	$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice1.html?officeUrl='+fileName+'&taskid='+id+'" style="z-index:9999;width:100%;height:800px;"></iframe>');
	//获取当前登陆人信息

	$("#createDate").html(new Date().format("yyyy-MM-dd"))
	
})

$("#submitBtn").on("click",function(){
	doSend();
})

$("#caBtn").on("click",function(){
	$.alert("暂未集成CA。");
})

function doSend() {
	var datas =  $("#webOfficeIframe")[0].contentWindow.getInto();
	if(datas.pdfUrl.length <= 0) {
		$.alert("请先生成PDF红头文件");
		return false;
	}
	wf_doSearchRole(function(data){
		
		var dto = {
				contactPerson:datas.contactPerson,
				contactNumber:datas.contactNumber,
				orgCode:datas.createOrgCode,
				title:datas.title,
				type:'0001',
				roleCodes:data.roleCode,
				completeDate:data.completeDate,
				wordUrl:datas.wordUrl,
				pdfUrl:datas.pdfUrl,
				urgency:datas.urgency,
				attachmentDTOs:datas.attachmentDTOs
		}
		var data_ = data;
		doJsonRequest("/consultation/doSave",dto,function(data){
			if(data.result) {
				window.location.href="consulation_skip.html?fromPage=0001&roleName="+data_.roleName.join(",");
			} else {
				$.alert("发送失败。");
			}
		},{showWaitting:true})		
	})
}

function wf_doSearchRole(fun) {
	var str = "checkType=multi";
	var roleCodes = "036,034,033"
	var obj = {
	    title:'选择发往单位',
	    height:"320px",
	    width:"750px",
	    url:'../users/send_role_dialog.html?roleCodes='+roleCodes,
	    fun:true,
	    myCallBack:fun
	}
	new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}

function saveRedBtn(url){
	pdfUrl = url;
//	var dto ={};
//	dto.pdfUrl = url;
//	addReportText(dto);
}

function addReportText(dto) {
	doJsonRequest("/consultation/savePDF",dto,function(data){},{showWaiting:true});
}

$(function(){
	getCurrenUserInfo(function(data){
		nowOrgName = data.orgName;
	})
})
var nowOrgName = "";

$("#redBtn").click(function(){
		var URL = "../../static/officefile/template/jianchajuheader.doc";
		if(nowOrgName=="市公安局") {
			URL = "../../static/officefile/template/gonganjuheader.doc";
		}
		//读取模板文件套红(URL:文件路径;code:套红编号;issuer:套红签发人;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
		var code = "001";
		var	issuer = $("#theIssuer").val();
		var ccuser = $("#ccUnit").val();
		var company = "ZF";
		weboffice.window.insertRedHeadFromUrl(URL,code,issuer,ccuser,nowOrgName);
});

function wf_getDate(data) {
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0001') {
		return getTheDate(86400);
	}
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0002') {
		return getTheDate(86400);
	}
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0003') {
		return getTheDate(86400);
	}
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0004') {
		return getTheDate(86400);
	}
}


var webOfficeEndUrl = "../../static/officefile/template/mailend.doc";