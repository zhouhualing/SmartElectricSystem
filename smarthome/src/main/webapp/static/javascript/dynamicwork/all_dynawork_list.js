var task = getURLParam("task");
/**
 * init method
 */
$(function(){
	//文件查阅
	getCurrenUserInfo(function(data){
		var title = "";
		if(task == 1){
			title = '所有动态期刊';
		}else if(task == 2){
			$("#collapseOne").append('<input type="hidden" value="0002" name="status"  id="status" />');
			title = '审批中的动态期刊';
		}else if(task == 3){
			$("#collapseOne").append('<input type="hidden" value="0006" name="status"  id="status" />');
			title = '已分发的动态期刊';
		}else if(task == 4){
			$("#collapseOne").append('<input type="hidden" value="0007" name="status"  id="status" />');
			title = '待提交的动态期刊';
		}
		$("#reportInfoQuery").attr("queryId","dynaIndexQuery");
		$("#reportInfoQuery").attr("conf","{initFun:initFun,title:'"+title+"',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'title'}");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	});
});
/*var scanObj = {
		text:"处理",
		fun:function(data) {
			window.location.href="dynamicwork_send.html?id="+data.id;
		}
};*/

function initFun(data, key) {
	if(key == "title") {
		var title2 = handleLongString(data.title,150,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+");' title='"+data.title+"'>"+title2+"</div>";
	}
}

function goPage(data) {
	window.location.href = "dynamicwork_send.html?id="+data.id;
}
var sendObj = {
		text:"处理",
		fun:function(data) {
			window.location.href="dynamicwork_send.html?id="+data.id;
		}
};
var oppObj = [sendObj];


