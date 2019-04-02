var claimObj = {
		text:"指派",
		fun:function(data){
			doAssign(data.taskId,data.userCode,data.businessKey);
		}
}
/**
 * 表格操作项
 */
var oppObj = [claimObj];

var classification = 1;
/**
 * 处理url参数
 */
if(getURLParam("classification") != null) {
	classification = getURLParam("classification");
	doReQuery(classification)
}


function testAlert() {
		C_doAreaSelect({
		params:{
			classification:1
		},
		myCallBack:function(data){
			var areaname = data.name;
			var areaid = data.parentIds;
			$("#areaname").val(areaname);
			$("#parentIds").val(areaid);
			doQuery();
		}
	})
}

/**
 * ready
 */
$(function(){
	doQuery()
	//select2组件注册
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
        confirmx('确认要删除所选条目吗？', "/spms/a/spms/spmsDevice/delete?id="+ids.join(','));
    });
})

/**
 * 点击tab栏，动态过滤设备类型信息
 * @param type
 */
function doReQuery(type) {
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
	window.location.href="spmsWorkOrderDetail.html?id="+data.businessKey;
}

function createFun(){
	window.location.href="spmsWorkOrderForm.html";
}

function deleteFun(data1,data2) {
	//alert(data1);
	//alert($.toJSON(data2));
	//alert("删除成功")
	if(data1.length == 0){
		$.alert("请至少勾选一项");
		return ;
	}
	var info = {
		ids:data1.join(",")
	}
	
	doJsonRequest("/spmsWorkOrder/batchDelete", info, function(data) {
				if (data.result) {
					if (data.data.success) {
						$.alert(data.data.msg);
						window.location.href = "spmsWorkOrderList.html";
					} else {
						$.alert(data.data.msg);
					}
				}
			}, {
				showWaiting : true
			})
}


function doAssign(taskId,userCode,businessKey) {
	var dto = {
			taskId:taskId,
			assignUserCode:userCode,
		    businessKey:businessKey
	}
	doJsonRequest("/workflow/doAssingee",dto,function(data){
		setTimeout(function(){
			doQuery();
		},500)
	},{showWaiting:true,successInfo:'已成功指派',failtureInfo:'指派失败'})
}