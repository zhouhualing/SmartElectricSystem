/**
 * init method
 */
$(function(){
	//我参与的公文
	getCurrenUserInfo(function(da){
		doRequest("/user/getLeaderUserCode",{userCode:da.userCode},function(data){
			if(data.result){
				$("#localUserCode").val(data.data);
				doQuery();
			}
		});
	})
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			goPage(data);
		}
}

function initFun(data,key) {
	if(key == "reportTitle") {
		var title = handleLongString(data.reportTitle,142,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reportTitle+"'>"+title+"</div>";
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

var oppObj = [scanObj];


