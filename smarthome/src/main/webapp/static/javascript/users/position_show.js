var id = getURLParam("id");

$(function(){
	initData();
})

function initData() {
	$("#id").val(id);
	var data = {
	     id:id
	}
	doJsonRequest("/position/getPositionData", data, function(data, status){
		if(data.result) {
			var data = data.data;
			$("#positionName").val(data.positionName);
			$("#positionCode").val(data.positionCode);
			$('#orgName').val(data.orgName);
		} else {
		}
	});
}

function goBack() {
	window.history.back();
}