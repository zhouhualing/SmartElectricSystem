/**
 * init
 */
$(function(){
	$("#fileDiv").addClass("hidden")
	var obj = {
			id:getURLParam("id")
		}
	doJsonRequest("/meet/meetcontentlib/getInfo", obj,function(data){
		if(data.result) {
			 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data.data});
			 $('#dataInputForm').removeClass('fileupload-processing');
			 $(".hideTd").hide();
			 if(data.data.files.length==0) {
				 $("#theMeetFileDiv").addClass("hidden");
			 }
			 $("#contentType").val(data.data.contentType)
			 $("#meetContent").val(data.data.meetContent);
			 $("#meetContent").trigger("keyup")
            var systemAttachments = data.data.meetContentDocDTOs;
            for(var i=0; i<systemAttachments.length; i++) {
                doInitSysytemAttachemnt(systemAttachments[i]);
            }
			  UM.getEditor('myEditor').setContent(data.data.meetContentDetail);
			 UM.getEditor('myEditor').setDisabled();
			 $(".edui-btn-toolbar").addClass("hidden");
//			 $("#myEditor").attr("style","width: 643px; min-height: 240px; z-index: 999;background-color: #eee;");
			 $(".edui-editor-body").removeClass("edui-editor-body").attr("style","background-color: #eee;");
		} else {
			$.alert("获取信息出错。");
		}
	})
//	$("#businessKey").val(getURLParam("id"));
//	doQueryWF("reportInfo","approvalDiv");
	
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

function doInitSysytemAttachemnt(data) {
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
    $(".files1").append('<tr ><td><p class="name"><input type="hidden" name="attachmentId" value="'+data.id+'"><a style="color:#428bca" href="'+href+'" target="_blank">'+data.businessTitle+'</a></p></td><td align="right"></td></tr>');
}


/**
 * do url skip
 */
function goSkip() {
	window.location.href = "meet_content_lib_list.html";
}



