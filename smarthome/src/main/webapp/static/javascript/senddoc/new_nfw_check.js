/**
	0009-拟政府发文 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var showCard = getURLParam("showCard");//是否显示请示卡页： 1：是，0：否
var allBtn = "revision_final";//weboffice按钮控制
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
var printBtnFlag;
/**
 * 当前为拟发电报时，页面元素调整，去掉属性，主送抄送报送及法制办审核版块。
 */
function nfdbChange(){
	$("tr[name='nfdb']").hide();
}

/*//正文加载webOffice控件
var flag = false;
$("#textPage").on('click',function(){
	if(!flag){
		
		flag = true;
	}
});*/

if(showCard == '0'){
	$("#qsy").hide();
	printBtnFlag = 1;
	$("#printBtn").hide();
	$("#tab1").hide();
	//打开时默认显示请示页:
	$("#textPage a").tab("show");
	$("#textPage").trigger("click");
}else{
	//打开时默认显示请示页:
	$(".nav-tabs a:first").tab("show");
}
$(function(){
	doQueryWF("reportInfo","approvalDiv");
	//根据发文类型动态生成title
	var title = "";
	switch (type) {
	case '0009':
		title = "拟以市政府及其办公厅行文申报表";
		break;
	}
	$("#title").text(title);
	//获取当前起草人信息
	getCurrenUserInfo(function(data){
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#signature").val(data.signature);
		$("#keyPosition").val(data.keyPosition);
		$("#roleCodes").val(data.roleCodes);
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
				//拟发文标题
				$("#nfwTitle").val(data.nfwTitle);
				if(data.nfwTitle == '0003'){
					nfdbChange();
				}
				$("#spanNfwTitle").html($("#nfwTitle").find("option:selected").text());
				//拟发文日期
				$("#reportDate").html(new Date(data.reportDate).format('yyyy年M月d日'));
				//公开审核页日期
				$("#auditTime").html(new Date(data.reportDate).format('yyyy年M月d日'));
				//拟办单位
				if(data.fromOrgName!=null && data.fromOrgName!=''){
					$("#createUserOrgName").html(data.fromOrgName);
					$("#fromOrgName").html(data.fromOrgName);
					$("#bmks").html(data.fromOrgName);
				}
				//拟文人
				if(data.attnPerson!=null && data.attnPerson!=''){
					$("#createUserName").html(data.attnPerson);
					$("#jbr").html(data.attnPerson);
				}
				//联系电话
				if(data.fromNumber!=null && data.fromNumber!=''){
					$("#phoneNumber").html(data.fromNumber);
					$("#lxdh").html(data.fromNumber);
				}
				//属性
				$("#openType").val(data.openType);
				$("#spanOpenType").html($("#openType").find("option:selected").text());
				$("#fileAttr").html($("#openType").find("option:selected").text());//属性与是否公开的选项一致
				//密级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//文件标题
				$("#reportTitle").html(data.reportTitle);
				$("#wjbt").html(data.reportTitle);
				//领导审核
				$("#fmszOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var leadingAudit = "";
				var mszAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					leadingAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					leadingAudit += data.fszOpinion + '<br>';				
				}
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					mszAudit += data.fmszOpinion;
				}
				$("#leadingAudit").html(leadingAudit);
				$("#mszAudit").html(mszAudit);
				//拟办意见
				//$("#hgOpinion").val(data.hgOpinion);
				//主办单位审核
                                $("#hgOpinionSPAN").html(data.hgOpinion);
			/*	$("#orgAudOpinion").val(data.orgAudOpinion);*/
				//备注
				$("#nfwRemarks").val(data.nfwRemarks);
				//主送抄送
				$("#sendUnit").html(data.sendUnit);
				$("#ccUnit").html(data.ccUnit);
				//附件--暂时没做
				
				//编号
				$("#nfwCode").html(data.nfwCode);
				/*
				 * 信息公开审核页
				 */
				//公开与否说明
				$("#openTypeReason").html(data.openTypeReason),
				//文件编号
				$("#reportCode").html(data.reportCode);
				//备注
				$("#remarks").val(data.remarks);
				//审批单位意见
				$("#proposedAdvice").html(data.proposedAdvice),
				//审批单位意见日期:
				/*$("#proposedDate").html(data.proposedDate);*/
				
				//发文依据
				$("#docBasis").val(data.docBasis);
				$("#spanDocBasis").html($("#docBasis").find("option:selected").text());
				
				//采纳意见
				var accept = data.accept;
				if(!isNaN(accept) && accept != null){
					$("#accept").html(accept+"条");
				}
				//未采纳意见
				var notAccept = data.notAccept;
				if(!isNaN(notAccept) && notAccept != null){
					$("#notAccept").html(notAccept+"条");
				}
				
				//主要分歧
				$("#mainDiffer").html(data.mainDiffer);
				
				$("#text").val(data.text);//正文,暂时的
				//附件
				$("#attachments").val(data.attachments);
			} else {
				$.alert("获取信息失败。");
			}
			$("#moban_title").html($("#nfwTitle").children('option:selected').text());//标题同步
			$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl='+$("#text").val()+'&taskid='+id+'&userName='+$("#userName").val()+'&allBtn='+allBtn+'"style="width:100%;height:800px;"></iframe>');
		});
		
		//若当前为秘书室或者副市长查看，则显示秘书室意见卡
		/*doJsonRequest("/senddoc/getCheckSecOpinCard",{sendDocId:id,fszRoleCode:$("#keyPosition").val(),mssRoleCode:$("#roleCodes").val()},function(data){
			if(data.result) {
				var data = data.data;
				if(data.id!=null){
					$("#opRepCardId").attr('disabled',true).val(data.id);
					$("#secOpDate").html(new Date(data.secOpDate).format('yyyy年M月d日'));
					$("#mainContent").attr('disabled',true).val(data.mainContent);
					$("#context").attr('disabled',true).val(data.context);
					$("#discuss").attr('disabled',true).val(data.discuss);
					$("#impExplain").attr('disabled',true).val(data.impExplain);
					$("#toDoOpinion").attr('disabled',true).val(data.toDoOpinion);
					$("#remark").attr('disabled',true).val(data.remark);
					
					$("#opRepCard").show();
				}
			}
		});*/
		}
	});
	
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