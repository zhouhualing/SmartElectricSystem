function doLoginOut() {
	var dto = {};
	doJsonRequest("/doLoginOut", dto, function(data) {
		if (data.result) {
			window.location.href = "login.html"
		} else {
			alert("退出失败。");
		}
	})
}
var deviceIds = new Array();
var EYONDeviceId = "";
var num = 0;
/**
 * Created by Administrator on 2015/1/22.
 * 用户端获取数据展示
 */
var maxTemps = 0;
var minTemps = 0;
var iIndex = 0;
var continueRefresh = null;
var gwId = null;
var functionIndex = [];
var lastTime = {};
var devicesId = [];
var datasForMM = new Array() ;
var gwStatus = null;
var tapFlag = false;
var Times = 0;
var isDevice = 0;
var Expenddata = {};
var ExpenddataForDoor = {};
var dataForBarGraph = {};
var dataForGlobal = new Array();
var dataForGlobalLength = new Array();
var buttonColor = new Array();
var indexI = 0;
var type = {};
var rbtn = "1";
var ydqss;
var ydqssll;
var changeFlag = {};
$(document)
		.ready(
				function() {
					var URL = "/spmsuc/getIndexData";
					doRequest(URL, null, function(dat) {
						if (dat.result) {
							//console.log(dat);
							init(dat["data"]);
						}
					}, {
						showWaiting : true
					});
					function init(dat) {
						var datas = dat["datas"];
						var spmsuser = (dat["spmsuser"]);
						if (spmsuser) {
							personInfo(spmsuser);
						}
						var weatherInform = dat["weatherInform"];
						if (weatherInform) {
							weatherInform = JSON.parse(weatherInform);
							//    console.log(weatherInform);
							changeWeather(weatherInform);
						}
						gwId = dat["gwId"];
						gwStatus = dat["gwStatus"];
						isDevice = dat["isDevice"];
						if (isDevice == 0) {
							d3.select(".isDevice").classed("display_none",
									false);
						}

						if (gwStatus == "2" || gwStatus == "0") {
							d3.select(".gwStatus").classed("display_none",false);
						}
						var content = d3.select(".content");
						indexI = datas.length;
						for ( var i = 0; i < datas.length; i++) {
							datasForMM[i] = datas[i];
							
							deviceId = datas[i]["deviceId"];
							var lastdata = datas[i]["datas"];
							if (lastdata && lastdata.length) {
								var len = lastdata.length;
								lastTime[deviceId] = lastdata[len - 1][0];
							} else {
								lastTime[deviceId] = 0;
							}
							
							devicesId.push(deviceId);
							eval("type['" + deviceId+"'] = '1'");
							eval("changeFlag['" + deviceId+"'] = false");
							deviceIds.push(deviceId);
							var deviceName = datas[i]["deviceName"];
							var status = datas[i]["status"];
							var act = datas[i]["act"];
							var onOff = datas[i]["onoffStatus"];
							/*
							 * 门窗传感信息页面显示
							 */
							if(datas[i]["deviceType"] == "3" || datas[i]["deviceType"] == 3){
								var remain =datas[i]["remain"];
								var doorStatus = datas[i]["status"];
								var content_kt_door= content.append("div").classed("content_kt_door", true).attr("id", deviceId).attr("onOff", onOff);
								//getlastTimeData(content_kt,deviceId,i);
								var content_kt_door_list = content_kt_door.append("div").classed("content_kt_door_list", true);
								var content_kt_list_title = content_kt_door_list.append("div").classed("content_kt_list_title",true);
								content_kt_list_title.append("span").classed("fl pl_30", true).attr("id", deviceId).html(deviceName);
								var span = content_kt_list_title.append("span").classed("content_kt_list_title_ico", true);
								var bj = span.append("a").classed("bj", true).attr("bj", deviceId).attr("title", "编辑名称");
								// 编辑设备名称
								bj.on("click", function() {
									$(this).toggleClass("click");
									var parent = $(this).parent().parent();
									var deviceId = $(this).attr("bj");
									if ($(this).hasClass("click")) {
										var html = parent.find("#" + deviceId).html();
										parent.find("#" + deviceId).html( "<input type=text value=" + html + ">" );
									} else {
										var URL = "/spmsuc/changeCustomName";
										var self = {
											deviceid : deviceId,
											newname : parent.find("#" + deviceId)
													.find("input").val()
										}
										clearTimeout(continueRefresh);
										doJsonRequest(URL, self, function(dat) {
											if (dat.result) {
												var value = parent.find(
														"#" + deviceId).find(
														"input").val();
												parent.find("#" + deviceId).html(
														value);
											}
											continueRefresh = setTimeout(function() {doRefresh();}, 5000);
										}, {
											showWaiting : false,
											handdingInfo : "名字更改中...",
											successInfo : "更改成功...",
											failtureInfo : "更改失败.."
										});
										/* var value = parent.find("#" + deviceId).find("input").val();
										 parent.find("#" + deviceId).html(value);*/
									}
								});
								var door= "door_close";
								var status ="关闭";
										if(onOff == "1" ||onOff == 1){
											status ="开启";
											door="door_open";
										}
								var content_kt_door_list_content = content_kt_door_list.append("div")
										.classed("content_kt_door_list_content", true);
								var content_kt_nr_kg_ktkg = content_kt_door_list_content
										.append("div").classed(door, true).attr("find", deviceId);
										
							content_kt_door_list_content.append("div")
										.classed("content_kt_door_state", true).html("当前状态："+status);
						var content_kt_door_state=	content_kt_door_list_content.append("div")
										.classed("content_kt_door_state", true);
						if(doorStatus == "1"){
									var span=content_kt_door_state.append("span").classed("fl pl_30", true).html("电量：");	
									content_kt_door_state.append("span").classed("electricity"+(remain==null? "" :remain) , true).attr("id","electricity"+deviceId);			
						}
									// 开关控制
								/*content_kt_nr_kg_ktkg.on("click", function() {
									var find = $(this).attr("find");
									var parent = $(this).parent().parent().parent();
									var onOff = parent.attr("onOff");
									if (onOff == "0") {
										var URL = "/spmsuc/deviceOpen";
									} else {
										var URL = "/spmsuc/deviceClose";
									}
									var id = parent.attr("id");
									var self = {
										gwid : gwId,
										deviceid : id
									}
									clearTimeout(continueRefresh);
									doJsonRequest(URL, self, function(dat) {
										if (dat.result) {
											turnOnOff(parent,id);
										}
										continueRefresh = setTimeout(function() {
											doRefresh();
										}, 5000);
									}, {
										showWaiting : true,
										handdingInfo : "开关更改中...",
										successInfo : "更改成功...",
										failtureInfo : "更改失败.."
									});
	
								});*/
								var content_kt_ydqs_btn = content_kt_door_list_content.append("div").classed("content_door_switch_btn", true);
								$(content_kt_ydqs_btn.node()).attr("index", i);
								content_kt_door_list_content.append("div").attr("id", deviceId)
								.classed("kgjl", true);
								
								//门窗离线
								if(doorStatus != "1"){
									//content_kt_door_state.classed("display_none", true);
									var s = $(content_kt_door_state).parent().find(".door_close");
									s.append("<span class='dk_ico'></span>")
									.html("<img style='margin-top: 83px;margin-left: 117px;' src=../../resources/spms/images/duankai.png width=32 height=32>");
									$(content_kt_door_state).parent().find(".content_kt_door_state").eq(0).hide();;
									//content_kt_ydqs_btn.classed("display_none", true);
									content_kt_door_list_content.append("div").style({
										"text-align" : "center",
										"margin-top" : "60px",
										"margin-bottom" : "126px"
									}).html("与设备失去联系");
								}
								
								d3.selectAll(".content_door_switch_btn").on("click",function() {
								var dto = {
										'id' : deviceId,
										'type':"3"
										};
								var parent = $(this).parent();
								var onoff = parent.find(".kgjl").toggle();
									if( ExpenddataForDoor[ dto.id ] != 0 && typeof(ExpenddataForDoor[ dto.id ]) ==  "undefined"){//TODO
										var URL = "/spmsDevice/getDeviceOperInfo";
										doJsonRequest(URL, dto, function(dat) {
											var index = +$(this).attr("index");
	//										var onoff =   d3.select(".onoff").node();
											functionIndex[index]	=	onOffStatus(onoff, dat["data"]["datas"]);
											ExpenddataForDoor[ dto.id ] = dat["data"]["datas"];
										},{showWaiting : true});
									}else{
										var index = +$(this).attr("index");
										functionIndex[index]	=	onOffStatus(onoff, ExpenddataForDoor[ dto.id ] );
									}
								});
							}
							if(datas[i]["deviceType"] == "2" || datas[i]["deviceType"] == 2){
							var content_kt = content.append("div").classed("content_kt", true).attr("id", deviceId).attr("onOff", onOff);
							//putData(content_kt,deviceId,i);//放数据
							$(content_kt.node()).attr("index", i)
							//getlastTimeData(content_kt,deviceId,i);
							var content_kt_list = content_kt.append("div").classed("content_kt_list", true);
							var content_kt_list_title = content_kt_list.append("div").classed("content_kt_list_title",true);
							content_kt_list_title.append("span").classed("fl pl_30", true).attr("id", deviceId).html(deviceName);
							var span = content_kt_list_title.append("span").classed("content_kt_list_title_ico", true);
							var bj = span.append("a").classed("bj", true).attr("bj", deviceId).attr("title", "编辑名称");
							// 编辑设备名称
							bj.on("click", function() {
								$(this).toggleClass("click");
								var parent = $(this).parent().parent();
								var deviceId = $(this).attr("bj");
								if ($(this).hasClass("click")) {
									var html = parent.find("#" + deviceId).html();
									parent.find("#" + deviceId).html( "<input type=text value=" + html + ">" );
								} else {
									var URL = "/spmsuc/changeCustomName";
									var self = {
										deviceid : deviceId,
										newname : parent.find("#" + deviceId)
												.find("input").val()
									}
									clearTimeout(continueRefresh);
									doJsonRequest(URL, self, function(dat) {
										if (dat.result) {
											var value = parent.find(
													"#" + deviceId).find(
													"input").val();
											parent.find("#" + deviceId).html(
													value);
										}
										continueRefresh = setTimeout(function() {doRefresh();}, 5000);
									}, {
										showWaiting : false,
										handdingInfo : "名字更改中...",
										successInfo : "更改成功...",
										failtureInfo : "更改失败.."
									});
									/* var value = parent.find("#" + deviceId).find("input").val();
									 parent.find("#" + deviceId).html(value);*/
								}
							});
							//span.append("a").classed("wd", true);
						//	if(datas[i]["deviceType"] == "2" || datas[i]["deviceType"] == 2){
								//查询空调舒适曲线设置信息
								/*--加载曲线数量有和没有图标显示不同--*/
								var cnum = 0;
								doJsonRequest("/rairconSet/QueryCurveNumForCurveDevice", {deviceId:deviceId,  minTemps:datasForMM[iIndex]["minTemp"],  maxTemps:datasForMM[iIndex]["maxTemp"]   }, function (nums) {//获取曲线数量
									cnum = parseInt(nums.data);
								},{showWaiting : true,async : false})
								var dk ;
								if(cnum == 0){//没有显示白色图标
									dk = span.append("a").classed("dk", true).attr(
										"bj", deviceId).attr("title",
										"查询空调舒适曲线设置信息");
								}else{//有则显示红色图标
									dk = span.append("a").classed("dk2", true).attr(
										"bj", deviceId).attr("title",
										"查询空调舒适曲线设置信息");
								}
								dk.on("click", function() {
									iIndex =  $(this).parent().parent().parent().parent().attr('index');
									minTemps = datasForMM[iIndex]["minTemp"];
									maxTemps = datasForMM[iIndex]["maxTemp"];
									openLineChart(deviceIds[iIndex],minTemps,maxTemps);
									EYONDeviceId = deviceIds[iIndex];
									//预显示应该出现的重复设置
									$('.RairconSetting_div4').children().attr("onclick","weekSetforCurve()");
									$('.RairconSetting_div4').children().attr("style","display:display;");
									//$('.RairconSetting_div4').children().children().text("重复日期  ");
									$('.repeats').show();
									$('.RairconSetting_div1').children().attr("style","display:display;");
								});
								/*--加载定时设置数量有和没有图标显示不同--*/
								var cnumForClocking = 0;
								doJsonRequest("/rairconSet/QueryClockNum", {deviceId:deviceId}, function (nums) {//获取曲线数量
									cnumForClocking = parseInt(nums.data);
								},{showWaiting : true,async : false})
								//空调定时设置
								var timingSet ;
								if(cnumForClocking == 0){//没有显示白色图标
									timingSet = span.append("a").classed("wd", true).attr(
										"bj", deviceId).attr("title",
										"查询空调定时设置信息");
								}else{//有则显示红色图标
									timingSet = span.append("a").classed("wd2", true).attr(
										"bj", deviceId).attr("title",
										"查询空调定时设置信息");
								}
								timingSet.on("click", function() {
									iIndex =  $(this).parent().parent().parent().parent().attr('index');
									minTemps = datasForMM[iIndex]["minTemp"];
									maxTemps = datasForMM[iIndex]["maxTemp"];
									rairconTSet(deviceIds[iIndex]);
									EYONDeviceId = deviceIds[iIndex];
								});
							
							var content_kt_nr = content_kt_list.append("div")
									.classed("content_kt_nr", true);
							// kg
							var content_kt_nr_kg = content_kt_nr.append("div")
									.classed("content_kt_nr_kg", true);
							content_kt_nr_kg.append("div").classed(
									"content_kt_nr_kg_ktpic1", true);
							/*$('.content_kt_nr_kg_ktpic1').click(function() {
								if (errorControl == "1") {
									errorState($('.content_kt_nr'));
								}
							})*/
							var content_kt_nr_kg_ktkg = content_kt_nr_kg
									.append("div").classed(
											"content_kt_nr_kg_ktkg", true)
									.attr("find", deviceId);
							// 开关控制
							content_kt_nr_kg_ktkg.on("click", function() {
								var find = $(this).attr("find");
								var parent = $(this).parent().parent().parent()
										.parent();
								var onOff = parent.attr("onOff");
								if (onOff == "0") {
									var URL = "/spmsuc/deviceOpen"
								} else {
									var URL = "/spmsuc/deviceClose"
								}
								var id = parent.attr("id");
								var self = {
									gwid : gwId,
									deviceid : id
								}
								clearTimeout(continueRefresh);
								doJsonRequest(URL, self, function(dat) {
									if (dat.result) {
										turnOnOff(parent)
									}
									continueRefresh = setTimeout(function() {
										doRefresh();
									}, 5000);
								}, {
									showWaiting : true,
									handdingInfo : "开关更改中...",
									successInfo : "更改成功...",
									failtureInfo : "更改失败.."
								});

							});
							// sdwd
							var content_kt_nr_sdwd = content_kt_nr.append("div").classed("content_kt_nr_sdwd", true);
							var content_kt_nr_sdwd_snwd = content_kt_nr_sdwd.append("div").classed("content_kt_nr_sdwd_snwd", true)
									.html("                        室内温度 ");
							var rtHtml = turnTemp(datas[i]["rt"])
							var wd = content_kt_nr_sdwd_snwd.append("div")
									.classed("wd", true);
							wd.html(rtHtml + "<span class=wd_celsius></span>");

							var content_kt_nr_sdwd_wdsz = content_kt_nr_sdwd
									.append("div").classed(
											"content_kt_nr_sdwd_wdsz", true);
							content_kt_nr_sdwd_wdsz.append("div").classed(
									"fz_22 pb_25", true).html("设定温度");
							var actHtml = turnTemp(datas[i]["act"]);
							var wd = content_kt_nr_sdwd_wdsz.append("div")
									.classed("wd", true).attr("id",
											"defaulttemp").attr("defaultact",
											"");
							wd.html(actHtml + "<span class=wd_celsius></span>");
							var content_kt_nr_sdwd_hk = content_kt_nr_sdwd
									.append("div").classed(
											"content_kt_nr_sdwd_hk", true);
							var font_family_a = content_kt_nr_sdwd_hk.append(
									"div")
									.classed("number font_family_a", true);
							var maxTemp = datas[i]["maxTemp"];
							var minTemp = datas[i]["minTemp"];
							font_family_a.append("p").classed("pb_60 pt_5",
									true).attr("id", "maxTemp").html(
									maxTemp + "℃")
							font_family_a.append("p").classed("pb_60", true)
									.attr("id", "amongTemp").html(
											Math.floor((minTemp + maxTemp) / 2)
													+ "℃");
							font_family_a.append("p").attr("id", "minTemp")
									.html(minTemp + "℃");
							var content_kt_nr_fs_right_hd = content_kt_nr_sdwd_hk
									.append("div").classed(
											"content_kt_nr_fs_right_hd", true)
									.attr("id", "wd").style({
										width : "36px",
										height : "192px"
									});
							//  act = 26;
							var v = (act - maxTemp) / (-(maxTemp - minTemp));
							var config = {
								wdDiv : wd.node(),
								gwId : gwId,
								deviceid : deviceId,
								min : minTemp,
								max : maxTemp,
								_defaultValue : v,
								renderTo : content_kt_nr_fs_right_hd.node()
							}
							var c1 = new Harmazing.chart.ControlBar(config);
							//  moshi
							var mode = datas[i]["mode"]; //模式
							var xuehua = [ "auto", "jh", "xuehua", "sun",
									"chushi" ];
							var content_kt_nr_ms = content_kt_nr.append("div")
									.classed("content_kt_nr_ms", true);
							var content_kt_nr_ms_left = content_kt_nr_ms
									.append("div").classed("content_kt_nr_ms_left", true)
							content_kt_nr_ms_left.append("div").classed(
									"fz_22 pb_25", true).html("模式");
							content_kt_nr_ms_left.append("div").classed(
									xuehua[mode], true).attr("id", "canFind")
									.attr("mode", xuehua[mode]);
							var content_kt_nr_ms_right = content_kt_nr_ms
									.append("div").classed(
											"content_kt_nr_ms_right", true);
							content_kt_nr_ms_right.append("a").classed("auto",
									true).attr("title", "自动模式").attr("dif",
									deviceId).attr("modeNum", mode);
							content_kt_nr_ms_right.append("a").classed("jh ",
									true).attr("title", "送风模式").attr("dif",
									deviceId).attr("modeNum", mode);
							content_kt_nr_ms_right.append("a").classed(
									"xuehua", true).attr("title", "制冷模式").attr(
									"dif", deviceId).attr("modeNum", mode);
							content_kt_nr_ms_right.append("a").classed("sun",
									true).attr("title", "制热模式").attr("dif",
									deviceId).attr("modeNum", mode);
							content_kt_nr_ms_right.append("a").classed(
									"chushi", true).attr("title", "除湿模式").attr(
									"dif", deviceId).attr("modeNum", mode);
							$(content_kt_nr_ms_right.node()).find(
									"a:eq(" + mode + ")").addClass("hover");
							//   var oldms
							// 更改模式：
							$(content_kt_nr_ms_right.node()).find("a").each(function(index) {
												$(this).on("click",function() {
													var URL = "/spmsuc/setAcMs";
													var self = {gwid : gwId,deviceid : $(this).attr("dif"),
																		newms : index,
																		oldms : +$(this).attr("modeNum")
																	};
																	//    console.log(self);
																	var parent = $(this).parent();
																	var that = $(this);
																	clearTimeout(continueRefresh);
																	doJsonRequest(URL,self,function(dat) {
																		if (dat.result) {
																			parent.find("a").removeClass("hover").attr("modeNum",index);
																			parent.parent().find(".content_kt_nr_ms_left #canFind").removeClass().addClass(xuehua[index]).attr("mode",xuehua[index]);
																					that.addClass("hover");
																					mode = index;
																				}
																				continueRefresh = setTimeout(function() {doRefresh()},5000);
																			},
																			{
																				showWaiting : true,
																				handdingInfo : "模式更改中...",
																				successInfo : "更改成功...",
																				failtureInfo : "更改失败.."
																			});
																	/* mode = index;
																	 $(this).parent().find("a").removeClass("hover");
																	 $(this).parent().parent().find(".content_kt_nr_ms_left #canFind").removeClass().addClass(xuehua[index]).attr("mode", xuehua[index]);
																	 $(this).addClass("hover");*/

																})
											});
							//fengsu
							//风速
							var speed = datas[i]["speed"] || 0;
							if(speed == 0){
							speed=7;
							}
							var content_kt_nr_fs = content_kt_nr.append("div")
									.classed("content_kt_nr_fs", true);
							var content_kt_nr_fs_left = content_kt_nr_fs
									.append("div").classed(
											"content_kt_nr_fs_left", true);
							content_kt_nr_fs_left.append("div").classed(
									"fz_22 pb_25", true).html("风速");
							content_kt_nr_fs_left.append("div").classed(
									"fengsu", true).attr("defaultspeed", speed)
									.attr("id", "defaultfs");
							var content_kt_nr_fs_right = content_kt_nr_fs
									.append("div").classed(
											"content_kt_nr_fs_right", true);
							var content_kt_nr_fs_right_pic = content_kt_nr_fs_right
									.append("div")
									.classed(
											"content_kt_nr_fs_right_pic content_kt_nr_fs_right_picNaN content_kt_nr_fs_right_pic"
													+ speed, true);
							var content_kt_nr_fs_right_hd = content_kt_nr_fs_right
									.append("div").classed(
											"content_kt_nr_fs_right_hd", true)
									.attr("id", "fs");
							var v = (speed - 8) / (-(8 - 0));
							var config = {
								fsDiv : content_kt_nr_fs_right_pic.node(),
								gwId : gwId,
								deviceid : deviceId,
								min : 1,
								max : 8,
								_defaultValue : v,
								renderTo : content_kt_nr_fs_right_hd.node()
							}
							var c1 = new Harmazing.chart.ControlBar1(config);
							// 展示更多按钮
							var content_kt_ydqs_btn = content_kt_nr.append("div").classed("content_kt_ydqs_btn", true);
							$(content_kt_ydqs_btn.node()).attr("index", i);
							//用电信息，用电量选择框
							var select = content_kt_nr.append("select")
									.classed("qsordl", true).style({
										position : "absolute",
										display : "none",
										right : "26px",
										top : "320px",
										"z-index" : 99
									});
							select.append("option").attr("value", 1).html("用电信息");
							select.append("option").attr("value", 2).html("用电量");
							
							content_kt_nr.append("div").attr("id", deviceId).classed("ydqs", true);
							//断开
							var content_kt_nr_dk = content_kt_list
									.append("div").classed(
											"content_kt_nr_dk display_none",
											true);
							var content_kt_nr_kg_dk = content_kt_nr_dk.append(
									"div").classed("content_kt_nr_kg_dk", true)
							var content_kt_nr_kg_ktpic2 = content_kt_nr_kg_dk
									.append("div")
									.classed("content_kt_nr_kg_ktpic2 p_r",
											true);
							content_kt_nr_kg_ktpic2
									.append("span")
									.classed("dk_ico", true)
									.html(
											"<img src=../../resources/spms/images/duankai.png width=32 height=32>");
							content_kt_nr_dk.append("div").style({
								"text-align" : "center",
								"margin-top" : "60px",
								"margin-bottom" : "126px"
							}).html("与设备失去联系");
							if (status == "0" || status == "2") {
								content_kt_nr.classed("display_none", true);
								content_kt_nr_dk.classed("display_none", false);
							}
							// 展示更多按钮
							var content_kt_ydqs_btn = content_kt_nr_dk.append("div").classed("content_kt_ydqs_btn", true);
							$(content_kt_ydqs_btn.node()).attr("index", i);
							//用电信息，用电量选择框
							var select = content_kt_nr_dk.append("select")
									.classed("qsordl", true).style({
										position : "absolute",
										display : "none",
										right : "26px",
										top : "320px",
										"z-index" : 99
									});
							select.append("option").attr("value", 1).html(
									"用电信息");
							select.append("option").attr("value", 2)
									.html("用电量");

							content_kt_nr_dk.append("div").attr("id", deviceId)
									.classed("ydqs", true);
							
							
							//图表展示按钮 
							d3.selectAll(".content_kt_ydqs_btn").on("click",function() {
								//hmBlockUI();
								var parent = $(this).parent();
								var ydqs = parent.find(".ydqs").toggle();
								
								var ydlll = parent.find(".ydqs");
								ydqssll = parent.find(".ydqs");
								var select = parent.find("select").toggle();
								var chartData = $(this).data("chart")|| $(this).parent().parent().parent().data("chart");
								var index = +$(this).attr("index");
								var rootDiv = $(this).parent().parent().parent()
								if( dataForGlobal[index] == null ){	
									//HolterPowerData( $(this) || $(this).parent().parent().parent() );
									if( typeof(Expenddata[ deviceIds[index] ]) ==  "undefined" && Expenddata[ deviceIds[index] ] != 0 ){
										//chartData = getlastTimeDataForOnclick(deviceIds[index]);
										var lastdata ;
										doJsonRequest("/spmsuc/getDeviceExpend", {deviceId:deviceIds[index],type:"1"}, function (data) {
											data = data.data;
											lastdata = data['datas'];
											Expenddata[deviceIds[index]] = lastdata.length == 0 ? 0 : lastdata;
											functionIndex[index] = ydl(ydqs, lastdata);
											dataForGlobal[index] = lastdata;
										},{showWaiting : true})
									}else{
										functionIndex[index] = ydl(ydqs, Expenddata[ deviceIds[index] ]);
									}
									select.change(function() {
										ydqss = ydqs;
//										var chartData = $(rootDiv).data("chart");
										var chartData = Expenddata[ deviceIds[index]];
										//console.log(chartData)
										if ($(this).val() == 1) {
											dataForGlobalLength[index] = 0;
											type[deviceIds[index]] = "1";
											changeFlag[deviceIds[index]] = true;
											functionIndex[index] = ydl(ydqs,Expenddata[ deviceIds[index]]);
											dataForGlobal[index] = Expenddata[ deviceIds[index]];
										} else {
											dataForGlobalLength[index] = 1;
											type[deviceIds[index]] = "2";
											changeFlag[deviceIds[index]] = true;
											if(typeof( dataForBarGraph[  deviceIds[index]  ]) == "undefined" ){
												doJsonRequest("/spmsuc/getDeviceExpend", {deviceId:deviceIds[index],type:type[deviceIds[index]],rbtn:"1"}, function (data) {
													data = data.data;
													dataForBarGraph[  deviceIds[index]  ] = data['datas'];
													functionIndex[index] = ydxx(ydqs,dataForBarGraph[  deviceIds[index]  ]);
													indexs = index;
													//四个按钮
													ydlll.append(
							"<div id='switchRangeDiv' style='position:absolute; left:90px; top:69px; z-index:99;'>"+
			            	"<input id='rBtn1' class='cBtn' type='button' onclick='rBtnClick(\"1\")' value='当前'/>"+
			            	"<input id='rBtn2' class='cBtn' type='button' onclick='rBtnClick(\"2\")' value='当天'/>"+
			            	"<input id='rBtn3' class='cBtn' type='button' onclick='rBtnClick(\"3\")' value='当月'/>"+
			            	"<input id='rBtn4' class='cBtn' type='button' onclick='rBtnClick(\"4\")' value='当年'/>"+
													"</div>");
												},{showWaiting : true})
											}else{
												functionIndex[index] = ydxx(ydqs,dataForBarGraph[  deviceIds[index]  ]);
												indexs = index;
												//四个按钮
												ydlll.append(
						"<div id='switchRangeDiv' style='position:absolute; left:90px; top:69px; z-index:99;'>"+
		            	"<input id='rBtn1' class='cBtn' type='button' onclick='rBtnClick(\"1\")' value='当前'/>"+
		            	"<input id='rBtn2' class='cBtn' type='button' onclick='rBtnClick(\"2\")' value='当天'/>"+
		            	"<input id='rBtn3' class='cBtn' type='button' onclick='rBtnClick(\"3\")' value='当月'/>"+
		            	"<input id='rBtn4' class='cBtn' type='button' onclick='rBtnClick(\"4\")' value='当年'/>"+
												"</div>");
											}  
											dataForGlobal[index] = Expenddata[ deviceIds[index]];
										}
									});
									
									
									
									
								}else{
									if(dataForGlobalLength[index] == 1){
										functionIndex[index] = ydxx(ydqs,dataForGlobal[index]);
										ydqs.append(
												"<div id='switchRangeDiv' style='position:absolute; left:90px; top:69px; z-index:99;'>"+
								            	"<input id='rBtn1' class='cBtn' type='button' onclick='rBtnClick(\"1\")' value='当前'/>"+
								            	"<input id='rBtn2' class='cBtn' type='button' onclick='rBtnClick(\"2\")' value='当天'/>"+
								            	"<input id='rBtn3' class='cBtn' type='button' onclick='rBtnClick(\"3\")' value='当月'/>"+
								            	"<input id='rBtn4' class='cBtn' type='button' onclick='rBtnClick(\"4\")' value='当年'/>"+
										"</div>");
										$("#rBtn" + buttonColor[index]).css("background-color","yellow");
									}else{
										functionIndex[index] = ydl(ydqs,dataForGlobal[index]);
										/*ydqs.append(
												"<div id='switchRangeDiv' style='position:absolute; left:90px; top:69px; z-index:99;'>"+
								            	"<input id='rBtn1' class='cBtn' type='button' onclick='rBtnClick(\"1\")' value='当前'/>"+
								            	"<input id='rBtn2' class='cBtn' type='button' onclick='rBtnClick(\"2\")' value='当天'/>"+
								            	"<input id='rBtn3' class='cBtn' type='button' onclick='rBtnClick(\"3\")' value='当月'/>"+
								            	"<input id='rBtn4' class='cBtn' type='button' onclick='rBtnClick(\"4\")' value='当年'/>"+
										"</div>");
										$("#rBtn" + buttonColor[index]).css("background-color","yellow");*/
									}
								}
								//hmUnBlockUI();
							});
							if (onOff == "0") {
								//    console.log(onOff);
								$(content_kt.node()).attr("onOff", 1);
								turnOnOff($(content_kt.node()))
							}
						}
						$(".content_kt_ydqs_btn").each(function() {
							var chartData = $(this).data( "chart" ) || $(this).parent().parent().parent().data("chart");
							var parent = $(this).parent();
							var index = +$(this).attr("index");
							var ydqs = parent.find(".ydqs");
							functionIndex[index] = ydl(ydqs,chartData);
						});
						  }
						continueRefresh = setTimeout(function() {
							doRefresh();
						}, 5000);
					}
					function turnTemp(temp) {
						var ten = Math.floor((temp / 10));
						var unit = temp % 10;
						if (ten == 0) {
							return "<span class='wd_" + unit + "'></span>";
						} else {
							return "<span class='wd_" + ten
									+ "'></span><span class='wd_" + unit
									+ "'></span>"
						}
					}
				});
