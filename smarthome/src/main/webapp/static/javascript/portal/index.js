$("#openClose", parent.document).hide();

/**
 * 用户数量按照所属区域进行统计(柱状图)
 *
 * @param content
 */
Highcharts.setOptions({ global: { useUTC: false },
    yAxis: {
        //   lineColor: '#c0c0c0',
        minTickInterval: 1
    } });

function objCheck(obj) {
    for (var k in obj) {
        return true;
    }
    return false;
}
function userCountByArea(content, userCountByAreaData) {
    if (!content)
        return;
    if (!objCheck(userCountByAreaData) || !userCountByAreaData.length)
        return;
    var categories = [], name = "数量", data = [];
    for (var i = 0; i < userCountByAreaData.length; i++) {
        for (var key in userCountByAreaData[i]) {
            categories.push(key);
            data.push({
                y: userCountByAreaData[i][key]
            })
        }
    }

    $(content).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: ''
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: '数量'
            }
        }, tooltip: {
            formatter: function () {

                var s = "<strong>" + this.x + "</strong></br>";
                s += "数量：<b>" + this.y + "</b>";
                return s
            },
            useHTML: true,
            shared: true,
            crosshairs: true
        },
        series: [
            {
                name: name,
                data: data
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

/**
 * 按服务类型统计用户数量(柱状图)
 *
 * @param content
 */
function userCountByRule(content, userCountByRuleData) {
  //  console.log(userCountByRuleData);
    if (!content)
        return;
    if (!objCheck(userCountByRuleData) || !userCountByRuleData.length)
        return;
    var categories = [], categoriesTip = [], name = '订阅数量', data = [];
    for (var i = 0; i < userCountByRuleData.length; i++) {
        for (var key in userCountByRuleData[i]) {
            if (key.length > 4)
                categories.push(key);//   categories.push(key.substr(0, 4) + "..");
            else {
                categories.push(key);
            }
            categoriesTip.push(key);
            data.push({
                y: userCountByRuleData[i][key],
                yName:key
            })
        }
    }
//console.log(categoriesTip);
    $(content).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: ''
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: '数量'
            }
        },
        tooltip: {
            formatter: function () {
             //   console.log(this);
                var index = 0;
               /* for (var i = 0; i < categories.length; i++) {
                    if (this.x == categories[i]) {
                        index = i;
                        break;
                    }
                }*/
                var s = "<strong>" + this["points"][0]["point"].yName + "</strong></br>";
                s += "数量：<b>" + this.y + "</b>";
                return s
            },
            useHTML: true,
            shared: true,
            crosshairs: true
        },
        series: [
            {
                name: name,
                data: data
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

/**
 * 用户数量根据在线状态统计(offline/online/exception)(饼图)
 *
 * @param content
 */
function userCountByOnlineStatus(content, data) {
  //  console.log(data)
    if (!content)
        return;
    if (!objCheck(data) || !data.length)
        return;
    var charData = [];
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        var dataItem = {};
        for (var key in item) {
            dataItem.name = key;
            if (key === '在线') {
                dataItem.color = '#00a650';
            } else if (key === '异常') {
                dataItem.color = '#ed1b24';
            } else if (key === '离线') {
                dataItem.color = '#cccccc';
            }
            dataItem.y = item[key];
        }
        charData.push(dataItem);
    }

    $(content).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            align: "left",
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b><br />数量: <b>{point.y}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [
            {
                type: 'pie',
                name: '占比',
                data: charData
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
// 服务期限统计
function userCountByProductRemaining(content, data){
   // console.log(data)
    if (!content)
        return;
    if (!objCheck(data) || !data.length)
        return;
    var charData = [];
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        var dataItem = {};
        for (var key in item) {
            dataItem.name = key;
            if (key === '本季度') {
                dataItem.color = '#00a650';
            } else if (key === '不足月') {
                dataItem.color = '#ed1b24';
            } else if (key === '已到期') {
                dataItem.color = '#cccccc';
            }
            dataItem.y = item[key];
        }
        charData.push(dataItem);
    }
    $(content).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            align: "left",
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b><br />数量: <b>{point.y}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [
            {
                type: 'pie',
                name: '占比',
                data: charData
            }
        ],
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
            // 禁用版权信息
        }
    })
}

/**
 * 用户开通/退订数量增量根据时间的曲线(双曲线)
 *
 * @param content
 */
//var tmpCategories = [];
function userIncrementByDate(content, data) {
    if (!content)
        return;
    if (!objCheck(data) || !data.length)
        return;
    var bindData = [], unbindData = [];
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        bindData.push([item.date,item.count]);
        unbindData.push([item.date,item.uncount]);
//        tmpCategories.push();
//        bindData.push([getDay(item.date), item.count]);
//        unbindData.push([getDay(item.date), item.uncount]);
    }
//    function getDay(datetime) {
//        var perDatetime = new Date(datetime);
//        return datetime
//            - (perDatetime.getMilliseconds() + (((perDatetime
//                .getHours() + 8)
//                * 60 + perDatetime.getMinutes())
//                * 60 + perDatetime.getSeconds())
//                * 1000)
//    }


    $(content).highcharts('StockChart', {
        chart: {
            alignTicks: false
        },

        rangeSelector: {
            enabled: false,
            inputEnabled: false,
            selected: false
        },
        navigator: {
            xAxis: {
                labels: {
                    formatter: function () {
                    	return Highcharts.dateFormat('%Y-%m-%d',this.value);
                    },
                    align: 'center'
                }
            }
        },
        credits: {
            enabled: false
        },
        scrollbar: {
            enabled: false
        },
        title: {
            align: "left",
            text: ''
        },

        legend: {
            enabled: true,
            verticalAlign: "top"
        },
        plotOptions: {
            series: {
                marker: {
                    enabled: true,
                    radius: 4
                }
            }
        },
        xAxis: {
            minTickInterval: 1000 * 3600 * 24,
//            tickPixelInterval: 1000 * 3600 * 24,//x轴上的间隔
            // title :{
            // text:"title"
            // },
//            type: 'date', //定义x轴上日期的显示格式
        	type: 'linear',
            labels: {
                formatter: function () {
                	return Highcharts.dateFormat('%Y-%m-%d',this.value);
                },
                align: 'center'
            }
        },
        yAxis: {
            opposite: false,
            // tickInterval:1,
            min: 0,
            title: {
                text: "服务数量"
            }
        },
        tooltip: {
            formatter: function () {
          //      console.log(this);
//            	var vDate = new Date(this.points[0].x)
                return this.points[0].series.name + ':' + this.points[0].y
                    + '<br />' + this.points[1].series.name + ':'
                    + this.points[1].y ;
            }
        },
        series: [
            {
                type: 'line',
                name: '开通',
                data: bindData,
//                pointInterval: 3600 * 1000,
                color: "green"

            },
            {
                type: 'line',
                name: '退订',
                data: unbindData,
//                pointInterval: 3600 * 1000,
                color: "red"
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

/**
 * 设备库存统计(按型号/按仓库位置（各类别设备）)
 *
 * @param content
 */
function deviceByModel(content, deviceByModelData) {
    if (!content)
        return;
    if (!objCheck(deviceByModelData) || !deviceByModelData.length)
        return;
    var colors = Highcharts.getOptions().colors, categories = [], name = "库存数量", data = [];
    for (var i = 0; i < deviceByModelData.length; i++) {
        var item = deviceByModelData[i];
        categories[i] = item.type;
        var dataItem = {
            y: item.value,
            color: colors[i]
        };
        data[i] = dataItem;
    }

    $(content).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: ''
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            minTickInterval: 1,
            title: {
                text: ''
            }
        },
        plotOptions: {
            column: {
                cursor: 'pointer',

                dataLabels: {
                    enabled: true,
                    crop: false,
                    inside: false,
                    overflow: "none",
                    // color: colors[0],
                    style: {
                        fontWeight: 'bold'
                    }
                }
            }
        },
        tooltip: {
            formatter: function () {
                var point = this.point, s = this.x + ':<b>' + this.y
                    + '<br/>';
                return s;
            }
        },
        series: [
            {
                name: name,
                data: data,
                color: 'white'
            }
        ],
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
            // 禁用版权信息
        }
    })


}

/**
 * 设备运营状态统计(设备数目按照 - 库存/运营/售后维修/报废 （个类别设备）)(饼图)
 *
 * @param content
 */
function deviceByOptStatus(content, deviceByOptStatusData) {
    if (!content)
        return;
    if (!objCheck(deviceByOptStatusData) || !deviceByOptStatusData.length)
        return;
    var categories = [], stockData = [], operateData = [], repairData = [], scrapData = [];
    for (var i = 0; i < deviceByOptStatusData.length; i++) {
        var item = deviceByOptStatusData[i];
        categories.push(item.name);
        stockData.push(item.stock);
        operateData.push(item.operate);
        repairData.push(item.repair);
        scrapData.push(item.scrap);
    }
    $(content).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: 'left',
            text: ''
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: '数量'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
                + '<td style="padding:0"><b>{point.y}</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [
            {
                name: '库存',
                data: stockData

            },
            {
                name: '运营',
                data: operateData

            },
            {
                name: '售后维修',
                data: repairData

            },
            {
                name: '报废',
                data: scrapData

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

/**
 * 网关软件版本统计(饼图)
 *
 * @param content
 */
function gwBySoftVersion(content) {

}

/**
 * 软件版本统计信息（柱状图）
 */
function SoftwareVersionStatistical(content,softwares,counts){
	$(content).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: ''
        },
        xAxis: {
            categories: softwares
        },
        yAxis: {
            minTickInterval: 1,
            title: {
                text: ''
            }
        },
        plotOptions: {
            column: {
                cursor: 'pointer',

                dataLabels: {
                    enabled: true,
                    crop: false,
                    inside: false,
                    overflow: "none",
                    // color: colors[0],
                    style: {
                        fontWeight: 'bold'
                    }
                }
            }
        },
        tooltip: {
            formatter: function () {
                var point = this.point, s = this.x + ':<b>' + this.y
                    + '<br/>';
                return s;
            }
        },
        series: [
            {
                name: '网关数量',
                data: counts,
                color: '#95dedd'
            }
        ],
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
            // 禁用版权信息
        }
    })
}

