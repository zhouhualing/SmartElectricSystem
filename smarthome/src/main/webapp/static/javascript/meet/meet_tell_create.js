var id = getURLParam("id");

if(id == null) {
getCurrenUserInfo(function(data){
	$("#org").html(data.orgName);
	$("#contactPerson").val(data.userName);
	$("#contactNumber").val(data.phoneNumber);
})
}
$(function(){
	$("#year").html(new Date().getTheYear())
	//初始化流程按钮
	wf_getOperator("meetTell",function(data){
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
				$("#number").val(obj.number);
				$("#meetTitle").val(obj.meetTitle)
				$("#meetTime").val(new Date(obj.meetTime).format("yyyy-MM-dd hh:mm"))
				$("#place").val(obj.place)
				$("#host").val(obj.host)
				$("#meetContent").val(obj.meetContent)
				
				$("#attend").val(obj.attend)
				$("#attendRequire").val(obj.attendRequire)
				$("#meetMark").val(obj.meetMark)
				$("#org").html(obj.org)
				$("#contactPerson").val(obj.contactPerson)
				$("#contactNumber").val(obj.contactNumber)
				var userCodeArr = [];
				var userNameArr = [];
				for(var i=0; i<obj.userDTOs.length; i++) {
					userCodeArr.push(obj.userDTOs[i].userCode);
					userNameArr.push(obj.userDTOs[i].userName);
				}
				var obj = {
						roleCode:userCodeArr,
						roleName:userNameArr
				}
				initUserInfo(obj);

			} else {
				$.alert("获取信息失败。");
			}
		})
	}
})


$("#hiddenTime").datetimepicker({
	showSecond: false, //显示秒
	timeFormat: 'HH:mm',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-mm-dd",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间',
	buttonText:"选择会议时间",
	buttonClass:"btn_click"
});


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

function initUserInfo(data){
	var userStr = "";
	for(var i=0; i<data.roleCode.length; i++) {
		userStr = userStr+ "<label><div style='color:black;font-weight:normal'><input type='hidden' name='userCode' value='"+data.roleCode[i]+"'/>"+((data.roleName[i].indexOf("-")==-1)?data.roleName[i]:(data.roleName[i].substring(0,data.roleName[i].indexOf("-"))))+" <a style='color:red' name='removeA'>x</a></div>&nbsp;&nbsp;</label>"
	}
	$("#attendsSelectedDiv").html($("#attendsSelectedDiv").html()+userStr);
	$("[name='removeA']",$("#attendsSelectedDiv")).on("click",function(){
		$(this).parent().parent().remove();
	})
	
	$("#attend").focus();
}



function getData() {
	var userDTOs = []
	var userNames = [];
	var userCodes = [];
	$("[name='userCode']",$("#attendsSelectedDiv")).each(function() {
		var obj = {
				userCode:$(this).val()
		}
		userDTOs.push(obj);
		userCodes.push($(this).val());
	})
	$("[name='userName']",$("#attendsSelectedDiv")).each(function() {

		userNames.push($(this).val());
	})
	var dto = {
			id:$("#id").val(),
			year:$("#year").html(),
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
			status:'0002',
			org:$("#org").html(),
			contactPerson:$("#contactPerson").val(),
			contactNumber:$("#contactNumber").val()
	}
	
	return dto;
}

function tempSubmit() {
	var dto = getData();
	dto.status = "0001";
	
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
	var roleName = data.assignRoleName;
	window.location.href="meetSuccess.html?flag=8&roleName="+roleName+"&fromPage=0002&btnSource=app";
}

function wf_beforeValid() {
	var flag = true;
	
	if($("#number").val().length ==0) {
		flag = false;
		$.alert("请输入号。");
		return flag;
	}
	
	if($("#meetTitle").val().length ==0) {
		flag = false;
		$.alert("请输入会议议题。");
		return flag;
	}
	if($("#meetTime").val().length ==0) {
		flag = false;
		$.alert("请选择会议时间。");
		return flag;
	}
	if($("#place").val().length ==0) {
		flag = false;
		$.alert("请输入会议地点。");
		return flag;
	}
	if($("#host").val().length ==0) {
		flag = false;
		$.alert("请输入主持人。");
		return flag;
	}
	if($("#meetContent").val().length ==0) {
		flag = false;
		$.alert("请输入议题。");
		return flag;
	}
	if($("[name='userCode']").length<=0) {
		flag = false;
		$.alert("请至少选择一个参会人员。");
		return flag;
	}
	if($("#attendRequire").val().length ==0) {
		flag = false;
		$.alert("请输入参会要求。");
		return flag;
	}
	return true;
	
}

function wf_getRoleCodes() {
	var userDTOs = [];
	$("[name='userCode']",$("#attendsSelectedDiv")).each(function() {
		userDTOs.push($(this).val());
	})
	var userCodes = userDTOs.join(",");
	return userCodes;
}
//function wf_getUserCodes() {
//	var userDTOs = [];
//	$("[name='userCode']",$("#attendsSelectedDiv")).each(function() {
//		userDTOs.push($(this).val());
//	})
//	var userCodes = userDTOs.join(",");
//	return userCodes;
//}