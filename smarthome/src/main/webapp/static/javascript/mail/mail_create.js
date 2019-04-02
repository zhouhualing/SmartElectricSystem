function doSend() {
	if($("#destaddr").val()== null || $("#destaddr").val().length ==0) {
		$.alert("请输入被通知人手机号。");
		return false;
	}
	if($("#messagecontent").val()== null || $("#messagecontent").val().length ==0) {
		$.alert("请输入短信内容。");
		return false;
	}
	var dto = {
			destaddr:$("#destaddr").val(),
			messagecontent:$("#messagecontent").val()
	}
	doJsonRequest("/mail/sendMessage",dto,function(data){
		if(data.result) {
			$.alert("已成功发送");
			$("#destaddr").val("");
			$("#messagecontent").val("");
		} else {
			$.alert("发送出错。");
		}
	},{showWaiting:true})
}