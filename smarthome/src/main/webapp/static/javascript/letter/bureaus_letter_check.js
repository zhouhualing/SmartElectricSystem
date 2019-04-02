var id = getURLParam("id");
var fromPage = getURLParam("fromPage"); //页面来源
var showLcgz = getURLParam("showLcgz"); //是否显示流程跟踪按钮，0为不显示
var taskId = getURLParam("taskId"); 
$("#businessKey").val(id);
//打开时默认显示函件:
$(".nav-tabs a:first").tab("show");
if(showLcgz == 0){
	$("#lcgz").hide();
}
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
	var dto = {id:id};
	$('#dataInputForm').addClass('fileupload-processing');
	doJsonRequest("/burLetter/getBurLetter", dto, function(data){
		if(data.result) {
			var data = data.data;
//			$("#mailFrame").attr("src","/cmcp"+data.text);
			$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' + data.text+'"style="width:100%;height:800px;"></iframe>');
			$('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
			$('#dataInputForm').removeClass('fileupload-processing');
			$("[id='file_removeBtn']").remove();
			$(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)")
			
			$("#contactPerson").html(data.contactPerson);
			$("#createOrgCode").val(data.orgName);
			$("#createOrgName").html(data.orgName);
			$("#contactNumber").html(data.contactNumber);
			$("#reportTitle").html(data.reportTitle);
			/*$("#burLetterCode").html(data.burLetterCode);*/
			$("#text").val(data.text);
			$("#urgencySelect").val(data.urgency);
			$("#urgency").html($("#urgencySelect").find("option:selected").text());
			$("#reportType").val(data.reportType);
			doQueryWF("reportInfo","approvalDiv");
		} else {
			$.alert("获取函文出错。");
		}
		
	})
})

function showPDF(_this){
	$(_this).attr("href",$(_this).attr("theUrl")).attr("target","_blank")
}
//-----------------委办局函文，以上已经js完成------------------------------------------------