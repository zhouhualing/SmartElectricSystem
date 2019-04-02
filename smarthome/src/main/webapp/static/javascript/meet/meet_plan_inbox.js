var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href = "meet_plan_create.html?id="+data.id;
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
		if(data[key] == "0004") {
			str = "市长办公会议";
		}
		return "大同市人民政府"+str+"议题审阅卡";
	}
	
}

var oppObj = [editObj];
