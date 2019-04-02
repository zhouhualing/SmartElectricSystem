//打开时默认显示正文页:
var mark = getURLParam("mark");
var id = getURLParam("id"); //当前收文id
$("#id").val(id);
$("#businessKey").val(id);
var issue = getURLParam("issue");//期刊期号
$("#issue").html(issue);
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
var superDate = "";
var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
var userTask = "";
var officeflag = true;
var officeUrl = "";

$("#qsy a").tab("show");
$(function () {
    'use strict';
    // Initialize the jQuery File Upload widget:
    $('#dataInputForm').fileupload({
        disableImageResize: false,
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: '/cmcp/attachment/upload'
    });

    // Enable iframe cross-domain access via redirect option:
    $('#dataInputForm').fileupload('option', {
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
            maxFileSize: attachment_maxSize,
            acceptFileTypes:attachment_regixType
    });
});
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
	});
	if(id != null) {
		var dto = {id:id}
		doJsonRequest("/super/getSuperVise",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(null!=data.files[0]){
					officeUrl = data.files[0].pdfUrl;
					$('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
					$(".name","#attachmentDiv").children("a").attr("onclick","openAttachments('','',this)")
				}
				var files = data.files;
				var attr = "";
				if(files!=null){
					$.each(files,function(n,item){
						attr+=(n+1)+"、"+item.name+"；"
					})
				}
				$("#attr").data('d',data).val(attr);
				$("#title").val(data.title);
				$("#issue").html(data.issue);
				$("#superCode").html(data.superCode);
				if(data.completeDate!='' && data.completeDate!=null){
					$("#completeDate").val(new Date(data.completeDate).format('yyyy年M月d日'));
				}else{
					$("#completeDate").datepicker({
						showSecond: false, //显示秒
						timeFormat: 'HH:mm',//格式化时间
						stepHour: 1,//设置步长
						stepMinute: 5,
						stepSecond: 10,
						dateFormat:"yy年m月d日",
						currentText:'现在',
						closeText:'确定',
						hourMax:'23',
						hourText:'时',
						minuteText:'分',
						secondText:'秒',
						timeText:'时间',
						buttonText:"选择",
						buttonClass:"btn_click"
					});
				}
				$("#superDate").html(new Date(data.superDate).format('yyyy年M月d日'));
				superDate = new Date(data.superDate);
				$("#content").val(data.content);
				$("#fszOpinion").val(data.fszOpinion);
				var fszOpinion = data.fszOpinion;
				if(fszOpinion!=null){
					$("#ldyjSpan").html(fszOpinion);
				}
				$("#unitSpan").html(data.unitOpinion);
				$("#remarkSpan").html(data.remark);
				$("#status").val(data.status);
				$("#superType").val(data.superType);
				$("#units").val(data.units);
			} else {
				$.alert("获取信息失败。");
			}
			
			doQueryWF("reportInfo","approvalDiv");
			var dto = {taskId:taskId}
			//初始化流程按钮
			wf_getOperator(dto,function(data){
				userTask = data.userTask;//获取当前流程
				$(".hideTd").hide();
				if(userTask == "usertask1"){
					//信息科
					if($("#unitSpan").html()==''){
						$("#content").removeAttr("readOnly");
					}
					$("#remark").removeAttr("readOnly");
				}else if(userTask == "usertask2"){
					//秘书室
					if($("#unitSpan").html()==''){
						$("#content").removeAttr("readOnly");
					}
					$("#remark").removeAttr("readOnly");
				}else if(userTask == "usertask3" ){
					//副市长
					if($("#unitSpan").html()==''){
						$("#content").removeAttr("readOnly");
					}
					$("#ldyj").removeAttr("readOnly");
				}else if(userTask == "usertask4" || userTask == "usertask6"){
					//承办部门
					$(".fileupload-buttons").removeClass('hidden');
					/*$(".hideTd").show();*/
					$("#unitOpinion").removeAttr("readOnly");
					$("#remark").removeAttr("readOnly");
				}
				
				if($("#ldyj").attr('readOnly') == 'readonly'){
					$("#ldyj").hide();
				}
				if($("#unitOpinion").attr('readOnly') == 'readonly'){
					$("#unitOpinion").hide();
				}
				if($("#remark").attr('readOnly') == 'readonly'){
					$("#remark").hide();
				}
			});
		});
	}
});
$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' +officeUrl+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});

