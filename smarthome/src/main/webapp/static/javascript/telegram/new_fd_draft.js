/**
	0001-市政府发文 
	0002-市政府发函 
	0005-办公厅发文 
	0006-办公厅发函
	0009-拟政府发文 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var fromPage = getURLParam("fromPage"); //页面来源
var nfwType = getURLParam("nfwType");
var textUrl = "";
//$("#id").val(id);
$("#reportType").val(type);

//打开时默认显示请示页:
$(".nav-tabs a:first").tab("show");

//正文加载webOffice控件
var flag = false;
var saveflag = true;
var allBtn = "revision_final_saveFile";//weboffice按钮控制
$("#textPage").click(function(){
    
	if(!flag){
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl='+textUrl+'&allBtn='+allBtn+'&taskid='+id+'" style="width:100%;height:800px;"></iframe>');
		flag = true;
	}
});
//.mouseout(function(){
//	if(flag&&saveflag){
//	weboffice.window.saveFileToUrl();
//	saveflag = false;
//	}
//});

$(function(){
	//获取当前登陆人信息
        
	getCurrenUserInfo(function(data){
		$("#userCode").html(data.userCode);
		$("#userName").html(data.userName);
	});
	//初始化流程按钮
	wf_getOperator("sendTelegramApprovalV1",function(data){
	});
	var date = new Date();
	$("#year").val(date.getTheYear());
	$("#month").val(date.getTheMonth());
	$("#day").val(date.getTheDate());
	if(id != null) {
		if(nfwType != "0009"){
			$("#id").val(id);
			$("#businessId").val(id);
		}
		var dto = {
				id:id
		}
		doJsonRequest("/senddoc/getReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#attnPerson").val(data.attnPerson+":"+data.fromNumber);
				$("#jbr").html(data.attnPerson);
				$("#lxdh").html(data.fromNumber);
				$("#fromOrgName").val(data.fromOrgName);
				$("#orgName").html(data.fromOrgName);
				$("#bmks").html(data.fromOrgName);
				//文件标题
				$("#reportTitle").val(data.reportTitle);
				$("#wjbt").html(data.reportTitle);
				//主送抄送报送
				$("#sendUnit").val(data.sendUnit);
				//秘密等级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//紧急程度
				if(data.urgency!='' && data.urgency!=null && data.urgency!='null'){
					$("#urgency").val(data.urgency);
				}else{
					$("#urgency").val('0004');
				}
				//属性
				$("#openType").val(data.openType);
				$("#fileAttr").html($("#openType").children('option:selected').text());//属性与是否公开的选项一致
				//文件编号
				$("#reportCode").val(data.reportCode);
				$("[name='secCode']").html(data.reportCode);
				//签发人(发文最终领导点击完成后添加该领导名字)
				$("#theIssuer").val(data.theIssuer);
				//公开与否说明
				var openTypeReason = data.openTypeReason;
				if(openTypeReason!=null && openTypeReason!=''){
					openTypeReason = openTypeReason.replace("<br>","");
					var ipos = openTypeReason.indexOf(" [");
					$("#openTypeReason").val(openTypeReason.substring(0,ipos));
				}
				$("#proposedAdvice").val(data.proposedAdvice);
				//审批单位意见日期:
				$("#proposedDate").html(data.proposedDate);
				//备注
				$("#remarks").val(data.remarks);
				textUrl = data.text;
				//附件
				$("#attachments").val(data.attachments);
				$("#pageSize").val(data.pageSize);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else{
		//获取当前登陆人信息
		getCurrenUserInfo(function(data){
			$("#fromOrgName").html(data.orgName);
			$("#orgName").html(data.orgName);
			$("#bmks").html(data.orgName);
			$("#attnPerson").val(data.userName+":"+data.phoneNumber);
			$("#jbr").html(data.userName);
			$("#lxdh").html(data.phoneNumber);
		});
		$("#urgency").val('0004');
	}
	var draftDate = new Date().format('yyyy年M月d日');
	$("#auditTime").html(draftDate);
	$("#reportDate").html(draftDate).css("color","black");
});

//----------与信息审核页的录入信息同步(开始)------------
//文件名称
$("#reportTitle").on("blur",function(){
	$("#wjbt").html($("#reportTitle").val());
});
$("#reportCode").on("blur",function(){
	$("[name='secCode']").html($("#reportCode").val());
});

//单位/部门
$("#fromOrgName").on("blur",function(){
	$("#orgName").html($("#fromOrgName").val());
	$("#bmks").html($("#fromOrgName").val());
});
//经办人
$("#attnPerson").on("blur",function(){
	$("#jbr").html($("#attnPerson").split(":")[0]);
});
//联系电话
$("#phoneNumber").on("blur",function(){
	if($("#attnPerson").split(":")[1]){
		$("#lxdh").html($("#attnPerson").split(":")[1]);
	}
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
//----------与信息审核页的录入信息同步(结束)------------

//校验
function wf_beforeValid(){
	//文件名称不能为空
	if($("#reportTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	}/*
            if(validLength($("#openTypeReason").val())>40){
             $.alert("公开属性原因应少-于40字");
		return false;
        }
        if($("#remarks").val().length>30){
             $.alert("备注应少-于30字");
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
	//属性
	if( $("#openType").val() == ''){
		$.alert("请选择属性");
		return false;
	}
	if($("#pageSize").val().length){
              var re = /^[0-9]+.?[0-9]*$/;
               if(!re.test($("#pageSize").val())){
                $.alert('页数不合理'); 
                return false;
           }
           if($("#pageSize").val().length>5){
                $.alert('页数应小于5位'); 
                return false;
                
           }
            
        }
	//公开属性原由
	if( ($("#openType").val() == '0002' || $("#openType").val() == '0003') && $("#openTypeReason").val() == ''){
		$.alert("请填写"+$("#fileAttr").html()+"原由");
		return false;
	}/*
        if($("#fromOrgName").val().length>25){
            $.alert("拟稿单位名字应少-于25字");
            return false;
        }
	if($("#attnPerson").val().length>25){
             $.alert("拟稿人级电话应少-于25字");
            return false;
        }
        if($("#reportCode").val().length>25){
             $.alert("发电编号应少-于25字");
            return false;
        }*/
        /*  if(!$("#reportCode").val()){
             $.alert("请填写发电编号");
            return false;
        }*/
    /*
        if($("#sendUnit").val().length>50){
             $.alert("发往单位名字应少-于50字");
            return false;
        }*/
           if(!$("#sendUnit").val()){
             $.alert("请填写发往单位");
            return false;
        }
    /*
        if($("#reportTitle").val().length>40){
              $.alert("发电标题应该少-于40字");
            return false;
        }
        if($("#attachments").val().length>4*38){
             $.alert("附件字数太多,应少-于150字");
            return false;
        }*/
           window.scrollTo(0,0);
	return true;
}

