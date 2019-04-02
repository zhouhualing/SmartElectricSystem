/**
 * 表格操作项
 */
var oppObj = [];
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
		var r = false;
    	doJsonRequest("/spmsWorkOrder/validateUser", {
    		processInstanceId : data.processInstanceId
    	}, function(data) {
    		var su = data.data;
    		if (!su.isValidate) {
    			$.alert("该工单所属订户已冻结！");
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
	window.location.href=data.approvalUrl;
}

function createFun(data){
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

function initFun(data,key) {
    if(key == "workOrderCode") {
        var str = "<span style='color:red'>!&nbsp;</span>"+data['workOrderCode'];
        if(data.timeOutFlag == 1) {
            return str;
        }  else {
            return data['workOrderCode'];
        }
    }
}
