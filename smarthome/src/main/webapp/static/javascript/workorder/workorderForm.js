wf_getOperator(WORKFLOW.openService,function(data){
	
});
getCurrenUserInfo(function(data){
	$("#createUser").html(data.userName);
})

function testAlert(classification) {
	C_doAreaSelect({
		"params" : {
			classification : classification,
		},
		myCallBack : function(data) {
			if (classification == 1) {
				$("#areaid").val(data.id).trigger("keyup");
				$("#areaname").val(data.name).trigger("keyup");
			}
			if (classification == 2) {
				$("#eleAreaId").val(data.id).trigger("keyup");
				$("#eleAreaName").val(data.name).trigger("keyup");
			}
		}
	})
}

function createData() {
	var d = {
		area : {
			id : $("#areaid").val()
		},
		eleArea: {
			id:$("#eleAreaId").val()
		},
		userType: $("#userType").val(),
		type : $("#type").val(),
		status : $("#status").val(),
		allottype : $("#allottype").val(),
		duration : $("#duration").val(),
		spmsUserName:$("#spmsUserName").val(),
		spmsUserMobile:$("#spmsUserMobile").val(),
		id :$("#myid").val(),
		exist:$("#exist").val(),
		userId:$("#userId").val(),
		idNumber:$("#idNumber").val(),
		address:$("#address").val(),
		email:$("#email").val(),
		meterNumber:$("#meterNumber").val(),
        spmsProductTypeDTO:{
            id:$("#addproducttypeid").val()
        }
	}
	return d;
}

function doCallBack(data) {
    if(data.message) {
        if(data.message.errorMsg) {
            $.alert(data.message.errorMsg)
        } else {
            window.location.href = window.location.href;
        }
    } else {
    	$.alert({
    		title: '提示信息',
    		msg: '操作成功',
    		height: 180,
    		confirmClick: function(){
    			window.location.href = window.location.href;
    		}
    	})
    }
}

function wf_beforeValid() {
	if ($("#type").val() == 1) {
		var flag = true;
		var dto = {
			"mobile" : $("#spmsUserMobile").val()
		};
		doJsonRequest("/businessChange/getUserByWorkOrder", dto, function(data) {
			var usr = data.data;
			if (usr.msg) {
				$.alert("该手机号已开通业务");
				flag=false;
				return;
			}
		}, {
			async : false
		});
		if (!flag) {
			return flag;
		}
		doJsonRequest("/businessChange/getUserDetails", dto, function(data) {
			var usr = data.data;
			if (typeof (usr.gw_id) != "undefined" && usr.gw_id != ""
					&& usr.gw_id != null) {
				$.alert("该手机是系统用户已绑定网关，不能业务开通");
				flag=false;
				return;
			}
		}, {
			async : false
		});
		
		if (!flag) {
			return flag;
		}

	}
	if($("#type").val()!=1){
		var flag=true;
		var dto={"mobile":$("#spmsUserMobile").val()};		
		doJsonRequest("/businessChange/getUserDetails",dto,function(data){
			var usr=data.data;
			if(typeof(usr.fullname) == "undefined"){
				$.alert("该手机不是系统用户，请核实");
				flag=false;
				return;
			}
			if(typeof(usr.gw_id) == "undefined" || usr.gw_id == "" || usr.gw_id == null){
				$.alert("该用户未绑定网关！");
				flag=false;
				return;
			}
				},{showWaiting : false, async : false});
				if(!flag){
				return flag;
				}
			}
    if($("#type").val().length <= 0) {
        $.alert("请选择工单类型。");
        return false;
    }
    if($("#spmsUserMobile").val().length <= 0) {
    	$.alert("请输入订户手机号");
    	return false;
    }else{
    	var mobile = $("#spmsUserMobile").val();
    	var r = false;
    	doJsonRequest("/spmsUser/validateUser", {
    		mobile : mobile
    	}, function(data) {
    		var su = data.data;
    		if (!su.isValidate) {
    			$.alert("该订户已冻结！");
    			r = false;
    		}else{
    			r = true;
    		}
    	}, {
    		showWaiting : true,
    		async : false
    	});
    	if (!r) {
    		return false;
    	}
    }
    
    //用电区域临时验证
    if($("#eleAreaName").val() == ""){
    	$.alert("请选择用电区域。");
		return false;
    }
    if($("#type").val() != 2 && $("#type").val() != 4){
    	if($("#addproducttypeid").val() == "") {
    		$.alert("请选择产品类型。");
    		return false;
    	}
    }
    if($("#type").val() == 4 && $("#bxCause").val() == ""){
    	$.alert("请选择报修项");
    	return false;
    }
    if($("#type").val() == 5 && $("#tdReason").val() == ""){
    	$.alert("请选择退订原因");
    	return false;
    }
    if($("#type").val() == 5 && ($("#orderProductID").val() == "")){
    	$.alert("请选择已订购产品");
    	return false;
    }
    if($("#type").val() == 2 && ($("#changeProductID").val() == "" && $("#orderProductID").val() == "")){
    	$.alert("请选择已订购产品、更换产品");
    	return false;
    }
    if($("#type").val() == 2 && $("#orderProductID").val() != "" && $("#changeProductID").val() != ""){
    	var id=$("#orderProductID").val();
    	for(var i=0;i<products.length;i++){
    		if(products[i].id==id){
    			if($("#changeProductID").val() == products[i].typeId){
    				$.alert("已订购产品和更换产品不能相同");
    				return false;
    			}else{
    				break;
    			}
    		}
    	}
    }
   /* if($("#type").val() == 5){
    	var id=$("#orderProductID").val();
    	var result = true;
		doJsonRequest("/spmsProduct/getInfo",{id:id},function(data){
			var product=data.data;
			if(product.expireDate < new Date().getTime()){
				$.alert("该产品未到期，不能退订");
				result = false;
			}
		},{showWaiting : true,async:false});
		if(!result){
    		return result;
    	}
	}*/
    //增加技术支持对订户拥有产品的验证
    if($("#type").val() == 4){
    	var spmsUserMobile = $("#spmsUserMobile").val();
    	var result = true;
    	doJsonRequest("/spmsUser/getByMobile;",{mobile:spmsUserMobile},function(data){
			var su=data.data;
			if(!su.hasProduct){
				$.alert("该订户没有订购产品，不能进行技术支持！");
				result =false;
			}
		},{showWaiting : true,async:false});
    	if(!result){
    		return result;
    	}
    }
	return $("#inputForm").valid();
}

function wf_getMark() {
	return $("#mark").val();
}
$(function(){
    findAllProductType();
   	mobileChangeListener();
})
/*-----时刻监听手机号的输入-------*/
function mobileChangeListener(){
 	$('#spmsUserMobile').bind('input propertychange', function() {
     	var re = /^1\d{10}$/
    	if (re.test( $("#spmsUserMobile").val())) {
				setUserDetail();
		}
    
    }); 

}
function findAllProductType() {
    /*-----取得所有产品类型信息-------*/
    doJsonRequest("/spmsProductType/getAll", null, function (data) {
        if (data.result) {
            for (var i = 0; i < data.data.length; i++) {
                $("#addproducttypeid").append("<option value='" + data.data[i].id + "'>" + data.data[i].names + "</option>");
            }
        } else {
            $.alert("产品类型获取失败");
        }
    });
}