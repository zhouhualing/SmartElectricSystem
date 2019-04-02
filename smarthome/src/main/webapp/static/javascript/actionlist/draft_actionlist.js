var id =getURLParam("id");
var fromPage = getURLParam("fromPage");
var businessId = getURLParam("businessId")
var businessTitle =getURLParam("businessTitle")
var currentUserName="";

var typeName1 = getURLParam("typeName1");
var reportType1 = getURLParam("reportType1");
var businessId1 = getURLParam("businessId1");
var title1 = getURLParam("title1");

var tobeCrId = getURLParam("tobeCrId");//若当前是从待创建列表点击进来
if(tobeCrId!=null && tobeCrId!=''){
	$("#chooseBtn").remove();
	doJsonRequest("/actionList/getToBeCreated",{id:tobeCrId},function(data){
		if(data.result) {
			var data = data.data;
			$("#businessTitle").val(data.docTitle);
			$("#tableName").val(data.docTableName);
			$("#businessId").val(data.docId);
			$("#dcRoleCode").val(data.dcRoleCode);
			$("#dcRoleName").val(data.dcRoleName);
			$("#tobeCrUserId").val(data.userId);
			/*$("#url").val(data.url);*/
		} else {
			$.alert("获取信息失败。");
		}
	}) 	
	
}
if(businessId1 != null) {
	var tableName = "";
	if("0001" == reportType1) {
		tableName = "tb_senddoc_senddoc"
	}else if("0002" == reportType1) {
		tableName = "tb_senddoc_senddoc"
	}else if("0003" == reportType1) {
		tableName = "tb_senddoc_senddoc"
	}else if("0005" == reportType1) {
		tableName = "tb_senddoc_senddoc"
	}else if("0006" == reportType1) {
		tableName = "tb_senddoc_senddoc"
	}else if("0009" == reportType1) {
		tableName = "tb_senddoc_senddoc"
	}else if("5001" == reportType1) {
		tableName = "tb_receivedoc_receivedoc"
	}else if("5002" == reportType1) {
		tableName = "tb_receivedoc_receivedoc"
	}else if("6001" == reportType1) {
		tableName = "tb_receivedoc_receivedoc"
	}else if("5003") {
		tableName = "tb_request_report"
	}else {
		tableName = "tb_bureaus_letter";
	}
	$("#tableName").val(tableName);
	$("#businessTitle").val(title1);
	$("#businessId").val(businessId1);
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
	if(fromPage=='0001') {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>起草</a> >> <a href='draft_actionlist_list.html'>草稿列表</a> >> <a href='#'>编辑草稿</a>")
	}
	if(fromPage=='0002') {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>创建一事一表</a>")
	}
	if(businessId != null) {
		$("#businessId").val(businessId);
		$("#businessTitle").val(businessTitle);
		$("#businessType").val('0001');
	}
	if(id != null) {
		$("#id").val(id);
		$("#backBtn").removeClass("hidden");
		$('#dataInputForm').addClass('fileupload-processing');
		doJsonRequest("/actionList/getActionList",id,function(data){
			if(data.result) {
				var data = data.data;
				 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
				 $('#dataInputForm').removeClass('fileupload-processing');
				 $("#actionContent").val(data.actionContent).trigger("keyup");
				 $("#businessType").val(data.businessType);
				 $("#businessTitle").val(data.businessTitle)
				 $("#businessId").val(data.businessId);
				 $("#title").val(data.title);
				 
			} else {
				$.alert("获取信息失败。");
			}
		}) 		
	}
	doQuery();
})

