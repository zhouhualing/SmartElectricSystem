var id = getURLParam("id");
$("#id").val(id);

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

function goBackToList(){
	window.location.href="meetmarkout_searchlist.html";
}
