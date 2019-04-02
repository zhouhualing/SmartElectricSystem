/*usertask11：秘书室
usertask12：副市长
usertask13：承办部门*/


//打开时默认显示正文页:
var mark = getURLParam("mark");
var id = getURLParam("id"); //当前收文id
var taskId = getURLParam("taskId");
$("#id").val(id);
$("#businessKey").val(id);
var fromPage = getURLParam("fromPage");
var btnTask = getURLParam("btnTask");//上级页面点击的是副市长批示（Fsz）还是承办部门处理(Cbbm)
var btnSource = "app";
var fromRoleCode = getURLParam("fromRoleCode");
if(fromPage == 'pc'){
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
	//初始化流程按钮
	wf_getOperator("superviseSZBranch",function(data){
		if(btnTask == 'Fsz'){
			$("#flow12 button").trigger("click");
		}else if(btnTask == 'Cbbm'){
			$("#flow14 button").trigger("click");
		}
	});
	if(id != null) {
		var dto = {id:id}
		doJsonRequest("/super/getSuperVise",dto,function(data){
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
				$("#attr").val(attr);//.data('d',data)
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

//fszOpinion，unitSpan，content，remark，附件
function getAddData(){
	/*var mailAttachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		mailAttachmentDTOs.push(obj);
	});
	if(mailAttachmentDTOs!=null){
		dto.attachmentDTOs =mailAttachmentDTOs;
	}
	var fszOpinion = makeOpinions($("#fszOpinion").val(),$("#ldyj").val());
	var unitOpinion = makeOpinions($("#unitSpan").html(),$("#unitOpinion").val());*/
	var dto = {
			relationId:$("#id").val(),
			content:$("#content").val(),
			remark:makeOpinions($("#remarkSpan").html(),$("#remark").val()),
			szOpinion:$("#szOpinion").val(),
			fszOpinion:$("#fszOpinion").val(),
			unitOpinion:$("#unitSpan").html(),
			status:"00002",
			title:$("#title").val(),
			issue:$("#issue").html(),
			superCode:$("#superCode").html(),
			completeDate:$("#completeDate").val().split('日')[0].replace(/[^\d]/g,'-'),
			superDate:$("#superDate").html().split('日')[0].replace(/[^\d]/g,'-'),
			units:$("#units").val(),
			taskId:taskId,
			fromRoleCode:fromRoleCode,
			flag:0
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