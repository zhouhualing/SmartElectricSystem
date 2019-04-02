//打开时默认显示正文页:
var id = getURLParam("id"); //当前事项id
var issue = getURLParam("issue");
/*var isMayor = getURLParam("isMayor");*/
var fromPage = getURLParam("fromPage");
var noticenterId = getURLParam("noticenterId");//当前动态期刊所属通知id
$("#issue").html(issue);
var date = new Date();
$("#superDate").html(date.format('yyyy年M月d日'));
$("#title").val("批示事项通知");
var fromList = getURLParam("fromList");


/*var noticenterId_MB = getURLParam("nId");
var dynaworkId_MB = getURLParam("dId");//当前事项id
var opinion_MB = getURLParam("op");
var completeDate_MB = getURLParam("cD");
var issue_MB = getURLParam("ie");
var superCode_MB = getURLParam("superCode");*/
//"nId="+noticenterId+"&dId="+dynaworkId+"&op="+opinion+"&cD="+completeDate+"&ie="+issue;

/*var superType = "";*/
$(function(){
	//初始化流程按钮
	wf_getOperator("superviseFSZ",function(data){
		/*if(noticenterId_MB!=null && dynaworkId_MB!=null){
			$("#flow10 button").trigger('click');
		}*/
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
	});
	/*if(superCode_MB!=null){
		$("#superCode").html(superCode_MB);
	}else{*/
		doJsonRequest("/code/getCode","0018",function(data){
			if(data.result) {
				$("#superCode").html(data.data);
			} else {
				$.alert("获取编码出错。");
			}
		});
	/*}*/
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/dynawork/getDynaWork",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#content").val(data.content);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}/*else if(dynaworkId_MB!=null){
		var dto = {
				id:dynaworkId_MB
		}
		doJsonRequest("/dynawork/getDynaWork",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#content").val(data.content);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}*/
	
});

function getAddData(){
	/*if(noticenterId_MB!=null && dynaworkId_MB!=null){
		var dto = {
				issue:issue_MB,
				superCode:$("#superCode").html(),
				fromList:fromList,
				completeDate:new Date(completeDate),
				superDate:date,
				content:$("#content").val(),
				status:"0002",
				superType:"0002",//副市长督办
				title:$("#title").val(),
				noticenterId:noticenterId_MB,
				dynaworkId:dynaworkId_MB,
				fszOpinion:opinion_MB + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]",
				flag:0
		}
	}else{*/
		var dto = {
				issue:$("#issue").html(),
				superCode:$("#superCode").html(),
				/*completeDate:$("#completeDate").val(),*/
				fromList:fromList,
				superDate:date,
				content:$("#content").val(),
				status:"0002",
				superType:"0002",//副市长督办
				title:$("#title").val(),
				noticenterId:noticenterId,
				dynaworkId:$("#id").val(),
				fszOpinion:$("#fszOpinion").val() + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]",
				flag:0
		}
	/*}*/
	
	//dto.fszOpinion = $("#fszOpinion").val() + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	/*if(superType == '0001'){
		dto.szOpinion = $("#szOpinion").val() + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	}else{
		
	}*/
	return dto;
}

function goSuccess(data) {
	/*if(noticenterId_MB!=null && dynaworkId_MB!=null){
		alert('success');
	}else{*/
		var roleName = data.assignRoleName;
		window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource=pc";
	/*}*/
}

//校验
function wf_beforeValid(){
	window.scrollTo(0,0);
	/*if(noticenterId_MB!=null && dynaworkId_MB!=null){
		return true;
	}else{*/
		/*if($("#completeDate").val()==''){
			$.alert("请输入截止日期。");
			return false;
		}*/
		if($("#fszOpinion").val()==''){
			$.alert("请输入领导意见。");
			return false;
		}
	/*}*/
	return true;
}

/*$("#completeDate").datepicker({
	showSecond: false, //显示秒
	timeFormat: 'HH:mm',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-m-d",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间',
	buttonText:"选择",
	buttonClass:"btn_click"
});*/

/*function wf_getDate(data) {
	return new Date($("#completeDate").val());
}*/