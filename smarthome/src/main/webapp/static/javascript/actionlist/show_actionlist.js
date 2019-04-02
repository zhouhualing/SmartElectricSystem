var id =getURLParam("id");
/*var fromPage = getURLParam("fromPage");*/
var businessId = getURLParam("businessId");
var businessTitle =getURLParam("businessTitle");
var task = getURLParam("task");
var makeSure = getURLParam("makeSure");//1：督办单位桌面待办标识
var appFlag = getURLParam("appFlag");//1:来自应用
if(makeSure == 1){
	$("#superviseBtn").after('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button"  id="makeSureBtn" class="btn_click click_btn "   onclick="doMakeSureBtn()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;确认&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
}
/*if(task == 1){
	//当前为督办单位打开
}else if(task == 2){
	//当前承办单位打开
}*/
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
	$("#dcDate").html(new Date().format("yyyy年M月d日"));
	$("#actionListId").val(id);
	/*if(fromPage=='0001') {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>起草</a> >> <a href='draft_actionlist_list.html'>草稿列表</a> >> <a href='#'>编辑草稿</a>")
	}
	if(fromPage=='0002') {
		$(".right_nav").empty().append("您当前的位置：<a href='#'>创建一事一表</a>")
	}*/
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
				 $(".hideTd").hide();
				 if($("td",$(".files")).length ==0) {
					 $("#attachmentDiv").hide(); 
				 }
				 $("#actionContent").val(data.actionContent).trigger("keyup");
				 $("#businessType").val(data.businessType);
				 $("#businessTitle").val(data.businessTitle)
				 $("#businessId").val(data.businessId);
				 $("#dcRoleCode").val(data.dcRoleCode);
				 $("#dcRoleName").val(data.dcRoleName);
				 $("#title").val(data.title);
				 
			} else {
				$.alert("获取信息失败。");
			}
		}) 	
		
		//加载督办信息
		doJsonRequest("/actionList/getAllActionListSVMesDTOs",id,function(data){
			if(data.result) {
				var datas = data.data;
				createAlsvmessages(datas);
			}
		});
		
		//加载进度表
		doJsonRequest("/actionList/getAllActionListScheduleDTOs",id,function(data){
			if(data.result) {
				var datas = data.data;
				createAlsvSchedules(datas);
			}
		});
	}
	doQuery();
	/*doQuery("actionListSuperviseMessageQuery");*/
	
})

function createAlsvmessages(datas){
	var tr = "";
	if(datas!=null && datas.length!=0){
		$.each(datas,function(n,item){
			var flag = "";
			if(item.reMessage == null){
				flag = "isNullFlag";
			}
			tr += '<tr style="height: 37px; text-align: center;" name="alsvmItem"><input type="hidden" name="alsvmId" value="'+item.id+'"><td align="center" class="table_th">'+new Date(item.dcDate).format('yyyy年M月d日')+'</td>' 
				+ '<td align="center" class="table_th"><textarea name="message" onkeyup="autoSize(this)" class="form-control autoSizeTextarea" rows="1" readonly>'+(item.message==null?"":item.message)+'</textarea></td>'
			    + '<td align="center" class="table_th"><textarea name="reMessage" onkeyup="autoSize(this)" flag="'+flag+'" class="form-control autoSizeTextarea" rows="1" readonly>'+(item.reMessage==null?"":item.reMessage)+'</textarea></td></tr>';
		})
	}
	if(task == 1){
		tr += '<tr style="height: 37px; text-align: center;" ><td align="center" class="table_th" id="dcDate">'+new Date().format('yyyy年M月d日')+'</td>' 
		+ '<td align="center" class="table_th"><textarea id="message" name="message" onkeyup="autoSize(this)" class="form-control autoSizeTextarea" rows="1"></textarea></td>'
	    + '<td align="center" class="table_th"></td></tr>';
	}
	
	$("#alsvm").append(tr);
	
	if(task == 2){
		$("textarea[flag='isNullFlag']").removeAttr('readonly');
	}
}

