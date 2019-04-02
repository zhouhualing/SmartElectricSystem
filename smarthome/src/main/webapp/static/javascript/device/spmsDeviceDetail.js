//为页面的id进行赋值。
$("[id='id']").val(getURLParam("id"));
var deviceId = getURLParam("id");
var type = getURLParam("type");
var rbtn = 1;
$(function () {


	$("#storage").select2("readonly", true);
	$("#status").select2("readonly", true);

	// 去除导航active样式并重新赋值
	$(".active").removeClass("active");
	$("#li" + type).attr("class", "active");
	// 为设备类型进行赋值
	if (type == 1) {
		$("#type").html("网关");
	} else if (type == 2) {
		$("#type").html("空调");
	} else {
		$("#type").html("门窗传感器");
	}

	var dto = {
		id : getURLParam("id"),
		rbtn:rbtn
	};

	doJsonRequest("/spmsDevice/getInfo", dto, function(data) {
		if (data.result) {
			var data = data.data;
			$("div", $("#fieldset1")).each(function() {
				for ( var key in data) {
					if ($(this).attr("id") == (key + "S")) {
						$(this).html(data[key]);
					}
				}
			});
			
			
			
			$("#storageS").html(data.storageText);
			$("#storageS").attr("eValue", data.storage);
			$("#storage").select2('val', data.storage);

			$("#statusS").html(data.statusText);
			$("#statusS").attr("eValue", data.status);
			$("#status").select2('val', data.status);

			if (data.bindUserID == 0) {
				$("#binduser").html("设备未关联");
			} else {
				$("#binduser").append(
						"<a href='../spmsUser/spmsUserDetail.html?id="
								+ data.bindUserID + "'>" + data.bindUserName
								+ "</a>");
			}
			/*-----zigbee------*/
			showZigbee(type,JSON.parse(data.zigbee));
		} else {
			$.alert("信息获取失败");
		}
	})

	dto["type"] = type;
	if (type == "2") {
		// var sbydxx = d3.select("#sbydxx");
		d3.select("#sbydxx").select("legend").select("b").html("设备用电信息");
	}
	if (type == "3") {
		// var sbydxx = d3.select("#sbydxx");
		d3.select("#sbydxx").select("legend").select("b").html("设备历史开关记录");
	}
	
	var URL = "/spmsDevice/getDeviceRunInfo";
	console.log(dto);
	doJsonRequest(URL, dto, function(dat) {
		if (dat.result) {
			console.log(dat);
			if (type == "1") {
				deviceInfo1(dat["data"]);
			}
			if (type == "2") {
				deviceInfo2(dat["data"]);
			}
			if (type == "3") {
				deviceInfo2(dat["data"]);
			}
		}
	})
	if (type == "2" || type == "3") {
		var URL = "/spmsDevice/getDeviceConfigInfo";
		doJsonRequest(URL, dto, function(dat) {
			if (dat.result) {
				console.log(dat)
				doConfig(dat["data"]);

			}
		});
	}

	$("#updown").on(
			"click",
			function() {
				console.log(type);
				if (type == "2") {
					$(this).toggleClass("togglebtn-down").toggleClass(
							"togglebtn-up");
					var parent = $(this).parent().parent();
					parent.find("#powerSelect").toggle();
					var sbydxx = d3.select("#sbydxx");
					var select2 = $(sbydxx.node()).find("#powerSelect").find(
							"select").select2();
				} else if (type == "1") {
					$(this).toggleClass("togglebtn-down").toggleClass(
							"togglebtn-up");
					var parent = $(this).parent().parent();
					parent.find("#gwstatus").toggle()
				} else if (type == "3") {
					$(this).toggleClass("togglebtn-down").toggleClass(
							"togglebtn-up");
					var parent = $(this).parent().parent();
					parent.find("#onOff").toggle()
				}
				console.log($("#onOff").css("display"));
				if ($(this).hasClass("togglebtn-down")) {
					var URL = "/spmsDevice/getDeviceOperInfo"
					doJsonRequest(URL, dto, function(dat) {
						if (dat.result) {
							console.log(dat);
							if (type == "1") {
								Status1(dat["data"]);
							}
							if (type == "2") {
								Status2(dat["data"]);

							}
							if (type == "3") {
								Status3(dat["data"]);
							}

						}
					},{showWaiting:true});
				}
			});
})
function doConfig(dat) {
	if (dat["bind"]) {
		$("#sbpzxx").show();
	}

	var turn = {
		customname : "自定义命名",
		status : "设备状态",
		mode : "设备可选模式",
		temp : "设备温度范围",
		speed : "设备风量范围"
	}
	if (type == "3") {
		turn = {
			customname : "自定义命名"
		}
	}
	var sbpzxx = d3.select("#sbpzxx")
	for ( var key in turn) {
		var html = dat[key] || "";
		var control = sbpzxx.append("div").classed("control-group", true);
		var label = control.append("label").classed("control-label", true)
				.html(turn[key] + ":");
		var controls = control.append("div").classed("controls", true);
		controls.append("div").html(html);
	}
	$("#pzxx").on("click", function() {
		$(this).toggleClass("togglebtn-down").toggleClass("togglebtn-up");
		var parent = $(this).parent().parent();
		parent.find(".control-group").toggle();
	});
}
function deviceInfo2(dat) {
	var operstatus = dat["operstatus"];
	var status = [ "离线", "在线", "异常" ];
	var onoff = [ "关", "开" ];
	var mode = [ "自动","送风","制冷", "制热",  "除湿" ];
	var speed = [ "自动", "风速一", "风速二", "风速三", "风速四", "风速五", "静音" ];
	var trun = {
		onoff : "开关状态",
		temp : "室内温度",
		actemp : "目标温度",
		mode : "空调模式",
		speed : "空调风速"
	}
	if (type == "3") {
		trun = {
			onoff : "开关状态",
			remain : "电池状态",
			operstatus : "运行状态"
		}
		$("#sbyxxx").find("#operStatus").parent().parent().remove();

	}
	$("#ajaxInfo").on("click", function() {
		$(this).toggleClass("togglebtn-down").toggleClass("togglebtn-up");
		var parent = $(this).parent().parent();
		parent.find(".control-group").toggle();
		// parent.find("table").toggle()
	});
	var sbyxxx = d3.select("#sbyxxx");
	sbyxxx.select("#operStatus").html(status[operstatus])
	$(sbyxxx.node()).find("table").hide();
	for ( var key in trun) {
		 var html = dat[key] || "";
		if (key == "onoff") {
			html = onoff[dat[key]];
		} else if (key == "mode") {
			html = mode[dat[key]];
		} else if (key == "speed") {
			html = speed[dat[key]];
		} else if(key == "actemp" || key == "temp" ) { 
			if(html!=""){
			html = html + "℃";}
		} else if(key == "operstatus") {
			html = status[dat[key]];
		} 
		var control = sbyxxx.append("div").classed("control-group", true);
		var label = control.append("label").classed("control-label", true)
				.html(trun[key] + ":");
		var controls = control.append("div").classed("controls", true);
		controls.append("div").html(html);
	}

}
function Status2(dat) {
	var powers = dat["power"];
	var reactivePowers = dat["reactivePower"];
	var powerFactors = dat["powerFactor"];
	var apparentPowers = dat["apparentPower"];
	generateChartOne(powers, reactivePowers, powerFactors, apparentPowers);

	var eps = dat["eP"];
	var reactiveEnergys = dat["reactiveEnergy"];
	// generateChartTwo(eps, reactiveEnergys);

	var activeDemands = dat["activeDemand"];
	var reactiveDemands = dat["reactiveDemand"];
	// generateChartThree(activeDemands, reactiveDemands);

	var currents = dat["current"];
	var currents = dat["voltage"];
	var frequencys = dat["frequency"];
	// generateChartFour(currents, currents, frequencys)

	var sbydxx = d3.select("#sbydxx");
	sbydxx.select("legend").select("b").html("设备用电信息");
	// $(sbydxx.node()).find("#powerSelect").show();

	$("#powerSelect").find("select").change(
			function() {
				$("#switchRangeDiv").hide();
				var value = $(this).val();
				if (value == "1") {
					generateChartOne(powers, reactivePowers, powerFactors,
							apparentPowers);
				} else if (value == "2") {
					$("#switchRangeDiv").show();
					generateChartTwo(eps, reactiveEnergys);
				} else if (value == "3") {
					generateChartThree(activeDemands, reactiveDemands);
				} else if (value == "4") {
					generateChartFour(currents, currents, frequencys)
				}
			});
}

