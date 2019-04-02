var task = getURLParam("task");
/**
 * init method
 */
$(function(){
	//文件查阅
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		doQuery();
	});
});

function initFun(data, key) {
	if(key == "title") {
		var title = handleLongString(data.title,135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.title+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="dynamicwork_draft.html?id="+data.id+"&fromPage=0001";
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			goPage(data);
		}
};

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id,
				};
				doJsonRequest("/dynawork/delDynaWorkIndex", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true});				
			},data:"1"});

		}
};
var oppObj = [editObj,deleObj];


