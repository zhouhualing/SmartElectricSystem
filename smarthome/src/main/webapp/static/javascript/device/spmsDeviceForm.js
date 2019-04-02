if(getURLParam("type") == 1) {
    $("#the_name1").html("网关列表");
} else if(getURLParam("type") == 2) {
    $("#the_name1").html("空调列表");
} else {
    $("#the_name1").html("传感器列表");
}
if(getURLParam("type") == 1) {
    $(".table_title h2").html("添加网关");
    $("#the_name").html("添加网关");
} else if (getURLParam("type") == 2) {
    $("#the_name").html("添加空调");
    $(".table_title h2").html("添加空调")
} else {
    $("#the_name").html("添加门窗传感器");
    $(".table_title h2").html("添加门窗传感器")
}

$(document).ready(function() {
	$("#type").select2("readonly", true);
	$("#type").select2("val", getURLParam("type"));
	
	//$("#storage").select2("readonly", true);
	getStorageAll();
	//对type进行初始化操作
//	$("#type").val(getURLParam("type"));
	$(".active").removeClass("active");
	$("li:even",$(".nav")).eq(getURLParam("type")-1).attr("class","active");
	
	$("#btnSubmit").click(function() {
		$("#inputForm").submit();
	});

	$("#inputForm").validate({
		submitHandler : function(form) {
			if($("#storage").val().length <=0) {
				$.alert("请选择仓库")
				return false;
			}
			var dto = {
				sn : $("#mySn").val(),
				mac : $("#myMac").val(),
				type : $("#type").val(),
				vender : $("#vender").val(),
				model : $("#model").val(),
				hardware : $("#hardware").val(),
				software : $("#software").val(),
				storage : $("#storage").val(),
				status  : $("#status").val(),
				operStatus : $("#operStatus").val()
				
			};
			doJsonRequest("/spmsDevice/validMacAndSn", dto, function(data) {
				if(data.data.result){
					doJsonRequest("/spmsDevice/doSave", dto, function(data) {
						if (data.result!=null) {
							//$.alert("成功！");
							window.location.href="spmsDeviceList.html?type="+$("#type").val();
						} else {
							$.alert("添加失败！");
						}
					});
				}else{
					$.alert("MAC或SN重复！");
					return false;
				}
			});
		}
	});
});
function getStorageAll(){
 doJsonRequest("/spmsDevice/getStorageAll", null, function (data) {
        if (data.result) {
            for (var i = 0; i < data.data.length; i++) {
            if(data.data[i].value!="已出库"){
                $("#storage").append("<option value='" + data.data[i].code + "'>" + data.data[i].value + "</option>");
                }
            }
        } else {
            $.alert("产品类型获取失败");
        }
    });

}