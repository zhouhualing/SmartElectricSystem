$(function(){
	var href =location.href;
	var len = href.indexOf("/view");
	href = href.substring(0,len);
	$("#okBtn").attr("href",href);
});