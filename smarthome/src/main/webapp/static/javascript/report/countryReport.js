$(function(){
	$("#fromDate").datetimepicker({
		showSecond: false, //显示秒
		timeFormat: '',//格式化时间
		stepHour: 1,//设置步长
		stepMinute: 5,
		stepSecond: 10,
		dateFormat:"yy-m-d",
		currentText:'现在',
		closeText:'确定',
		hourMax:'23',
		hourText:'时',
		minuteText:'分',
		secondText:'秒',
		timeText:'时间'
	});
	$("#toDate").datetimepicker({
		showSecond: false, //显示秒
		timeFormat: '',//格式化时间
		stepHour: 1,//设置步长
		stepMinute: 5,
		stepSecond: 10,
		dateFormat:"yy-m-d",
		currentText:'现在',
		closeText:'确定',
		hourMax:'23',
		hourText:'时',
		minuteText:'分',
		secondText:'秒',
		timeText:'时间'
	});
	if(new Date().getTheYear() == 2014) {
		$("#fromDate").val("2014-10-13");
	} else {
		$("#fromDate").val(new Date().format("yyyy-MM-dd"));
	}
	$("#toDate").val(new Date().format("yyyy-MM-dd"));
	doQuery();
})

function queryEnd(data) {
	clickmedTables.reportInfoQuery.hideFoot();
	if(data != undefined) {
		if(data.exportName) {
			window.location.href="/cmcp/attachment/downloadExportFile?showName=0003&fileName="+data.exportName;
		}
	}
}

function doExport() {
	$("#export").val("0001");
	doQuery();
	$("#export").val("");
}

var oppObj = [];


