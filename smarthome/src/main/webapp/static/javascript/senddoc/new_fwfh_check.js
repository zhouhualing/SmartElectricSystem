/**
	0001-市政府发文 
	0002-市政府发函 
	0005-办公厅发文 
	0006-办公厅发函 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var fullYear = new Date().getFullYear();
var allBtn = "revision_final";//weboffice按钮控制
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
var printBtnFlag;
if(type =="0001"||type =="0002"){
    $("#rmzfbgt").html("大同市人民政府");
    $("#qfps").html("市长审签：");
    $("#fgfszsq").html("分管副市长审核：");
    $("#mszsq").html("秘书长审核：");
}
//正文加载webOffice控件
var flag = false;
var roleCode = "";
$(function(){
	doQueryWF("reportInfo","approvalDiv");
	//根据发文类型动态生成title
	var title = "";
	var repCode = "";
	
	switch (type) {
	case '0001':
		title = "大同市人民政府发文卡片";
		repCode = "同政发";
		break;
	case '0002':
		title = "大同市人民政府发函卡片";	
		repCode = "同政函";
		break;
	case '0005':
		title = "大同市人民政府办公厅发文卡片";
		repCode = "同政办发";
		break;
	case '0006':
		title = "大同市人民政府办公厅发函卡片";
		repCode = "同政办函";
		break;
	}
	$("#title").text(title);
	$("#repCode").text(repCode);
	//获取当前起草人信息
	getCurrenUserInfo(function(data){
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#signature").val(data.signature);
		roleCode = data.roleCodes;
		if(id != null) {
			$("#id").val(id);
			$("#businessId").val(id);
			$("#backBtn").removeClass("hidden");
			var dto = {
					id:id
			};
			$('#dataInputForm').addClass('fileupload-processing');
			doJsonRequest("/senddoc/getReport",dto,function(data){
				if(data.result) {
					var data = data.data;
					if(data.nfwId != null) {
						$("#businessKey1").val(data.nfwId);
						initNfwInfoA(data.nfwId);
						doQueryWF("reportInfo1","approvalDiv1");
					} else {
						$("#hiddenDiv1").hide();
					}
					$("#reportTitle").html(data.reportTitle);
					$("#wjbt").html(data.reportTitle);
					$("#sendUnit").html(data.sendUnit);
					$("#ccUnit").html(data.ccUnit);
					$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
					$("#urgency").val(data.urgency);
					$("#urgencyspan").html($("#urgency").find("option:selected").text());
					$("#putOnRecords").html(data.putOnRecords);
					$("#openType").val(data.openType);
					$("#fileAttr").html($("#"+data.openType).html());//属性与是否公开的选项一致
					$("input[name='ot'][value='"+data.openType+"']").attr("checked",'checked');
					if(data.reportCode!=null&&data.reportCode!=''){
						var strs= new Array(); //定义一数组 
						strs=data.reportCode.split("_"); //字符分割 
						fullYear = strs[0];
						$("#fullYear").val(strs[0]);
						$("#reportCode").val(strs[1]);
						$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
					}
					$("#theIssuer").html(data.theIssuer);
					$("#openTypeReason").html(data.openTypeReason),
					/*$("#draftUnitOpinion").html(data.draftUnitOpinion);
					$("#draftUnitDate").html(data.draftUnitDate);*/
					$("#proposedAdvice").html(data.proposedAdvice),
					//$("#proposedDate").html(new Date(data.proposedDate).format('yyyy-MM-dd hh:mm'));
					/*$("#proposedDate").html(data.proposedDate);*/
					$("#remarks").val(data.remarks);
					$("#text").val(data.text);//正文,暂时的
					$("#reportDate").html(new Date(data.reportDate).format('yyyy年M月dd日'));
					$("#auditTime").html(new Date(data.auditTime).format('yyyy年M月dd日'));
					//拟办单位
					if(data.fromOrgName!=null && data.fromOrgName!=''){
						$("#createUserOrgName").val(data.fromOrgName);
						$("#fromOrgName").html(data.fromOrgName);
						$("#bmks").html(data.fromOrgName);
					}
					//拟文人
					if(data.attnPerson!=null && data.attnPerson!=''){
						$("#createUserName").val(data.attnPerson);
						$("#jbr").html(data.attnPerson);
					}
					//联系电话
					if(data.fromNumber!=null && data.fromNumber!=''){
						$("#phoneNumber").val(data.fromNumber);
						$("#lxdh").html(data.fromNumber);
					}
					//小纸条
					$("#lPageSpan").html(data.littlePage);
					/*$("#orgAudOpinion").val(data.orgAudOpinion);
					$("#fzr").append(data.orgAuditing);
					$("#orgAudDate").html(data.orgAudDate);*/
					$("#fzbOpinionSPAN").html(data.fzbOpinion);
					$("#hgOpinionSPAN").html(data.hgOpinion);
					$("#fmszOpinionSPAN").html(data.fmszOpinion);
					$("#mszOpinionSPAN").html(data.mszOpinion);
					$("#fszOpinionSPAN").html(data.fszOpinion);
					$("#szOpinionSPAN").html(data.szOpinion);
					$("#attnCode").val(data.attnCode);//办理人code
					$("#attnName").val(data.attnName);//办理人name
					//附件
					$("#attachments").val(data.attachments);
					$("#status").val(data.status);
					//份数
					if(data.issued!=''){
						$("#issuedCount").attr("display","block");
						$("#issued").val(data.issued);
					}
				} else {
					$.alert("获取信息失败。");
				}
				
				doGetAttendInfo("sendDocApprovalV1","usertask16",id,function(data){
					for(var i=0; i<data.length; i++) {
						var arr = data[i].userCode;//当前环节有谁参与
						var arrO = data[i].otherUserCode;//除此环节都有谁参与
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
				
				//$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl='+$("#text").val()+'&taskid='+id+'&shAttBtn=1&showPrintBtn='+$("#status").val()+'&userName='+$("#userName").val()+'" style="width:100%;height:800px;"></iframe>');
			});
		}
	});
});

$("#textPage").click(function(){
	if(!flag){
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?checkInfo=1&officeUrl='+$("#text").val()+'&taskid='+id+'&shAttBtn=1&showPrintBtn='+$("#status").val()+'&userName='+$("#userName").val()+'&allBtn='+allBtn+'" style="width:100%;height:800px;"></iframe>');
		flag = true;
	}
});

var Tab1 =function(){
    $("#tab1,#tab2,#tab3,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab1").css({
        "display":"block",
        "height":"auto"
    })
};
var Tab2 =function(){
    $("#tab1,#tab2,#tab3,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab2").css({
        "display":"block",
        "height":"auto"
    })
};
var Tab3 =function(){
    $("#tab1,#tab2,#tab3,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab3").css({
        "display":"block",
        "height":"auto"
    })
};
var Tab4 =function(){
    $("#tab1,#tab2,#tab3,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab4").css({
        "display":"block",
        "height":"auto"
    })
};

var nfwIdd = null;
function initNfwInfoA(NfwId) {
	nfwIdd = NfwId;
}

function showNfwInfo() {
	window.open("../senddoc/new_nfw_check.html?id="+nfwIdd+"&type=0009","_target")
}

function beforePrint() {
	if($("#reportCode").val() == null || ($("#reportCode").val().length ==0)) {
		
		$("#secCode").addClass("noPrint");
	}
}

function afterPrint() {
	if($("#reportCode").val() == null || ($("#reportCode").val().length ==0)) {
		$("#secCode").removeClass("noPrint");
	}
}