function queryEnd () {
	$("[name='detailInfo']").trigger("keyup");
	clickmedTables.actionListQuery.hideFoot();
	$("[name='completeDate']").datepicker({
		showSecond: true, //显示秒
		timeFormat: 'HH:mm:ss',//格式化时间
		stepHour: 1,//设置步长
		stepMinute: 5,
		stepSecond: 10,
		dateFormat:"yy-m-d",
		currentText:'现在',
		closeText:'确定',
		hourMax:'23',
		hourText:'时',
		minuteText:'分',
		secondText:'秒',
		timeText:'时间',
		buttonType:true
	});
	
	$("[name='testDate']").datepicker({
		showSecond: true, //显示秒
		timeFormat: 'HH:mm:ss',//格式化时间
		stepHour: 1,//设置步长
		stepMinute: 5,
		stepSecond: 10,
		dateFormat:"yy-m-d",
		currentText:'现在',
		closeText:'确定',
		hourMax:'23',
		hourText:'时',
		minuteText:'分',
		secondText:'秒',
		timeText:'时间',
		buttonType:true
	});
	
	$("[name='selectUserTBtn']").on("click",function(){
		doSearchUser(this);
	})
	
	$("[name='formatDivX']").each(function(){
		$(this).parent().attr("style","border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2;");
	})
	
}

$(function(){
	getCurrenUserInfo(function(data){
		currentUserName = data.userName;
		$("#createUserName").html(data.userName);
		$("#createUserOrgName").html(data.orgName);
		$("#phoneNumber").html(data.phoneNumber)
	})
	
});

function initUserInfo(data,srcData) {
	$(srcData).parent().prev("input[name='userName']").val(data.userName).trigger("change")
	$(srcData).parent().siblings("input[name='userCode']").val(data.userCode).trigger("change")
}

function doSearchUser(_this) {
	var obj = {
	    title:'选择责任人',
	    height:"500px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?checkType=radio',
	    myCallBack:initUserInfo,
	    fun:true,
	    srcData:_this
	}
	new jqueryDialog(obj);
}

function doSearchItem() {
	var obj = {
	    title:'选择公文',
	    height:"500px",
	    width:"950px",
	    url:'../report/reports_actionlist_dialog.html',
	    myCallBack:"myCallBack"
	}
	new jqueryDialog(obj);
	$(".ui-dialog-titlebar-close").attr("style","border-bottom-right-radius:4px;border-bottom-left-radius: 4px");
}


function myCallBack(data) {
	var data = JSON.parse(data);
	var tableName = "";
	if("0001" == data.reportType) {
		tableName = "tb_senddoc_senddoc"
	}else if("0002" == data.reportType) {
		tableName = "tb_senddoc_senddoc"
	}else if("0003" == data.reportType) {
		tableName = "tb_senddoc_senddoc"
	}else if("0005" == data.reportType) {
		tableName = "tb_senddoc_senddoc"
	}else if("0006" == data.reportType) {
		tableName = "tb_senddoc_senddoc"
	}else if("0009" == data.reportType) {
		tableName = "tb_senddoc_senddoc"
	}else if("5001" == data.reportType) {
		tableName = "tb_receivedoc_receivedoc"
	}else if("5002" == data.reportType) {
		tableName = "tb_receivedoc_receivedoc"
	}else if("6001" == data.reportType) {
		tableName = "tb_receivedoc_receivedoc"
	}else if("5003") {
		tableName = "tb_request_report"
	}else {
		tableName = "tb_bureaus_letter";
	}
	$("#tableName").val(tableName);
	$("#businessTitle").val(data.reportTitle);
	$("#businessId").val(data.id);
}

function myCancelCallBack() {
}


$("#submitBtn").on("click",function(){
	doSubmit("0002");
} )

$("#tempSubmitBtn").on("click",function(){
	doSubmit("0001");
} )



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

function goSuccess() {
	window.location.href = "new_reqrep_draft.html";
}

function goBack() {
	window.location.href = "draft_actionlist_list.html";
}
$("#addDetailPlan").on("mouseover",function(){
	$(this).addClass("ui-state-hover")
}).on("mouseout",function(){
	$(this).removeClass("ui-state-hover")
}).on("click",function(){
	addDetailPlan();
})

function addDetailPlan() {
	$("[name='userName']:last").trigger("change")
	if(($("id").val() == null) || ($("id").val()==-1)) {
		doSaveActionList('0001',function(){
			var dtoArr = [{
				actionListId:$("#id").val()
			}];
			doJsonRequest("/actionList/saveActionListItem",dtoArr,function(data){
				if(data.result) {
					doQuery();
				} else {
					$.alert("添加失败。");
				}
			},{showWaiting:true})	
		})		 
	} else {
		var dtoArr = [{
			businessId:$("#id").val()
		}];
		doJsonRequest("/actionList/saveActionListItem",dtoArr,function(data){
			if(data.result) {
				doQuery();
			} else {
				$.alert("添加失败。");
			}
		},{showWaiting:true})			
	}

	
}

