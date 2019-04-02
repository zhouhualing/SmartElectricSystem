function doLogin() {
	$("#loginForm").attr("action","cmcp/doLogin").submit();
/*	
	var data = {
		userName:$("#userName").val(),
		password:$("#password").val()
	}
	
	doRequest("doLogin", data, function(data,status){
		if(status == "success") {
			window.location.href="view/common/indexMain.html";
		}
	})
	*/
}