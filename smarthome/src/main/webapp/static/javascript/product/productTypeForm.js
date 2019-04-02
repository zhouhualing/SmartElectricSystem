$("#btnSubmit").click(function() {
	var flag= true;
	$(".temp").each(function(){
		if($(this).val()<15 || $(this).val()>35){
		flag =false;
		}
	});
	if(!flag){
		$.alert("温度在15-35℃之间");
		return;
	}
	if(!$("#muluId").val().length){
		$.alert("产品目录不能为空");
		return;
	}
	if(!$("#status").val().length){
		$.alert("产品类型状态不能为空");
		return;
	}
	if(!$("#validPeriod").val().length){
		$.alert("产品有效期限不能为空");
		return;
	}
	if(!$("#rmbType").val().length){
		$.alert("产品类型收付费方式不能为空");
		return;
	}
	if(!$("#lianDong").val().length){
		$.alert("传感器联动不能为空");
		return;
	}
	if(!$("#configurationInformation").val().length){
		$.alert("服务配置不能为空");
		return;
	}
	
	if($("#zhiLengMix").val()>$("#zhiLengMax").val()){
		$.alert("制冷最低温度应该小于等于其最高温度");
		return;
	}
	if($("#zhiReMix").val()>$("#zhiReMax").val()){
		$.alert("制热最低温度应该小于等于其最高温度");
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
		finishEditText($("#btnSubmit")[0],'basic','/spmsProductType/doSave','inputForm',function(data){
			$("#selectBizArea").hide();
			$("#selectEleArea").hide();
			//$("a#pagetitlename").html("订户详情");

			var userid = data.data.id;
			window.location.href="spmsProductTypeList.html";
		});
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
/**
 * 搜索产品区域
 * @param type
 */
function searchArea1(type) {
	var name = 'name';
	var id = 'id';
	C_doAreaSelect({
		"params" : {
			classification : type
		},
		myCallBack : function(data) {
			$("#areaId").val(data.id).trigger("keyup");
			$("#bizAreaName").val(data.name).trigger("keyup");
		}
	})
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
	alert($("#productId").val());
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
