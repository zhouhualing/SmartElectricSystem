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

function doAddPermission() {
	var obj = {
	    title:'选择权限',
	    height:"300px",
	    url:'../users/permission_dialog.html?nids='+$('#permissionIds').val(),
	    myCallBack:"onSelectPermissions"
	}
	new jqueryDialog(obj);
}

function onSelectPermissions(data) {
	var str = $('#permissionIds').val();
	var ids = str.split(',');
	$.each(data,function(i,item){
		ids.push(item.id);
	});
	$('#permissionIds').val(ids.join(','));
	doQuery();
}

var deleteObj = {
     text:"移除",
     fun:function(data){
    	 var str = $('#permissionIds').val();
    	 var ids = str.split(',');
    	 ids = $.grep(ids,function(id,i){
    		 return id != data.id;
    	 });
    	 $('#permissionIds').val(ids.join(','));
    	 doQuery();
	 }
}

var oppObj = [deleteObj];

function doSubmit() {
	var str = $('#permissionIds').val();
	var arr = str.split(',');
	var ids = $.grep(arr,function(id,i){
		return id>0;
	});
	var data = {
	     id:id,
	     roleName:$('#roleName').val(),
	     roleCode:$('#roleCode').val(),
	     permissionIds:ids.join(',')
	};
	doJsonRequest("/role/addRole", data, function(data, status){
		if(data.result) {
			var data = data.data;
			if (data) {
				$.alert(data);
			} else {
				window.history.back();
			}
		} else {
			$.alert('保存失败');
		}
	});
}
