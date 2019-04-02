/**
	0001-市政府发文 
	0002-市政府发函 
	0005-办公厅发文 
	0006-办公厅发函 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var taskId = getURLParam("taskId");  
var fromPage = getURLParam("fromPage"); 
var userTask = "";//当前流程
var ltPage = getURLParam("ltPage"); //小纸条是否可编辑
var lcbz = "";//流程备注信息
var allBtn = "revision_final";//weboffice按钮控制
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);

var userName = "";
var roleCode = "";
//正文加载webOffice控件
var flag = false;

$(function(){
	getCurrenUserInfo(function(data){
		userName = data.userName;
		roleCode = data.roleCodes;
		$("#userName").val(data.userName);
		if(id != null) {
			$("#id").val(id);
			$("#businessId").val(id);
			$("#backBtn").removeClass("hidden");
			var dto = {
					id:id
			};
			doJsonRequest("/senddoc/getReport",dto,function(data){
				if(data.result) {
					var data = data.data;
					if(data.nfwId != null) {
						$("#businessKey1").val(data.nfwId);
						initNfwInfoA(data.nfwId);
					} else {
						$("#hiddenDiv1").hide();
					}
					//属性
					$("#openType").val(data.openType);
					$("#spanOpenType").html($("#openType").find("option:selected").text());
					$("#fileAttr").html($("#openType").find("option:selected").text());//属性与是否公开的选项一致
					
					//标题
					$("#reportTitle").val(data.reportTitle);
					$("#wjbt").html(data.reportTitle);
					
					//发送单位
					$("#sendUnit").val(data.sendUnit);
					//密级
					$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
					//紧急程度
					$("#urgency").val(data.urgency);
					//编号
					$("#reportCode").val(data.reportCode);
					$("[name='secCode']").html(data.reportCode);
					//日期
					if(data.reportDate!=null){
						$("#reportDate").html(new Date(data.reportDate).format('yyyy年MM月dd日'));
					}
					if(data.auditTime!=null){
						$("#auditTime").html(new Date(data.auditTime).format('yyyy年MM月dd日'));
					}
					//经办人
					$("#attnPerson").html(data.attnPerson+":"+data.fromNumber);
					$("#jbr").html(data.attnPerson);
					$("#lxdh").html(data.fromNumber);
					
					//经办部门
					$("#fromOrgName").html(data.fromOrgName);
					$("#orgName").html(data.fromOrgName);
					$("#bmks").html(data.fromOrgName);
					//公开与否说明
					var openTypeReason = data.openTypeReason;
					if(openTypeReason!=''){
						openTypeReason = openTypeReason.replace("<br>","");
					}
					$("#openTypeReason").html(openTypeReason),
					$("#proposedAdviceSPAN").html(data.proposedAdvice),
					//审批单位意见日期:
					/*$("#proposedDate").html(data.proposedDate);*/
					//备注
					$("#remarks").val(data.remarks);
					$("#text").val(data.text);
					//小纸条
					$("#liPage").val(data.littlePage);
					
					$("#hgOpinionSPAN").html(data.hgOpinion);
					
					$("#attnCode").val(data.attnCode);//办理人code
					$("#attnName").val(data.attnName);//办理人name
					//附件
					$("#attachments").val(data.attachments);
					$("#status").val(data.status);
					
					var leadingAudit = "";
					if(data.szOpinion!='' && data.szOpinion!=null){
						leadingAudit += data.szOpinion + '<br>';
					}
					if(data.fszOpinion!='' && data.fszOpinion!=null){
						leadingAudit += data.fszOpinion + '<br>';				
					}
					if(data.mszOpinion!='' && data.mszOpinion!=null){
						leadingAudit += data.mszOpinion + '<br>';
					}
					if(data.fmszOpinion!='' && data.fmszOpinion!=null){
						leadingAudit += data.fmszOpinion;
					}
					$("#leadingAudit").html(leadingAudit);
					$("#pageSize").val(data.pageSize);
					//初始化流程跟踪页
					doQueryWF("reportInfo","approvalDiv");
				} else {
					$.alert("获取信息失败。");
				}
				
				doGetAttendInfo("sendTelegramApprovalV1","usertask16",id,function(data){
					for(var i=0; i<data.length; i++) {
						var arr = data[i].userCode;
						var arrO = data[i].otherUserCode;
						var currentUserCode = data[i].currentUserCode;
						if(($.inArray(currentUserCode,arr) != -1)&&($.inArray(currentUserCode,arrO)==-1)) {
							//隐藏请示页,保密审核页,流程跟踪页,并且默认添加流程备注信息:"收到"
							$("#textPage a").tab("show");
							$("#textPage").trigger("click");
						}else{
							$("#qsy").show();
							$("#xxgksh").show();
							$("#lcgz").show();
							$(".nav-tabs a:first").tab("show");
						}
					}
				})
			});
			
		}
	});
});

$("#textPage").click(function(){
	if(!flag){
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?showBtn=12345&checkInfo=1&officeUrl='+$("#text").val()+'&allBtn='+allBtn+'&userName='+$("#userName").val()+'&showPrintBtn='+$("#status").val()+'&taskid='+id+'&shAttBtn=1" style="width:100%;height:800px;"></iframe>');
		flag = true;
	}
});

//----------与信息审核页的录入信息同步(结束)------------

function initNfwInfoA(NfwId) {
	$("#showNfwInfoA").attr("href","../senddoc/new_nfw_check.html?id="+NfwId+"&type=0009").attr("target","_blank");
}
