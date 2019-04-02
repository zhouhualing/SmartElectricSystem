$(".fileupload-buttons").hide();
var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");  
var localText = "";
var lcbz = "";
var officeflag = true;
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
$("#qsy a").tab("show");
$(function () {
    'use strict';
    /*$("body").hide();*/
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
    
});

$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(null!=data.files[0]){
					//if(data.files[0].pdfUrl.endWith(".pdf")){
					localText = data.files[0].pdfUrl;
						//$("#mailFrame").attr("src",data.files[0].pdfUrl);
					//}
					 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
					 $(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)")
				}
				//来文单位
				$("#docCameOrgan").val(data.docCameOrgan);
				//文号
				$("#receiveCode").val(data.receiveCode);
				//紧急程度
				$("#urgency").val(data.urgency);
				//文件标题
				$("#receiveTitle").val(data.receiveTitle);
				//秘书审核
				$("#fmszOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				
				var mszpsAudit = "";
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszpsAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					mszpsAudit += data.fmszOpinion;
				}
				$("#mszpsAudit").html(mszpsAudit);
				//市长审核
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var ldpsAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					ldpsAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					ldpsAudit += data.fszOpinion;				
				}
				$("#ldpsAudit").html(ldpsAudit);


				var leadingAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					leadingAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					leadingAudit += data.fszOpinion + '<br>';				
				}
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					leadingAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					leadingAudit += data.fmszOpinion;
				}
				$("#leadingAudit").html(leadingAudit);
				
				//呈批意见
				$("#hgOpinion").val(data.hgOpinion);
				/*$("#wskOpinion").val(data.wskOpinion);*/
				
				/*var nbyjAudit = "";
				if(data.hgOpinion!='' && data.hgOpinion!=null){
					nbyjAudit += data.hgOpinion + '<br>';
				}
				if(data.wskOpinion!='' && data.wskOpinion!=null){
					nbyjAudit += data.wskOpinion + '<br>';				
				}*/
				/*$("#nbyjAudit").html(data.hgOpinion);*/
				//备注
				//正文
				$("#text").val(data.text);
				$("#drafterName").html(data.drafterName);
				$("#drafterPhoneNum").html(data.drafterPhoneNum);
				if(data.receiveDate!=''&&data.receiveDate!=null){
					$("#receiveDate").val(new Date(data.receiveDate).format('yyyy年MM月dd日'));
				}
				$("#lwCode").val(data.lwCode);
				$("#upCardFlag").val(data.upCardFlag);
				$("#createUserName").val(data.createUserName);
				//小纸条
				$("#liPage").val(data.littlePage);
				//当前是否是退回办理人
				$("#isReturn").val(data.isReturn);
				doQueryWF("reportInfo","approvalDiv");
				//初始化流程按钮
				var dto = {
					taskId:taskId	
				}
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
});

$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' + localText+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});

function showPDF(_this){
//	$("#mailFrame").attr("src",$(_this).attr("theUrl"))
	 weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}
