/**
 * init method
 */
$(function(){
	doQuery();
});

/**
 * create btn method
 */
function createPosition() {
	window.location = "position_add.html";
}

var showObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="position_show.html?id="+data.id
		}
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href="position_edit.html?id="+data.id
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				}
				doJsonRequest("/position/delete", dto, function(data,status){
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


var oppObj = [showObj,editObj,deleObj];


