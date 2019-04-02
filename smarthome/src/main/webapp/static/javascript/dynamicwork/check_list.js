var task = getURLParam("task");
$(function(){
	if(task == 1){
		//动态查询
		getCurrenUserInfo(function(data){
			//data.orgCode == "001" || data.orgCode == "002" || data.orgCode == "003" || data.orgCode == "004" || data.orgCode == "005"
			if(data.orgCode!='xinxk' && data.roleCodes.indexOf('063')<=-1){
				//如果当前登陆人不是信息科和邢兆东副调研员时，默认按照信息科已分发的列表进行查询
				$("#userCode").val('xinxike');
				$(".row").append('<input type="hidden" name="status"  id="status" value="0006"/>');
			}else{
				$("#userCode").val(data.userCode);
			}
			
			$("#reportInfoQuery").attr("queryId","dynaworkCheckQuery");
			$("#reportInfoQuery").attr("conf","{title:'参与的动态',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,searchFiledName:'title'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 2){
		//督办单查询
		getCurrenUserInfo(function(data){
			$("#userCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","superviseCheckQuery");
			$("#reportInfoQuery").attr("conf","{title:'参与的批示通知单',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,searchFiledName:'title'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}else if(task == 3){
		//督办汇总查询
		getCurrenUserInfo(function(data){
			$("#userCode").val(data.userCode);
			$("#reportInfoQuery").attr("queryId","supervisesumCheckQuery");
			$("#reportInfoQuery").attr("conf","{title:'参与的批示汇总单',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,searchFiledName:'title'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
			doQuery();
		})
	}
});

function initFun(data, key) {
	if(key == "title") {
		var title = handleLongString(data[key],200,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
}

function goPage(data) {
	if(task == 1){
		window.location.href="dynamicwork_check.html?id="+data.id;
	}else if(task == 2){
		window.location.href="supervisecard_check.html?id="+data.id+"&mark="+data.mark+"&superType="+data.superType;
	}else if(task == 3){
		window.location.href="supersum_list_check.html?id="+data.id;
	}
}

var scanObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}

var oppObj = [scanObj];