/**************
 * 删除展示图
 */

function deleteSelf(me) {

    var url = "/spmsPortalCustom/deleteSort";
    console.log(me.attr("module"));
    var self = {
        divname: me.attr("module")
    }
    doJsonRequest(url, self, function (a) {
        console.log(a);
        if (a.result) {
            a = a.data;
        }
        if (a && a["succcess"]) {
            me.hide();
            $("li[module=" + me.attr("module") + "]").hide();
        }
        else {
            console.log("删除失败")
        }

    }, function (a, b, c) {
        $.alert("删除失败");
        console.log(a, b, c)
    });

}
//添加图表
function addSelf(me) {
    var url = "/spmsPortalCustom/addSort";
    var self = {
        divname: me.attr("module")
    }
    console.log(self);
    doJsonRequest(url, self, function (a) {
        if (a.result) {
            a = a.data;
        }
        if (a && a["succcess"]) {
            $('.block_charts').append(me.show());
            $("li[module=" + me.attr("module") + "]").show();
        }

      //  console.log(a)
    }, function (a, b, c) {
        $.alert("添加失败");
       // console.log(a, b, c)
    });

}
// 展示可以添加的图表
function addShow() {
    var url = "/spmsPortalCustom/getCanAddSort";
    doJsonRequest(url, null, function (dat) {
        if (dat.result) {
            dat = dat.data;
            console.log(dat);
            var idToTitle = {
                userCountByArea: "用户数量区域统计",
                userCountByRule: "用户服务类别统计",
                userCountByOnlineStatus: "用户数在线及异常情况统计",
                deviceByModel: "设备库存域统计",
                deviceByOptStatus: "设备运营状态统计",
                userIncrementByDate: "订户开通/退订统计",
                SoftwareVersionStatistical:"软件版本统计"
            };
            // 生成弹出框
            $("#easyDialog_wrapper").remove();
            var unselect = dat;
            console.log(unselect);
            if (!objCheck(unselect)) {
                $.toast("没有可添加的模块", 1500);
                return;
            }
            var getPro = function (obj) {
                for (var key in obj) {
                    return key;
                }
            }
            var easyDialog_wrapper = $("<div></div>").addClass("easyDialog_wrapper").attr("id", "easyDialog_wrapper");
            var easyDialog_content = $("<div></div>").addClass("easyDialog_content");
            var h4 = $("<h4></h4>").addClass("easyDialog_title").html('<a href="javascript:close()"  title="关闭窗口" class="close_btn" id="closeBtn">×</a>添加图表');
            easyDialog_content.append(h4);
            easyDialog_wrapper.append(easyDialog_content);
            var easyDialog_text = $("<div></div>").addClass("easyDialog_text");
            easyDialog_content.append(easyDialog_text);
            var shadow = $("<div></div>").addClass("shadow");
            $("body").append(easyDialog_wrapper).append(shadow);
            var ul = $("<ul></ul>").css({
                width: "80%",
                margin: "1% 10%"
            })
            easyDialog_text.append(ul).addClass("unselect");
            for (var k in unselect) {
                var li = $("<li></li>").attr("id", k).css("cursor", "pointer").css("float", "left")
                    .css("width", "100%").css("position","relative")
                    .html("<p>" + unselect[k] + "</p>");
                ul.append(li);
                li.append($("<p>").css({
                    position: "absolute",
                 //   color: "#4b9",
                    "top":"0px",
                    right: "20px"
                }).addClass("added"));
            }

            $(".unselect li").on("click", function () {
                if ($(this).hasClass("sel")) {
                    $(this).removeClass("sel");
                    $(this).find(".added").html("");
                }
                else {
                    $(this).addClass("sel");
                    $(this).find(".added").html("√");
                }

            });

            var p = $("<p></p>").css({
                "text-align": "center",
                width: "100%"
            });
            easyDialog_content.append(p);
            var ensure = $("<button></button>").addClass("ensure").html("确  定");
            var cancel = $("<button></button>").addClass("cancel").html("取 消");

            p.append(ensure).append(cancel);
            //关闭弹出框
            cancel.on("click", function () {
                close();
            });
            //关闭弹出框
            $(".close_btn").on("click", function () {
                close();
            });

            ensure.on("click", function () {
                var module = [];
                $(".unselect li").each(function () {
                    if ($(this).hasClass("sel")) {
                        module.push($(this).attr("id"));
                    }
                });
                console.log(module)
                $(".block_charts li").each(function () {
                    console.log($(this).attr("module"))
                    if (module.indexOf($(this).attr("module")) > -1) {
                        addSelf($(this));
                        //   return;
                    }

                })
                close();

            });
            //关闭弹出框
            function close() {
                easyDialog_wrapper.remove();
                shadow.remove();
            }
        }
    }, function () {
        $.alert("获取失败")
    });
    return;


}
function move(li) {
    var url = "/spmsPortalCustom/changeSort";
    var idAndIndex = {};
    li.each(function (i) {
        idAndIndex[i + 1] = $(this).attr("module");
        /*  if (idAndIndex) {
         idAndIndex += "," + (i + 1).toString() + ":" + $(this).attr("module");
         }
         else {
         idAndIndex = (i + 1).toString() + ":" + $(this).attr("module");
         }*/
    });
    console.log(idAndIndex);
    var self = idAndIndex

    doJsonRequest(url, self, function (a) {
        console.log(a)
    }, function (a, b, c) {
        //alert("移动失败");
        console.log(a, b, c)
    });

}
var idAndIndex = [];
function init(parentDiv, portalSort) {
    var ids = ["userCountByArea", "userCountByRule", "userCountByOnlineStatus",
        'deviceByModel', "deviceByOptStatus", 'userIncrementByDate','userCountByProductRemaining','SoftwareVersionStatistical'];


    var idToTitle = {
        userCountByArea: "用户数量区域统计",
        userCountByRule: "用户服务类别统计",
        userCountByOnlineStatus: "用户数在线及异常情况统计",
        deviceByModel: "设备库存域统计",
        deviceByOptStatus: "设备运营状态统计",
        userIncrementByDate: "订户开通/退订统计",
        userCountByProductRemaining:"服务期限统计",
        SoftwareVersionStatistical:"软件版本统计信息"
    };
    var titles = [];
    for (var index in portalSort) {
        var self = {};
        self["index"] = index;
        self["id"] = portalSort[index];
        for (var key in portalSort[index]) {
            self["id"] = key;
            self["title"] = portalSort[index][key]
        }
        idAndIndex.push(self);
   //     console.log(idAndIndex);
        titles.push(idToTitle[self["id"]]);
    }
 //   console.log(idAndIndex)
    idAndIndex.sort(function (a, b) {

        return a["index"] > b["index"] ? 1 : -1;

    });
    var sortDiv = $(".charSort");
    for (var i = 0; i < idAndIndex.length; i++) {
        var li = $("<li></li>").addClass("chart_data tc border_02 bgcolor_02 mt5 fl")
            .css({
                width: "49%",
                padding: "0%",
                margin: ".25%"
            }).attr("index", idAndIndex[i]["index"]).attr("module", idAndIndex[i]["id"]);
        var div = $("<div></div>").addClass("widget-header").css({
            //    cursor: "move"
        });
        div.append($("<h3></h3>").html("<strong>" + idAndIndex[i]["title"] + "</strong>"))
            .append($("<div></div>").addClass("cha"));
        li.append(div).append($("<div></div>").attr("id", idAndIndex[i]["id"]).css({
            width: "99%",
            height: "400px",
            "overflow": "hidden",
            cursor: "auto"

        }));
        parentDiv.append(li);
        var liSort = $("<li></li>").attr("index", idAndIndex[i]["index"]).attr("module", idAndIndex[i]["id"]).html(idAndIndex[i]["title"]);
        $(".charSort").append(liSort);
    }
    for (var j = 0; j < ids.length; j++) {
        if ($("#" + ids[j]).length) {
            continue;
        }
        var li = $("<li></li>").addClass("chart_data tc border_02 bgcolor_02 mt5 fl")
            .css({
                width: "49%",
                padding: "0%",
                margin: ".25%"
            }).attr("index", i).attr("module", ids[j]);
        var div = $("<div></div>").addClass("widget-header").css({
            cursor: "move"
        });
        div.append($("<h3></h3>").html("<strong>" + idToTitle[ids[j]] + "</strong>"))
            .append($("<div></div>").addClass("cha"));
        li.append(div).append($("<div></div>").attr("id", ids[j]).css({
            width: "99%",
            height: "400px",
            "overflow": "hidden",
            cursor: "auto"

        }));

        parentDiv.append(li);
        var liSort = $("<li></li>").attr("index", i).attr("module", ids[j]).html(idToTitle[ids[j]]);
        sortDiv.append(liSort);
        i++;

    }


}
function charSort() {
    $(".charSort").parent().parent().css("display", "block");
    var shadow = $("<div></div>").addClass("shadow");
    $("body").append(shadow);
    shadow.on("click", function () {
        $(".charSort").parent().parent().hide();
        shadow.remove();
    });
}
function ajaxPost(url, data, suc, err, beforeSend, complete) {
    $.ajax({
        url: url,
        dataType: "json",
        // type: "POST",
        data: data,
        //contentType: "application/json",
        beforeSend: function () {
            if (beforeSend)
                beforeSend()
        },
        complete: function () {
            if (complete)
                complete();
        },
        success: function (a, b, c) {
            if (suc)
                suc(a, b, c);
        },
        error: function (a, b, c) {
            if (err)
                err(a, b, c);
        }
    })
}