if(getURLParam("flag") == "show") {
	$("#inputForm_2").hide();
    if(getURLParam("isClaim") == 1) {
        $("#btnCliam").show();
        $("#btnAssign").show();
    } else  if(getURLParam("isClaim") == 2){
        //管理员非认领
        $("#btnAssign").show();
    } else if(getURLParam("isClaim") == 3){
        //renling
        $("#btnCliam").show();
    }
    $("#the_name").html("未受理工单");
} else {
    $("#the_name").html("待办工单");    
	wf_getOperator({taskId:getURLParam("taskId")});
}

var kongTiaoCount=0;
var chuanGanCount=0;

//初始化工单基本信息之后 设置默认值
initWorkOrderInfo(function(data){
    kongTiaoCount = data.spmsProductTypeDTO.kongTiaoCount;
    chuanGanCount = data.spmsProductTypeDTO.chuangGanCount;
});

$(function(){
	$("#taskId").val(getURLParam("taskId"));
	doQueryWF("reportInfo","searchDiv_wf");
	//getProductInfo();
})

/*******保存前验证************/
function wf_beforeValid() {
	var dto = {
			"addDevices" : getAddDevices(),
			"processInstanceId" : getURLParam("processInstanceId"),
			"taskId" : getURLParam("taskId")
			};
	$("#addDeviceTable tr").each(
			function(){
				checkDevice($(this).find("td").eq(1).find("input").eq(0));
			}
	);
	var flag = false;
	doJsonRequest("/businessChange/countUserDevices",dto,function(data){
		if(!data.data.success){
			$.alert(data.data.msg);
			flag = false;
		}else{
			flag = true;
		}
	},{showWaiting : true, async : false});
	return flag;
}
/**********取出要保存的数据**********/

function getAddDevices(){
	var addDevices=[];
	$("#addDeviceTable tr").each(
			function(){
				var mac=$(this).find("td").eq(1).find("input").eq(0).val();
				if(mac!=null && mac!=""){
					addDevices.push({"deviceTypeId":$(this).find("td").eq(0).find("select").eq(0).val(),"deviceMac":mac});
				}
			}
	);
	$("#cancleDeviceTable tr").each(
			function(){
				var mac=$(this).find("td").eq(2).text();
				if(mac!=null && mac!=""){
					addDevices.push({"deviceTypeId":$(this).find("td").eq(3).text(),"deviceMac":$(this).find("td").eq(2).text()});
				}
			}
	);
	return addDevices;
}

function createData() {
	var d={
		"delDevices":delDevices,	
		"addDevices":getAddDevices(),
		"processInstanceId":getURLParam("processInstanceId"),
		"taskId":getURLParam("taskId")
	}
	return d;
}

function doCallBack(){
	$.alert({
		title: '提示信息',
		msg: '操作成功',
		height: 180,
		confirmClick: function(){
			history.go(-1);
		}
	});
}

function wf_getMark() {
	return $("#mark").val();
}

var selecti = 0;

function addDevice(){
	var tr= "<tr>"+
                " <td>"+
                     " <select id='select" + selecti + "' name='deviceType' class='the_select2' style='width:187px;'>"+
                         "  <option value='2'>智能空调</option>"+
                          " <option value='3'>门窗传感器</option>"+
                     " </select>"+
                " </td>"+
                " <td><input type='text' name='deviceMac' placeholder='请输入mac地址' onblur='checkDevice(this)'/><span name='errorInfo'></span></td>"+
              " </tr>";
	$("#addDeviceTable").append(tr);
	$("#select"+selecti).select2();
	selecti++;
}
//检查输入的mac地址是否存在
function checkDevice(obj){
	var flag=false;
	var mac=$(obj).val();
	//var par=$(obj).parent().parent();
	var type=$(obj).parent().parent().find("select").eq(0).val();
	//alert(mac);
	if(mac!=null&& mac!=""){
		var dto={
			"deviceMac":mac,
			"deviceTypeId":type
		}

		 $.ajax({
		        type: "POST",
		        url: clickmedBaseUrl + clickmedAdminPath + "/businessChange/checkDevice",
		        timeout: 600000,
		        data: JSON.stringify(dto),
		        async: false,
		        dataType: "json",
		        contentType: "application/json",
		        success: function(data) {
		        	if(data==null){
		        		$(obj).val("");
		        		alert("输入的mac地址不存在或者已经被使用");
		        	}else{
		        	flag=true;
		        	}
		        }
		 })
		 return flag;
	}
	
}


var delDevices=[];	
function showDelDevice(row){
	hmBlockUI();
	var dto = {};
	doJsonRequest("/businessChange/listStorage",dto,function(data){
		var div =  '<div id=\"delDiv\" style=\" position: absolute;background: rgb(64,69,78);width: 360px;top: 40%;left: 35%;z-index: 10001;text-align:center;line-height:33px;height:100px;\">请选择要存入的仓库：<br/>'+
		'<select id=\"delSelect\" style=\"width:125px;z-index:10002\">';
		for(var i = 0;i<data.data.storage.length;i++){
			var item = data.data.storage[i];
			div = div + '<option value=\"' + item.code + '\">' + item.value + '</option>';
		}
		div = div + '</select><input type=\"button\" style=\"margin-left:5px;\" class=\"btn btn-primary\" value=\"确定\" onclick=\"delDevice('+row+')\">' +
		'<input type=\"button\" style=\"margin-left:5px;\" class=\"btn btn-primary\" value=\"取消\" onclick=\"cancelDelDevice()\">'+
		'</div>';
		$("body").eq(0).append(div);
		$("#delSelect").select2();
	});
}

function cancelDelDevice(row){
	$("#delDiv").remove();
	hmUnBlockUI();
}
function delDevice(row){
	var deltr=$("tr[row="+row+"]");
	var delDevice={"deviceId":deltr.find("td").eq(0).html(),"deviceMac":deltr.find("td").eq(2).html(),storage:$("#delSelect").val(),deviceTypeId:deltr.find("td").eq(3).html()};
	delDevices.push(delDevice);
	$("tr[row="+row+"]").remove();
	$("#delDiv").remove();
	hmUnBlockUI();
}

/***获取用户当前设备信息信息**********/
$(function(){
	var dto={
		processInstanceId:getURLParam("processInstanceId")
	}
	doJsonRequest("/businessChange/getUserDevices",dto,function(data){
		var devices=data.data;
		for(var i=0;i<devices.length;i++){
			var tr='<tr row='+i+'>'+
			   '<td style="display:none">'+devices[i].device_id+'</td>'+
			   '<td>'+devices[i].typename+'</td>'+
			   '<td>'+devices[i].mac+'</td>'+
			   '<td style="display:none">'+devices[i].typeid+'</td>'+
			   '<td>'+  
			   '<input  class="btn btn-primary" type="button" value="回收设备" onClick="showDelDevice('+i+')"/>'+  
			   '</td>'+
			  '</tr>';
			$("#cancleDeviceTable").append(tr);
		}		
	});
})
function testSave(){
	doJsonRequest("/businessChange/saveChangeDevices",createData(),function(data){
		alert("ok");
	})
}
/******************获取变更的设备信息********************/
