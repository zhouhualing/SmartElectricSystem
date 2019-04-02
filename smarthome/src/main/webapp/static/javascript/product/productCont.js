var arrStart = getBeoreMonth(2);
$("#beginDate").val(arrStart[0]+"年"+arrStart[1]+"月");
var arrEnd = getBeoreMonth(0);
$("#endDate").val(arrEnd[0]+"年"+arrEnd[1]+"月");
var productTypeIds = [];
var productTypeMap = [];
/**
 * Created by Administrator on 2015/2/2.
 */
$(function () {
    doQuery();
//    var URL = "/spmsProduct/getAllProducttype"
//    doJsonRequest(URL, null, function (dat) {
//        if (dat.result) {
//            console.log(dat);
//            var types = dat["data"]
//            var kinds = d3.select("#kinds");
//            kinds.append("option").attr("value","").html("全部").attr("selected",true);
//            if (types) {
//                for (var type in  types) {
//                    productTypeMap[types[type]] == type;
//                    if (type == objFirst(types)) {
//                        kinds.append("option").attr("value", types[type]).html(type);
//                    } else {
//                        kinds.append("option").attr("value", types[type]).html(type);
//                    }
//                }
//            }
//            $("#kinds").select2();
//            getChartData();
//            $("#kinds").change(function () {
//                getChartData();
//            });
//        }
//    }, {
//        showWaiting: true,
//    handdingInfo: "获取数据..",
//        successInfo: "获取成功",
//        failtureInfo: "获取失败"
//})

});


var getChartData = function () {
    //var self = {};
    //var URL = "/spmsProduct/getCountAmount";
    //self.type = $("#kinds").val() || "";
    //self.start = $("#beginDate").val() || "";
    //self.end = $("#endDate").val() || "";
    //doJsonRequest(URL, self, function (dat) {
    //    if (dat.result) {
    //        console.log(dat);
    //        var data = dat["data"];
    //        var dom = d3.select("#xiaoshoujine").node();
    //        columnType1(dom, data, "销售数量与金额统计", ["销售数量", "金额"], ["数量","金额"])
    //    }
    //});
    //var URL = "/spmsProduct/getCostEarnings";
    //doJsonRequest(URL, self, function (dat) {
    //    if (dat.result) {
    //        console.log(dat);
    //        var data = dat["data"];
    //        var dom = d3.select("#chengbenshouyi").node();
    //        columnType2(dom, data, "成本与收益统计", ["成本", "收益"], ["", "金额"]);
    //
    //    }
    //});
    //var URL = "/spmsProduct/getOldEarnings";
    //doJsonRequest(URL, self, function (dat) {
    //    if (dat.result) {
    //    	var data = dat["data"];
    //    	var dom = d3.select("#chanpinchengben").node();
    //        lineType(dom, data, "同期对比曲线", ["成本同比增长", "收益同比增长"], ["", ""])
    //    }
    //});

}
//标识是否需要初始化产品类型
var flag =true;
function initFun(data,key,tr,returnInfos) {
    if(flag) {
        var types = returnInfos.productTypes;
        var kinds = d3.select("#kinds");
        kinds.append("option").attr("value","").html("全部").attr("selected",true);
        if (types) {
            for (var type in  types) {
                productTypeIds.push(types[type]);
                productTypeMap.push(type)
                if (type == objFirst(types)) {
                    kinds.append("option").attr("value", types[type]).html(type);
                } else {
                    kinds.append("option").attr("value", types[type]).html(type);
                }
            }
        }
        $("#kinds").select2();
        flag = false;
    }
    if(key == "productType") {
        var index = $.inArray(data[key],productTypeIds);
        if(index != -1) {
            return productTypeMap[index];
        } else {
            return "全部";
        }

    }
}

function queryEnd (datas) {
    var chart1Data = datas.chart1;
    console.log(chart1Data);
    var data = chart1Data;
    var dom = d3.select("#xiaoshoujine").node();
    columnType1(dom, data, "销售数量与金额统计", ["销售数量", "销售额"], ["数量","金额"]);


    var chart2Data = datas.chart2;
    console.log(chart2Data);
    data = chart2Data;
    dom = d3.select("#chengbenshouyi").node();
    columnType2(dom, data, "成本与收益统计", ["成本", "收益"], ["", "金额"]);

    var chart3Data = datas.chart3;
    data = chart3Data;
    dom = d3.select("#chanpinchengben").node();
    lineType(dom, data, "同期对比曲线", ["成本同比增长", "收益同比增长"], ["", ""])
}
var objFirst = function (obj) {
    for (var key in obj) {
        return key;
    }
    return "";
}
var objCheck = function (obj) {
    for (var k in obj) {
        return true;
    }
    return false;
}
Highcharts.setOptions({ global: { useUTC: false },
    colors: [ "rgb(255,204,0)","rgb(104,202,255)"],
    yAxis: {
        lineColor: '#c0c0c0',
        gridLineWidth: 0,
        lineWidth: 1
        //   lineColor: '#c0c0c0',
        //   minTickInterval: 1
    } });