function rBtnClick(id){
	$("#rBtn1").css("background-color","rgb(0, 51, 153)");
	$("#rBtn2").css("background-color","rgb(0, 51, 153)");
	$("#rBtn3").css("background-color","rgb(0, 51, 153)");
	$("#rBtn4").css("background-color","rgb(0, 51, 153)");
	$("#" + id).css("background-color","yellow");
	
	if(id == "rBtn1"){
		rbtn = 1;
	}
	if(id == "rBtn2"){
		rbtn = 2;
	}
	if(id == "rBtn3"){
		rbtn = 3;
	}
	if(id == "rBtn4"){
		rbtn = 4;
	}
	var dto = {
			id : getURLParam("id"),
			rbtn:rbtn,
			type:type
		};
	var URL = "/spmsDevice/getDeviceOperInfo"
		doJsonRequest(URL, dto, function(dat) {
			if (dat.result) {
				console.log(dat);
				if (type == "2") {
					var eps = dat["data"]["eP"];
					var reactiveEnergys = dat["data"]["reactiveEnergy"]
					generateChartTwo(eps, reactiveEnergys);
				}
			}
		},{showWaiting:true});
}

function deviceInfo1(dat) {
	var operstatus = dat["operstatus"];
	var status = [ "离线", "在线", "异常" ];
	var devicelist = dat["devicelist"];
	$("#sbpzxx").hide();
	$("#ajaxInfo").on("click", function() {
		$(this).toggleClass("togglebtn-down").toggleClass("togglebtn-up");
		var parent = $(this).parent().parent();
		parent.find(".control-group").toggle();
		parent.find("table").toggle()
	});
	var sbyxxx = d3.select("#sbyxxx");
	sbyxxx.select("#operStatus").html(status[operstatus]);
	sbyxxx.select("table").style("display", "table");
	if (devicelist && devicelist.length) {
		var tbody = sbyxxx.select("table").select("tbody");
		$(tbody.node()).show()
		for (var j = 0; j < devicelist.length; j++) {
			var type = devicelist[j]["type"];
			var id = devicelist[j]["id"];
			var mac = devicelist[j]["mac"];
			var operStatus = devicelist[j]["operStatus"];
			var typetext = devicelist[j]["typetext"]
			var tr = tbody.append("tr");
			var td = tr.append("td");
			td.append("a").attr("href",
					"spmsDeviceUpdate.html?id=" + id + "&type=" + type).html(
					typetext);
			td = tr.append("td");
			td.append("a").attr("href",
					"spmsDeviceUpdate.html?id=" + id + "&type=" + type).html(
					mac);
			td = tr.append("td");
			td.append("a").attr("href",
					"spmsDeviceUpdate.html?id=" + id + "&type=" + type).html(
					status[operStatus]);
		}
	}
}
function Status1(dat) {
	console.log(dat);
	var datas = dat["datas"];
	var gwstatus = d3.select("#gwstatus").node();
	gwStatus(gwstatus, datas);
}
function Status3(dat) {

	var datas = dat["datas"];
	var onoff = d3.select("#onOff").node();
	onOffStatus(onoff, datas);
}

