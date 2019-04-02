if(getURLParam("type") == 1) {
    $("#the_name").html("网关列表");
} else if(getURLParam("type") == 2) {
    $("#the_name").html("空调列表");
} else {
    $("#the_name").html("传感器列表");
}
/**
 * Created by Administrator on 2015/1/27.
 */
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});

var onOffStatus = function (dom, data) {
    if (!data || !data.length) {
        $(dom).attr("style", "").addClass("input_view").attr("style", "text-align:center").html("暂无数据");
        return;
    }
    $(dom).highcharts('StockChart', {

        chart: {
            backgroundColor: '#FFFFFF'
        },
        rangeSelector: {
            inputEnabled: false
        },
        navigator: {
            series: {
                step: true,
                type: 'line'
            }
        },
        yAxis: {
            opposite: false,
            gridLineWidth: 0,
            labels: {
                style: {
                    color: "#080808"
                },
                formatter: function (d) {
                    console.log(d, this);
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
        xAxis: {
            minTickInterval: 5 * 60 * 1000,
            minRange: 3600 * 1000
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
        series: [
            {
                name: "开关记录",
                data: data,
                step: true
            }
        ]
    })
}
var gwStatus = function (dom, data) {
    if (!data || !data.length) {
        $(dom).attr("style", "").addClass("input_view").attr("style", "text-align:center").html("暂无数据");

        return;
    }
    $(dom).highcharts('StockChart', {

        chart: {
            backgroundColor: '#FFFFFF'
        },
        rangeSelector: {
            inputEnabled: false
        },
        navigator: {
            series: {
                step: true,
                type: 'line'
            }
        },
        yAxis: {
            opposite: false,
            gridLineWidth: 0,
            labels: {
                style: {
                    color: "#080808"
                },
                formatter: function (d) {
                    //     console.log(d, this);
                    if (this.value == 1) {
                        return "在线";
                    } else if (this.value == 0) {
                        return "离线";
                    } else if (this.value == 2) {
                        return "异常";
                    } else {
                        return "";
                    }
                }
            }
        },
        xAxis: {
            minTickInterval: 5 * 60 * 1000,
            minRange: 3600 * 1000
        },
        tooltip: {
            headerFormat: '<b> 运行记录</b><br>',
            formatter: function (d) {
                //    console.log(d, this);
                if (this.y == 1) {
                    return "<b>运行状态为：在线</b><br>";
                } else if (this.y == 0) {
                    return "<b>运行状态为：离线</b><br>";
                } else if (this.y == 2) {
                    return "<b>运行状态为：异常</b><br>";
                } else {
                    return "";
                }
            }



        },
        series: [
            {
                name: "运行状态",
                data: data,
                step: true
            }
        ]
    });
}

var generateChartFour = function (currents, voltages, frequencys) {
    var data1 = currents.slice(0);
    var data2 = voltages.slice(0);
    var data3 = frequencys.slice(0);
    $('#power').highcharts('StockChart', {
        chart: {
            type: 'spline',
            alignTicks: false
        },
        navigator: {
            enabled: true
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        legend: {
            enabled: true,
            layout: 'vertical',
            align: 'right',
            x: -40,
            verticalAlign: 'top',
            y: -10,
            floating: true,
            backgroundColor: '#FFFFFF'
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
                    hover: {
                    },
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
        xAxis: {
            labels: {
                style: {
                    color: "#856963"
                }
            }
        },
        yAxis: [
            {
                //电流
                lineColor: '#c0c0c0',
                lineWidth: 1, //坐标=轴
                gridLineWidth: 0,
                labels: {
                    formatter: function () {
                        return this.value + 'A';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: '电流',
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: false, //是否显示在右边
                min: 0
            },
            {
                //电压
                lineColor: '#c0c0c0',
                lineWidth: 1,
                gridLineWidth: 0,
                labels: {
                    formatter: function () {
                        return this.value + 'V';
                    },
                    style: {
                        color: '#4572A7'
                    }
                },
                title: {
                    text: '电压',
                    style: {
                        color: '#4572A7'
                    }
                },
                opposite: false, //是否显示在右边
                min: 0
            },
            {
                //频率
                lineColor: '#c0c0c0',
                lineWidth: 1,
                gridLineWidth: 0,
                labels: {
                    formatter: function () {
                        return this.value + 'Hz';
                    },
                    style: {
                        color: '#CD950C'
                    }
                },
                title: {
                    text: '频率',
                    style: {
                        color: '#CD950C'
                    }
                },
                opposite: true, //是否显示在右边
                min: 0
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
        series: [
            {
                //电流
                name: '电流',
                color: '#89A54E',
                data: data1,
                yAxis: 0,
                tooltip: { valueSuffix: ' A' }
            },
            {
                //电压
                name: '电压',
                color: '#4572A7',
                data: data2,
                yAxis: 1,
                tooltip: { valueSuffix: ' V' }
            },
            {
                //频率
                name: '频率',
                color: '#CD950C',
                data: data3,
                yAxis: 2,
                tooltip: { valueSuffix: ' Hz' }
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
        }
    });
}
var generateChartThree = function (activeDemands, reactiveDemands) {
    var data1 = activeDemands.slice(0);
    var data2 = reactiveDemands.slice(0);

    $('#power').highcharts('StockChart', {
        chart: {
            type: 'spline',
            zoomType: 'xy',
            animation: Highcharts.svg
        },
        navigator: {
            enabled: true
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        legend: {
            enabled: true,
            layout: 'vertical',
            align: 'right',
            x: 10,
            verticalAlign: 'top',
            y: -10,
            floating: true,
            backgroundColor: '#FFFFFF'
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
                    hover: {
                    },
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
        xAxis: {
            labels: {
                style: {
                    color: "#856963"
                }
            }
        },
        yAxis: [
            {
                //有功需求
                labels: {
                    formatter: function () {
                        return this.value + 'KwH';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: '需求',
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: false, //是否显示在右边
                min: 0
            }/* ,{
             //无功需求
             labels: {
             formatter: function() {
             return this.value +'KwH';
             },
             style: {
             color: '#4572A7'
             }
             },
             title: {
             text: '无功需求',
             style: {
             color: '#4572A7'
             }
             },
             opposite: true, //是否显示在右边
             min : 0
             } */
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
        series: [
            {
                //有功需求
                name: '有功需求',
                color: '#89A54E',
                data: data1,
                yAxis: 0,
                tooltip: { valueSuffix: ' KwH' }
            },
            {
                //无功需求
                name: '无功需求',
                color: '#4572A7',
                data: data2,
                yAxis: 0,
                tooltip: { valueSuffix: ' KwH' }
            }
        ]
    });
}

var generateChartTwo = function (eps, reactiveEnergys) {
    console.log(eps)
    var data1 = eps.slice(0);
    var data2 = reactiveEnergys.slice(0);

    $('#power').highcharts('StockChart', {
        chart: {
            type: 'column',
            alignTicks: false,
            backgroundColor: "rgba(0,0,0,0)"//,
            //animation: Highcharts.svg
        },
        navigator: {
            enabled: true
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        legend: {
            enabled: true,
            layout: 'vertical',
            align: 'right',
            x: 0,
            verticalAlign: 'top',
            y: -10,
            floating: true,
            backgroundColor: '#FFFFFF'
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
                    hover: {
                    },
                    select: {
                        fill: '#039',
                        style: {
                            color: 'white'
                        }
                    }
                }
            },
            buttonSpacing: 20,
            buttons: [],
            inputEnabled: false,
            labelStyle: {
                color: 'silver',
                fontWeight: 'bold'
            },
            selected: 0
        },
        xAxis: {
            labels: {
                style: {
                    color: "#856963"
                },
                formatter:function(){
                	if(rbtn == 4){
                			return  Highcharts.dateFormat('%Y-%m', this.value); 
                	}else if(rbtn == 3){
                			return  Highcharts.dateFormat('%Y-%m-%d', this.value); 
                	}else if(rbtn == 2){
                			return  Highcharts.dateFormat('%H', this.value); 
                	}else if(rbtn == 1){
                			return  Highcharts.dateFormat('%H:%M:%S', this.value); 
                	}
                }
            }
        },
        yAxis: [
            {
                //有功电能
                //tickPositions: [0, 20, 50, 100] // 指定竖轴坐标点的值
                lineColor: '#c0c0c0',
                lineWidth: 1, //坐标=轴
                labels: {
                    formatter: function () {
                        return this.value + 'KwH';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: '电能',
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: false, //是否显示在右边
                min: 0
            }/*,{
             //无功电能
             labels: {
             formatter: function() {
             return this.value +'KwH';
             },
             style: {
             color: '#4572A7'
             }
             },
             title: {
             text: '无功电能',
             style: {
             color: '#4572A7'
             }
             },
             opposite: true, //是否显示在右边
             min : 0
             }*/
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
                //有功电能
                name: '有功电能',
                color: '#89A54E',
                data: data1,
                yAxis: 0,
                tooltip: { valueSuffix: ' KwH' }
            },
            {
                //无功电能
                name: '无功电能',
                color: '#4572A7',
                data: data2,
                yAxis: 0,
                tooltip: { valueSuffix: ' KwH' }
            }
        ]

    });
}
var generateChartOne = function (powers, reactivePowers, powerFactors, apparentPowers) {
    var data1 = powers.slice(0);
    var data2 = reactivePowers.slice(0);
    var data3 = powerFactors.slice(0);
    var data4 = apparentPowers.slice(0);
    $('#power').highcharts('StockChart', {
        chart: {
            type: 'spline',
            alignTicks: false
        },
        navigator: {
            enabled: true
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        },

        legend: {
            enabled: true,
            layout: 'Horizontal',
            align: 'right',
            x: 0,
            verticalAlign: 'top',
            y: -10,
            floating: true,
            backgroundColor: '#FFFFFF'
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
                    hover: {
                    },
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
        xAxis: {
            labels: {
                style: {
                    color: "#856963"
                }
            }
        },
        yAxis: [
            {
                //有功功率
                lineColor: '#c0c0c0',
                lineWidth: 1,
                //     gridLineWidth: 0,
                labels: {
                    formatter: function () {
                        return this.value + 'W';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: '功率',
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: false, //是否显示在右边
                min: null
            },
            /*{
             //无功功率
             labels: {
             formatter: function() {
             return this.value +'Kw';
             },
             style: {
             color: '#4572A7'
             }
             },
             title: {
             text: '无功功率',
             style: {
             color: '#4572A7'
             }
             },
             opposite: false, //是否显示在右边
             min : 0
             },*/{
                //功率因数
                lineColor: '#c0c0c0',
                lineWidth: 1, //坐标=轴
                gridLineWidth: 0, //中间横隔
                labels: {
                    formatter: function () {
                        return this.value + '%';
                    },
                    style: {
                        color: '#CD950C'
                    }
                },
                title: {
                    text: '功率因数',
                    style: {
                        color: '#CD950C'
                    }
                },
                offset: 0,
                max: 100,
                opposite: true, //是否显示在右边
                min: 0
            }/*{
             //视在功率
             labels: {
             formatter: function() {
             return this.value +'Kw';
             },
             style: {
             color: '#AA4643'
             }
             },
             title: {
             text: '视在功率',
             style: {
             color: '#AA4643'
             }
             },
             opposite: true, //是否显示在右边
             min : 0
             }*/
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
        series: [
            {
                //有功功率
                name: '有功功率',
                color: '#89A54E',
                data: data1,
                yAxis: 0,
                tooltip: { valueSuffix: ' W' }
            },
            {
                //无功功率
                name: '无功功率',
                color: '#4572A7',
                data: data2,
                yAxis: 0,
                tooltip: { valueSuffix: ' W' }
            },
            {
                //功率因数
                name: '功率因数',
                color: '#CD950C',
                data: data3,
                yAxis: 1,
                tooltip: { valueSuffix: ' %' }
            },
            {
                //视在功率
                name: '视在功率',
                color: '#AA4643',
                data: data4,
                yAxis: 0,
                tooltip: { valueSuffix: ' W' }
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
        }
    });
}