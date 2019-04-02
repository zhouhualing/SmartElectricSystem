/*--------全局变量--------*/
var Curvenum = 0;//曲线数量
var CurveRepeat = 0;
var editOradd = 0;
var addShowTimingSetJudge = 0 ;//控制定时界面添加按钮
//曲线数量
var rairconSetting = "";//用来设定曲线是修改该是添加新的
var CurveObj = {};
var repaedMap = new Array();
var windspeeds = ["自动","风速一","风速二","风速三","风速四","风速五","静音"];
var modes = ["自动","送风","制冷","制热","除湿"];
/*------空调定时设置-------*/
function rairconTSet(id){
	/*--加载定时设置内容--*/
	doJsonRequest("/rairconSet/QueryTimingSet", {deviceId:id}, function (data) {
		timingSetObj = data.data;
		ShowTimingSet(timingSetObj);
	})
}
function showcharts(){
	$(".content").hide();
	$(".Raircon").hide();
	$(".contentInfo").hide();
	$(".content").hide();
	$(".contentInfoToSetting").hide(); 
	$('#showTimingSetDiv').hide();
	$('#charts').show();
}
 function showInfo2(that) {
     $("a").removeClass("hover");
     $(that).addClass("hover");
     $(".contentInfoToSetting").show();
     $('#showTimingSetDiv').hide();
     $(".contentInfo").hide();
     $(".content").hide();
     $(".Raircon").hide();
     $('#charts').hide();
     clearTimeout(continueRefresh);
 }
 
 function starShow(showOrhide){
	 if(showOrhide == "show"){
		 $(".content").hide();
		 $(".Raircon").show();
		 $(".contentInfo").hide();
		 $(".content").hide();
		 $('#charts').hide();
		$('#showTimingSetDiv').hide();
	     $(".contentInfoToSetting").hide();
	 }
	 if(showOrhide == "hide"){
		 $(".Raircon").hide();
		 $(".contentInfo").hide();
	     $(".content").show();
	     $('#charts').hide();
	     $('#showTimingSetDiv').hide();
	     $(".contentInfoToSetting").hide();
	 }
 }
 function starShowSetting(showOrhide){
 	$("a").removeClass("hover");
	$(".index").addClass("hover");
	 if(showOrhide == "show"){
		 $(".content").hide();
		 $(".Raircon").hide();
		 $(".contentInfo").hide();
		 $(".content").hide();
		 $('#container').hide();
		 $('#showTimingSetDiv').hide();
	     $(".contentInfoToSetting").hide();
	     $('#charts').show();
	 }
	 if(showOrhide == "hide"){
		 $(".Raircon").hide();
		 $(".contentInfo").hide();
	     $(".content").show();
	     $('#showTimingSetDiv').hide();
	     $(".contentInfoToSetting").hide();
	     $('#charts').hide();
	     Curvenum = 0;
	 }
	 QueryCurveNumForCurveDevice(EYONDeviceId);
}
 function QueryCurveNumForCurveDevice(id){
	 if(id == "all")return;
	 /*--加载曲线数量有和没有图标显示不同--*/
	var cnum = 0;
	doJsonRequest("/rairconSet/QueryCurveNumForCurveDevice", {deviceId:id,minTemps:minTemps,maxTemps:maxTemps}, function (nums) {//获取曲线数量
		cnum = parseInt(nums.data);
	},{showWaiting : true,async : false})
	var dk ;
	if(cnum == 0){//没有显示白色图标
		$('.content_kt[id='+id+']').children().children().children('span:last-child').children().eq(1).attr('class','dk');
	}else{//有则显示红色图标
		$('.content_kt[id='+id+']').children().children().children('span:last-child').children().eq(1).attr('class','dk2');
	}
 }
var clocking = new Array();
var temp = new Array();

/*-----------加载单条曲线数据-------------*/
function lo(Curveids){
	doJsonRequest("/rairconSet/QueryCurveSetting", {Curveid:Curveids}, function (data) {
		var obj = data.data;
		clocking = new Array();
		temp = new Array();
		for(var j = 0 ; j < obj.length ; j++){
			clocking[j] =  obj[j].on_off == 0  ?  obj[j].clocking+',待机'  :  obj[j].clocking;
			var r = {
                y: 25,
                marker: {
					fillColor: 'red',
					lineColor: 'red'
                    //symbol: 'url(/demo/img/sun.png)'
                }
            }
			temp[j] =  (     obj[j].on_off == 0  ?   r   :   parseInt( (   (obj[j].temp == null || obj[j].temp == "") ? 0 : obj[j].temp   ) )   )      ;
		}
		showCh();
		showcharts();
		/*----------加载重复设置----------*/
		loadRepeat(Curveids);
	});
}
/**
	加载重复设置
*/
function loadRepeat(Curveids){
	doJsonRequest("/rairconSet/QueryCurveSettingRepeat", {CurveId:Curveids,deviceId:EYONDeviceId}, function (data) {
			var obj = data.data;
			var st = "";
			if(obj != null && obj[0] != null){
				var o = obj[0];
				st = st + ((o.monday!=null && o.monday != 0) ? "周一" : "");
				st = st + ((o.tuesday!=null && o.tuesday != 0 ) ? "周二" : "");
				st = st + ((o.wednesday!=null && o.wednesday != 0 ) ? "周三" : "");
				st = st + ((o.thursday!=null && o.thursday != 0 ) ? "周四" : "");
				st = st + ((o.friday!=null && o.friday != 0 ) ? "周五" : "");
				st = st + ((o.saturday!=null && o.saturday != 0 ) ? "周六" : "");
				st = st + ((o.sunday!=null && o.sunday != 0 ) ? "周日" : "");
				$(".repeats").html("");
			}
			if("" == st)st="无";
			$(".repeats").html("");
			$(".repeats").append("应用日期："+st);
		});
}
function showCh(){
	$('#container').highcharts({
	 	chart: {
            	backgroundColor: {
                	linearGradient: [0, 0, 500, 500],
                stops: [
                    [0, 'rgb(105, 105, 105)'],
                    [1, 'rgb(59, 52, 46)']
                ]
            },
            legend: {
	            layout: 'vertical',
	            backgroundColor: 'none',
	            floating: true,
	            align: 'left',
	            x: 100,
	            verticalAlign: 'top',
	            y: 70
        	}
            
        },
        title: {
        	style:{
        			color: '#ffffff',
        			fontWeigth:900,
					fontSize: '16px'
        	},
        	text: '舒睡曲线设置'+(num+1)+'',
        },
        xAxis: {
        	categories: clocking,
        	labels:{
            	style: {
					color: '#ffffff'//刻度颜色
				}
            }
            
        },
        yAxis: {
        	allowDecimals:false,
        	title: {
        		text: '温度(℃)',
        		color:'#ffffff'
        	},
        	labels:{
            	style: {
					color: '#ffffff'//刻度颜色
				}
            }
        	
        },
      legend:{
		enabled:false
		},
      series: [{
            data: temp,
            step: false,//left,right,center,true,默认false
            name: '温度'
        }]
    });
    $('#charts').hide();
}
/*------判断曲线是否存在-------*/
function JudgeCurveExist(){
	if(CurveObj[num] == null){ 
		alertFM("当前设备没有曲线，请添加一条",true);
		$('#container').empty();
		$('#container').append("<p>当前设备没有舒睡曲线相关设置，请添加一条</p>");
		showcharts();
		//addNewCurve();
		return true;
	}
}
/*--------曲线图翻页--------*/
function nextPage(lor){
	if(JudgeCurveExist()){return;}
	if(lor == "l"){
		if(num == 0){//调整曲线数量 - 
			num = Curvenum-1;
		}else{
			num = num-1;	
		}
		loadCurveData();
		lo(CurveObj[num].id);
	}else{
		if(num >= Curvenum-1){//调整曲线数量 + 
			num = 0;
		}else{
			num = num+1;	
		}
		loadCurveData();
		lo(CurveObj[num].id);
	}	
	
}

