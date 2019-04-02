$(function(){
	var href =location.href;
	var len = href.indexOf("/view");
	href = href.substring(0,len);
	$("#cancelBtn").click(function(){
		window.location.href=href;
	});
	var action = href + "/resetPassword/doResetPassword";
	$("#emailForm").attr("action",action);
});

function checkPassword(){
	hmBlockUI();
	var password = $("#password").val();
	var repassword = $("#repassword").val();
	if("" == password || "" == repassword){
		hmUnBlockUI();
		$.alert("密码不能为空！");
		return false;
	}else{
		if(password != repassword){
			hmUnBlockUI();
			$.alert("两次输入的密码不一致！");
			return false;
		}else{
			return true;
		}
	}
}