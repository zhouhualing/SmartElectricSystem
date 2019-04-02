var id = getURLParam("id");
var taskId = getURLParam("taskId");
var officefileUrl = "";
$("#businessKey").val(id);
$(".nav-tabs a:first").tab("show");
var flag = true;
var userTask="";
var meetType="";
var allBtn = "revision_final_saveFile";//weboffice按钮控制
function tempSubmit() {
	weboffice.window.saveFileToUrl();
	attachmentDTOs = weboffice.window.getAttachmentDTOs();
	officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
	var dateStr = $("#year1").val()+"-"+(($("#month1").val().length==1)?("0"+$("#month1").val()):$("#month1").val())+"-"+(($("#day1").val().length==1)?("0"+$("#day1").val()):$("#day1").val());
	var dto = {
			id:$("#id").val(),
			meetContent:$("#meetContent").val(),
			give:$("#give").val(),
			send:$("#send").val(),
			secretLevel:$("#secretLevel").val(),
			urgency:$("#urgency").val(),
			proofread:$("#proofread").val(),
			year:$("#year").val(),
			number:$("#number").val(),
			sendDate:dateStr,
			sendCount:$("#sendCount").val(),
			status:'0001',
			text:officefileUrl
	}

	doJsonRequest("/meet/meetmark/doSave",dto, function(data){
		if(data.result) {
			$.alert("暂存成功。");
			$("#id").val(data.data.id);
		} else {
			$.alert("保存失败。");
		}
	})
}