function createAlsvSchedules(datas){
	
	var tr = "";
	if(datas!=null && datas.length!=0){
		$.each(datas,function(n,item){
			var files = item.files;
			var filesA = "";
			if(files!=null){
				$.each(files,function(m,it){
					filesA+="<a href='"+it.pdfUrl+"' class='"+it.id+"'>"+it.name+"</a>&nbsp;&nbsp;";
				});
			}
			var spanDom = "";
			if(task == 2){
				spanDom = '<span onclick="createOne(this,9000000000)" style="color: blue; cursor: pointer;">添加</span>';
			}
			tr += '<tr style="height: 37px; text-align: center;" name="alsvsItem"><input type="hidden" name="scheduleId" value="'+item.id+'"><td align="center" class="table_th" name="scDate">'+new Date(item.scDate).format('yyyy年M月d日')+'</td>' 
				+ '<td align="center" class="table_th"><textarea name="scMessage" onkeyup="autoSize(this)" class="form-control autoSizeTextarea" rows="1" readonly>'+(item.scMessage==null?"":item.scMessage)+'</textarea></td>'
			    + '<td align="center" class="table_th" name="attrs" id="atts_'+(n+1)+'">'+filesA+'</td>'
			    + '<td align="center" class="table_th">'+spanDom+'</td></tr>';
		})
	}else{
		if(task == 2){
			tr += '<tr style="height: 37px; text-align: center;" name="alsvsItem"><td align="center" class="table_th">'+new Date().format('yyyy年M月d日')+'</td>' 
			+ '<td align="center" class="table_th"><textarea id="scMessage" onkeyup="autoSize(this)" name="scMessage" class="form-control autoSizeTextarea" rows="1" ></textarea></td>'
		    + '<td align="center" class="table_th" name="attrs" id="atts_1"></td>'
		    + '<td align="center" class="table_th"><span onclick="openUpLoad(null,\'atts_1\')" style="color: blue; cursor: pointer;">上传附件</span>&nbsp;&nbsp;<span onclick="delUpLoadData(\'atts_1\')" style="color: blue; cursor: pointer;">删除附件</span>&nbsp;&nbsp;<span onclick="createOne(this,9000000000)" style="color: blue; cursor: pointer;">添加</span></td></tr>';
		}
	}
	$("#schedule").append(tr);
	
	/*if(task == 1){
		//督办部门进来只有查看，无上传附件和操作权限
	}else if(task == 2){
		$("#scMessage").removeAttr('readonly');
	}*/
}
function createOne(dom,num){
	$(dom).parent().parent().after('<tr style="height: 37px; text-align: center;" name="alsvsItem"><td align="center" class="table_th">'+new Date().format('yyyy年M月d日')+'</td>' 
			+ '<td align="center" class="table_th"><textarea id="scMessage" onkeyup="autoSize(this)" name="scMessage" class="form-control autoSizeTextarea" rows="1"></textarea></td>'
		    + '<td align="center" class="table_th" name="attrs"  id="atts_'+(num+1)+'"></td>'
		    + '<td align="center" class="table_th"><span onclick="openUpLoad(null,\'atts_'+(num+1)+'\')" style="color: blue; cursor: pointer;">上传附件</span>&nbsp;&nbsp;<span onclick="delUpLoadData(\'atts_'+(num+1)+'\')" style="color: blue; cursor: pointer;">删除附件</span>&nbsp;&nbsp;<span onclick="createOne(this,'+(num+1)+')" style="color: blue; cursor: pointer;">添加</span>&nbsp;&nbsp;<span onclick="delOne(this,'+(num+1)+')" style="color: blue; cursor: pointer;">删除</span></td></tr>');
}

function delOne(dom){
    $(dom).parent().parent().remove();     
}

function openUpLoad(id,thisId){
	var obj = {
		    title:'上传附件',
		    height:"350px",
		    width:"650px",
		    url:'upload_page.html?id='+id+"&thisId="+thisId,
		    myCallBack:"getUpLoadData"
		};
		new jqueryDialog(obj);
}

function getUpLoadData(data){
	var $_dom = $("#"+data.thisId);
	var dom = "";
	var attachmentDTOs = data.attachmentDTOs;
	$.each(attachmentDTOs,function(n,attr){
		dom += "<a class='"+attr.id+"' name='"+data.id+"' href='"+attr.url+"'>"+attr.title+"</a>&nbsp;&nbsp;"
	})
	$_dom.append(dom);
}

function delUpLoadData(id){
	$("#"+id).html("");
}

