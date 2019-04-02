//打开时默认显示正文页:

var id = getURLParam("id"); //当前收文id
$("#id").val(id);
$("#businessKey").val(id);
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");

var officeflag = true;
var officeUrl = "";

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
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
	});
	if(id != null) {
		var dto = {id:id}
		doJsonRequest("/super/getSuperVise",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(null!=data.files[0]){
					officeUrl = data.files[0].pdfUrl;
					$('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
					$(".name","#attachmentDiv").children("a").attr("onclick","openAttachments('','',this)")
				}
				var files = data.files;
				var attr = "";
				if(files!=null){
					$.each(files,function(n,item){
						attr+=(n+1)+"、"+item.name+"；"
					})
				}
				$("#attr").data('d',data).val(attr);
				
				$("#title").val(data.title);
				$("#issue").html(data.issue);
				$("#superCode").html(data.superCode);
				$("#completeDate").val(new Date(data.completeDate).format('yyyy年M月d日'));
				$("#superDate").html(new Date(data.superDate).format('yyyy年M月d日'));
				$("#content").val(data.content);
				$("#szOpinion").val(data.szOpinion);
				$("#fszOpinion").val(data.fszOpinion);
				var szOpinion = data.szOpinion;
				var fszOpinion = data.fszOpinion;
				if(szOpinion!=null && fszOpinion!=null){
					$("#ldyjSpan").html(data.szOpinion + "<br>" + fszOpinion);
				}else if(szOpinion==null && fszOpinion!=null){
					$("#ldyjSpan").html(fszOpinion);
				}else if(szOpinion!=null && fszOpinion==null){
					$("#ldyjSpan").html(szOpinion);
				}
				$("#unitSpan").html(data.unitOpinion);
				$("#remarkSpan").html(data.remark);
				$("#status").val(data.status);
				$("#superType").val(data.superType);
				$(".hideTd").hide();
			} else {
				$.alert("获取信息失败。");
			}
			doQueryWF("reportInfo","approvalDiv");
		});
	}
});

$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' +officeUrl+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});

function openAttachments(url,type,_this){
//	$("#mailFrame").attr("src",$(_this).attr("theurl"))
	 weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}
