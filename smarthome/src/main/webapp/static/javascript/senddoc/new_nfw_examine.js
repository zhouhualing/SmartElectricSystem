var userTask = "";//当前流程
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
var realRoleCode = null;
var businessType = getURLParam("businessType");
//正文加载webOffice控件
var flag = false;
var allBtn = "revision_final_saveFile";//weboffice按钮控制
/**
 * 当前为拟发电报时，页面元素调整，去掉属性，主送抄送报送及法制办审核版块。
 */
function nfdbHide(){
	$("tr[name='nfdb']").hide();
}
function nfdbShow(){
	$("tr[name='nfdb']").show();
}

$(function(){
	//获取当前起草人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#roleCodes").val(data.roleCodes);
		$("#signature").val(data.signature);
		$("#keyPosition").val(data.keyPosition);
		realRoleCode = data.realRoleCode;
		$("#textPage").click(function(){
			if(!flag){
				$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl='+$("#text").val()+'&allBtn='+allBtn+'&taskid='+id+'&userName='+$("#userName").val()+'&shAttBtn=1"style="width:100%;height:800px;"></iframe>');
				flag = true;
			}
		});
	});
	
	//根据发文类型动态生成title
	var title = "";
	switch (type) {
	case '0009':
		title = "拟以市政府及其办公厅行文申报表";
		break;
	}
	$("#title").text(title);
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
					nfdbHide();
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
				$("#reportTitle").val(data.reportTitle);
	            var titleString ="";
	            for(var i=0;i<data.reportTitle.length;i+=42){
	                titleString+=data.reportTitle.substr(i,42)+"</br>"
	            }
				$("#wjbt").html(data.reportTitle).css({
                    "word-break":" break-all"
                });
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
				if(data.hgOpinion == '' || data.hgOpinion == null){
					$("#next8").hide();
				}
                $("#hgOpinionSPAN").html(data.hgOpinion);
				//主办单位审核
				/*$("#orgAudOpinion").val(data.orgAudOpinion);*/
				//备注
				$("#nfwRemarks").val(data.nfwRemarks);
				//主送抄送
				$("#sendUnit").val(data.sendUnit);
				$("#ccUnit").val(data.ccUnit);
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
				//签发人(发文最终领导点击完成后添加该领导名字)
				/*$("#theIssuer").val(data.theIssuer);*/
				//备注
				$("#remarks").val(data.remarks);
				//审批单位意见
                $("#proposedAdviceSPAN").html(data.proposedAdvice)
				//审批单位意见日期:
				/*$("#proposedDate").html(data.proposedDate);*/
				
				$("#text").val(data.text);//正文,暂时的
				
				//小纸条
				/*$("#liPage").val(data.littlePage);*/
				$("#lPageSpan").html(data.littlePage);
				$("#attnCode").val(data.attnCode);//办理人code
				$("#attnName").val(data.attnName);//办理人name
				//附件
				$("#attachments").val(data.attachments);
				
				$("#isReturn").val(data.isReturn);
				
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
			} else {
				$.alert("获取信息失败。");
			}
			$("#moban_title").html($("#nfwTitle").children('option:selected').text());//标题同步
			doQueryWF("reportInfo","approvalDiv");
			var dto = {taskId:taskId}
			//初始化流程按钮
			wf_getOperator(dto,function(data){
				userTask = data.userTask;//获取当前流程
				if(userTask!='usertask7' && userTask!='usertask8'){
					$("#buttonList").show();
				}
				//根据当前步骤控制页面可输入项:
				//委办局起草(退回起草人)
				if(userTask == 'usertask1'){
					if($("#isReturn").val()==1){
						$("#buttonList span").hide();
						$("#toVoid").show();
					}else{
						//属性
						$("#spanOpenType").hide();
						$("#openType").show();
						//文件标题
						$("#reportTitle").removeAttr("disabled");
						//备注
						$("#nfwRemarks").removeAttr("disabled");
						//主送
						$("#sendUnit").removeAttr("disabled");
						//抄送
						$("#ccUnit").removeAttr("disabled");
						//附件
						$("#attachments").removeAttr("disabled");
						//属性公开原由
						$("#lcOTReason").show();
						//备注
						$("#remarks").removeAttr("disabled");
						//主办单位审核
						/*$("#orgAudOpinion").removeAttr("disabled");*/
					}
					disOpinion();
				}
				//文书科办理
				if(userTask == 'usertask3'){
					disOpinion();
				}
				//文书核稿
				if(userTask == 'usertask4'){
					$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					$("#ldsh").hide();
					$("#mszsh").hide();
                    if($("#proposedAdviceSPAN").html()){
                        $("#proposedAdvice").attr("placeholder","");
                    }
                    if($("#hgOpinionSPAN").html()){
                        $("#hgOpinion").attr("placeholder","");
                    }
					//属性
					$("#spanOpenType").hide();
					$("#openType").show();
					//文件标题
					$("#reportTitle").removeAttr("disabled");
					//备注
					$("#nfwRemarks").removeAttr("disabled");
					//主送
					$("#sendUnit").removeAttr("disabled");
					//抄送
					$("#ccUnit").removeAttr("disabled");
					//附件
					$("#attachments").removeAttr("disabled");
					//属性公开原由
					$("#lcOTReason").show();
					//备注
					$("#remarks").removeAttr("disabled");
				}
				//秘书科室
			/*	if(userTask == 'usertask9'){
					disOpinion();
					$("#secOpDate").html(new Date().format('yyyy年M月d日'));
					loadSecOpinCard('mss');
				}*/
				//分管副秘书长审批
				if(userTask == 'usertask5'){
					$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					$("#hgOpinion").hide();
					$("#proposedAdvice").hide();
					$("#ldsh").hide();
				}
				//秘书长审批
				if(userTask == 'usertask6'){
					$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					$("#hgOpinion").hide();
					$("#proposedAdvice").hide();
					$("#ldsh").hide();
				}
				//副市长审批
				if(userTask == 'usertask7'){
					$("#hgOpinion").hide();
					$("#proposedAdvice").hide();
					$("#mszsh").hide();
					//加载当前副市长下面的秘书科室提出的意见卡
					/*loadSecOpinCard('fsz');*/
					
					if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
						$("#buttonList").show();
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}else{
						$("#ldsh").hide();
						$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
					}
				}
				//市长审批
				if(userTask == 'usertask8'){
					$("#mszsh").hide();
					$("#hgOpinion").hide();
					$("#proposedAdvice").hide();
					//如果当前为市长审批,则显示标签,并判断登录人是市长还是秘书,秘书没有操作权限,只有添加标签信息和保存权限.
//					$("#littlePage").show();
					/*if(mark=='002' && $("#userCode").val()!='lijm'){
//						$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
						$("#ldsh").hide();
						$("#buttonList span").hide();
					}else{
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}*/
//					$("#liPage").removeAttr("disabled");
					
					if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
						$("#buttonList").show();
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}else{
						$("#ldsh").hide();
						$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
					}
				}
			});
		});
	}
});

