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
	$("#qrBtn").show();
}

var kongTiaoCount=0;
var chuanGanCount=0;
var deviceList = new Array();
var qr = false;

//初始化工单基本信息之后 设置默认值
initWorkOrderInfo(function(data){
    kongTiaoCount = data.spmsProductTypeDTO.kongTiaoCount;
    chuanGanCount = data.spmsProductTypeDTO.chuangGanCount;
});


$(function(){
	$("#taskId").val(getURLParam("taskId"))
	doQueryWF("reportInfo","searchDiv_wf");
	getProductInfo();
})

function xgFun(){
	if(qr){
		qr = false;
	}
	if(wf_beforeValid()){
		hmBlockUI();
		doJsonRequest("/spmsProduct/bindDeviceToProduct",createData(),function(data){
			if(data.result){
				//保存备注
				var dto = {jszcdtos:[
				      	           {taskId:$("#taskId").val(),iKey:"mark",iValue:$("#mark").val()},
				      	   ]};
				var url = "/jszc/doSave";
				doJsonRequest(url,dto,function(data){
					if(data.result){
						$.alert("修改成功");
						hmUnBlockUI();
					}
				});
			}
		});
	}
	qr = true;
}

function qrFun(){
	if(wf_beforeValid()){
		hmBlockUI();
		doJsonRequest("/spmsProduct/bindDeviceToProduct",createData(),function(data){
			if(data.result){
				doJsonRequest("/spmsWorkOrder/QR",{taskId:$("#taskId").val()},function(data){
					if(data.result){
						$("#qrflow").show();
						$("#xgBtn").show();
						$("#qrBtn").hide();
						qr = true;
						
						//保存备注
						var dto = {jszcdtos:[
						      	           {taskId:$("#taskId").val(),iKey:"mark",iValue:$("#mark").val()},
						      	   ]};
						var url = "/jszc/doSave";
						doJsonRequest(url,dto,function(data){
							if(data.result){
								hmUnBlockUI();
							}
						});
//						$("#ltr").hide();
//						$("#gwMac").attr("disabled","disabled");
//						$(".delete_ico").each(function(){
//							$(this).parent().hide();
//						});
					}
				});
			}else{
				$.alert("安装调试失败!");
				hmUnBlockUI();
			}
		});
	}
}

function wf_beforeValid() {
	if(qr){
		return true;
	}
	if(!$("#inputForm_2").valid()) {
		return false;
	}
//	if($("#theFlag:checked").length <= 0) {
//		$.alert("请确认安装调试完毕。");
//		return false;
//	}
	if($("[name='oldMac']").length <= 0) {
		$.alert("请至少添加一个设备。");
		return false;
	}
	var obj = [];
	$("[name='oldMac']").each(function(){
		obj.push($(this).val());
	})
	var dto = {
		gwMac : $("#gwMac").val(),
		spmsUserDTO: {
			id:$("#wfuserId").val()
		},
		id:$("#wfproductId").val(),
		spmsDeviceMac:obj.join(",")
	}
	var result = false;
	doJsonRequest("/spmsProduct/checkGWandDevice",dto,function(data){
		if(data.data.result) {
			$.alert(data.data.message);
			result = false;
		} else {
			result = true;
			 $("#gwMac").val(data.data.gw);
		}
	},{showWaiting:true,async:false});
	return result;
}