var columnType1 = function (dom, data, title, names, yAx) {
    if (!data || !objCheck(data)) {
        $(dom).attr("style", "").addClass("input_view").attr("style", "text-align:center").html("暂无数据");
        return;
    }
    var name0 = "", name1 = "", categories = [], data0 = [], data1 = [];
    for (var name in data) {
        categories.push(name);
        data0.push(data[name][0]);
        data1.push(data[name][1]);
    }
    $(dom).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: title,
            style: {
                "font-size": 18,
                "font-weight": "bold"

            }
        },
        xAxis: {
            categories: categories
        },
        yAxis: [
            {
                min: 0,
                minTickInterval: 1,
                title: {
                    text: yAx[0]
                },
                labels: {
                    formatter: function () {
                        return this.value;
                    },
                    style: {
                        color: '#89A54E'
                    }
                }
            },
            {
                min: 0,
                minTickInterval: 1,
                gridLineWidth: 0,
                opposite: true,
                title: {
                    text: yAx[1]
                },
                labels: {
                    formatter: function () {
                        return this.value;
                    },
                    style: {
                        color: '#89A54E'
                    }
                }
            }
        ],

        series: [

            {
                name: names[0],
                yAxis: 0,
               /* tooltip: { valueSuffix: ' W' },*/
                data: data0
            },   {
                name: names[1],
                yAxis: 1,
                tooltip: { valueSuffix: ' 元' },
                data: data1
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
var columnType2 = function (dom, data, title, names, yAx) {
    if (!data || !objCheck(data)) {
        $(dom).attr("style", "").addClass("input_view").attr("style", "text-align:center").html("暂无数据");
        return;
    }
    var name0 = "", name1 = "", categories = [], data0 = [], data1 = [];
    for (var name in data) {
        categories.push(name);
        data0.push(data[name][0]);
        data1.push(data[name][1]);
    }
    $(dom).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: title,
            style: {
                "font-size": 18,
                "font-weight": "bold"

            }
        },
        xAxis: {
            categories: categories
        },
        yAxis: [
            {
                min: 0,
                minTickInterval: 1,
                title: {
                    text: yAx[0]
                },
                labels: {
                    formatter: function () {
                        return this.value;
                    },
                    style: {
                        color: '#89A54E'
                    }
                }
            },
            {
                min: 0,
                minTickInterval: 1,
                gridLineWidth: 0,
                opposite: true,
                title: {
                    text: yAx[1]
                },
                labels: {
                    formatter: function () {
                        return this.value;
                    },
                    style: {
                        color: '#89A54E'
                    }
                }
            }
        ],

        series: [

            {
                name: names[0],
                yAxis: 0,
                tooltip: { valueSuffix: ' 元' },
                data: data0
            },   {
                name: names[1],
                yAxis: 1,
                tooltip: { valueSuffix: ' 元' },
                data: data1
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
var lineType = function (dom, data, title, names, yAx) {
   /* if (!data || !objCheck(data)) {
        $(dom).attr("style", "").addClass("input_view").attr("style", "text-align:center").html("暂无数据");
        return;
    }*/
    var name0 = "", name1 = "", categories = [], data0 = [], data1 = [];
    for (var name in data) {
        categories.push(name);
        data0.push(data[name][0]);
        data1.push(data[name][1]);
    }
    $(dom).highcharts({
        chart: {
            type: 'spline'
        },
        title: {
            align: "left",
            text: title,
            style: {
                "font-size": 18,
                "font-weight": "bold"

            }
        },
        xAxis: {
            categories: categories
        },
        yAxis: [
            {
                min: 0,
                minTickInterval: 1,
                title: {
                    text: yAx[0]
                },
                labels: {
                    formatter: function () {
                        return this.value + '%';
                    },
                    style: {
                        color: '#89A54E'
                    }
                }
            }
        ],

        series: [
            {
                name: names[1],
                yAxis: 0,
                tooltip: { valueSuffix: ' %' },
                data: data1
            },
            {
                name: names[0],
                yAxis: 0,
                tooltip: { valueSuffix: ' %' },
                data: data0
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