/**
 * init method
 */
$(function(){
});

function doSearchOrg() {
	var obj = {
	    title:'选择组织机构',
	    height:"300px",
	    width:"450px",
	    url:'../org/org_org_tree.html?checkType=radio',
	    myCallBack:"initOrgInfo"
	}
	new jqueryDialog(obj);
}

function initOrgInfo(data) {
	$('#orgCode').val(data[0].code);
	$("#orgName").val(data[0].realName);
}

function doAddPosition() {
	var obj = {
	    title:'添加职位',
	    height:"300px",
	    url:'../users/position_dialog.html?nids='+$('#positionIds').val(),
	    myCallBack:"onAddPositions"
	}
	new jqueryDialog(obj);
}

function onAddPositions(data) {
	var ids = $('#positionIds').val() ? $('#positionIds').val().split(',') : [];
	var names = $('#positionNames').val() ? $('#positionNames').val().split('，') : [];
	$.each(data,function(i,item){
		if ($.inArray(item.id+'', ids) == -1) {
			ids.push(item.id);
			names.push(item.positionName);	
		}
	});
	$('#positionIds').val(ids.join(','));
	$('#positionNames').val(names.join('，'));
}

function doRemovePosition() {
	if ($('#positionIds').val()) {
		var obj = {
		    title:'移除职位',
		    height:"300px",
		    url:'../users/position_dialog.html?ids='+$('#positionIds').val(),
		    myCallBack:"onRemovePositions"
		}
		new jqueryDialog(obj);
	}
}

function onRemovePositions(data) {
	var ids = $('#positionIds').val().split(',');
	var names = $('#positionNames').val().split('，');
	$.each(data,function(i,item){
		ids = $.grep(ids,function(id,j){
			return id!=item.id;
		});
		names = $.grep(names,function(name,j){
			return name!=item.positionName;
		});
	});
	$('#positionIds').val(ids.join(','));
	$('#positionNames').val(names.join('，'));
}

function doAddRole() {
	var obj = {
	    title:'添加角色',
	    height:"300px",
	    url:'../users/role_dialog.html?nids='+$('#roleIds').val(),
	    myCallBack:"onAddRoles"
	}
	new jqueryDialog(obj);
}

function onAddRoles(data) {
	var ids = $('#roleIds').val() ? $('#roleIds').val().split(",") : [];
	var names = $('#roleNames').val() ? $('#roleNames').val().split("，") : [];
	$.each(data,function(i,item){
		if ($.inArray(item.id+'', ids) == -1) {
			ids.push(item.id);
			names.push(item.roleName);	
		}
	});
	$('#roleIds').val(ids.join(','));
	$('#roleNames').val(names.join('，'));
}

function doRemoveRole() {
	if ($('#roleIds').val()) {
		var obj = {
		    title:'移除角色',
		    height:"300px",
		    url:'../users/role_dialog.html?ids='+$('#roleIds').val(),
		    myCallBack:"onRemoveRoles"
		}
		new jqueryDialog(obj);
	}
}

function onRemoveRoles(data) {
	var ids = $('#roleIds').val().split(',');
	var names = $('#roleNames').val().split('，');
	$.each(data,function(i,item){
		ids = $.grep(ids,function(id,j){
			return id!=item.id;
		});
		names = $.grep(names,function(name,j){
			return name!=item.roleName;
		});
	});
	$('#roleIds').val(ids.join(','));
	$('#roleNames').val(names.join('，'));
}

function doSubmit() {
	var form = $('#dataInputForm');
	var password = form.find('#password').val();
	var confirmPassword = form.find('#confirmPassword').val();
	var orgCode = form.find('#orgCode').val();
	if ((password || confirmPassword) && password!=confirmPassword) {
		$.alert('密码和确认密码不一致');
		return;
	}
	if(orgCode==''){
		$.alert('请选择组织机构。');
		return;
	}
	doRequest("/user/addUser", form.serialize(), function(data,status){
		if(data=='success') {
			window.location.href="user_show_list.html";
		} else {
			$.alert("提交失败。");
		}
	});
}
