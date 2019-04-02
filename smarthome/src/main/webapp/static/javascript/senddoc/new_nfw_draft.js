
/**
	0009-拟政府发文 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
$("#id").val(id);
$("#reportType").val(type);
//打开时默认显示请示页:
$(".nav-tabs a:first").tab("show");
//正文加载webOffice控件
var flag = false;
var saveflag = true;
var allBtn = "revision_final_saveFile";//weboffice按钮控制
/**
 * 当前为拟发电报时，页面元素调整，去掉属性，主送抄送报送。
 */
function nfdbHide(){
	$("tr[name='nfdb']").hide();
}
function nfdbShow(){
	$("tr[name='nfdb']").show();
}
$(function(){
	//初始化流程按钮
	wf_getOperator("draftSendDocApprovalV1",function(data){
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#createUserName").val(data.userName);
		$("#jbr").html(data.userName);
		$("#createUserOrgName").val(data.orgName);
		$("#fromOrgName").html(data.orgName);
		$("#bmks").html(data.orgName);
		$("#phoneNumber").val(data.phoneNumber);
		$("#lxdh").html(data.phoneNumber);
		$("#signature").val(data.signature);
		
		$("#textPage").click(function(){
			if(!flag){
				$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl='+$("#text").val()+'&allBtn='+allBtn+'&taskid='+id+'&userName='+$("#userName").val()+'" style="width:100%;height:800px;"></iframe>');
				flag = true;
			}
		});
	});
	var draftDate = new Date().format('yyyy年M月d日');
	$("#reportDate").html(draftDate);
	$("#auditTime").html(draftDate);
	if(id != null) {
		$("#id").val(id);
		$("#businessId").val(id);
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		}
		doJsonRequest("/senddoc/getReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				//拟发文标题
				$("#nfwTitle").val(data.nfwTitle);
				if(data.nfwTitle == '0003'){
					nfdbHide();
				}
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
				//属性
				$("#openType").val(data.openType);
				$("#fileAttr").html($("#openType").children('option:selected').text());//属性与是否公开的选项一致
				//密级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//文件标题
				$("#reportTitle").val(data.reportTitle);
				$("#wjbt").html(data.reportTitle);
				//领导审核
				$("#leadingAudit").html(data.leadingAudit);
				/*//主办单位审核删除签字
				if(data.orgAudOpinion!=null && data.orgAudOpinion!=''){
					var str = data.orgAudOpinion;
					var ipos2 = str.indexOf("  [");
					$("#orgAudOpinion").val(str.substring(0,ipos2));
				}*/
				//备注
				$("#nfwRemarks").val(data.nfwRemarks);
				//主送抄送报送
				$("#sendUnit").val(data.sendUnit);
				$("#ccUnit").val(data.ccUnit);
				//编号
				$("#nfwCode").html(data.nfwCode);
				/*
				 * 信息公开审核页
				 */
				//公开与否说明删除签字
				var orgaudops = data.openTypeReason;
				if(orgaudops!=null && orgaudops!=''){
					var ipos = orgaudops.indexOf("  [");
					$("#openTypeReason").val(orgaudops.substring(0,ipos));
				}
				
				//文件编号
				$("#reportCode").val(data.reportCode);
				//签发人(发文最终领导点击完成后添加该领导名字)
				/*$("#theIssuer").val(data.theIssuer);*/
				//备注
				$("#remarks").val(data.remarks);
				//审批单位意见日期:
				$("#proposedDate").html(data.proposedDate);
				
				$("#text").val(data.text);//正文,暂时的
				
				//附件
				$("#attachments").val(data.attachments);
				
				//发文依据
				$("#docBasis").val(data.docBasis);
				//采纳意见
				$("#accept").val(data.accept);
				//未采纳意见
				$("#notAccept").val(data.notAccept);
				//主要分歧
				$("#mainDiffer").val(data.mainDiffer);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else{
		doJsonRequest("/code/getCode","0009",function(data){
			if(data.result) {
				$("#nfwCode").html(data.data);
			} else {
				$.alert("获取编码出错。");
			}
		});
	}
	$("#moban_title").html($("#nfwTitle").children('option:selected').text());//标题同步
	/**
	 * 当选择类型为发电时，调整页面元素。
	 */
	$("#nfwTitle").change(function(){
		$("#moban_title").html($(this).children('option:selected').text());
		if($("#nfwTitle").val() == '0003'){
			nfdbHide();
		}else{
			nfdbShow();
		}
	});
});

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
$("#proposedAdvice").on("blur",function(){
	if($("#proposedAdvice").val()!=''){
		$("#proposedDate").html(new Date().format('yyyy-M-d hh:mm'));
	}else{
		$("#proposedDate").html('');
	}
});
// ----------与信息审核页的录入信息同步(结束)------------



//删除左右两端的空格
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

//校验
function wf_beforeValid(){
	//文件名称不能为空
    /*
         if(validLength($("#openTypeReason").val())>40){
             $.alert("公开属性原因应少-于40字");
		return false;
        }*/
      
        if(!$("#createUserOrgName").val().length){
            $.alert("请填写拟文单位");
		return false;
               
        }
         if(!$("#createUserName").val().length){
            $.alert("请填写拟文人");
		return false;
                
        } if(!$("#phoneNumber").val().length){
            $.alert("请填写联系电话");
		return false;
                
        }
	if($("#reportTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	}
    /*
        if($("#reportTitle").val().length>40){
		$.alert("文件标题应少-于40字");
		return false;
	}*/
        
  /*        if(validLength($("#orgAudOpinion").val())>35){
		$.alert("主办单位应少-于35字");
		return false;
	}
            if($("#remarks").val().length>30){
             $.alert("审核页备注应少-于30字");
		return false;
        }
         if($("#nfwRemarks").val().length>35){
		$.alert("请示页备注应少-于35字");
		return false;
	}*/
         if($("#sendUnit").val()==''){
             $.alert("请填写主送单位");
 		return false;
                
         }
    /*
         if($("#sendUnit").val().length>50){
		$.alert("主送应少-于50字");
		return false;
	}*/
         
         if(!$("#createUserOrgName").val().length){
             $.alert("请填写拟文单位");
 		return false;
                
         }/*
         if($("#ccUnit").val().length>35){
		$.alert("抄送应少-于35字");
		return false;
	}
         if($("#attachments").val().length>4*38){
		$.alert("附件字数太多，应少-于150字");
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
    if($("#phoneNumber").val().length<12){
      var re = /^[0-9]+.?[0-9]*$/;
      if(!re.test($("#phoneNumber").val())){
          $.alert("电话格式错误");
          return false;
      }
    }
  if($("#phoneNumber").val().length>=12){
   $.alert("电话格式错误");
	return false;
  }
	//公开属性原由
	if( ($("#openType").val() == '0002' || $("#openType").val() == '0003') && $("#openTypeReason").val() == ''){
		$.alert("请填写"+$("#fileAttr").html()+"原由");
		return false;
	}
        window.scrollTo(0,0);
	return true;
}

function getAddData(){
	var attachmentDTOs = new Array();
	
	
	//保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		$("#text").val(officefileUrl);
	}
	//主办单位 加时间
/*	var orgAudOpinion = $("#orgAudOpinion").val();
	if(orgAudOpinion!=''){
		orgAudOpinion = orgAudOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	}*/
	//公开属性原由
	var openTypeReason = $("#openTypeReason").val();
	if(openTypeReason!='' && openTypeReason.indexOf("[")==-1){
		openTypeReason = openTypeReason + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
	}
		var dto = {
			id:$("#id").val(),
			reportType:$("#reportType").val(),
			nfwTitle:$("#nfwTitle").val(),
			reportDate:$("#reportDate").html().split('日')[0].replace(/[^\d]/g,'-'),
			auditTime:$("#auditTime").html().split('日')[0].replace(/[^\d]/g,'-'),
			fromOrgName:$("#createUserOrgName").val(),
			attnPerson:$("#createUserName").val(),
			fromNumber:$("#phoneNumber").val(),
			openType:$("#openType").val(),
			secretLevel:$('input:checkbox[name="secretLevel"]:checked').val(),
			reportTitle:$("#reportTitle").val(),
			/*orgAudOpinion:orgAudOpinion,//主办单位审核
*/			nfwRemarks:$("#nfwRemarks").val(),//备注
			sendUnit:$("#sendUnit").val(),
			ccUnit:$("#ccUnit").val(),
			nfwCode:$("#nfwCode").html(),//编号
			openTypeReason:openTypeReason,
			reportCode:$("#reportCode").val(),
			remarks:$("#remarks").val(),
			status:"0002",
			attachmentDTOs:attachmentDTOs,
			attachments:$("#attachments").val(),
			//发文依据
			docBasis:$("#docBasis").val(),
			//采纳意见
			accept:$("#accept").val(),
			//未采纳意见
			notAccept:$("#notAccept").val(),
			//主要分歧
			mainDiffer:$("#mainDiffer").val(),
			text:$("#text").val(),//正文,暂时的
			flag:0,//当前为新建流程.
		};
		return dto;
}

//*************************
//保存到草稿箱
$("#tempSubmitBtn").on("click",function(){
	addReport("0001");
});

function addReport(status) {
	var dto = getAddData();
	dto.status = status;
	doJsonRequest("/senddoc/addReport",dto,function(data){
		if(data.result) {
			var data = data.data;
			$("#id").val(data.id);
			$("#businessId").val(data.id);
			if(status == "0001") {
				$.alert("暂存成功。");
			}
		} else {
			alert("添加报告出错。");
		}
	},{showWaiting:true});
}

function goSuccess(data) {
	if((data.operaterId == 'flow3') && data.assignUserName != null){
		//起草页面,如果当前操作为提交文书科,且文书科角色下只对应一个用户
		addTransactor(data);
	}
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource=app";
}

function addTransactor(data){
	var dto = {
			id:data.businessKey,
			attnCode:data.assignUserCode
	};
	doJsonRequest("/senddoc/addReportText",dto,function(data){});
}

function wf_getDate(data) {
	return getTheDate(86400);
}


function checkNum(dom){
	if($(dom).val()!='' && isNaN($(dom).val())){
	   $.alert("请输入数字。");
	   return false;
	}
}