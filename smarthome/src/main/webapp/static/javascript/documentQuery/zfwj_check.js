/**
	0001-市政府发文 
	0002-市政府发函 
	0005-办公厅发文 
	0006-办公厅发函 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var flag = false;
var text = "";
var status = "";
$(function(){
	if(id != null) {
		$("#id").val(id);
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		};
		$('#dataInputForm').addClass('fileupload-processing');
		doJsonRequest("/senddoc/getReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				text = data.text;//正文,暂时的
				status = data.status;
				if(!flag){
					$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?checkInfo=1&officeUrl='+text+'&taskid='+id+'&shAttBtn=1&showPrintBtn='+status+'" style="width:100%;height:800px;"></iframe>');
					flag = true;
				}
				$("#textPage a").tab("show");
				$("#textPage").trigger("click");
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else {
		$.alert("获取文件信息失败。");
	}
});