//删除左右两端的空格
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function getAddData(){

	var attachmentDTOs = new Array();
	//保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		textUrl = officefileUrl;
	}
	
	//公开属性原由
	var openTypeReason = $("#openTypeReason").val();
	if(openTypeReason!=''){
		openTypeReason = openTypeReason + "  [" + $("#userName").html() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
	}
	var fromNumber = "";
	if($("#attnPerson").val().split(":").length >=2){
		fromNumber = $("#attnPerson").val().split(":")[1];
	}
	var nfwId = "";
	if(nfwType == '0009'){
		nfwId = id;
	}
		var dto = {
			id:$("#id").val(),
			reportType:"0003",
			//文件标题
			reportTitle:$("#reportTitle").val(),
			//主送抄送报送
			sendUnit:$("#sendUnit").val(),
			//秘密等级
			secretLevel:$("#secretLevel").val(),
			//紧急程度
			urgency:$("#urgency").val(),
			openType:$("#openType").val(),
			//编号
			reportCode:$("#reportCode").val(),
			//承办单位/承办人/联系电话
			attnPerson:$("#attnPerson").val().split(":")[0],
			fromNumber:fromNumber.substr(0,12),
			fromOrgName:$("#fromOrgName").val(),
			text:textUrl,
			attachmentDTOs:attachmentDTOs,
			attachments:$("#attachments").val(),
			status:"0002",
			openTypeReason:openTypeReason,
			//备注
			remarks:$("#remarks").val(),
			nfwId:nfwId,
			//保密信息审核日期
			auditTime:$("#auditTime").html().split('日')[0].replace(/[^\d]/g,'-'),
			reportDate:$("#reportDate").html().split('日')[0].replace(/[^\d]/g,'-'),
			pageSize:$("#pageSize").val()
		};
		return dto;
}

function goSuccess(data) {
	if(data.operaterId == 'flow16' && data.assignUserName != null){
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
}

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
			if(status == "0002") {
				$.alert({
			    	    title:'提示信息',
			    	    msg:'提交成功。',
			    	    height:180,
			    	    confirmClick:"goSuccess"
			    	});
				
			}	
		} else {
			alert("添加报告出错。");
		}
	},{showWaiting:true});
}