var doRefresh = function() {
	var refreshURL = "/spmsuc/getDevicesCurrentStatus";
	var self = {
		gwid : gwId,
		last : lastTime,
		devices : devicesId,
		type : type
	}
	console.log(self);
	clearTimeout(continueRefresh);
	if (isDevice == 1) {
		doJsonRequest(refreshURL, self, function(dat) {
			if (dat.result) {
				//  console.log(dat)
				newInit(dat["data"]);
			}
			continueRefresh = setTimeout(function() {
				doRefresh();
			}, 5000);
		});
	}
}
function newInit(dat) {
//	console.log(dat);
	var gw = dat["gw"] || "";
	var gwStatus = gw["gwStatus"];
	if (gwStatus == "2" || gwStatus == "0") {
		d3.select(".gwStatus").classed("display_none", false);
	} else {
		d3.select(".gwStatus").classed("display_none", true);
	}
	var ac = dat["ac"];
	var acLen = ac.length;
	var sensor = dat["sensor"];
	var sensorLen = sensor.length;
	//   console.log($(".content_kt").length)
	
	$(".content")
			.find(".content_kt")
			.each(
					function() {
						var rootId = $(this).attr("id");
						var rootDiv = d3.select(this);
						var tempData = null;
						for ( var i = 0; i < acLen; i++) {
							if (rootId == ac[i]["deviceId"]) {
								tempData = ac[i];
								break;
							}
						}
						if (!tempData) {
							return;
						}
						
						//修改电池电量状态
						if(typeof($("#electricity"+tempData["deviceId"])) != "undefined"){
							var remain = tempData["remain"];
							$("#electricity"+tempData["deviceId"]).attr("class","electricity"+(remain==null? "" :remain));
						}
						
						var status = tempData["status"]
						setStatus(rootDiv, status);
						var onOff = tempData["onOff"];
						setOnOff(rootDiv.node(), onOff);

						var temp = tempData["temp"];
						setTemp(rootDiv, temp);

						var maxTemp = tempData["maxTemp"], minTemp = tempData["minTemp"], acTemp = tempData["acTemp"];
						setAcTemp(rootDiv, maxTemp, minTemp, acTemp);

						var mode = tempData["mode"];
						setMode(rootDiv, mode);

						var speed = tempData["speed"];
						if(speed==0){
							speed=7;						
						}
						setSpeed(rootDiv, speed);
						var newpower = tempData["newpower"];
						var chartNew = $(
								$(rootDiv.node()).find(".ydqs#" + rootId)[0])
								.highcharts();
						var chartRemain = $(rootDiv.node()).find(".content_kt_ydqs_btn");
						if(type[rootId] != '2'){
							setNewPower(rootDiv, chartNew, newpower,tempData["deviceId"]);
						}
					});
	//刷新图表
	function setNewPower(rootDiv, chartNew, newpower,deviceId) {
		//     console.log(chartNew);
		/* newpower = [
		 [1431768150000, 4400],
		 [1431783450000, 1500],
		 [1431800190000, 1500],
		 [1431985830000, 1500]
		 ]*/
		if (newpower.length) {
			var last;
			var series = chartNew.series[0].options.data;
			var seriesLen = series.length;
			if (!seriesLen) {
				var lastPoint = 0;
			} else {
				var lastPoint = series[seriesLen - 1];
			}

			var lastTimes = new Date(lastPoint[0]);
			var powerLen = newpower.length;
			for ( var m = 0; m < powerLen; m++) {
				if(changeFlag[deviceId]){
					break;
				}
				var newTime = new Date(newpower[m][0]);
				if (lastTimes.getMinutes() == newTime.getMinutes()) {
					series.pop();
					chartNew.series[0].setData(series);
					chartNew.series[0].addPoint([ newpower[m][0],
							newpower[m][1] ], true, false);
				} else if (lastTimes < newTime) {
					chartNew.series[0].addPoint([ newpower[m][0],
							newpower[m][1] ], true, false);
				}
			}
			if(changeFlag[deviceId]){
				changeFlag[deviceId] = false;
			}
			var nowSeries = chartNew.series[0].options.data;

			var nowSeriesLen = nowSeries.length;
			var id = $(rootDiv.node()).attr("id");
			if (nowSeriesLen) {
				lastTime[id] = nowSeries[nowSeriesLen - 1][0];
			} else {
				lastTime[id] = 0;
			}
			$(rootDiv.node()).data("chart", nowSeries);

		}
	}

	// 网关状态
	function setStatus(root, status) {
		//   return;
		if (status == "0" || status == "2") {
			root.select(".content_kt_nr").classed("display_none", true);
			root.select(".content_kt_nr_dk").classed("display_none", false);
		} else {
			root.select(".content_kt_nr").classed("display_none", false);
			root.select(".content_kt_nr_dk").classed("display_none", true);
		}

	}

	// 设定室内温度
	function setTemp(root, temp) {

		var ten = Math.floor(temp / 10);
		var one = temp % 10;
		var wddiv = root.select(".content_kt_nr").select(".content_kt_nr_sdwd")
				.select(".wd");
		var span0 = wddiv.selectAll("span")[0][0];
		var span1 = wddiv.selectAll("span")[0][1];
		$(span0).removeClass().addClass("wd_" + ten);
		$(span1).removeClass().addClass("wd_" + one);

	}

	// 设定空调温度
	function setAcTemp(root, maxTemp, minTemp, acTemp) {

		var ten = Math.floor(acTemp / 10);
		var one = acTemp % 10;
		var v = (acTemp - maxTemp) / (-(maxTemp - minTemp));
		var wdDiv = root.select(".content_kt_nr").select(
				".content_kt_nr_sdwd_wdsz").select(".wd");
		var span0 = wdDiv.selectAll("span")[0][0];
		var span1 = wdDiv.selectAll("span")[0][1];
		$(span0).removeClass().addClass("wd_" + ten);
		$(span1).removeClass().addClass("wd_" + one);
		var number = root.select(".content_kt_nr").select(
				".content_kt_nr_sdwd_wdsz").select(".content_kt_nr_sdwd_hk")
				.select(".number");
		number.select("#maxTemp").html(maxTemp + "℃");
		number.select("#minTemp").html(minTemp + "℃");
		number.select("#amongTemp").html(
				Math.floor((minTemp + maxTemp) / 2 - 1) + "℃")
		var content_kt_nr_fs_right_hd = root.select(".content_kt_nr").select(
				".content_kt_nr_sdwd_hk").select(".content_kt_nr_fs_right_hd");
		var config = {
			wdDiv : wdDiv.node(),
			gwId : gwId,
			deviceid : $(root.node()).attr("id"),
			min : minTemp,
			max : maxTemp,
			_defaultValue : v,
			renderTo : content_kt_nr_fs_right_hd.node()
		}
		var c1 = new Harmazing.chart.ControlBar(config);

	}

	// 设定开关状态
	function setOnOff(root, onOff) {
		/*   console.log(onOff);
		 console.log($(root).attr("onOff"))*/
		if (onOff == $(root).attr("onOff")) {
			return;
		}
		var parent = $(root);
		turnOnOff(parent)

	}

	function setMode(root, mode) {

		var xuehua = [ "auto", "jh", "xuehua", "sun", "chushi" ];
		var content_kt_nr_ms = root.select(".content_kt_nr").select(
				".content_kt_nr_ms");
		var canFind = content_kt_nr_ms.select(".content_kt_nr_ms_left").select(
				"#canFind");
		if ($(canFind.node()).hasClass("kt_close_none")) {
			$(canFind.node()).attr("mode", xuehua[mode]);
		} else {
			$(canFind.node()).removeClass().addClass(xuehua[mode]).attr("mode",
					xuehua[mode]);
		}
		var content_kt_nr_ms_right = content_kt_nr_ms
				.select(".content_kt_nr_ms_right");
		$(content_kt_nr_ms_right.node()).find("a").removeClass("hover").attr(
				"modeNum", mode);
		$(content_kt_nr_ms_right.node()).find("a:eq(" + mode + ")").addClass(
				"hover");

	}

	function setSpeed(root, speed) {

		var content_kt_nr_fs = root.select(".content_kt_nr_fs");
		var content_kt_nr_fs_right = content_kt_nr_fs
				.select(".content_kt_nr_fs_right");
		var div = content_kt_nr_fs_right.select("div");
		$(div.node())
				.removeClass()
				.addClass(
						"content_kt_nr_fs_right_pic content_kt_nr_fs_right_picNaN content_kt_nr_fs_right_pic"
								+ speed);
		var content_kt_nr_fs_right_hd = content_kt_nr_fs_right
				.select(".content_kt_nr_fs_right_hd");
		var v = (speed - 8) / (-(8 - 0));
		var config = {
			fsDiv : div.node(),
			gwId : gwId,
			deviceid : $(root.node()).attr("id"),
			min : 1,
			max : 8,
			_defaultValue : v,
			renderTo : content_kt_nr_fs_right_hd.node()
		}
		var c1 = new Harmazing.chart.ControlBar1(config);

	}
}