function doEditBaseInfo() {
	// doRequest();
	// alert($.toJSON($("[name='test1']")[0]));
	if(!$("#inputForm").valid()){
		$.alert("请完善必填信息");
		return false;
	}
	finishEditText($("[name='test1']")[0], 'basic',
			'/spmsDevice/doEditBaseInfo');
	// var data = $("#inputForm").serialize();
	// doRequest("/user/editUser", data, function(data) {
	// },{showWaiting:true})
}
function doEditOpreInfo() {
	doRequest();
	finishEditText($("[name='test2']")[0], 'motion',
			'/spmsDevice/doEditOpreInfo');
}

function deleteDevice() {
	var config = {
		msg : "您确定要删除么？",
		confirmClick : confirmClick
	}
	$.confirm(config);
	function confirmClick() {
		var dto = {
			'ids' : getURLParam("id")
		};
		doRequest('/spmsDevice/doDel', dto, function(data) {
			if (data.result == true) {
				if (data.data == true) {
					$.alert("删除成功!");
					window.location.href = "spmsDeviceList.html?type="
							+ $("#type").val();
				} else {
					$.alert("已绑定的设备无法删除");
				}
			} else {
				$.alert("删除失败！");
			}
		});
	}

}
// 动态加载设备信息
function doReQuery(type) {
	$("#type").val(type);
	$(".active").removeClass("active");
	$("li:even", $(".nav")).eq(type - 1).attr("class", "active");
	doQuery();
}


