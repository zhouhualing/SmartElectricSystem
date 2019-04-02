/**
 * @description: js
 * @author: Li JianHui (sillynemo)
 * @create: 14-9-22 下午1:14
 */
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});
$(document).ready(function () {
    var oldtemp;
    /* 旧温度 */
    var oldfs;
    /* 旧风速 */
    var oldms;
    /* 旧模式 */
    /* 更变自定义名用 */
    var olddeviceName;
    var flag = 1;
    /* END自定义名 */

    /*移除设备--空调*/
    function removeAc(deviceid) {
        $(".content_kt#" + deviceid + "").remove();
    }

    /*移除设备--窗传感器*/
    function removeWin(deviceid) {
        $(".content_kt_door_list#" + deviceid + "").remove();
    }

    /*移除设备--门传感器*/
    function removeDoor(deviceid) {
        $(".content_kt_door#" + deviceid + "").remove();
    }


    /* 下拉切换图表 */
    $(".qsordl").change(function () {
        var deviceid, data;
        deviceid = $(this).next().attr("id");
        $.each(datas, function (entryIndex, entry) {
            if (entry["deviceId"] == deviceid) {
                data = jQuery.parseJSON(entry["data"]);
            }
        });
        if ($(this).val() == 1) {
            var chart = $(this).next().highcharts();

            $(this).next().highcharts('StockChart', {
                chart: {
                    type: 'spline',
                    alignTicks: false,
                    backgroundColor: "rgba(0,0,0,0)"
                },
                navigator: {
                    enabled: true
                },
                rangeSelector: {
                    buttonTheme: { // styles
                        // for the
                        // buttons
                        fill: 'none',
                        stroke: 'none',
                        'stroke-width': 0,
                        r: 8,
                        style: {
                            color: '#039',
                            fontWeight: 'bold'
                        },
                        states: {
                            hover: {},
                            select: {
                                fill: '#039',
                                style: {
                                    color: 'white'
                                }
                            }
                        }
                    },
                    buttonSpacing: 20,
                    buttons: [
                        {
                            type: 'minute',
                            count: 60,
                            text: '1小时'
                        },
                        {
                            type: 'minute',
                            count: 720,
                            text: '12小时'
                        },
                        {
                            type: 'day',
                            count: 1,
                            text: '1天'
                        },
                        {
                            type: 'day',
                            count: 5,
                            text: '5天'
                        },
                        {
                            type: 'day',
                            count: 10,
                            text: '10天'
                        },
                        {
                            type: 'day',
                            count: 15,
                            text: '15天'
                        },
                        {
                            type: 'month',
                            count: 1,
                            text: '一个月'
                        },
                        {
                            type: 'all',
                            text: '全部'
                        }
                    ],
                    inputEnabled: false,
                    labelStyle: {
                        color: 'silver',
                        fontWeight: 'bold'
                    },
                    selected: 0
                },

                title: {
                    align: "left",
                    text: '用电情况',
                    //color : "#FFF"
                    style: {
                        color: "#FAFAFA"
                    }
                },

                legend: {
                    enabled: true,
                    verticalAlign: "top",
                    itemStyle: {
                        "color": "#FAFAFA"
                    }
                },
                yAxis: {
                    opposite: false,
                    labels: {
                        style: {
                            color: "#fff"
                        }
                    },
                    min: 0
                },
                xAxis: {
                    labels: {
                        style: {
                            color: "#fff"
                        }
                    }
                },
                series: [
                    {
                        name: '功率(单位:W)',
                        data: data,
                        color: "rgb(255,147,52)"
                    }
                ],
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                    // 禁用版权信息
                }
            });

        }
        if ($(this).val() == 2) {
            var chart = $(this).next().highcharts();
            $(this).next().highcharts('StockChart', {
                chart: {
                    type: 'column',
                    alignTicks: false,
                    backgroundColor: "rgba(0,0,0,0)"
                },
                navigator: {
                    enabled: true
                },
                rangeSelector: {
                    buttonTheme: { // styles for the buttons
                        fill: 'none',
                        stroke: 'none',
                        'stroke-width': 0,
                        r: 8,
                        style: {
                            color: '#039',
                            fontWeight: 'bold'
                        },
                        states: {
                            hover: {},
                            select: {
                                fill: '#039',
                                style: {
                                    color: 'white'
                                }
                            }
                        }
                    },
                    buttonSpacing: 20,
                    buttons: [
                        {
                            type: 'minute',
                            count: 60,
                            text: '1小时'
                        },
                        {
                            type: 'minute',
                            count: 720,
                            text: '12小时'
                        },
                        {
                            type: 'day',
                            count: 1,
                            text: '1天'
                        },
                        {
                            type: 'day',
                            count: 5,
                            text: '5天'
                        },
                        {
                            type: 'day',
                            count: 10,
                            text: '10天'
                        },
                        {
                            type: 'day',
                            count: 15,
                            text: '15天'
                        },
                        {
                            type: 'month',
                            count: 1,
                            text: '一个月'
                        },
                        {
                            type: 'all',
                            text: '全部'
                        }
                    ],
                    inputEnabled: false,
                    labelStyle: {
                        color: 'silver',
                        fontWeight: 'bold'
                    },
                    selected: 0
                },

                title: {
                    align: "left",
                    text: '用电情况',
                    style: {
                        color: "#FAFAFA"
                    }
                },

                legend: {
                    enabled: true,
                    verticalAlign: "top",
                    itemStyle: {
                        "color": "#FAFAFA"
                    }
                },
                yAxis: {
                    opposite: false,
                    //title:{text:"单位:千瓦(Kw)",align:"high",rotation:0,offset: 0},
                    //max:maxLoad + maxLoad/10,
                    min: 0
                },
                plotOptions: {
                    column: {
                        dataGrouping: {
                            groupPixelWidth: 200,
                            units: [
                                ['minute', [5]],
                                ['hour', [1]],
                                ['day', [1]],
                                ['month', [1, 3, 6]]
                            ]
                        }
                    }
                },
                series: [
                    {

                        name: '用电量(单位:Wh)',
                        data: data,
                        color: "rgb(255,147,52)"
                    }
                ],
                lang: {
                    noData: "暂无数据"
                },
                noData: {
                    style: {
                        fontWeight: 'bold',
                        fontSize: '15px',
                        color: '#303030'
                    }
                },
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                    // 禁用版权信息
                }
            });
        }
    });
    /* 切换图表end */

    /* 配置范围END */
    /*网关在线状态切换*/
    function gwStatusOffline() {
        $(".gwStatus").removeClass("display_none");
    }

    function gwStatusOnline() {
        $(".gwStatus").addClass("display_none");
    }

    /*网关在线状态切换END*/
    /* 设备离线与连线 */
    function deviceAcOnline(deviceid) {
        var ac = $("div.content_kt#" + deviceid + "");// 找到空调的大div模块
        var offline = ac.find(".content_kt_nr_dk");
        var online = ac.find(".content_kt_nr");

        offline.addClass("display_none");
        online.removeClass("display_none");

    }

    function deviceAcOffline(deviceid) {
        var ac = $("div.content_kt#" + deviceid + "");// 找到空调的大div模块
        var offline = ac.find(".content_kt_nr_dk");
        var online = ac.find(".content_kt_nr");

        online.addClass("display_none");
        offline.removeClass("display_none");

    }

    function deviceWinOnline(deviceid) {
        var win = $("div.content_kt_door_list#" + deviceid + "");// 找到窗的大div模块
        var offline = win.find(".content_kt_door_list_content#close");
        var online = win.find(".content_kt_door_list_content#open");

        offline.addClass("display_none");
        online.removeClass("display_none");
    }

    function deviceWinOffline(deviceid) {
        var win = $("div.content_kt_door_list#" + deviceid + "");// 找到窗的大div模块
        var offline = win.find(".content_kt_door_list_content#close");
        var online = win.find(".content_kt_door_list_content#open");

        online.addClass("display_none");
        offline.removeClass("display_none");
    }

    function deviceDoorOnline(deviceid) {
        var door = $("div.content_kt_door#" + deviceid + "");// 找到门的大div模块
        var offline = door.find(".content_kt_door_list_content#close");
        var online = door.find(".content_kt_door_list_content#open");

        offline.addClass("display_none");
        online.removeClass("display_none");
    }

    function deviceDoorOffline(deviceid) {
        var door = $("div.content_kt_door#" + deviceid + "");// 找到门的大div模块
        var offline = door.find(".content_kt_door_list_content#close");
        var online = door.find(".content_kt_door_list_content#open");

        online.addClass("display_none");
        offline.removeClass("display_none");
    }

    /* 离线与连线END */
    /* 获取并更改设备配置项（制热最高， 制冷最低，允许制热） */
    function updateDeviceInfo(deviceid, maxTemp, minTemp, allowHeat, act, speed) {
        /*
         * 1、按deviceid 找到对应的空调的模块 2、把温度尺和模式的图标做修改（如果不允许制热， 则制热的小图和大图不显示） 3、重绘控件
         * 4、改变的时候， 传比例，不传真值
         */
        var c1, c2;
        if (speed == 0) {
            speed = 7;
        }
        //maxTemp = '32';
        $.each(cbs, function (entryIndex, entry) {
            if (entry["deviceId"] == deviceid) {
                c1 = entry["cb"];
                c2 = entry["cb2"];
            }
        })
        var ac = $("div.content_kt#" + deviceid + "");// 找到空调的大div模块
        ac.find("#maxTemp").html("" + maxTemp + "℃");
        ac.find("#minTemp").html("" + minTemp + "℃");
        ac
            .find("#amongTemp")
            .html(""
                + (parseInt(minTemp) + parseInt((maxTemp - minTemp) / 2))
                + "℃");// 修改刻度尺

        var sunmode = ac.find("a.sun");// 制热按钮显示和隐藏
        if (allowHeat == 0) {
            sunmode.css("display", "none");
        } else {
            sunmode.css("display", "block");
        }
        maxTemp = parseInt(maxTemp);
        minTemp = parseInt(minTemp);
        act = parseInt(act);
        speed = parseInt(speed);
        if (act > maxTemp) {
            act = maxTemp;
        }
        if (act < minTemp) {
            act = minTemp;
        }

        // 重新绘制控件按钮
        var callbackFunction = function (value) {
            var trueValue = Math.round(-value * (maxTemp - minTemp) + maxTemp);
            changeValue(ac.find(".content_kt_nr_sdwd_wdsz").children(".fz_22")
                .siblings()[0].children[0], Math.floor(trueValue
                / 10), "wd_");
            changeValue(ac.find(".content_kt_nr_sdwd_wdsz").children(".fz_22")
                .siblings()[0].children[1], (trueValue) % 10, "wd_");
        };
        var config = {};
        config.bgImageUrl = _ctx + "/spms/pc2.png";
        config.buttonImageUrl = _ctx + "/spms/pc1.png";
        config.callbackFunction = callbackFunction;
        config.clearRefresh = function () {
            clearTimeout(global);
        };
        var v = (act - maxTemp) / (-(maxTemp - minTemp));

        config._defaultValue = v;
        /* 拖拽完成执行回调 */
        config.endCallbackFunction = function (v) {
            var newtemp = Math.round(-v * (maxTemp - minTemp) + maxTemp);
            if (oldtemp != newtemp) {
                var mk = window.addMask(" ");
                $.ajax({
                    type: "POST",
                    timeout: 10000,
                    url: ctx + "/spms/spmsUser/setAcTemp",
                    data: {
                        deviceid: deviceid,
                        newtemp: newtemp,
                        oldtemp: oldtemp,
                        gwid: gwid
                    },
                    dataType: "json",
                    success: function (resObj) {
                        if (resObj.success) {
                            $.changeSDWD(deviceid, newtemp);
                        } else {
                            alert("ERROR:" + resObj.msg);
                            $.changeSDWD(deviceid, oldtemp);
                        }

                    },
                    error: function (resObj) {

                        $.changeSDWD(deviceid, oldtemp);
                    },
                    complete: function () {
                        setTimeout(window.removeMask, 1000);
                    }

                });

            }
            global = setTimeout("$.updateAllDevice()", 5000);
        };

        config.renderTo = ac.find(".content_kt_nr_fs_right_hd#wd")[0];
        $(config.renderTo).html("");
        c1 = new Harmazing.chart.ControlBar(config);

        function callbackFunction2(value) {
            changeValue(ac.find(".fengsu")[0], Math.round(-value * 6 + 7), "f")
            changeValue(ac.find(".content_kt_nr_fs")
                    .find(".content_kt_nr_fs_right")[0].children[0],
                Math.round(-value * 6

                    + 7), "content_kt_nr_fs_right_pic");
        }

        // 右边的滚动条
        var config2 = {};
        config2.bgImageUrl = _ctx + "/spms/pc2.png";
        config2.buttonImageUrl = _ctx + "/spms/pc1.png";
        config2.callbackFunction = callbackFunction2;
        config2.clearRefresh = function () {
            clearTimeout(global);
        };
        config2.renderTo = ac.find(".content_kt_nr_fs_right_hd")[1];
        //default_fs = ac.find("#defaultfs").attr("defaultspeed")
        var value = (speed - 7) / (-6);
        //config2._defaultValue = parseFloat(value);
        config2._defaultValue = value;
        /* 松开鼠标的回调函数 */
        config2.endCallbackFunction = function (v) {
            var newfs = Math.round(-v * 6 + 7);
            if (oldfs != newfs) {
                var mk = window.addMask(" ");
                $.ajax({
                    type: "POST",
                    timeout: 10000,
                    url: ctx + "/spms/spmsUser/setAcFs",
                    data: {
                        deviceid: deviceid,
                        newfs: newfs,
                        oldfs: oldfs,
                        gwid: gwid
                    },
                    dataType: "json",
                    success: function (resObj) {
                        if (resObj.success) {
                            $.changeSDFS(deviceid, newfs);
                        } else {
                            alert("ERROR:" + resObj.msg);
                            $.changeSDFS(deviceid, oldfs);
                        }
                    },
                    error: function (resObj) {

                        $.changeSDFS(deviceid, oldfs);
                    },
                    complete: function () {
                        setTimeout(window.removeMask, 1000);
                    }

                });

            }
            global = setTimeout("$.updateAllDevice()", 5000);
        };
        $(config2.renderTo).html("");
        c2 = new Harmazing.chart.ControlBar(config2);

        $.each(cbs, function (entryIndex, entry) {
            if (entry["deviceId"] == deviceid) {
                entry["cb"] = c1;
                entry["cb2"] = c2;
            }
        })

        // 重绘END
    }

    /* END */

    /* 更新所有门窗信息 */
    $.extend({
        'updateWinDoors': function () {
            var mk = window.addMask(" ");
            $.ajax({
                type: "POST",
                timeout: 10000,
                url: ctx + "/spms/spmsUser/updateWinDoors",
                data: {
                    gwid: gwid,
                    devices: devices
                },
                dataType: "json",
                async: false,
                cache: false,
                success: function (resObj) {
                    var arr = eval(resObj);
                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i].type == 3) {
                            if (arr[i].onoff == 1) {
                                $.openDoor(arr[i].id,
                                    arr[i].remain);

                            }
                            if (arr[i].onoff == 0) {
                                $.closeDoor(arr[i].id,
                                    arr[i].remain);
                            }
                        }
                        if (arr[i].type == 4) {
                            if (arr[i].onoff == 1) {
                                $.openWin(arr[i].id,
                                    arr[i].remain);
                            }
                            if (arr[i].onoff == 0) {
                                $.closeWin(arr[i].id,
                                    arr[i].remain);
                            }
                        }
                    }
                    setTimeout(window.removeMask, 1000);
                },
                error: function () {
                    alert("error");
                    setTimeout(window.removeMask, 1000);
                }
            });

        }
    });

    /* 更新所有设备信息 */
    $.extend({
        'updateAllDevice': function () {
            var params = {
                gwid: gwid,
                devices: devices
            };
            $.each(lastdata, function (key, val) {
                for (var key in val) {
                    params[key] = val[key];
                }
            });

            $.ajax({
                type: "POST",
                timeout: 10000,
                url: ctx + "/spms/spmsUser/getDevicesCurrentStatus",
                data: params,
                dataType: "json",
                async: true,
                cache: false,
                success: function (resObj) {
                    var arr = eval(resObj);
                    //23456
                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i].type == 1) {

                            if (arr[i].gwStatus == 1) {
                                //隐藏“网关不在线”的提示框
                                gwStatusOnline();
                            } else {
                                //显示“网关不在线”的提示框
                                gwStatusOffline();
                            }
                        }
                        if (arr[i].type == 2) {
                            if (arr[i].success) {
                                if (arr[i].status == 1) {
                                    if (arr[i].onOff == 1) {

                                        deviceAcOnline(arr[i].deviceId);
                                        $.openAc(arr[i].deviceId, arr[i].temp,
                                            arr[i].acTemp, arr[i].mode,
                                            arr[i].speed, arr[i].maxTemp,
                                            arr[i].minTemp,
                                            arr[i].allowHeat);
                                        clearTimeout(global); //取消第一次openAC中的定时器
                                        $.openAc(arr[i].deviceId, arr[i].temp,
                                            arr[i].acTemp, arr[i].mode,
                                            arr[i].speed, arr[i].maxTemp,
                                            arr[i].minTemp,
                                            arr[i].allowHeat);
                                        //8888
                                        //alert($("div.content_kt#"+ arr[i].deviceId+ "").find(".qsordl").val());
                                        if ($("div.content_kt#" + arr[i].deviceId + "").find(".qsordl").val() == 1) {
                                            var chart1 = $($("div.content_kt#" + arr[i].deviceId + "").find(".ydqs#" + arr[i].deviceId + "")[0]).highcharts();

                                            if (arr[i].newpower.length > 0) {
                                                var last;
                                                $.each(arr[i].newpower,
                                                    function (index, eum) {
                                                        chart1.series[0]
                                                            .addPoint(
                                                            [
                                                                arr[i].newpower[index][0],
                                                                arr[i].newpower[index][1]],
                                                            true,
                                                            false);
                                                        last = arr[i].newpower[index][0];
                                                        $.each(datas, function (entryIndex, entry) {
                                                            if (entry["deviceId"] == arr[i].deviceId) {
                                                                var a = [arr[i].newpower[index][0], arr[i].newpower[index][1]];
                                                                var b = eval(entry['data']);
                                                                b.push(a);
                                                            }
                                                        })
                                                    })
                                                //新数据加入原始数据123

                                                $.each(lastdata, function (entryIndex, entry) {
                                                    if (entry[arr[i].deviceId]) {
                                                        entry[arr[i].deviceId] = last;
                                                    }
                                                })
                                            }
                                        } else if ($("div.content_kt#" + arr[i].deviceId + "").find(".qsordl").val() == 2) {
                                            //9999
                                            var chart1 = $($("div.content_kt#" + arr[i].deviceId + "").find(".ydqs#" + arr[i].deviceId + "")[0]).highcharts();
                                            if (arr[i].newpower.length > 0) {
                                                var last;
                                                for (var m = 0; m < arr[i].newpower.length; m++) {
                                                    var dldata;
                                                    last = arr[i].newpower[m][0];
                                                    dldata = chart1.series[0].options.data;
                                                    var lastponit = dldata[dldata.length - 1];
                                                    var lasttime = new Date(lastponit[0]);
                                                    var newtime = new Date(arr[i].newpower[m][0]);
                                                    if (lasttime.getMinutes() == newtime.getMinutes()) {
                                                        //删除最后一个点
                                                        dldata.pop();
                                                        chart1.series[0].setData(dldata);
                                                        chart1.series[0].addPoint([arr[i].newpower[m][0], arr[i].newpower[m][1]], true, false);
                                                    } else if (lasttime < newtime) {
                                                        chart1.series[0].addPoint([arr[i].newpower[m][0], arr[i].newpower[m][1]], true, false);
                                                    }/* else {
                                                        chart1.series[0].addPoint([arr[i].newpower[m][0], arr[i].newpower[m][1]], true, false);
                                                    }*/
                                                    $.each(datas, function (entryIndex, entry) {
                                                        if (entry["deviceId"] == arr[i].deviceId) {
                                                            var a = [arr[i].newpower[m][0], arr[i].newpower[m][1]];
                                                            var b = eval(entry['data']);
                                                            b.push(a);
                                                        }
                                                    })

                                                }
                                                $.each(lastdata, function (entryIndex, entry) {
                                                    if (entry[arr[i].deviceId]) {
                                                        entry[arr[i].deviceId] = last;
                                                    }
                                                })
                                            }

                                        }
                                    } else if (arr[i].onOff == 0) {
                                        // 关闭状态，还是要获取室内温度
                                        deviceAcOnline(arr[i].deviceId);
                                        $.closeAc(arr[i].deviceId);
                                        $.changeSNWD(arr[i].deviceId,
                                                arr[i].temp + "");

                                        if ($("div.content_kt#" + arr[i].deviceId + "").find(".qsordl").val() == 1) {
                                            var chart1 = $($("div.content_kt#" + arr[i].deviceId + "").find(".ydqs#" + arr[i].deviceId + "")[0]).highcharts();

                                            if (arr[i].newpower.length > 0) {
                                                var last;
                                                $.each(arr[i].newpower,
                                                    function (index, eum) {
                                                        chart1.series[0]
                                                            .addPoint(
                                                            [
                                                                arr[i].newpower[index][0],
                                                                arr[i].newpower[index][1]],
                                                            true,
                                                            false);
                                                        last = arr[i].newpower[index][0];
                                                        $.each(datas, function (entryIndex, entry) {
                                                            if (entry["deviceId"] == arr[i].deviceId) {
                                                                var a = [arr[i].newpower[index][0], arr[i].newpower[index][1]];
                                                                var b = eval(entry['data']);
                                                                b.push(a);
                                                            }
                                                        })
                                                    })
                                                //新数据加入原始数据123

                                                $.each(lastdata, function (entryIndex, entry) {
                                                    if (entry[arr[i].deviceId]) {
                                                        entry[arr[i].deviceId] = last;
                                                    }
                                                })
                                            }
                                        } else if ($("div.content_kt#" + arr[i].deviceId + "").find(".qsordl").val() == 2) {
                                            //9999
                                            var chart1 = $($("div.content_kt#" + arr[i].deviceId + "").find(".ydqs#" + arr[i].deviceId + "")[0]).highcharts();
                                            if (arr[i].newpower.length > 0) {
                                                var last;
                                                for (var m = 0; m < arr[i].newpower.length; m++) {
                                                    var dldata;
                                                    last = arr[i].newpower[m][0];
                                                    dldata = chart1.series[0].options.data;
                                                    var lastponit = dldata[dldata.length - 1];
                                                    var lasttime = new Date(lastponit[0]);
                                                    var newtime = new Date(arr[i].newpower[m][0]);
                                                    if (lasttime.getMinutes() == newtime.getMinutes()) {
                                                        //删除最后一个点
                                                        dldata.pop();
                                                        chart1.series[0].setData(dldata);
                                                        chart1.series[0].addPoint([arr[i].newpower[m][0], arr[i].newpower[m][1]], true, false);
                                                    } else if (lasttime.getMinutes() < newtime.getMinutes()) {
                                                        chart1.series[0].addPoint([arr[i].newpower[m][0], arr[i].newpower[m][1]], true, false);
                                                    } else {
                                                        chart1.series[0].addPoint([arr[i].newpower[m][0], arr[i].newpower[m][1]], true, false);
                                                    }
                                                    $.each(datas, function (entryIndex, entry) {
                                                        if (entry["deviceId"] == arr[i].deviceId) {
                                                            var a = [arr[i].newpower[m][0], arr[i].newpower[m][1]];
                                                            var b = eval(entry['data']);
                                                            b.push(a);
                                                        }
                                                    })

                                                }
                                                $.each(lastdata, function (entryIndex, entry) {
                                                    if (entry[arr[i].deviceId]) {
                                                        entry[arr[i].deviceId] = last;
                                                    }
                                                })
                                            }

                                        }
                                    } else {
                                        deviceAcOffline(arr[i].deviceId);
                                    }
                                } else {
                                    deviceAcOffline(arr[i].deviceId);
                                }

                            } else {
                                alert("ERROR:" + arr[i].msg);
                            }
                        }
                        if (arr[i].type == 3) {
                            if (arr[i].success) {
                                if (arr[i].status == 1) {
                                    deviceDoorOnline(arr[i].deviceId);
                                    if (arr[i].onOff == 1) {
                                        $.openDoor(arr[i].deviceId,
                                            arr[i].remain);
                                        if (arr[i].newtime != null
                                            && arr[i].newtime != "") {
                                            var chart = $($(".content_kt_door#"
                                                + arr[i].deviceId + "")
                                                .find(".kgjl")[0])
                                                .highcharts();
                                            chart.series[0]
                                                .addPoint(
                                                [
                                                    arr[i].newtime,
                                                    arr[i].newopenclose],
                                                true, false);
                                        }

                                    } else if (arr[i].onOff == 0) {
                                        /* 门窗关闭了，还是要更新电量 */
                                        deviceDoorOnline(arr[i].deviceId);
                                        $.closeDoor(arr[i].deviceId,
                                            arr[i].remain);

                                        if (arr[i].newtime != null
                                            && arr[i].newtime != "") {
                                            var chart = $($(".content_kt_door#"
                                                + arr[i].deviceId + "")
                                                .find(".kgjl")[0])
                                                .highcharts();
                                            //abc
                                            chart.series[0]
                                                .addPoint(
                                                [
                                                    arr[i].newtime,
                                                    arr[i].newopenclose],
                                                true, false);
                                        }

                                    } else {
                                        deviceDoorOffline(arr[i].deviceId);
                                    }
                                } else {
                                    deviceDoorOffline(arr[i].deviceId);
                                }

                            } else {
                                alert("ERROR:" + arr[i].msg);
                            }
                        }
                        if (arr[i].type == 4) {
                            if (arr[i].success) {
                                if (arr[i].status == 1) {
                                    deviceWinOnline(arr[i].deviceId);
                                    if (arr[i].onOff == 1) {
                                        $.openWin(arr[i].deviceId,
                                            arr[i].remain);
                                    } else if (arr[i].onOff == 1) {
                                        /* 门窗关闭了，还是要更新电量 */
                                        deviceWinOnline(arr[i].deviceId);
                                        $.closeWin(arr[i].deviceId,
                                            arr[i].remain);

                                    } else {
                                        deviceWinOffline(arr[i].deviceId);
                                    }
                                } else {
                                    deviceWinOffline(arr[i].deviceId);
                                }

                            } else {
                                alert("ERROR:" + arr[i].msg);
                            }
                        }
                    }
                    global = setTimeout("$.updateAllDevice()", 5000);
                },
                error: function () {

                    global = setTimeout("$.updateAllDevice()", 5000);
                }
            });
        }
    });

    /* 打开chuang传感器 */
    $.extend({
        'openWin': function (deviceid, remain) {
            var kt_list_title = $("span#" + deviceid + ".fl").parent();
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".window_close").addClass("window_open")
                .removeClass("window_close");
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state").children("table")
                .empty();
            kt_list_title
                .siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state")
                .children("table")
                .append("<tr><td width='52%' align='right'>当前状态：</td><td width='48%'>打开</td></tr><tr><td align='right'>电量：</td><td class='electricity4'></td></tr>");
        }
    });
    /* 打开men传感器 */
    $.extend({
        'openDoor': function (deviceid, remain) {
            var kt_list_title = $("span#" + deviceid + ".fl").parent();
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".door_close").addClass("door_open")
                .removeClass("door_close");
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state").children("table")
                .empty();
            kt_list_title
                .siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state")
                .children("table")
                .append("<tr><td width='52%' align='right'>当前状态：</td><td width='48%'>打开</td></tr><tr><td align='right'>电量：</td><td class='electricity4'></td></tr>");
        }
    });
    /* 打开空调(id，室内温度，设定温度，模式，风速) */
    $.extend({
        'openAc': function (deviceid, rt, act, mode, speed, maxTemp, minTemp, allow) {
            /* 打开空调的时候初始化控件 */
            $("#wdnum").removeClass("display_none");
            updateDeviceInfo(deviceid, maxTemp, minTemp, allow, act,
                speed);

            $.changeSNWD(deviceid, rt);
            $.changeSDWD(deviceid, act);
            $.changeSDMS(deviceid, mode);
            $.changeSDFS(deviceid, speed);
        }
    });
    /* 关闭空调函数 */
    $.extend({
        'closeAc': function (deviceid) {
            var kt_nr_kg = $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_kg");

            var sdwd = kt_nr_kg.siblings(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_wdsz")
                .children(".wd");
            sdwd.addClass("kt_close_none").removeClass("wd")
                .addClass("ml_45");
            sdwd.empty();

            var sdwd_hk = kt_nr_kg.siblings(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_hk");
            sdwd_hk.addClass("display_none");

            var ms_left = kt_nr_kg.siblings(".content_kt_nr_ms")
                .children(".content_kt_nr_ms_left");
            ms_left.empty();
            ms_left.append('<div class="fz_22 pb_25">模式</div>');
            ms_left.append(' <div class="kt_close_none  ml_10"></div>');

            var ms_right = kt_nr_kg.siblings(".content_kt_nr_ms")
                .children(".content_kt_nr_ms_right");
            ms_right.addClass("display_none");

            var fs_left = kt_nr_kg.siblings(".content_kt_nr_fs")
                .children(".content_kt_nr_fs_left");
            fs_left.empty();
            fs_left.append(' <div class="fz_22 pb_25">风速</div>');
            fs_left.append(' <div class="kt_close_none  ml_10"></div>');
            var fs_right = kt_nr_kg.siblings(".content_kt_nr_fs")
                .children(".content_kt_nr_fs_right");
            fs_right.addClass("display_none");
        }
    });

    /* 关闭窗子函数 */
    $.extend({
        'closeWin': function (deviceid, remain) {
            var kt_list_title = $("span#" + deviceid + ".fl").parent();
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".window_open").addClass("window_close")
                .removeClass("window_open");
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state").children("table")
                .empty();
            kt_list_title
                .siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state")
                .children("table")
                .append("<tr><td width='52%' align='right'>当前状态：</td><td width='48%'>关闭</td></tr><tr><td align='right'>电量：</td><td class='electricity"
                    + remain + "'></td></tr>");
        }
    });

    /* 关闭门函数 */
    $.extend({
        'closeDoor': function (deviceid, remain) {
            var kt_list_title = $("span#" + deviceid + ".fl").parent();
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".door_open").addClass("door_close")
                .removeClass("door_open");
            kt_list_title.siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state").children("table")
                .empty();
            kt_list_title
                .siblings(".content_kt_door_list_content#open")
                .children(".content_kt_door_state")
                .children("table")
                .append("<tr><td width='52%' align='right'>当前状态：</td><td width='48%'>关闭</td></tr><tr><td align='right'>电量：</td><td class='electricity"
                    + 4 + "'></td></tr>");
        }
    });

    /* 改变电量 */
    $.extend({
        'changeRemain': function (deviceid, newRemain) {
            newRemain = 4;
            /* 先根据deviceid找到对应的代码块 */
            var remain = $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_door_list_content")
                .children(".content_kt_door_state")
                .find("[class^='electricity']");
            remain.attr("class", "");
            remain.addClass("electricity" + newRemain);
        }
    });

    /* 改变室内温度 */
    $.extend({
        'changeSNWD': function (deviceid, newSnwd) {
            newSnwd = newSnwd + "";
            /* 先根据deviceid找到对应的代码块 */
            var snwd = $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_snwd");
            var temp = snwd.children()
            temp.remove();
            var str = '';
            str = str + "<div class='wd'>"
            // 把收到的新温度先解析称成数组，再遍历数组拼装html语句
            var wd = newSnwd.replace(/(.)(?=[^$])/g, "$1,").split(",");
            $.each(wd, function (n, value) {
                var temp = "<span class='wd_" + value + "'/>";
                str = str + temp;
            });
            snwd.append(str + '<span class="wd_celsius"></span>'
                + "</div>");

        }
    });

    /* 更新温度 */
    $.extend({
        'changeSDWD': function (deviceid, newSnwd) {
            $("span#" + deviceid + ".fl").parent().siblings(".content_kt_nr")
                .children(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_hk")
                .removeClass("display_none");
            $("span#" + deviceid + ".fl").parent().siblings(".content_kt_nr")
                .children(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_wdsz").children(".fz_22")
                .siblings().attr("class", "wd");
            $("span#" + deviceid + ".fl").parent().siblings(".content_kt_nr")
                .children(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_wdsz").children(".fz_22")
                .siblings(".wd").empty();
            $("span#" + deviceid + ".fl")
                .parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_sdwd")
                .children(".content_kt_nr_sdwd_wdsz")
                .children(".fz_22")
                .siblings()
                .append("<span class='wd_0'></span><span class='wd_0'></span><span class='wd_celsius'></span>");
            var a, b;
            $.each(datainfo, function (entryIndex, entry) {
                if (entry["deviceId"] == deviceid) {
                    a = entry["max"];
                    b = entry["min"];
                }
            })
            a = parseInt(a);
            b = parseInt(b);
            if (newSnwd > a) {
                newSnwd = a;
            }

            if (newSnwd < b) {
                newSnwd = b;
            }
            var value = (newSnwd - a) / (-(a - b));
            var c;
            $.each(cbs, function (entryIndex, entry) {
                if (entry["deviceId"] == deviceid) {
                    c = entry["cb"];
                }
            })
            c.changeDefaultValue(value);
        }
    });

    /* 更新模式 */
    $.extend({
        'changeSDMS': function (deviceid, newSdms) {
            /* 先根据deviceid找到对应的代码块 */
            /* 把收到的代码转化成对应模式名的字符串 */
            var value = "";
            if (newSdms == 0) {
                value = "auto";
            } else if (newSdms == 1) {
                value = "jh";
            } else if (newSdms == 2) {
                value = "xuehua";
            } else if (newSdms == 3) {
                value = "sun";
            } else if (newSdms == 4) {
                value = "chushi";
            }
            var ms_left = $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_ms")
                .children(".content_kt_nr_ms_left");
            ms_left.children(".fz_22").next().attr("class", value);
            var ms_right = $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_ms")
                .children(".content_kt_nr_ms_right");
            ms_right.removeClass("display_none");
            ms_right.children("a").removeClass("hover");
            ms_right.children("a." + value + "").addClass(" hover");
        }
    });

    /* 更新风速 */
    $.extend({
        'changeSDFS': function (deviceid, newSdfs) {
            newSdfs = parseInt(newSdfs);
            //newSdfs = 1;
            if (newSdfs == 0) {
                newSdfs = 7;
            }
            $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_fs")
                .children(".content_kt_nr_fs_right")
                .removeClass("display_none");
            $("span#" + deviceid + ".fl").parent()
                .siblings(".content_kt_nr")
                .children(".content_kt_nr_fs")
                .children(".content_kt_nr_fs_left")
                .children(".fz_22").siblings().attr("class",
                "fengsu");
            var value = (newSdfs - 7) / (-6);

            var c2;
            $.each(cbs, function (entryIndex, entry) {
                if (entry["deviceId"] == deviceid) {
                    c2 = entry["cb2"];
                }
            })

            c2.changeDefaultValue(value);

        }
    });
    var button = $(".show_or_hide_button");
    var con = $(".content_gerenzhongxin");
    button.on("click", function () {
        if (button.data("show")) {
            button.data("show", false);
            button.html("");
            button.addClass("down").removeClass("up");
            con.animate({
                height: "440px"
            }, 250);
        } else {
            button.data("show", true);
            button.html("");
            con.animate({
                height: "668px"
            }, 250);
            $('html, body, .content').animate({
                scrollTop: $(document).height()
            }, 300);
            button.addClass("up").removeClass("down");
        }
    });

    /* 个人中心——变更密码 */
    $("#modifyPwd_button").click(function () {
        var oldpwd = $("#oldpwd").val();
        var newpwdone = $("#newpwdone").val();
        var newpwdtwo = $("#newpwdtwo").val();

        if (oldpwd == '') {
            alert("旧密码不能为空");
            return;
        }
        if (newpwdone == '') {
            alert("新密码不能为空");
            return;
        }
        if (newpwdtwo == '') {
            alert("确认新密码不能为空");
            return;
        }
        if (newpwdtwo != newpwdone) {
            alert("两次输入的密码不同，请确认！");
            $("#newpwdone").focus();
            return;
        }
        var mk = window.addMask(" ");
        $.ajax({
            type: "POST",
            timeout: 10000,
            url: ctx + "/spms/spmsUser/modifyPwd",
            data: {
                oldpwd: oldpwd,
                newpwd: newpwdone
            },
            dataType: "json",
            async: false,
            success: function (resObj) {
                setTimeout(window.removeMask, 1000);
                button.data("show", false);
                button.html("");
                button.addClass("down").removeClass("up");
                con.animate({
                    height: "440px"
                }, 250);
                /* 清空密码输入框 */
                $("#oldpwd").val('');
                $("#newpwdone").val('');
                $("#newpwdtwo").val('');
                alert("消息：" + resObj.msg);

            },
            error: function () {
                alert("error");
            }
        });

    });

    /* 设置自定名称 */
    $(".bj").click(function () {
        if (flag === 1) {
            var info = $(this).parent().siblings(".fl");
            olddeviceName = $(info).text();
            var input = $("<input type='text' value='" + olddeviceName + "'/>");
            info.html(input);
            input.click(function () {
                return false;
            });
            input.val("").focus().val(olddeviceName);
            flag = 0;
            input.keydown(function (event) {
                if (event.keyCode == 13) {
                    var deviceid = $(info).attr("id");
                    var newtxt = input.val();
                    if (newtxt == "") {
                        alert("自定义名不能为空");
                        input.val("").focus().val(olddeviceName);
                    } else {
                        if (newtxt != olddeviceName) {
                            var mk = window.addMask(" ");
                            $.ajax({
                                type: "POST",
                                timeout: 10000,
                                url: ctx
                                    + "/spms/spmsUser/changeCustomName",
                                data: {
                                    newname: newtxt,
                                    deviceid: deviceid
                                },
                                dataType: "json",
                                async: true,
                                success: function (resObj) {
                                    setTimeout(window.removeMask, 1000);
                                    if (resObj.success) {
                                        info.html(newtxt);
                                    } else {
                                        alert("自定义设备名错误：" + resObj.msg);
                                        info.html(txt);
                                    }
                                },
                                error: function () {
                                    setTimeout(window.removeMask, 1000);
                                    alert("error");
                                }
                            });
                        } else {
                            info.html(newtxt);
                        }
                        flag = 1;
                    }
                }
            });
        } else {
            var info = $(this).parent().siblings(".fl");
            var deviceid = $(info).attr("id");
            var input = info.children("input");
            var newtxt = input.val();
            if (newtxt == "") {
                alert("自定义名不能为空");
                input.val("").focus().val(olddeviceName);
            } else {
                if (newtxt != olddeviceName) {
                    var mk = window.addMask(" ");
                    $.ajax({
                        type: "POST",
                        timeout: 10000,
                        url: ctx + "/spms/spmsUser/changeCustomName",
                        data: {
                            newname: newtxt,
                            deviceid: deviceid
                        },
                        dataType: "json",
                        async: false,
                        success: function (resObj) {
                            setTimeout(window.removeMask, 1000);
                            if (resObj.success) {
                                info.html(newtxt);
                            } else {
                                alert("自定义设备名错误：" + resObj.msg);
                                info.html(txt);
                            }
                        },
                        error: function () {
                            setTimeout(window.removeMask, 1000);
                            alert("error");
                        }
                    });
                } else {
                    info.html(newtxt);
                }
                flag = 1;
            }
        }
    });

    /* close or open 空调 */
    $(".content_kt_nr_kg_ktkg").click(function () {
        var deviceid = $(this).parent().parent()
            .siblings(".content_kt_list_title").children(".fl").attr("id");
        if ($(this).parent().siblings(".content_kt_nr_sdwd")
            .children(".content_kt_nr_sdwd_wdsz").children().hasClass('wd')) {
            var mk = window.addMask(" ");
            /* 始终显示室内温度 */
            /*
             * var nrwd =
             * $(this).siblings(".content_kt_nr_sdwd").children(".content_kt_nr_sdwd_snwd").children(".wd");
             * nrwd.addClass("kt_close_snwd").removeClass("wd"); nrwd.empty();
             */

            $.ajax({
                type: "POST",
                timeout: 10000,
                url: ctx + "/spms/spmsUser/deviceClose",
                data: {
                    userid: userid,
                    deviceid: deviceid,
                    gwid: gwid
                },
                dataType: "json",
                success: function (resObj) {
                    /* 还有室内温度需要继续更新 */
                    if (resObj.success) {
                        if (resObj.status == 1) {
                            $.closeAc(deviceid);
                        } else {
                            alert("ERROR:" + resObj.msg);
                        }
                    } else {
                        alert("ERROR:" + resObj.msg);
                    }
                },
                error: function (resObj) {
                    alert("error" + resObj.msg);
                },
                complete: function () {
                    setTimeout(window.removeMask, 1000);
                }
            });

        } else {
            /*
             * alert(" 【点击开关】从数据库读取一条最新的数据"); var nrwd =
             * $(this).siblings(".content_kt_nr_sdwd").children(".content_kt_nr_sdwd_snwd").children(".kt_close_snwd");
             * nrwd.addClass("wd").removeClass("kt_close_snwd");
             * nrwd.append('${data.rtHtml}<span class="wd_celsius"></span>');
             * 
             * var sdwd =
             * $(this).siblings(".content_kt_nr_sdwd").children(".content_kt_nr_sdwd_wdsz").children(".kt_close_none");
             * sdwd.addClass("wd").removeClass("kt_close_none").removeClass("ml_45");
             * sdwd.append(' ${data.actHtml}<span class="wd_celsius"></span>');
             * 
             * var sdwd_hk =
             * $(this).siblings(".content_kt_nr_sdwd").children(".content_kt_nr_sdwd_hk");
             * sdwd_hk.append(' <div class="number font_family_a"><p class="pb_60 pt_5">32℃</p><p class="pb_60">24℃</p><p>16℃</p></div><div
             * class="content_kt_nr_fs_right_hd">${data.act}</div>');
             * 
             * var ms_left =
             * $(this).siblings(".content_kt_nr_ms").children(".content_kt_nr_ms_left");
             * ms_left.empty(); ms_left.append('<div class="fz_22 pb_25">模式</div><div
             * class="xuehua"></div>');
             * 
             * var ms_right =
             * $(this).siblings(".content_kt_nr_ms").children(".content_kt_nr_ms_right");
             * ms_right.append('<a class="auto"></a><a class="xuehua"></a><a
             * class="sun"></a><a class="chushi"></a>');
             * 
             * var fs_left =
             * $(this).siblings(".content_kt_nr_fs").children(".content_kt_nr_fs_left");
             * fs_left.empty(); fs_left.append('<div class="fz_22 pb_25">风速</div><div
             * class="fengsu"></div>'); var fs_right =
             * $(this).siblings(".content_kt_nr_fs").children(".content_kt_nr_fs_right");
             * fs_right.append('<div class="content_kt_nr_fs_right_pic"></div><div
             * class="content_kt_nr_fs_right_hd">${data.speed }</div>');
             */
            var mk = window.addMask(" ");
            /* 调一条add，onoff为1的操作到数据库， 在跳转后会重新加载一次最新数据 */
            $.ajax({
                type: "POST",
                timeout: 10000,
                url: ctx + "/spms/spmsUser/deviceOpen",
                data: {
                    userid: userid,
                    deviceid: deviceid,
                    gwid: gwid
                },
                dataType: "json",
                cache: false,
                success: function (resObj) {
                    if (resObj.status == 1) {
                        if (resObj.success) {

                            deviceAcOnline(resObj.deviceId);
                            $.openAc(resObj.deviceId, resObj.temp,
                                resObj.acTemp, resObj.mode,
                                resObj.speed, resObj.maxTemp,
                                resObj.minTemp, resObj.allowHeat);
                            $.openAc(resObj.deviceId, resObj.temp,
                                resObj.acTemp, resObj.mode,
                                resObj.speed, resObj.maxTemp,
                                resObj.minTemp, resObj.allowHeat);
                        } else {
                            alert(resObj.msg);
                        }
                    } else {
                        alert("ERROR:" + resObj.msg);
                    }
                },
                error: function (resObj) {
                    alert("error" + resObj.msg);
                },
                complete: function () {
                    setTimeout(window.removeMask, 1000);
                }
            });
            // setInterval(window.removeMask,1000);
        }
    });

    /* 记录变更前的温度 */
    $(".content_kt_nr_fs_right_hd#wd").mousedown(function () {
        var temp = $(this).parent().siblings(".content_kt_nr_sdwd_wdsz")
            .children("div.wd").children("span");
        var t = $(temp[0]).attr("class");
        oldtemp = '';
        oldtemp = oldtemp + t.substring(t.length - 1, t.length);
        t = $(temp[1]).attr("class");
        oldtemp = oldtemp + t.substring(t.length - 1, t.length);
        // alert(oldtemp);
    });
    /* 记录变更前的模式 */
    $(".content_kt_nr_ms_right").children("a").mousedown(function () {
        var t = $(this).siblings(".hover");
        var classs = t.attr("class");
        t = classs.split(" ");
        oldms = t[0];

    });
    /* 点击图表更新模式 */
    $(".content_kt_nr_ms_right").children("a").click(function () {
        /* 获取设备ID */
        var deviceid = $(this).parent().parent().parent()
            .siblings(".content_kt_list_title").children(".fl").attr("id");
        /* 获取模式 */
        var classs = $(this).attr("class");
        t = classs.split(" ");
        var newms = t[0];
        if (oldms != newms && oldms != null) {
            var value1, value2;
            if (newms == "auto") {
                value1 = 0;
            } else if (newms == "jh") {
                value1 = 1;
            } else if (newms == "xuehua") {
                value1 = 2;
            } else if (newms == "sun") {
                value1 = 3;
            } else if (newms == "chushi") {
                value1 = 4;
            }

            if (oldms == "auto") {
                value2 = 0;
            } else if (oldms == "jh") {
                value2 = 1;
            } else if (oldms == "xuehua") {
                value2 = 2;
            } else if (oldms == "sun") {
                value2 = 3;
            } else if (oldms == "chushi") {
                value2 = 4;
            }
            var mk = window.addMask(" ");
            $.ajax({
                type: "POST",
                timeout: 10000,
                url: ctx + "/spms/spmsUser/setAcMs",
                data: {
                    deviceid: deviceid,
                    newms: value1,
                    oldms: value2,
                    gwid: gwid
                },
                dataType: "json",
                success: function (resObj) {
                    if (resObj.status == 1) {
                        if (resObj.success) {
                            $.changeSDMS(deviceid, value1);
                        } else {
                            alert(resObj.msg);
                        }
                    } else {
                        alert(resObj.msg);
                    }

                },
                error: function (resObj) {
                    alert("error" + resObj);
                },
                complete: function () {
                    setTimeout(window.removeMask, 1000);
                }
            });

        }

    });

    /* 记录变更前的风速 */
    $(".content_kt_nr_fs_right_hd#fs").mousedown(function () {

        var fs = $(this).parent().children(".content_kt_nr_fs_right_pic*");
        var classs = $(fs[0]).attr("class");
        var t = classs.split(" ");
        oldfs = t[t.length - 1].substring(t[t.length - 1].length - 1,
            t[t.length - 1].length);
    });

    /* 点击下圆点出发回调函数（改变数值） */
    function changeValue(render, num, base) {
        for (var i = 0; i < 10; i++) {
            $(render).removeClass(base + i);
        }
        $(render).addClass(base + num);
    }

    /* 读取datainfo ， 选择绘制控件 */
    $.each(datainfo, function (entryIndex, entry) {
        if (entry["type"] == 2) {
            if (entry["status"] == 1) {
                deviceAcOnline(entry["deviceId"]);
                if (entry["onoff"] == 1) {

                    $.openAc(entry["deviceId"], entry["rt"],
                        entry["act"], entry["mode"],
                        entry["speed"], entry["max"], entry["min"],
                        entry["allow"]);
                } else {
                    $.closeAc(entry["deviceId"], entry["rt"]);
                }
            } else {
                deviceAcOffline(entry["deviceId"]);
            }
        }
        if (entry["type"] == 3) {
            if (entry["status"] == 1) {
                deviceDoorOnline(entry["deviceId"]);
            } else {
                deviceDoorOffline(entry["deviceId"]);
            }
        }
        if (entry["type"] == 4) {
            if (entry["status"] == 1) {
                deviceWinOnline(entry["deviceId"]);
            } else {
                deviceWinOffline(entry["deviceId"]);
            }
        }

    })

    //初始绘制图表

    $.each(datas, function (entryIndex, entry) {
        var data0;
        if (entry["type"] == '2') {
            var d_id = entry["deviceId"];
            var last;
            data0 = jQuery.parseJSON(entry["data"]);
            for (var key in data0) {
                // x = key;
                last = data0[key][0];
                //data0[key][0] += 28800000;
            }
            /*初始绘图的时候，记录下最后一点的时间戳，放进lastdata*/

            $.each(lastdata, function (entryIndex, entry) {
                //if(entry[d_id]){
                //alert(d_id);
                entry[d_id] = last;

                //}
            })
            //lastdata[d_id][0] = last;

            $($(".content_kt#" + entry["deviceId"] + "").find(".ydqs")[0])
                .highcharts('StockChart', {
                    chart: {
                        type: 'spline',
                        alignTicks: false,
                        backgroundColor: "rgba(0,0,0,0)"
                    },
                    navigator: {
                        enabled: true
                    },
                    rangeSelector: {
                        buttonTheme: { // styles
                            // for the
                            // buttons
                            fill: 'none',
                            stroke: 'none',
                            'stroke-width': 0,
                            r: 8,
                            style: {
                                color: '#039',
                                fontWeight: 'bold'
                            },
                            states: {
                                hover: {},
                                select: {
                                    fill: '#039',
                                    style: {
                                        color: 'white'
                                    }
                                }
                            }
                        },
                        buttonSpacing: 20,
                        buttons: [
                            {
                                type: 'minute',
                                count: 60,
                                text: '1小时'
                            },
                            {
                                type: 'minute',
                                count: 720,
                                text: '12小时'
                            },
                            {
                                type: 'day',
                                count: 1,
                                text: '1天'
                            },
                            {
                                type: 'day',
                                count: 5,
                                text: '5天'
                            },
                            {
                                type: 'day',
                                count: 10,
                                text: '10天'
                            },
                            {
                                type: 'day',
                                count: 15,
                                text: '15天'
                            },
                            {
                                type: 'month',
                                count: 1,
                                text: '一个月'
                            },
                            {
                                type: 'all',
                                text: '全部'
                            }
                        ],
                        inputEnabled: false,
                        labelStyle: {
                            color: 'silver',
                            fontWeight: 'bold'
                        },
                        selected: 0
                    },

                    title: {
                        align: "left",
                        text: '用电情况',
                        //color : "#FFF"
                        style: {
                            color: "#FAFAFA"
                        }
                    },

                    legend: {
                        enabled: true,
                        verticalAlign: "top",
                        itemStyle: {
                            "color": "#FAFAFA"
                        }

                    },
                    yAxis: {
                        opposite: false,
                        labels: {
                            style: {
                                color: "#fff"
                            }
                        },
                        min: 0
                    },
                    xAxis: {
                        labels: {
                            style: {
                                color: "#fff"
                            }
                        }
                    },
                    series: [
                        {
                            name: '功率(单位:W)',
                            data: data0,
                            color: "rgb(255,147,52)"
                        }
                    ],
                    exporting: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                        // 禁用版权信息
                    }
                });
            $($(".content_kt#" + entry["deviceId"] + "").find(".ydqs")[1])
                .highcharts('StockChart', {
                    chart: {
                        type: 'spline',
                        alignTicks: false,
                        backgroundColor: "rgba(0,0,0,0)"
                    },
                    navigator: {
                        enabled: true
                    },
                    rangeSelector: {
                        buttonTheme: { // styles
                            // for the
                            // buttons
                            fill: 'none',
                            stroke: 'none',
                            'stroke-width': 0,
                            r: 8,
                            style: {
                                color: '#039',
                                fontWeight: 'bold'
                            },
                            states: {
                                hover: {},
                                select: {
                                    fill: '#039',
                                    style: {
                                        color: 'white'
                                    }
                                }
                            }
                        },
                        buttonSpacing: 20,
                        buttons: [
                            {
                                type: 'minute',
                                count: 60,
                                text: '1小时'
                            },
                            {
                                type: 'minute',
                                count: 720,
                                text: '12小时'
                            },
                            {
                                type: 'day',
                                count: 1,
                                text: '1天'
                            },
                            {
                                type: 'day',
                                count: 5,
                                text: '5天'
                            },
                            {
                                type: 'day',
                                count: 10,
                                text: '10天'
                            },
                            {
                                type: 'day',
                                count: 15,
                                text: '15天'
                            },
                            {
                                type: 'month',
                                count: 1,
                                text: '一个月'
                            },
                            {
                                type: 'all',
                                text: '全部'
                            }
                        ],
                        inputEnabled: false,
                        labelStyle: {
                            color: 'silver',
                            fontWeight: 'bold'
                        },
                        selected: 0
                    },

                    title: {
                        align: "left",
                        text: '用电情况',
                        //color : "#FFF"
                        style: {
                            color: "#FAFAFA"
                        }
                    },

                    legend: {
                        enabled: true,
                        verticalAlign: "top",
                        itemStyle: {
                            "color": "#FAFAFA"
                        }
                    },
                    yAxis: {
                        opposite: false,
                        labels: {
                            style: {
                                color: "#fff"
                            }
                        },
                        min: 0
                    },
                    xAxis: {
                        labels: {
                            style: {
                                color: "#fff"
                            }
                        }
                    },
                    series: [
                        {
                            name: '功率(单位:W)',
                            data: data0,
                            color: "rgb(255,147,52)"
                        }
                    ],
                    exporting: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                        // 禁用版权信息
                    }
                });
        }

        if (entry["type"] == '3' || entry["type"] == '4') {
            data0 = jQuery.parseJSON(entry["data"]);
            for (var key in data0) {
                // x = key;
                last = data0[key][0];
                //data0[key][0] += 28800000;
            }
            $($(".content_kt_door#" + entry["deviceId"] + "")
                .find(".kgjl")[0]).highcharts('StockChart', {
                title: {
                    text: '开关记录',
                    align: "left",
                    style: {
                        color: "#FAFAFA"
                    }
                },
                chart: {
                    backgroundColor: "rgba(0,0,0,0)"
                },
                rangeSelector: {
                    inputEnabled: false
                },
                navigator: {
                    series: {
                        step: true
                    }
                },
                tooltip: {
                    headerFormat: '<b>开关记录</b><br>',
                    formatter: function (d) {
                        console.log(d, this);
                        if (this.y == 1) {
                            return "<b>开关状态为：开启</b><br>";
                        } else if (this.y == 0) {
                            return "<b>开关状态为：关闭</b><br>";
                        } else {
                            return "";
                        }
                    }
                },
                yAxis: {
                    opposite: false,
                    gridLineWidth: 0,
                    labels: {
                        style: {
                            color: "#fff"
                        },
                        formatter: function (d) {
                            if (this.value == 1) {
                                return "开";
                            } else if (this.value == 0) {
                                return "关";
                            } else {
                                return "";
                            }
                        }
                    }
                },
                series: [
                    {
                        data: data0,
                        step: true
                    }
                ]
            });
            $($(".content_kt_door#" + entry["deviceId"] + "")
                .find(".kgjl")[1]).highcharts('StockChart', {
                title: {
                    text: '开关记录',
                    align: "left",
                    style: {
                        color: "#FAFAFA"
                    }
                },
                chart: {
                    backgroundColor: "rgba(0,0,0,0)"
                },
                rangeSelector: {
                    inputEnabled: false
                },
                navigator: {
                    series: {
                        step: true
                    }
                },

                tooltip: {
                    headerFormat: '<b>开关记录</b><br>',
                    formatter: function (d) {
                        console.log(d, this);
                        if (this.y == 1) {
                            return "<b>开关状态为：开启</b><br>";
                        } else if (this.y == 0) {
                            return "<b>开关状态为：关闭</b><br>";
                        } else {
                            return "";
                        }
                    }
                },
                yAxis: {
                    opposite: false,
                    gridLineWidth: 0,
                    labels: {
                        style: {
                            color: "#fff"
                        },
                        formatter: function (d) {
                            if (this.value == 1) {
                                return "开";
                            } else if (this.value == 0) {
                                return "关";
                            } else {
                                return "";
                            }
                        }
                    }
                },
                series: [
                    {
                        data: data0,
                        step: true
                    }
                ]
            });
        }
    });
    //初始绘图END
    // 上面的下拉按钮
    $(".content_kt_list").find(".content_kt_ydqs_btn").on("click", function () {
        /* 在这取好data0 */
        var deviceid = $(this).siblings(".ydqs").attr("id");
        var data0;
        /* 判断ID为点击事件所属id的data，转json格式用作数据展示 */

        $.each(datas, function (entryIndex, entry) {
            if (entry["deviceId"] == deviceid) {
                data0 = jQuery.parseJSON(entry["data"]);
            }
        })
        for (var key in data0) {
            // x = key;
            //data0[key][0] += 28800000;
        }
        $.each(datainfo, function (entryIndex, entry) {
            if (entry["deviceId"] == deviceid) {
                if (entry["isshow"]) {
                    entry["isshow"] = false;
                    $(this).removeClass("hover");
                    $($(".content_kt#" + deviceid + "").find(".ydqs")[0]).css(
                        "display", "none");
                    $($(".content_kt#" + deviceid + "").find(".ydqs")[1]).css(
                        "display", "none");
                    $($(".content_kt#" + deviceid + "").find(".qsordl")[0])
                        .css("display", "none");
                    $($(".content_kt#" + deviceid + "").find(".qsordl")[1])
                        .css("display", "none");
                    $($(".content_kt#" + deviceid + "").find(".qsordl")[0])
                        .val(1);
                    $($(".content_kt#" + deviceid + "").find(".qsordl")[1])
                        .val(1);
                } else {
                    entry["isshow"] = true;
                    $(this).addClass("hover");
                    $($(".content_kt#" + deviceid + "").find(".qsordl")[0])
                        .css("display", "block");
                    $($(".content_kt#" + deviceid + "").find(".qsordl")[1])
                        .css("display", "block");
                    if ($(".content_kt#" + deviceid + "").find(".ydqs")
                        .children().hasClass("highcharts-container")) {
                        $($(".content_kt#" + deviceid + "").find(".ydqs")[0])
                            .css("display", "block");
                        $($(".content_kt#" + deviceid + "").find(".ydqs")[1])
                            .css("display", "block");
                    } else {
                        $($(".content_kt#" + deviceid + "").find(".ydqs")[0])
                            .css("display", "block").highcharts(
                            'StockChart', {
                                chart: {
                                    type: 'spline',
                                    alignTicks: false,
                                    backgroundColor: "rgba(0,0,0,0)"
                                },
                                navigator: {
                                    enabled: true
                                },
                                rangeSelector: {
                                    buttonTheme: { // styles
                                        // for the
                                        // buttons
                                        fill: 'none',
                                        stroke: 'none',
                                        'stroke-width': 0,
                                        r: 8,
                                        style: {
                                            color: '#039',
                                            fontWeight: 'bold'
                                        },
                                        states: {
                                            hover: {},
                                            select: {
                                                fill: '#039',
                                                style: {
                                                    color: 'white'
                                                }
                                            }
                                        }
                                    },
                                    buttonSpacing: 20,
                                    buttons: [
                                        {
                                            type: 'minute',
                                            count: 60,
                                            text: '1小时'
                                        },
                                        {
                                            type: 'minute',
                                            count: 720,
                                            text: '12小时'
                                        },
                                        {
                                            type: 'day',
                                            count: 1,
                                            text: '1天'
                                        },
                                        {
                                            type: 'day',
                                            count: 5,
                                            text: '5天'
                                        },
                                        {
                                            type: 'day',
                                            count: 10,
                                            text: '10天'
                                        },
                                        {
                                            type: 'day',
                                            count: 15,
                                            text: '15天'
                                        },
                                        {
                                            type: 'month',
                                            count: 1,
                                            text: '一个月'
                                        },
                                        {
                                            type: 'all',
                                            text: '全部'
                                        }
                                    ],
                                    inputEnabled: false,
                                    labelStyle: {
                                        color: 'silver',
                                        fontWeight: 'bold'
                                    },
                                    selected: 0
                                },

                                title: {
                                    align: "left",
                                    text: '用电情况',
                                    //color : "#FFF"
                                    style: {
                                        color: "#FAFAFA"
                                    }
                                },

                                legend: {
                                    enabled: true,
                                    verticalAlign: "top",
                                    itemStyle: {
                                        "color": "#FAFAFA"
                                    }
                                },
                                yAxis: {
                                    opposite: false,
                                    labels: {
                                        style: {
                                            color: "#fff"
                                        }
                                    },
                                    min: 0
                                },
                                xAxis: {
                                    labels: {
                                        style: {
                                            color: "#fff"
                                        }
                                    }
                                },
                                series: [
                                    {
                                        name: '功率(单位:W)',
                                        data: data0,
                                        color: "rgb(255,147,52)"
                                    }
                                ],
                                exporting: {
                                    enabled: false
                                },
                                credits: {
                                    enabled: false
                                    // 禁用版权信息
                                }
                            });
                        $($(".content_kt#" + deviceid + "").find(".ydqs")[1])
                            .css("display", "block").highcharts(
                            'StockChart', {
                                chart: {
                                    type: 'spline',
                                    alignTicks: false,
                                    backgroundColor: "rgba(0,0,0,0)"
                                },
                                navigator: {
                                    enabled: true
                                },
                                rangeSelector: {
                                    buttonTheme: { // styles
                                        // for the
                                        // buttons
                                        fill: 'none',
                                        stroke: 'none',
                                        'stroke-width': 0,
                                        r: 8,
                                        style: {
                                            color: '#039',
                                            fontWeight: 'bold'
                                        },
                                        states: {
                                            hover: {},
                                            select: {
                                                fill: '#039',
                                                style: {
                                                    color: 'white'
                                                }
                                            }
                                        }
                                    },
                                    buttonSpacing: 20,
                                    buttons: [
                                        {
                                            type: 'minute',
                                            count: 60,
                                            text: '1小时'
                                        },
                                        {
                                            type: 'minute',
                                            count: 720,
                                            text: '12小时'
                                        },
                                        {
                                            type: 'day',
                                            count: 1,
                                            text: '1天'
                                        },
                                        {
                                            type: 'day',
                                            count: 5,
                                            text: '5天'
                                        },
                                        {
                                            type: 'day',
                                            count: 10,
                                            text: '10天'
                                        },
                                        {
                                            type: 'day',
                                            count: 15,
                                            text: '15天'
                                        },
                                        {
                                            type: 'month',
                                            count: 1,
                                            text: '一个月'
                                        },
                                        {
                                            type: 'all',
                                            text: '全部'
                                        }
                                    ],
                                    inputEnabled: false,
                                    labelStyle: {
                                        color: 'silver',
                                        fontWeight: 'bold'
                                    },
                                    selected: 0
                                },

                                title: {
                                    align: "left",
                                    text: '用电情况',
                                    //color : "#FFF"
                                    style: {
                                        color: "#FAFAFA"
                                    }
                                },

                                legend: {
                                    enabled: true,
                                    verticalAlign: "top",
                                    itemStyle: {
                                        "color": "#FAFAFA"
                                    }

                                },
                                yAxis: {
                                    opposite: false,
                                    labels: {
                                        style: {
                                            color: "#fff"
                                        }
                                    },
                                    min: 0
                                },
                                xAxis: {
                                    labels: {
                                        style: {
                                            color: "#fff"
                                        }
                                    }
                                },
                                series: [
                                    {
                                        name: '功率(单位:W)',
                                        data: data0,
                                        color: "rgb(255,147,52)"
                                    }
                                ],
                                exporting: {
                                    enabled: false
                                },
                                credits: {
                                    enabled: false
                                    // 禁用版权信息
                                }
                            });

                    }

                }
            }
        })

    });

    // 下面的下拉按钮

    $($(".content_kt_door_list").find(".content_door_switch_btn")[0]).each(
        function (i, d) {
            var deviceid = $(this).siblings(".kgjl").attr("id");
            var data0;
            // 判断ID为点击事件所属id的data，转json格式用作数据展示
            $.each(datas, function (entryIndex, entry) {
                if (entry["deviceId"] == deviceid) {
                    data0 = jQuery.parseJSON(entry["data"]);
                }
            })
            for (var key in data0) {
                // x = key;
                //data0[key][0] += 28800000;
            }
            $(d).on("click", function () {

                var chartContainer = $(d.parentNode).find(".kgjl");
                if (chartContainer.data("show")) {
                    $(this).removeClass("hover");
                    chartContainer.data("show", false);
                    chartContainer.css("display", "none");
                } else {
                    chartContainer.data("show", true);
                    $(this).addClass("hover");
                    chartContainer.css("display", "block");
                    if (chartContainer.children()
                        .hasClass("highcharts-container")) {
                        $(".content_kt#" + deviceid + "").find(".ydqs")
                            .css("display", "block");
                    } else {
                        chartContainer.highcharts('StockChart', {
                            title: {
                                text: '开关记录',
                                align: "left",
                                style: {
                                    color: "#FAFAFA"
                                }
                            },
                            chart: {
                                backgroundColor: "rgba(0,0,0,0)"
                            },
                            rangeSelector: {
                                inputEnabled: false
                            },
                            navigator: {
                                series: {
                                    step: true
                                }
                            },
                            tooltip: {
                                headerFormat: '<b>开关记录</b><br>',
                                formatter: function (d) {
                                    console.log(d, this);
                                    if (this.y == 1) {
                                        return "<b>开关状态为：开启</b><br>";
                                    } else if (this.y == 0) {
                                        return "<b>开关状态为：关闭</b><br>";
                                    } else {
                                        return "";
                                    }
                                }
                            },
                            yAxis: {
                                opposite: false,
                                gridLineWidth: 0,
                                labels: {
                                    style: {
                                        color: "#fff"
                                    },
                                    formatter: function (d) {
                                        if (this.value == 1) {
                                            return "开";
                                        } else if (this.value == 0) {
                                            return "关";
                                        } else {
                                            return "";
                                        }
                                    }
                                }
                            },
                            series: [
                                {
                                    data: data0,
                                    step: true
                                }
                            ]
                        });
                    }

                }
            });

        });
    $($(".content_kt_door_list").find(".content_door_switch_btn")[1]).each(
        function (i, d) {
            var deviceid = $(this).siblings(".kgjl").attr("id");
            var data0;
            // 判断ID为点击事件所属id的data，转json格式用作数据展示
            $.each(datas, function (entryIndex, entry) {
                if (entry["deviceId"] == deviceid) {
                    data0 = jQuery.parseJSON(entry["data"]);
                }
            })
            for (var key in data0) {
                // x = key;
                //data0[key][0] += 28800000;
            }
            $(d).on("click", function () {

                var chartContainer = $(d.parentNode).find(".kgjl");
                if (chartContainer.data("show")) {
                    $(this).removeClass("hover");
                    chartContainer.data("show", false);
                    chartContainer.css("display", "none");
                } else {
                    chartContainer.data("show", true);
                    $(this).addClass("hover");
                    chartContainer.css("display", "block");
                    if (chartContainer.children()
                        .hasClass("highcharts-container")) {
                        $(".content_kt#" + deviceid + "").find(".ydqs")
                            .css("display", "block");
                    } else {
                        chartContainer.highcharts('StockChart', {
                            title: {
                                text: '开关记录',
                                align: "left",
                                style: {
                                    color: "#FAFAFA"
                                }
                            },
                            chart: {
                                backgroundColor: "rgba(0,0,0,0)"
                            },
                            rangeSelector: {
                                inputEnabled: false
                            },
                            navigator: {
                                series: {
                                    step: true
                                }
                            },
                            tooltip: {
                                headerFormat: '<b>开关记录</b><br>',
                                formatter: function (d) {
                                    console.log(d, this);
                                    if (this.y == 1) {
                                        return "<b>开关状态为：开启</b><br>";
                                    } else if (this.y == 0) {
                                        return "<b>开关状态为：关闭</b><br>";
                                    } else {
                                        return "";
                                    }
                                }
                            },
                            yAxis: {
                                opposite: false,
                                gridLineWidth: 0,
                                labels: {
                                    style: {
                                        color: "#fff"
                                    },
                                    formatter: function (d) {
                                        if (this.value == 1) {
                                            return "开";
                                        } else if (this.value == 0) {
                                            return "关";
                                        } else {
                                            return "";
                                        }
                                    }
                                }
                            },
                            series: [
                                {
                                    data: data0,
                                    step: true
                                }
                            ]
                        });
                    }

                }
            });

        });
    /*定时请求数据*/
    var global = setTimeout("$.updateAllDevice()", 5000);
    /*end*/
    $(".harmazing_chart_control_bar_button").mousedown(function () {
        clearTimeout(global);
    })
});

;

function doLoginOut() {
    var dto = {};
    doJsonRequest("/doLoginOut",dto,function(data) {
        if(data.result) {
            window.location.href="login.html"
        } else {
            $.alert("退出失败。");
        }
    })
}