//打开时默认显示正文页:

var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
$("#reportType").val(type);
var date = new Date();
var receiveDate1 = date.format('yyyy年M月d日');
$(function(){
	$("#qsy").children("a").trigger("click");
	//初始化流程按钮
	wf_getOperator("getDocFromHigherV1",function(data){
	});
	doJsonRequest("/code/getCode","001",function(data){
		if(data.result) {
			$("#lwCode").val(data.data);
		} else {
			$.alert("获取编码出错。");
		}
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
		$("#drafterName").val(data.userName);
		$("#drafterPhoneNum").val(data.phoneNumber);
		$("#receiveDate").val(receiveDate1);
		if(id != null) {
			$("#id").val(id);
			var dto = {
					id:id
			}
			doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
				if(data.result) {
					var data = data.data;
					if(data.drafterName!=null && data.drafterName!=''){
						$("#drafterName").val(data.drafterName);
					}
					if(data.drafterPhoneNum!=null && data.drafterPhoneNum!=''){
						$("#drafterPhoneNum").val(data.drafterPhoneNum);
					}
					//正文
					$("#text").val(data.text);
					$("#reportType").val(data.reportType);
					//来文单位
					$("#docCameOrgan").val(data.docCameOrgan);
					//文号
					$("#receiveCode").val(data.receiveCode);
					//非涉密
					$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
					//注秘注急
					$("#urgency").val(data.urgency);
					//来文标题
					$("#receiveTitle").val(data.receiveTitle);
					//来文编号
					$("#lwCode").val(data.lwCode);
					//备注
					$("#remark").val(data.remark);
					$("#hgOpinion").val(data.hgOpinion);
					//
					$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid='+id+'" style="z-index:9999;width:100%;height:800px;"></iframe>');
				} else {
					$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid=" style="z-index:9999;width:100%;height:800px;"></iframe>');
					$.alert("获取信息失败。");
				}
			});
		}else{
			$("#urgency").val('0004');
			$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid=" style="z-index:9999;width:100%;height:800px;"></iframe>');
		}
	
	});
});

function getAddData(){
var datas = $("#webOfficeIframe")[0].contentWindow.getInto();
		var dto = {
			id:$("#id").val(),
			drafterCode:$("#userCode").val(),
			drafterName:$("#drafterName").val(),
			drafterOrgName:$("#userOrg").val(),
			drafterPhoneNum:$("#drafterPhoneNum").val(),
			receiveDate:new Date(),
			reportType:$("#reportType").val(),
			//正文
			text:$("#text").val(),
			//来文单位
			docCameOrgan:$("#docCameOrgan").val(),
			//文号
			receiveCode:$("#receiveCode").val(),
			//非涉密
			secretLevel:$("#secretLevel").val(),
			//注秘注急
			urgency:$("#urgency").val(),
			//来文标题
			receiveTitle:$("#receiveTitle").val(),
			//来文编号
			lwCode:$("#lwCode").val(),
			//备注
			remark:$("#remark").val(),
			upCardFlag:'2',
			/*//附件
			attachmentDTOs:attachmentDTOs,*/
			status:'0002',
			flag:0,//当前为新建流程.
			attachmentDTOs:datas.mailAttachmentDTOs,
			hgOpinion:$("#hgOpinion").val()
		};
		return dto;
}



function goSuccess(data) {
	if(data.operaterId == 'flow16' && data.assignUserName != null){
		//起草页面,如果当前操作为提交文书科,且文书科角色下只对应一个用户
		addTransactor(data);
	}
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001";
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
               
	}/*
        if(validLength($("#receiveTitle").val())>40){
		$.alert("文件标题应少-于40字");
		return false;
                
	}*/
        if(!$("#docCameOrgan").val().length){
		$.alert("来文单位不能为空");
		return false;
               
	}
    /*
         if(validLength($("#docCameOrgan").val())>25){
		$.alert("来文单位应少-于25字");
		return false;
                
	}*/
        if(!$("#receiveCode").val().length){
		$.alert("文号不能为空");
		return false;
               
	}
    /*
        if(validLength($("#receiveCode").val())>25){
		$.alert("文号应少-于25字");
		return false;
                
	}
         if(validLength($("#lwCode").val())>15){
		$.alert("编号应少-于15字");
		return false;
                
	}
         if(validLength($("#remark").val())>50){
		$.alert("备注应少-于50字");
		return false;
                
	}*/
        
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
	  window.scrollTo(0,0);
	return true;
}



//保存到草稿箱
$("#tempSubmitBtn").on("click", function() {
    var dto = getAddData();

    dto.status = "0001";
    doJsonRequest("/receivedoc/addReceiveDoc", dto, function(data) {
        if (data.result) {
            var data = data.data;
            $("#id").val(data.id);
            $.alert("暂存成功。");
        } else {
            alert("添加报告出错。");
        }
    }, {showWaiting: true});
});