$(function(){
	var href =location.href;
	var len = href.indexOf("/view");
	href = href.substring(0,len);
	href = href + "/resetPassword/goSendEmail";
	$(".reset_password").attr("href", href);
});