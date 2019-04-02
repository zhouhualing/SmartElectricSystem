
var task = getURLParam("task");

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	if(task == 1){
		//我参与的公文
		getCurrenUserInfo(function(data){
			$("#localUserCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","partakesDocsQuery");
			$("#reportInfoQuery").attr("conf","{title:'已审批的公文列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun2,showTitle:false}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 2){
		//我参与的审批中的公文
		getCurrenUserInfo(function(data){
			$("#localUserCode").val(data.userCode);
			$("#localUserCode").after('<input type="hidden" value="0002" name="status"  id="status" />');
			$("#reportInfoQuery").attr("queryId","partakesDocsSHZQuery");
			$("#reportInfoQuery").attr("conf","{title:'审批中的公文列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun2,showTitle:false}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 3){
		//我参与的已分发的公文
		getCurrenUserInfo(function(data){
			$("#localUserCode").val(data.userCode);
			$("#localUserCode").after('<input type="hidden" value="0006" name="status"  id="status" />');
			$("#reportInfoQuery").attr("queryId","partakesDocsYFFQuery");
			$("#reportInfoQuery").attr("conf","{title:'已分发的公文列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun2,showTitle:false}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 4){
		//我参与的已办结的公文
		getCurrenUserInfo(function(data){
			$("#localUserCode").val(data.userCode);
			$("#localUserCode").after('<input type="hidden" value="0007" name="status"  id="status" />');
			$("#reportInfoQuery").attr("queryId","partakesDocsYBJQuery");
			$("#reportInfoQuery").attr("conf","{title:'已办结的公文列表',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,showTitle:false}");
			$("#reportInfoQuery").empty();
			oppObj.push(showObj);
			oppObj.push(createObj);
			clickmedCommonTableToScan();
			doQuery();
		})
	}
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}

function initFun(data,key) {
	if(data.hasActionList == "0002") {
		clickmedTables.reportInfoQuery.operatorArr[1].text = "查看一事一表";
		clickmedTables.reportInfoQuery.operatorArr[2].text = "";
	} else {
		clickmedTables.reportInfoQuery.operatorArr[1].text = "";
		clickmedTables.reportInfoQuery.operatorArr[2].text = "创建一事一表";
	}
	
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,142,"...");
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

function initFun2(data, key) {
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,142,"...");
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
	if(type == "0001" || type == "0002"  || type == "0005"  || type == "0006" ){
		//发文、发函
		window.location.href="../senddoc/new_fwfh_check.html?id="+data.id+"&type="+type+"&showCard=1";
	}else if(type == "0003"){
		//发电
		window.location.href="../telegram/new_fd_check.html?id="+data.id+"&type="+type;
	}else if(type == "0009"){
		//拟发文
		window.location.href="../senddoc/new_nfw_check.html?id="+data.id+"&type="+type+"&showCard=1";
	}else if(type == "5001"){
		//上级来文
		window.location.href="../receivedoc/new_sjlw_check.html?id="+data.id+"&type="+type;
	}else if(type == "5002"){
		//下级来文
		window.location.href="../receivedoc/new_xjlw_check.html?id="+data.id+"&type="+type+"&checkFlag=1";
	}else if(type == "5003"){
		//请示报告
		window.location.href="../report/new_reqrep_check.html?id="+data.id+"&type="+type+"&checkFlag=1";
	}else if(type == "6001"){
		//来电
		window.location.href="../telegram/new_sd_check.html?id="+data.id+"&type="+type;
	}else if(type == "7001"){
		//委办局函文
		window.location.href="../letter/bureaus_letter_read.html?id="+data.id+"&type="+type;
	}
}

var showObj = {
		text:"查看一事一表",
		fun:function(data) {
			window.location.href="../../view/actionlist/show_actionlist.html?appFlag=1&id="+data.actionListId+"&fromPage=0002"
		}
}

var createObj = {
		text:"创建一事一表",
		fun:function(data) {
			window.location.href="../../view/actionlist/draft_actionlist.html?typeName1="+data.typeName+"&reportType1="+data.reportType+"&businessId1="+data.id+"&title1="+data.reportTitle
		}
}

var oppObj = [scanObj];


