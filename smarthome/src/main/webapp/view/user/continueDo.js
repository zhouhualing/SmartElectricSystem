/**
 * Created by Administrator on 2015/1/23.
 */
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});
var ydl = function (div, data) {
    var chart = $(div).highcharts('StockChart', {
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
               /* {
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
                }*/
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
    })
    return chart;
}

var ydxx = function (div, data) {
    var chart = $(div).highcharts('StockChart', {
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
               /* {
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
                /*{
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
                }*/
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
        xAxis: {
            labels: {
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
    })
    return chart;
}
var onOffStatus = function (dom, data) {
    $(dom).highcharts('StockChart', {

        chart: {
            backgroundColor: "rgba(0,0,0,0)"
        },
        rangeSelector: {
            /*inputEnabled: false*/
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
              
               
            ],
            inputEnabled: false,
            labelStyle: {
                color: 'silver',
                fontWeight: 'bold'
            },
            selected: 0
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
                    color: "#ffffff"
                },
                formatter: function (d) {
                    //console.log(d, this);
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
                //console.log(d, this);
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