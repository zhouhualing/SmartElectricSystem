var id = getURLParam("id");
var taskId = getURLParam("taskId");
var type;

$(function(){
	$("#businessKey").val(id);
	if(id != null) {
		var dto = {
				id:id
			}
		doJsonRequest("/meet/meetplan/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
				$("#id").val(obj.id);

				if(obj.titleType == "0003"){
					$(".hidenLabel").removeClass("hidden");
					$(".hidenLabel1").addClass("hidden");
					$(".changeTd").each(function(){
						$(this).children("td").eq(0).attr("style","width:18%;font-size:20px;color:black;font-weight: bold;");
						$(this).children("td").eq(1).attr("style","width:82%;color:black");
					})
				} else {
					$(".hidenLabel").addClass("hidden");
					$(".hidenLabel1").removeClass("hidden");
					$(".changeTd").each(function(){
						$(this).children("td").eq(0).attr("style","width:12%;font-size:20px;color:black;font-weight: bold;");
						$(this).children("td").eq(1).attr("style","width:88%;color:black");
					})
				}
				if("0001" == obj.titleType) {
					$("#title").html("大同市人民政府常务会议议题审阅卡");
					$("#titleType1").html("市政府第&nbsp;"+obj.meetCount+"&nbsp;次常务会议议题");
				}
				if("0002" == obj.titleType) {
					$("#title").html("大同市人民政府党组会议议题审阅卡");
					$("#titleType1").html("市政府第&nbsp;"+obj.meetCount+"&nbsp;次党组会议议题");
				}
				if("0003" == obj.titleType) {
					$("#title").html("大同市人民政府全体会议议题审阅卡");
					$("#titleType1").html("大同市人民政府全体会议议题");
				}
				if("0004" == obj.titleType) {
					$("#title").html("大同市人民政府市长办公会议议题审阅卡");
					$("#titleType1").html("市政府第&nbsp;"+obj.meetCount+"&nbsp;次市长办公会议议题");
				}
				if("0005" == obj.titleType) {
					$("#title").html("大同市人民政府副市长办公会议议题审阅卡");
					$("#titleType1").html("市政府第&nbsp;"+obj.meetCount+"&nbsp;次副市长办公会议议题");
				}		
				type = obj.titleType

				$("#place").html(obj.place);
				$("#host").html(obj.host);
				$("#attend").val(obj.attend);
				$("#attend1").val(obj.attend);
				$("#meetContent").val(obj.meetContent);
				$("#attendObserver").val(obj.attendObserver);
				$("#attendObserver1").val(obj.attendObserver);
				$("#personCount").html(obj.personCount);
				$("#personCount1").html(obj.personCount);
				$("#personCount2").html(obj.personCount1);
				$("#personCount3").html(obj.personCount2);
				$("#meetCount").html(obj.meetCount);
				$("#planDate").html(new Date(obj.planDate).format("yyyy 年 MM 月 dd 日 hh:mm"));
				for(var i=0; i <obj.meetPlanContentDTOs.length; i++) {
					addContentFun(obj.meetPlanContentDTOs[i].files,obj.meetPlanContentDTOs[i].meetContent,obj.meetPlanContentDTOs[i].meetContentId,obj.meetPlanContentDTOs[i].contentMark);
				}
			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})


/**
 * add content
 */
function addContent() {
	var obj = {
	    title:'选择议题',
	    height:"320px",
	    width:"750px",
	    url:'../meet/meet_plan_content_list.html',
	    fun:true,
	    myCallBack:afterSelectContent
	}
	nowSearchRole = new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}
function afterSelectContent(obj) {
	var dto = {
			id:obj.id
	}
	doJsonRequest("/meet/meetcontentlib/getInfo",dto,function(data){
		if(data.result) {
			var attachmentFiles = data.data.files;
			addContentFun(attachmentFiles,obj.meetContent,obj.id,'');
		} else {
			$.alert("获取信息失败。");
		}
	})
}

function addContentFun(obj,meetContent,id,mark) {
	var attachmentFiles = obj;
	var attachemntStr = "";
	for(var i=0; i<attachmentFiles.length; i++) {
		attachemntStr = attachemntStr+ "<label><div><input type='hidden' name='attachmentId' value='"+attachmentFiles[i].id+"'/><a style='color:black;font-weight: normal;' href='"+attachmentFiles[i].url+"'><u>"+attachmentFiles[i].name+"</u></a></div></label>&nbsp;&nbsp;"
	}
	var theSize = $("[name='meetContentTr']").size();
	var str = $('<tr name="meetContentTr" style="padding:0x">'+
		'<td style="width:3%;font-size:15px; padding-top:5px;vertical-align: top;"></td>'+
		' <td   style="width:97%; padding-top:5px;">'+
		'	<div  name="meetContent" rows="1" style="width:100%;font-size:20px;font-weight:bold;color:black"  type="text" disabled idValue="'+(theSize+1)+'"> </div>'+
		' </td>'+
		'</tr>'+
		'<tr name="meetContentMarkTr">'+
		'<td  colspan="2" >'+
		'<table width="100%" border="0" style="margin:3px">'+
		'<tr>'+
		' <td style="width:11%;"></td>'+
		' <td style="width:86%;">'+
		'	<input type="hidden" name="contentId" value="'+id+'"/><textarea style="width:95%;" disabled name="contentMark" cols="" rows="1" onkeyup="autoSize(this)">'+mark+'</textarea>'+
		'</td>'+
		'</tr>'+
		'</table>'+
		'</td>'+
		'</tr>'+
		'<tr class="hidden">'+
		'<td  colspan="2" style="padding-top:10px">'+
		'<table width="100%" border="0" style="margin:3px">'+
		'<tr>'+
		' <td style="width:11%;color:black;">&nbsp;&nbsp;&nbsp;&nbsp;附件：</td>'+
		' <td style="width:86%;" name="attachmentTd">'+
		attachemntStr+
		'</td>'+
		'</tr>'+
		'</table>'+
		'</td>'+
		'</tr>');
	$("#meetInfo").append(str);
	if(meetContent.startWith("<p>")) {
		$("[name='meetContent']",str).html("<p>"+doLowwerToUpper(theSize+1)+"、"+meetContent.substring(3))
	} else {
		$("[name='meetContent']",str).html(doLowwerToUpper(theSize+1)+"、"+meetContent)
	}
	$("[name='removeA']",str).on("click",function(){
		$(this).parent().remove();
	})
	$("[name='removeTr']",str).on("click",function(){
		$(this).parent().parent().nextAll("tr:lt(2)").remove();
		$(this).parent().parent().nextAll("tr[name='meetContentTr']").each(function(){
			$("[name='meetContent']",this).val(doLowwerToUpper($("[name='meetContent']",this).attr("idValue")-1)+" "+$("[name='meetContent']",this).val().substring(2));
			$("[name='meetContent']",this).attr("idValue",$("[name='meetContent']",this).attr("idValue")-1);
		})
		$(this).parent().parent().remove();
		
	})
	
	autoSize($("[name='meetContent']",str));	
	autoSize($("[name='contentMark']",str));
}

function getData() {
//	var dateStr = $("#year").val()+"-"+(($("#month").val().length==1)?("0"+$("#month").val()):$("#month").val())+"-"+(($("#day").val().length==1)?("0"+$("#day").val()):$("#day").val())+" "+(($("#hour").val().length==1)?("0"+$("#hour").val()):$("#hour").val())+":"+(($("#minute").val().length==1)?("0"+$("#minute").val()):$("#minute").val());
//	var meetPlanContentObj = [];
//	$("[name='meetContentMarkTr']").each(function() {
//		var obj = {};
//		obj.meetContentId = $("[name='contentId']",this).val();
//		obj.contentMark = $("[name='contentMark']", this).val();
//		var attIds = [];
//		$("[name='attachmentId']",$(this).next("tr")).each(function(){
//			attIds.push($(this).val())
//		})
//		obj.attachmentIds = attIds.join(",");
//		meetPlanContentObj.push(obj);
//	})
	var dto = {
			id:$("#id").val(),
			titleType:$("#titleType").val(),
			status:'0002',
			place:$("#place").val(),
			host:$("#host").val(),
			attend:$("#attend").val(),
			attendObserver:$("#attendObserver").val(),
			personCount:$("#personCount").val(),
			meetCount:$("#meetCount").val(),
			workFlowDTO:{
				isNeedInvoke:'0002'
			}
	}
	
	return dto;
}

function tempSubmit() {
	var dateStr = $("#year").val()+"-"+(($("#month").val().length==1)?("0"+$("#month").val()):$("#month").val())+"-"+(($("#day").val().length==1)?("0"+$("#day").val()):$("#day").val())+" "+(($("#hour").val().length==1)?("0"+$("#hour").val()):$("#hour").val())+":"+(($("#minute").val().length==1)?("0"+$("#minute").val()):$("#minute").val());
	var meetPlanContentObj = [];
	$("[name='meetContentMarkTr']").each(function() {
		var obj = {};
		obj.meetContentId = $("[name='contentId']",this).val();
		obj.contentMark = $("[name='contentMark']", this).val();
		var attIds = [];
		$("[name='attachmentId']",$(this).next("tr")).each(function(){
			attIds.push($(this).val())
		})
		obj.attachmentIds = attIds.join(",");
		meetPlanContentObj.push(obj);
	})
	var dto = {
			id:$("#id").val(),
			titleType:$("#titleType").val(),
			status:'0001',
			place:$("#place").val(),
			host:$("#host").val(),
			attend:$("#attend").val(),
			attendObserver:$("#attendObserver").val(),
			personCount:$("#personCount").val(),
			meetCount:$("#meetCount").val(),
			planDate:dateStr,
			meetPlanContentDTOs:meetPlanContentObj
	}

	doJsonRequest("/meet/meetplan/doSave",dto, function(data){
		if(data.result) {
			$.alert("暂存成功。");
			$("#id").val(data.data.id);
		} else {
			$.alert("保存失败。");
		}
	})
}


function goSuccess(data) {
	if(type=="0004") {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";			
	}else if(type=="0005") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";	
	} else {
		if(data.operaterId == "flow3") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=4&roleName="+roleName+"&fromPage=0002&btnSource=app";
		} else {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";	
		}
	}

}