/*------------修改单个曲线------------*/
function edit(){
	editOradd = 1;
	hmBlockUI();
	if(JudgeCurveExist()){return;};
	rairconSetting = CurveObj[num].id;
	$('#charts').hide();
	$("#chartstableDiv").show();
	doJsonRequest("/rairconSet/QueryCurveSetting", {Curveid:CurveObj[num].id}, function (data) {
		$(".chartstable tr").remove();   
		var obj = data.data;
		var tr = "<tr>"+
        		"<td><font style=\"color:white;\">时间</font></td>"+
        		"<td><font style=\"color:white;\">开关</font></td>"+
        		"<td><font style=\"color:white;\">温度</font></td>"+
        		"<td><font style=\"color:white;\">模式</font></td>"+
        		"<td><font style=\"color:white;\">风速</font></td>"+
        		"<td><font style=\"color:white;\">状态</font></td>"+
        		"<td><font style=\"color:white;\">操作</font></td>"+
        	"</tr>";
		for(var j = 0 ; j < obj.length ; j++){
			var disabled = (  (obj[j].on_off == 0) ?  "disabled='disabled'" : '' );
			tr = tr + "<tr>";	
			//时钟
			tr = tr + "<td>";
			tr = tr + "<input class=\"time-picker\" size=\"7\" readonly=\"readonly\" name=\"clocking\"  type=\"text\" value=\""+obj[j].clocking+"\"/>";
			tr = tr + "</td>";
			//开关
			tr = tr + "<td>";
			tr = tr + "<select name=\"on_off\" onchange=\"onOffForCurveSave(this)\">"+
							((obj[j].on_off == 1) ? 
							"<option value =\"1\">开</option><option value =\"0\">待机</option>" :
							 
							"<option value =\"0\">待机</option><option value =\"1\">开</option>")+
					"</select>";
			tr = tr + "</td>";
			//温度
			tr = tr + "<td>";
			tr = tr + "<input "+disabled+" size=\"7\" name=\"temp\" type=\"text\" value=\""+((obj[j].temp == null || obj[j].temp == "") ? 0 : obj[j].temp)+"\"/>";
			tr = tr + "</td>";
			//模式
			tr = tr + "<td>";
			tr = tr + "<select name=\"mode\" "+disabled+" >"+
			"<option value =\""+obj[j].mode+"\">"+modes[parseInt(obj[j].mode)]+"</option>";
			for(var wi = 0 ; wi < modes.length ; wi++){
				if(wi != parseInt(obj[j].mode))
				tr = tr + "<option value =\""+wi+"\">"+modes[wi]+"</option>";
			}
			tr = tr + "</select>";
			tr = tr + "</td>";
			//风速
			tr = tr + "<td>";
			//tr = tr + "<font>"+windspeeds[parseInt(obj[j].windspeed)]+"</font>";
			tr = tr + "<select name=\"windspeed\" "+disabled+" >"+
			"<option value =\""+obj[j].windspeed+"\">"+windspeeds[parseInt(obj[j].windspeed)]+"</option>";
			for(var wi = 0 ; wi < windspeeds.length ; wi++){
				if(wi != parseInt(obj[j].windspeed))
				tr = tr + "<option value =\""+wi+"\">"+windspeeds[wi]+"</option>";
			}
			tr = tr + "</select>";
			tr = tr + "</td>";
			//定时计划开启或关闭
			var se = new Array('关闭定时','开启定时');
			tr = tr + "<td>";
			
			
			tr = tr + "<select name=\"startend\"\">"+
							((obj[j].startend == 1) ? 
							"<option value =\"1\">开启定时</option><option value =\"0\">关闭定时</option>" :
							 
							"<option value =\"0\">关闭定时</option><option value =\"1\">开启定时</option>")+
					"</select>";			
						
						
			tr = tr + "</td>";
			//操作
			tr = tr + "<td>";
			//tr = tr + "<input  onclick=\"delClocking('"+obj[j].id+"')\" type=\"submit\" value=\"删除\"/>";
			tr = tr + "<input  onclick=\"delRow(this)\" type=\"submit\" value=\"删除\"/>";
			tr = tr + "</td>";
			tr = tr + "</tr>";
			rairconSetting = CurveObj[num].id;
		}
		$(".chartstable").append(tr); 
		castDatetimepicker();
	},{showWaiting:true});
	  	
}

/*------------添加新曲线------------*/
function addNewCurve(){
	hmBlockUI();
	rairconSetting = CurveId;
	$('#charts').hide();
	$("#chartstableDiv").show();
	var tr = "";
	$(".chartstable tr").remove(); 
		tr = tr + "<tr>"+
        		"<td><font style=\"color:#000000;\">时间</font></td>"+
        		"<td><font style=\"color:#000000;\">开关</font></td>"+
        		"<td><font style=\"color:#000000;\">温度</font></td>"+
        		"<td><font style=\"color:#000000;\">模式</font></td>"+
        		"<td><font style=\"color:#000000;\">风速</font></td>"+
        		"<td><font style=\"color:#000000;\">定时操作</font></td>"+
        		"<td><font style=\"color:#000000;\">操作</font></td>"+
        	"</tr>";
		tr = tr + "<TR>";
		tr = tr + "<TD>";
		tr = tr + "<input name=\"clocking\" class=\"time-picker\" readonly=\"readonly\" size=\"7\" class=\"clocking\" type=\"text\" value=\"\"/>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";
		tr = tr + "<select name=\"on_off\" class=\"on_off\" onchange=\"onOffForCurveSave(this)\">"+
					  "<option value =\"1\">开</option>"+
					  "<option value =\"0\">待机</option>"+
					"</select>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";
		tr = tr + "<input name=\"temp\" size=\"7\" type=\"text\" class='temp'  value=\"\"/>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";//制冷:1，制热:2，送风:3，除湿:4，自动:0
		tr = tr + "<select id=\"mode\" name=\"mode\">"+
					  "<option value =\"0\">"+modes[0]+"</option>"+
					  "<option value =\"1\">"+modes[1]+"</option>"+
					  "<option value =\"2\">"+modes[2]+"</option>"+
					  "<option value =\"3\">"+modes[3]+"</option>"+
					  "<option value =\"4\">"+modes[4]+"</option>"+
					"</select>";			
		tr = tr + "</TD>";
		tr = tr + "<TD>";
		tr = tr + "<select name=\"windspeed\" class='windspeed'>"+
					  "<option value =\"0\">"+windspeeds[0]+"</option>"+
					  "<option value =\"1\">"+windspeeds[1]+"</option>"+
					  "<option value =\"2\">"+windspeeds[2]+"</option>"+
					  "<option value =\"3\">"+windspeeds[3]+"</option>"+
					  "<option value =\"4\">"+windspeeds[4]+"</option>"+
					  "<option value =\"5\">"+windspeeds[5]+"</option>"+
					  "<option value =\"6\">"+windspeeds[6]+"</option>"+
					"</select>";
		tr = tr + "</TD>";
		//定时计划开启或关闭
			tr = tr + "<td>";
			tr = tr + "<select name=\"startend\">"+
						"<option value =\"1\">开启定时</option>"+
						"<option value =\"0\">关闭定时</option>"+
						"</select>";
			tr = tr + "</td>";
		//操作
		tr = tr + "<td>";
		tr = tr + "<input  onclick=\"delRow(this)\" type=\"submit\" value=\"删除\"/>";
		tr = tr + "</td>";
		
		tr = tr + "</TR>";
		rairconSetting = "";
	$(".chartstable").append(tr); 
	castDatetimepicker();
}

/*------删除当前行-------*/
function delRow(obj){
	$(obj).parent().parent().remove(); 
	
	/*var len=$("chartstable tr").length;
	if(len>1){
		$("tr[id='"+(len-1)+"']").remove();  
	}*/
}

/*------------保存新曲线------------*/
function saveNewCurve(){
	var min = 0;
	var max = 0;
	
	confirmFM("是否保存?",function(){
		var Clocksetting = new Array();//空调时钟设置对象数组
		var t=true;
		var flag=true;
		$('.chartstable tr:not(:first)').each(function(){
			var tds = $(this).children();
			if(tds.length < 7) return;
			var clocksetting = {}
			for(var i = 0 ; i < tds.length ; i++){
			if(tds[i].childNodes[0].value==""){
				flag=false;
			}
				if(tds[i].childNodes[0].name == 'clocking'){
					clocksetting.clocking = tds[i].childNodes[0].value;continue;
				}
				if(tds[i].childNodes[0].name == 'on_off'){
					clocksetting.on_off = tds[i].childNodes[0].value;continue;
				}
				
				if(tds[i].childNodes[0].name == 'temp'){
					if(isNaN(tds[i].childNodes[0].value)||parseInt(tds[i].childNodes[0].value) < parseInt(minTemps)   ||  parseInt(tds[i].childNodes[0].value)   >  parseInt(maxTemps)  ){
						if( EYONDeviceId == "all" ){
							if( clocksetting.on_off != "0" ){
								if( isNaN(tds[i].childNodes[0].value) || parseInt(tds[i].childNodes[0].value) < 15  ||  parseInt(tds[i].childNodes[0].value)   >  35   ){
									min = 15;
									max = 35;
									t=false;
								}
							}
						}else{
							if( clocksetting.on_off != "0" ){
								min = parseInt(minTemps);
								max = parseInt(maxTemps);
								t=false;	
							}
						}
					}
					clocksetting.temp = tds[i].childNodes[0].value;continue;
				}
				if(tds[i].childNodes[0].name == 'windspeed'){
					clocksetting.windspeed = tds[i].childNodes[0].value;continue;
				}
				if(tds[i].childNodes[0].name == 'mode'){
					clocksetting.mode = tds[i].childNodes[0].value;continue;
				}
				if(tds[i].childNodes[0].name == 'startend'){
					clocksetting.startend = tds[i].childNodes[0].value;continue;
				}
			}
				Clocksetting.push(clocksetting);
		})
		if(!flag){
			alertFM("所有信息都是必填的请完善信息",true);
			return;
		}
		if(!t){
			alertFM("空调温度："+min+"——"+max+"!",true);
			return;
		}
		var d = "";  
		
		if( Clocksetting == null || Clocksetting.length == 0 ){ alertFM("没有节点数据，无法添加~！",true);return;}
		if( Clocksetting.length > 1 ){
			if( Clocksetting[0].clocking != null && Clocksetting[1].clocking != null && Clocksetting[0].clocking == Clocksetting[1].clocking){
				alertFM("时间不能相同~！",true);return;
			}
		}
		doJsonRequest("/rairconSet/addCurvesClocks", {Clocksetting:Clocksetting,deviceId:EYONDeviceId,rairconSetting:rairconSetting}, function (data) {
			$("#chartstableDiv").hide();
			$('#charts').show();
			d = data.data;
			
			openLineChart(EYONDeviceId);
			rairconSetting = "";
			
			hmUnBlockUI();
		},{showWaiting:true,async : false});//,async : false
	},true) 
}