function turnOnOff(parent, id) {
	var onOff = parent.attr("onOff");
	if (onOff == "1") {
		onOff = 0;
	} else {
		onOff = 1;
	}
	parent.attr("onOff", onOff);
	var content_kt_nr_sdwd = parent.find(".content_kt_nr_sdwd").find(
			".content_kt_nr_sdwd_wdsz");

	content_kt_nr_sdwd.find("#defaulttemp").toggleClass("kt_close_none ml_45");
	content_kt_nr_sdwd.find("#defaulttemp").find("span").toggle();
	parent.find(".content_kt_nr_sdwd").find(".content_kt_nr_sdwd_hk").toggle();

	var content_kt_nr_ms = parent.find(".content_kt_nr_ms");
	var content_kt_nr_ms_left = content_kt_nr_ms.find(".content_kt_nr_ms_left");
	var className = content_kt_nr_ms_left.find("#canFind").attr("mode");
	content_kt_nr_ms_left.find("#canFind").toggleClass("kt_close_none")
			.toggleClass(className);
	content_kt_nr_ms.find(".content_kt_nr_ms_right").toggle();

	var content_kt_nr_fs = parent.find(".content_kt_nr_fs");
	var content_kt_nr_fs_left = content_kt_nr_fs.find(".content_kt_nr_fs_left");
	content_kt_nr_fs_left.find("#defaultfs").toggleClass("fengsu").toggleClass(
			"kt_close_none ml_45");
	content_kt_nr_fs.find(".content_kt_nr_fs_right").toggle();
	
	if(typeof(id) != "undefined"){
		var door = $("div.content_kt_door#" + id + "");// 找到门的大div模块
		var dqzt = door.find(".content_kt_door_state").eq(0);
		var offline = door.find(".door_close");
		var online = door.find(".door_open");

		if(onOff == 1){
			offline.removeClass("door_close");
			offline.addClass("door_open");
			dqzt.html("当前状态：开启");
			
		}else{
			online.removeClass("door_open");
			online.addClass("door_close");
			dqzt.html("当前状态：关闭");
		}
	}
}
function changeWeather(dat) {
	if (!dat["results"] || !dat["results"].length) {
		return;
	}
	return;
	dat = dat["results"][0];
	var currentCity = dat["currentCity"] + "&nbsp&nbsp&nbsp"
			|| "&nbsp&nbsp&nbsp";
	var weather_data = dat["weather_data"];
	var _data = "";
	if (weather_data && weather_data.length) {
		_data = weather_data[0];
		var date = _data["date"];
		//    console.log(date);
		var really = date.split("：")[1].split("℃")[0];
		//    console.log(really)
		var weather = _data["weather"] + "&nbsp&nbsp";
		weather += _data["temperature"];
		$(".top_center").find(".pt_27").find("b").html("室外温度&nbsp");
		$(".top_center").find(".font_family_a").find(".fz_28").html(
				really + "&nbsp<i style='font-size: 14px'>℃</i>");
		$(".top_center").find(".pt_30").find("b").html(currentCity);
		$(".top_center").find(".fl").find(".weather").html(weather).removeClass();
	}
}
function personInfo(spmsuser) {
	var fullname = spmsuser["fullname"] || "";
	var typeText = spmsuser["typeText"] || "";
	var bizAreaName = spmsuser["bizAreaName"] || "";
	var eleAreaName = spmsuser["eleAreaName"];
	var mobile = spmsuser["mobile"];
	var email = spmsuser["email"];
	var address = spmsuser["address"];
	var table = d3.select(".contentInfo").select("table");
	//
	table.select(".fullname").html(fullname)
	table.select(".typeText").html(typeText)
	table.select(".bizAreaName").html(bizAreaName)
	table.select(".eleAreaName").html(eleAreaName)
	table.select(".mobile").html(mobile)
	table.select(".email").html(email)
	table.select(".address").html(address)
}
function showInfo(that) {
	//alert(showInfo);
	addShowTimingSetJudge = 0;
	$("a").removeClass("hover");
	$(that).addClass("hover");
	$(".contentInfo").show();
	$(".contentInfoToSetting").hide();
	$(".content").hide();
	$(".Raircon").hide();
	$("#charts").hide();
	$("#chartstableDiv").hide();
	$('#showTimingSetDiv').hide();
	clearTimeout(continueRefresh);

}
function showAc(that) {
	//alert(showAc);
	addShowTimingSetJudge = 0;
	continueRefresh = setTimeout(function() {
		doRefresh();
	}, 5000);
	$("a").removeClass("hover");
	$(that).addClass("hover");
	$(".content").show();
	$(".contentInfo").hide();
	$("#showTimingSetDiv").hide();
	$("#chartstableDiv").hide();
	$("#charts").hide();
	$(".contentInfoToSetting").hide();
	$(".Raircon").hide();
	
	for ( var int = 0; int < devicesId.length; int++) {
		QueryCurveNumForCurveDevice(devicesId[int]);
		QueryClockNumForRedImg(devicesId[int]);
	}
	
}
function changePassword(that) {
	$(".change_password").toggle();
	$(that).toggleClass("up").toggleClass("down");
	$(".content_gerenzhongxin").toggleClass("h668");
	window.scrollTo(0, 200);

	$("#modifyPwd_button").click(function() {
		if ($("#oldpwd").val() == "") {
			alert("请输入旧密码");
			return;
		}
		if ($("#newpwdone").val() == "") {
			alert("请输入新密码");
			return;
		}
		if ($("#newpwdtwo").val() == "") {
			alert("请再次输入新密码");
			return;
		}
		if ($("#newpwdtwo").val() != $("#newpwdone").val()) {
			alert("新密码与确认密码不一致");
			return;
		}
		if (tapFlag) {
			return;
		}
		tapFlag = true;
		setTimeout(function() {
			tapFlag = false;
		}, 2000);
		var URL = "/spmsuc/modifyPwd";
		var self = {
			oldpwd : $("#oldpwd").val(),
			newpwd : $("#newpwdtwo").val()
		}
		doJsonRequest(URL, self, function(dat) {
			if (dat.result) {
				var da = dat["data"];
				if (da["success"]) {
					alert("修改成功");
				} else {
					alert(da["msg"]);
				}
			}
		}, {
			showWaiting : true,
			handdingInfo : "正在提交..",
			successInfo : "修改成功",
			failtureInfo : "修改失败"
		})
	});
}
/**
 * 空调异常相关
 */
