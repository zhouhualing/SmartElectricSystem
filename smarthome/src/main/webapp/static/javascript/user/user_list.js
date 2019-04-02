//表格操作项
var oppObj = [];


//ready action
$(function(){
	doQuery();
})

//row click
function rowClick(data) {
	window.location.href="user_detail.html?id="+data.id;
}

/**
 * 创建用户
 */
function createFun() {
	window.location.href="user_add.html";
}

/**
 * 批量删除用户
 * @param data
 */
function deleteFun(data) {
	if(data.length==0) {
		$.alert("请选择要冻结的用户。");
		return false;
	}
	var config = {
		msg: "您确定要冻结么？",
		confirmClick: function () {
		var dto = {ids: data +""};
			doJsonRequest("/user/deleteUsers",dto,function(data){
				doQuery();
			},{showWaiting:true,successInfo:'删除成功',failtureInfo:'删除失败'})
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
	C_doOrgSelect(function(data){
		$.alert(data.name+":"+data.id+":"+data.code)
	})
}


function doASelect() {
	C_doAreaSelect(function(data){
		$.alert(data.name+":"+data.id+":"+data.code)
	})
}
