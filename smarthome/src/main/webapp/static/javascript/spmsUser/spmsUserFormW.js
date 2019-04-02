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
initWorkOrderInfo(function(data){
	//初始化默认填写信息
	$("#fullname").val(data.spmsUserName);
	$("#type").select2("val",data.userType);
	$("#address").val(data.address);
	$("#email").val(data.email);
	$("#mobile").val(data.spmsUserMobile);
	$("#ammeter").val(data.meterNumber);
	$("#bizAreaName").val(data.area.name);
	$("#bizArea").val(data.area.id);
	$("#eleAreaName").val(data.eleArea.name);
	$("#eleArea").val(data.eleArea.id);
    $("#idNumber").val(data.idNumber);
    $("#userId").val(data.userId);
});

$(function(){
	$("#taskId").val(getURLParam("taskId"))
	doQueryWF("reportInfo", "searchDiv_wf");
})



function wf_beforeValid() {
	return $("#inputForm_2").valid();
}

function createData() {
	var dto = {
		fullname:$("#fullname").val(),
		type:$("#type").val(),
		bizAreaId:$("#bizArea").val(),
		eleAreaId:$("#eleArea").val(),
		address:$("#address").val(),
		mobile:$("#mobile").val(),
		email:$("#email").val(),
		ammeter:$("#ammeter").val(),
		idNumber:$("#idNumber").val(),
		userId:$("#userId").val(),
		spmsWorkOrderId:getURLParam("workOrderId")
	}
	return dto;
}

function doSelect(type) {
	var name = 'name';
	var id = 'id';
    var tempId = 11;
    if(type == 1) {
        tempId = 2;
    }
	C_doAreaSelect({
		"params" : {
			classification : type,
            id:tempId
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

function doSelectUser() {
	C_doUserSelect({
		params:{
			userCodes:getURLParam("userCodes")
		},
		myCallBack:function(datas){
			doAssign(datas.code);
		}
	});
}

function doCallBack(){
	$.alert({
		title: '提示信息',
		msg: '操作成功',
		height: 180,
		confirmClick: function(){
			history.go(-1);
		}
	})
}

function wf_getMark() {
	return $("#mark").val();
}
