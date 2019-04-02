$("#cancelDate").datepicker({
	dateFormat:"yy-mm-dd",
});

function doSubmit() {
	if($("#todoTheme").val().length ==0) {
		$.alert("请输入通知公告标题");
		return false;
	}
	
	if($("#cancelDate").val().length ==0) {
		$.alert("请输入取消通知时间");
		return false;
	}
	var dto = {
			todoTheme:$("#todoTheme").val(),
			content:UM.getEditor('myEditor').getContent(),
			cancelDate:$("#cancelDate").val()
	}
	doJsonRequest("/noticenter/addNotice",dto, function(data) {
		if(data.result) {
            window.location.href="noticeSuccess.html?flag=1";
		} else {
			$.alert("添加通知公告失败。");
		}
	},{showWaiting:true});
}

