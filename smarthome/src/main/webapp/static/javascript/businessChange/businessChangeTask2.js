if(getURLParam("flag") == "show") {
	$("#inputForm_2").hide();

    if(getURLParam("isClaim") == 1) {
        $("#btnCliam").show();
        $("#btnAssign").show();
    } else  if(getURLParam("isClaim") == 2){
        //管理员非认领
        $("#btnAssign").show();
    } else if(getURLParam("isClaim") == 3){
        //renling
        $("#btnCliam").show();
    }
    $("#the_name").html("未受理工单");
} else {
    $("#the_name").html("待办工单");    
	wf_getOperator({taskId:getURLParam("taskId")});
}

var kongTiaoCount=0;
var chuanGanCount=0;


//初始化工单基本信息之后 设置默认值
initWorkOrderInfo(function(data){
    kongTiaoCount = data.spmsProductTypeDTO.kongTiaoCount;
    chuanGanCount = data.spmsProductTypeDTO.chuangGanCount;
});

$(function(){
	$("#taskId").val(getURLParam("taskId"));
	doQueryWF("reportInfo","searchDiv_wf");
	//getProductInfo();
})

/*******保存前验证************/
function wf_beforeValid() {
	/**如果欠费余额为0 不做限制**/
	if($("#qfye").text() != "0元"){
		/******如果欠费并没有缴费则不允许通过*************/
		if($('#jf').val()!=1){
			alert("该用户欠费，不能进行业务变更操作");
			return false;
		}
	}
	return true;
}
/**********取出要保存的数据**********/
function createData() {
	var dto = {
		jszcdtos : [ {
			taskId : $("#taskId").val(),
			iKey : "jf",
			iValue : $("#jf").val()
		},{ 
		taskId : $("#taskId").val(),
		iKey : "qfye",
		iValue : $("#qfye").text()
	} ]
	};
	return dto;
}

function doCallBack(){
	$.alert({
		title: '提示信息',
		msg: '操作成功',
		height: 180,
		confirmClick: function(){
			history.go(-1);
		}
	});
}

function wf_getMark() {
	return $("#mark").val();
}


function doDelete(_this) {
	$(_this).parents("tr:first").remove();
}

/***获取用户欠费信息**********/
$(function(){
	//alert("ok");
	var dto={
			processInstanceId:getURLParam("processInstanceId")
	}
	doJsonRequest("/businessChange/getUserBanlance",dto,function(data){
		var balance=data.data;
		if(balance>=0){
			$(".balance").text(0+"元");
			$("#qfqk").text("正常");	
		}else{
			$(".balance").text(balance*-1+"元");
			$("#qfqk").text("欠费");	
		}
	})	
})