$(function(){
//	$("#year").html(new Date().getTheYear());
	//初始化流程按钮
	var wfOpDTO　= {
			taskId:taskId
	}
	
	wf_getOperator(wfOpDTO,function(data){
		userTask = data.userTask;
		if("usertask4" == data.userTask) {
			$("#zbdw").removeAttr("disabled")
		}else if("usertask9" == data.userTask) {
			$("#hg").removeAttr("disabled")
		}else if("usertask10" == data.userTask) {
			$("#fgmsz").removeAttr("disabled")
		}else if("usertask11" == data.userTask) {
			$("#msz").removeAttr("disabled")
		}else if("usertask13" == data.userTask) {
			$("#zcr").removeAttr("disabled")
		}else if("usertask12" == data.userTask) {
			$("#zcr").removeAttr("disabled")
		}else if("usertask17" == data.userTask || "usertask18" == data.userTask) {
			if($("#year1").val().length <= 0) {
				$("#year1").removeAttr("disabled")
				$("#month1").removeAttr("disabled")
				$("#day1").removeAttr("disabled")
				$("#sendCount").removeAttr("disabled")
				$("#year").removeAttr("disabled")
				$("#number").removeAttr("disabled")
			}
		} else if(("usertask19" == data.userTask)||("usertask21" == data.userTask)) {
			doGetAttendInfo("meetMark","usertask19,usertask21",id,function(data){
				
				for(var i=0; i<data.length; i++) {
					var arr = data[i].userCode;
					var arrO = data[i].otherUserCode;
					var currentUserCode = data[i].currentUserCode;
					if(($.inArray(currentUserCode,arrO)!=-1)) {

					} else {
						$("li","#buttonUL").eq(0).addClass("hidden");
						$("a",$("li","#buttonUL").eq(1)).trigger("click");
						$("li","#buttonUL").eq(2).addClass("hidden");
						$("#printBtn").addClass("hidden")					
					}
				}
			})
		}
	});
	if(id != null) {
		var dto = {
				id:id
			}
		doJsonRequest("/meet/meetmark/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
				$("#id").val(obj.id);
				$("#meetContent").val(obj.meetContent).trigger("keyup");
				$("#give").val(obj.give).trigger("keyup");
				$("#send").val(obj.send).trigger("keyup");
				$("#urgency").initSpanDict(obj.urgency);
				$("#proofread").val(obj.proofread);
				$("#year").val(obj.year);
				$("#sendCount").val(obj.sendCount);
				meetType = obj.meetType;
				var meetTypeText ="";
				if(obj.meetType == "0001") {
					meetTypeText = "常务";
				} else if(obj.meetType == "0002") {
					meetTypeText = "党组";
				} else {
					meetTypeText = "办公";
				}
				$("#meetTypeText").html(meetTypeText);
				doQueryWF("reportInfo","approvalDiv");
				officefileUrl = obj.text;
				if(obj.sendDate != null) {
					var timeArr = obj.sendDate.split("-")
					$("#year1").val(timeArr[0]);
					$("#month1").val(timeArr[1]);
					$("#day1").val(timeArr[2]);
					$("#number").val(obj.number);
					$("#year").val(obj.year);
				}
				
			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})

$("#textPage").on("click",function(){
	if(flag) {
		var appendStr ="";
		if("usertask17" == userTask || "usertask18" == userTask) {
			appendStr = "showPdfBtn=0001&";
			allBtn = allBtn+"_savePDF";
		}
		if("usertask21" == userTask) {
			allBtn = "";
		}
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice8.html?'+appendStr+'type=approval&id='+id+'&allBtn='+allBtn+'&officeUrl='+officefileUrl+'&taskId='+taskId+'" style="width:100%;height:800px;"></iframe>');
		flag =false;
	} 
});

function getData() {
	weboffice.window.saveFileToUrl();
	attachmentDTOs = weboffice.window.getAttachmentDTOs();
	officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
	var dateStr = $("#year1").val()+"-"+(($("#month1").val().length==1)?("0"+$("#month1").val()):$("#month1").val())+"-"+(($("#day1").val().length==1)?("0"+$("#day1").val()):$("#day1").val());
	var dto = {
			id:$("#id").val(),
			meetContent:$("#meetContent").val(),
			give:$("#give").val(),
			send:$("#send").val(),
			secretLevel:$("#secretLevel").val(),
			urgency:$("#urgency").val(),
			proofread:$("#proofread").val(),
			year:$("#year").val(),
			number:$("#number").val(),
			sendDate:dateStr,
			sendCount:$("#sendCount").val(),
			status:'0002',
			text:officefileUrl
	}
	return dto;
}

function getSendData() {
	var dto = {
			id:id,
			status:'0006',
			sendDate:$("#year1").val()+"-"+$("#month1").val()+"-"+$("#day1").val(),
			sendCount:$("#sendCount").val(),
			number:$("#number").val(),
			year:$("#year").val(),
			text:window.frames["weboffice"].document.getElementById('officeUrl').value
	}
	return dto;
}

function getCompleteData() {
	var dto = {
			id:id,
			status:'0007'
	}
	return dto;
}

function goSuccess(data) {
	var btnSource = "app";
	if(getURLParam("fromPage")=="pc") {
		btnSource = "pc";
	}
	if(data.operaterId == "flow25"){
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=9&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
	}else if(data.operaterId == "flow28" || data.operaterId == "flow44" || data.operaterId == "flow45"|| data.operaterId == "flow46"|| data.operaterId == "flow47"){
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=10&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
	}else if(data.operaterId == "flow65"||data.operaterId == "flow62"){
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=11&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
	} else if(data.operaterId =="flow70") {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=12&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
	}else {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;			
	}

}

function wf_getMark(data) {
	if(("flow54" ==data) || ("flow57" ==data)) {
		return $("#zbdw").val();
	} else if(("flow18" == data)||("flow28" == data)||("flow48" == data)||("next1" == data)) {
		return $("#hg").val();
	} else if(("flow19" == data)||("flow37" == data)||("flow44" == data)||("next2" == data)) {
		return $("#fgmsz").val();
	} else if(("flow20" == data)||("flow30" == data)||("flow38" == data)||("flow45" == data)||("next3" == data)) {
		return $("#msz").val();
	} else if(("flow36" == data)||("flow47" == data)||("flow58" == data)||("flow59" == data)||("next4" == data)||("next5" == data)) {
		return $("#zcr").val();
	}
}

function wf_beforeValid(data) {
	if(flag) {
		$.alert("请先审阅正文。");
		return false;
	} else {
		weboffice.window.saveFileToUrl();
		officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
	}
	if(("flow54" ==data) || ("flow57" ==data)) {
		if($("#zbdw").val().length ==0) {
			$.alert("请输入主办单位意见。");
			return false;
		}
	} else if(("flow18" == data)||("flow28" == data)||("flow48" == data)||("next1" == data)) {
		if($("#hg").val().length ==0) {
			$.alert("请输入核稿意见。");
			return false;
		}
	} else if(("flow19" == data)||("flow37" == data)||("flow44" == data)||("next2" == data)) {
		if($("#fgmsz").val().length ==0) {
			$.alert("请输入审核意见。");
			return false;
		}		
	} else if(("flow20" == data)||("flow30" == data)||("flow38" == data)||("flow45" == data)||("next3" == data)) {
		if($("#msz").val().length ==0) {
			$.alert("请输入审核意见。");
			return false;
		}		
	} else if(("flow36" == data)||("flow47" == data)||("flow58" == data)||("flow59" == data)||("next4" == data)||("next5" == data)) {
		if($("#zcr").val().length ==0) {
			$.alert("请输入签发意见。");
			return false;
		}		
	} else if((data == 'flow65')||(data == 'flow69') ||(data == 'flow62')||(data == 'flow71') ) {
		if($("#year").val().length ==0) {
			$.alert("请输入正确的时间。");
			return false;
		}
		if($("#year1").val().length ==0) {
			$.alert("请输入正确的时间。");
			return false;
		}
		if($("#month1").val().length ==0) {
			$.alert("请输入正确的时间。");
			return false;
		}
		if($("#day1").val().length ==0) {
			$.alert("请输入正确的时间。");
			return false;
		}
		if($("#sendCount").val().length ==0) {
			$.alert("请输入印发数量。");
			return false;
		}
		if($("#number").val().length ==0) {
			$.alert("请输入期数。");
			return false;
		}
		
		if(!officefileUrl.endWith('.pdf')){
			 $.alert('请将正文转为PDF。'); 
			return false;
		}
	}
	return true;
}

/**
 * 主持人环节 常务党组提交会务科 办公提交文书科
 * @returns {String}
 */
function wf_getIgnoreRole() {
	if((userTask == "usertask12") || (userTask=="usertask13")) {
		if(meetType=="0003") {
			return "037";
		} else {
			return "014";
		}
	} else if(userTask == "usertask11") {
		if(meetType=="0003") {
		} else {
			return "014";
		}	
	}
}

$(function(){
	doGetMarkInfo(id,"meetMark");
})