$("#processInstanceId").val(getURLParam("processInstanceId"))
$(function(){
	doQueryWF("reportInfo", "searchDiv_wf");
})

if((getURLParam("status") == 5)||(getURLParam("status") == 6)) {
    $("#btnDelete").hide();
}

/**
 *
 */
initWorkOrderInfo(function(data){
},"id")

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
	history.go(-1);
}

function wf_getMark() {
	return $("#mark").val();
}

function doAssign(userCode) {
	var dto = {
		taskId:getURLParam("taskId"),
		assignUserCode:userCode,
		businessKey:getURLParam("workOrderId")
	}
	doJsonRequest("/workflow/doAssingee",dto,function(data){
		history.go(-1);
	},{showWaiting:true,successInfo:'已成功指派',failtureInfo:'指派失败'})
}

function doClaim() {
	var dto = {
		taskId:getURLParam("taskId"),
		businessKey:getURLParam("workOrderId")
	}
	doJsonRequest("/workflow/doClaim",dto,function(data){
		history.go(-1);
	},{showWaiting:true,successInfo:'认领成功',failtureInfo:'认领失败'})
}

function doDelete() {
    $.confirm({
        title: '提示信息',
        msg: '确定要作废此工单么？',
        height: 180,
        confirmBtn: true,
        cancelBtn: true,
        closeBtn: true,
        confirmClick: function(){
            var dto = {
                id:getURLParam("id")
            }
            doJsonRequest("/spmsWorkOrder/doModifyDelete",dto,function(data) {
                if(data.result) {
                    goBack();
                } else {

                }
            })
        }
    })

}