function queryEnd () {
	clickmedTables.actionListShowQuery.hideFoot();
	$("textarea").trigger("keyup");
	$("[name='completeDate']").datepicker({
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
		timeText:'时间',
		buttonType:true
	});
	
	$("[name='testDate']").datepicker({
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
/*
function queryEnd1 () {
	clickmedTables.actionListSuperviseMessageQuery.hideFoot();
	if(clickmedTables.actionListSuperviseMessageQuery.getCount() == 0) {
		$("#hidDiv1").hide();
	}
}*/

$(function(){
	getCurrenUserInfo(function(data){
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



/*function onChangePage(_this) {
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
}*/


function goBack() {
	if(appFlag==1){
		history.go(-1);
	}else{
		$('#desk .window-container',parent.document).each(function(){
			parent.HROS.window.close($(this).attr('appid'));
		});
	}
	
//	window.location.href = "actionlist_me.html";
}
$("#addDetailPlan").on("mouseover",function(){
	$(this).addClass("ui-state-hover")
}).on("mouseout",function(){
	$(this).removeClass("ui-state-hover")
}).on("click",function(){
	addDetailPlan();
})

function addDetailPlan() {
	
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
	var objArr = new Array();
	$("tbody tr",clickmedTables.actionListShowQuery.table).each(function(){
		var obj = {};
		$(this).children().find("input").each(function(){
				obj[$(this).attr("name")] = $(this).val();
		})
		
		$(this).children().find("textarea").each(function(){
				obj[$(this).attr("name")] = $(this).val();
		})
		objArr.userName = null;
		objArr.push(obj);
	})
	doJsonRequest("/actionList/saveActionListItem",objArr,function(data){
		if(data.result) {
			fun(status);
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
			title:$("#title").val(),
			dcRoleCode:$("#dcRoleCode").val(),
			dcRoleName:$("#dcRoleName").val(),
			attachmentDTOs:attachmentDTOs
	}
	doJsonRequest("/actionList/saveActionList",obj,function(data){
		if(data.result) {
			var data= data.data;
			$("#id").val(data.id)
			if(fun) {
				fun();
			}
		} else {
			$.alert("添加失败。");
		}
	},{showWaiting:true})
}

function wf_doSearchRole(id,fun) {
	var obj = {
	    title:'请输入备注信息',
	    height:"320px",
	    width:"750px",
	    url:'actionlist_complete_dialog.html?id='+id,
	    fun:true,
	    myCallBack:fun
	}
	new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}



function initFun(data, key) {
	if(key == "status") {
		if(data.status == "0002") {
			if(clickmedTables.actionListShowQuery.operatorArr[0]) {
				clickmedTables.actionListShowQuery.operatorArr[0].text = "";
			}
		} else {
			if(clickmedTables.actionListShowQuery.operatorArr[0]) {
				clickmedTables.actionListShowQuery.operatorArr[0].text = "完成";
			}
		}
	}
//	if(key == 'completeDate'){
//		return ('<div name="formatDivX" class="input-group" style="padding: 0px;width:120px"><input type="text"  filedName="completeDate" name="completeDate" class="form-control" disabled style="width:120px" placeholder="" value="'+((new Date(data.completeDate).format("yyyy-MM-dd"))==null?"":(new Date(data.completeDate).format("yyyy-MM-dd")))+'"/><input type="hidden" filedName="id" name="id" value="'+data.id+'"/></div>');
//	}
//	
//	if(key == "userEntity.userName") {
//		return ('<div name="formatDivX" class="input-group" style="padding: 0px;width:120px"><input type="hidden" filedName="userCode"  name="userCode"  value="'+((data.userEntity==null)?"":data.userEntity.userCode)+'"/><input type="text" disabled filedName="userName"  name="userName" class="form-control" style="width:120px" placeholder=""  value="'+((data.userEntity==null)?"":data.userEntity.userName)+'"/><span class="input-group-btn"><button name="selectUserTBtn" class="btn_select click_btn">选择</button></span></div>');
//	}
//	
//	if(key == "detailInfo") {
//		return "<textArea name='detailInfo' filedName='detailInfo' class='form-control'  row='1'>"+(data.detailInfo==null?"":data.detailInfo)+"</textArea>";
//	}
}

var editObj = {
		text:"完成",
		fun:function(data) {
			$.confirm({
				title:'警告信息',
				msg:"确定完成了么？",
				confirmClick:function(datas, result){
					wf_doSearchRole(data.id,function(datas){
						var dto = {
								id:data.id,
								status:'0002',
								completeMark:datas
						}
						doJsonRequest("/actionList/modifyItemStatus",dto,function(data){
							if(data.result) {
								doQuery();
							}
						})					
					})
					
				}
			})
		}
}
function doMakeSureBtn() {
		    	var dto = {
		    			id:id,
		    	}
				doJsonRequest("/report/doSuperviseActionList",dto,function(data){
					if(data.result) {
						$("#message").val("");
						$.alert("已确认。");
						hideBtns();
					}
				},{showWaiting:true})	
}

function doSupervise() {
//	var obj = {
//		    title:'请输入督办信息',
//		    height:"320px",
//		    width:"750px",
//		    url:'actionlist_complete_dialog.html?id='+id,
//		    fun:true,
//		    myCallBack:function(data){
	if($("#message").val() == ''){
		$.alert("请输入督办意见。");
		return false;
	}
		    	var dto = {
		    			id:id,
		    			message:$("#message").val(),
		    			dcDate:new Date()
		    	}
				doJsonRequest("/report/doSuperviseActionList",dto,function(data){
					if(data.result) {
						/*doQuery("actionListSuperviseMessageQuery");*/
//						$("#message").val("");
						window.location.href='superviseSuccess.html';
						hideBtns();
					}
				},{showWaiting:true})	
//		    }
//		}
//		new jqueryDialog(obj);
//		$(".dialog_div_").parent().addClass("wf_top");
}


var oppObj = [editObj];
if(task==1) {
	$("#superviseBtn").removeClass("hidden");
	oppObj = [];
}else if(task == 2){
	$("#submitBtn").removeClass("hidden");
}
function doSubmitBtn(){
	var subFlag = false;
	//承办单位提交督办回复信息和进度表
	var dto = {};
	var aLSVMessageDTOs = new Array();
	$("tr[name='alsvmItem']").each(function(n){
		var actionListSuperviseMessageDTO = {};
		var id = $(this).children('input[name="alsvmId"]').val();
		
		if(typeof($(this).contents().find('textarea[name="reMessage"]').attr("readonly")) == "undefined"){
			actionListSuperviseMessageDTO.id = $(this).children('input[name="alsvmId"]').val();
			/*actionListSuperviseMessageDTO.message = $(this).contents().find('textarea[name="message"]').val();*/
			var reMessage = $(this).contents().find('textarea[name="reMessage"]').val();
			if(reMessage == ''){
				subFlag = true;
			}
			actionListSuperviseMessageDTO.reMessage = reMessage;
			aLSVMessageDTOs[n] = actionListSuperviseMessageDTO;
		}
	})
	if(subFlag){
		$.alert("请输入回复意见。")
		return false;
	}
	var aLScheduleDTOs = new Array();
	$("tr[name='alsvsItem']").each(function(n){
		var actionListScheduleDTO = {};
		var attachmentDTOs  = new Array();
		$("a",this).each(function(){
			var obj = {
					id:$(this).attr('class')
				}
				attachmentDTOs.push(obj);
		});
		actionListScheduleDTO.id = $(this).children('input[name="scheduleId"]').val();
		actionListScheduleDTO.scDate = new Date();
		actionListScheduleDTO.scMessage = $(this).contents().find('textarea[name="scMessage"]').val();
		actionListScheduleDTO.attachmentDTOs = attachmentDTOs;
		aLScheduleDTOs[n] = actionListScheduleDTO;
	});
	
	dto.actionListSuperviseMessageDTOs = aLSVMessageDTOs;
	dto.actionListScheduleDTOs = aLScheduleDTOs;
	dto.task = task;
	dto.businessId = id;
	dto.dcRoleCode = $("#dcRoleCode").val();
	dto.businessTitle = $("#businessTitle").val();
	doJsonRequest("/actionList/setAllALMAndSDTOs",dto,function(data){
		if(data.result) {
			if(data.data == 'success'){
				window.location.href='actionlistSuccess.html?dcRoleName='+$("#dcRoleName").val();
//				$.alert("成功提交至"+$("#dcRoleName").val());
				hideBtns();
			}else{
				$.alert("提交失败。");
			}
			/*$.alert("提交成功。");
			hideBtns();*/
			/*history.back();*/
		}else{
			$.alert("提交失败。");
		}
	}/*,{showWaiting:true}*/);
}

function hideBtns(){
	$("#btnArea").hide();
}