//打开时默认显示正文页:
var fromList = getURLParam("fromList"); //督办单来源表：s:督办单汇总表    d:动态期刊
var id = getURLParam("id"); //当前事项id
var superId = getURLParam("superId"); //在督办汇总单中点击再次生成督办单时的id
var issue = getURLParam("issue");//期号
var fromPage = getURLParam("fromPage");
var dynaworkId = getURLParam("dynaworkId");//当前动态内容id
var noticenterId = getURLParam("noticenterId");//当前动态期刊所属通知id
$("#issue").html(issue);
var date = new Date();
$("#superDate").html(date.format('yyyy年M月d日'));
$("#title").val("批示事项通知");
$(function(){
	//初始化流程按钮
	wf_getOperator("superviseSZ",function(data){
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
	});
	doJsonRequest("/code/getCode","0018",function(data){
		if(data.result) {
			$("#superCode").html(data.data);
		} else {
			$.alert("获取编码出错。");
		}
	});
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
	}else if(superId!=null){
		var dto = {
				id:superId
		}
		doJsonRequest("/super/getSuperVise",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#content").val(data.content);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
});

function getAddData(){
	var dto = {
			superId:superId,
			fromList:fromList,
			issue:$("#issue").html(),
			superCode:$("#superCode").html(),
			/*completeDate:$("#completeDate").val(),*/
			superDate:date,
			content:$("#content").val(),
			status:"0002",
			superType:"0001",//市长督办
			title:$("#title").val(),
			noticenterId:noticenterId,
			dynaworkId:$("#id").val(),
			flag:0
	}
	dto.szOpinion = $("#szOpinion").val() + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	return dto;
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource=pc";
}

//校验
function wf_beforeValid(){
	window.scrollTo(0,0);
	/*if($("#completeDate").val()==''){
		$.alert("请输入截止日期。");
		return false;
	}*/
	if($("#szOpinion").val()==''){
		$.alert("请输入领导意见。");
		return false;
	}
	return true;
}

/*$("#completeDate").datepicker({
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
	buttonText:"选择",
	buttonClass:"btn_click"
});*/

/*function wf_getDate(data) {
	return new Date($("#completeDate").val());
}*/