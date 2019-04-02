
/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode)
		doQuery();
	})
	
});

function doCallBack() {
	return $.toJSON(getSelectRadio("reportInfoQuery"));
}


