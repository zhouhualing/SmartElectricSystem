var id = getURLParam("id");
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId"); 
var mark = getURLParam("mark"); 
var userTask = "";
$("#reportType").val(type);
$("#businessKey").val(id);
//打开时默认显示函件:
$(".nav-tabs a:first").tab("show");
var flagQRSH = false;//确认收函按钮
var allBtn = "revision_final_saveFile_savePDF";//weboffice按钮控制
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
//正文加载webOffice控件
var flag = false;
$("#textPage").click(function(){
	if(!flag){
		$("#textIframe").append('<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice.html?rightInfo=1&officeUrl=&taskid=&allBtn='+allBtn+'" style="z-index:9999;width:100%;height:800px;"></iframe>');
		flag = true;
	}
});

//回复
/*$("#replayBtn").on("click", function(){
	window.location.href="drafting_replayconsultation.html?fromPage="+fromPage+"&id="+id;
})*/


$(function(){
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		/*$("#redHeadTemp").val(data.redHeadTemp);*/
		var redHfes = data.redHeadFileEntities;
		for(var i = 0;i<redHfes.length;i++){
			var redHfe = redHfes[i];
			if(type == redHfe.reportType){
				var span = $("<span></span>");
				var btn = $(document.createElement("button")).addClass('btn').addClass('btn_click').attr('id','redBtn').attr('display','none').html(redHfe.mark).data('redData',redHfe);
				btn.appendTo(span);
				btn.click(function(){
					var thisRed = $(this).data('redData');
					//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
					var URL = baseRedHeadUrl+thisRed.redHeadTemp;
					var endUrl = baseRedHeadUrl+thisRed.endTemp;
					weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
					$(this).hide();
				});
				span.appendTo($("#buttonList"));
			}
		}
	});
	
	/*if(flag=="0001") {
		$("#replayTd").show();
	}
	$("#createDate").html(new Date().format("yyyy-MM-dd"))*/
	var dto = {
		id:id
	}
	$('#dataInputForm').addClass('fileupload-processing');
	doJsonRequest("/burLetter/getBurLetter", dto, function(data){
		if(data.result) {
			var data = data.data;
			var replyLetters = data.replyLetters;
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
			//letterType:'0001',
			$("#urgencySelect").val(data.urgency);
			$("#urgency").html($("#urgencySelect").find("option:selected").text());
			$("#reportType").val(data.reportType);
			
			doQueryWF("reportInfo","approvalDiv");
			var dto = {taskId:taskId}
			//初始化流程按钮
			wf_getOperator(dto,function(data){
				userTask = data.userTask;//获取当前流程
				if(userTask == 'usertask1'){
					//发起委局
					var table = $("<table width='100%' class='table_1'><tr><td colspan='3'>分发单位回执</td></tr><tr><td>序号</td><td>单位</td><td>回复</td><tr></table>");
					$("#orgReplayTd").append(table);
					var datas = replyLetters;
					for(var i=0; i<datas.length; i++) {
						var tdStr = "";
						if(datas[i].id != null) {
							if(datas[i].text == null) {
								tdStr = "确认收到"
							}  else {
								tdStr = "<a target='_blank' href='bureaus_letter_check.html?fromPage=0002&showLcgz=0&id="+datas[i].id+"' style='color:blue;line-height:4px'>查看回复</a>";
							}
							
						}
						var tr = $("<tr><td align='center'>"+(parseInt(i)+1)+"</td><td>"+datas[i].orgName+"</td><td>"+tdStr+"</td></tr>");
						table.append(tr);
					}				
				}else if (userTask == 'usertask2'){
					$("#textPage").show();
					$("#qrsh").show();
					/*//接收委局
					$("#buttonList").prepend('<span><button type="button"  class="btn btn_click"  id="redBtn" onclick="redBtn()" >套红</button></span>');*/
				}
			});
		} else {
			$.alert("获取函文出错。");
		}
		
	})
})

function confirmBtn(){
	$.confirm({msg:"直接确认收函么？",confirmClick:function(){
		flagQRSH = true;
		$("#hf button").trigger('click');
	},data:"1"})
}

//-----------------委办局函文，以上已经js完成------------------------------------------------

//发函参数
function getSendData(){
	return {id:id,flag:1};
}

//回函参数
function getReplyData(){
	if(flagQRSH == true){
		return {id:id,flag:1};
	}
	$("#webOfficeIframe")[0].contentWindow.saveFileToUrl();
	var datas =  $("#webOfficeIframe")[0].contentWindow.getInto();
	var dto = {
		contactPerson:datas.contactPerson,
		contactNumber:datas.contactNumber,
		orgCode:datas.createOrgCode,
		orgName:datas.createOrgName,
		reportTitle:datas.reportTitle,
		/*burLetterCode:datas.burLetterCode,*/
		//letterType:'0001',
		text:datas.text,
		urgency:datas.urgency,
		attachmentDTOs:datas.attachmentDTOs,
		reportType:$("#reportType").val()==""?"7001":$("#reportType").val(),
		status:"0000",
		mainId:id
	};
	return dto;
}

