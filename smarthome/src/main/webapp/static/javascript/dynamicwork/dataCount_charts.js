var start = getURLParam("start");
var end = getURLParam("end");
// 路径配置
var fileLocation = '/cmcp/static/javascript/component/echart/echarts-map';
require.config({
    paths:{ 
        'echarts' : fileLocation,
        'echarts/chart/bar' : fileLocation
    }
});

$(function(){
	doRequest("/dynawork/getViewSuperviseDC",{start:start,end:end},function(data){
		if(data.result) {
			var option = data.data;
			/*alert($.toJSON(option));
			return false;*/
			/*var option = {
				        'tooltip': {'show': true},
				        'legend': {'data':['李俊明','李世杰','王克建','操学诚','曹惠斌','张韬','刘振国','杨勤荣','批示总件数']},
				        'xAxis' : [
				            {
				                'type' : 'category',
				                'data' : ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月","全年"]
				            }
				        ],
				        'yAxis' : [
				            {
				                'type' : 'value'
				            }
				        ],
				        'series' : 
				        	[
				            {
				                "name":"李俊明",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40],
				                "type":"bar"
				            },
				            {
				                "name":"李世杰",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"王克建",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"操学诚",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"曹惠斌",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"张韬",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"刘振国",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"杨勤荣",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            },
				            {
				                "name":"批示总件数",
				                "type":"bar",
				                "data":[5, 20, 40, 50, 70, 60, 90, 100, 10, 20, 80, 25, 40]
				            }
				        ]
				    };*/
		
		// 使用
		require(['echarts','echarts/chart/bar'],function (ec) {
		    // 基于准备好的dom，初始化echarts图表
		    var myChart = ec.init(document.getElementById('main')); 
		        // 为echarts对象加载数据 
		        myChart.setOption(option); 
		    });
	}
	});
})

