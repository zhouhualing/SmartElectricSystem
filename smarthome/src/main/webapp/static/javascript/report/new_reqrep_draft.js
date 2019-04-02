//打开时默认显示正文页:

var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
$("#reqRepType").val(type);
var date = new Date();

$(function(){
	//获取当前登陆人信息
	var reqRepDate = date.format('yyyy年MM月dd日');
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		if(id == null) {
			$("#docCameOrgan").val(data.orgName);
			$("#drafterPhone").val(data.phoneNumber);
			$("#drafterName").val(data.userName);
		}
		$("#userName").val(data.userName);
		$("#reqRepDate").html(reqRepDate);
	});
	$("#qsy").children("a").trigger("click");
	$("#textIframe").append('<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid3='+id+'" style="z-index:9999;width:100%;height:800px;"></iframe>');
	//初始化流程按钮
	wf_getOperator("internalCheckReport",function(data){
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
			id:id
		};
		doJsonRequest("/reqrep/getReqRep",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#reqRepType").val(data.reqRepType);
				$("#reqRepCode").val(data.reqRepCode);
				$("#reqRepTitle").val(data.reqRepTitle);
				$("#reqRepText").val(data.reqRepText);
				/*if(data.reqRepDate!=''&&data.reqRepDate!=null){
					$("#reqRepDate").html(new Date(data.reqRepDate).format('yyyy年MM月dd日'));
				}*/
				$("#docCameOrgan").val(data.docCameOrgan);
				$("#bmldps").val(data.fgldOpinion);
				$("#drafterPhone").val(data.drafterPhone);
				$("#drafterName").val(data.drafterName);
				$("#remark").val(data.remark);
				$("#drafterPhone").val(data.drafterPhone);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else{
		doJsonRequest("/code/getCode","5003",function(data){
			if(data.result) {
				$("#reqRepCode").val(data.data);
			} else {
				$.alert("获取编码出错。");
			}
		});
	}
});

function getAddData(){
	var datas = $("#webOfficeIframe")[0].contentWindow.getInto();
	var bmldps = $("#bmldps").val();
	var fgldOpinion = $("#fgldOpinion").val();
	//拟办意见
	if(bmldps!=''){
		bmldps = bmldps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]";
	}
	if(fgldOpinion!=''){
		fgldOpinion = bmldps + '<br>' + fgldOpinion;
	}else{
		fgldOpinion = bmldps;
	}
	var dto = {
		id:$("#id").val(),
		drafterName:$("#drafterName").val(),
		drafterPhone:$("#drafterPhone").val(),
		docCameOrgan:$("#docCameOrgan").val(),
		reqRepDate:new Date(),
		reqRepType:$("#reqRepType").val(),
		//文号
		reqRepCode:$("#reqRepCode").val(),
		//来文标题
		reqRepTitle:$("#reqRepTitle").val(),
		reqRepText:$("#reqRepText").val(),
		remark:$("#remark").val(),
		fgldOpinion:fgldOpinion,
		status:'0002',
		flag:0,//当前为新建流程.
		attachmentDTOs:datas.mailAttachmentDTOs
	};
	return dto;
}

//保存到草稿箱
$("#tempSubmitBtn").on("click",function(){
	var dto = getAddData();
	dto.status = "0001";
	doJsonRequest("/reqrep/addReqRep",dto,function(data){
		if(data.result) {
			var data = data.data;
			$("#id").val(data.id);
			$.alert("暂存成功。");
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
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001";
}

function wf_getDate(data) {
	return getTheDate(172800);
}



//校验
function wf_beforeValid(){
	if($("#reqRepTitle").val() == ''){
		$.alert("请求标题不能为空");
		return false;
	}
	if($('#docCameOrgan').val() == ''){
		$.alert("承办部门不能为空");
		return false;
	}
    if($('#drafterPhone').val() == ''){
        $.alert("承办人电话不能为空");
        return false;
    }
    if($('#drafterName').val() == ''){
        $.alert("承办人不能为空");
        return false;
    }
        window.scrollTo(0,0);
	return true;
}