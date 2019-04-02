var id =getURLParam("id");
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
$("#businessKey").val(id);
$("#businessId").val(id);

$(".nav-tabs a:first").tab("show");

$("#nextPage").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
})

$("#approvalBtn").on("click",function(){
	approvalReport();
})

$("[name='showPlan']").on("change",function(){
	if($(this).val()=="0001") {
		$("#hidDiv").removeClass("hidden")
	} else {
		$("#hidDiv").addClass("hidden")
	}
})

function queryEnd () {
	clickmedTables.reportInfoQuery.hideFoot();
}

function queryEnd3 () {
	clickmedTables.actionListQuery.hideFoot();
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

    if (window.location.hostname === 'blueimp.github.io') {
        // Demo settings:
        $('#dataInputForm').fileupload('option', {
            url: '//jquery-file-upload.appspot.com/',
            // Enable image resizing, except for Android and Opera,
            // which actually support image resizing, but fail to
            // send Blob objects via XHR requests:
            disableImageResize: /Android(?!.*Chrome)|Opera/
                .test(window.navigator.userAgent),
            maxFileSize: 5000000,
            acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
        });
        // Upload server status check for browsers with CORS support:
        if ($.support.cors) {
            $.ajax({
                url: '//jquery-file-upload.appspot.com/',
                type: 'HEAD'
            }).fail(function () {
                $('<div class="alert alert-danger"/>')
                    .text('Upload server currently unavailable - ' +
                            new Date())
                    .appendTo('#fileupload');
            });
        }
    } else {
//        // Load existing files:
//        $('#dataInputForm').addClass('fileupload-processing');
//        $.ajax({
//            // Uncomment the following to send cross-domain cookies:
//            //xhrFields: {withCredentials: true},
//            url: $('#dataInputForm').fileupload('option', 'url'),
//            dataType: 'json',
//            context:$('#dataInputForm')[0],
//            contentType: "application/json",
//        }).always(function () {
//            $(this).removeClass('fileupload-processing');
//        }).done(function (result) {
//            $(this).fileupload('option', 'done')
//                .call(this, $.Event('done'), {result: result});
//        });
    }

});

/**
 * document ready
 */
$(function(){
	$("input[name='reportType']").on("change",function(){
		onChangePage(this);
	})
		var dto = {
				id:id
		}
		$('#dataInputForm').addClass('fileupload-processing');
		doJsonRequest("/report/getReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
				 $('#dataInputForm').removeClass('fileupload-processing');
				 $("#fileDiv").hide()
				 $(".hideTd").hide()
				$("[name='reportType']").initRadio(data.reportType);
				onChangePage($("[name='reportType']:checked"));
				var value = $("[name='reportType']:checked").next("span").html();
				$("[name='reportType']").parent().empty().append("<span class='input-group-addon' ></label>起草方式：</span><input type='text' name='reportType' disabled class='form-control' id='reportType' value='"+value+"'/>");
				$("#businessType").val(data.businessType)
				$("#businessType").replaceWith("<input type='text' name='businessType' disabled class='form-control' id='businessType' value='"+$("#businessType option:selected").text()+"'/>");
				
				$("#fromNumber").val(data.fromNumber);
				$("#reportDate").html(new Date(data.reportDate).format("yyyy-MM-dd"));
				$("#completeDate").val(new Date(data.completeDate).format("yyyy-MM-dd hh:mm:ss"));
				$("#fromOrgName").val(data.fromOrgName);
				$("#reportTitle").val(data.reportTitle);
				$("#reportCode").html(data.reportCode);
				$("#reportSummary").val(data.reportSummary).trigger("keyup");
				$("#proposedAdvice").val(data.proposedAdvice).trigger("keyup");
				$("#reportUserName").val(data.reportUserName);
				$("#reportUserCode").val(data.reportUserCode);
				$("#userCode").val(data.createUserCode);
				$("#createUserName").html(data.createUserName);
				$("#createUserOrgName").html(data.createUserOrgName);
				$("#phoneNumber").html(data.phoneNumber)
				doQuery("reportInfoQuery");
//				doQuery("actionListQuery");
			} else {
				$.alert("获取信息失败。");
			}
		}) 		

})


function approvalReport() {
	var approvalObj = $("input[name='approvalFlag']:checked");
	if(approvalObj.length == 0) {
		$.alert("请选择通过或拒绝。");
		return false;
	}
		var dto =  {
			id:id,
			workFlowDTO:{
				taskId:taskId,
				mark:$("#mark").val(),
				approvalFlag:approvalObj.val()
			}
		}
		
		var msg = "拒绝";
		if(approvalObj.val() == "0001")  {
			msg = "通过";
		}
		doJsonRequest("/report/approvalReport",dto,function(data){
			if(data.result) {
				$.alert({
		    	    title:'提示信息',
		    	    msg:'已审批'+msg+"该报告。",
		    	    height:180,
		    	    confirmClick:"goSuccess"
		    	});
			} else {
				alert("审批出错。");
			}
		},{showWaiting:true})
}

function goSuccess() {
	if(fromPage == "pc") {
		window.location.href="../common/indexMain.html?businessType=0001";
	} else {
		window.location.href="inbox_reports_list.html";
	}
	
}

$("#reportType").on("change",function(){
	onChangePage(this);
})

function onChangePage(_this) {
	var value = $(_this).val();
	if(value=="0001") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").addClass("hidden");
		$("#proposedAdviceDiv").addClass("hidden");
	}
	
	if(value=="0002") {
		$("#fromNumberDiv").removeClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
	}
	
	if(value=="0003") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
	}
}

function goBack() {
	window.history.back();
	if(fromPage == "pc") {
		window.location.href="../common/indexMain.html";
	}
}

function doResize() {
	if($("frameset", window.parent.document).eq(1).attr("cols") == "225,*") {
		$("frameset", window.parent.document).eq(1).attr("cols","0,*");
	} else  if($("frameset", window.parent.document).eq(1).attr("cols") == "0,*") {
		$("frameset", window.parent.document).eq(1).attr("cols","225,*");
	}
}