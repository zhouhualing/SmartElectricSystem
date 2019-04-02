function doCallBack() {
	return getSelectRadio("orgTargetDiv");
}

$(function(){
	doInit();
})

function doInit() {
	doJsonRequest("/user/getCurrentUserInfo",null,function(resultData,status){
		if(data.result) {
			var data = data.data;
			if(resultData.position =="0002") {
				$("#orgCode").val(resultData.orgCode+"%");
			} else {
				$("#orgCode").val(resultData.orgCode+'%');
			}
			doQuery("orgTargetDiv");
		} else {
			$.alert("获取当前用户信息出错。");
		}
	},{asyn:false})
}