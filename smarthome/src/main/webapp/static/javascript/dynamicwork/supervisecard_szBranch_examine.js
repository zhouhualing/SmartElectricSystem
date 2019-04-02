/*usertask11：秘书室
usertask12：副市长
usertask13：承办部门*/


//打开时默认显示正文页:
var mark = getURLParam("mark");
var id = getURLParam("id"); //当前收文id
$("#id").val(id);
/*$("#businessKey").val(id);*/
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
var userTask = "";
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
		doJsonRequest("/super/getBranchSuper",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(null!=data.files[0]){
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
				$("#units").val(data.units);
				$("#relationId").val(data.relationId);
				
				if(data.relationId != null) {
					$("#businessKey").val(data.relationId);
					doQueryWF("reportInfo","approvalDiv");
				}
				$("#taskId").val(data.taskId);
			} else {
				$.alert("获取信息失败。");
			}
			
			var dto = {taskId:taskId}
			//初始化流程按钮
			wf_getOperator(dto,function(data){
				userTask = data.userTask;//获取当前流程
				$(".hideTd").hide();
				if(userTask == "usertask11"){
					//秘书室
					if($("#unitSpan").html()==''){
						$("#content").removeAttr("readOnly");
					}
					$("#remark").removeAttr("readOnly");
				}else if(userTask == "usertask12"){
					//副市长
					if($("#unitSpan").html()==''){
						$("#content").removeAttr("readOnly");
					}
					$("#ldyj").removeAttr("readOnly");
				}else if(userTask == "usertask13"){
					//承办部门
					$(".fileupload-buttons").removeClass('hidden');
					/*$(".hideTd").show();*/
					$("#unitOpinion").removeAttr("readOnly");
					$("#remark").removeAttr("readOnly");
				}
				
				if($("#ldyj").attr('readOnly') == 'readonly'){
					$("#ldyj").hide();
				}
				if($("#unitOpinion").attr('readOnly') == 'readonly'){
					$("#unitOpinion").hide();
				}
				if($("#remark").attr('readOnly') == 'readonly'){
					$("#remark").hide();
				}
			});
		});
	}
});
$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' +officeUrl+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});
//点击流程跟踪:将所有子流程都显示出来
$("#lcgz").click(function(){
	var length = $("#tab4 input[name='businessKey']").length;
	for(var i = 1;i<length;i++){
		doQueryWF("reportInfo"+i,"approvalDiv"+i);
	}
})

function getAddData(){
	var dto = {
		id:$("#id").val(),
		relationId:$("#relationId").val(),
		flag:1
	}
	if(userTask!="usertask13"){
		//除承办部门之外的环节都可以修改正文内容
		dto.content = $("#content").val();
	}
	if(userTask != "usertask12"){
		//秘书室、承办部门可以填写备注
		dto.remark = makeOpinions($("#remarkSpan").html(),$("#remark").val());
	}
	
	if(userTask == "usertask12"){
		//副市长
		dto.fszOpinion = makeOpinions($("#fszOpinion").val(),$("#ldyj").val());
	}else if(userTask == "usertask13"){
		//承办部门
		var mailAttachmentDTOs = new Array();
		$("input[name='attachmentId']").each(function(){
			var obj = {
				id:$(this).val()
			}
			mailAttachmentDTOs.push(obj);
		});
		dto.unitOpinion = makeOpinions($("#unitSpan").html(),$("#unitOpinion").val());
		if(mailAttachmentDTOs!=null){
			dto.attachmentDTOs =mailAttachmentDTOs;
		}
		dto.units = getUnits();
	}
	return dto;
}

function getUnits(){
	var units = "";
	var localUnit = $("#userOrg").val();
	if($("#units").val()!=''){
		var hisUnits = $("#units").val().split('<br>');
		for(var i=0;i<hisUnits.length;i++){
			if(hisUnits[i]!=localUnit){
				units+=hisUnits[i]+"<br>";
			}
		}
	}
	units += localUnit;
	return units;
}

function makeOpinions(lastOpinion,localOpinion){
	var retOpinion = "";
	if(lastOpinion!='' && localOpinion!=''){
		retOpinion = localOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]<br>"+lastOpinion;
	}else if(lastOpinion == '' && localOpinion!=''){
		retOpinion = localOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	}else if(lastOpinion != '' && localOpinion==''){
		retOpinion = lastOpinion;
	}
	return retOpinion;
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&mark="+mark+"&fromPage=0002&btnSource="+btnSource;
}

function goSuccessSend(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&mark="+mark+"&flag=4&btnSource="+btnSource;
}

function goMainFolw(){
	//返回主流程前先将子流程内容添加到主线流程
	var dto={
		id:id,
		relationId:$("#relationId").val(),
		content:$("#content").val(),
		remark:makeOpinions($("#remarkSpan").html(),$("#remark").val()),
		fszOpinion:$("#fszOpinion").val(),
		unitOpinion:$("#unitSpan").html(),
		units:$("#units").val(),
	}
	doJsonRequest("/super/copy2MainSuperCard",dto,function(data){
		window.location.href="supervisecard_sz_examine.html?id="+$("#relationId").val()+"&mark="+mark+"&fromPage="+fromPage+"&taskId="+$("#taskId").val()+"&btnTask="+$("#taskId").val();
	},{showWaiting:true});
}
//校验
function wf_beforeValid(){
	window.scrollTo(0,0);
	return true;
}

//校验
function wf_beforeValid(){
	window.scrollTo(0,0);
	if(userTask == "usertask3" || userTask == "usertask5" ){
		//市长&副市长
		if($("#ldyj").val() == ''){
			$.alert("请输入意见。");
			return false;
		}
	}else if(userTask == "usertask4" || userTask == "usertask6"){
		//承办部门
		if($("#unitOpinion").val() == ''){
			$.alert("请输入承办单位意见。");
			return false;
		}
	}
	return true;
}

function openAttachments(url,type,_this){
//	$("#mailFrame").attr("src",$(_this).attr("theurl"))
	weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}

$("#qsy").click(function(){
	//获取附件名称接口（供主页面使用）
	var attachmentNames = "";
	var i=1;
	$("input[name='attachmentId']").each(function(){
		attachmentNames=attachmentNames+i+"、"+$(this).next().html()+"；";
		i=i+1;
	});
	
	$("#attr").val(attachmentNames);
})