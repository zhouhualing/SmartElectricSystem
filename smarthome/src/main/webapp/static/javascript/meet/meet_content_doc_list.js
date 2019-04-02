var editObj = {
		text:"查看议程\议题单",
		fun:function(data) {
			window.location.href = "meet_content_doc_show.html?id="+data.id;
		}
};

$(function(){
	doQuery();
})

function initFun(data,key) {
	if(key == "titleType") {
		var str = "';"
		if(data[key] == "0001") {
			str = "常务会议";
		}
		if(data[key] == "0002") {
			str = "党组会议";
		}
		if(data[key] == "0003") {
			str = "全体会议";
		}
		return "<a href='"+"meet_content_doc_show.html?id="+data.id+"'>"+""+str+""+"</a>";
	}
}

var oppObj = [editObj];
