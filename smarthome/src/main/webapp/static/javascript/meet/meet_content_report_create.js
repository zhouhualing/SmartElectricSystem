var id = getURLParam("id");
var cwRoles = ["034","033","082","083","035","036","084","085","041","086","087","088","045","040","089","090","038","091","092","093","094","095","096","097","098","099","047","100","101","102","103","104","105","106","107","046","108","032","109","110","111","112","113","114","115","044","117","118","119","120","121","122"];
var bgRoles = ["007","067","068","069","070","071","072","073","037","074","075","076","077","014","078","079","080","081"];
$(".nav-tabs a:first").tab("show");
getCurrenUserInfo(function(data){
	$("#org").html(data.orgName);
	$("#contactPerson").val(data.userName);
	$("#contactNumber").val(data.phoneNumber);
//	var roleArr = data.roleCodes.split(" ");
//	for(var i=0; i <roleArr.length; i++) {
//		if($("option[value='0001']").length <= 0) {
//			if($.inArray(roleArr[i],cwRoles)!= -1) {
//				$("#contentType").append('<option value="0001" >常务会议</option>');
//			}		
//			$("#contentType").trigger("change");
//		}
//		
//		if($("option[value='0002']").length <= 0) {
//			if($.inArray(roleArr[i],bgRoles)!= -1) {
//				$("#contentType").append('<option value="0002" >办公会议</option>');
//			}
//			$("#contentType").trigger("change");
//		}	
//	}
})

//function wf_getPrintInfo() {
//	var obj = [];
//	var tem = {
//			businessId:'111',
//			businessType:'test'
//	}
//	obj.push(tem)
//	return obj;
//}



function changeCard() {
	if($("#contentType").val() == "0001") {
		$("#hideDiv1").removeClass("hidden");
		$("#hideDiv2").removeClass("hidden");
		$("#hideDiv4").removeClass("hidden");
		$("#hideDiv3").addClass("hidden");
		$("#fzb").attr("disabled","");
		$("#label1").html("会&nbsp;&nbsp;&nbsp;&nbsp;议<br/>主要目的");
		$("#label2").html("法制办意见");
//		$("#label3").html("是否需相关<br/>&nbsp;单位协调&nbsp;");
		$("#label4").html("分管副市长<br/>&nbsp;&nbsp;&nbsp;意见&nbsp;&nbsp;&nbsp;");
		$("#label5").html("秘书长意见");
		$("#label6").html("市长意见");
		$("#meetGoal").val("");
		$("#fzb").val("");
//		$("#needHelp").val("");
		$("#meetAccord").val("");
		$("#describe").val("");
		wf_getOperator("meetContentCW",function(data){
		});
	} else if($("#contentType").val() == "0002"){
		$("#hideDiv1").addClass("hidden");
		$("#hideDiv2").addClass("hidden");
		$("#hideDiv3").removeClass("hidden");
		$("#hideDiv4").addClass("hidden");
		$("#fzb").removeAttr("disabled")
		$("#label1").html("主要问题");
		$("#label2").html("现有分歧");
//		$("#label3").html("建议");
		$("#label4").html("副市长批示");
		$("#label5").html("秘书长批示");
		$("#label6").html("市长批示");
		$("#meetGoal").val("");
		$("#fzb").val("");
//		$("#needHelp").val("");
		$("#meetAccord").val("");
		$("#describe").val("");
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
	//初始化流程按钮
	wf_getOperator("meetContentCW",function(data){
	});
	if(id != null) {
		$("#id").val(id)
		var dto = {
				id:id
			}
		doJsonRequest("/meet/meetcontentlib/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
			    $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data.data});
			    $('#dataInputForm').removeClass('fileupload-processing');
			    $("#contentType").val(obj.contentType);
			    changeCard();
				$("#meetContent").val(obj.meetContent);
				$("#needHelp").val(obj.needHelp);
				$("#attendOrg").val(obj.attendOrg);
				$("#attachment").val(obj.attachment);
				$("#describe1").val(obj.describe1);
				$("[name='needHelp']").each(function(){
					if($(this).val()==obj.needHelp) {
						$(this).attr("checked","")
					}
				})
				if(obj.contentType == "0001") {
					$("#meetAccord").val(obj.meetAccord);
					$("#describe").val(obj.describe);
					$("#meetGoal").val(obj.meetGoal);
				} else {
					$("#meetGoal").val(obj.meetAccord);
					$("#fzb").val(obj.describe);
					$("#jy").val(obj.meetGoal);
				}
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



function getData(data) {
	var dto = {};
	dto.id = $("#id").val();
	dto.meetContent = $("#meetContent").val();
	dto.meetContentDetail = $("#meetContent").val();
	dto.needHelp = $("[name='needHelp']:checked").val();
	dto.attendOrg = $("#attendOrg").val();
	dto.attachment = $("#attachment").val();
	dto.status = "0002";
	dto.describe1 = $("#describe1").val();
	dto.org = $("#org").html();
	dto.contactPerson = $("#contactPerson").val();
	dto.contactNumber = $("#contactNumber").val();
	if($("#contentType").val() == "0001") {
		dto.meetAccord = $("#meetAccord").val();
		dto.describe = $("#describe").val();
		dto.meetGoal = $("#meetGoal").val();
		dto.contentType="0001";
	} else {
		dto.meetAccord = $("#meetGoal").val();
		dto.describe = $("#fzb").val();
		dto.meetGoal = $("#jy").val();
		dto.contentType="0002";
	}
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});
	dto.attachmentDTOs = attachmentDTOs;

	
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
	if($("#contentType").val() == "0001") {
		if($("#meetContent").val().length ==0) {
			flag = false;
			$.alert("请输入会议议题。");
			return flag;
		}
		if($("#meetAccord").val().length ==0) {
			flag = false;
			$.alert("请输入上会依据。");
			return flag;
		}
		if($("#describe").val().length ==0) {
			flag = false;
			$.alert("请输入上会依据说明。");
			return flag;
		}
		
		if($("#meetGoal").val().length ==0) {
			flag = false;
			$.alert("请输入会议主要目的。");
			return flag;
		}
		if($("[name='needHelp']:checked").length > 0) {
			$("[name='needHelp']:checked").val();
		} else {
			flag = false;
			$.alert("请选择是否需要相关单位协调。");
			return flag;	
		}
		if($("#attendOrg").val().length ==0) {
			flag = false;
			$.alert("请输入列席部门。");
			return flag;
		}	
		if($("#contactPerson").val().length ==0) {
			flag = false;
			$.alert("请输入承办人。");
			return flag;
		}

	} else {
		if($("#meetContent").val().length ==0) {
			flag = false;
			$.alert("请输入会议议题。");
			return flag;
		}
		if($("#meetGoal").val().length ==0) {
			flag = false;
			$.alert("请输入主要问题。");
			return flag;
		}
		if($("#fzb").val().length ==0) {
			flag = false;
			$.alert("请输入现有分歧。");
			return flag;
		}
		if($("#jy").val().length ==0) {
			flag = false;
			$.alert("请输入建议。");
			return flag;
		}
		if($("#attendOrg").val().length ==0) {
			flag = false;
			$.alert("请输入列席部门。");
			return flag;
		}
		
	}
	return true;
	
}