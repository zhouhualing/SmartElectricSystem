var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");  
var taskIds =getURLParam("taskIds"); //当前关联件需要截取的taskId区间
var relationId = getURLParam("relationId");  //如果当前是关联件，此ID为主线receiveId.
var textIframe = "";//正文
var checkFlag = getURLParam("checkFlag");  //1:当前页面为查询页面，查询页需展示所有流程跟踪
$("#id").val(id);
if(relationId!=null && relationId!=''){
	$("#businessKey").val(relationId);
}else{
	$("#businessKey").val(id);
}
$("#reportType").val(type);
$("#relationId").val(relationId);
$("#qsy a").tab("show");

$(function(){
	//taskIds是上级页面点击进来的参数，表名当前页面为关联件页面
	if(taskIds!=null && taskIds!='null' && taskIds!=''){
		$("#approvalDiv").append('<input type="hidden" id="orderId" name="orderId" value="'+taskIds+'">');
	}
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
				
				//呈批意见
				//$("#hgOpinion").val(data.hgOpinion);
                $("#hgOpinionSPAN").html(data.hgOpinion);
				//备注
				$("#remark").val(data.remark);
				//正文
				$("#text").val(data.text);
				$("#drafterName").html(data.drafterName);
				$("#drafterPhoneNum").html(data.drafterPhoneNum);
				if(data.receiveDate!=''&&data.receiveDate!=null){
					$("#receiveDate").html(new Date(data.receiveDate).format('yyyy年M月d日'));
				}
				if(data.undertakeDate!=''&&data.undertakeDate!=null){
					$("#undertakeDate").html(new Date(data.undertakeDate).format('yyyy年M月d日'));
				}
				$("#lwCode").val(data.lwCode);
				$("#upCardFlag").val(data.upCardFlag);
				$("#createUserName").val(data.createUserName);
				if($("#text").val().indexOf("~!~!")>0){
					//当前为征求过委局意见的正文
					textIframe = '<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl='+$("#text").val()+'&showPrintBtn='+data.status+'&reportType=5002&isStart=0&taskid='+id+'&isAdviceDraft=2" style="z-index:9999;width:100%;height:900px;"></iframe>';
				}else{
					textIframe = '<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl='+$("#text").val()+'&showPrintBtn='+data.status+'&reportType=5002&isStart=0&taskid='+id+'" style="z-index:9999;width:100%;height:900px;"></iframe>';
				}
				//表名当前为普通查看页面
				if((taskIds == null || taskIds == 'null' || taskIds == '') && data.taskIds!=null && data.taskIds!=''){
					if(data.taskIds.indexOf(',')>0){
						$("#approvalDiv").append('<input type="hidden" id="orderId" name="orderId" value="'+data.taskIds+'">');
						$("#businessKey").val(data.relationId);
					}else{
						//var taskIds = parseInt(data.taskIds)+1;
						var taskIds = data.taskIds;
						$("#approvalDiv").append('<input type="hidden" id="orderId" name="orderId" value="'+taskIds+',9999999999999">');
					}
					
				}
			} else {
				$.alert("获取信息失败。");
			}
			doQueryWF("reportInfo","approvalDiv");
		});
	}
});


//正文加载webOffice控件
var flag = false;
$("#textPage").click(function(){
	if(!flag){
		$("#textIframe").append(textIframe);
		flag = true;
	}
});