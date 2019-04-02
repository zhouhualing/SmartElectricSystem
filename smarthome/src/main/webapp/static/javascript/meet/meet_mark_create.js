var id = getURLParam("id");
var officefileUrl = "";
var flag = true;
var redFlag = false;
$(".nav-tabs a:first").tab("show");
var allBtn = "revision_final_saveFile";//weboffice按钮控制
function doRed() {
	  if(redFlag) {
		  return ;
          setTimeout(function(){
              redFlag=false;
          },2000);
	  }
      if(!flag){
			//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
			var URL = baseRedHeadUrl+"hyjy_t.doc";
			var endUrl = baseRedHeadUrl+"hyjy_w.doc";
			weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
			$("#redBtn").addClass("hidden");
            redFlag =true;
            window.scrollTo(0,0);
		}else{
			$.alert("请先审阅正文");
		}

}
function tempSubmit() {
	var attachmentDTOs = [];
	if(!flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
	} 

	var dateStr = $("#year1").val()+"-"+(($("#month1").val().length==1)?("0"+$("#month1").val()):$("#month1").val())+"-"+(($("#day1").val().length==1)?("0"+$("#day1").val()):$("#day1").val());
	var dto = {
			id:$("#id").val(),
			meetContent:$("#meetContent").val(),
			give:$("#give").val(),
			send:$("#send").val(),
			secretLevel:$("[name=secretLevel]:checked").val(),
			urgency:$("#urgency").val(),
			proofread:$("#proofread").val(),
			year:$("#year").html(),
			number:$("#number").val(),
			sendDate:dateStr,
			sendCount:$("#sendCount").val(),
			status:'0001',
			text:officefileUrl,
			attachmentDTOs:attachmentDTOs,
			meetType:$("#meetType").val()
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
	wf_getOperator("meetMark",function(data){
		$("#zbdw").removeAttr("disabled");
	});
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
				if(obj.secretLevel == "0004") {
					$("#secretLevel").attr("checked","");
				}
				$("#urgency").val(obj.urgency);
				$("#proofread").val(obj.proofread);
				$("#year").html(obj.year);
				$("#number").val(obj.number);
				$("#sendCount").val(obj.sendCount);
				if(flag) {
					$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice8.html?id='+obj.id+'&officeUrl='+obj.text+'&allBtn='+allBtn+'" style="width:100%;height:800px;"></iframe>');
					flag =false;
				} 
				$("#meetType").val(obj.meetType)
			} else {
				$.alert("获取信息失败。");
			}
		})
	} else {
		$("#urgency").val("0004")
	}
})

$("#textPage").on("click",function(){
	if(flag) {
		$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice8.html?officeUrl='+$("#text").val()+'&allBtn='+allBtn+'" style="width:100%;height:800px;"></iframe>');
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
			secretLevel:$("[name=secretLevel]:checked").val(),
			urgency:$("#urgency").val(),
			proofread:$("#proofread").val(),
			year:$("#year").html(),
			number:$("#number").val(),
			sendDate:dateStr,
			sendCount:$("#sendCount").val(),
			status:'0002',
			text:officefileUrl,
			attachmentDTOs:attachmentDTOs,
			meetType:$("#meetType").val()
	}
	return dto;
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";
}


function wf_getMark(data) {
	return $("#zbdw").val();
}

function wf_beforeValid() {
	if(flag){
		$.alert("请先创建正文。");
		return false;
	} 
	if(confirm("提示：请确认此件已套红。")){
		
	}else{
		return false;
	}
	weboffice.window.saveFileToUrl();
	officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
//	if(!officefileUrl.endWith(".pdf")) {
//		$.alert("请先转为PDF");
//		return false;
//	} 
	if($("#meetContent").val().length ==0) {
		flag = false;
		$.alert("请输入会议议题。");
		return flag;
	}
	
	if($("#send").val().length ==0) {
		flag = false;
		$.alert("请输入发。");
		return flag;
	}
	
	if($("[name=secretLevel]:checked").length ==0) {
		flag = false;
		$.alert("请选择秘密等级。");
		return flag;
	}
	
//	if($("#proofread").val().length ==0) {
//		flag = false;
//		$.alert("请输入校对。");
//		return flag;
//	}
	
//	if($("#number").val().length ==0) {
//		flag = false;
//		$.alert("请输入期数。");
//		return flag;
//	}
	
	if($("#zbdw").val().length ==0) {
		flag = false;
		$.alert("请输入主办单位意见。");
		return flag;
	}
	
	return true;
}