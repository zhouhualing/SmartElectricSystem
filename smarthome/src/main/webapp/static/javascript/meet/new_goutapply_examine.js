var fromPage = getURLParam("fromPage");
var type = getURLParam("type");
var id = getURLParam("id");
var targetStep = getURLParam("targetStep");
var showBack = getURLParam("showBack");
if(showBack == '001'){
	$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="showToBack()" >返回</button></span>');
}
$("#type").val(type);
$("#id").val(id);
var btnSource = "app";
if(fromPage == "pc"){
	btnSource = fromPage;
}
if(type == '0001'){
	if(targetStep == 'start'){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToEnd(\'0005\')" >作废</button></span>');
	}else if(targetStep == 'sz'){
		$("#szps").show();
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'start\')" >退回秘书长</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'end\')" >同意并分发</button></span>');
	}else if(targetStep == 'end'){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToEnd(\'0007\')" >确认收到</button></span>');
	}
}else if(type=='0002'){
	if(targetStep == 'start'){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToEnd(\'0005\')" >作废</button></span>');
	}else if(targetStep == 'sz'){
		$("#szps").show();
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'start\')" >退回副市长</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'end\')" >同意并分发</button></span>');
	}else if(targetStep == 'end'){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToEnd(\'0007\')" >确认收到</button></span>');
	}
}else if(type=='0003'){
	if(targetStep == 'start'){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToEnd(\'0005\')" >作废</button></span>');
	}else if(targetStep == 'fsz'){
		$("#szps").show();
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'start\')" >退回</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'msz\')" >秘书长批示</button></span>');
	}else if(targetStep == 'msz'){
		$("#mszOpinion").removeAttr('readonly');
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'start\')" >退回</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'fsz\')" >副市长批示</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'sz\')" >市长批示</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'end\')" >同意并分发</button></span>');
	}else if(targetStep == 'sz'){
		$("#szps").show();
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToNext(\'start\')" >退回</button></span>  <span><button type="button" class="btn btn_click" onclick="sendToNext(\'end\')" >同意并分发</button></span>');
	}else if(targetStep == 'end'){
		$("#buttonList").append('<span><button type="button" class="btn btn_click" onclick="sendToEnd(\'0007\')" >确认收到</button></span>');
	}
}

$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#orgCode").val(data.orgCode);//根据orgCode判断启用哪些流程按钮
		$("#userId").val(data.userId);
		$("#userCode").val(data.userCode);
		$("#userName").val(data.userName);
		$("#roleCode").val(data.roleCodes);
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/goutapply/getGoutApply",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#status").val(data.status);
				$("#type").val(data.type);
				$("#applyUnit").html(data.applyUnit);
				$("#applyDate").html(new Date(data.applyDate).format('yyyy年M月d日'));
				$("#applyUserName").val(data.applyUserName);
				$("#applyOrgName").val(data.applyOrgName);
				$("#applyPhoneNum").val(data.applyPhoneNum);
				$("#reason").val(data.reason);
				$("#leaveDate").val(new Date(data.leaveDate).format('yyyy年M月d日'));
				$("#returnDate").val(new Date(data.returnDate).format('yyyy年M月d日'));
				$("#goToPlace").val(data.goToPlace);
				$("#proxyNameOrg").val(data.proxyNameOrg);
				$("#proxyPhoneNum").val(data.proxyPhoneNum);
				$("#remark").val(data.remark);
				$("#contactsName").val(data.contactsName);
				$("#contactsPhoneNum").val(data.contactsPhoneNum);
				$("#contactsMBNum").val(data.contactsMBNum);
				
				$("#mszOpinion").val(data.mszOpinion);
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				if(data.szOpinion!=null&&data.fszOpinion!=null){
					$("#szpsSpan").html(data.szOpinion+"<br>"+data.fszOpinion);
				}else if(data.szOpinion!=null&&data.fszOpinion==null){
					$("#szpsSpan").html(data.szOpinion);
				}else if(data.szOpinion==null&&data.fszOpinion!=null){
					$("#szpsSpan").html(data.fszOpinion);
				}
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
});

