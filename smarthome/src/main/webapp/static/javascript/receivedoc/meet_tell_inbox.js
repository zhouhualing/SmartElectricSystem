var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href = "meet_tell_create.html?id="+data.id;
		}
};

var deleteObj = {
		text:"删除",
		fun:function(data) {
			var dto = {
					id:data.id
			}
			doJsonRequest("/meet/meettell/doDelete",dto,function(data){
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
	if(key == "meetTitle") {
		var title = handleLongString(data[key],190,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data[key]+"'>"+title+"</div>";
	}
	
}

function goPage(data) {
	window.location.href="meet_tell_create.html?id="+data.id
}
var oppObj = [editObj,deleteObj];