var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
    btnSource = fromPage;
}
function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&btnSource="+btnSource+"&mark="+mark;
}
function goSuccessEnd(data) {
	window.location.href="../tododocs/docs_down.html?flag=1&btnSource="+btnSource+"&mark="+mark;
}

function goSuccessReply(data) {
	window.location.href="../tododocs/docs_down.html?flag=4&btnSource="+btnSource+"&mark="+mark;
}

//校验
function wf_beforeValid(operaterId){
	if(flagQRSH == true){
		return true;
	}
	if (userTask == 'usertask2'){
		var datas =  $("#webOfficeIframe")[0].contentWindow.getInto();
		//文件名称不能为空
		if(datas.reportTitle == ''){
			$.alert("请输入文件标题");
			return false;
		}
		if(datas.urgency == ''){
			$.alert("请选择密级");
			return false;
		}
		if(!datas.text.endWith(".pdf")) {
			$.alert("请将正文转为PDF");
			return false;
		}
	}
	return true;
}

/*function redBtn(){
	//读取红头文件
	weboffice.window.insertRedHeadFromUrl($("#redHeadTemp").val(),"","","","");
};*/

function wf_getDate(data) {
	var urgency = "";
	if (userTask == 'usertask1'){
		urgency = $("#urgencySelect").val();
	}else if(userTask == 'usertask2'){
		urgency = $("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val();
	}
	if( urgency == '0001') {
		return getTheDate(86400);
	}
	if(urgency == '0002') {
		return getTheDate(86400);
	}
	if(urgency == '0003') {
		return getTheDate(86400);
	}
	if(urgency == '0004') {
		return getTheDate(86400);
	}
}

function showPDF(_this){
	$(_this).attr("href",$(_this).attr("theUrl")).attr("target","_blank")
}


//页面的审批意见带到备注信息中
function wf_getMark(operaterId){
	if(operaterId == 'flow3' && flagQRSH == true){
		return "收到";
	}
}
/*

var orgCode = "";
$(function(){
	getCurrenUserInfo(function(data){
		orgCode = $.trim(data.roleCodes);
	})
})
$("#confirmBtn").on("click",function(){
	var dto = {
			orgCode:orgCode,
			title:"确认收函",
			mailId:id,
			type:'0002'
	}
	doJsonRequest("/consultation/doSave",dto,function(data){
		if(data.result) {
			window.location.href=window.location.href="consulation_skip.html?fromPage="+fromPage;
		} else {
			$.alert("回函失败。");
		}
	},{showWaitting:true})	
})

	function clostApp() {
		$('#desk .window-container',parent.document).each(function(){
			parent.HROS.window.close($(this).attr('appid'));
		});
		//parent.HROS.copyright.hideP();
	}

if(fromPage=="0002") {
	$("#submitBtn").html("关闭")
	$("#submitBtn").on("click",function(){
		window.close();
	})
	$("#removeTable").remove();
	$("#replayBtn").hide();
} else if(fromPage=="pc"){
	$("#submitBtn").html("关闭")
	$("#submitBtn").on("click",function(){
		clostApp();
	});
	$("#confirmBtn").removeClass("hidden");

}else {
	$("#submitBtn").on("click",function(){
		goBack();
	})
}


function doSend() {
	
	var mailAttachmentDTOs = new Array();
	$("input[name='attachmentId']", $("#attachmentDiv")).each(function(){
		var obj = {
			id:$(this).val()
		}
		mailAttachmentDTOs.push(obj);
	});
	
	if(mailAttachmentDTOs.length <= 0) {
		$.alert("必须上传函件。");
		return false;
	} 
	wf_doSearchRole(function(data){
		var attachmentDTOs = new Array();
		$("input[name='attachmentId']", $("#attachmentDiv1")).each(function(){
			var obj = {
				id:$(this).val()
			}
			attachmentDTOs.push(obj);
		});
		var dto = {
				contactPerson:$("#contactPerson").val(),
				contactNumber:$("#contactNumber").val(),
				orgCode:$("#createOrgCode").val(),
				title:$("#title").val(),
				mark:$("#mark").val(),
				number:$("#number").val(),
				roleCodes:data.roleCode,
				completeDate:data.completeDate,
				mailAttachmentDTOs:mailAttachmentDTOs,
				attachmentDTOs:attachmentDTOs
		}
		
		doJsonRequest("/consultation/doSave",dto,function(data){
			if(data.result) {
				window.location.href="drafting_consultation.html";
			} else {
				$.alert("发送失败。");
			}
		},{showWaitting:true})		
	})
}

function wf_doSearchRole(fun) {
	var str = "checkType=multi";
	var roleCodes = "035,036"
	var obj = {
	    title:'选择发往单位',
	    height:"320px",
	    width:"750px",
	    url:'../users/send_role_dialog.html?roleCodes='+roleCodes,
	    fun:true,
	    myCallBack:fun
	}
	new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}*/