var id =getURLParam("id");
//上下级来文parent_id
var report_type=getURLParam("report_type");
if(report_type==="superior"){
	$("#collapseOne").html("");//暂时为空
}
/**edit_or_not 是起草（编辑）或查看
 * 起草0001
 * 查看0002
 */

var edit_or_not = "";


$(function(){
	//初始化流程按钮
	wf_getOperator("getDocFromLower",function(data){
	});
	$("#clickme").tab("show");	
	if(id==null){
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl=" style="width:100%;height:500px;"></iframe>');
	}else{
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl=aaaaa" style="width:100%;height:500px;"></iframe>');
	}

	if(id != null) {
		$("#id").val(id);
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		};
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#receiveDate").html(new Date(data.receiveDate).format("yyyy-MM-dd"));
				$("#receiveCode").html(data.receiveCode);
				$("#receiveTitle").val(data.receiveTitle);
				$("#docCameOrgan").val(data.docCameOrgan);
				$("#docCameDate").val(new Date(data.docCameDate).format("yyyy-MM-dd"));
				$("#docCameNum").val(data.docCameNum);
				$("#docCameCode").val(data.docCameCode);
				$("#attachments").val();
				$("#completeDate").val(new Date(data.completeDate).format("yyyy-MM-dd hh:mm:ss"));
				$("#docCameSummary").val(data.docCameSummary).trigger("keyup");
				$("#remark").val(data.remark).trigger("keyup");
				$("#reportUserName").val(data.reportUserName);
				$("#reportUserCode").val(data.reportUserCode);
				$("#createUserName").val(data.createUserName);
				$("#createUserOrgName").val(data.createUserOrgName);
				$("#phoneNumber").val(data.phoneNumber);
				$("#zmzj").val(data.zmzj);
				$("#huiqian").val(data.huiqian);
				$("#cb_time").val(new Date().format("yyyy-MM-dd hh:mm:ss")),
				$("#createUserCode").val(data.createUserCode)
				
			} else {
				$.alert("获取信息失败。");
			}
		}); 		
	} else {
		$("#receiveDate").html(new Date().format("yyyy-MM-dd"))
		doJsonRequest("/code/getCode","0001",function(data){
			if(data.result) {
				$("#receiveCode").html(data.data);
			} else {
				$.alert("获取编码出错。")
			}
		})
	}
});

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


$(function(){
	if(id==null){
	getCurrenUserInfo(function(data){
		$("#createUserName").val(data.userName);
		$("#createUserOrgName").val(data.orgName);
		$("#phoneNumber").val(data.phoneNumber);
		$("#cb_time").val(new Date().format("yyyy-MM-dd hh:mm:ss")),
		$("#createUserCode").val(data.userCode)
	});
	}
});

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

function doSearchRole() {
	var obj = {
	    title:'选择角色',
	    height:"500px",
	    width:"750px",
	    url:'../users/role_dialog.html?selectCount=0001',
	    myCallBack:"initFollowRoleInfo"
	};
	new jqueryDialog(obj);
}

function initFollowUserInfo(data) {
	$("#reportUserName").val(data.userName);
	$("#reportUserCode").val(data.userCode);
}

function initFollowRoleInfo(data) {
	$("#reportUserName").val(data.roleName);
	$("#reportUserCode").val(data.roleCode);
}

function addReport(status) {
	var flag = true;
	if(status == '0002') {
		flag = valide("dataInputForm");
	}
	
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});
	
	
	if(flag) {
//		var dto = $("#dataInputForm").serialize();
////		dto = dto+"&status="+status+"&receiveCode="+$("#receiveCode").html()+"&receiveDate="+$("#receiveDate").html()+"&report_type="+report_type+"$attachmentDTOs="+attachmentDTOs;
//		dto = dto+"&status="+status+"&createUserName="+$("#createUserName").val()+"&cb_time="+$("#cb_time").val()+"&report_type="+report_type+"&phoneNumber="+$("#phoneNumber").val()+"&createUserCode="+$("#createUserCode").val();
		dto = save_dto();
		doRequest("/receivedoc/addReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#id").val(data.id);
				if(status == "0001") {
					$.alert("暂存成功。");
				}
				if(status == "0002") {
					$.alert({
				    	    title:'提示信息',
				    	    msg:'提交成功。',
				    	    height:180,
				    	    confirmClick:"goSuccess"
				    	});
					
				}	
			} else {
				alert("添加报告出错。");
			}
//			window.history.back();
		},{showWaiting:true})
	}
}

$("#submitBtn").on("click",function(){
	//提交
	addReport("0002");
} )

$("#tempSubmitBtn").on("click",function(){
	//保存至草稿箱
	addReport("0001");
} )


$("#docCameDate").datepicker({
	dateFormat:"yy-mm-dd",
});

$("#beforePage").on("click",function(){
	$(".nav-tabs a").eq(0).tab("show");
});

$("#nextPage").on("click",function(){
	$(".nav-tabs a").eq(1).tab("show");
})



$("#completeDate").datetimepicker({
	showSecond: true, //显示秒
	timeFormat: 'HH:mm:ss',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-mm-dd",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间'
});

/**
 * 

function onChangePage(_this) {
	var value = $(_this).val();
	if(value=="0001") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").addClass("hidden");
		$("#proposedAdviceDiv").addClass("hidden");
		$("#text2").html("报告内容");
	}
	
	if(value=="0002") {
		$("#fromNumberDiv").removeClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
		$("#text1").html("来电单位");
		$("#text2").html("来电摘要");
	}
	
	if(value=="0003") {
		$("#fromNumberDiv").addClass("hidden");
		$("#fromOrgNameDiv").removeClass("hidden");
		$("#proposedAdviceDiv").removeClass("hidden");
		$("#text1").html("来文单位");
		$("#text2").html("来文摘要");
	}
}
 */
function goSuccess() {
	$("a[href='../../view/receivesenddoc/draft_receive_list.html']", $(parent.frames['leftFrame'].document)).children("li").trigger("click");
}

function goBack() {
	if(id===null){
		window.location.href = "drafting_recevie.html";
	}else{
		window.location.href = "drafting_receive_list.html";
	}
	
}

$(function(){
	$("#save").on("click",function(){
		save_dto();
	})
})



function save_dto(){
	
	var dto={
			id:id,
			docCameOrgan:$("#docCameOrgan").val(),
			docCameCode:$("#docCameCode").val(),
			zmzj:$("#zmzj").val(),
			receiveTitle:$("#receiveTitle").val(),
			ldps:$("#ldps").val(),
			mszps:$("#mszps").val(),
			cpyj:$("#cpyj").val(),
			remark:$("#remark").val(),
			createUserName:$("#createUserName").val(),
//			cb_time:$("#cb_time").val(),
			phoneNumber:$("#phoneNumber").val(),
			createUserCode:$("#createUserCode").val(),
			status:"0002",
			report_type:report_type,
			docCameSummary:""
			
	}
	//console.log(dto);
	return dto;
}