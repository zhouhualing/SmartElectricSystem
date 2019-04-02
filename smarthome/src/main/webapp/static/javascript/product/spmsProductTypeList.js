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

/**
 * ready
 */
$(function(){
    doQuery();
	//select2组件注册
	$(".the_select2").addChangeListener(function(){
		doQuery();
	})
	
    $("#checkAll").on('click',function(){
    	alert("aaaa");
        $("[name=checkId]:checkbox").attr("checked",this.checked);
    });

    $("#delBtn").click(function(){
        var checkIds = $("[name=checkId]:checked");
        if(checkIds.length==0){
            alert("请至少选择一条数据");
            return ;
        }
        var ids = [];
        checkIds.each(function(index,e){
            ids.push($(e).val());
        });
        confirmx('确认要删除所选条目吗？', "/spms/a/spms/spmsProduct/delete?id="+ids.join(','));
    });
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
	window.location.href="spmsProductTypeDetail.html?id="+data.id;
	
}
//产品类型添加按钮触发函数
function createFun(){
	window.location.href="spmsProductTypeForm.html";
}
//产品类型逻辑删除方法
function deleteFun(data1,data2) {
	if(data1.length <=0) {
		$.alert("请至少选择一个产品类型。");
		return false;
	}
	var config = {
		msg: "您确定要删除么？",
		confirmClick: function () {
			doJsonRequest("/spmsProductType/deleteProductAll",data1,function(data){
				if(data.data.success==true) {
					window.location.href="spmsProductTypeList.html";
				} else {
					$.alert(data.data.msg);

				}
			})
		}
	}
	$.confirm(config);
}
