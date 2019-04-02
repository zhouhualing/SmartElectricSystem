var id = getURLParam("id");
var taskId = getURLParam("taskId");
$("#businessKey").val(id);
var userTask = "";
$(function(){
	$("#year").html(new Date().getTheYear())
	//初始化流程按钮
	var dto = {
		taskId:taskId
	}
	wf_getOperator(dto,function(data){
		if(data.userTask == "usertask1") {
			$("#hiddenBtn1").removeClass("hidden");
			userTask = data.userTask;
		}
	});
	if(id != null) {
		var dto = {
				id:id
			}
		doJsonRequest("/meet/meettell/getInfo",dto,function(data){
			if(data.result) {
				var obj = data.data;
				$("#id").val(obj.id);
				$("#year").val(obj.year);
				$("#number").html(obj.number);
				$("#meetTitle").val(obj.meetTitle)
				$("#meetTime").val(new Date(obj.meetTime).format("yyyy-MM-dd hh:mm"))
				$("#place").val(obj.place)
				$("#host").val(obj.host)
				$("#meetContent").val(obj.meetContent)
				
				$("#attend").val(obj.attend)
				$("#attendRequire").val(obj.attendRequire)
				$("#meetMark").val(obj.meetMark)
				
				$("#org").html(obj.org)
				$("#contactPerson").html(obj.contactPerson)
				$("#contactNumber").html(obj.contactNumber)
				doQueryWF("reportInfo","approvalDiv");
				var userCodeArr = [];
				var userNameArr = [];
				for(var i=0; i<obj.userDTOs.length; i++) {
					userCodeArr.push(obj.userDTOs[i].userCode);
					userNameArr.push(obj.userDTOs[i].userName);
				}
				var obj = {
						userCode:userCodeArr,
						userName:userNameArr
				}
				initUserInfo(obj);

			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})



function setMeetTime() {
	$("#meetTime").val($("#hiddenTime").val())
}

function doSearchUser(_this) {
	var obj = {
	    title:'选择参会人员',
	    height:"350px",
	    width:"750px",
	    url:'../users/follow_user_dialog1.html?checkType=multi',
	    myCallBack:initUserInfo,
	    fun:true,
	    srcData:_this
	}
	new jqueryDialog(obj);
}

function initUserInfo(data,str){
	var codes = [];
	var names = [];
	if(data.userCode){
		codes = data.userCode;
		names = data.userName;
	} else {
		codes = data.roleCode;
		names = data.roleName;
	}
	if(str) {
		var userStr = "";
		for(var i=0; i<codes.length; i++) {
			userStr = userStr+ "<label><div style='color:black;font-weight:normal'><input type='hidden' name='userCode' value='"+codes[i]+"'/>"+((names[i].indexOf("-")==-1)?names[i]:(names[i].substring(0,names[i].indexOf("-"))))+" <a style='color:red' name='removeA'>x</a></div></label>&nbsp;&nbsp;"
		}
		$("#attendsSelectedDiv1").html($("#attendsSelectedDiv1").html()+userStr);
		$("[name='removeA']",$("#attendsSelectedDiv1")).on("click",function(){
			$(this).parent().remove();
		})	
	} else {
		var userStr = "";
		for(var i=0; i<codes.length; i++) {
			userStr = userStr+ "<label><div style='color:black;font-weight:normal'><input type='hidden' name='userCode' value='"+codes[i]+"'/>"+((names[i].indexOf("-")==-1)?names[i]:(names[i].substring(0,names[i].indexOf("-"))))+" <a style='color:red' name='removeA' class='hidden'>x</a></div></label>&nbsp;&nbsp;"
		}
		$("#attendsSelectedDiv").html($("#attendsSelectedDiv").html()+userStr);
		$("[name='removeA']",$("#attendsSelectedDiv")).on("click",function(){
			$(this).parent().remove();
		})	
	}

}



function getData() {
	var userDTOs = []
	$("[name='userCode']",$("#attendsSelectedDiv1")).each(function() {
		var obj = {
				userCode:$(this).val()
		}
		userDTOs.push(obj);
	})
	
	var dto = {
			id:$("#id").val(),
			userDTOs:userDTOs,
			type:'0002'
	}
	
	return dto;
}

function tempSubmit() {
	var userDTOs = []
	$("[name='userCode']",$("#attendsSelectedDiv")).each(function() {
		var obj = {
				userCode:$(this).val()
		}
		userDTOs.push(obj);
	})
	
	var dto = {
			id:$("#id").val(),
			year:$("#year").val(),
			number:$("#number").val(),
			meetTitle:$("#meetTitle").val(),
			meetTime:$("#meetTime").val(),
			place:$("#place").val(),
			host:$("#host").val(),
			meetContent:$("#meetContent").val(),
			attend:$("#attend").val(),
			attendRequire:$("#attendRequire").val(),
			meetMark:$("#meetMark").val(),
			userDTOs:userDTOs,
			status:'0001'
	}
	
	doJsonRequest("/meet/meettell/doSave",dto, function(data){
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
	if(getURLParam("fromPage")=="pc") {
		btnSource = "pc";
	}
	if(data.operaterId == "flow3") {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=3&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;
	} else {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=2&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;	
	}

}

function wf_getRoleCodes() {
	var userDTOs = [];
	$("[name='userCode']",$("#attendsSelectedDiv1")).each(function() {
		userDTOs.push($(this).val());
	})
	var userCodes = userDTOs.join(",");
	return userCodes;
}

function wf_beforeValid(data) {
	var flag = true;
	if(userTask == "usertask1") {
		if(data == "flow2") {
			if($("[name='userCode']",$("#attendsSelectedDiv1")).length<=0) {
				flag = false;
				$.alert("请至少选择一个通知的参会人员。");
				return flag;
			}		
		}
	}
	return true;
	
}

