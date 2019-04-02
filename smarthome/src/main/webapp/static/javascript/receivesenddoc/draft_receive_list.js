//$("#reportType,#status").on("change",function(){
//	doQuery();
//})

var check_status=getURLParam("check_status")

$(function(){
	if(check_status==="spz"){
		$("#fliterparams").append('<input type="hidden" name="status"  id="status" class="form-control" placeholder="0002"/>')
		$("#status").val("0002");
	}else if(check_status==="yff"){
		$("#fliterparams").append('<input type="hidden" name="status"  id="status" class="form-control" placeholder="0006"/>')
		$("#status").val("0006");
	}else if(check_status==="ybj"){
		var status_data=["0003","0004","0005","0007"]
		for(var i=0;i<4;i++){
			$("#fliterparams").append('<input type="hidden" name="status"  id="status" class="form-control" placeholder=""/>')
			$("#status").val(status_data[i]);
		}		
	}
})

/**
 * init method
 */
$(function(){
	$("#status option").eq(1).remove();
	getCurrenUserInfo(function(data){
		$("#createUserCode").val(data.userCode);
		doQuery();
	})
	
});

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_receives_show.html?id="+data.id
		}
}

var editObj = {
		text:"分发",
		fun:function(data) {
			if(data.status =="0003"||data.status =="0006") {
				window.location.href="send_receives.html?id="+data.id
			} else {
				$.alert("完成或分发状态的可以分发。");
			}
			
		}
}

var editObj = {
		text:"编辑",
		fun:function(data) {
			window.location.href="drafting_recevie.html?id="+data.id
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

if(check_status==="myreports"){
	var oppObj = [editObj,deleObj];
}else{
	var oppObj = [scanObj,deleObj];
}