/**
 * 隐藏意见输入框
 */
function disOpinion(){
	$("#ldsh").hide();
	$("#mszsh").hide();
	$("#hgOpinion").hide();
	$("#proposedAdvice").hide();
}

/**
 * 加载秘书室意见卡
 */
/*function loadSecOpinCard(flag){
	if(flag == 'fsz'){
		doJsonRequest("/senddoc/getSecOpinCard",{sendDocId:$("#id").val(),fszRoleCode:$("#roleCodes").val()},function(data){
			if(data.result) {
				var data = data.data;
				if(data.id!=null){
					$("#opRepCardId").attr('disabled',true).val(data.id);
					if(data.secOpDate!=null && data.secOpDate!=''){
						$("#secOpDate").html(new Date(data.secOpDate).format('yyyy年M月d日'));	
					}
					$("#mainContent").attr('disabled',true).val(data.mainContent);
					$("#context").attr('disabled',true).val(data.context);
					$("#discuss").attr('disabled',true).val(data.discuss);
					$("#impExplain").attr('disabled',true).val(data.impExplain);
					$("#toDoOpinion").attr('disabled',true).val(data.toDoOpinion);
					$("#remark").attr('disabled',true).val(data.remark);
					
					$("#opRepCard").show();
				}
				
			}
		});
	}else if(flag == 'mss'){
		doJsonRequest("/senddoc/getSecOpinCard",{sendDocId:$("#id").val(),mssRoleCode:$("#roleCodes").val()},function(data){
			if(data.result) {
				var data = data.data;
				$("#opRepCardId").val(data.id);
				if(data.secOpDate!=null && data.secOpDate!=''){
					$("#secOpDate").html(new Date(data.secOpDate).format('yyyy年M月d日'));	
				}
				$("#mainContent").val(data.mainContent);
				$("#context").val(data.context);
				$("#discuss").val(data.discuss);
				$("#impExplain").val(data.impExplain);
				$("#toDoOpinion").val(data.toDoOpinion);
				$("#remark").val(data.remark);
				
				$("#opRepCard").show();
			}
		});
	}
	
}*/

