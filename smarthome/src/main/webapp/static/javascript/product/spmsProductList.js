//产品保存方法
$(document).ready(function() {
		 $("#type").select2("readonly", true); 
			$("#btnSubmit").click(function(){
				
				xiaoYan();	
				
				$("#inputForm").submit();
		            });
			
			
			$("#inputForm").validate({submitHandler:function(){
				
				var dto = {
						spmsProductTypeDTO:{
							id:$("#spmsProductType").val()
						},
						status:$("#status").val(),
						spmsUserDTO:{
							id:$("#spmsUser").val()
						},
	 					subscribeDate:$("#subscribeDate").val(),
 						activateDate:$("#activateDate").val(),
 						expireDate:$("#expireDate").val(),
 						initialCostRmb:$("#initialCostRmb").val(),
 						electricityCostRmb:$("#electricityCostRmb").val(), 
 						spmsDeviceDTO:{
 							id:$("#spmsDevice").val()
 							}
						
				}
				doJsonRequest("/spmsProduct/doSave",dto,function(data){
					if(data.result) {
						
						window.location.href="spmsProductList.html";
					} else {
						alert("保存失败")
					}
				})
			}});
		//校验方法	
	function xiaoYan(){
		var x=document.getElementById("initialCostRmb").value;
		var y=$("#electricityCostRmb").val();
		if(x==""||isNaN(x)||y==""||isNaN(y))
			{
			
			return alert("价格格式不对，请重新输入");
			}
		}
	
/*
	 $("#spmsProductType").blur(function (){
		 var pTypeName=$("#spmsProductType").val();
		 alert(pTypeName);
		 $("#spmsProductTypeNames").html(pTypeName);
	 })
 */
});
	
/**
 * 表格操作项
 */
var oppObj = [];

/**
 * 处理url参数
 */
if(getURLParam("type") != null) {
	doReQuery(getURLParam("type"))
}

$(function(){
	
	
})
/**
 * ready
 */
$(function(){
findAllProductType() ;
    doQuery();
	//select2组件注册
	$(".the_select2").addChangeListener(function(){
		doQuery();
	})
	
})

/**
 * 点击tab栏，动态过滤设备类型信息
 * @param type
 */
function doReQuery(type) {
	$("#type").val(type);
	$(".active").removeClass("active");
	$("li:even",$(".nav")).eq(type-1).attr("class","active");
	doQuery();
}

/**
 * table组件，当row被点击时执行操作
 * @param a
 * @param b
 */
function rowClick(data,tr) {
	
	window.location.href="spmsProductDetail.html?id="+data.id;
}

function createFun(){
	window.location.href="spmsProductForm.html";
}
//产品列表删除方法
function deleteFun(data1,data2) {
	/*alert(data1);
	alert($.toJSON(data2));*/
	//alert(data1);
	doJsonRequest("/spmsProduct/deleteAll",data1,function(data){
		if(data.result==true) {
			
			
			window.location.href="spmsProductList.html";
		} else {
			$.alert("删除失败");
			
		}
	})
	
}
function findAllProductType() {
    /*-----取得所有产品类型信息-------*/
    doJsonRequest("/spmsProductType/getAll", null, function (data) {
        if (data.result) {
            for (var i = 0; i < data.data.length; i++) {
                $("#spmsProductType").append("<option value='" + data.data[i].names + "'>" + data.data[i].names + "</option>");
            }
        } else {
            $.alert("产品类型获取失败");
        }
    });}