// 刷新Zigbee信息
function RefreshZigbee(){
	doJsonRequest('/spmsDevice/getRefreshZigbee', {type:type,deviceId:deviceId}, function(data) {
		var data = data.data;
		showZigbee(type,JSON.parse(data.zigbee));
	},{showWaiting : true});
}
// 发消息
function sendmMessage(){
	doJsonRequest('/spmsDevice/sendmMessageForZigbee', {type:type,deviceId:deviceId}, function(data) {
		
	},{showWaiting : true});
}

function showZigbee(type,zigbee){
	if(type == 1){//网关
		$("#inputForm3zigbee").empty();
		$("#inputForm3zigbee").append(
		"		<legend>"+
	    "       zigbee信息表 <span> <input class=\"edit_complete\" name=\"test2\""+
	    "                            style=\"display: none;\" id=\"motionComplete\" type=\"button\""+
	    "                             onClick=\"doEditOpreInfo()\">"+
		"				<input class=\"edit_cancel\" name=\"\" style=\"display: none\""+
	    "                       id=\"motionCancel\" type=\"button\""+
	    "                       onClick=\"cancelContol(this,'motion'),cancelStroge()\">  " +
	    "				<input class=\"refresh_ico\" name=\"\" type=\"button\" id=\"motionEdit\""+
	    "                onClick=\"RefreshZigbee()\">  "+
	    "				<input class=\"send_ico\" name=\"\" type=\"button\" id=\"motionEdit\""+
	    "                onClick=\"sendmMessage()\">  "+
		"			</span> <a class=\"togglebtn togglebtn-down\" href=\"javascript:void(0)\" "+
	    "                       onClick=\"togglebtnText(this)\"></a>"+
	    "    </legend>"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">Zigbee通道:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"zigbeeChannel\">"+zigbee.zigbeeChannel+"</div>"+
	    "        </div>"+
	    "    </div>"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">Zigbee通道掩码:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"zigbeeChannelMask\">"+zigbee.zigbeeChannelMask+"</div>"+
	    "        </div>"+
	    "    </div>"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">Zigbee发送功率:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"zigbeeTxPower\">"+zigbee.zigbeeTxPower+"</div>"+
	    "        </div>"+
	    "    </div>	"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">更新日期:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"zigbeeupdateTime\">"+zigbee.updateTime+"</div>"+
	    "        </div>"+
	    "    </div>	"
		);
	}else if(type == 2){//空调
		$("#inputForm3zigbee").empty();
		$("#inputForm3zigbee").append(
		"		<legend>"+
	    "       zigbee信息表 <span> <input class=\"edit_complete\" name=\"test2\""+
	    "                            style=\"display: none;\" id=\"motionComplete\" type=\"button\""+
	    "                             onClick=\"doEditOpreInfo()\">"+
		"				<input class=\"edit_cancel\" name=\"\" style=\"display: none\""+
	    "                       id=\"motionCancel\" type=\"button\""+
	    "                       onClick=\"cancelContol(this,'motion'),cancelStroge()\">  " +
	    "				<input class=\"refresh_ico\" name=\"\" type=\"button\" id=\"motionEdit\""+
	    "                onClick=\"RefreshZigbee()\"> "+
	    "				<input class=\"send_ico\" name=\"\" type=\"button\" id=\"motionEdit\""+
	    "                onClick=\"sendmMessage()\">  "+
		"			</span> <a class=\"togglebtn togglebtn-down\" href=\"javascript:void(0)\" "+
	    "                       onClick=\"togglebtnText(this)\"></a>"+
	    "    </legend>"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">RxRssi平均值:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"averageRxRssi\">"+zigbee.averageRxRssi+"</div>"+
	    "        </div>"+
	    "    </div>"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">RxLQI平均值:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"averageRxLQI\">"+zigbee.averageRxLQI+"</div>"+
	    "        </div>"+
	    "    </div>"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">TxRssi平均值:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"averageTxRssi\">"+zigbee.averageTxRssi+"</div>"+
	    "        </div>"+
	    "    </div>	"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">TxLQI平均值:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"averageTxLQI\">"+zigbee.averageTxLQI+"</div>"+
	    "        </div>"+
	    "    </div>	"+
	    "    <div class=\"control-group\">"+
	    "        <label class=\"control-label\">更新日期:</label>"+
	    "        <div class=\"controls\">"+
	    "            <div class=\"input_view\" id=\"TxLQIupdateTime\">"+zigbee.updateTime+"</div>"+
	    "        </div>"+
	    "    </div>	"
		);
	}
}
