$("#reportType,#status").on("change",function(){
	doQuery();
})

var task = getURLParam("task");

$(function(){
	$("#status option").eq(1).remove();
	if(task == 1){
		//所有已分发已办结的政府发文
		$("#collapseOne").append('<input type="hidden" value="0001" name="reportType"  id="reportType" />');
		$("#collapseOne").append('<input type="hidden" value="0006" name="status"  id="status" />');
		$("#reportInfoQuery").attr("queryId","allFWQuery");
		$("#reportInfoQuery").attr("conf","{title:'政府发文列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,searchFiledName:'reportTitle'}");
	}else if(task == 2){
		//所有已分发已办结的办公厅发文
		$("#collapseOne").append('<input type="hidden" value="0005" name="reportType"  id="reportType" />');
		$("#collapseOne").append('<input type="hidden" value="0006" name="status"  id="status" />');
		$("#reportInfoQuery").attr("queryId","allFWQuery");
		$("#reportInfoQuery").attr("conf","{title:'办公厅发文列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,searchFiledName:'reportTitle'}");
	}
	$("#reportInfoQuery").empty();
	clickmedCommonTableToScan();
	doQuery();
});


function initFun(data, key) {
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,135,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reportTitle+"'>"+title+"</div>";
	}
	if(key == "reportCode") {
		if(data.reportType=="0001"||data.reportType=="0002"||data.reportType=="0005"||data.reportType=="0006"){
			var repCode = '';
			var strs= new Array(); //定义一数组 
			if(data.reportCode!=null&&data.reportCode!=''){
				strs=data.reportCode.split("_"); //字符分割 
				switch (data.reportType) {
				case '0001':
					repCode = "同政发";
					break;
				case '0002':
					repCode = "同政函";
					break;
				case '0005':
					repCode = "同政办发";
					break;
				case '0006':
					repCode = "同政办函";
					break;
				}
				return repCode+"【"+strs[0]+"】"+strs[1]+"号";
			}
		}
	}
}

function goPage(data) {
	var type = data.reportType;
//	if(type == "0001" || type == "0002"  || type == "0005"  || type == "0006" ){
		//发文、发函
		window.location.href="../documentQuery/zfwj_check.html?id="+data.id+"&type="+data.reportType;
//	}
//	else if(type == "0003"){
//		//发电
//		window.location.href="../telegram/new_fd_check.html?id="+data.id+"&type="+data.reportType;
//	}else if(type == "0009"){
//		//拟发文
//		window.location.href="../senddoc/new_nfw_check.html?id="+data.id+"&type="+data.reportType+"&showCard=1";
//	}else if(type == "5001"){
//		//上级来文
//		window.location.href="../receivedoc/new_sjlw_check.html?id="+data.id+"&type="+data.reportType;
//	}else if(type == "5002"){
//		//下级来文
//		window.location.href="../receivedoc/new_xjlw_check.html?id="+data.id+"&type="+data.reportType;
//	}else if(type == "6001"){
//		//来电
//		window.location.href="../telegram/new_sd_check.html?id="+data.id+"&type="+data.reportType;
//	}else if(type == "7001"){
//		//委办局函文
//		window.location.href="../letter/bureaus_letter_read.html?id="+data.id+"&type="+data.reportType;
//	}
}


var scanObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}

var oppObj = [scanObj];