/**
	0009-拟政府发文
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var taskId = getURLParam("taskId");  
var fromPage = getURLParam("fromPage"); 
var ltPage = getURLParam("ltPage"); //小纸条是否可编辑
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
//打开时默认显示请示页:
$(".nav-tabs a:first").tab("show");


//----------与信息审核页的录入信息同步(开始)------------
//文件名称
$("#reportTitle").on("blur",function(){
	$("#wjbt").html($("#reportTitle").val());
});
//单位/部门
$("#createUserOrgName").on("blur",function(){
	$("#fromOrgName").html($("#createUserOrgName").val());
	$("#bmks").html($("#createUserOrgName").val());
});
//经办人
$("#createUserName").on("blur",function(){
	$("#jbr").html($("#createUserName").val());
});
//联系电话
$("#phoneNumber").on("blur",function(){
	$("#lxdh").html($("#phoneNumber").val());
});
//属性
$("#openType").change(function(){
	$("#fileAttr").html($(this).children('option:selected').text());
});
//公开信息审核页 审批单位意见动态添加日期:
/*$("#proposedAdvice").on("blur",function(){
	if($("#proposedAdvice").val()!=''){
		$("#proposedDate").html(new Date().format('yyyy-M-d hh:mm'));
	}else{
		$("#proposedDate").html('');
	}
});*/
//----------与信息审核页的录入信息同步(结束)------------