/*----------删除当前曲线设置-----------*/
function delCurve(){
	if(JudgeCurveExist()){return;};
	confirmFM("是否删除当前曲线?",function(){
		doJsonRequest("/rairconSet/delCurve", {curveid:CurveObj[num].id}, function(data) {
		var d = data.data;
			if(d == "500"){
				alertFM("删除失败",true);
			}else{
				//if(num == Curvenum){
					num = 0 ;
					//Curvenum = Curvenum - 1; 
				//}else{
					//num = num + 1 ;
					//Curvenum = Curvenum - 1; 
				//}
				//刷新折线图
				doJsonRequest("/rairconSet/QueryCurve", {deviceId:EYONDeviceId,minTemps:minTemps,maxTemps:maxTemps}, function (data) {
						CurveObj = data.data;
						if(CurveObj.length < 1){
							$('#container').empty();
							$('#container').append("<p>当前设备没有舒睡曲线相关设置，请添加一条</p>");
							//showcharts();
							//addNewCurve();
						}else{
							lo(CurveObj[num].id);
						}
				},{showWaiting : true,async : false});
				/*--重新加载曲线数量--*/
				doJsonRequest("/rairconSet/QueryCurveNum", {deviceId:EYONDeviceId,minTemps:minTemps,maxTemps:maxTemps}, function (nums) {//获取曲线数量
					Curvenum = parseInt(nums.data);
				});
			}
		},{showWaiting:true});
	},true)
}
/*---------添加折线图的点------------*/
function addClocking(){
	var tr = tr + "<TR>";
		tr = tr + "<TD>";
		tr = tr + "<input size=\"7\" name=\"clocking\" class=\"time-picker\" readonly=\"readonly\" type=\"text\" value=\"\"/>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";
		tr = tr + "<select name=\"on_off\" onchange=\"onOffForCurveSave(this)\">"+
						"<option value =\"1\">开</option>"+
						"<option value =\"0\">待机</option>"+
					"</select>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";
		tr = tr + "<input size=\"7\" name=\"temp\" type=\"text\" value=\"\"/>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";//制冷:1，制热:2，送风:3，除湿:4，自动:0
		tr = tr + "<select name=\"mode\">"+
					  "<option value =\"0\">"+modes[0]+"</option>"+
					  "<option value =\"1\">"+modes[1]+"</option>"+
					  "<option value =\"2\">"+modes[2]+"</option>"+
					  "<option value =\"3\">"+modes[3]+"</option>"+
					  "<option value =\"4\">"+modes[4]+"</option>"+
					"</select>";
		tr = tr + "</TD>";
		tr = tr + "<TD>";
		tr = tr + "<select name=\"windspeed\" class='windspeed'>"+
					  "<option value =\"0\">"+windspeeds[0]+"</option>"+
					  "<option value =\"1\">"+windspeeds[1]+"</option>"+
					  "<option value =\"2\">"+windspeeds[2]+"</option>"+
					  "<option value =\"3\">"+windspeeds[3]+"</option>"+
					  "<option value =\"4\">"+windspeeds[4]+"</option>"+
					  "<option value =\"5\">"+windspeeds[5]+"</option>"+
					  "<option value =\"6\">"+windspeeds[6]+"</option>"+
			"</select>";

		tr = tr + "</TD>";
		//定时计划开启或关闭
			tr = tr + "<td>";
			tr = tr + "<select name=\"startend\">"+
						"<option value =\"1\">开启定时</option>"+
						"<option value =\"0\">关闭定时</option>"+
						"</select>";
			tr = tr + "</td>";
		//操作
		tr = tr + "<td>";
		tr = tr + "<input  onclick=\"delRow(this)\" type=\"submit\" value=\"删除\"/>";
		tr = tr + "</td>";	
		tr = tr + "</TR>";
	$(".chartstable").append(tr); 
	castDatetimepicker();
}

/*---------添加折线图的点保存------------*/
function addClockingToSave(){
	var Clocksetting = {
		temp:$('#temp').val(),
		clocking:$('#clocking').val(),
		on_off:$('#on_off').val(),
		windspeed:$('#windspeed').val(),
		mode:$('#mode').val(),
		rairconSetting:CurveObj[num]
	}
	var d = "";
	doJsonRequest("/rairconSet/addCurveSetting", Clocksetting, function (data) {
		d = data.data;
	},{showWaiting:true});
	$("#chartstableDiv").hide();
	$('#charts').show();
}
/*---------删除折线图的点-----------*/
function delClocking(id){
	var d = "";
	confirmFM("确认删除?",function(){
		doJsonRequest("/rairconSet/delCurveSetting", {clocksettingid:id}, function (data) {
			d = data.data;
		},{showWaiting:true});
		$("#chartstableDiv").hide();
		$('#charts').show();
	},true)
}

//关闭编辑窗口
function closeEdit(){
	//if( confirm("是否保存?") ){
		//saveNewCurve();
	//}else{
	editOradd = 0;
	hmUnBlockUI();
		$('#chartstableDiv').hide();
		$('#charts').show();
		rairconSetting = "";
	//}
}
//关闭定时设置
function closeShowTimingSet(){
	addShowTimingSetJudge = 0;
	$('#showTimingSetDiv').hide();
	$(".content").show();
	//刷新图标
	QueryClockNumForRedImg(EYONDeviceId);
}
function QueryClockNumForRedImg(id){
	if(id=="all")return;
	/*--加载定时设置数量有和没有图标显示不同--*/
	var cnumForClocking = 0;
	doJsonRequest("/rairconSet/QueryClockNum", {deviceId:id}, function (nums) {//获取定时数量
		cnumForClocking = parseInt(nums.data);
	},{showWaiting : true,async : false})
	if(cnumForClocking == 0){
		//白
		$('.content_kt[id='+id+']').children().children().children('span:last-child').children('a:last-child').attr('class','wd');
	}else{
		//红
		$('.content_kt[id='+id+']').children().children().children('span:last-child').children('a:last-child').attr('class','wd2');
	}
	rairconSetting = "";
}



