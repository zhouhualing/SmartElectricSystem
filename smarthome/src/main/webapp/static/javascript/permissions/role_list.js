
var showObj1 = {
		text:"删除",
		fun:function(data){
			$.confirm({
				title: '提示信息',
				msg: '您确定要移除么？<br/><font color=\'red\'>将冻结该角色下的所有账户！</font>',
				height: 150,
                confirmBtn: true,
				confirmClick: function(){
					var info ={
						id:data.id
					}
					doJsonRequest("/rolePermissions/doDelete", info, function(data) {
						if (data.result) {
							//window.location.href="role_list.html";
							//alert(info);
							doQuery();
						} else {
							$.alert("操作失败");
						}
					},{showWaiting:true})
				}
			})
		}
}
//表格操作项
var oppObj = [showObj1];


//ready action
$(function(){
	//alert(11);
	doQuery();
})

//row click
function rowClick(data) {
	window.location.href="role_detail.html?id="+data.id;
	//alert(data.id);
}


function createFun() {
	//window.location.href="user_add.html";
	window.location.href="role_detail.html";
}


function deleteFun(data) {
	if(data.length==0) {
		$.alert("请选择要移除的角色。");
		return false;
	}
	var config = {
		msg: '您确定要移除么？<br/><font color=\'red\'>将冻结该角色下的所有账户！</font>',
		confirmClick: function () {
			doJsonRequest("/rolePermissions/doDeletes",data,function(data){
				doQuery();
			},{showWaiting:true,successInfo:'移除成功',failtureInfo:'移除失败'})
		}
	}
	$.confirm(config);
}

function doSelect() {
	//方式一
//	C_doOrgSelect({title:'test',myCallBack:function(data){
//		$.alert(data.name)
//	}})
	
	//方式二
	/*C_doOrgSelect(function(data){
		$.alert(data.name+":"+data.id+":"+data.code)
	})*/
}


function doASelect() {
	/*C_doAreaSelect(function(data){
		$.alert(data.name+":"+data.id+":"+data.code)
	})*/
}
