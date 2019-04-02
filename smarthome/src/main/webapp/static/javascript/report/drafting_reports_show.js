var id =getURLParam("id");
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
$("#reportId").val(id)
$("#businessId").val(id);
$("#businessKey").val(((taskId==null)||(taskId=="undefined"))?-1:taskId);
function queryEnd3 () {
	clickmedTables.actionListQuery.hideFoot();
}

$("[name='showPlan']").on("change",function(){
	if($(this).val()=="0001") {
		$("#hidDiv").removeClass("hidden")
	} else {
		$("#hidDiv").addClass("hidden")
	}
})


function init() {
	if(fromPage == "0001") {
		$("ul").remove()
		$("#tab2").remove();
		$("#nextPage").remove();
	}
}
init();


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

function queryEnd () {
	clickmedTables.reportInfoQuery.hideFoot();
}

function queryEnd1 () {
	clickmedTables.reportInfoQuery1.hideFoot();
}

$("#businessKey").val(id);

$(".nav-tabs a:first").tab("show");

$("#nextPage").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
})

$("#nextPage1").on("click",function(){
	$(".nav-tabs a").eq(2).tab("show");
})

$("#beforePage1").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})
function initPath() {
	if(getURLParam("fromPage")=="0002") {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>已办</a> >> <a href='approvaled_reports_list.html'>已办事项</a> >> <a href='#'>查看签批信息</a>")
	}
	if(getURLParam("fromPage")=="0003") {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>已办</a> >> <a href='approvaled_reports_list.html'>已办事项</a> >> <a href='#'>查看办理信息</a>")
	}
	if(getURLParam("fromPage")=="0004") {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>查阅</a> >> <a href='mecreate_reports_list.html'>我发起的请示报告</a> >> <a href='#'>查看详细信息</a>")
	}
	if(getURLParam("fromPage")=="0005") {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>查阅</a> >> <a href='show_reports_list.html'>请示报告查看</a> >> <a href='#'>查看详细信息</a>")
	}
}
/**
 * document ready
 */
$(function(){
	initPath();
	$("input[name='reportType']").on("change",function(){
		onChangePage(this);
	})
	if(id != null) {
		$("#backBtn").removeClass("hidden");
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
				if(data.status == "0006" || data.status == "0007") {
					$("#nextPage1").removeClass("hidden");
					$("#tab3Li").removeClass("hidden");
				}
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
	}

})


function doSearchOrg() {
	var obj = {
	    title:'选择跟件人',
	    height:"500px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?checkType=radio',
	    myCallBack:"initFollowUserInfo"
	}
	new jqueryDialog(obj);
}

function initFollowUserInfo(data) {
	$("#reportUserName").val(data.userName);
	$("#reportUserCode").val(data.userCode);
}

function addReport(status) {
	var flag = true;
	if(status == '0002') {
		flag = valide("dataInputForm");
	}
	if(flag) {
		var dto = $("#dataInputForm").serialize();
		dto = dto+"&status="+status;
		doRequest("/report/addReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#id").val(data.id);
				if(status == "0001") {
					$.alert("暂存成功。");
				}
				if(status == "0002") {
					$.alert("提交成功。");
				}	
			} else {
				alert("添加报告出错。");
			}
		},{showWaiting:true})
	}
}

$("#submitBtn").on("click",function(){
	addReport("0002");
} )

$("#tempSubmitBtn").on("click",function(){
	addReport("0001");
} )

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
}