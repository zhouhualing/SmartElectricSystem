/**
 * init method
 */
$(function(){
	doQuery();
});

/**
 * create btn method
 */
function createUser() {
	window.location = "user_add.html";
}

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="user_show.html?id="+data.id
		}
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href="user_edit.html?id="+data.id
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				}
				doJsonRequest("/user/delete", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true})				
			},data:"1"})

		}
}


var oppObj = [scanObj,editObj];


function doSearchTarget() {
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


