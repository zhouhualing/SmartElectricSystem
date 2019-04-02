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
var nfwType =  getURLParam("nfwType");
var fullYear = new Date().getFullYear();
var btnSource = "app";
//正文加载webOffice控件
var flag = false;
var allBtn = "revision_final_saveFile";//weboffice按钮控制
var saveflag = true;
if(type =="0001"||type =="0002"){
    $("#rmzfbgt").html("大同市人民政府");
    $("#qfps").html("市长审签：");
    $("#fgfszsq").html("分管副市长审核：");
    $("#mszsq").html("秘书长审核：");
}
if(fromPage == 'pc'){
	btnSource = fromPage;
}
//$("#id").val(id);
if(nfwType == '0009'){
	switch (type) {
		case '0004':
			type = "0005";
			break;
		case '0005':
			type = "0006";
			break;
		default:
			break;
	}
}
$("#reportType").val(type);

//打开时默认显示请示页:
$(".nav-tabs a:first").tab("show");

$(function(){
	//初始化流程按钮
	wf_getOperator("sendDocApprovalV1",function(data){
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
	case '0009':
		title = "大同市人民政府办公厅拟发文卡片";
		break;
	}
	$("#title").text(title);
	$("#repCode").text(repCode);
//	$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
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
				/*$('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
				$('#dataInputForm').removeClass('fileupload-processing');*/
				//文件标题
				$("#reportTitle").val(data.reportTitle);
				$("#wjbt").html(data.reportTitle);
				//主送抄送
				$("#sendUnit").val(data.sendUnit);
				$("#ccUnit").val(data.ccUnit);
				//秘密等级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//紧急程度
				if(data.urgency!='' && data.urgency!=null && data.urgency!='null'){
					$("#urgency").val(data.urgency);
				}else{
					$("#urgency").val('0004');
				}
				//报备
				$("#putOnRecords").val(data.putOnRecords);
				//公开属性
				$("#openType").val(data.openType);
				$("#fileAttr").html($("#"+data.openType).html());//属性与是否公开的选项一致
				$("input[name='ot'][value='"+data.openType+"']").attr("checked",'checked');
				//文件编号
				if(data.reportCode!=null&&data.reportCode!=''){
					var strs= new Array(); //定义一数组 
					strs=data.reportCode.split("_"); //字符分割 
					fullYear = strs[0];
					$("#fullYear").val(strs[0]);
					$("#reportCode").val(strs[1]);
					$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
				}
				//签发人(发文最终领导点击完成后添加该领导名字)
				$("#theIssuer").val(data.theIssuer);
				//公开与否说明
				var openTypeReason = data.openTypeReason;
				if(openTypeReason!=''){
					openTypeReason = openTypeReason.replace("<br>","");
				}
				$("#openTypeReason").val(openTypeReason),
				//备注
				$("#remarks").val(data.remarks);
				$("#text").val(data.text);//正文,暂时的
				//附件
				$("#attachments").val(data.attachments);
				
				/*$("#orgAudOpinion").val(data.orgAudOpinion);
				$("#fzr").append(data.orgAuditing);
				$("#orgAudDate").html(data.orgAudDate);*/
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else{
		$("#urgency").val('0004');
		/*var attaNames="";*///正文附件
	}
	var draftDate = new Date().format('yyyy年M月d日');
	$("#reportDate").html(draftDate);
});

//----------与信息审核页的录入信息同步(开始)------------
//文件名称
$("#reportTitle").on("blur",function(){
	$("#wjbt").html($("#reportTitle").val());
});
$("#reportCode").on("blur",function(){
	if($("#fullYear").val()!=null&&$("#reportCode").val()!=null){
		$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
	}
});
$("#fullYear").on("blur",function(){
	if($("#fullYear").val()!=null&&$("#reportCode").val()!=null){
		$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
	}
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

//选择公开属性:
$("#chooseOpenType input[name='ot']").click(function(){
	$("#openType").val($(this).val());
	$("#fileAttr").html($("#"+$(this).val()).html());
});
//公开信息审核页 起草单位意见动态添加日期:
/*$("#draftUnitOpinion").on("blur",function(){
	if($("#draftUnitOpinion").val()!=''){
		$("#draftUnitDate").html(new Date().format('yyyy-MM-dd hh:mm'));
	}else{
		$("#draftUnitDate").html('');
	}
});*/

//----------与信息审核页的录入信息同步(结束)------------

//----------审批人员签批(开始)------------
/*var flaghg = false;
$("#orgAudOpinion").on("blur",function(){
	if($(this).val()==''){
		$(this).parent().children("label").html('');
		$(this).parent().children("P").children("span").html('');
		flaghg = false;
	}else{
		if(!flaghg){
			if($("#signature").val()!=''){
				$(this).parent().children("label").append("<img alt='' style='width:80px;height:40px;' src='/cmcp/static/images/"+$("#signature").val()+"'>");
			}
			$(this).parent().children("P").children("span").html(new Date().format('yyyy-M-d hh:mm'));
			flaghg = true;
		}
	}
});*/
//----------审批人员签批(结束)------------

//校验
function wf_beforeValid(){
	//文件名称不能为空
	if($("#reportTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	}
    /*
     if(validLength($("#openTypeReason").val())>40){
             $.alert("公开属性原因应少-于40字");
		return false;
        }
        if(validLength($("#remarks").val())>35){
             $.alert("备注应少-于35字");
		return false;
        }
        
         if(validLength($("#proposedAdvice").val())>40){
            $.alert("审批单位意见应少-于40字");
            return false;
        }
        if(validLength($("#reportTitle").val())>40){
		$.alert("文件标题应少-于40字");
		return false;
	}
         if(validLength($("#sendUnit").val())>50){
		$.alert("主送应少-于50字");
		return false;
	} */
         if(!$("#sendUnit").val()){
		$.alert("请填写主送单位");
		return false;
	}
    /*
         if(validLength($("#ccUnit").val())>35){
		$.alert("抄送应少-于35字");
		return false;
               
	}
         if(validLength($("#attachments").val())>150){
		$.alert("附件太长，应少-于150字");
		return false;
             
	}
    */
  /*      if(validLength($("#orgAudOpinion").val())>35){
            $.alert("主办单位审核应少-于35字");
		return false;
        }
         if(validLength($("#putOnRecords").val())>10){
        
		$.alert("报备应少-于10字");
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
	if($('input:radio[name="ot"]:checked').val()==null){
		$.alert("请选择属性");
		return false;
	}
	//公开属性原由
	if(($("#openType").val() == '0002' || $("#openType").val() == '0003') && $("#openTypeReason").val() == ''){
		$.alert("请填写"+$("#fileAttr").html()+"原由");
		return false;
	}
/*	if($("#orgAudOpinion").val()==''){
		$.alert('请填写意见');
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
		$("#text").val(officefileUrl);
	}
	//封装 公开属性原由:
	var openTypeReason = $("#openTypeReason").val();
	if(openTypeReason!='' && openTypeReason.indexOf("[")==-1){
		openTypeReason = openTypeReason + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
	}
	var nfwId = "";
	if(nfwType == '0009'){
		nfwId = id;
	}
	var fullReportCode =null;
	if($("#reportCode").val()!=null&&$("#reportCode").val()!=''){
		fullReportCode =$("#fullYear").val()+"_"+$("#reportCode").val();
	}
		var dto = {
			id:$("#id").val(),
			/*orgAudOpinion:$("#orgAudOpinion").val(),//负责人意见
			orgAuditing:$("#fzr").html(),//负责人签字
			orgAudDate:$("#orgAudDate").html(),*/
			nfwId:nfwId,
			reportType:$("#reportType").val(),
			//文件标题
			reportTitle:$("#reportTitle").val(),
			//主送抄送
			sendUnit:$("#sendUnit").val(),
			ccUnit:$("#ccUnit").val(),
			//秘密等级
			secretLevel:$("#secretLevel").val(),
			//紧急程度
			urgency:$("#urgency").val(),
			//报备
			putOnRecords:$("#putOnRecords").val(),
			//属性
			openType:$("#openType").val(),
			//编号
			reportCode:fullReportCode,
			//承办单位/承办人/联系电话
			fromOrgName:$("#createUserOrgName").val(),
			attnPerson:$("#createUserName").val(),
			fromNumber:$("#phoneNumber").val(),
			//签发人
			theIssuer:$("#theIssuer").val(),
			//公开与否说明
			openTypeReason:openTypeReason,
			//起草单位审核意见
			/*draftUnitOpinion:$("#draftUnitOpinion").val(),*/
			//起草单位审核日期
			/*draftUnitDate:draftUnitDate,*/
			//备注
			remarks:$("#remarks").val(),
			//保密信息审核日期
			reportDate:$("#reportDate").html().split('日')[0].replace(/[^\d]/g,'-'),
			attachmentDTOs:attachmentDTOs,
			attachments:$("#attachments").val(),
			status:"0002",
			//以下不套流程的时候暂时用不到...
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


$("#completeDate").datetimepicker({
	showSecond: true, //显示秒
	timeFormat: 'HH:mm:ss',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-mm-dd",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间'
});
function goSuccess(data) {
	if(data.operaterId == 'flow16' && data.assignUserName != null){
		//起草页面,如果当前操作为提交文书科,且文书科角色下只对应一个用户
		addTransactor(data);
	}
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource="+btnSource;
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
