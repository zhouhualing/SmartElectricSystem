//打开时默认显示正文页:

var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
$("#reportType").val(type);
var date = new Date();
var receiveDate1 = date.format('yyyy年MM月dd日');
var lwCode = "";
lwCode="【"+new Date().getFullYear()+"】";
//打开时默认显示请示页:
$(".nav-tabs a:first").tab("show");
//正文加载webOffice控件
var flag = false;
var saveflag = true;
$(function(){
//	$("#qsy").children("a").trigger("click");
	$("#textPage").click(function(){
		if(!flag){
			$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice6.html?officeUrl='+$("#text").val()+'&taskid='+id+'" style="width:100%;height:500px;"></iframe>');
			flag = true;
		}
	});
//	.mouseout(function(){
//		if(flag&&saveflag){	
//		weboffice.window.saveFileToUrl();
//		saveflag = false;
//		}
//	});
//	$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice6.html?officeUrl=&taskid=" style="z-index:9999;width:100%;height:500px;"></iframe>');
//    $('#dataInputForm').fileupload({
//        disableImageResize: false,
//        url: '/cmcp/attachment/upload'
//    });
//    $('#dataInputForm').fileupload(
//        'option',
//        'redirect',
//        window.location.href.replace(
//            /\/[^\/]*$/,
//            '/cors/result.html?%s'
//        )
//    );
	//初始化流程按钮
	wf_getOperator("minutesMeeting",function(data){
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
		$("#drafterName").val(data.userName);
		$("#drafterPhoneNum").val(data.phoneNumber);
//		$("#receiveDate").val(receiveDate1);
	});
	
	$("span[name='lwCode']").text("【"+new Date().getFullYear()+"】");
	doJsonRequest("/code/getCode","5001",function(data){
		if(data.result) {
			$("#lwCode").val(data.data);
		} else {
			$.alert("获取编码出错。");
		}
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				//正文
				//编号
				$("#text").val(data.text);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else{
		$("#urgency").val('0004');
	}
});

function getAddData(){
	/*var attachmentDTOs = new Array();
	//保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		$("#text").val(officefileUrl);
	}*/
	var attachmentDTOs = new Array();
	//保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		$("#text").val(officefileUrl);
	}
	//封装 公开属性原由:
	var openTypeReason = $("#openTypeReason").val();
	if(openTypeReason!=''){
		openTypeReason = openTypeReason + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-MM-dd hh:mm") + "]";
	}
//var datas = $("#webOfficeIframe")[0].contentWindow.getInto();
		var dto = {
			drafterCode:$("#userCode").val(),
			drafterName:$("#userName").val(),
			drafterOrgName:$("#userOrg").val(),
			drafterPhoneNum:$("#phoneNumber").val(),
//			receiveDate:new Date(),
			id:$("#id").val(),
			send:$("#send").val(),
			sendOut:$("#sendOut").val(),
			reportType:$("#reportType").val(),
			//正文
			text:$("#text").val(),
			//来文单位
			docCameOrgan:$("#drafterName").val(),
			//文号
			receiveCode:$("#receiveCode").val(),
			//非涉密
			secretLevel:$("#secretLevel").val(),
			//注秘注急
			urgency:$("#urgency").val(),
			//来文标题
			receiveTitle:$("#receiveTitle").val(),
			//来文编号
			lwCode:lwCode,
			//备注
			remark:$("#remark").val()+ "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]",
			upCardFlag:'2',
			/*//附件
			attachmentDTOs:attachmentDTOs,*/
			status:'0002',
			flag:0,//当前为新建流程.
			text:$("#text").val(),//正文,暂时的
			attachmentDTOs:attachmentDTOs
		};
		return dto;
}

//保存到草稿箱
$("#tempSubmitBtn").on("click",function(){
	var dto = getAddData();
	dto.status = "0001";
	doJsonRequest("/receivedoc/addReceiveDoc",dto,function(data){
		if(data.result) {
			var data = data.data;
			$("#id").val(data.id);
			$.alert("暂存成功。"+ $("#id").val());
		} else {
			alert("添加报告出错。");
		}
	},{showWaiting:true});
});


function goSuccess(data) {
	if(data.operaterId == 'flow16' && data.assignUserName != null){
		//起草页面,如果当前操作为提交文书科,且文书科角色下只对应一个用户
		addTransactor(data);
	}
	var roleName = data.assignRoleName;
	window.location.href="../senddoc/senddoc_down.html?roleName="+roleName+"&fromPage=0001";
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
	
	return true;
}