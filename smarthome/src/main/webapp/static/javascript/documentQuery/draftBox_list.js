$("#param").val(getURLParam("param")); //当前收文id
$(function(){
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		doQuery();
	});
});

function initFun(data, key) {
	if(key == "reportTitle") {
		if(data.reportTitle!='' && data.reportTitle!=null){
			var title = handleLongString(data.reportTitle,152,"...");
			return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reportTitle+"'>"+title+"</div>";
		}else{
			return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title=''></div>";
		}
		
	}
}

function goPage(data) {
	var type = data.reportType;
	if(type=='0001' || type=='0002' || type=='0005' || type=='0006'){
		//政府发文、政府发函，办公厅发文，办公厅发函
		window.location.href="../senddoc/new_fwfh_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}else if(type=='0003'){
		//发电
		window.location.href="../telegram/new_fd_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}else if(type=='0009'){
		//拟发文
		window.location.href="../senddoc/new_nfw_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}else if(type=='5001'){
		//上级来文
		window.location.href="../receivedoc/new_sjlw_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}else if(type=='5002'){
		//下级来文
		window.location.href="../receivedoc/new_xjlw_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}else if(type=='6001'){
		//收电
		window.location.href="../telegram/new_sd_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}else if(type=='7001'){
		//委办局函文
		window.location.href="../letter/bureaus_letter_draft.html?id="+data.reportId+"&type="+type+"&fromPage=0001";
	}
}

var editObj = {
	text:"编辑",
	fun:function(data) {
		goPage(data);
	}
};

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						reportId:data.reportId,
						tableName:data.tableName
						
				};
				doJsonRequest("/senddoc/deleteReport", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true});				
			},data:"1"});

		}
};
var oppObj = [editObj,deleObj];


