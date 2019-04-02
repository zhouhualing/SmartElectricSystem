var id = getURLParam("id");

$("#businessKey").val(id);
$(".nav-tabs a:first").tab("show");
//getCurrenUserInfo(function(data){
//	$("#org").html(data.orgName);
//	$("#contactPerson").html(data.userName);
//	$("#contactNumber").html(data.phoneNumber);
//})

function changeCard() {
	if($("#contentType").val() == "0001") {
		$("#hideDiv1").removeClass("hidden");
		$("#hideDiv2").removeClass("hidden");
		$("#fzb").attr("disabled","");
		$("#label1").html("会&nbsp;&nbsp;&nbsp;&nbsp;议<br/>主要目的");
		$("#label2").html("法制办意见");
		$("#label3").html("是否需相关<br/>&nbsp;单位协调&nbsp;");
		$("#label4").html("分管副市长<br/>&nbsp;&nbsp;&nbsp;意见&nbsp;&nbsp;&nbsp;");
		$("#label5").html("秘书长意见");
		$("#label6").html("市长意见");
		wf_getOperator("meetContentCW",function(data){
		});
	} else {
		$("#hideDiv1").addClass("hidden");
		$("#hideDiv2").addClass("hidden");
		$("#fzb").removeAttr("disabled")
		$("#label1").html("主要问题");
		$("#label2").html("现有分歧");
		$("#label3").html("建议");
		$("#label4").html("副市长批示");
		$("#label5").html("秘书长批示");
		$("#label6").html("市长批示");
		wf_getOperator("meetContentBG",function(data){
		});
	}
}

$(function(){
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
	
	$("#year").html(new Date().getTheYear())
	if(id != null) {
		$("#id").val(id)
		var dto = {
				id:id
			}
		$("#fileDiv").addClass("hidden")
		doJsonRequest("/meet/meetcontentlib/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
			    $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data.data});
			    $('#dataInputForm').removeClass('fileupload-processing');
			    $(".hideTd").hide();
				$("#meetContent").val(obj.meetContent);
				if(obj.needHelp=="0001") {
					$("#needHelp").val("需要");
				} else {
					$("#needHelp").val("不需要");
				}
 				
				$("#attendOrg").val(obj.attendOrg);
				$("#attachment").val(obj.attachment);
				$("#org").html(obj.org);
				$("#contactPerson").html(obj.contactPerson);
				$("#contactNumber").html(obj.contactNumber);
				if(obj.contentType == "0001") {
					$("#meetAccord").val(obj.meetAccord);
					$("#describe").val(obj.describe);
					$("#meetGoal").val(obj.meetGoal);
				} else {
					$("#meetGoal").val(obj.meetAccord);
					$("#fzb").val(obj.describe);
					$("#needHelp").val(obj.meetGoal);
				}
				$("#org").html(obj.org);
				$("#contactPerson").html(obj.contactPerson);
				$("#contactNumber").html(obj.contactNumber);
				doGetMarkInfo(id,"meetContentCW");
				doQueryWF("reportInfo","approvalDiv");
			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})


//$("#hiddenTime").datetimepicker({
//	showSecond: false, //显示秒
//	timeFormat: 'HH:mm',//格式化时间
//	stepHour: 1,//设置步长
//	stepMinute: 5,
//	stepSecond: 10,
//	dateFormat:"yy-mm-dd",
//	currentText:'现在',
//	closeText:'确定',
//	hourMax:'23',
//	hourText:'时',
//	minuteText:'分',
//	secondText:'秒',
//	timeText:'时间',
//	buttonText:"选择会议时间",
//	buttonClass:"btn_click"
//});


function setMeetTime() {
	$("#meetTime").val($("#hiddenTime").val())
}

function doSearchUser(_this) {
	var obj = {
	    title:'选择参会人员',
	    height:"350px",
	    width:"750px",
	    url:'../users/follow_user_dialog.html?checkType=multi',
	    myCallBack:initUserInfo,
	    fun:true,
	    srcData:_this
	}
	new jqueryDialog(obj);
}

function initUserInfo(data){
	var userStr = "";
	for(var i=0; i<data.userCode.length; i++) {
		userStr = userStr+ "<label><div style='color:black;font-weight:normal'><input type='hidden' name='userCode' value='"+data.userCode[i]+"'/>"+data.userName[i]+" <a style='color:red' name='removeA'>x</a></div></label>&nbsp;&nbsp;"
	}
	$("#attendsSelectedDiv").html($("#attendsSelectedDiv").html()+userStr);
	$("[name='removeA']",$("#attendsSelectedDiv")).on("click",function(){
		$(this).parent().remove();
	})
}



function getData() {
	var dto = {};
	dto.id = $("#id").val();
	dto.meetContent = $("#meetContent").val();
	dto.needHelp = $("#needHelp").val();
	dto.attendOrg = $("#attendOrg").val();
	dto.attachment = $("#needHelp").val();
	dto.status = "0002";
	if($("#contentType").val() == "0001") {
		dto.meetAccord = $("#meetAccord").val();
		dto.describe = $("#describe").val();
		dto.meetGoal = $("#meetGoal").val();
		dto.contentType="0001";
	} else {
		dto.meetAccord = $("#meetGoal").val();
		dto.describe = $("#fzb").val();
		dto.meetGoal = $("#needHelp").val();
		dto.contentType="0002";
	}
	return dto;
}

function tempSubmit() {
	var dto = getData();
	dto.status = "0001";
	
	doJsonRequest("/meet/meetcontentlib/doSave",dto, function(data){
		if(data.result) {
			$.alert("暂存成功。");
			$("#id").val(data.data.id);
		} else {
			$.alert("保存失败。");
		}
	})
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";
}

function wf_beforeValid() {
	var flag = true;
	
//	if($("#meetContent").val().length ==0) {
//		flag = false;
//		$.alert("请输入会议议题。");
//		return flag;
//	}
//	if($("#meetTime").val().length ==0) {
//		flag = false;
//		$.alert("请选择会议时间。");
//		return flag;
//	}
//	if($("#place").val().length ==0) {
//		flag = false;
//		$.alert("请输入会议地点。");
//		return flag;
//	}
//	if($("#host").val().length ==0) {
//		flag = false;
//		$.alert("请输入主持人。");
//		return flag;
//	}
//	if($("#meetContent").val().length ==0) {
//		flag = false;
//		$.alert("请输入议题。");
//		return flag;
//	}
//	if($("#attend").val().length ==0) {
//		flag = false;
//		$.alert("请输入参会人员。");
//		return flag;
//	}
//	if($("#attendRequire").val().length ==0) {
//		flag = false;
//		$.alert("请输入参会要求。");
//		return flag;
//	}
	return true;
	
}