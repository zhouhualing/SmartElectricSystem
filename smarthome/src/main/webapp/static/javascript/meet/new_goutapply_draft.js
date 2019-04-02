var type = "";
$("#applyDate").html(new Date().format('yyyy年M月d日'));
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#applyUnit").html(data.orgName);
		/*$("#applyUserName").val(data.userName);
		$("#applyOrgName").val(data.orgName);
		$("#applyPhoneNum").val(data.phoneNumber);*/
		$("#contactsName").val(data.userName);
		$("#contactsPhoneNum").val(data.phoneNumber);
		$("#contactsMBNum").val(data.mobilePhone);
		var orgCode = data.orgCode;
		$("#orgCode").val(orgCode);//根据orgCode判断启用哪些流程按钮
		var configList = "002,003,011,012,013,014,015,016,017";
		if(orgCode == '004'){
			type = "0001";
			creatBtn(1);
		}else if(configList.indexOf(orgCode)>=0){
			type = "0002";
			creatBtn(1);
		}else{
			type = "0003";
			creatBtn(2);
		}
		$("#userId").val(data.userId);
		$("#userCode").val(data.userCode);
		$("#userName").val(data.userName);
	});
});

function creatBtn(n){
	if(n==1){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'sz\')" >市长批示</button></span>');
	}else{
		//  msz
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'fsz\')" >副市长批示</button></span>');
	}
}

function sendToNext(step){
	if(verification()){
		if(step == 'fsz'){
			doRequest("/goutapply/getRoleCodes",{type:"0003"},function(data){
				if(data.result){
					var data = data.data;
					var roleCodes = data.fszStep;
					var obj = {
						    title:'选择处理角色',
						    height:"320px",
						    width:"750px",
						    url:'../users/follow_role_dialog.html?operaterId=&isShowDate=0002&selectType=0003&markType=0002&ignoreRole=&checkType=checkbox&roleCodes='+roleCodes,
						    myCallBack:"setRoleCode"
						}
						new jqueryDialog(obj);
				}
			});
		}else{
			var dto = getAddData();
			dto.step = step;
			doJsonRequest("/goutapply/addGoutApply",dto,function(data){
				if(data.result) {
					if(data.data == 'success'){
						//跳转到成功页面
						window.location.href="../tododocs/docs_down.html?flag=7&fromPage=0001&btnSource=app";
					}else{
						$.alert("提交失败。");
					}
				} else {
					$.alert("提交失败。");
				}
			},{showWaiting:true});
		}
	}
}

//校验
function verification(){
	if($("#reason").val()==''){
		$.alert("请输入外出事由。");
		return false;
	}
	if(($("#leaveDate").val()==''||$("#returnDate").val()=='') || new Date($("#leaveDate").val())-new Date($("#returnDate").val())>0){
		$.alert("请输入正确的外出时间和返回时间。");
		return false;
	}
	if($("#applyUserName").val()==''){
		$.alert("请输入姓名。");
		return false;
	}
	return true;
}

function setRoleCode(da){
	var roleCode = da.roleCode;
	var dto = getAddData();
	dto.step = "fsz";
	dto.roleCode = roleCode[0];
	doJsonRequest("/goutapply/addGoutApply",dto,function(dat){
		if(dat.result) {
			if(dat.data == 'success'){
				//跳转到成功页面
				window.location.href="../tododocs/docs_down.html?flag=7&fromPage=0001&btnSource=app";
			}else{
				$.alert("提交失败。");
			}
		} else {
			$.alert("提交失败。");
		}
	},{showWaiting:true});
}

function getAddData(){
		var dto = {
			status:'0002',
			type:type,
			applyUnit:$("#applyUnit").html(),
			applyDate:new Date(),
			applyUserName:$("#applyUserName").val(),
			applyOrgName:$("#applyOrgName").val(),
			applyPhoneNum:$("#applyPhoneNum").val(),
			reason:$("#reason").val(),
			leaveDate:$("#leaveDate").val(),
			returnDate:$("#returnDate").val(),
			goToPlace:$("#goToPlace").val(),
			proxyNameOrg:$("#proxyNameOrg").val(),
			proxyPhoneNum:$("#proxyPhoneNum").val(),
			remark:$("#remark").val(),
			contactsName:$("#contactsName").val(),
			contactsPhoneNum:$("#contactsPhoneNum").val(),
			contactsMBNum:$("#contactsMBNum").val(),
			userId:$("#userId").val(),
			userCode:$("#userCode").val(),
			userName:$("#userName").val(),
		};
		return dto;
}

$("#leaveDate").datepicker({
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
});

$("#returnDate").datepicker({
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
});
