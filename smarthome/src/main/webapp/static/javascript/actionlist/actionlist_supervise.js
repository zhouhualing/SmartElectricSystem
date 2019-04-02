var task = getURLParam("task");
/*
function initFlag(data) {
	if(data.flag == 0) {
		return "";
	} else {
		return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/>";
	}
}
*/
/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		if(task == 1){
			$("#collapseOne").append('<input type="hidden" name="dcUserCode"  id="createUserCode" value="'+data.userCode+'"/>');
		}else{
			$("#collapseOne").append('<input type="hidden" name="createUser.userCode"  id="createUserCode" value="'+data.userCode+'"/><input type="hidden" name="status" id="status" value="0002"/>');
			$("#reportInfoQuery").attr("queryId","actionListQuery");
			$("#reportInfoQuery").attr("conf","{title:'办理列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun2,searchFiledName:'businessTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan(); 
		}
		doQuery();
	});
});
/*var nowSearchObj = null;
function createAnalysis() {
	var objs = clickmedTables.reportInfoQuery.createSearchParam();
	nowSearchObj = objs;
	var obj = {
	    title:'饼图分析',
	    height:"500px",
	    width:"750px",
	    url:'supervise_approval.html?type=0001',
	    myCallBack:myCallBack,
	    cancelText:"关闭",
	    confirmBtn:false,
	    fun:true
	}
	new jqueryDialog(obj);
}

function myCallBack() {
	
}
*/
var title = "";
var num = "";
if(task == 1){
	title = "督办";
	num = "0003";
}else{
	title = "办理";
	num = "0004";
}

function initFun(data, key) {
	if(key == "title") {
		var title = handleLongString(data.title,170,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.title+"'>"+title+"</div>";
	}
}

function initFun2(data, key) {
	if(key == "businessTitle") {
		var title = handleLongString(data.businessTitle,172,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.businessTitle+"'>"+title+"</div>";
	}
}


function goPage(data) {
	if(task == 1){
		window.location.href="show_actionlist.html?fromPage="+num+"&id="+data.actionId+"&task="+task+"&appFlag=1";
	}else{
		window.location.href="show_actionlist.html?fromPage="+num+"&id="+data.id+"&task="+task+"&appFlag=1";
	}
}



var scanObj = {
	text:title,
	fun:function(data) {
		goPage(data);
	}
}
var oppObj = [scanObj];
