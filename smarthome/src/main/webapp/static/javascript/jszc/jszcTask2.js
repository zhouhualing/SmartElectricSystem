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


//初始化工单基本信息
initWorkOrderInfo(function(data){
    $("#addsubscribeDate").val(new Date(data.createDate).format("yyyy-MM-dd"))
    if(getURLParam("flag") == "show") {

    } else {
        initData(data);
    }
})

$(function(){
	$("#taskId").val(getURLParam("taskId"))
	doQueryWF("reportInfo","searchDiv_wf");
})

/**
 * 验证表单
 * @returns
 */
function wf_beforeValid() {
	if($("#failureCause").val() == ""){
		$.alert("请选择故障原因");
		return false;
	}

	if($("#isSolve").val() != 1){
		$.alert("调试不成功无法提交工单");
		return false;
	}
	return $("#inputForm_2").valid();
}

/**
 * 存储数据
 * @returns {Array}
 */
function createData() {
	var dto = {jszcdtos:[
	           {taskId:$("#taskId").val(),iKey:"failureCause",iValue:$("#failureCause").val()},
	           {taskId:$("#taskId").val(),iKey:"isSolve",iValue:$("#isSolve").val()},
	           ]};
	return dto;
}

function doSelect(type) {
	var name = 'name';
	var id = 'id';
	C_doAreaSelect({
		"params" : {
			classification : type
		},
		myCallBack : function(data) {
			if (type == 1) {
				$("#bizArea").val(data.id).trigger("keyup");
				$("#bizAreaName").val(data.name).trigger("keyup");
			}
			if (type == 2) {
				$("#eleArea").val(data.id).trigger("keyup");
				$("#eleAreaName").val(data.name).trigger("keyup");
			}
		}
	})
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
function initData(datass) {
	/*-----取得所有产品类型信息-------*/
	doJsonRequest("/jszc/listFailureCause", null, function (data) {
		if (data.result) {
			producttypeinfo = data.data;
			for (var i = 0; i < data.data.length; i++) {
				$("#failureCause").append("<option value='" + data.data[i][0] + "'>" + data.data[i][1] + "</option>");
			}
		} else {
			$.alert("产品类型获取失败");
		}
	});
}