function getAddData(){
	var completeDate = $("#completeDate").val();
	if(completeDate.indexOf("日")!=-1){
		completeDate = $("#completeDate").val().split('日')[0].replace(/[^\d]/g,'-');
	}
	var dto = {
			id:$("#id").val(),
			flag:1
	}
	if(userTask!="usertask4" && userTask!="usertask6"){
		//除承办部门之外的环节都可以修改正文内容
		dto.content = $("#content").val();
	}
	if(userTask == "usertask1" || userTask == "usertask2" || userTask == "usertask4" || userTask == "usertask6"){
		//信息科、秘书室、承办部门可以填写备注
		dto.remark = makeOpinions($("#remarkSpan").html(),$("#remark").val());
		
		if(userTask == "usertask2"){
			dto.completeDate = $("#completeDate").val().split('日')[0].replace(/[^\d]/g,'-');
		}
	}
	
	if(userTask == "usertask3"){
		//副市长
		if($("#superType").val() == '0002'){
			//如果当前为副市长督办单流程，则需要将所有信息全部传到后端保存
			dto.issue=$("#issue").html();
			dto.superCode=$("#superCode").html();
			dto.completeDate=completeDate;
			dto.superDate=$("#superDate").html().split('日')[0].replace(/[^\d]/g,'-');
			dto.status=$("#status").val();
			dto.superType=$("#superType").val();
			dto.title=$("#title").val();
			dto.unitOpinion=$("#unitSpan").html();
			dto.remark=$("#remarkSpan").html();
		}
		dto.fszOpinion = makeOpinions($("#fszOpinion").val(),$("#ldyj").val());
	}else if(userTask == "usertask4" || userTask == "usertask6"){
		//承办部门
		var mailAttachmentDTOs = new Array();
		$("input[name='attachmentId']").each(function(){
			var obj = {
				id:$(this).val()
			}
			mailAttachmentDTOs.push(obj);
		});
		dto.unitOpinion = makeOpinions($("#unitSpan").html(),$("#unitOpinion").val());
		if(mailAttachmentDTOs!=null){
			dto.attachmentDTOs =mailAttachmentDTOs;
		}
		dto.units = getUnits();
	}
	return dto;
}

function getUnits(){
	var units = "";
	var localUnit = $("#userOrg").val();
	if($("#units").val()!=''){
		var hisUnits = $("#units").val().split('<br>');
		for(var i=0;i<hisUnits.length;i++){
			if(hisUnits[i]!=localUnit){
				units+=hisUnits[i]+"<br>";
			}
		}
	}
	units += localUnit;
	return units;
}

function makeOpinions(lastOpinion,localOpinion){
	var retOpinion = "";
	if(lastOpinion!='' && localOpinion!=''){
		retOpinion = localOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]<br>"+lastOpinion;
	}else if(lastOpinion == '' && localOpinion!=''){
		retOpinion = localOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	}else if(lastOpinion != '' && localOpinion==''){
		retOpinion = lastOpinion;
	}
	return retOpinion;
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&mark="+mark+"&fromPage=0002&btnSource="+btnSource;
}

function goSuccessEnd(data) {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&mark="+mark+"&flag=6&btnSource="+btnSource;
}

function goSuccessSend(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&mark="+mark+"&flag=4&btnSource="+btnSource;
}

//校验
function wf_beforeValid(){
	window.scrollTo(0,0);
	if(userTask == "usertask2"){
		if($("#completeDate").val()==''){
			$.alert("请输入截止日期。");
			return false;
		}
		var str = $("#completeDate").val();
		// 转换日期格式
		str = str.replace(/-/g, '/'); // "2010/08/01";
		// 创建日期对象
		var completeDate = new Date(str).getTime();
	    //取当前年月日，舍去时分秒
		var d = new Date(new Date().format('yyyy/MM/dd'));
		if(d.getTime()>completeDate){
			$.alert("请选择当前日期后的时间。");
			return false;
		}
	}else if(userTask == "usertask3"){
		//副市长
		if($("#ldyj").val() == ''){
			$.alert("请输入意见。");
			return false;
		}
	}else if(userTask == "usertask4" || userTask == "usertask6"){
		//承办部门
		if($("#unitOpinion").val() == ''){
			$.alert("请输入承办单位意见。");
			return false;
		}
	}
	return true;
}

function openAttachments(url,type,_this){
//	$("#mailFrame").attr("src",$(_this).attr("theurl"))
	 weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}

$("#qsy").click(function(){
	//获取附件名称接口（供主页面使用）
	var attachmentNames = "";
	var i=1;
	$("input[name='attachmentId']").each(function(){
		attachmentNames=attachmentNames+i+"、"+$(this).next().html()+"；";
		i=i+1;
	});
	
	$("#attr").val(attachmentNames);
})