function sendToEnd(status){
	//作废或者完成（作废则所有待办都删除，完成则仅删除当前待办）
	var dto = getAddData();
	dto.status = status;
	dto.roleCode = $("#roleCode").val();
	doJsonRequest("/goutapply/endGoutApply",dto,function(data){
		if(data.result) {
			if(data.data == 'success'){
				//跳转到成功页面
				var flag = 0;
				if(status=="0005"){
					flag = 3;
				}
				window.location.href="../tododocs/docs_down.html?flag="+flag+"&fromPage=0001&btnSource="+btnSource;
			}else{
				$.alert("提交失败。");
			}
		} else {
			$.alert("提交失败。");
		}
	},{showWaiting:true});
}

function sendToNext(step){
	/**
	 * 校验
	 */
	if(typeof($("#mszOpinion").attr("readonly")) == "undefined" && !$("#mszOpinion").is(":hidden")){
		if($("#mszOpinion").val()==''){
			$.alert("请输入审批意见。");
			return false;
		}
	}
	if(typeof($("#szps").attr("readonly")) == "undefined" && !$("#szps").is(":hidden")){
		if($("#szps").val()==''){
			$.alert("请输入审批意见。");
			return false;
		}
	}
	
	if(step == 'fsz'){
		doRequest("/goutapply/getRoleCodes",{type:"0003"},function(data){
			if(data.result){
				var data = data.data;
				var roleCodes = data.fszStep;
				var obj = {
				    title:'选择处理角色',
				    height:"320px",
				    width:"750px",
				    url:'../users/follow_role_dialog.html?operaterId=&isShowDate=0002&ignoreRole=&selectType=0003&markType=0002&checkType=checkbox&roleCodes='+roleCodes,
				    myCallBack:"setRoleCode"
				}
				new jqueryDialog(obj);
			}
		});
	}else{
		var dto = getAddData();
		dto.step = step;
		if(targetStep == 'sz'){
			dto.szOpinion = makeOpinions($("#szOpinion").val(),$("#szps").val());
		}else if(targetStep == 'fsz'){
			dto.fszOpinion = makeOpinions($("#fszOpinion").val(),$("#szps").val());
		}else if(targetStep == 'msz'){
			if($("#mszOpinion").val()!=''){
				dto.mszOpinion = $("#mszOpinion").val() + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
			}else{
				dto.mszOpinion = $("#mszOpinion").val();
			}
		}
		
		doJsonRequest("/goutapply/updateGoutApply",dto,function(data){
			if(data.result) {
				if(data.data != null && data.data != '' &&  data.data != 'error'){
					//跳转到成功页面
					if(data.data == 'success'){
						window.location.href="../tododocs/docs_down.html?flag=7&fromPage=0001&btnSource="+btnSource;
					}else{
						window.location.href="../tododocs/docs_down.html?fromPage=0001&btnSource="+btnSource+"&roleName="+data.data;
					}
					
				}else{
					$.alert("提交失败。");
				}
			} else {
				$.alert("提交失败。");
			}
		},{showWaiting:true});
	}
	
	
}

function setRoleCode(da){
	var dto = getAddData();
	dto.step = "fsz";
	dto.roleCode = da.roleCode[0];
	if(targetStep == 'sz'){
		dto.szOpinion = makeOpinions($("#szOpinion").val(),$("#szps").val());
	}else if(targetStep == 'fsz'){
		dto.fszOpinion = makeOpinions($("#fszOpinion").val(),$("#szps").val());
	}else if(targetStep == 'msz'){
		if($("#mszOpinion").val()!=''){
			dto.mszOpinion = $("#mszOpinion").val() + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		}else{
			dto.mszOpinion = $("#mszOpinion").val();
		}
	}
	doJsonRequest("/goutapply/updateGoutApply",dto,function(data){
		if(data.result) {
			if(data.data == 'success'){
				//跳转到成功页面
				window.location.href="../tododocs/docs_down.html?flag=7&fromPage=0001&btnSource="+btnSource;
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
			id:id,
			type:type,
			userId:$("#userId").val(),
			userCode:$("#userCode").val(),
			userName:$("#userName").val()
		};
		return dto;
}

function makeOpinions(lastOpinion,localOpinion){
	var retOpinion = "";
	if(lastOpinion!='' && localOpinion!=''){
		retOpinion = localOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]<br>"+lastOpinion;
	}else if(lastOpinion == '' && localOpinion!=''){
		retOpinion = localOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
	}else if(lastOpinion != '' && localOpinion==''){
		retOpinion = lastOpinion;
	}
	return retOpinion;
}

function showToBack(){
	window.location.href = "goutapply_calendar.html";
}