function createData() {
	var obj = [];
	$("[name='oldMac']").each(function(){
		obj.push($(this).val());
	})
	var dto = {
		gwMac : $("#gwMac").val(),
		spmsUserDTO: {
			id:$("#wfuserId").val()
		},
		id:$("#wfproductId").val(),
		spmsDeviceMac:obj.join(",")
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
function getProductInfo() {
	var dto = {
		id:getURLParam("workOrderId"),
	}
	hmBlockUI();
	doJsonRequest("/spmsProduct/getProductInfoByWorkOrder",dto,function(data){
		if(data.result) {
			$("#productName").html(data.data.spmsProductDTOs[0].spmsTypeName);
			$("#productId").val(data.data.spmsProductDTOs[0].id);
			$("#gwMac").val(data.data.spmsProductDTOs[0].gwMac);
			
			var p = data.data.devices;
			for(var i = 0;i<p.length;i++){
				var str2 = p[i].type == 2?"selected='selected'":"";
				var str3 = p[i].type == 3?"selected='selected'":"";
				var tr = "<tr>" +
				"<td>" +
				"<a id='updateTypeurl' href='javascript:void(0)'>" + (p[i].type == 2 ? '智能空调' : '门窗传感器') + "</a>" +
				"<select id='updateType' name='updateType' style='display:none'  class='the_select2'><option value='2' "+str2+">智能空调</option><option value='3' "+str3+">门窗传感器</option></select>" +
				"</td>" +
				"<td>" +
				"<a href='javascript:void(0)'>" + p[i].mac + "</a>" +
				"<input type='hidden' name='oldMac' value='" + p[i].mac + "'/>" +
				"<input type='text' name='addMac' style='display:none;' value='" + p[i].mac + "'/>" +
				"</td>" +

				"<td>" +
				"<a href='javascript:void(0)'  onclick='doDelete(this,\"" + p[i].mac + "\",\"" + p[i].type + "\")'><i class='delete_ico'></i>回收设备</a>" +
				"<input class='edit_complete' name='' style='display:none;'  type='button' onclick='finishDeviceMac(this)'/>" +
				"<input class='edit_cancel' name='' style='display:none;'  type='button' onclick='cancleDeviceMac(this)'/>" +
				"</td>" +
				"</tr>";
				$("#ftr").after(tr);
			}
			
			
			dto = {taskId:$("#taskId").val()};
			doJsonRequest("/spmsWorkOrder/isQR",dto,function(data){
				if(data.result){
					if(!data.data.qr){
						$("#qrflow").hide();
						$("#xgBtn").hide();
					}else{
						qr = true;
						if(getURLParam("flag") != "show"){
							$("#xgBtn").show();
						}
						$("#qrBtn").hide();
//						$("#ltr").hide();
//						$("#gwMac").attr("disabled","disabled");
					}
					if(typeof(data.data.mark) != "undefined"){
						$("#mark").val(data.data.mark);
					}
					hmUnBlockUI();
				}
			});
		} else {

		}
	})
}

//如果id部位
function bindingDevice(_this) {
	//现有设备数
    var acnum = 0;
    var cgnum = 0;
    $("[name='updateType']").each(function(){
           if($(this).val() == 2) {
               acnum++;
           } else if ($(this).val() == 3) {
               cgnum++;
           }
    })
    var str2 = "";
    var str3 = "";
    if($("[name='deviceType']",$(_this).parents("td")).val() == 2) {
        str2="selected='selected'"
        acnum++;
    } else if ($("[name='deviceType']",$(_this).parents("td")).val() == 3) {
        str3="selected='selected'"
        cgnum++;
    }
    if(kongTiaoCount <acnum) {
        $.alert("已超出空调最大绑定数。");
        return false;
    }
    if(chuanGanCount <cgnum) {
        $.alert("已超出传感器最大绑定数。");
        return false;
    }

	var d = {
		productId: $(_this).parents(".table-condensed").attr("productId"),
		devicemac: $("[name='deviceMac']", $(_this).parent().parent()).val(),
		devicetype: $("[name='deviceType']", $(_this).parent().parent()).val()
	};
	
	for(var i = 0;i < deviceList.length ; i ++){
		if(deviceList[i].devicemac == d.devicemac && deviceList[i].devicetype == d.devicetype){
			$.alert("该设备已存在");
			return false;
		}
	}
	
	doJsonRequest("/spmsUser/validDevice", d, function (data) {
		if (data.result) {
			if (data.data.success) {
				d.devicemac=data.data.deviceMac;
				deviceList.push(d);
				$("tr", $(_this).parent().parent().parent()).eq($("tr", $(_this).parent().parent().parent()).length - 2).after("<tr>" +
				"<td>" +
				"<a id='updateTypeurl' href='javascript:void(0)'>" + (d.devicetype == 2 ? '智能空调' : '门窗传感器') + "</a>" +
				"<select id='updateType' name='updateType' style='display:none'  class='the_select2'><option value='2' "+str2+">智能空调</option><option value='3' "+str3+">门窗传感器</option></select>" +
				"</td>" +
				"<td>" +
				"<a href='javascript:void(0)'>" + d.devicemac + "</a>" +
				"<input type='hidden' name='oldMac' value='" + d.devicemac + "'/>" +
				"<input type='text' name='addMac' style='display:none;' value='" + d.devicemac + "'/>" +
				"</td>" +

				"<td>" +
				"<a href='javascript:void(0)'  onclick='doDelete(this,\"" + d.devicemac + "\",\"" + d.devicetype + "\")'><i class='delete_ico'></i>回收设备</a>" +
				"<input class='edit_complete' name='' style='display:none;'  type='button' onclick='finishDeviceMac(this)'/>" +
				"<input class='edit_cancel' name='' style='display:none;'  type='button' onclick='cancleDeviceMac(this)'/>" +
				"</td>" +
				"</tr>");
                $("[name='updateType']",$(_this).parent().parent()).val(d.devicetype);
				$("[name='deviceMac']", $(_this).parent().parent()).val("").blur();
			} else {
				$.alert(data.data.msg);
				$("[name='deviceMac']", $(_this).parent().parent()).val("").blur();
			}
		} else {
			$.alert("添加设备失败");
		}
	},{showWaiting:true});
}

function doDelete(_this,mac,type) {
	$(_this).parents("tr:first").remove();
	var list = new Array();
	for(var i = 0;i < deviceList.length ; i ++){
		if(deviceList[i].devicetype != type){
			list.push(deviceList[i]);
			continue;
		}
		if(deviceList[i].devicetype == type){
			if(deviceList[i].devicemac != mac){
				list.push(deviceList[i]);
			}
		}
	}
	deviceList = list;
}