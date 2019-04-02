var showObj = {
		text:"查看",
		fun:function(data) {
			if(data.reportType=='0002') {
				window.location.href = "meet_content_lib_show.html?id="+data.id;
			} else {
				window.location.href = "meet_content_report_show.html?id="+data.id;
			}
			
		}
};

var editObj = {
		text:"修改",
		fun:function(data) {
//			if(data.reportType=='0002') {
				window.location.href = "meet_content_lib_create.html?fromPage=0001&id="+data.id;
//			} else {
//				window.location.href = "meet_content_report_create.html?id="+data.id;
//			}
		}
};
var deleObj = {
		text:"删除",
		fun:function(data) {
			if(data.status == "0001") {
				var dto = {
						id:data.id
				}
				doJsonRequest("/meet/meetcontentlib/doDelete",dto,function(data){
					doQuery();
					if(data.result) {
						$.alert("删除成功");
					} else {
						$.alert("删除失败");
					}
				},{showWaiting:true})
			} else if(data.status == "0006"){
				$.alert("上会状态不能删除。");
			} else {
				var dto = {
						id:data.id,
						status:'0003'
				}
				doJsonRequest("/meet/meetcontentlib/modifyStatus",dto,function(data){
					doQuery();
					if(data.result) {
						$.alert("删除成功");
					} else {
						$.alert("删除失败");
					}
				},{showWaiting:true})
			}
		}
};

$(function(){
	doQuery();
})

var oppObj = [showObj,editObj,deleObj];

/**
 * do link to createurl.
 */
function createMeetContentLib() {
	window.location.href="meet_content_lib_create.html"
}

function initFun(data, key) {
	if(key == "status") {
		if(data[key] == "0006") {
			clickmedTables.reportInfoQuery.operatorArr[1].text = "";
			clickmedTables.reportInfoQuery.operatorArr[2].text = "";
		}  else {
			clickmedTables.reportInfoQuery.operatorArr[1].text = "修改";
			clickmedTables.reportInfoQuery.operatorArr[2].text = "删除";
		}

	}
	
	if(key =="meetContent") {
		var str = "";
		if(data.reportType=='0002') {
			str = "meet_content_lib_show.html?id="+data.id;
		} else {
			str = "meet_content_report_show.html?id="+data.id;
		}
		return "<div class='reportTitles' onclick ='goPage(\""+str+"\")' title='"+data[key]+"'>"+handleLongString(data[key],88,'...')+"</div>";
	}	
	
}
function goPage(str){
    window.location.href=str;
}