$(function(){
	//error();
})
function error(){
	if( devicesId.length != 0 ){
		for ( var int = 0; int < devicesId.length; int++) {
			var s = $('.content_kt_nr_kg_ktkg[find='+devicesId[int]+']').parent().html();
			if(  s.indexOf("airconditioning_notopen") > 0  ){
				//alert(1);
				$('.content_kt_nr_kg_ktkg[find='+devicesId[int]+']').parent().children().eq(0).attr("onclick",'errorState(this)');
			}else{
				//alert(1);
				setTimeout('error()',5000);
			}
		}
	}else{
		setTimeout('error()',5000);
	}
}


/**
 * 获取用电统计
 */
function getlastTimeData(content_kt,deviceId,i){
	doJsonRequest("/spmsuc/getDeviceExpend", {deviceId:deviceId}, function (data) {
		data = data.data;
		var lastdata = data['datas'];
		if ( lastdata != null && lastdata.length > 0 ) {
			//var len = lastdata.length;
			lastTime[deviceId] = lastdata;//lastdata[len - 1][0];
			Expenddata[deviceId] = lastdata;
			$(content_kt.node()).data("chart", lastdata ).attr("index", i);
		} else{
			Expenddata[deviceId]  =  0;
		}
	})//,{showWaiting : true,async : false})
}

