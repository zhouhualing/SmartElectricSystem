var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");  
var lcbz = "";
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
$("#qsy a").tab("show");
$(".nav-tabs a:first").tab("show");

//正文加载webOffice控件
var flag = false;
/*var attaNames="";*///正文附件
$("#textPage").click(function(){
	if(!flag){
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice6.html?officeUrl='+$("#text").val()+'&taskid='+id+'"style="width:100%;height:800px;"></iframe>');
		flag = true;
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
    
});
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
//				if(null!=data.files[0]){
//					$("#mailFrame").attr("src",data.files[0].pdfUrl);
//					 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
//					 $(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)")
//				}
				//来文类型
				$("#reportType").val(data.reportType);
				//来文单位
				$("#docCameOrgan").val(data.docCameOrgan);
				//文号
				$("#receiveCode").val(data.receiveCode);
				//秘密等级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//紧急程度
				$("#urgency").val(data.urgency);
				//文件标题
				$("#receiveTitle").val(data.receiveTitle);
				//秘书审核
				$("#fmszOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				var mszpsAudit = "";
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszpsAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					mszpsAudit += data.fmszOpinion;
				}
				$("#mszpsAudit").html(mszpsAudit);
				//市长审核
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var ldpsAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					ldpsAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					ldpsAudit += data.fszOpinion;				
				}
				$("#ldpsAudit").html(ldpsAudit);
				$("#text").val(data.text);//正文,暂时的
				//呈批意见
				$("#hgOpinion").val(data.hgOpinion);
				//备注
				$("#remark").val(data.remark);
				//正文
				$("#text").val(data.text);
				$("#drafterName").val(data.docCameOrgan);
				$("#drafterPhoneNum").html(data.drafterPhoneNum);
				if(data.receiveDate!=''&&data.receiveDate!=null){
					$("#receiveDate").val(new Date(data.receiveDate).format('yyyy年MM月dd日'));
				}
				$("#lwCode").html(data.lwCode);
				$("#upCardFlag").val(data.upCardFlag);
				$("#createUserName").val(data.createUserName);
				//小纸条
				$("#liPage").val(data.littlePage);
				//当前是否是退回办理人
				$("#meetDate").val(data.meetDate);
				
				$("#isReturn").val(data.isReturn);
				$("#send").val(data.send);
				$("#sendOut").val(data.sendOut);
				$("#receiveText").val(data.receiveText);
				doQueryWF("reportInfo","approvalDiv");
				//初始化流程按钮
				var dto = {
					taskId:taskId	
				}
				wf_getOperator(dto,function(data){

					userTask = data.userTask;//获取当前流程
					//根据当前步骤控制页面可输入项:
					
					if(userTask == 'usertask7' && $("#isReturn").val()==1){
						//后续要加上,当退回到办理人修改的时候的逻辑.
						$("#docCameOrgan").removeAttr("disabled");
						$("#receiveCode").removeAttr("disabled");
						$("#urgency").removeAttr("disabled");
						$("#receiveTitle").removeAttr("disabled");
						$("#lwCode").removeAttr("disabled");
						$("#remark").removeAttr("disabled");
						
						/*var date = new Date();
						var receiveDate = date.format('yyyy年MM月dd日');
						$("#receiveDate").html(receiveDate);
						$("#drafterName").html($("#userName").val());
						$("#drafterPhoneNum").html($("#phoneNumber").val());*/
						
					}
					if(userTask == 'usertask7' && $("#isReturn").val()!=1){
						//后续要加上,当退回到办理人修改的时候的逻辑.
						$("#meetDate").removeAttr("disabled");
					}
					if(userTask == 'usertask8' ){
						lcbz = "收到";
					}
					if(userTask == 'usertask2'){
						$("#hgOpinion").removeAttr("disabled");
					}
					if(userTask == 'usertask3'){
						$("#fmszOpinion").removeAttr("disabled");
					}
					if(userTask == 'usertask4'){
						$("#mszOpinion").removeAttr("disabled");
					}
					if(userTask == 'usertask5'){
						$("#ldps").removeAttr("disabled");
					}
					if(userTask == 'usertask6'){
						$("#ldps").removeAttr("disabled");
						
						$("#littlePage").show();
						if($("#userName").val()!='李俊明'){
							$("#ldps").attr('disabled',true);
							$("#liPage").removeAttr("disabled");
							$("#buttonList span").hide();
							$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存</button>");
						}
					}
					//会签
					if(userTask == 'usertask9'){
						$("#receiveText").removeAttr("disabled");
					}
					//优化领导签批样式：
					if($("#ldps").attr("disabled") == "disabled"){
						$("#ldps").hide();
					}
//					if($("#mszps").attr("disabled") == "disabled"){
//						$("#mszps").hide();
//					}
				});
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
});
//function showPDF(_this){
//	$("#mailFrame").attr("src",$(_this).attr("theUrl"))
//}
function getAddData(){
	
	//核稿
	var hgOpinion = $("#hgOpinion").val();
	if(userTask == 'usertask2'){
		if(hgOpinion!='' && hgOpinion.indexOf("  [")<=0){
			hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-MM-dd hh:mm') + "]";
		}
	}
	//秘书长审批
	var fmszOpinion = $("#fmszOpinion").val();
	var mszOpinion = $("#mszOpinion").val();
	
//	var mszps = $("#mszps").val();
//	if(mszps!=''){
//		mszps = mszps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]";
//		if(userTask == 'usertask3'){
//			//如果当前环节为副秘书长审批
//			if(fmszOpinion!=''){
//				fmszOpinion = mszps + "<br>" + fmszOpinion;
//			}else{
//				fmszOpinion = mszps;
//			}
//		}else if(userTask == 'usertask4'){
//			//如果当前环节为秘书长审批
//			if(mszOpinion!=''){
//				mszOpinion = mszps + '<br>' + mszOpinion;
//			}else{
//				mszOpinion = mszps;
//			}
//		}
//	}
	
	//市长审批
	var fszOpinion = $("#fszOpinion").val();
	var szOpinion = $("#szOpinion").val();
	var ldps = $("#ldps").val();
	if(ldps!=''){
		ldps = ldps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]";
		if(userTask == 'usertask5'){
			//如果当前环节为副市长审批
			if(fszOpinion!=''){
				fszOpinion = ldps + '<br>' + fszOpinion;
			}else{
				fszOpinion = ldps;
			}
		}else if(userTask == 'usertask6'){
			//如果当前环节为市长审批
			if(szOpinion!=''){
				szOpinion = ldps + '<br>' + szOpinion;
			}else{
				szOpinion = ldps;
			}
		}
	}
	
	
	/*var attachmentDTOs = new Array();
	//保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		$("#text").val(officefileUrl);
	}*/
	var dto = {};
	if(userTask == 'usertask7'){
		dto = {
				id:$("#id").val(),
				status:'0002',
				drafterCode:$("#userCode").val(),
				meetDate:$("#meetDate").val(),
				flag:1,//当前为审批流程.
				upCardFlag:'2',
			};
	}else if(userTask == 'usertask2'){
		//核稿
		dto = {
				id:$("#id").val(),
				//呈批意见
				hgOpinion:hgOpinion,
				//附件
				//attachmentDTOs:attachmentDTOs,
				status:'0002',
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask3'){
		//秘书长审核
		dto = {
				id:$("#id").val(),
				//秘书审核
				fmszOpinion:fmszOpinion,
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask4'){
		//秘书长审核
		dto = {
				id:$("#id").val(),
				//秘书审核
				mszOpinion:mszOpinion,
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask5' ||  userTask == 'usertask6'){
		//市长审核
		dto = {
				id:$("#id").val(),
				//市长审核
				fszOpinion:fszOpinion,
				szOpinion:szOpinion,
				//附件
				//attachmentDTOs:attachmentDTOs,
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask9'){
		//市长审核
		dto = {
				id:$("#id").val(),
				//市长审核
				receiveText:$("#receiveText").val()+ "  [" + $("#userName").val() + "  " + new Date().format('yyyy-MM-dd hh:mm') + "]",
				//附件
				//attachmentDTOs:attachmentDTOs,
				flag:1,//当前为审批流程.
			};
	}else{
		dto = {
				id:$("#id").val(),
				flag:1,//当前为审批流程.
		};
	}
		return dto;
}
var btnSource = "app";
if(fromPage == 'pc'){
	btnSource = fromPage;
}
function goSuccess(data) {
	/*if(data.operaterId == 'flow16' && data.assignUserName != null){
		//起草页面,如果当前操作为提交文书科,且文书科角色下只对应一个用户
		//addTransactor(data);
	}*/
	var roleName = data.assignRoleName;
	window.location.href="receivedoc_down.html?roleName="+roleName+"&fromPage=0002"+"&btnSource="+btnSource;
}
//分发
function goSuccessSend() {
	window.location.href="receivedoc_down.html?fromPage=0002"+"&flag=0"+"&btnSource="+btnSource;
}
//办结
function goSuccessEnd() {
	window.location.href="receivedoc_down.html?fromPage=0002"+"&flag=1"+"&btnSource="+btnSource;
}


function wf_getDate(data) {
	if($("#urgency").val() == '0001') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0002') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0003') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0004') {
		return getTheDate(86400);
	}
	return getTheDate(86400);
}



//校验
function wf_beforeValid(){
	//文件名称不能为空
	if($("#receiveTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	}
	//秘密等级
	if($('input:checkbox[name="secretLevel"]:checked').val()==null){
		$.alert("请确认此文件为非涉密件");
		return false;
	}
	//紧急程度
	if($("#urgency").val() == ''){
		$.alert("请选择紧急程度");
		return false;
	}
	
	//呈批意见
	if(userTask == 'usertask2' && $("#hgOpinion").val() == ''){
		$.alert("请输入呈批意见");
		return false;
	}else if((userTask == 'usertask3' ||  userTask == 'usertask4') && $("#mszps").val()==''){
	//秘书长审核
		$.alert("请输入批示意见");
		return false;
	}else if((userTask == 'usertask5' ||  userTask == 'usertask6') && $("#ldps").val()==''){
	//领导审核
		$.alert("请输入批示意见");
		return false;
	}
	return true;
}

//小纸条获取焦点时自动取消 名字+时间
$("#liPage").on('focus',function(){
	var liPage = $("#liPage").val();
	if(liPage!=''){
		liPage = liPage.split("  [")[0];
	}
	$("#liPage").val(liPage);
});
//保存小纸条信息
function saveLittlePage(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-MM-dd hh:mm') + "]";
	}
	var dto = { id:id,littlePage:lps };
	doJsonRequest("/receivedoc/addLittlePage",dto,function(data){
		window.location.href="receivedoc_down.html?fromPage=0002&flag=2&btnSource="+btnSource;
	},{showWaiting:true});
}
function wf_getMark(operaterId){
	return lcbz;
}