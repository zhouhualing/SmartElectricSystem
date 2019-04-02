$(function(){
	var dto = {
			id:getURLParam("id")
	}
	doJsonRequest("/noticenter/getNoticeInfo",dto, function(data) {
		if(data.result) {
           $("#todoTheme").html(data.data.todoTheme);
           $("#content").html(data.data.content);
           $("#createUserName").html(data.data.createUserName);
           $("#createDate").html(new Date(data.data.createDate).format("yyyy-MM-dd hh:mm"))
		} else {
			$.alert("获取通知公告信息失败。");
		}
	});
})

function clostApp() {
	$('#desk .window-container',parent.document).each(function(){
		parent.HROS.window.close($(this).attr('appid'));
	});
}

if(getURLParam("fromPage") == "0001"){
	$("#backBtn").text("返回");
	$("#backBtn").on("click",function(){
		goBack();
	})
} else {
	$("#backBtn").on("click",function(){
		clostApp();
	})
}