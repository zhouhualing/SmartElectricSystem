//打开时默认显示正文页:

var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
$("#reportType").val(type);
var date = new Date();
var receiveDate1 =date.format('yyyy年M月d日');
$(function(){
	$("#qsy").children("a").eq(0).trigger("click");
	//初始化流程按钮
	wf_getOperator("getTelegramV1",function(data){
	});
	$("#receiveDate").val(receiveDate1);
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
//		$("#drafterName").html(data.userName);
//		$("#drafterPhoneNum").html(data.phoneNumber);
//		$("#receiveDate").html(receiveDate1);
	});
	doJsonRequest("/code/getCode","6001",function(data){
		if(data.result) {
			$("#lwCode").val(date.getFullYear()+data.data);
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
				//编号
				$("#text").val(data.text);
				if(data.receiveDate!='' && data.receiveDate!=null){
					$("#receiveDate").val(new Date(data.receiveDate).format('yyyy年M月d日'));
				}
				$("#reportType").val(data.reportType);
				$("#receiveTitle").val(data.receiveTitle);
				//来文单位
				$("#docCameOrgan").val(data.docCameOrgan);
				//文号
				$("#receiveCode").val(data.receiveCode);
				//紧急程度
				$("#urgency").val(data.urgency);
				//来文编号
				$("#lwCode").val(data.lwCode);
				$("#nbyj").val(data.hgOpinion);
				$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid2='+id+'" style="z-index:9999;width:100%;height:900px;"></iframe>');
				/*var wskOpinion = data.wskOpinion;
				if(wskOpinion!=null && wskOpinion!=''){
					var ipos2 = wskOpinion.indexOf("  [");
					$("#nbyj").val(wskOpinion.substring(0,ipos2));
				}*/
			} else {
				$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid2=" style="z-index:9999;width:100%;height:900px;"></iframe>');
				$.alert("获取信息失败。");
			}
		});
	}else{
		$("#urgency").val('0004');
		$("#textIframe").append('<iframe  id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice4.html?officeUrl=&taskid2=" style="z-index:9999;width:100%;height:900px;"></iframe>');
	}
});

function getAddData(){
	//文书科意见
	/*var wskOpinion = $("#nbyj").val();
	if(wskOpinion!='' && wskOpinion.indexOf("  [")<=0){
		wskOpinion = wskOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	}*/
	var datas = $("#webOfficeIframe")[0].contentWindow.getInto();
	var dto = {
		receiveDate:$("#receiveDate").val().split('日')[0].replace(/[^\d]/g,'-'),
		id:$("#id").val(),
		reportType:"6001",
		//正文
		receiveTitle:$("#receiveTitle").val(),
		//来文单位
		docCameOrgan:$("#docCameOrgan").val(),
		//文号
		receiveCode:$("#receiveCode").val(),
		//紧急程度
		urgency:$("#urgency").val(),
		//来文编号
		lwCode:$("#lwCode").val(),
		hgOpinion:$("#nbyj").val(),
		/*wskOpinion:wskOpinion,*/
		upCardFlag:'1',
		status:'0002',
		flag:0,//当前为新建流程.
		attachmentDTOs:datas.mailAttachmentDTOs
	};
	return dto;
}

//保存到草稿箱
$("#tempSubmitBtn").on("click",function(){
	var dtoo = getAddData();
     
	dtoo.status = "0001";
	doJsonRequest("/receivedoc/addReceiveDoc",dtoo,function(data){
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
//	//文件名称不能为空
if($("#docCameOrgan").val().length==0 ){
		$.alert("来电单位不能为空");
		return false;
	}
    /*
	if($("#docCameOrgan").val().length>25 ){
		$.alert("来电单位应少-于25字");
		return false;
	}*/
         if($("#receiveCode").val().length==0 ){
		$.alert("文号不能为空");
		return false;
	}/*
        if($("#receiveCode").val().length>25 ){
		$.alert("文号应少-于25字");
		return false;
	}
         if($("#receiveTitle").val().length>70 ){
		$.alert("内容摘要应少-于70字");
		return false;
	}*/
         if(!$("#receiveTitle").val().length ){
		$.alert("请填写内容摘要");
		return false;
	}/*
         if(validLength($("#nbyj").val())>30 ){
		$.alert("拟办意见应少-于30字");
		return false;
	}
        if($("#lwCode").val().length>15){
            $.alert("编号应小-于15字");
		return false;
        }
    */
           var re = /^[0-9]+.?[0-9]*$/;
            if(!re.test($("#lwCode").val())){
                $.alert('编号不合理'); 
                return false;
           }
//	//秘密等级
//	if($('input:checkbox[name="secretLevel"]:checked').val()==null){
//		$.alert("请确认此文件为非涉密件");
//		return false;
//	}
	//紧急程度
	if($("#urgency").val() == ''){
		$.alert("请选择紧急程度");
		return false;
	}
	
  window.scrollTo(0,0);
	return true;
}
