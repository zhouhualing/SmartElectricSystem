$(function(){
	
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	})
})

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href = "notice_show.html?fromPage=0001&id="+data.id;
		}
}

var cancelObj = {
	text:"撤销",
	fun:function(data) {
		$.confirm({
                title: '提示信息',
                msg: '确定要撤销么？',
                height: 180,
                confirmClick: function(){
                	doJsonRequest("/noticenter/setEndTodo",data.id, function(data){
                		if(data.result) {
                            doQuery();
                		} else {
                			$.alert("撤销失败。");
                		}
                	})
                }
            })
	}
}

var showObj = {
	text:"公示",
	fun:function(data) {
		$.confirm({
            title: '提示信息',
            msg: '确定要重新公示么？',
            height: 180,
            confirmClick: function(){
            	doJsonRequest("/noticenter/reStartTodo",data.id, function(data){
            		if(data.result) {
                        doQuery();
            		} else {
            			$.alert("重新公示失败。");
            		}
            	})
            }
        })
	}
}

function initFun(data,key) {
	if(key=="status") {
		if(data[key]=="0002") {
			clickmedTables.reportInfoQuery.operatorArr[2].text = "";
			clickmedTables.reportInfoQuery.operatorArr[1].text = "撤销";
			return "公示中"
		} else {
			clickmedTables.reportInfoQuery.operatorArr[1].text = "";
			clickmedTables.reportInfoQuery.operatorArr[2].text = "公示";
			return "已失效";
		}
	}
}

var oppObj = [scanObj,cancelObj,showObj];


