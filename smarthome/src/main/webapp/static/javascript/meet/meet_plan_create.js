var id = getURLParam("id");
var planIds = [];

var mainRoles = ["037"];
var fszRoles = ["007","067","068","069","070","071","072"];
getCurrenUserInfo(function(data){
	$("#org").html(data.orgName);
	$("#contactPerson").html(data.userName);
	$("#contactNumber").html(data.phoneNumber);
	var roleArr = data.roleCodes.split(" ");
	for(var i=0; i <roleArr.length; i++) {
		if($("option[value='0001']").length <= 0) {
			if($.inArray(roleArr[i],mainRoles)!= -1) {
				$("#titleType").append('<option value="0001">常务会议</option>');
				$("#titleType").append('<option value="0002">党组会议</option>');
				$("#titleType").append('<option value="0003">全体会议</option>');
				$("#titleType").append('<option value="0004">市长办公会议</option>');
			}		
			$("#titleType").trigger("change");
		}
		
		if($("option[value='0002']").length <= 0) {
			if($.inArray(roleArr[i],fszRoles)!= -1) {
				$("#titleType").append('<option value="0005">副市长办公会议</option>');
			}
			$("#titleType").trigger("change");
		}	
	}
})


$(function(){
	//初始化流程按钮
	wf_getOperator("meetPlan",function(data){
	});
	if(id != null) {
		var dto = {
				id:id
			}
		doJsonRequest("/meet/meetplan/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
				$("#id").val(obj.id);
				$("#titleType").val(obj.titleType);
				$("#titleType").trigger("change")
				
				$("#place").val(obj.place);
				$("#host").val(obj.host);
				$("#attend").val(obj.attend).trigger("keyup");
				$("#attend1").val(obj.attend).trigger("keyup");
				$("#meetContent").val(obj.meetContent);
				$("#attendObserver").val(obj.attendObserver).trigger("keyup");
				$("#attendObserver1").val(obj.attendObserver).trigger("keyup");
				$("#personCount").val(obj.personCount);
				$("#personCount1").val(obj.personCount);
				$("#personCount2").val(obj.personCount1);
				$("#personCount3").val(obj.personCount2);
				$("#meetCount").val(obj.meetCount);
				
				
				if(obj.planDate != null) {
					$("#year").val(new Date(obj.planDate).getTheYear());	
					$("#month").val(new Date(obj.planDate).getTheMonth());
					$("#day").val(new Date(obj.planDate).getTheDate());
//					$("#hour").val(new Date(obj.planDate).getTheHour());
//					$("#minute").val(new Date(obj.planDate).getTheMinute());
				}
				$("#time").val(obj.time);
				for(var i=0; i <obj.meetPlanContentDTOs.length; i++) {
					planIds.push(obj.meetPlanContentDTOs[i].meetContentId);
					addContentFun(obj.meetPlanContentDTOs[i].files,obj.meetPlanContentDTOs[i].meetContent,obj.meetPlanContentDTOs[i].meetContentId,obj.meetPlanContentDTOs[i].contentMark,meetContentId,obj.meetPlanContentDTOs[i].reportType,obj.meetPlanContentDTOs[i].meetContentDocDTOs);
				}
			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})

/**
 * change title
 */
function changeTitle() {
	var str = "";
	var currentTitleType = $("#titleType").val();
	if(currentTitleType == "0003"){
		$(".hidenLabel").removeClass("hidden");
		$(".hidenLabel1").addClass("hidden");
		$(".changeTd").each(function(){
			$(this).children("td").eq(0).attr("style","width:19%;font-size:20px;color:black;font-weight: bold;");
			$(this).children("td").eq(1).attr("style","width:81%;");
		})
	} else {
		$(".hidenLabel").addClass("hidden");
		$(".hidenLabel1").removeClass("hidden");
		$(".changeTd").each(function(){
			$(this).children("td").eq(0).attr("style","width:12%;font-size:20px;color:black;font-weight: bold;");
			$(this).children("td").eq(1).attr("style","width:88%;");
		})
	}
	if(currentTitleType =="0001") {
		//初始化流程按钮
		wf_getOperator("meetPlan",function(data){
		});
		 $("#titleType").attr("style","color:red;font-weight:bold;width:125px");
		str = "市&nbsp;政&nbsp;府&nbsp;常&nbsp;务&nbsp;会&nbsp;议";
	} else if(currentTitleType == "0002"){
		//初始化流程按钮
		wf_getOperator("meetPlan",function(data){
		});
		 $("#titleType").attr("style","color:red;font-weight:bold;width:125px"); 
		str = "市&nbsp;政&nbsp;府&nbsp;党&nbsp;组&nbsp;会&nbsp;议";
	} else if(currentTitleType == "0003"){
		//初始化流程按钮
		wf_getOperator("meetPlan",function(data){
		});
		 $("#titleType").attr("style","color:red;font-weight:bold;width:125px"); 
		str = "大&nbsp;同&nbsp;市&nbsp;人&nbsp;民&nbsp;政&nbsp;府&nbsp;全&nbsp;体&nbsp;会&nbsp;议";
	} else if(currentTitleType == "0004") {
		//初始化流程按钮
		wf_getOperator("meetPlanSz",function(data){
		});
		str = "市&nbsp;长&nbsp;办&nbsp;公&nbsp;会&nbsp;议";
        $("#titleType").attr("style","color:red;font-weight:bold;width:auto");
    } else if(currentTitleType == "0005") {
		//初始化流程按钮
		wf_getOperator("meetPlanFsz",function(data){
		});
		str = "副&nbsp;市&nbsp;长&nbsp;办&nbsp;公&nbsp;会&nbsp;议";
		 $("#titleType").attr("style","color:red;font-weight:bold;width:auto");
	}
	$("#titleType1").html(str);
}

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
			addContentFun(attachmentFiles,obj.meetContentDetail,obj.id,'',obj.reportType,data.data.meetContentDocDTOs);
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
    var nowSystemFiles = systemFiles;
	var attachemntStr = "";
	for(var i=0; i<attachmentFiles.length; i++) {
		attachemntStr = attachemntStr+ "<label><div style='color:blue'><input type='hidden' name='attachmentId' value='"+attachmentFiles[i].id+"'/><a style='color:blue' href='"+attachmentFiles[i].url+"'>"+attachmentFiles[i].name+"</a> <a style='color:red' class='noPrint' name='removeA'>x</a></div></label>&nbsp;&nbsp;"
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
        attachemntStr = attachemntStr+ "<label><div style='color:blue'><input type='hidden' name='contentDocId' value='"+nowSystemFiles[i].id+"'/><a style='color:blue' href='"+href+"' target='_blank'>"+nowSystemFiles[i].businessTitle+"</a> <a style='color:red' class='noPrint' name='removeA'>x</a></div></label>&nbsp;&nbsp;"
    }
	var str1 = "";
	if(type=="0002") {
		str1='	<a onclick="doAlert()"><div  name="meetContent" rows="1" style="width:100%;font-size:20px;font-weight:bold;color:black "  type="text" disabled idValue="'+(theSize+1)+'"> </div></a>';
	} else {
		str1='	<a href="/cmcp/view/meet/meet_content_report_show.html?id='+id+'" target="_blank"><div  name="meetContent" rows="1" style="width:100%;font-size:20px;font-weight:bold;color:black "  type="text" disabled idValue="'+(theSize+1)+'"> </div></a>';
	}
	var theSize = $("[name='meetContentTr']").size();
	var str = $('<tr name="meetContentTr" style="padding:0x">'+
		'<td style="width:3%;font-size:15px; padding-top:5px;vertical-align: top;"><a style="color:red" class="noPrint" name="removeTr">X</a></td>'+
		' <td   style="width:97%; padding-top:5px;">'+
		str1+
		' </td>'+
		'</tr>'+
		'<tr name="meetContentMarkTr">'+
		'<td  colspan="2" style="padding-top:10px">'+
		'<table width="100%" border="0" style="margin:3px">'+
		'<tr>'+
		' <td style="width:13.7%;"></td>'+
		' <td style="width:86%;">'+
		'	<input type="hidden" name="contentId" value="'+id+'"/><span><textarea style="width:95%; height:60px; overflow:auto;" name="contentMark" cols="" rows="" onkeyup="autoSize(this)">'+mark+'</textarea></span>'+
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
	if(meetContent != null) {
		if(meetContent.startWith("<p>")) {
			$("[name='meetContent']",str).html("<p>"+doLowwerToUpper(theSize+1)+"、"+meetContent.substring(3))
		} else {
			$("[name='meetContent']",str).html(doLowwerToUpper(theSize+1)+"、"+meetContent)
		}
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
	var dateStr = $("#year").val()+"-"+(($("#month").val().length==1)?("0"+$("#month").val()):$("#month").val())+"-"+(($("#day").val().length==1)?("0"+$("#day").val()):$("#day").val());
//	var dateStr = $("#year").val()+"-"+(($("#month").val().length==1)?("0"+$("#month").val()):$("#month").val())+"-"+(($("#day").val().length==1)?("0"+$("#day").val()):$("#day").val())+" "+(($("#hour").val().length==1)?("0"+$("#hour").val()):$("#hour").val())+":"+(($("#minute").val().length==1)?("0"+$("#minute").val()):$("#minute").val());
	if(dateStr.length == 2) {
		dateStr = "";
	}
	var dto = {
			id:$("#id").val(),
			titleType:$("#titleType").val(),
			status:'0002',
			place:$("#place").val(),
			host:$("#host").val(),
			planDate:dateStr,
			time:$("#time").val()
	}
	if($("#titleType").val() == "0003") {
		dto.meetContent = $("#meetContent").val();
		dto.attend = $("#attend1").val();
		dto.attendObserver = $("#attendObserver1").val();
		dto.personCount = $("#personCount1").val();
		dto.personCount1 = $("#personCount2").val();
		dto.personCount2 = $("#personCount3").val();
	} else {
		var meetPlanContentObj = [];
		$("[name='meetContentMarkTr']").each(function() {
			var obj = {};
			obj.meetContentId = $("[name='contentId']",this).val();
			obj.contentMark = $("[name='contentMark']", this).val();
			var attIds = [];
            var contentDocIds = [];
			$("[name='attachmentId']",$(this).next("tr")).each(function(){
				attIds.push($(this).val())
			})
            $("[name='contentDocId']",$(this).next("tr")).each(function(){
                contentDocIds.push($(this).val())
            })
			obj.attachmentIds = attIds.join(",");
            obj.contentDocIds = contentDocIds.join(",");
			meetPlanContentObj.push(obj);
		})
		dto.meetPlanContentDTOs = meetPlanContentObj;
		dto.meetCount = $("#meetCount").val();
		dto.attend = $("#attend").val();
		dto.personCount =$("#personCount").val();
		dto.attendObserver = $("#attendObserver").val();
	}


	
	return dto;
}

function tempSubmit() {
	var flag =true;
	$("#year,#month,#day").each(function(){
		if($(this).val().length==0) {
			$.alert("请输入正确时间。");
			flag = false;
			return false;
		}
	})
	if(!flag) {
		return false;
	}
   var dto = getData();
   dto.status="0001";
	doJsonRequest("/meet/meetplan/doSave",dto, function(data){
		if(data.result) {
			$.alert("暂存成功。");
			$("#id").val(data.data.id);
		} else {
			$.alert("保存失败。");
		}
	})
}

function validYear(_this) {

	if($(_this).val().length !=4) {
		$.alert("请输入正确的年份。");
		$(_this).val("");
	}
}
function validMonth(_this) {
	if(parseInt($(_this).val()) >12) {
		$.alert("请输入正确的月份。");
		$(_this).val("");
	}
}
function validDay(_this) {

	if(parseInt($(_this).val()) >31) {
		$.alert("请输入正确的日子。");
		$(_this).val("");
	}
}
function validHour(_this) {

	if(parseInt($(_this).val()) >23) {
		$.alert("请输入正确的小时。");
		$(_this).val("");
	}
}
function validMinute(_this) {

	if(parseInt($(_this).val()) >59) {
		$.alert("请输入正确的分钟。");
		$(_this).val("");
	}
}
function wf_beforeValid() {
	var flag = true;
	$("#year,#month,#day").each(function(){
		if($(this).val().length==0) {
			flag = false;
			$.alert("请输入正确时间。");
			return false;
		}
	})
	
	if(!flag) {
		return flag;
	}
	
	if($("#titleType").val().length ==0) {
		flag = false;
		$.alert("请选择会议类型。");
		return flag;
	}
	
	if($("#place").val().length ==0) {
		flag = false;
		$.alert("请输入会议地点。");
		return flag;
	}
	
	
	if($("#titleType").val() == "0003") {
		if($("#attend1").val().length ==0) {
			flag = false;
			$.alert("请输入出席人员。");
			return flag;
		}
		if($("#personCount1").val().length ==0) {
			flag = false;
			$.alert("请输入参会人数。");
			return flag;
		}		
		if($("#personCount2").val().length ==0) {
			flag = false;
			$.alert("请输入出席人数。");
			return flag;
		}
		if($("#personCount2").val().length ==0) {
			flag = false;
			$.alert("请输入列席人数。");
			return flag;
		}
	} else {
		if($("#attend").val().length ==0) {
			flag = false;
			$.alert("请输入出席人员。");
			return flag;
		}
		if($("#meetCount").val().length ==0) {
			flag = false;
			$.alert("请输入会议次数。");
			return flag;
		}
		if($("#personCount").val().length ==0) {
			flag = false;
			$.alert("请输入会议人数。");
			return flag;
		}		
	}

	return true;
	
}

function createDoc() {
	if($("#id").val().length == 0) {
		$.alert("请先创建会议方案");
		return false;
	}
	var dto =  {
			id:$("#id").val()
	}
	doJsonRequest("/meet/meetplan/createMeetDoc",dto,function(data){
		if(data.result) {
			$.alert("议程/议题单创建成功。");
		} else {
			$.alert("创建议题单出错。")
		}
	},{showWaiting:true})
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="meetSuccess.html?roleName="+roleName+"&fromPage=0002&btnSource=app";
}