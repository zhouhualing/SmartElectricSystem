$("#reportType,#status").on("change",function(){
	doQuery();
})

var userId = "";


function initFlag(data) {
	
		if(data.flag == '1') {
			return "<img src='../../static/images/red.png'/>";
		} else {
			return "";
		}
}

/**
 * init method
 */
$(function(){
		
		doQuery();
	
});
var nowSearchObj = null;
function createAnalysis() {
	var objs = clickmedTables.reportInfoQuery.createSearchParam();
	nowSearchObj = objs;
	var obj = {
	    title:'饼图分析',
	    height:"500px",
	    width:"950px",
	    url:'supervise_approval.html?filterType=0001',
	    myCallBack:myCallBack,
	    cancelText:"关闭",
	    confirmBtn:false,
	    fun:true
	}
	new jqueryDialog(obj);
}

function myCallBack() {
	
}


function initFun(data, key) {
	if(key == "todoTheme") {
		var title = handleLongString(data.todoTheme,180,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.todoTheme+"'>"+title+"</div>";
	} else if(key == "userNames") {
		var currentUser = data.userNames;
		var beforeArr = currentUser.split(",");
		var nowArr = [];
		for(var i=0; i<beforeArr.length; i++) {
			
			if($.inArray(beforeArr[i],nowArr)==-1) {
				nowArr.push(beforeArr[i]);
			}
		}
		var nowUser = nowArr.join(",");
		var title = handleLongString(nowUser,56,"...");
		return "<span title='"+nowUser+"'>"+title+"</span>";
	}
}

function goPage(data) {
	var currentUser = data.userNames;
	var beforeArr = currentUser.split(",");
	var nowArr = [];
	for(var i=0; i<beforeArr.length; i++) {
		
		if($.inArray(beforeArr[i],nowArr)==-1) {
			nowArr.push(beforeArr[i]);
		}
	}
	var nowUser = nowArr.join(",");
	window.location.href="superviseDetail.html?id="+data.businessId+"&title="+data.todoTheme+"&businessName="+data.businessTypeText+"&deadLineTime="+data.theDeadLineTime+"&userNames="+nowUser+"&businessType="+data.businessType;
}

var superviseObj = {
		text:"查看详情",
		fun:function(data) {
			 goPage(data);
		}
}


var oppObj = [superviseObj];


