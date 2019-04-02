var id = getURLParam("id");
var taskId = getURLParam("taskId");
var type;

$("#businessKey").val(id);
$(".nav-tabs a:first").tab("show");
//getCurrenUserInfo(function(data){
//	$("#org").html(data.orgName);
//	$("#contactPerson").html(data.userName);
//	$("#contactNumber").html(data.phoneNumber);
//})

function changeCard(type) {
	if(type == "0001") {
		$("#hideDiv1").removeClass("hidden");
		$("#hideDiv2").removeClass("hidden");
		$("#hideDiv4").removeClass("hidden");
		$("#hideDiv3").addClass("hidden");
		$("#label1").html("会&nbsp;&nbsp;&nbsp;&nbsp;议<br/>主要目的");
		$("#label2").html("法制办意见");
		$("#label4").html("分管副市长<br/>&nbsp;&nbsp;&nbsp;意见&nbsp;&nbsp;&nbsp;");
		$("#label5").html("秘书长意见");
		$("#label6").html("市长意见");
	} else {
		$("#hideDiv1").addClass("hidden");
		$("#hideDiv2").addClass("hidden");
		$("#hideDiv3").removeClass("hidden");
		$("#hideDiv4").addClass("hidden");
		$("#label1").html("主要问题");
		$("#label2").html("现有分歧");
		$("#label4").html("副市长批示");
		$("#label5").html("秘书长批示");
		$("#label6").html("市长批示");
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
			    $(".hideTd").addClass("hidden");
				$("#meetContent").val(obj.meetContent);
				$("#needHelp").val(obj.needHelp);
				$("#attendOrg").val(obj.attendOrg);
				$("#attachment").val(obj.attachment);
				if(obj.needHelp == "0001") {
					$("#needHelp").html("<span>&nbsp;需要</span>");
				} else {
					$("#needHelp").html("<span>&nbsp不需要</span>");
				}
				
				$("#org").html(obj.org);
				$("#contactPerson").html(obj.contactPerson);
				$("#contactNumber").html(obj.contactNumber);
				$("#describe1").val(obj.describe1);
				if(obj.contentType == "0001") {
					changeCard("0001")
					$("#contentType").html("常务会议");
					$("#meetAccord").val(obj.meetAccord);
					$("#describe").val(obj.describe);
					$("#meetGoal").val(obj.meetGoal);
					
				} else {
					changeCard("0002")
					$("#contentType").html("办公会议");
					$("#meetGoal").val(obj.meetAccord);
					$("#fzb").val(obj.describe);
					$("#jy").val(obj.meetGoal);
					$("#fszyj").attr("wf_mark","usertask2");
					$("#mszyj").attr("wf_mark","usertask4");
				}
				//目前只有常务会议议题
				doGetMarkInfo(id,"meetContentCW");
				doQueryWF("reportInfo","approvalDiv");
				type = obj.contentType;
				var dto = {
						taskId:taskId
					}
					wf_getOperator(dto,function(data){
						if(obj.contentType == "0001") {
							if(data.userTask=="usertask2") {
								$("#fileDiv").removeClass("hidden")
								$("#fzb").removeAttr("disabled");
								$("#attachment").removeAttr("disabled");
							}
							if(data.userTask=="usertask3") {
								$("#fgfmsz").removeAttr("disabled");
							}
							if(data.userTask=="usertask4") {
								$("#fileDiv").removeClass("hidden")
								$("#attachment").removeAttr("disabled");
								$("#fgfsz").removeAttr("disabled");
							}
							if(data.userTask=="usertask6") {
								$("#msz").removeAttr("disabled");
							}
							if(data.userTask=="usertask7") {
								$("#sz").removeAttr("disabled");
							}
						} else {
							if(data.userTask=="usertask2") {
								$("#fgfsz").removeAttr("disabled");
							}
							if(data.userTask=="usertask4") {
								$("#msz").removeAttr("disabled");
							}	
							if(data.userTask=="usertask5") {
								$("#sz").removeAttr("disabled");
							}
						}
					});
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


function getAttaData() {
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});
	var dto = {
			id:id,
			attachmentDTOs:attachmentDTOs,
			attachment:$("#attachment").val()
	}
	return dto;
}


