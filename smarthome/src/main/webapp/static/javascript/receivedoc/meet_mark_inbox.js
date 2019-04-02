var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href = "meet_mark_create.html?id="+data.id;
		}
};

var deleteObj = {
		text:"删除",
		fun:function(data) {
			var dto = {
					id:data.id
			}
			doJsonRequest("/meet/meetmark/doDelete",dto,function(data){
				doQuery();
				if(data.result) {
					$.alert("删除成功");
				} else {
					$.alert("删除失败");
				}
			},{showWaiting:true})
		}
};


$(function(){
	doQuery();
})

function initFun(data,key) {
	if(key == "year") {
		if((data[key].length >0)&&(data["number"] != null)) {
			return "【"+data[key]+"】第"+data["number"]+"期";
		}
	}
	
	if(key == "meetContent") {
		var title = handleLongString(data[key],110,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="meet_mark_create.html?id="+data.id
}

var oppObj = [editObj,deleteObj];