//校验
function wf_beforeValid(){
	//文件名称不能为空

	if($("#reportTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	} /*
     if(validLength($("#lcOTReason").val())>40){
         $.alert("公开属性原因应少-于40字");
         return false;
    }
    if($("#remarks").val().length>30){
         $.alert("审核页备注应少-于30字");
         return false;
    }
    if($("#nfwRemarks").val().length>35){
		$.alert("请示页备注应少-于35字");
		return false;
	}
         if($("#sendUnit").val().length>50){
		$.alert("主送应少-于50字");
		return false;
	}
         if($("#ccUnit").val().length>35){
		$.alert("抄送应少-于35字");
		return false;
	}
         if($("#attachments").val().length>150){
		$.alert("附件字数太多，应少-于150字");
		return false;
	}
     if(validLength($("#proposedAdvice").val())>40){
        $.alert("审批单位意见应少-于40字");
        return false;
    }*/
	//秘密等级
	if($('input:checkbox[name="secretLevel"]:checked').val()==null){
		$.alert("请确认此文件为非涉密件");
		return false;
	}
	//属性
	if( $("#openType").val() == ''){
		$.alert("请选择属性");
		return false;
	}
	//公开属性原由
	if( ($("#openType").val() == '0002' || $("#openType").val() == '0003') && $("#openTypeReason").html() == ''){
		$.alert("请填写"+$("#fileAttr").html()+"原由");
		return false;
	}
	//核稿人
	if(userTask == 'usertask4'){
		if($("#hgOpinion").val()==''&&($("#hgOpinionSPAN").html().length==0)){
			$.alert('请填写审批意见');
			return false;
		}
        /*
        if(validLength($("#hgOpinion").val())>35){
			$.alert('审批意见应少-于35字');
			return false;
		}*/
                
        if($("#proposedAdvice").val()==''&&$("#proposedAdviceSPAN").html().length==0){
			$.alert('请填写办公厅审核意见');
			return false;
		}         
	}
	//领导审批
	if(userTask == 'usertask7' || userTask == 'usertask8'){
		if($("#ldsh").val()==''){
			$.alert('请填写审批意见');
			return false;
		}
        /*
        if(validLength($("#ldsh").val())>40){
			$.alert('审批意见应少-于40字');
			return false;
		}*/
	}
	if(userTask == 'usertask5' || userTask == 'usertask6'){
		if($("#mszsh").val()==''){
			$.alert('请填写审批意见');
			return false;
		}/*
        if(validLength($("#mszsh").val())>40){
			$.alert('审批意见应少 -于40字');
			return false;
		}*/
	}
    // 审批单位意见
    window.scrollTo(0,0);
	return true;
}
//*************************
var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
function goSuccess(data) {
	if((data.operaterId == 'flow3') && data.assignUserName != null){
		//起草页面,如果当前操作为提交文书科,且文书科角色下只对应一个用户,保存办理人(如果对应多个用户,需在用户认领任务页面保存办理人)
		addTransactor(data);
	}
	window.location.href="../tododocs/docs_down.html?roleName="+data.assignRoleName+"&fromPage=0002&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
}
function addTransactor(data){
	var dto = {
			id:data.businessKey,
			attnCode:data.assignUserCode
	};
	doJsonRequest("/senddoc/addReportText",dto,function(data){});
}
//办结
function goSuccessEnd() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=1"+"&btnSource="+btnSource+"&mark="+mark;
}
//作废
function goVoidEnd(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=3"+"&btnSource="+btnSource+"&mark="+mark;
}
function getAddData(){
	var attachmentDTOs = new Array();
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		$("#text").val(officefileUrl);
	}
	
	//主办单位
/*	var orgAudOpinion = $("#orgAudOpinion").val();
	if(userTask == 'usertask1'){
		if(orgAudOpinion!='' && orgAudOpinion.charAt(orgAudOpinion.length - 1) != "]"){
			orgAudOpinion = orgAudOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		}
	}*/
	//核稿
	var hgOpinion = $("#hgOpinion").val();
	if(userTask == 'usertask4'){
		//&& hgOpinion.indexOf("  [")<=0
		if(hgOpinion!='' && hgOpinion.charAt(hgOpinion.length - 1) != "]"){
			hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
                        hgOpinion +="</br>"+$("#hgOpinionSPAN").html();
		}
	}
	//领导审批
	var fmszOpinion = $("#fmszOpinion").val();
	var mszOpinion = $("#mszOpinion").val();
	var fszOpinion = $("#fszOpinion").val();
	var szOpinion = $("#szOpinion").val();
	var ldsh = $("#ldsh").val();
	if(ldsh!=''){
		ldsh = ldsh + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-M-d hh:mm') + "]";
		if(userTask == 'usertask7'){
			//如果当前环节为副市长审批
			if(fszOpinion!=''){
				fszOpinion = ldsh + '<br>' + fszOpinion;
			}else{
				fszOpinion = ldsh;
			}
		}else if(userTask == 'usertask8'){
			//如果当前环节为市长审批
			if(szOpinion!=''){
				szOpinion = ldsh + '<br>' + szOpinion;
			}else{
				szOpinion = ldsh;
			}
		}
	}
	
	var mszsh = $("#mszsh").val();
	if(mszsh!=''){
		mszsh = mszsh + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-M-d hh:mm') + "]";
		if(userTask == 'usertask5'){
			//如果当前环节为副秘书长审批
			if(fmszOpinion!=''){
				fmszOpinion = mszsh + "<br>" + fmszOpinion;
			}else{
				fmszOpinion = mszsh;
			}
		}else if(userTask == 'usertask6'){
			//如果当前环节为秘书长审批
			if(mszOpinion!=''){
				mszOpinion = mszsh + '<br>' + mszOpinion;
			}else{
				mszOpinion = mszsh;
			}
		}
	}
	
	//公开属性原由
	var fnOpenTypeReason = "";
	var openTypeReason = $("#openTypeReason").html();//读出来的
	var lcOTReason = $("#lcOTReason").val();//当前输入的
	if(lcOTReason != ''){
		lcOTReason = lcOTReason + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
	}
	if(openTypeReason == ''){
		fnOpenTypeReason = lcOTReason;
	}else if(lcOTReason == ''){
		fnOpenTypeReason = openTypeReason;
	}else{
		fnOpenTypeReason = lcOTReason + '<br>' +  openTypeReason ;
	}
	
	/*var proposedDate = "";
	if($("#proposedDate").html()!=''){
		proposedDate = $("#proposedDate").html().split('日')[0].replace(/[^\d]/g,'-');
	}*/
	
	var dto = {};
	if(userTask == 'usertask1'){
		//退回起草人
		dto={
			id:id,	
			/*orgAudOpinion:orgAudOpinion,//主办单位意见
*/			openTypeReason:fnOpenTypeReason,//公开属性原由
			
			nfwTitle:$("#nfwTitle").val(),
			openType:$("#openType").val(),
			secretLevel:$('input:checkbox[name="secretLevel"]:checked').val(),
			reportTitle:$("#reportTitle").val(),
			sendUnit:$("#sendUnit").val(),
			ccUnit:$("#ccUnit").val(),
			attachments:$("#attachments").val(),
			remarks:$("#remarks").val(),
			nfwRemarks:$("#nfwRemarks").val(),//备注
			attachmentDTOs:attachmentDTOs,
			
			text:$("#text").val(),
			flag:1,//当前为审批.
		}
	}else if(userTask == 'usertask4'){
		//文书核稿
		dto={
			id:id,
			hgOpinion:hgOpinion,//拟办意见
			proposedAdvice:opStr($("#proposedAdvice").val(),$("#proposedAdviceSPAN").html()),//审核信息
		/*	proposedDate:proposedDate,//审核日期
*/			openTypeReason:fnOpenTypeReason,//公开属性原由
			
			nfwTitle:$("#nfwTitle").val(),
			openType:$("#openType").val(),
			secretLevel:$('input:checkbox[name="secretLevel"]:checked').val(),
			reportTitle:$("#reportTitle").val(),
			sendUnit:$("#sendUnit").val(),
			ccUnit:$("#ccUnit").val(),
			attachments:$("#attachments").val(),
			remarks:$("#remarks").val(),
			nfwRemarks:$("#nfwRemarks").val(),//备注
			attachmentDTOs:attachmentDTOs,
			
			text:$("#text").val(),
			flag:1,//当前为审批.
		}
		
	}/*else if(userTask == 'usertask9'){
		//秘书科室
		var secOpinRepDTO={
			id:$("#opRepCardId").val(),
			sendDocId:$("#id").val(),
			secOpDate:new Date(),
			mainContent:$("#mainContent").val(),
			context:$("#context").val(),
			discuss:$("#discuss").val(),
			impExplain:$("#impExplain").val(),
			toDoOpinion:$("#toDoOpinion").val(),
			remark:$("#remark").val(),
			fszRoleCode:$("#keyPosition").val(),
			mssRoleCode:$("#roleCodes").val()
		}
		dto={
			id:id,
			flag:1,//当前为审批.
			secOpinRepDTO:secOpinRepDTO
		}
	}*/else{
		dto={
			id:id,
			fmszOpinion:fmszOpinion,
			mszOpinion:mszOpinion,
			fszOpinion:fszOpinion,
			szOpinion:szOpinion,
			littlePage:getLittileMsg(),
			attachmentDTOs:attachmentDTOs,
			text:$("#text").val(),
			flag:1,//当前为审批.
		}
	}
	return dto;
}

