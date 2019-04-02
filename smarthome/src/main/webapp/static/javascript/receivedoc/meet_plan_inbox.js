var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href = "meet_plan_create.html?id="+data.id;
		}
};

var deleteObj = {
		text:"删除",
		fun:function(data) {
			var dto = {
					id:data.id
			}
			doJsonRequest("/meet/meetplan/doDelete",dto,function(data){
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
	if(key == "titleType") {
		var str = "";
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
		return "<a href='"+"meet_plan_create.html?id="+data.id+"'>"+str+"</a>";
	}else if(key == "meetCount") {
		if((data[key] != null)&&(data[key].length >0)) {
			return "第"+data[key]+"次";
		}
		
	} else if(key == "planDate") {
		return ((data["planDate"]==null?"":new Date(data["planDate"]).format("yyyy-MM-dd"))+" "+(data["time"]==null?"":data["time"]));
	}
}

var oppObj = [editObj,deleteObj];
