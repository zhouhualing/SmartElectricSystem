/**
 * init method
 */
$(function(){
	doQuery();
	$('.btn').click(function(){
		var typename = $("#typename").val();
		if(typename!=null&&typename!=""){
			var dto = {categoryName:typename};
			doJsonRequest("/dynawork/addCategory",dto,function(data){
				if(data.data=='success') {
					doQuery();
				} else {
					$.alert("提交失败。");
				}
			});
		}
	});
});


var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				var dto = {
						id:data.iorder
				}
				doJsonRequest("/dynawork/delCategory",dto,function(data){
					doQuery();
					if(data.data=='success') {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true})				
			},data:"1"})

		}
}


var oppObj = [deleObj];


