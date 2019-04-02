function clickFun(n){
	var start = $("#startYear").val()+"-"+$("#startMonth").val();
	var end = $("#endYear").val()+"-"+$("#endMonth").val();
	if($("#startYear").val()=='' || $("#startMonth").val()=='' || $("#endYear").val()=='' || $("#endMonth").val()=='' || start>end){
		$.alert("请选择正确的日期。");
		return false;
	}
	
	if(n == 1){
		//查询列表
		$("#months").val(start+','+end);
		doQuery();
	}else{
		//查询图标
		openCharts(start,end);
	}
}

function openCharts(start,end) {
	var obj = {
	    title:'图标分析',
	    height:"500px",
	    width:"950px",
	    url:'dataCount_charts.html?start='+start+"&end="+end,
	    myCallBack:myCallBack,
	    cancelText:"关闭",
	    confirmBtn:false,
	    fun:true
	}
	new jqueryDialog(obj);
}

function myCallBack(){}