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

//初始化订单基本信息之后 设置默认值
initWorkOrderInfo();
$(function(){
	$("#taskId").val(getURLParam("taskId"))
	doQueryWF("reportInfo","searchDiv_wf");
	getCurrenUserInfo(function(data){
		$("#createUserName").html(data.userName);
		$("#createDate").html(new Date().format("yyyy-MM-dd"));
	})
})


function wf_beforeValid() {
    if($("#infoType").val().length <=0) {
        $.alert("请选择反馈信息类型");
        return false;
    }
	return $("#inputForm_2").valid();
}

function createData() {
	var dto = {
        infoType:$("#infoType").val(),
		spmsWorkOrderId:getURLParam("workOrderId")
	}
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

function infoTypeChange() {
   if($("#infoType").val() == 4) {
       $("#info").show();
   } else {
       $("#info").hide();
   }
}