var id = getURLParam("id");

$(function(){
	initData();
})

function initData() {
	$("#id").val(id);
	var data = {
	     id:id
	}
	doJsonRequest("/role/getRoleData", data, function(data, status){
		if(data.result) {
			var data = data.data;
			$("#roleName").val(data.roleName);
			$("#roleCode").val(data.roleCode);
			$('#permissionIds').val(data.permissionIds||'0');
			doQuery();
		} else {
		}
	});
}

function goBack() {
	window.history.back();
}
