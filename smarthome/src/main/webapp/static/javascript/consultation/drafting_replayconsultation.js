var mailId = getURLParam("id")
var fromPage = getURLParam("fromPage")
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
	$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice3.html?officeUrl='+fileName+'&taskid='+id+'" style="z-index:9999;width:100%;height:800px;"></iframe>');
	//获取当前登陆人信息

	$("#createDate").html(new Date().format("yyyy-MM-dd"))
	
})

$("#caBtn").on("click",function(){
	$.alert("暂未集成CA。");
})


function saveRedBtn(url){
	pdfUrl = url;
//	var dto ={};
//	dto.pdfUrl = url;
//	addReportText(dto);
}


$(function(){
	getCurrenUserInfo(function(data){
		nowOrgName = data.orgName;
		nowOrgCode = $.trim(data.roleCodes);
	})
})
var nowOrgName = "";
var nowOrgCode = "";

$("#redBtn").click(function(){
		//读取模板文件套红(URL:文件路径;code:套红编号;issuer:套红签发人;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
	var URL = "../../static/officefile/template/jianchajuheader.doc";
	if(nowOrgName=="市公安局") {
		URL = "../../static/officefile/template/gonganjuheader.doc";
	}
		var code = "001";
		var	issuer = $("#theIssuer").val();
		var ccuser = $("#ccUnit").val();
		var company = "ZF";
		weboffice.window.insertRedHeadFromUrl(URL,code,issuer,ccuser,nowOrgName);
});


var webOfficeEndUrl = "../../static/officefile/template/mailend.doc";

$("#submitBtn").on("click",function(){
	doSend();
})

function doSend() {
		var datas =  $("#webOfficeIframe")[0].contentWindow.getInto();
		if(datas.pdfUrl.length <= 0) {
			$.alert("请先生成PDF红头文件");
			return false;
		}
		var dto = {
				orgCode:nowOrgCode,
				title:datas.title,
				mailId:mailId,
				type:'0002',
				attachmentDTOs:datas.attachmentDTOs,
				wordUrl:datas.wordUrl,
				pdfUrl:datas.pdfUrl
		}
		
		doJsonRequest("/consultation/doSave",dto,function(data){
			if(data.result) {
				var str = "0002"
				if(fromPage == "pc"){
					str="pc";
				}
				window.location.href=window.location.href="consulation_skip.html?fromPage="+str;
			} else {
				$.alert("回函失败。");
			}
		},{showWaitting:true})		
}

function wf_doSearchRole(fun) {
	var str = "checkType=multi";
	var roleCodes = "035,036"
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