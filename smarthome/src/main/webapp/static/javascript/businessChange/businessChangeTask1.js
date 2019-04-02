/***获取用户欠费信息**********/
var products=[];
function setUserDetail(){
	if($("#type").val()==1){
		var dto={"mobile":$("#spmsUserMobile").val()};	
		doJsonRequest("/businessChange/getUserDetails",dto,function(data){
			var usr=data.data;
			if(!usr.msg){
				$("#exist").val("");
				$("#userId").val("");
				$("#spmsUserName").val("");
				$("#idNumber").val("");
				$("#address").val("");
				$("#email").val("");
				$("#meterNumber").val("");
				$("#areaid").val("");
				$("#eleAreaId").val("");
				$("#areaname").val("");	
				$("#eleAreaName").val("");	
				return;
			}
			if(typeof(usr.gw_id) != "undefined" && usr.gw_id != "" && usr.gw_id != null){
				$.alert("该手机是系统用户已绑定网关,不能业务开通");
				return;
			}
			$("#exist").val("isExist");
			$("#userId").val(usr.id);
			$("#spmsUserName").val(usr.fullname);
			$("#idNumber").val(usr.idNumber);
			$("#address").val(usr.address);
			$("#email").val(usr.email);
			$("#meterNumber").val(usr.ammeter);
			$("#areaid").val(usr.biz_area_id);
			$("#eleAreaId").val(usr.ele_area_id);
			$("#userType").select2('val', usr.type);		
			$("#areaname").val(usr.bizAreaName);	
			$("#eleAreaName").val(usr.eleAreaName);	
		},{async : false});
		doJsonRequest("/businessChange/getUserByWorkOrder", dto, function(data) {
			var usr = data.data;
			if (usr.msg) {
				$.alert("该手机号已开通业务");
				return;
			}
		}, {
			async : false
		});
	}
	if($("#type").val()!=1){
		var dto={"mobile":$("#spmsUserMobile").val()};		
		doJsonRequest("/businessChange/getUserDetails",dto,function(data){
			var usr=data.data;
			if(typeof(usr.fullname) == "undefined"){
				$.alert("该手机不是系统用户，请核实");
				return;
			}
			if(typeof(usr.gw_id) == "undefined" || usr.gw_id == "" || usr.gw_id == null){
				$.alert("该用户未绑定网关！");
				return;
			}
			$("#spmsUserName").val(usr.fullname);
			$("#idNumber").val(usr.idNumber);
			$("#address").val(usr.address);
			$("#email").val(usr.email);
			$("#meterNumber").val(usr.ammeter);
			$("#areaid").val(usr.biz_area_id);
			$("#eleAreaId").val(usr.ele_area_id);
			$("#userType").select2('val', usr.type);		
			$("#areaname").val(usr.bizAreaName);	
			$("#eleAreaName").val(usr.eleAreaName);	
		},{async : false});
		
		//设置已购产品
		doJsonRequest("/businessChange/getUserProduct",dto,function(data){
			products=data.data;
			$("#orderProductID").empty();
			var option1="<option value=''>请选择</option>";
			$("#orderProductID").append(option1);
			for(var i=0;i<products.length;i++){
				var option="<option value='"+products[i].id+"'>"+products[i].names+"</option>";
				$("#orderProductID").append(option);
			}
		})		
	}
}
//设置更换产品
function setChangeProduct(){
	var dtob={};
	doJsonRequest("/businessChange/getProducts",dtob,function(data){
		var product=data.data;
		for(var i=0;i<product.length;i++){
			var option="<option value='"+product[i].id+"'>"+product[i].names+"</option>";
			$("#changeProductID").append(option);
		}			
	})
}

function setProductType(){
	//alert("ok");
	var id=$("#orderProductID").val();
	//alert(id);
	for(var i=0;i<products.length;i++){
		if(products[i].id==id){
			$("#addproducttypeid").val(products[i].typeId);
			//alert($("#addproducttypeid").val());
		}
	}
	setStartDate(id);
}
/*function setStartDate(id){
	var type = $("#type").val();
	if(type == 5){
		doJsonRequest("/spmsProduct/getInfo",{id:id},function(data){
			var product=data.data;
			if(product.expireDate < new Date().getTime()){
				$.alert("该产品未到期，不能退订");
				$("#orderProductID").val("");
				return;
			}
			$("#startDate").val(product.actDate);
		})
	}
}*/
//产品未到期也可退订
function setStartDate(id){
	var type = $("#type").val();
	if(type == 5){
		doJsonRequest("/spmsProduct/getInfo",{id:id},function(data){
			var product=data.data;
				$("#startDate").val(product.actDate);
		});
	}
}
