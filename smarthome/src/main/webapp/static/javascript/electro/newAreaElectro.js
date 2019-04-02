Highcharts.setOptions({ global: { useUTC: false },
    yAxis: {
        lineColor: '#c0c0c0',
        lineWidth: 1, //坐标=轴
        gridLineWidth: 0} });


function generateChartOne(powers, reactivePowers, powerFactors, apparentPowers,deviceNum) {
    var data1 = powers.slice(0);
    var data2 = reactivePowers.slice(0);
    var data3 = powerFactors.slice(0);
    var data4 = apparentPowers.slice(0);
  //  console.log(data1);
    $('#aeraPowerContent').highcharts('StockChart', {
        chart: {
            type: 'spline',
            alignTicks: false,
            animation: false
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
            x: 17,
            verticalAlign: 'top',
            y: -15,
            floating: true,
            backgroundColor: '#FAFAFA'
        },
        rangeSelector: {
            buttonTheme: { // styles for the buttons
                fill: 'none',
                stroke: 'none',
                'stroke-width': 0,
                r: 8,

                style: {
                    color: '#039',
                    "margin-left": 40,
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
//                {
//                    type: 'minute',
//                    count: 60,
//                    text: '1小时'
//                },
//                {
//                    type: 'minute',
//                    count: 720,
//                    text: '12小时'
//                },
//                {
//                    type: 'day',
//                    count: 1,
//                    text: '1天'
//                },
//                {
//                    type: 'all',
//                    text: '全部'
//                }
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
                        return Highcharts.numberFormat(this.value,2) + 'W';
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
                min: 0,
                opposite: false //是否显示在右边

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
                        return Highcharts.numberFormat(this.value,2) + '%';
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
                ///    max: 100,
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
        tooltip: {
            formatter: function () {
              //  console.log(this);
                var index = 0;
                /* for (var i = 0; i < categories.length; i++) {
                 if (this.x == categories[i]) {
                 index = i;
                 break;
                 }
                 }*/
                var s = "";
               //s += "设备数量：<b>"+(deviceNum[this.x]== null ? 0:deviceNum[this.x])+"</b>台</br>";
                for (var i = 0; i < this["points"].length; i++) {

                    if (this["points"][i]["series"]["name"].indexOf("因")>0) {
                        s += this["points"][i]["series"]["name"] + "：<b>" + Highcharts.numberFormat(this["points"][i]["y"],2) + "</b> %</br>"
                    }else if(this["points"][i]["series"]["name"].indexOf("备")>0){
                    	s += this["points"][i]["series"]["name"] + "：<b>" + Highcharts.numberFormat(this["points"][i]["y"],0) + "</b> 台</br>"
                    }else {
                        s += this["points"][i]["series"]["name"] + "：<b>" + Highcharts.numberFormat(this["points"][i]["y"],2) + "</b> W</br>"
                    }
                }

                return s
            },
            useHTML: true,
            shared: true,
            crosshairs: true
        },
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
                //视在功率
                name: '视在功率',
                color: '#AA4643',
                data: data4,
                yAxis: 0,
                unit: " W",
                tooltip: { valueSuffix: ' W' }
            },
            {
                //有功功率
                name: '有功功率',
                color: '#89A54E',
                data: data1,
                yAxis: 0,
                unit: " W",
                tooltip: { valueSuffix: ' W' }
            },
            {
                //无功功率
                name: '无功功率',
                color: '#4572A7',
                data: data2,
                yAxis: 0,
                unit: " W",
                tooltip: { valueSuffix: ' W' }
            },
            {
                //功率因数
                name: '功率因数',
                color: '#CD950C',
                data: data3,
                yAxis: 1,
                unit: " %",
                tooltip: { valueSuffix: ' %' }
            },
            {
	            //设备数量
	            name: '设备数量',
	            color: '#22BBCC',
	            data: deviceNum,
	            yAxis: 0,
	            unit: " 台",
	            tooltip: { valueSuffix: ' 台' }
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
function generateChartTwo(eps, reactiveEnergys) {
    var data1 = eps;
    var data2 = reactiveEnergys;

        $('#aeraPowerContent').highcharts('StockChart', {
            chart: {
                type: 'column',
                alignTicks: false,
                backgroundColor: "rgba(0,0,0,0)",
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
                backgroundColor: '#FAFAFA'
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
                            return Highcharts.numberFormat(this.value,2) + 'KwH';
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
            plotOptions: {
                column: {
//                	grouping:false
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
            ],
            tooltip: {
                formatter: function () {
                	var index = 0;
                    var s = "";
                    for (var i = 0; i < this["points"].length; i++) {

                        if (this["points"][i]["series"]["name"].indexOf("有")>0) {
                            s += this["points"][i]["series"]["name"] + "：<b>" + Highcharts.numberFormat(this["points"][i]["y"],3) + "</b> KwH</br>"
                        }else {
                            s += this["points"][i]["series"]["name"] + "：<b>" + Highcharts.numberFormat(this["points"][i]["y"],3) + "</b> KwH</br>"
                        }
                    }

                    return s
                },
                useHTML: true,
                shared: true,
                crosshairs: true
            }

        });
    }
function generateChartThree(activeDemands, reactiveDemands) {
    var data1 = activeDemands.slice(0);
    var data2 = reactiveDemands.slice(0);

    $('#aeraPowerContent').highcharts('StockChart', {
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
            backgroundColor: '#FAFAFA'
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
            	labels: {
                    formatter: function () {
                        return Highcharts.numberFormat(this.value,2) + 'KwH';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                //有功需求
                lineColor: '#c0c0c0',
                lineWidth: 1, //坐标=轴
                //  gridLineWidth: 0, //中间横隔
                labels: {
                    formatter: function () {
                        return Highcharts.numberFormat(this.value,2) + 'KwH';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: '功能',
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: false, //是否显示在右边
                min: 0
            }/*,{
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

function generateUserStatus(statusData) {
    var charData = [];
    var sum = 0;
    for (var i = 0; i < statusData.length; i++) {
        var item = statusData[i];
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
            sum += item[key];
        }
        charData.push(dataItem);
    }
    $('#statusDataContent').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            align: "left",
            text: '用户状态统计'
        },
        subtitle: {
            text: '总用户数:<br/>' + sum,
            useHTML: true,
            floating: true,

            style: {
                fontSize: "18px",
                textAlign: "center",
                marginTop: "18px!important"
            },
            verticalAlign: 'middle'
        },
        tooltip: {
            enabled: true,
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b><br />数量: <b>{point.y}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                innerSize: "50%",
                animation: false,
                dataLabels: {
                    enabled: true,

                    format: '<b>{point.name}</b>',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
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
            enabled: false // 禁用版权信息
        }
    });
}

function generateUserEleLevelStatus(statusData) {
    var charData = [];
    var sum = 0;
    for (var i = 0; i < statusData.length; i++) {
        var item = statusData[i];
        var dataItem = {};
        for (var key in item) {
            dataItem.name = key;
            if (key === '高用电设备') {
                dataItem.color = '#ed1b24';
            } else if (key === '关机') {
                dataItem.color = '#cccccc';
            } else if (key === '低用电设备') {
                dataItem.color = '#00a650';
            }
            dataItem.y = item[key];
            sum += item[key];
        }
        charData.push(dataItem);
    }
    $('#areaElectroLevel').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            align: "left",
            text: '设备用电级别统计'
        },
        subtitle: {
            text: '总设备数:<br/>' + sum,
            useHTML: true,
            floating: true,

            style: {
                fontSize: "18px",
                textAlign: "center",
                marginTop: "18px!important"
            },
            verticalAlign: 'middle'
        },
        tooltip: {
            enabled: true,
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b><br />数量: <b>{point.y}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                innerSize: "50%",
                animation: false,
                dataLabels: {
                    enabled: true,

                    format: '<b>{point.name}</b>',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
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
            enabled: false // 禁用版权信息
        }
    });
}

var rbtnClick = false;
function rBtnClick(id){
	rbtnClick = true;
	$("#rBtn1").css("background-color","rgb(0, 51, 153)");
	$("#rBtn2").css("background-color","rgb(0, 51, 153)");
	$("#rBtn3").css("background-color","rgb(0, 51, 153)");
	$("#rBtn4").css("background-color","rgb(0, 51, 153)");
	$("#" + id).css("background-color","orange");
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
	 rUpdateData();
}

function rUpdateData(){
	var dat = {
	        areaId: id,
	    	rbtn:rbtn
	    }
		console.log(dat);
	    console.log(Time++);
	    clearTimeout(refresh);
	    doJsonRequest("/electro/updateData", dat, function (data) {
	        refresh = setTimeout("updateData()", 5000);
	        if (data.result) {
	        	if($("#switchChart").val() == 2){
	        		if (data.data.newpowers.length != 0) {
	        			var neweps = data.data.neweps;
	        			var newreactiveEnergys = data.data.newreactiveEnergys;
	        			generateChartTwo(neweps, newreactiveEnergys);
	        		}
	        	}else if($("#switchChart").val() == 1){
//	        		var chart = $('#aeraPowerContent').highcharts();
	        		deviceNum =data.data.newdeviceNum;
	        		powers = data.data.newpowers;
	                reactivePowers = data.data.newreactivePowers;
	                powerFactors = data.data.newpowerFactors;
	                apparentPowers = data.data.newapparentPowers;
	                activeDemands = data.data.newactiveDemands;
	                reactiveDemands = data.data.newreactiveDemands;
//	                chart.series[0].setData(apparentPowers);
//                    chart.series[1].setData(powers);
//                    chart.series[2].setData(reactivePowers);
//                    chart.series[3].setData(powerFactors);
//                    chart.series[4].setData(deviceNum);
//                    $('#aeraPowerContent').highcharts();
	                generateChartOne(powers, reactivePowers, powerFactors, apparentPowers,deviceNum);
	        	}
	        }
	        rbtnClick = false;
	    },{showWaiting:true});
}
