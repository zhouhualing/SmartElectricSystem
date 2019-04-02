var id = getURLParam("id");
var taskId = getURLParam("taskId");
var type;

function createDoc() {
	var dto =  {
			id:id
	}
	doJsonRequest("/meet/meetplan/createMeetDoc",dto,function(data){
		if(data.result) {
			$.alert("议程/议题单创建成功。");
		} else {
			$.alert("创建议题单出错。")
		}
	},{showWaiting:true})
}

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
						$(this).children("td").eq(0).attr("style","width:19%;font-size:20px;color:black;font-weight: bold;");
						$(this).children("td").eq(1).attr("style","width:81%;color:black");
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
					$("#titleType1").html("市&nbsp;政&nbsp;府&nbsp;常&nbsp;务&nbsp;会&nbsp;议");
				}
				if("0002" == obj.titleType) {
					$("#title").html("大同市人民政府党组会议议题审阅卡");
					$("#titleType1").html("市&nbsp;政&nbsp;府&nbsp;党&nbsp;组&nbsp;会&nbsp;议");
				}
				if("0003" == obj.titleType) {
					$("#title").html("大同市人民政府全体会议议题审阅卡");
					$("#titleType1").html("大&nbsp;同&nbsp;市&nbsp;人&nbsp;民&nbsp;政&nbsp;府&nbsp;全&nbsp;体&nbsp;会&nbsp;议");
				}
				if("0004" == obj.titleType) {
					$("#title").html("大同市人民政府市长办公会议议题审阅卡");
					$("#titleType1").html("市&nbsp;长&nbsp;办&nbsp;公&nbsp;会&nbsp;议");
					$("#businessType").val("meetPlanSz");
					$("#reportInfo").attr("wf-tasklist",'{workFlowTypeId:"meetPlanSz",title:"流程跟踪"}');
				}
				if("0005" == obj.titleType) {
					$("#title").html("大同市人民政府副市长办公会议议题审阅卡");
					$("#titleType1").html("副&nbsp;市&nbsp;长&nbsp;办&nbsp;公&nbsp;会&nbsp;议");
					$("#businessType").val("meetPlanFsz");
					$("#reportInfo").attr("wf-tasklist",'{workFlowTypeId:"meetPlanFsz",title:"流程跟踪"}');
				}	
				for(var i=0; i <obj.meetPlanContentDTOs.length; i++) {
					addContentFun(obj.meetPlanContentDTOs[i].files,obj.meetPlanContentDTOs[i].meetContent,obj.meetPlanContentDTOs[i].meetContentId,obj.meetPlanContentDTOs[i].contentMark,obj.meetPlanContentDTOs[i].reportType,obj.meetPlanContentDTOs[i].meetContentDocDTOs);
				}
				if(obj.meetPlanContentDTOs.length <=0) {
					$("#contentTitleTr").addClass("hidden")
				}
				doQueryWF("reportInfo","approvalDiv");
				type = obj.titleType
				var dto = {
						taskId:taskId
				}
				//初始化流程按钮
				wf_getOperator(dto,function(data){
					if(data.userTask == "usertask1") {
						$("#createDocBtn").removeClass("hidden");
						$("#addContentBtn").removeClass("hidden");
					} else {
						$("[name='removeA']").addClass("hidden");
						$("[name='removeTr']").addClass("hidden");
						$("[name='contentMark']").attr("disabled","")
					}
					if(obj.titleType=="0004") {
						$("#msz").attr("wf_mark","usertask2");
						$("#zcr").attr("wf_mark","usertask3");
						if(data.userTask == "usertask2" ) {
							$("#mszys").removeAttr("disabled");
						}
						if(data.userTask == "usertask3" ) {
							$("#zcrsq").removeAttr("disabled");
						}
						doGetMarkInfo(id,"meetPlanSz");
					}else if(obj.titleType=="0005") {
						$("#zcr").attr("wf_mark","usertask2,usertask5");
						$("#msz").attr("wf_mark","usertask4");
						if(data.userTask == "usertask2" || data.userTask == "usertask5") {
							$("#zcrsq").removeAttr("disabled");
						}
						if(data.userTask == "usertask4") {
							$("#mszys").removeAttr("disabled");
						}
						doGetMarkInfo(id,"meetPlanFsz");
					} else {
						if(data.userTask == "usertask2") {
							$("#mszys").removeAttr("disabled");
						}
						if(data.userTask == "usertask3") {
							$("#zcrsq").removeAttr("disabled");
						}	
						if(data.userTask == "usertask4") {
							$("#zcrsq").removeAttr("disabled");
						}	
						doGetMarkInfo(id,"meetPlan");
					}

				});
				$("#place").html(obj.place);
				$("#host").html(obj.host);
				$("#attend").val(obj.attend).trigger("keyup");
				$("#attend1").val(obj.attend).trigger("keyup");
				$("#meetContent").val(obj.meetContent);
				$("#attendObserver").val(obj.attendObserver).trigger("keyup");
				$("#attendObserver1").val(obj.attendObserver).trigger("keyup");
				$("#personCount").html(obj.personCount);
				$("#personCount1").html(obj.personCount);
				$("#personCount2").html(obj.personCount1);
				$("#personCount3").html(obj.personCount2);
				$("#meetCount").html(obj.meetCount);
				$("#planDate").html(new Date(obj.planDate).format("yyyy 年 MM 月 dd 日 ")+obj.time);
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
			addContentFun(attachmentFiles,obj.meetContent,obj.id,'',obj.reportType,data.data.meetContentDocDTOs);
		} else {
			$.alert("获取信息失败。");
		}
	})
}
function doAlert() {
	$.alert("该议题没有议题呈报单。");
}
function addContentFun(obj,meetContent,id,mark,type,systemFiles) {
	var attachmentFiles = obj;
	var attachemntStr = "";
	 var nowSystemFiles = systemFiles;
	for(var i=0; i<attachmentFiles.length; i++) {
		attachemntStr = attachemntStr+ "<label><div style='color:blue'><input type='hidden' name='attachmentId' value='"+attachmentFiles[i].id+"'/><a style='color:blue' href='"+attachmentFiles[i].url+"'>"+attachmentFiles[i].name+"</a> <a style='color:red' class='' name='removeA'>x</a></div></label>&nbsp;&nbsp;"
	}
    for(var i=0; i<nowSystemFiles.length; i++) {
        var type = nowSystemFiles[i].businessType;
        var businessId = nowSystemFiles[i].businessId;
        var href = "";
        if(type == "0001" || type == "0002"  || type == "0005"  || type == "0006" ){
            //发文、发函
            href="../senddoc/new_fwfh_check.html?id="+businessId+"&type="+type+"&showCard=1";
        }else if(type == "0003"){
            //发电
            href="../telegram/new_fd_check.html?id="+businessId+"&type="+type;
        }else if(type == "0009"){
            //拟发文
            href="../senddoc/new_nfw_check.html?id="+businessId+"&type="+type+"&showCard=1";
        }else if(type == "5001"){
            //上级来文
            href="../receivedoc/new_sjlw_check.html?id="+businessId+"&type="+type;
        }else if(type == "5002"){
            //下级来文
            href="../receivedoc/new_xjlw_check.html?id="+businessId+"&type="+type+"&checkFlag=1";
        }else if(type == "5003"){
            //请示报告
            href="../report/new_reqrep_check.html?id="+businessId+"&type="+type+"&checkFlag=1";
        }else if(type == "6001"){
            //来电
            href="../telegram/new_sd_check.html?id="+businessId+"&type="+type;
        }else if(type == "7001"){
            //委办局函文
            href="../letter/bureaus_letter_read.html?id="+businessId+"&type="+type;
        }
        attachemntStr = attachemntStr+ "<label><div style='color:blue'><input type='hidden' name='contentDocId' value='"+nowSystemFiles[i].id+"'/><a style='color:blue' href='"+href+"' target='_blank'>"+nowSystemFiles[i].businessTitle+"</a> </div></label>&nbsp;&nbsp;"
    }
	var theSize = $("[name='meetContentTr']").size();
	var str1 = "";
	if(type=="0002") {
		str1='	<a onclick="doAlert()"><div  name="meetContent" rows="1" style="width:100%;font-size:20px;font-weight:bold;color:black "  type="text" disabled idValue="'+(theSize+1)+'"> </div></a>';
	} else {
		str1='	<a href="/cmcp/view/meet/meet_content_report_show.html?id='+id+'" target="_blank"><div  name="meetContent" rows="1" style="width:100%;font-size:20px;font-weight:bold;color:black "  type="text" disabled idValue="'+(theSize+1)+'"> </div></a>';
	}
	var str = $('<tr name="meetContentTr" style="padding:0x">'+
			'<td style="width:3%;font-size:15px; padding-top:5px;vertical-align: top;"><a style="color:red"  name="removeTr">X</a></td>'+
			' <td   style="width:97%; padding-top:5px;">'+
			str1+
//			'	<div  name="meetContent" rows="1" style="width:100%;font-size:20px;font-weight:bold;color:black"  type="text" disabled idValue="'+(theSize+1)+'"> </div>'+
			' </td>'+
			'</tr>'+
			'<tr name="meetContentMarkTr">'+
			'<td  colspan="2" style="padding-top:10px">'+
			'<table width="100%" border="0" style="margin:3px">'+
			'<tr>'+
			' <td style="width:13.7%;"></td>'+
			' <td style="width:86%;">'+
			'	<input type="hidden" name="contentId" value="'+id+'"/><textarea style="width:95%; height:60px; overflow:auto;" name="contentMark" cols="" rows="" onkeyup="autoSize(this)" >'+mark+'</textarea>'+
			'</td>'+
			'</tr>'+
			'</table>'+
			'</td>'+
			'</tr>'+
			'<tr '+(attachemntStr.length==0?"class='hidden'":"")+'>'+
			'<td  colspan="2" style="padding-top:10px">'+
			'<table width="100%" border="0" style="margin:3px">'+
			'<tr>'+
			' <td style="width:13.7%;color:black;">&nbsp;&nbsp;&nbsp;&nbsp;附件：</td>'+
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
//	var dto = {
//			id:$("#id").val(),
//			titleType:$("#titleType").val(),
//			status:'0002',
//			place:$("#place").val(),
//			host:$("#host").val(),
//			attend:$("#attend").val(),
//			attendObserver:$("#attendObserver").val(),
//			personCount:$("#personCount").val(),
//			meetCount:$("#meetCount").val(),
//			workFlowDTO:{
//				isNeedInvoke:'0002'
//			}
//	}
//	
//	return dto;
	
	var dto = {
			id:$("#id").val(),
	}

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
	dto.meetPlanContentDTOs = meetPlanContentObj;
	dto.flowType="mszApproval";
	return dto;
}

function getCompleteData() {
	var dto= {
		id:id,
		status:"0003"
	}
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
	dto.meetPlanContentDTOs = meetPlanContentObj;
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
	var btnSource = "app";
	if(getURLParam("fromPage") =="pc") {
		btnSource="pc";
	}
	if(type=="0004") {
		if(data.operaterId == "flow7" ) {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=4&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;		
		} else {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;				
		}
		
	}else if(type=="0005") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
	} else {
		if(data.operaterId == "flow3") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=4&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;
		} else if(data.operaterId == "flow4" || data.operaterId == "flow8" ||data.operaterId == "flow7") {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?flag=7&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;
		} else {
			var roleName = data.assignRoleName;
			window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
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
		if((data == "flow4")||(data == "flow5")||(data == "flow6") ||(data=="next1")) {
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
		if((data == "flow4")||(data == "flow5")||(data == "flow6") ||(data=="next1")) {
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