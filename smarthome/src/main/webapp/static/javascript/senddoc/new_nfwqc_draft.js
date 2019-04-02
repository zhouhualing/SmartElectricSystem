$(function(){
	doQuery();
});
var nfwType = "";
var id = "";
function initFun(data, key) {
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,130,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+",\"0001\")' title='"+data.reportTitle+"'>"+title+"</div>";
	}
}

//拟发文起草
var editObj = {
		text:"起草",
		fun:function(data) {
			//goPage(data,'0001');
			nfwType = data.reportType;
			id = data.id;
			chooseReportType(nfwType);
		}
};
/*var editBGTObj = {
		text:"以办公厅起草",
		fun:function(data) {
			goPage(data,'0005');
		}
};*/
var oppObj = [editObj];

function goPage(data,type) {
	nfwType = data.reportType;
	id = data.id;
	goThisPage(data.nfwTitle);
//	window.location.href="new_fwfh_draft.html?id="+data.id+"&type="+type+"&fromPage="+data.reportType;
}
function chooseReportType(type){
	var obj = {
			title:'选择发文类型',
			height:'500px',
			width:"750px",
		    url:'choose_reportType.html?nfwType='+type,
		    myCallBack:"goThisPage"
	};
	new jqueryDialog(obj);
}
function goThisPage(data){
	if("0003"==data){
		window.location.href="../telegram/new_fd_draft.html?id="+id+"&type="+data+"&nfwType="+nfwType;
	}else{
		window.location.href="new_fwfh_draft.html?id="+id+"&type="+data+"&nfwType="+nfwType;
	}
}