/**
 *	点击获取用电统计数据
 */
//function getlastTimeDataForOnclick(deviceId,rbtn){
function rBtnClick(id){
	buttonColor[indexs] = id;
	var lastdata ;
	$("#rBtn1").css("background-color","rgb(0, 51, 153)");
	$("#rBtn2").css("background-color","rgb(0, 51, 153)");
	$("#rBtn3").css("background-color","rgb(0, 51, 153)");
	$("#rBtn4").css("background-color","rgb(0, 51, 153)");
	
	doJsonRequest("/spmsuc/getDeviceExpend", {deviceId:deviceIds[indexs],type:type[deviceIds[indexs]],rbtn:id}, function (data) {
		data = data.data;
		lastdata = data['datas'];
		//Expenddata[deviceIds[indexs]] = lastdata;
		//ydlBarGraph(ydqss, lastdata);
		dataForGlobal[indexs] = lastdata;
		functionIndex[indexs] = ydxx(ydqss,lastdata);
		ydqss.append(
				"<div id='switchRangeDiv' style='position:absolute; left:90px; top:69px; z-index:99;'>"+
            	"<input id='rBtn1' class='cBtn' type='button' onclick='rBtnClick(\"1\")' value='当前'/>"+
            	"<input id='rBtn2' class='cBtn' type='button' onclick='rBtnClick(\"2\")' value='当天'/>"+
            	"<input id='rBtn3' class='cBtn' type='button' onclick='rBtnClick(\"3\")' value='当月'/>"+
            	"<input id='rBtn4' class='cBtn' type='button' onclick='rBtnClick(\"4\")' value='当年'/>"+
		"</div>");
		$("#rBtn" + id).css("background-color","yellow");
	},{showWaiting : true})
	return lastdata;
}
