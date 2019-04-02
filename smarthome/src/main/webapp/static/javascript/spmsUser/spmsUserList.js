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

function testAlert() {
	var str = "checkType=multi";

	var obj = {
	    title:'流程信息',
	    height:"320px",
	    width:"1050px",
	    url:'follow_role_dialog.html',
	    fun:true,
	    myCallBack:function(){
	    	
	    }
	}
	nowSearchRole = new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}
$("#bizAreaButton").on("click",function(){
	C_doAreaSelect({
		params:{
			classification:1
		},
		myCallBack:function(data){
			$("#bizAreaParentId").val(data.parentIds);
			$("#bizAreaName").val(data.name);
			doQuery();
		}
	})
})

$("#eleAreaButton").on("click",function(){
	C_doAreaSelect({
		params:{
			classification:2
		},
		myCallBack:function(data){
			$("#eleAreaParentId").val(data.parentIds);
			$("#eleAreaName").val(data.name);
			doQuery();
		}
	})
})

/**
 * ready
 */
$(function(){
// $.alert("111");
	doQuery();
	// select2组件注册
	$(".the_select2").addChangeListener(function(){
		doQuery();
	})
    $("#checkAll").on('click',function(){
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
        confirmx('确认要删除所选条目吗？', "/spms/spmsDevice/doDel?id="+ids.join(','));
    });
})

/**
 * 点击tab栏，动态过滤设备类型信息
 * 
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
 * 
 * @param a
 * @param b
 */
function rowClick(data,tr) {
	window.location.href="spmsUserDetail.html?id="+data.id;
}

function createFun(){
	//hh
	window.location.href="spmsUserForm.html";
}
//批量删除
function deleteFun(data, data1) {
	if(data.length <= 0) {
		$.alert("请选择要冻结的订户。");
		return false;
	}

	var config = {
		msg: "您确定要冻结么？",
		confirmClick: function () {
			var dto = {ids: data +""};
			doJsonRequest('/spmsUser/doDels',dto,function(data) {
				if (data.result==true) {
					$.alert("冻结成功!");
					window.location.href="spmsUserList.html";
				}else{
					$.alert("冻结失败！");
				}
			});
		}
	}
	$.confirm(config);
}

function initFun(data,key) {
	if(key =="productSize") {
		if(data[key] != null) {
			return "已订购";
		} else {
			return "未订购";
		}
	}
}
function importFun(){
	var obj = {
		title : '选择文件进行导入',
		height : "320px",
		width : "600px",
		url : '../spmsUser/spmsUserImport.html',
		confirmBtn : false,
		cancelText : "关闭",
		myCallBack : function(){
			
		}
	}
	new jqueryDialog(obj);
}