//加载空调定时设置内容
function ShowTimingSet(obj){
	var weekName = ['周一','周二','周三','周四','周五','周六','周日'];
	$('#charts').hide();
	$(".showTimingSetTable tr").remove(); 
	var tr = "<tr align=\"center\">"+
        		"<th width=\"110px\"><font style=\"color:#ffffff;\">时间</font></th>"+
        		"<th width=\"110px\"><font style=\"color:#ffffff;\">空调开关</font></th>"+
        		"<th width=\"110px\"><font style=\"color:#ffffff;\">温度</font></th>"+
        		"<th width=\"110px\"><font style=\"color:#ffffff;\">模式</font></th>"+
        		"<th width=\"110px\"><font style=\"color:#ffffff;\">风速</font></th>"+
        		"<th width=\"110px\"><font style=\"color:#ffffff;\">定时状态</font></th>"+
        		"<th width=\"300px\"><font style=\"color:#ffffff;\">重复</font></th>"+
        		"<th width=\"150px\"><font style=\"color:#ffffff;\">操作</font></th>"+
        	"</tr>";
		for(var j = 0 ; j < obj.length ; j++){
			var disabled = (  (obj[j].on_off == 0) ?  "disabled='disabled'" : '' );
			tr = tr + "<tr>";	
			//时钟
			tr = tr + "<td style=\"width:110px\">";
			tr = tr + "<font>"+obj[j].clocking+"</font>";
			tr = tr + "<input size=\"5\" name=\"clocking\" class=\"time-picker\" readonly=\"readonly\" type=\"text\" style=\"display:none;\"  value=\""+obj[j].clocking+"\"/>";
			tr = tr + "<input size=\"5\" name=\"clockSetId\"   type=\"hidden\"   value=\""+obj[j].id+"\"/>";
			tr = tr + "</td>";
			//开关
			tr = tr + "<td style=\"width:110px\">";
			tr = tr + "<font>"+(  (obj[j].on_off == 1) ? "开" : "待机"  )+"</font>"+
			"<select name=\"on_off\" style=\"display:none\" onchange=\"onOffForCurveSave(this)\">"+
			((obj[j].on_off == 1) ? 
			"<option value=\"1\">开</option>"+
			"<option value=\"0\">待机</option>"
			:
			"<option value=\"0\">待机</option>"+
			"<option value=\"1\">开</option>")+
			"</select>";
			tr = tr + "</td>";
			//温度
			tr = tr + "<td style=\"width:110px\">";
			tr = tr + "<font>"+((obj[j].temp == null || obj[j].temp == "") ? 0 : obj[j].temp)+"</font>";
			tr = tr + "<input "+disabled+" size=\"7\" name=\"temp\"  type=\"text\" style=\"display:none;\"  value=\""+obj[j].temp+"\"/>";
			tr = tr + "</td>";
			//模式
			var seStr = "";
			var seStrF = "";
			for(var mi = 0 ; mi < modes.length ; mi++){
				if(mi != parseInt(obj[j].mode)){
					seStr = seStr + "<option value=\""+mi+"\">"+modes[mi]+"</option>";
				}else{
					seStrF = "<option value=\""+mi+"\">"+modes[mi]+"</option>";
				}
			}
			seStr = seStrF + seStr;
			tr = tr + "<td style=\"width:110px\">";
			tr = tr + "<font>"+modes[parseInt(obj[j].mode)]+"</font>";
			//tr = tr + "<input size=\"7\" name=\"mode\" style=\"display:none;\"  value=\""+obj[j].mode+"\"/>";
			tr = tr + "<select name=\"mode\" "+disabled+" style=\"display:none;\">"+ seStr + "</select>";
			tr = tr + "</td>";
			//风速
			tr = tr + "<td style=\"width:110px\">";
			tr = tr + "<font>"+windspeeds[parseInt(obj[j].windspeed)]+"</font>";
			tr = tr + "<select "+disabled+" name=\"windspeed\" style=\"display:none;\">"+
			"<option value =\""+obj[j].windspeed+"\">"+windspeeds[parseInt(obj[j].windspeed)]+"</option>";
			for(var wi = 0 ; wi < windspeeds.length ; wi++){
				if(wi != parseInt(obj[j].windspeed))
				tr = tr + "<option value =\""+wi+"\">"+windspeeds[wi]+"</option>";
			}
			tr = tr + "</select>";
			tr = tr + "</td>";
			//定时计划开启或关闭
			var se = new Array('关闭定时','开启定时');
			tr = tr + "<td><font>"+
			se[obj[j].startend]+"</font>"+
			"<select name=\"startend\" style=\"display:none\">"+
			(
				(obj[j].startend == 1) ?
				"<option value=\"1\">开启定时</option>"+
				"<option value=\"0\">关闭定时</option>"
				:
				"<option value=\"0\">关闭定时</option>"+
				"<option value=\"1\">开启定时</option>"
			)
			"</select>";
			tr = tr + "</td>";
			//重复状况
			tr = tr + "<td style=\"width:275px\" nowrap=\"nowrap\" id=\"repeat"+j+"\">"+
			"   <font "+( obj[j].monday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\"> 周一 </font>  "+
			"<input size=\"7\" name=\"monday\"  onclick=\"checking(this)\"  type=\"checkbox\" style=\"display:none;\" value=\""+obj[j].monday+"\"    "+( obj[j].monday == "1" ? " checked=\"true\" " : "")+"  />"+//星期一
			"  <font "+( obj[j].tuesday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\">,周二 </font>   "+
			"<input size=\"7\" name=\"tuesday\"  onclick=\"checking(this)\" type=\"checkbox\" style=\"display:none;\" value=\""+obj[j].tuesday+"\" "+( obj[j].tuesday == "1" ? " checked=\"true\" " : "")+" />"+//星期二
			"   <font "+( obj[j].wednesday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\">,周三 </font>   "+
			"<input size=\"7\" name=\"wednesday\"  onclick=\"checking(this)\" type=\"checkbox\" style=\"display:none;\" value=\""+obj[j].wednesday+"\" "+( obj[j].wednesday == "1" ? " checked=\"true\" " : "")+" />"+//星期三
			" <font "+( obj[j].thursday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\">,周四</font>  "+
			"<input size=\"7\" name=\"thursday\"  onclick=\"checking(this)\" type=\"checkbox\" style=\"display:none;\" value=\""+obj[j].thursday+"\" "+( obj[j].thursday == "1" ? " checked=\"true\" " : "")+" />"+//星期四
			"   <font "+( obj[j].friday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\">,周五 </font>    "+
			"<input size=\"7\" name=\"friday\"  onclick=\"checking(this)\" type=\"checkbox\" style=\"display:none;\" value=\""+obj[j].friday+"\" "+( obj[j].friday == "1" ? " checked=\"true\" " : "")+" />"+//星期五
			"<font "+( obj[j].saturday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\">,周六 </font>   "+
			"<input size=\"7\" name=\"saturday\"  onclick=\"checking(this)\" type=\"checkbox\" style=\"display:none;\" value=\""+obj[j].saturday+"\" "+( obj[j].saturday == "1" ? " checked=\"true\" " : "")+" />"+//星期六
			"   <font "+( obj[j].sunday == "0" ? "style=\"display:none;\"" : "")+"  color=\"#ffffff\">,周日 </font>  "+
			"<input size=\"7\" name=\"sunday\" style=\"display:none;\" onclick=\"checking(this)\" type=\"checkbox\" value=\""+obj[j].sunday+"\"  "+( obj[j].sunday == "1" ? " checked=\"true\" " : "")+" />";//星期日
			tr = tr + "</td>";
			//操作
			tr = tr + "<td style=\"width:150px\">";
			tr = tr + "<input class=\"btn\" onclick=\"editTimingSet(this.parentNode.parentNode,"+j+",'"+obj[j].id+"')\" type=\"submit\" value=\"编辑\"/>";
			tr = tr + "<input class=\"btn\"  onclick=\"delTR(this,'"+obj[j].id+"')\" type=\"submit\" value=\"删除\"/>";
			
			tr = tr + "<input class=\"btn\" style=\"display:none;\" onclick=\"saveClockSet(this.parentNode.parentNode,"+j+",'"+obj[j].id+"')\" type=\"submit\" value=\"保存\"/>";
			tr = tr + "<input class=\"btn\" style=\"display:none;\" onclick=\"cancelClockSetAlone(this.parentNode.parentNode,"+j+",'"+obj[j].id+"')\" type=\"submit\" value=\"取消\"/>";
			tr = tr + "</td>";
			tr = tr + "</tr>";
	}
	$(".showTimingSetTable").append(tr); 
	$(".content").hide();
	$(".Raircon").hide();
	$(".contentInfo").hide();
	$(".content").hide();
	$(".contentInfoToSetting").hide();
	$("#showTimingSetDiv").show();
	castDatetimepicker();
}
/*-------saveClockSetAlone------------*/
function saveClockSetAlone(obj,id){
	var i = 0;
	$(obj).children().each( function(){
		i++;
		if(i < 7){
			$(this).children().eq(0).show();
			$(this).children().eq(1).hide();
		}
		if(i == 7){
			$(this).children().eq(1).hide();
			$(this).children().eq(3).hide();
			$(this).children().eq(5).hide();
			$(this).children().eq(7).hide();
			$(this).children().eq(9).hide();
			$(this).children().eq(11).hide();
			$(this).children().eq(13).hide();
		}
		if(i == 8){
			$(this).children().eq(0).show();
			$(this).children().eq(1).show();
			$(this).children().eq(2).hide();
			$(this).children().eq(3).hide();
		}
	} );
}
/*-------cancelClockSetAlone------------*/
function cancelClockSetAlone(obj,j,id){
	var i = 0;
	$(obj).children().each( function(){
		i++;
		if(i < 7){
			$(this).children().eq(0).show();
			$(this).children().eq(1).hide();
		}
		if(i == 7){
			$(this).children().eq(1).hide();
			$(this).children().eq(3).hide();
			$(this).children().eq(5).hide();
			$(this).children().eq(7).hide();
			$(this).children().eq(9).hide();
			$(this).children().eq(11).hide();
			$(this).children().eq(13).hide();
			if(repaedMap[j].monday != "1" ) { $(this).children().eq(0).hide();}
			if(repaedMap[j].tuesday != "1" ) { $(this).children().eq(2).hide();}
			if(repaedMap[j].wednesday != "1" ) { $(this).children().eq(4).hide();}
			if(repaedMap[j].thursday != "1" ) { $(this).children().eq(6).hide();}
			if(repaedMap[j].friday != "1"  ) { $(this).children().eq(8).hide();}
			if(repaedMap[j].saturday != "1" ) { $(this).children().eq(10).hide();}
			if(repaedMap[j].sunday != "1"  ) { $(this).children().eq(12).hide();}
			$(this).children().eq(1).val(repaedMap[j].monday);
			$(this).children().eq(3).val(repaedMap[j].tuesday);
			$(this).children().eq(5).val(repaedMap[j].wednesday);
			$(this).children().eq(7).val(repaedMap[j].thursday);
			$(this).children().eq(9).val(repaedMap[j].friday);
			$(this).children().eq(11).val(repaedMap[j].saturday);
			$(this).children().eq(13).val(repaedMap[j].sunday);
			if (repaedMap[j].monday == "1") {
				$(this).children().eq(1).prop("checked", "checked");
			} else {
				$(this).children().eq(1).prop("checked", false);
			}
			if (repaedMap[j].tuesday == "1") {
				$(this).children().eq(3).prop("checked","checked");
			} else {
				$(this).children().eq(3).prop("checked", false);
			}
			if (repaedMap[j].wednesday == "1") {
				$(this).children().eq(5).prop("checked", "checked");
			} else {
				$(this).children().eq(5).prop("checked", false);
			}
			if (repaedMap[j].thursday == "1") {
				$(this).children().eq(7).prop("checked", "checked");
			} else {
				$(this).children().eq(7).prop("checked", false);
			}
			if (repaedMap[j].friday == "1") {
				$(this).children().eq(9).prop("checked","checked");
			} else {
				$(this).children().eq(9).prop("checked", false);
			}
			if (repaedMap[j].saturday == "1") {
				$(this).children().eq(11).prop("checked", "checked");
			} else {
				$(this).children().eq(11).prop("checked", false);
			}
			if (repaedMap[j].sunday == "1") {
				$(this).children().eq(13).prop("checked", "checked");
			} else {
				$(this).children().eq(13).prop("checked", false);
			}
		}
		if(i == 8){
			$(this).children().eq(0).show();
			$(this).children().eq(1).show();
			$(this).children().eq(2).hide();
			$(this).children().eq(3).hide();
		}
	} );
}

/*---------编辑空调定时功能--------*/
function editTimingSet(obj,j,id){
	//$(obj).css("background-color","#ffffff");
	//$(obj).css("border-collapse","collapse");
	var i = 0;
	$(obj).children().each( function(){
		i++;
		if(i < 7){
			$(this).children().eq(1).show();
			$(this).children().eq(0).hide();
		}	
		if(i == 7){
			while(     typeof(  repaedMap[j]  ) == "undefined"     ){
				repaedMap.push(1);
			}
			repaedMap[j] = {
				id:id,
				monday:$(this).children().eq(1).val(),
				tuesday:$(this).children().eq(3).val(),
				wednesday:$(this).children().eq(5).val(),
				thursday:$(this).children().eq(7).val(),
				friday:$(this).children().eq(9).val(),
				saturday:$(this).children().eq(11).val(),
				sunday:$(this).children().eq(13).val()
			}
			
			$(this).children().eq(0).show();
			$(this).children().eq(1).show();
			$(this).children().eq(2).show();
			$(this).children().eq(3).show();
			$(this).children().eq(4).show();
			$(this).children().eq(5).show();
			$(this).children().eq(6).show();
			$(this).children().eq(7).show();
			$(this).children().eq(8).show();
			$(this).children().eq(9).show();
			$(this).children().eq(10).show();
			$(this).children().eq(11).show();
			$(this).children().eq(12).show();
			$(this).children().eq(13).show();
		}
		if(i == 8){
			$(this).children().eq(0).hide();
			$(this).children().eq(1).hide();
			$(this).children().eq(2).show();
			$(this).children().eq(3).show();
		}
	} );
}

/*------添加空调定时设置-------*/
function addShowTimingSet(){
	
    var TrROW = $(".showTimingSetTable tr").length;
	var weekName = ['星期一','星期二','星期三','星期四','星期五','星期六','星期日'];
	var tr = "<tr>";	
		//时钟
		tr = tr + "<td>";
		tr = tr + "<input size=\"7\"   type=\"hidden\" value=\"\"/>";
		tr = tr + "<input name=\"clocking\" size=\"7\" class=\"time-picker\" readonly=\"readonly\" id=\"clocking\" type=\"text\" value=\"\"/>";
		tr = tr + "</td>";
		//开关
		tr = tr + "<td>";
		tr = tr + "<input size=\"7\"  type=\"hidden\" value=\"\"/>";
		tr = tr + "<select name=\"on_off\" onchange=\"onOffForCurveSave(this)\">"+
						"<option value =\"1\">开</option>"+
						"<option value =\"0\">待机</option>"+
				"</select>";
		tr = tr + "</td>";
		//温度
		tr = tr + "<td>";
		tr = tr + "<input size=\"7\"  type=\"hidden\" value=\"\"/>";
		tr = tr + "<input size=\"7\" name=\"temp\" type=\"text\" value=\"\"/>";
		tr = tr + "</td>";
		//模式	
		tr = tr + "<td>";//制冷:1，制热:2，送风:3，除湿:4，自动:0
		tr = tr + "<input size=\"7\" type=\"hidden\" value=\"\"/>";
		tr = tr + "<select name=\"mode\">"+
					  "<option value =\"0\">"+modes[0]+"</option>"+
					  "<option value =\"1\">"+modes[1]+"</option>"+
					  "<option value =\"2\">"+modes[2]+"</option>"+
					  "<option value =\"3\">"+modes[3]+"</option>"+
					  "<option value =\"4\">"+modes[4]+"</option>"+
					"</select>";
		tr = tr + "</td>";
		//风速
		tr = tr + "<td>";
		tr = tr + "<input size=\"7\"  type=\"hidden\" value=\"\"/>";
		tr = tr + "<select name=\"windspeed\">"+
					"<option value =\"0\">"+windspeeds[0]+"</option>"+
					"<option value =\"1\">"+windspeeds[1]+"</option>"+
					"<option value =\"2\">"+windspeeds[2]+"</option>"+
					"<option value =\"3\">"+windspeeds[3]+"</option>"+
					"<option value =\"4\">"+windspeeds[4]+"</option>"+
					"<option value =\"5\">"+windspeeds[5]+"</option>"+
					"<option value =\"6\">"+windspeeds[6]+"</option>"+
					"</select>";
		tr = tr + "</td>";
		//定时计划开启或关闭
		tr = tr + "<td>";
		tr = tr + "<input size=\"7\"  type=\"hidden\" value=\"\"/>";
		tr = tr + "<select name=\"startend\">"+
					"<option value =\"1\">开启定时</option>"+
					"<option value =\"0\">关闭定时</option>"+
					"</select>";
		tr = tr + "</td>";
		//重复状况
		tr = tr + "<td id=\"repeat"+TrROW+"\">"+
		" <font color=\"#ffffff\"> 周一 </font> <input name=\"monday\" onclick=\"checking(this)\" type=\"checkbox\"  value=\"0\" />   "+
		" <font color=\"#ffffff\"> 周二 </font> <input name=\"tuesday\" onclick=\"checking(this)\" type=\"checkbox\" value=\"0\" />   "+
		" <font color=\"#ffffff\"> 周三 </font> <input name=\"wednesday\" onclick=\"checking(this)\" type=\"checkbox\" value=\"0\" />   "+
		" <font color=\"#ffffff\"> 周四 </font> <input name=\"thursday\" onclick=\"checking(this)\" type=\"checkbox\" value=\"0\" />  "+
		" <font color=\"#ffffff\"> 周五 </font> <input name=\"friday\" onclick=\"checking(this)\" type=\"checkbox\" value=\"0\" />   "+
		" <font color=\"#ffffff\"> 周六 </font> <input name=\"saturday\" onclick=\"checking(this)\" type=\"checkbox\" value=\"0\" />   "+
		" <font color=\"#ffffff\"> 周日 </font> <input name=\"sunday\" onclick=\"checking(this)\" type=\"checkbox\" value=\"0\" />   ";
		tr = tr + "</td>";
		//操作
		tr = tr + "<td>";
		tr = tr + "<input  onclick=\"saveClockSet(this.parentNode.parentNode,'')\" type=\"button\" value=\"保存\"/>&nbsp&nbsp";
		tr = tr + "<input  onclick=\"delTR2(this)\" type=\"button\" value=\"取消\"/>";
		tr = tr + "</td>";
		if( addShowTimingSetJudge == 0 ){
			$(".showTimingSetTable").append(tr); 	
			castDatetimepicker();
			addShowTimingSetJudge = 1;
		}
}
/*--------保存定时---------*/
function saveClockSet(obj,j,id){
	var Clocksetting = {};
	Clocksetting.id = id;
	var i = 0;
	var flag=true;
	var t=false;
	var re = false;
	$(obj).children().each( function(){
		if($(this).children().eq(1).val() =="" && i < 7){
			flag=false;
		}
		if (!flag) {
			alertFM("所有信息不可为空，请填充~！",true);
			return false;
		}
		if($(this).children().eq(1).attr("name") == "temp" &&( isNaN($(this).children().eq(1).val())||parseInt($(this).children().eq(1).val())<parseInt(parseInt(minTemps))|| parseInt($(this).children().eq(1).val())>parseInt(maxTemps))){
			t=true;
		}
		i++;
		if(i < 7){
			if( $(this).children().eq(1).attr("name") == "clocking" ){
				Clocksetting.clocking = $(this).children().eq(1).val();
			} 
			if( $(this).children().eq(1).attr("name") == "on_off" ){
				Clocksetting.on_off=$(this).children().eq(1).val();
			}
			if( $(this).children().eq(1).attr("name") == "temp" ){
				Clocksetting.temp=$(this).children().eq(1).val();
			}
			if( $(this).children().eq(1).attr("name") == "mode" ){
				Clocksetting.mode=$(this).children().eq(1).val();
			}
			if( $(this).children().eq(1).attr("name") == "windspeed" ){
				
				Clocksetting.windspeed=$(this).children().eq(1).val();
			}
			if( $(this).children().eq(1).attr("name") == "startend" ){
				Clocksetting.startend=$(this).children().eq(1).val();
			}
		}
		if(i == 7){
			Clocksetting.monday = $(this).children().eq(1).val();//星期一monday
			Clocksetting.tuesday = $(this).children().eq(3).val();//星期二tuesday
			Clocksetting.wednesday = $(this).children().eq(5).val();//星期三wednesday
			Clocksetting.thursday = $(this).children().eq(7).val();//星期四thursday
			Clocksetting.friday = $(this).children().eq(9).val();//星期五friday
			Clocksetting.saturday = $(this).children().eq(11).val();//星期六saturday
			Clocksetting.sunday = $(this).children().eq(13).val();//星期日sunday
			var r = false;
			var tdT = "";
			if(Clocksetting.monday == "1"){ r=true;}
			if(Clocksetting.tuesday == "1"){  r=true;}
			if(Clocksetting.wednesday == "1"){ r=true;}
			if(Clocksetting.thursday == "1"){r=true;}
			if(Clocksetting.friday == "1"){ r=true;}
			if(Clocksetting.saturday == "1"){ r=true;}
			if(Clocksetting.sunday == "1"){ r=true;}
			if(!r){
				alertFM("重复选项为必填项，请选择一个重复日期~！",true);
				re = true;
				return false;
				
			}
		}
	} );
	if(re){
		return;
	}
	if (t) {
		if(Clocksetting.on_off != 0 && "0" != Clocksetting.on_off){
			alertFM("空调温度："+parseInt(minTemps)+"——"+parseInt(maxTemps)+"!",true);
			return false;
		}
	}	
	
	if(!flag){
		return false;
	}
	var weeks = {
		monday:Clocksetting.monday,
		tuesday:Clocksetting.tuesday,
		wednesday:Clocksetting.wednesday,
		thursday:Clocksetting.thursday,
		friday:Clocksetting.friday,
		saturday:Clocksetting.saturday,
		sunday:Clocksetting.sunday
	};
	//TODO   重复判断
	doJsonRequest("/rairconSet/queryCurveRepeatToTimingSet", {deviceId:EYONDeviceId,clocking:Clocksetting.clocking,clocksettingId:Clocksetting.id,week:weeks}, function(data) {
		var da = data.data;
		if(!da.result){
			confirmFM("有相同设置，是否继续?",function(){
				var d = "";  
				doJsonRequest("/rairconSet/UpdateShowTimingSet", {Clocksetting:Clocksetting,clocking:Clocksetting.clocking,spmsDevice:EYONDeviceId,week:weeks}, function (data) {
					$("#chartstableDiv").hide();
					d = data.data;
					//隐藏和显示 
					$(obj).children().each( function(){
						i++;
						if(i < 7){
							if( $(this).children().eq(1).attr("name") == "clocking" ){
								$(this).children().append(Clocksetting.clocking);
							} 
							if( $(this).children().eq(1).attr("name") == "on_off" ){
								$(this).children().append(Clocksetting.on_off == 1 ? "开":"待机");
							}
							if( $(this).children().eq(1).attr("name") == "temp" ){
								$(this).children().append(Clocksetting.temp);
							}
							if( $(this).children().eq(1).attr("name") == "mode" ){
								$(this).children().append(modes[parseInt(Clocksetting.mode)]);
							}
							if( $(this).children().eq(1).attr("name") == "windspeed" ){
								$(this).children().append( windspeeds[parseInt(Clocksetting.windspeed)] );
							}
							if( $(this).children().eq(1).attr("name") == "startend" ){
								
								$(this).children().append(Clocksetting.startend == 1 ? "开启定时":"关闭关闭");
							}
						}
						if(i == 7){
							var r = false;
							var tdT = "";
							if(Clocksetting.monday == "1"){ $(this).children().eq(0).show();r=true;}
							if(Clocksetting.tuesday == "1"){ $(this).children().eq(2).show(); r=true;}
							if(Clocksetting.wednesday == "1"){ $(this).children().eq(4).show(); r=true;}
							if(Clocksetting.thursday == "1"){ $(this).children().eq(6).show(); r=true;}
							if(Clocksetting.friday == "1"){ $(this).children().eq(8).show(); r=true;}
							if(Clocksetting.saturday == "1"){ $(this).children().eq(10).show(); r=true;}
							if(Clocksetting.sunday == "1"){ $(this).children().eq(12).show(); r=true;}
							for(var ii = 0 ; ii < 14 ;ii++){
								$(this).children().eq(ii).hide();
							}
						}
						if(i == 8){
							$(this).children().eq(0).show();
							$(this).children().eq(1).show();
							$(this).children().eq(2).hide();
							$(this).children().eq(3).hide();
						}
						addShowTimingSetJudge = 0;
					} );
					doJsonRequest("/rairconSet/QueryTimingSet", {deviceId:EYONDeviceId}, function (data) {
						timingSetObj = data.data;
						ShowTimingSet(timingSetObj);
					})
				},{showWaiting:true});
			},true)
			/*else{
				cancelClockSetAlone(obj,j,id);
			}*/
		}else{
			var d = "";  
				doJsonRequest("/rairconSet/UpdateShowTimingSet", {Clocksetting:Clocksetting,clocking:Clocksetting.clocking,spmsDevice:EYONDeviceId,week:weeks}, function (data) {
					$("#chartstableDiv").hide();
					d = data.data;
					//隐藏和显示 
					$(obj).children().each( function(){
						i++;
						if(i < 7){
							if( $(this).children().eq(1).attr("name") == "clocking" ){
								
								$(this).children().append(Clocksetting.clocking);
							} 
							if( $(this).children().eq(1).attr("name") == "on_off" ){
								
								$(this).children().append(Clocksetting.on_off == 1 ? "开":"待机");
							}
							if( $(this).children().eq(1).attr("name") == "temp" ){
								
								$(this).children().append(Clocksetting.temp);
							}
							if( $(this).children().eq(1).attr("name") == "mode" ){
								
								$(this).children().append(modes[parseInt(Clocksetting.mode)]);
							}
							if( $(this).children().eq(1).attr("name") == "windspeed" ){
								
								
								$(this).children().append( windspeeds[parseInt(Clocksetting.windspeed)] );
							}
							if( $(this).children().eq(1).attr("name") == "startend" ){
								
								$(this).children().append(Clocksetting.startend == 1 ? "开启定时":"关闭关闭");
							}
						}
						if(i == 7){
							var r = false;
							var tdT = "";
							if(Clocksetting.monday == "1"){ $(this).children().eq(0).show();r=true;}
							if(Clocksetting.tuesday == "1"){ $(this).children().eq(2).show(); r=true;}
							if(Clocksetting.wednesday == "1"){ $(this).children().eq(4).show(); r=true;}
							if(Clocksetting.thursday == "1"){ $(this).children().eq(6).show(); r=true;}
							if(Clocksetting.friday == "1"){ $(this).children().eq(8).show(); r=true;}
							if(Clocksetting.saturday == "1"){ $(this).children().eq(10).show(); r=true;}
							if(Clocksetting.sunday == "1"){ $(this).children().eq(12).show(); r=true;}
							for(var ii = 0 ; ii < 14 ;ii++){
								$(this).children().eq(ii).hide();
							}
						}
						if(i == 8){
							$(this).children().eq(0).show();
							$(this).children().eq(1).show();
							$(this).children().eq(2).hide();
							$(this).children().eq(3).hide();
						}
						addShowTimingSetJudge = 0;
					} );
					doJsonRequest("/rairconSet/QueryTimingSet", {deviceId:EYONDeviceId}, function (data) {
						timingSetObj = data.data;
						ShowTimingSet(timingSetObj);
					})
				},{showWaiting:true});
		}
	})
}	






//复选框勾选
function checking(obj){
	if( $(obj).val() == "0" ){
		$(obj).val("1");
	}else{
		$(obj).val("0");
	}
}	

/*-----删除TR----*/
function delTR(row,id){
	confirmFM("确定执行此操作?",function(){
		$(row).parent().parent().remove(); 	
	 	if(id!=''){
		 	doJsonRequest("/rairconSet/delClocksetting", {clocksettingid:id}, function (data) {
				d = data.data;
			},{showWaiting:true});
	 	}
	},true)
}
/*-----删除TR  不走后台的----*/
function delTR2(row){
	$(row).parent().parent().remove(); 	
	addShowTimingSetJudge = 0;
}
var CurveId = ""; 
/*--------打开折线图----------*/
function openLineChart(id){

	/*--加载曲线内容--*/
	doJsonRequest("/rairconSet/QueryCurve", {deviceId:id,minTemps:minTemps,maxTemps:maxTemps}, function (data) {
		CurveObj = data.data;
		Curvenum = CurveObj.length;
		//num=0;
		if(CurveObj.length < 1){
			$('#container').empty();
			$('#container').append("<p>当前设备没有舒睡曲线相关设置，请添加一条</p>");
			showcharts();
			//addNewCurve();
		}else{
			if(editOradd == 0){
				num=0;
				CurveId = CurveObj[0].id;
				lo(CurveObj[0].id);
			}else{
				CurveId = CurveObj[num].id;
				lo(CurveObj[num].id);
				editOradd = 0;
			}
		}
		/*--加载曲线数量--
		doJsonRequest("/rairconSet/QueryCurveNum", {deviceId:id,minTemps:minTemps,maxTemps:maxTemps}, function (nums) {//获取曲线数量
		Curvenum = parseInt(nums.data);
	})*/
	},{async:false})
	
}





/*-------加载所有折线图信息---------*/
function loadCurveData(){
	/*--加载曲线内容--*/
	doJsonRequest("/rairconSet/QueryCurve", {deviceId:EYONDeviceId,minTemps:minTemps,maxTemps:maxTemps}, function (data) {
		CurveObj = data.data;
		Curvenum = CurveObj.length;
	},{async:false})
}
/*---------加载重复设置信息（曲线图）--------*/
function weekSetforCurve(){
	/*----------加载重复设置----------*/
	var obj = CurveObj[num];
	/*----------加载重复设置----------*/
	if( CurveObj[num] != null && typeof(CurveObj[num]) != "undefined"){
		doJsonRequest("/rairconSet/QueryCurveSettingRepeat", {deviceId:EYONDeviceId,CurveId:CurveObj[num].id}, function (data) {
			var robj = data.data;
			obj = robj[0];
			if( typeof(  obj  ) == "undefined" ){
				obj = {
					monday:0,
					tuesday:0,
					wednesday:0,
					thursday:0,
					friday:0,
					saturday:0,
					sunday:0
				}
			}
		},{showWaiting : true,async : false});
	}else{
		if(JudgeCurveExist()){return;};
	}
	
	if(   $(".RepeatPopup").is(":visible") == false   ){
		var monday = "<input onclick=\"checking(this)\" name=\"monday\" type=\"checkbox\" value=\""+(obj.monday==null ? 0 : obj.monday)+"\" "+(obj.monday!=0 && obj.monday==1 ? "checked" : "")+"/>";
		var tuesday = "<input onclick=\"checking(this)\" name=\"tuesday\" type=\"checkbox\" value=\""+(obj.tuesday==null ? 0 : obj.tuesday)+"\" "+(obj.tuesday!=0 && obj.tuesday==1 ? "checked" : "")+"/>";
		var wednesday = "<input onclick=\"checking(this)\" name=\"wednesday\" type=\"checkbox\" value=\""+(obj.wednesday==null ? 0 : obj.wednesday)+"\" "+(obj.wednesday!=0 && obj.wednesday==1 ? "checked" : "")+"/>";
		var thursday = "<input onclick=\"checking(this)\" name=\"thursday\" type=\"checkbox\" value=\""+(obj.thursday==null ? 0 : obj.thursday)+"\" "+(obj.thursday!=0 && obj.thursday==1 ? "checked" : "")+"/>";
		var firday = "<input onclick=\"checking(this)\" name=\"friday\" type=\"checkbox\" value=\""+(obj.friday==null ? 0 : obj.friday)+"\" "+(obj.friday!=0 && obj.friday==1 ? "checked" : "")+"/>";
		var saturday = "<input onclick=\"checking(this)\" name=\"saturday\" type=\"checkbox\" value=\""+(obj.saturday==null ? 0 : obj.saturday)+"\" "+(obj.saturday!=0 && obj.saturday==1 ? "checked" : "")+"/>";
		var sunday = "<input onclick=\"checking(this)\" name=\"sunday\" type=\"checkbox\" value=\""+(obj.sunday==null ? 0 : obj.sunday)+"\" "+(obj.sunday!=0 && obj.sunday==1 ? "checked" : "")+"/>";
		
		$(".RepeatPopup").show();
		var tr = 
		"	<div><h4 class=\"h3_title\" style=\"margin:0px;text-align:center;\">请选择该空调的舒睡曲线及重复周期</h4></div>	"+
		"	<div  style=\"margin:55px 0;text-align:center;\">														"+
		"		<div id=\""+(CurveObj[num].id)+"1\" style=\"text-align:center;\">											"+
					monday+"<span style='font-size:19px;'>周一</span>																		"+
					tuesday+"<span style='font-size:19px;'>周二</span>																		"+
					wednesday+"<span style='font-size:19px;'>周三</span>																		"+
					thursday+"<span style='font-size:19px;'>周四</span>																		"+						
		"		</div>																								"+
		"		<div id=\""+(CurveObj[num].id)+"2\" style=\"text-align:center;margin-left:-53px;\"> 				"+
					firday+"<span style='font-size:19px;'>周五</span>																		"+
					saturday+"<span style='font-size:19px;'>周六</span>																		"+
					sunday+"<span style='font-size:19px;'>周日</span>																		"+
		"		</div>																								"+
		"	</div>																									"+
		"	<div style=\"clear:both\"></div>																		"+
		"	<div style=\"width:100%;background:#F2F2F2;padding:8px 0px; float:left;color:#000;\">					"+
		"	<div onclick=\"closeRepeatPopup()\" onmouseout=\"this.style.backgroundColor='#F2F2F2'\" onmousemove=\"this.style.backgroundColor='#d4d9d6'\"  style=\"cursor: pointer;float:left;width:48%;border-right:1px solid #999999;text-align:center;\">取消</div>			"+
		"	<div onclick=\"saveRepeatPopup(this)\" onmouseout=\"this.style.backgroundColor='#F2F2F2'\" onmousemove=\"this.style.backgroundColor='#d4d9d6'\" style=\"cursor: pointer;float:left;width:48%;text-align:center;\">确定</div>										"+
		"	</div>";
		hmBlockUI();
		$(".RepeatPopup").append(tr); 
	}else{
		closeRepeatPopup();
	}
}
/*------关闭重复设置窗口------*/
function closeRepeatPopup(){
	$(".RepeatPopup").hide();
	$(".RepeatPopup div").remove();
	$(".RepeatPopup h4").remove();
	hmUnBlockUI();
}
/*------关闭设置窗口------*/
function closeWeekDiv(){
	$(".weekDiv").hide();
}

/*---------保存重复设置(折线图相关)----------*/
function saveRepeatPopup(obj){
	var weeks = {
		monday:$("#"+(CurveObj[num].id)+"1").children().eq(0).val(),
		tuesday:$("#"+(CurveObj[num].id)+"1").children().eq(2).val(),
		wednesday:$("#"+(CurveObj[num].id)+"1").children().eq(4).val(),
		thursday:$("#"+(CurveObj[num].id)+"1").children().eq(6).val(),
		friday:$("#"+(CurveObj[num].id)+"2").children().eq(0).val(),
		saturday:$("#"+(CurveObj[num].id)+"2").children().eq(2).val(),
		sunday:$("#"+(CurveObj[num].id)+"2").children().eq(4).val()
	};
	//修改之前先查询判断一下是否有重复设置的
	doJsonRequest("/rairconSet/queryCurveRepeat", {deviceId:EYONDeviceId,curveId:CurveObj[num].id,weeks:weeks}, function(data) {
		var da = data.data;
		if(!da.result){
			confirmFM("有相同设置，是否继续?",function(){
				doJsonRequest("/rairconSet/updateCurveRepeat", {deviceId:EYONDeviceId,curveId:CurveObj[num].id,weeks:weeks}, function(data) {
					var d = data.data;
					closeRepeatPopup();
					loadRepeat(CurveObj[num].id);
					//$(".weekTable").hide();  
					//$('#charts').show();
				},{showWaiting:true})
			},true)
		}else{
				doJsonRequest("/rairconSet/updateCurveRepeat", {deviceId:EYONDeviceId,curveId:CurveObj[num].id,weeks:weeks}, function(data) {
					var d = data.data;
					closeRepeatPopup();
					loadRepeat(CurveObj[num].id);
					//$(".weekTable").hide();  
					//$('#charts').show();
				},{showWaiting:true})
		}
	})
} 

/*-----------------空调异常相关----------------------*/
$(function(){
	setInterval('getError()', 60000); 
})
var errorData = "";
var errorControl  = "";
function getError(){
	//TODO
	doJsonRequest("/deviceError/getDeviceError", {deviceId:devicesId}, function(data) {
		var obj = data.data;
		if( obj.length > 0 ){
			//异常代码
			for ( var int = 0; int < obj.length; int++) {
				errorData = "<div class=\"errorDataClass\" style='margin-right:30%;margin-top:9%;float:right;'>尊敬的用户，您好，您的空调当前处于异常状态,<br />异常信息：<span style='color:#3399FF;'>" + obj[int].errorExplain + "</span>您可以联系售后人员为您解决。</div>"
				errorStateClick(obj[int].deviceId,errorData);
				errorControl = "1";
			}
		}
	})
}

function errorState(obj,errorData){
	if(   !$(obj).parent().parent().children().eq(7).is(":visible")    ){
		//显示异常信息
		$(obj).parent().parent().children().eq(1).css("display","none");
		$(obj).parent().parent().children().eq(2).css("display","none");
		$(obj).parent().parent().children().eq(3).css("display","none");
		$(obj).parent().parent().children().eq(4).css("display","none");
		if($(obj).parent().parent().children().length > 7){
			$(obj).parent().parent().children().eq(5).css("display","none");
			$(obj).parent().parent().children().eq(6).css("display","none");
			$(obj).parent().parent().children().eq(7).css("display","none");
		}
		$(obj).parent().parent().children().eq(7).remove();
		errorData = "<div class=\"errorDataClass\" style='margin-right:30%;margin-top:9%;float:right;'>尊敬的用户，您好，您的空调当前处于异常状态,<br />不能进行<span style='color:#3399FF;'>温度</span>调整，您可以联系售后人员为您解决。</div>"
		$(obj).parent().parent().append(errorData);
	}else{
		if($(obj).parent().parent().children().length > 7){
			$(obj).parent().parent().children().eq(5).css("display","none");
			$(obj).parent().parent().children().eq(6).css("display","none");
			$(obj).parent().parent().children().eq(7).css("display","none");
		}
		//隐藏异常信息
		$(obj).parent().parent().children().eq(1).css("display","inline");
		$(obj).parent().parent().children().eq(2).css("display","inline");
		$(obj).parent().parent().children().eq(3).css("display","inline");
		$(obj).parent().parent().children().eq(4).css("display","inline");
		$(obj).parent().parent().children().eq(7).css("display","none");
	}
}
//TODO   --- errorData
function errorStateClick(deviceId,errorData){
	$('.content_kt_nr_kg_ktkg[find='+deviceId+']').parent().children().eq(0).addClass("airconditioning_notopen");
	$('.content_kt_nr_kg_ktkg[find='+deviceId+']').parent().children().eq(0).attr("onclick",'errorState(this)');
}	


/*----------时间插件----------------*/
function castDatetimepicker(){
	$('.time-picker').each(function(){
		$(this).datetimepicker({
			datepicker:false,
			format:'H:i',
			step:1
		});
	})
}
/*-----------加载所有曲线图------------------*/
function openAllLineChart(obj){
	num = 0;
	/*QueryClockNum(EYONDeviceId);
	QueryCurveNumForCurveDevice(EYONDeviceId);*/
	EYONDeviceId = "all";
	//隐藏在此不该出现的设置重复按钮
	addShowTimingSetJudge = 0;
	$('.RairconSetting_div4').children().attr("style","display:none;");
	
	$('.RairconSetting_div1').children().attr("style","display:none;");
	$('.repeats').hide();
	//$('.RairconSetting_div4').children().children().text("设备匹配\t\t  ");
	$("a").removeClass("hover");
	$(obj).addClass("hover");
	doJsonRequest("/rairconSet/QueryCurve", {deviceId:'all'}, function (data) {
		CurveObj = data.data;
		Curvenum = CurveObj.length;
		if(CurveObj.length < 1){
			$('#container').empty();
			$('#container').append("<p>当前没有舒睡曲线相关设置，请添加一条</p>");
			showcharts();
			//addNewCurve();
		}else{
			CurveId = CurveObj[num].id;
			lo(CurveObj[num].id);
		}
		//JudgeCurveExist();
	})/*--加载曲线数量--
	doJsonRequest("/rairconSet/QueryCurveNum", {deviceId:'all',minTemps:minTemps,maxTemps:maxTemps}, function (nums) {//获取曲线数量
		Curvenum = parseInt(nums.data);
	})*/
	
}
/*----------舒睡曲线相关工具类-----------*/
function onOffForCurveSave(obj){
	var i = 0 ;
	if($(obj).val() == "0"){
		$(obj).parent().parent().children().each(function(){
			if( i == 2 ){
				$(this).children().val("25") ;
				$(this).children().attr("disabled","disabled");
			}
			if( i == 3 ){
				$(this).children().val("0") ;
				$(this).children().attr("disabled","disabled");
			}
			if( i == 4 ){
				$(this).children().val("0") ;
				$(this).children().attr("disabled","disabled");
			}
			i++;
		})
	}
	if($(obj).val() == "1"){
		$(obj).parent().parent().children().each(function(){
			if( i == 2 ){
				//$(this).children().val("15") ;
				$(this).children().attr("disabled",null);
			}
			if( i == 3 ){
				//$(this).children().val("0") ;
				$(this).children().attr("disabled",null);
			}
			if( i == 4 ){
				//$(this).children().val("0") ;
				$(this).children().attr("disabled",null);
			}
			i++;
		})
	}
}
/*
 * 弹出框alert
 */
function alertFM(str,bool){
	if(!bool){
		$('#anniu3').remove();
		hmUnBlockUI();
		hmUnBlockUIFM();
	}else{
		var tr = 
			"<div id='anniu3' style=\"position:absolute;"+
							"background:#FFFFFF;"+
							"width:360px;"+
							"left:37%;"+
							"top:33%;"+
							"z-index:2000000;\">"+
			"	<div style=\"margin:55px 0;text-align:center;\">										"+
			"		<div style=\"text-align:center; color:#000;\">										"+
			"           <span>"+str+"</span>															"+
			"		</div>																				"+
			"	</div>																					"+
			"	<div style=\"clear:both\"></div>														"+
			"	<div style=\"width:100%;background:#F2F2F2;padding:8px 0px; float:left;color:#000;\">	"+
			"	<div onclick=\"alertFM(null,false)\" onmouseout=\"this.style.backgroundColor='#F2F2F2'\" onmousemove=\"this.style.backgroundColor='#d4d9d6'\"  style=\"cursor: pointer;float:right;width:100%;border-right:0px solid #999999;text-align:center;\">确定</div>			"+
			"	</div>"+
		"	</div>";
		$("#body").append(tr); 
		hmBlockUI();
		hmBlockUIFM();
	}
}
/*弹出框 - TODO*/
function confirmFM(str,functions,bool){
	if(!bool){
		$('#anniu2').remove();
		hmUnBlockUI();
		hmUnBlockUIFM();
	}else{
		var tr = 
			"<div id='anniu2' style=\"position:absolute;"+
							"background:#FFFFFF;"+
							"width:360px;"+
							"left:37%;"+
							"top:33%;"+
							"z-index:2000000;\">"+
			"	<div style=\"margin:55px 0;text-align:center;\">														"+
			"		<div style=\"text-align:center; color:#000;\">											            "+
			"           <span>"+str+"</span>																		    "+
			"		</div>																								"+
			"	</div>																									"+
			"	<div style=\"clear:both\"></div>																		"+
			"	<div style=\"width:100%;background:#F2F2F2;padding:8px 0px; float:left;color:#000;\">					"+
			"	<div id='anniu' onmouseout=\"this.style.backgroundColor='#F2F2F2'\" onmousemove=\"this.style.backgroundColor='#d4d9d6'\" style=\"cursor: pointer;float:left;width:49%;text-align:center;border-right:1px solid #999999;\">确定</div>										"+
			"	<div onclick=\"confirmFM(null,null,false)\" onmouseout=\"this.style.backgroundColor='#F2F2F2'\" onmousemove=\"this.style.backgroundColor='#d4d9d6'\"  style=\"cursor: pointer;float:left;width:50%;text-align:center;\">取消</div>			"+
			"	</div>"+
		"	</div>";
		$("#body").append(tr); 
		$("#anniu").click(function(){
			functions();
			$('#anniu2').remove();
			hmUnBlockUI();
			hmUnBlockUIFM();
		})
		hmBlockUI();
		hmBlockUIFM();
	}
}
/**
 * 锁屏用
 */
function hmBlockUIFM(){
	var divObj = $('<div id="loadingMaskLayerDivFM" style="filter:alpha(opacity=50);display:none;width:100%;height:100%;z-index:19000;background-color:gray;opacity:0.5;position:fixed;left:0px;top:0px;"><div class="loading"><i></i><span id="windowMaskLayerInfo"></span></div></div>');
	$('body').append(divObj);
	//$('#windowMaskLayerInfo').html("处理中");
	divObj.show();
}

function hmUnBlockUIFM(){
	$("#loadingMaskLayerDivFM").remove();
}