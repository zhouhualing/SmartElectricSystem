var userId = getURLParam("id");

$(function(){
	initData();
})

function initData() {
	$("#id").val(userId);
	var data = {
	     id:userId
	}
	doJsonRequest("/user/getUserData", data, function(data, status){
		if(data.result) {
			var data = data.data;
			$("#userName").val(data.userName);
			$("#userCode").val(data.userCode);
			$('#status').val(data.status);
			$('#positionNames').val(data.positionNames);
			$('#roleNames').val(data.roleNames);
			$('#orgName').val(data.orgName);
			$('#email').val(data.email);
			$('#phoneNumber').val(data.phoneNumber);
			$('#mobilePhone').val(data.mobilePhone);
		} else {
		}
	});
}

function goBack() {
	window.history.back();
}