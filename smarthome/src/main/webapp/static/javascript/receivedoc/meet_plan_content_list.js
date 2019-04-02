var showObj = {
		text:"查看",
		fun:function(data) {
			window.location.href = "meet_content_lib_show.html?id="+data.id;
		}
};

var saveObj = {
		text:"保存",
		fun:function(data,_this) {
			var dto = {
					id:$("[name='id']",$(_this).parent().parent()).val(),
					meetContent:$("[name='meetContent']",$(_this).parent().parent()).val()
			}
			doJsonRequest("/meet/meetcontentlib/modifyContent",dto,function(data){
				if(data.result) {
					$.alert({
						title:'提示',
						msg:"议题修改成功。",
						confirmClick:function(){
							location.reload() 
						}
					});
				} else {
					$.alert("议题修改失败。");
				}
			},{showWaitting:true})
		}
};

var createObj = {
		text:"创建方案",
		fun:function(data) {
			window.location.href = "meet_plan_create.html?planId="+data.id;
		}
};

function initFun(data,key) {
//	if(key == "meetContent") {
//		return "<input type='hidden' filedName='id' name='id' value='"+data.id+"'/><textarea style='width:99%;background:#F0F8FF'  onkeyup='autoSize(this)' cols='' rows='6'filedName='meetContent'  name='meetContent'>"+data.meetContent+"</textarea>"
//	}
}

function queryEnd() {
//	$("[name='meetContent']").trigger("keyup")
}

$(function(){
	var ids = [];
	$("[name='contentId']",$(parent.document)).each(function(){
		ids.push($(this).val());
	})
	$("#id").val(ids.join(","))
	doQuery();
})

var oppObj = [];


function doCallBack() {
	var result = getSelectRadio("reportInfoQuery");
	if(result == null) {
		$.alert("请选择一个议题。");
		return false;
	} else {
		return result;
	}
}