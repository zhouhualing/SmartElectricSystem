$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	});
});

var editObj = {
		text:"处理",
		fun:function(data) {
			goPage(data);
		}
};

function initFun(data, key) {
	if(key == "superTitle") {
		var title = handleLongString(data.superTitle,135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.superTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href=clickmedBaseUrl + data.todoURL + "&appFlag=1";
	/*//市长督办单
	if(data.tableName == 'tb_supervise_lc'){
		if(data.superType == '0001'){
			window.location.href="supervisecard_sz_examine.html?id="+data.superId+"&type="+data.superType+"&mark=003&fromPage=0001&taskId="+data.taskId;
		}else if(data.superType == '0002'){
			window.location.href="supervisecard_examine.html?id="+data.superId+"&type="+data.superType+"&mark=003&fromPage=0001&taskId="+data.taskId;
		}
	}else if(data.tableName == 'tb_supervise_branch_lc'){
		//市长督办单子流程
		window.location.href="supervisecard_szBranch_examine.html?id="+data.superId+"&type="+data.superType+"&mark=003&fromPage=0001&taskId="+data.taskId;
	}else if(data.tableName == 'tb_supervise_sum_lc'){
		//督办汇总单
		window.location.href="supersum_list_examine.html?id="+data.superId+"&type="+data.superType+"&mark=003&fromPage=0001&taskId="+data.taskId;
	}*/
}

var oppObj = [editObj];

