var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");
var officeflag = true;
var officeUrl = "";
$("#id").val(id);
$("#businessKey").val(id);
$("#reqRepType").val(type);
$("#qsy a").tab("show");
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
    
});
$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' +officeUrl+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});
$(function(){
	//初始化流程按钮
	var dto = {
		taskId:taskId	
	}
	
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/reqrep/getReqRep",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(null!=data.files[0]){
					officeUrl =  data.files[0].pdfUrl;
				}
				$("#reqRepType").val(data.reqRepType);
				$("#reqRepCode").html(data.reqRepCode);
				$("#reqRepTitle").html(data.reqRepTitle);
				$("#docCameOrgan").html(data.docCameOrgan);
				$("#drafterPhone").html(data.drafterPhone);
				$("#drafterName").html(data.drafterName);
				$("#reqRepText").val(data.reqRepText);
				/**
				 * 内容高度自适应。
				 */
				var height = $("#reqRepText")[0].scrollHeight + "px";
				$("#reqRepText").css('height',height);
				
				$("#remark").html(data.remark);
				if(data.reqRepDate!=''&&data.reqRepDate!=null){
					$("#reqRepDate").html(new Date(data.reqRepDate).format('yyyy年MM月dd日'));
				}
				//领导审核
				$("#fgldOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var ldpsAudit = "";
				var mszAudit = "";
				var bmldAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					ldpsAudit += data.szOpinion+ '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					ldpsAudit += data.fszOpinion+ '<br>';				
				}
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszAudit += data.mszOpinion+ '<br>';
				}
				if(data.fgldOpinion!='' && data.fgldOpinion!=null){
					bmldAudit += data.fgldOpinion+ '<br>';
				}
				$("#ldpsAudit").html(ldpsAudit);
				$("#mszAudit").html(mszAudit);
				$("#bmldAudit").html(bmldAudit);
				$("#isReturn").val(data.isReturn);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
	doQueryWF("reportInfo","approvalDiv");
});

