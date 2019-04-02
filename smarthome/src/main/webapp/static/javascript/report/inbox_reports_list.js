/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#userId").val(data.userId);
		doQuery();
	});
});


function initFlag(data) {
	if(data.theFlag == '1') {
		if(data.speed=="0001") {
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!!</span>";
		} else if(data.speed=="0002"){
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!!</span>";
		} else if(data.speed=="0003"){
			return "<img src='../../static/images/red.png' style='vertical-align:baseline;'/><span style='color:red'>!</span>";
		} else {
		}
		
	} else if(data.theFlag == '0'){
		if(data.speed=="0001") {
			return "<span style='color:red'>!!!</span>";
		} else if(data.speed=="0002"){
			return "<span style='color:red'>!!</span>";
		} else if(data.speed=="0003"){
			return "<span style='color:red'>!</span>";
		} else {
		}
	} 
}

function initFun(data, key) {
	if(key == "reportTitle"){
		var title = handleLongString(data.reportTitle,144,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.reportTitle+"'>"+title+"</div>";
	}
}

var editObj = {
	text:"处理",
	fun:function(data) {
		goPage(data);
	}
};

var oppObj = [editObj];

function goPage(data) {
	window.location.href=clickmedBaseUrl + data.todoURL + "&appFlag=1";
}
