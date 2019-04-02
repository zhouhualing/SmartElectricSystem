var task = getURLParam("task");
/**
 * init method
 */
$(function(){
	//文件查阅
	getCurrenUserInfo(function(data){
		$("#localUserCode").val(data.userCode);
		var title = "";
		if(task == 1){
			title = '已审批的请示报告';
		}else if(task == 2){
			$("#localUserCode").after('<input type="hidden" value="0002" name="status"  id="status" />');
			title = '审批中的请示报告';
		}else if(task == 3){
			$("#localUserCode").after('<input type="hidden" value="0006" name="status"  id="status" />');
			title = '已分发的请示报告';
		}else if(task == 4){
			$("#localUserCode").after('<input type="hidden" value="0007" name="status"  id="status" />');
			title = '已办结的请示报告';
		}
		$("#reportInfoQuery").attr("queryId","partakesReqRepsQuery");
		$("#reportInfoQuery").attr("conf","{title:'"+title+"',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,searchFiledName:'reqRepTitle'}");
		$("#reportInfoQuery").empty();
		clickmedCommonTableToScan();
		doQuery();
	});
});

function initFun(data, key) {
	if(key == "reqRepTitle") {
		var title = handleLongString(data.reqRepTitle,142,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reqRepTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	if(data.reqRepType == "5003"){
		window.location.href="../report/new_reqrep_check.html?id="+data.id+"&type="+data.reqRepType;
	}
}


var scanObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
};
var oppObj = [scanObj];


