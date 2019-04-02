var assignObj = {
	text:"",
	fun:function(data){
		C_doUserSelect({
			params:{
				userCodes:data.userCodes
			},
			myCallBack:function(datas){
				data.userCode = datas.code;
				doAssign(data);
			}
		});
	}
}
var claimObj = {
		text:"认领",
		fun:function(data){
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
			doClaim(data);
		}
}
/**
 * 表格操作项
 */
var oppObj = [assignObj,claimObj];

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
	window.location.href=data.approvalUrl+"&flag=show&isClaim="+data.flag+"&userCodes="+data.userCodes;
}

function createFun(){
	window.location.href="spmsWorkOrderForm.html";
}

//渲染表格
function initFun(data,key) {
	if(key=="workOrderCode") {
        //管理员也认领
		if(data.flag == 1) {
			clickmedTables.reportInfoQuery.operatorArr[0].text="指派";
			clickmedTables.reportInfoQuery.operatorArr[1].text="认领";
		} else  if(data.flag == 2){
        //管理员非认领
			clickmedTables.reportInfoQuery.operatorArr[0].text="指派";
            clickmedTables.reportInfoQuery.operatorArr[1].text="";
		} else if(data.flag == 3){
        //renling
            clickmedTables.reportInfoQuery.operatorArr[0].text="";
            clickmedTables.reportInfoQuery.operatorArr[1].text="认领";
        }
        var str = "<span style='color:red'>!&nbsp;</span>"+data['workOrderCode'];
        if(data.timeOutFlag == 1) {
            return str;
        }  else {
            return data['workOrderCode'];
        }
	}

}

function deleteFun(data1,data2) {
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

function doAssign(data) {
	var dto = {
		taskId:data.taskId,
		assignUserCode:data.userCode,
		businessKey:data.businessKey
	}
	doJsonRequest("/workflow/doAssingee",dto,function(data){
		setTimeout(function(){
			doQuery();
		},500)
	},{showWaiting:true,successInfo:'已成功指派',failtureInfo:'指派失败'})
}

function doClaim(data) {
	var dto = {
			taskId:data.taskId,
			businessKey:data.businessKey
	}
	doJsonRequest("/workflow/doClaim",dto,function(data){
		doQueryAfter("600");
	},{showWaiting:true,successInfo:'认领成功',failtureInfo:'认领失败'})
}