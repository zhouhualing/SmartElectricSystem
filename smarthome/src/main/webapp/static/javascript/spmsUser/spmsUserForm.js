$("#basicComplete").click(function() {
	var type = $("#type").val();
	if(type=="" || type ==null){
		$.alert("请选择订户类型");
		return;
	}
	$("#inputForm").submit();
});

$("#basicCancel,#basicComplete").on("click",function(){
	$("#searchCompanyBtn").hide();
	$("#searchOrgBtn").hide();
})

$("#basicEdit").on("click",function(){
	$("#selectBizArea").show();
	$("#selectEleArea").show();
})

if(getURLParam("id") == null) {
	$("#basicEdit").trigger("click");
}




$("#inputForm").validate({
	submitHandler : function(form) {
//		var dto = {
//			fullname : $("#fullname").val(),
//			type : $("#type").val(),
//			bizArea : $("#bizArea").val(),
//			eleArea : $("#eleArea").val(),
//			address : $("#address").val(),
//			mobile : $("#mobile").val(),
//			email : $("#email").val(),
//			ammeter : $("#ammeter").val(),
//			updateFlag : 0,
//		};
//		doRequest("/spmsUser/doSave", dto, function(data) {
//			if (data.data.id != null) {
//				$("#id").val(data.data.id);
//				$("div",$("#fieldsetUser")).each(function(){
//					for(var key in data) {
//						if($(this).attr("id") == (key+"S")) {
//							$.alert(data[key]);
//							$(this).html(data[key]);
//						}
//					}
//				});
//
//			}
//		},{showWaiting:true,successInfo:"保存成功",failtureInfo:"保存失败"});
		var dat={
				userid:$("#id").val(),
				mobile:$("#mobile").val()
		}
		
		doJsonRequest('/spmsUser/ValidationMobile', dat, function(data) {
			if(data.result){
				if(data.data.success){
					finishEditText($(".edit_complete")[0],'basic','/spmsUser/doSave','inputForm',function(data){
						$("#selectBizArea").hide();
						$("#selectEleArea").hide();
						//$("a#pagetitlename").html("订户详情");
						var userid = data.data.id;
						window.location.href="spmsUserDetail.html?id="+userid;
					});
				}else{
					$.alert(data.data.msg);
				}
			}else{
				$.alert("保存失败");
			}
		},{showWaiting:true,successInfo:'添加成功',failtureInfo:'添加失败'});
	}
});
//订户绑定网关
function bindingGateway() {
	$("#gatewayComplete").click(function() {
		$("#command").submit()
	});
	$("#command").validate({
		submitHandler : function(form) {
			var dto = {
				id : $("#id").val(),
				spmsDevice : $("#gateway").val(),
				updateFlag : 1,
			};
			doRequest("/spmsUser/doSave", dto, function(data) {
				if (data.data.id != null) {
					alert("保存成功!");
					$("#id").val(data.data.id);
				} else {
					alert("保存失败!");
				}
			});
		}
	});
}

function saveProduct(){
	$("#productAddComplete").click(function() {
		$("#addProductForm").submit();
	});
	$("#addProductForm").validate({
		submitHandler : function(form) {
			var dto = {
				userId : $("#id").val(),
				spmsUserDTO : {
					id : $("#id").val(),
				},
				spmsProductTypeDTO : {
					id : $("#addServiceId").val(),
				},
				subscribeDate : $("#addServiceStartTime").val(),
				expireDate : $("#addServiceEndTime").val(),
				status : 1,
				updateFlag : 0,
				
			};
			doJsonRequest("/spmsProduct/doSave", dto, function(data) {
				if (data.result) {
					alert("保存成功!");
					$("#productId").val(data.data.id);
				} else {
					alert("保存失败!");
				}
			});
		}
	});
}
//产品绑定设备
function bindingDevice(){
	var dto = {
			id : $("#productId").val(),
			spmsUserDTO : {
				id : $("#id").val(),
			},
			spmsDeviceDTO : {
				type : $("#deviceType").val(),
				mac : $("#deviceMac").val(),
			},
			updateFlag : 1,
	}
	doJsonRequest("/spmsProduct/doSave", dto, function(data) {
		if (data.result) {
			alert("保存成功!");
		} else {
			alert("保存失败!");
		}
	});
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

function startServiceTime(endTime) {
	var meal = $('#addServiceId option:selected').attr('serviceTypeNum');
	var month = 0;
	var year = 0;
	if (meal == '1')
		month = 3;
	else if (meal == '2')
		month = 6;
	else if (meal == '3')
		year = 1;
	WdatePicker({
		dateFmt : 'yyyy-MM-dd',
		isShowClear : false,
		onpicked : function() {
			var endDate = CalculateDate($dp.cal.getP('y'), $dp.cal.getP('M'),
					$dp.cal.getP('d'), year, month, '-1');
			$('#' + endTime).val(endDate);
		}
	});
}

/* 判断套餐时间是否已经开始使用 */
function checkEndTime(startTime) {
	var start = new Date(startTime.replace("-", "/").replace("-", "/"));
	var curreTime = new Date();
	if (curreTime < start) {
		return false;
	}
	return true;
}
var dataJson;
function findAllProductType() {
	doRequest('/spmsProductType/getAll', null, function(data) {
		dataJson = data.data;
		if (data.result) {
			$.each(data.data, function(index, content) {
				$("#addServiceId").append(
						"<option value='" + content.id + "'>" + content.names
								+ "</option>");
			});
		}
	});
}
function changeProperties() {
	var perDto = null;
	for(var i=0;i<dataJson.length;i++) {
		perDto = dataJson[i];
		if(perDto.id==$("#addServiceId").val()){
			$("#addServiceType").html(perDto.validPeriod);
			//$("#addServiceStyle").html($("option",$("#areaType")).eq(perDto.muluId-1).html());
			$("#addServiceStyle").html(perDto.muluDictValue);
			$("#addServiceMode").html(perDto.configDictValue);
		}
	}
}

function bingGw(t){
	var userid = $("#id").val()
	if(userid=="" || userid ==null || typeof(userid) == "undefined"){
		$.alert("请先保存订户信息");
	}
}

function addProductForm(t){
	var userid = $("#id").val();
	if(userid=="" || userid ==null || typeof(userid) == "undefined"){
		$.alert("请先保存订户信息");
		return ;
	}
	var gwid = $("#gateway").val();
	if(gwid =="" || gwid ==null ||typeof(gwid) == "undefined"){
		$.alert("请先添加绑定网关");
		return ;
	}
	
	//$('#addProductForm').show();
}
