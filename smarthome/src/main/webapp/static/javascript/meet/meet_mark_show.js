var id = getURLParam("id");
var taskId = getURLParam("taskId");
var officefileUrl = "";
$("#businessKey").val(id);
$(".nav-tabs a:first").tab("show");
var flag = true;
var userTask="";
var allBtn = "revision_final";//weboffice按钮控制
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
			year:$("#year").html(),
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
	doGetAttendInfo("meetMark","usertask19,usertask21",id,function(data){
		for(var i=0; i<data.length; i++) {
			var arr = data[i].userCode;
			var arrO = data[i].otherUserCode;
			var currentUserCode = data[i].currentUserCode;
			if(($.inArray(currentUserCode,arr) != -1)&&($.inArray(currentUserCode,arrO)==-1)) {
				$("li","#buttonUL").eq(0).addClass("hidden");
				$("a",$("li","#buttonUL").eq(1)).trigger("click");
				$("li","#buttonUL").eq(2).addClass("hidden");
				$("#printBtn").addClass("hidden")
			}
		}
	})
	$("#year").html(new Date().getTheYear());
	//初始化流程按钮
	if(id != null) {
		var dto = {
				id:id
			}
		doJsonRequest("/meet/meetmark/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
				$("#id").val(obj.id);
				$("#meetContent").val(obj.meetContent);
				$("#give").val(obj.give);
				$("#send").val(obj.send);
				$("#urgency").initSpanDict(obj.urgency);
				$("#proofread").val(obj.proofread);
				$("#year").html(obj.year);
				$("#number").html(obj.number);
				$("#sendCount").val(obj.sendCount);
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
				}
				
			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})

$("#textPage").on("click",function(){
		if(flag) {
			$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice8.html?type=show&id='+id+'&officeUrl='+officefileUrl+'&taskId='+taskId+'&allBtn='+allBtn+'" style="width:100%;height:800px;"></iframe>');
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
			year:$("#year").html(),
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
			sendDate:$("#year1").val()+"-"+$("#month1").val()+"-"+$("#day1").val(),
			sendCount:$("#sendCount").val()
	}
	return dto;
}

function goSuccess(data) {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";	
}

function wf_getMark(data) {
	if(("flow18" == data)||("flow28" == data)||("flow48" == data)) {
		return $("#hg").val();
	}
	if(("flow19" == data)||("flow37" == data)||("flow44" == data)) {
		return $("#fgmsz").val();
	}
	if(("flow20" == data)||("flow30" == data)||("flow38" == data)||("flow45" == data)) {
		return $("#msz").val();
	}
	if(("flow36" == data)||("flow47" == data)||("flow58" == data)||("flow59" == data)) {
		return $("#zcr").val();
	}
}

function wf_beforeValid(data) {
	if(("flow18" == data)||("flow28" == data)||("flow48" == data)) {
		if($("#hg").val().length ==0) {
			$.alert("请输入核稿意见。");
			return false;
		}
	}
	
	if(("flow19" == data)||("flow37" == data)||("flow44" == data)) {
		if($("#fgmsz").val().length ==0) {
			$.alert("请输入审核意见。");
			return false;
		}		
	}
	
	if(("flow20" == data)||("flow30" == data)||("flow38" == data)||("flow45" == data)) {
		if($("#msz").val().length ==0) {
			$.alert("请输入审核意见。");
			return false;
		}		
	}
	if(("flow36" == data)||("flow47" == data)||("flow58" == data)||("flow59" == data)) {
		if($("#zcr").val().length ==0) {
			$.alert("请输入签发意见。");
			return false;
		}		
	}
	if((data == 'flow65')||(data == 'flow69') ||(data == 'flow62')||(data == 'flow71')) {
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
	}
	return true;
}

$(function(){
	doGetMarkInfo(id,"meetMark");
})