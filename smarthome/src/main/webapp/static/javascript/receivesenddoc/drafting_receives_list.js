/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	})
	
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_receives_show.html?id="+data.id+"&fromPage=0001";
		}
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href="drafting_recevie.html?id="+data.id+"&fromPage=0001";
		}
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.id
				}
				doJsonRequest("/receivedoc/delReceive", dto, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true})				
			},data:"1"})

		}
}

var receive_task = {
		text:"认领",
		fun:function(data){
			var dto = {
					id:data.id
			}
			doJsonRequest("/receivedoc/taskReceive",dto,function(data,status){
				if(data.result){
					$.alert("认领成功。");
					
				}else{
					$.alert("认领失败。");
				}
			},{showWaitting:true})
		}
}


var oppObj = [editObj,deleObj];


