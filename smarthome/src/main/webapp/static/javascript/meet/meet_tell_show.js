var id = getURLParam("id");
var taskId = getURLParam("taskId");

$("#businessKey").val(id);
$(function(){
	$("#year").html(new Date().getTheYear())
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
				$("#org").html(obj.org);
				$("#contactPerson").html(obj.contactPerson);
				$("#contactNumber").html(obj.contactNumber);
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
		userStr = userStr+ "<label><div style='color:black;font-weight:normal'><input type='hidden' name='userCode' value='"+data.userCode[i]+"'/>"+data.userName[i]+" <a style='color:red' name='removeA' class='hidden'>x</a></div></label>&nbsp;&nbsp;"
	}
	$("#attendsSelectedDiv").html($("#attendsSelectedDiv").html()+userStr);
	$("[name='removeA']",$("#attendsSelectedDiv")).on("click",function(){
		$(this).parent().remove();
	})
}



function getData() {
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
			status:'0002'
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
	if(data.operaterId == "flow3") {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=3&roleName="+roleName+"&fromPage=0002&btnSource=app";
	} else {
		var roleName = data.assignRoleName;
		window.location.href="meetSuccess.html?flag=2&roleName="+roleName+"&fromPage=0002&btnSource=app";	
	}

}

