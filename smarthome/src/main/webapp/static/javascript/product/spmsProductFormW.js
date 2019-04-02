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
        findAllProductType(data);
    }
})

$(function(){
	$("#taskId").val(getURLParam("taskId"))
	doQueryWF("reportInfo","searchDiv_wf");
})


function wf_beforeValid() {
	return $("#inputForm_2").valid();
}

function createData() {
	var dto = {
		spmsProductTypeDTO:{
			id:$("#addproducttypeid").val()
		},
		subscribeDate:$("#addsubscribeDate").val(),
		spmsUserDTO: {
			id:$("#wfuserId").val()
		},
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
function findAllProductType(datass) {
	/*-----取得所有产品类型信息-------*/
	doJsonRequest("/spmsProductType/getAll", null, function (data) {
		if (data.result) {
			producttypeinfo = data.data;
            var doubiIndex =0;
			for (var i = 0; i < data.data.length; i++) {
                if(datass.spmsProductTypeDTO.id == data.data[i].id) {
                    doubiIndex =i;
                    $("#addproducttypeid").append("<option value='" + data.data[i].id + "'>" + data.data[i].names + "</option>");
                }

			}
			$("#addproducttypeid").select2();
			$("#addproducttypeid").select("val", data.data[doubiIndex].id);
            $("#addproducttypeid").select2("readonly", true);
			/*--为初始的添加产品框赋值--*/
			$("#addproductId").val(data.data[doubiIndex].id);
			$("#addvalidPeriod").html(producttypeinfo[doubiIndex].validPeriodText);
			$("#addmuluId").html(producttypeinfo[doubiIndex].mulu);
			$("#addconfigurationInformation").html(producttypeinfo[doubiIndex].config);
			$("#addareaName").html(producttypeinfo[doubiIndex].areaName);
			if (producttypeinfo[doubiIndex].lianDong == 0) {
				$("#addlianDong").html("允许");
			} else {
				$("#addlianDong").html("不允许");
			}
			$("#addkongTiaoCount").html(producttypeinfo[doubiIndex].kongTiaoCount);
			$("#addchuangGanCount").html(producttypeinfo[doubiIndex].chuangGanCount);
			$("#addcountRmb").html(producttypeinfo[doubiIndex].countRmb);
			$("#addzhiLengMix").html(producttypeinfo[doubiIndex].zhiLengMix);
			$("#addzhiLengMax").html(producttypeinfo[doubiIndex].zhiLengMax);
			$("#addzhiReMix").html(producttypeinfo[doubiIndex].zhiReMix);
			$("#addzhiReMax").html(producttypeinfo[doubiIndex].zhiReMax);
			$("#adddescribes").html(producttypeinfo[doubiIndex].describes);
		} else {
			$.alert("产品类型获取失败");
		}
	});
}

function changeProperties(t) {
	var id = $(t).val();
	for (var i = 0; i < producttypeinfo.length; i++) {
		if (producttypeinfo[i].id == id) {
			$("#addproductId").val(id);
			$("#addvalidPeriod").html(producttypeinfo[i].validPeriodText);
			$("#addmuluId").html(producttypeinfo[i].mulu);
			$("#addconfigurationInformation").html(producttypeinfo[i].config);
			$("#addareaName").html(producttypeinfo[i].areaName);
			if (producttypeinfo[i].lianDong == 0) {
				$("#addlianDong").html("允许");
			} else {
				$("#addlianDong").html("不允许");
			}
			$("#addkongTiaoCount").html(producttypeinfo[i].kongTiaoCount);
			$("#addchuangGanCount").html(producttypeinfo[i].chuangGanCount);
			$("#addcountRmb").html(producttypeinfo[i].countRmb);
			$("#addzhiLengMix").html(producttypeinfo[i].zhiLengMix);
			$("#addzhiLengMax").html(producttypeinfo[i].zhiLengMax);
			$("#addzhiReMix").html(producttypeinfo[i].zhiReMix);
			$("#addzhiReMax").html(producttypeinfo[i].zhiReMax);
			$("#adddescribes").html(producttypeinfo[i].describes);
		}
	}
}