function getData(data) {
	var dto = {};
	dto.id = $("#id").val();
	dto.meetContent = $("#meetContent").val();
	dto.needHelp = $("#needHelp").val();
	dto.attendOrg = $("#attendOrg").val();
	dto.attachment = $("#attachment").val();
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
	var attachmentDTOs = new Array();
	$("input[name='attachmentId']").each(function(){
		var obj = {
			id:$(this).val()
		}
		attachmentDTOs.push(obj);
	});
	dto.attachmentDTOs = attachmentDTOs;
	
	if(type == "0001") {
		if(data.operaterId == "flow2" || data.operaterId == "flow5") {
			dto.workFlowDTO = {
				isNeedInvoke :"0002"
			}
		}
	} else if(type=="0002") {
//		if(data.operaterId == "flow2" || data.operaterId == "flow5") {
//			dto.workFlowDTO = {
//				isNeedInvoke = "0002";
//			}
//		}
	}
	return dto;
}
function getCompleteDate() {
	var dto = {
			id:id,
			status:'0004'
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
	var btnSource = "app";
	if(getURLParam("fromPage") =="pc") {
		btnSource="pc";
	}
	if(type == "0001") {
		if(data.operaterId == "flow4" || data.operaterId == "flow7" || data.operaterId == "flow9"|| data.operaterId == "flow12"|| data.operaterId == "flow13") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=5&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
		} else if(data.operaterId == "flow16"){
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=6&fromPage=0002&btnSource="+btnSource;				
		}else if(data.operaterId == "flow15"){
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=1&fromPage=0002&btnSource="+btnSource;				
		}else {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
		}
	} else {
		if(data.operaterId == "flow7") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=5&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
		} else {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
		}
	}

}

function wf_getMark(flow) {
	if(type == "0001") {
		if(flow=="flow3" || flow=="flow4" ) {
			return $("#fzb").val();
		}
		if(flow=="flow6" || flow=="flow7" ) {
			return $("#fgfmsz").val();
		}
		if(flow=="flow8" || flow=="flow9" ) {
			return $("#fgfsz").val();
		}
		
		if(flow=="flow11" || flow=="flow12" ) {
			return $("#msz").val();
		}
		if(flow=="flow13" || flow=="flow14" ) {
			return $("#sz").val();
		}
	} else {
		if(flow=="flow3"||flow=="flow4"||flow=="flow5") {
			return $("#fgfsz").val();
		}
		if(flow=="flow8") {
			return $("#msz").val();
		}
		if(flow=="flow9" ||flow=="flow10") {
			return $("#sz").val();
		}
	}
}

function wf_beforeValid(flow) {
	var flag = true;
	if(type=="0001") {
		if(flow=="flow3" || flow=="flow4" ) {
			if($("#fzb").val().length <= 0) {
				flag = false;
				$.alert("请输入法制办意见。");
				return flag;
			}
		}
		
		if(flow=="flow6" || flow=="flow7" ) {
			if($("#fgfmsz").val().length <= 0) {
				flag = false;
				$.alert("请输入分管副秘书长意见。");
				return flag;
			}
		}
		if(flow=="flow8" || flow=="flow9" ) {
			if($("#fgfsz").val().length <= 0) {
				flag = false;
				$.alert("请输入副市长意见。");
				return flag;
			}
		}
		if(flow=="flow11" || flow=="flow12" ) {
			if($("#msz").val().length <= 0) {
				flag = false;
				$.alert("请输入秘书长意见。");
				return flag;
			}
		}
		if(flow=="flow13" || flow=="flow14" ) {
			if($("#sz").val().length <= 0) {
				flag = false;
				$.alert("请输入市长意见。");
				return flag;
			}
		}
	} else {
		if(flow=="flow3"||flow=="flow4"||flow=="flow5") {
			if($("#fgfsz").val().length <= 0) {
				flag = false;
				$.alert("请输入副市长意见。");
				return flag;
			}
		}
		if(flow=="flow8" ) {
			if($("#msz").val().length <= 0) {
				flag = false;
				$.alert("请输入秘书长意见。");
				return flag;
			}
		}
		
		if(flow=="flow9" ||flow=="flow10") {
			if($("#sz").val().length <= 0) {
				flag = false;
				$.alert("请输入市长意见。");
				return flag;
			}
		}
	}

	return true;
	
}
