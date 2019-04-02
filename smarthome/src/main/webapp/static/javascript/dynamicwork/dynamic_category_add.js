function doSubmit() {
	var dto = {categoryCode:$("#categoryCode").val(),categoryName:$("#categoryName").val()};
	doJsonRequest("/dynawork/addCategory",dto,function(data){
		if(data.data=='success') {
			window.location.href="dynamic_category_list.html";
		} else {
			$.alert("提交失败。");
		}
	});
}
