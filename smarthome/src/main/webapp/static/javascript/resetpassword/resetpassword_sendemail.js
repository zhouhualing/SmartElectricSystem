/**
 * 验证用户是否存在
 */
function checkUserCode(){
	var result = false;
	var userCode = $("#userCode").val();
	if("" != userCode){
		var href ="/resetPassword/checkUserCode";
		var dto = {
				userCode: userCode
	        };
		var conf = {
				async:false,
				showWaiting:true,
				handdingInfo:"处理中"
					};
		doJsonRequest(href,dto,function(data){
			if(data.data.success){
				result = true;
			}else{
				$.alert("用户名错误或不存在！");
				result = false;
			}
		},conf);
	}else{
		$.alert("用户名不能为空！");
		result =  false;
	}
	return result;
}

$(function(){
	var href =location.href;
	var len = href.indexOf("/view");
	href = href.substring(0,len);
	href = href + "/resetPassword/doSendEmail";
	$("#emailForm").attr("action",href);
});