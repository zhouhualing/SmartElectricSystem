var type = getURLParam("filterType");

if(type != null) {
	$("#filterDiv").removeClass("hidden");
	$("[name='type']").eq(parseInt(type)-1).attr("checked","")
}


    
var myChart = [];
var domCode = $("[md='sidebar-code']");

var domGraphic = $("[md='graphic']");

var domMain = $("[md='main']");

var domMessage = $("[md='wrong-message']");
var iconResize = $("[md='icon-resize']");

var needRefresh = false;

    function findIdxFromEvent(event) {
        event = event || window.event;
        return findIdx(event.target || event.srcElement);
    }

    function findIdx(d) {
        var p = d;
        while (p.className != 'container-fluid') {
            p = p.parentElement;
        }
        return $(p).attr('idx');
    }

var idx;

    function autoResize(event) {
        idx = findIdxFromEvent(event);
        if (iconResize[idx].className == 'icon-resize-full') {
            focusCode();
            iconResize[idx].className = 'icon-resize-small';
        }
        else {
            focusGraphic();
            iconResize[idx].className = 'icon-resize-full';
        }
    }


    function focusCode() {
        domCode[idx].className = 'span8 ani';
        domGraphic[idx].className = 'span4 ani';
    }

    function focusGraphic() {
        domCode[idx].className = 'span4 ani';
        domGraphic[idx].className = 'span8 ani';
        if (needRefresh) {
            myChart[idx].showLoading();
            setTimeout(refresh, 100);
        }
    }

var domTextarea = $("[md='code']");

var editor = [];
	$(function(){
	    for (var i = 0, l = domTextarea.length; i < l; i++) {
	        editor[i] = CodeMirror.fromTextArea(
	            domTextarea[i],
	            { lineNumbers: true }
	        );
	        editor[i].setOption("theme", 'monokai');
	        editor[i].on('change', function (){needRefresh = true;});
	    }
		
	})

    function refresh(isBtnRefresh, idd){
        if (isBtnRefresh) {
            idx = idd;
            needRefresh = true;
            focusGraphic();
            return;
        }
        needRefresh = false;
        if (myChart[idx] && myChart[idx].dispose) {
            myChart[idx].dispose();
        }
        myChart[idx] = echarts.init(domMain[idx]);
        (new Function (editor[idx].doc.getValue().replace(
            'option', 'option[' + idx + ']'))
        )()
        myChart[idx].setOption(option[idx], true);
        domMessage[idx].innerHTML = '';
    }

    function refreshAll() {
//        for (var i = 0, l = myChart.length; i < l; i++) {
            (new Function (editor[0].doc.getValue().replace(
                'option', 'option[' + 0 + ']'))
            )();
//        	var dto = parent.nowSearchObj;
            var dto = [];
        	var url = "/report/getReportChart";
        	if(getURLParam("type") == '0001') {
        		url = "/report/getReportChart1";
        	}
        	var obj = {};
        	var typeObj = {
        			fieldName:'filterType',
        			value:type
        	}
        	dto.push(typeObj);
        	doJsonRequest(url,dto, function(data){
        		if(data.result) {
        		    obj = data.data;
                	obj.timeline.label = {};
                	obj.timeline.label.formatter = function(s) {
                        return s.slice(0, 7);
                    }
                	myChart[0] = echarts.init(domMain[0]);
                    myChart[0].setOption(obj, true);
        		} else {
        			alert("失败")
        		}		
        	},{showWaiting:true})
//        }
    }

var developMode = false;
    if (developMode) {

        // for develop
        require.config({
            packages: [
                {
                    name: 'echarts',
                    location: '../../src',
                    main: 'echarts'
                },
                {
                    name: 'zrender',
                    //location: 'http://ecomfe.github.io/zrender/src',
                    location: '../../../zrender/src',
                    main: 'zrender'
                }
            ]
        });
    }
    else {

        // for echarts online home page
        var fileLocation = '/cmcp/static/javascript/component/echart/echarts-map';
        require.config({
            paths:{ 
                echarts: fileLocation,
                'echarts/chart/line': fileLocation,
                'echarts/chart/bar': fileLocation,
                'echarts/chart/scatter': fileLocation,
                'echarts/chart/k': fileLocation,
                'echarts/chart/pie': fileLocation,
                'echarts/chart/radar': fileLocation,
                'echarts/chart/map': fileLocation,
                'echarts/chart/chord': fileLocation,
                'echarts/chart/force': fileLocation,
                'echarts/chart/gauge': fileLocation,
                'echarts/chart/funnel': fileLocation
            }
        });
    }

// 按需加载
require(
    [
        'echarts',
        'echarts/chart/line',
        'echarts/chart/bar',
        'echarts/chart/scatter',
        'echarts/chart/k',
        'echarts/chart/pie',
        'echarts/chart/radar',
        'echarts/chart/force',
        'echarts/chart/chord',
        'echarts/chart/map',
        'echarts/chart/gauge',
        'echarts/chart/funnel'
    ],
    requireCallback
);

var echarts;
var option = {};
    function requireCallback (ec) {
        echarts = ec;
        if (myChart.length > 0) {
            for (var i = 0, l = myChart.length; i < l; i++) {
                myChart[i].dispose && myChart[i].dispose();
            }
        }
       
        myChart = [];
        for (var i = 0, l = domMain.length; i < l; i++) {
           
//            myChart[i] = echarts.init(domMain[i]);

        }
        
        setTimeout(function(){
        	 refreshAll();
        },1000)
       
        
        window.onresize = function (){
            for (var i = 0, l = myChart.length; i < l; i++) {
                myChart[i].resize && myChart[i].resize();
            }
        };
    }
    
function doChange(type){
	window.location.href="supervise_approval.html?filterType="+type;
}