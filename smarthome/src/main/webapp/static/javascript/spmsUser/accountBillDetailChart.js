function createChart1(content,data){
	$(content).highcharts({
		plotOptions : {
			column : {
				dataLabels : {
					rotation: -90,
					enabled : true,
					y: -40
				}
			}
		},
        chart: {
            type: 'column'
        },
        title: {
            align: "left",
            text: data.title
        },
        xAxis: {
            categories: data.xAxis,
            labels: {
                rotation:-45
            }
        },
        exporting:{  
            filename:'chart',  
            url:'http://localhost:8080/HighChartsDemo/SaveAsImage'//这里是一个重点哦,也可以修改exporting.js中对应的url  
        },
        yAxis: {
            min: 0,
            title: {
                text: '用电量'
            }
        }, tooltip: {
            formatter: function () {

                var s = this.y + "千瓦时";
                return s
            },
            useHTML: true,
            shared: true,
            crosshairs: true
        },
        series: [
            {
                name: "用电量",
                data: data.data
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
function createChart2(content, data) {
	var charData = [];
	var keys = data.keys;
	var values = data.values;
	var dataItem = {};
	for (var i = 0; i < keys.length; i++) {
		dataItem.name = keys[i];
		dataItem.y = values[i];
	}
	charData.push(dataItem);
	$(content)
			.highcharts(
					{
						chart : {
							plotBackgroundColor : null,
							plotBorderWidth : null,
							plotShadow : false
						},
						title : {
							align : "left",
							text : data.title
						},
						tooltip : {
							pointFormat : '{series.name}: <b>{point.percentage:.2f}%</b><br />用电量: <b>{point.y}千瓦时</b>'
						},
						plotOptions : {
							pie : {
								allowPointSelect : true,
								cursor : 'pointer',
								dataLabels : {
									enabled : true,
									formatter : function() {
										if(this.percentage > 0){
											return this.point.name + "：" + this.percentage + "%";
										}
									},
									style: {
						                  font: '18px Trebuchet MS, Verdana, sans-serif'
						            }
								},
								showInLegend : true,
							},
						},
						series : [ {
							type : 'pie',
							name : '占比',
							data : charData
						} ],
						exporting : {
							enabled : false
						},
						credits : {
							enabled : false
						// 禁用版权信息
						}
					});
}
$(function(){
	var id = getURLParam("id");
	var dto = {id:id};
	doRequest('/spmsAccountBill/createChart1', dto, function (data) {
		 data.data.title = data.data.billCycle + "每日用电量情况";
		 createChart1("#highChart1",data.data);
	});
	doRequest('/spmsAccountBill/createChart2', dto, function (data) {
		data.data.title = data.data.billCycle + "设备用电量占比";
		createChart2("#highChart2",data.data);
	});
	
});
function back(){
	history.go(-1);
}
function sendEmail(){
	var id = getURLParam("id");
	var dto = {'id': id + ""};
	var conf = {};
	conf.showWaiting = true;
    doRequest('/spmsAccountBill/sendAccountEmail', dto, function (data) {
    	if(data.data.flag == 1){
    		$.alert("发送账单成功");
    	}else{
    		$.alert("发送账单失败");
    	}
    },conf);
}

function printBill(){
	var bdhtml=window.document.body.innerHTML;//获取当前页的html代码  
	var sprnstr="<!--begin-->";//设置打印开始区域  
	var eprnstr="<!--end-->";//设置打印结束区域  
	var prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+12); //从开始代码向后取html  
	prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html  
	window.document.body.innerHTML=prnhtml;  
	window.print();  
	window.document.body.innerHTML=bdhtml; 
}