function saveDetailPlan(fun, status) {
	if(status != "0001") {
		if(!valide("dataInputForm")) {
			return false;
		}
	}
	var objArr = new Array();
	$("tbody tr",clickmedTables.actionListQuery.table).each(function(){
		var obj = {};
		$(this).children().find("input").each(function(){
				obj[$(this).attr("name")] = $(this).val();
		})
		
		$(this).children().find("textarea").each(function(){
				obj[$(this).attr("name")] = $(this).val();
		})
		objArr.push(obj);
	})
	doJsonRequest("/actionList/saveActionListItem",objArr,function(data){
		if(data.result) {
			fun(status);
			if(status == "0001") {
				$.alert("已暂存至一事一表草稿箱");
			}
		} else {
			$.alert("添加失败。");
		}
	},{showWaiting:true})
}

function doSubmit(status) {
	saveDetailPlan(doSaveActionList, status)
}

function doSaveActionList(status,fun) {
	var tempId = null;
	
	if(($("#id").val()==null)||($("#id").val()==-1)) {
		tempId = null;
	} else {
		tempId = $("#id").val()
	}
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});
	var obj = {
			id:tempId,
			status:status,
			actionContent:$("#actionContent").val(),
			businessType:$("#businessType").val(),
			businessTitle:$("#businessTitle").val(),
			businessId:$("#businessId").val(),
			dcRoleCode:$("#dcRoleCode").val(),
			dcRoleName:$("#dcRoleName").val(),
			title:$("#title").val(),
			tobeCreatedId:tobeCrId,
			attachmentDTOs:attachmentDTOs,
			tobeCreatedUserId:$("#tobeCrUserId").val(),
			tableName:$("#tableName").val()
	}
	doJsonRequest("/actionList/saveActionList",obj,function(data){
		if(data.result) {
			var data= data.data;
			$("#id").val(data.id)
			if(fun) {
				fun();
			}
			if(status == "0002") {
				if($("#dcRoleName").val()!=''){
//					$.alert("成功提交至"+$("#dcRoleName").val());
					window.location.href='actionlistSuccess.html?dcRoleName='+$("#dcRoleName").val();
				}else{
//					$.alert("成功提交至督查室");
					window.location.href='actionlistSuccess.html';
				}
				hideBtns()
			}
		} else {
			$.alert("添加失败。");
		}
	},{showWaiting:true})
}


function initFun(data, key) {
	if(key == 'completeDate'){
		return ('<div name="formatDivX" class="input-group" style="padding: 0px;width:120px"><input type="text"  filedName="completeDate" name="completeDate" class="form-control" disabled style="width:120px" placeholder="" value="'+((new Date(data.completeDate).format("yyyy-MM-dd"))==null?"":(new Date(data.completeDate).format("yyyy-MM-dd")))+'"/><input type="hidden" filedName="id" name="id" value="'+data.id+'"/></div>');
	}
	
	if(key == "userName") {
		var tempUserName = data.userName;
		if(data.userName == null) {
			tempUserName = currentUserName;
		}
		return ('<div name="formatDivX" class="input-group" style="padding: 0px;width:120px"><input type="text"  filedName="userName"  name="userName" value="'+tempUserName+'" class="form-control" style="width:120px" placeholder=""  value="'+((data.userEntity==null)?"":data.userEntity.userName)+'"/></div>');
	}
	
	if(key == "detailInfo") {
		return "<textArea name='detailInfo' filedName='detailInfo' onkeyup='autoSize(this)' class='form-control' style='float:right;width:97%;margin-top: -40px'  row='1'>"+(data.detailInfo==null?"":data.detailInfo)+"</textArea>";
	}
}

function hideBtns(){
	$("#btnArea").hide();
}
