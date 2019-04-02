//初始化信息
$(function () {
    //编辑
        var dto = {
            configName:"logTime"
        };
        doJsonRequest("/systemConfig/getSystemConfig", dto, function (data) {
        	$("#configValue").val(data.data.configValue);
        	$("#configValueS").html(data.data.configValue);
        }, {showWaiting: true});

        dto = {
        		configName:"logTimeD"
        };
        doJsonRequest("/systemConfig/getSystemConfig", dto, function (data) {
        	$("#configValue1").val(data.data.configValue);
        	$("#configValueS1").html(data.data.configValue);
        }, {showWaiting: true});

        dto = {
        		configName:"openGetWay"
        };
        doJsonRequest("/systemConfig/getSystemConfig", dto, function (data) {
        	$("#configValue2").val(data.data.configValue);
        	$("#configValueS2").html(data.data.configValue);
        }, {showWaiting: true});
});

function doEdit1() {
    //方式一
    //$("#inputForm").submit();
    //方式二
    var data = {
    	configValue:$("#configValue").val()
    };
    data["configName"] = "logTime";
    reg= /^[0-9]*[1-9][0-9]*$/; 
    if(!reg.test(data["configValue"])){    
        $.alert("对不起，您输入的数字格式不正确!请输入正整数。");//请将“整数类型”要换成你要验证的那个属性名称！    
        return;
    }
    //  console.log(data);
    doJsonRequest("/systemConfig/saveSystemConfig", data, function (data) {
        location.href = "log_time_config.html";
    }, {showWaiting: true});

}

function doEdit2() {
	//方式一
	//$("#inputForm").submit();
	//方式二
	var data = {
		configValue:$("#configValue1").val()
	}
	data["configName"] = "logTimeD";
	reg= /^[0-9]*[1-9][0-9]*$/; 
	if(!reg.test(data["configValue"])){    
		$.alert("对不起，您输入的数字格式不正确!请输入正整数。");//请将“整数类型”要换成你要验证的那个属性名称！    
		return;
	}
	//  console.log(data);
	doJsonRequest("/systemConfig/saveSystemConfig", data, function (data) {
		location.href = "log_time_config.html";
	}, {showWaiting: true});
	
}
function doEdit3() {
	//方式一
	//$("#inputForm").submit();
	//方式二
	var data = {
			configValue:$("#configValue2").val()
	}
	data["configName"] = "openGetWay";
	reg= /^[0-9]*[1-9][0-9]*$/; 
	if(!reg.test(data["configValue"])){    
		$.alert("对不起，您输入的数字格式不正确!请输入正整数。");//请将“整数类型”要换成你要验证的那个属性名称！    
		return;
	}
	//  console.log(data);
	doJsonRequest("/systemConfig/saveSystemConfig", data, function (data) {
		location.href = "log_time_config.html";
	}, {showWaiting: true});
	
}
