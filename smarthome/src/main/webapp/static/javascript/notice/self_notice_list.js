$(function(){
		doQuery();
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			 window.parent.HROS.window.createTemp({
			        appid: 274,
			        title: data.businessTypeText,
			        url: clickmedBaseUrl + data.todoURL+"&id="+data.businessId+"&fromId="+data.id+"&cardIds="+data.createCardIds,
			        width: 1200,
			        height: 650,
			        isflash: false,
			        isresize: 1,
			        isopenmax: true
			 });
		}

}

function initFun(data,key) {
	if(key == "todoTheme") {
		var title = handleLongString(data.todoTheme,180,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+title+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.parent.HROS.window.createTemp({
        appid: 274,
        title: data.businessTypeText,
        url: clickmedBaseUrl + data.todoURL+"&id="+data.businessId+"&fromId="+data.id+"&cardIds="+data.createCardIds+"&close=true",
        width: 1200,
        height: 650,
        isflash: false,
        isresize: 1,
        isopenmax: true
 });
}

var oppObj = [scanObj];