function getLittileMsg(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		if($("#lPageSpan").html()!=''){
			if($("#userCode").val() == 'lijm'){
				lps = lps + '<br/>' + $("#lPageSpan").html();
			}else{
				lps = $("#lPageSpan").html() + '<br/>' + lps;
			}
		}
		return lps;
	}else{
		return $("#lPageSpan").html();
	}
}

function getStatusData(){
	var dto = {
			id:id
	};
	return dto;
}

function saveRedBtn(url){
	var dto = getStatusData();
	dto.text = url;
	addReportText(dto);
}

function addReportText(dto) {
	doJsonRequest("/senddoc/addReportText",dto,function(data){},{showWaiting:true});
}

//删除左右两端的空格
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function saveLittlePage(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		if($("#lPageSpan").html()!=''){
			if($("#userCode").val() == 'lijm'){
				lps = lps + '<br/>' + $("#lPageSpan").html();
			}else{
				lps = $("#lPageSpan").html() + '<br/>' + lps;
			}
		}
	}else{
		$.alert('请输入备注信息');
		return false;
	}
        
	var dto = { id:id,littlePage:lps};
	doJsonRequest("/senddoc/addReportText",dto,function(data){
            $("#lPageSpan").html(dto.littlePage);
            $("#liPage").val("");
            $.alert("保存成功");
            return;
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=2&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
	},{showWaiting:true});
}

//页面的审批意见带到备注信息中
function wf_getMark(operaterId){
	if(operaterId == 'flow15'){
		return $("#hgOpinion").val();
	}
	if(operaterId == 'flow41' || operaterId == 'flow42' ){
		return $("#ldsh").val();
	}
	if(operaterId == 'flow39' || operaterId == 'flow40'){
		return $("#mszsh").val();
	}
}
function wf_getDate(data) {
	return getTheDate(86400);
}


//----------常用语（开始）------------
function commonLanguage(){
	var obj = {
		    title:'选择常用语',
		    height:"350px",
		    width:"650px",
		    url:'../users/user_common_language.html?userCode='+$("#userCode").val(),
		    myCallBack:"addCommonLanguage"
		};
		new jqueryDialog(obj);
}

function addCommonLanguage(data){
	var language = "";
	$.each(data,function(){
		language += this.language+" ";
	});
	var $_dom = $("#hgOpinion,#ldsh,#mszsh,#proposedAdvice");
	$_dom.each(function(){
		if(!$(this).is(":hidden")){
			$(this).val($(this).val()+language);
			$(this)[0].focus();
			return false;
		}
	});
}

function opStr(hgOpinion,hgOpinionSPAN){
	var hgOp = "";
	if(hgOpinion != ''){
		hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
		if(hgOpinionSPAN!=''){
			hgOp = hgOpinion + "<br>" + hgOpinionSPAN;
		}else{
			hgOp = hgOpinion;
		}
	}
	return hgOp;
}
//----------常用语（结束）------------