function wf_getMark(data) {
	if(type=="0004") {
		if(data=="flow3" || data=="flow6") {
			return $("#mszys").val();
		}
		if(data=="flow4" || data=="flow5") {
			return $("#zcrsq").val();
		}
	}else if(type=="0005") {
		if(data=="flow3" || data=="flow6"||data=="flow7") {
			return $("#zcrsq").val();
		}
		if((data == "flow5")||(data == "flow8")) {
			return $("#mszys").val();
		}
		if((data=="flow9")||(data=="flow10")) {
			return $("#zcrsq").val();
		}
	} else {
		if((data == "flow4")||(data == "flow5")||(data == "flow6")) {
			return $("#mszys").val();
		}
		if(("flow10" == data)||("flow8" == data)) {
			return $("#zcrsq").val();
		}
		if((data=="flow7")||(data=="flow9")) {
			return $("#zcrsq").val();
		}					
	}

}

function wf_beforeValid(data) {
	if(type=="0004") {
		if(data=="flow3" || data=="flow6") {
			if($("#mszys").val().length == 0) {
				$.alert("请输入阅示意见。");
				return false;
			}
		}
		if(data=="flow4" || data=="flow5") {
			if($("#zcrsq").val().length == 0) {
				$.alert("请输入审签意见。");
				return false;
			}
		}
	}else if(type=="0005") {
		if(data=="flow3" || data=="flow6"||data=="flow7") {
			if($("#zcrsq").val().length == 0) {
				$.alert("请输入审签意见。");
				return false;
			}
		}
		if((data == "flow5")||(data == "flow8")) {
			if($("#mszys").val().length == 0) {
				$.alert("请输入阅示意见。");
				return false;
			}
		}
		if((data=="flow9")||(data=="flow10")) {
			if($("#zcrsq").val().length == 0) {
				$.alert("请输入审签意见。");
				return false;
			}		
		}
	} else {
		if((data == "flow4")||(data == "flow5")||(data == "flow6")) {
			if($("#mszys").val().length == 0) {
				$.alert("请输入阅示意见。");
				return false;
			}
		}
		if((data=="flow8")||(data=="flow10")) {
			if($("#zcrsq").val().length == 0) {
				$.alert("请输入审签意见。");
				return false;
			}		
		}
		
		if((data=="flow7")||(data=="flow9")) {
			if($("#zcrsq").val().length == 0) {
				$.alert("请输入审签意见。");
				return false;
			}		
		}					
	}

	return true;
}

$(function(){
	
})