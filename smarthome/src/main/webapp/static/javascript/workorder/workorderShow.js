/**
 * 表格操作项
 */
var oppObj = [];
/**
 * 处理url参数
 */
function testAlert() {
	var str = "checkType=multi";

	var obj = {
		title : '运营区域选择',
		height : "320px",
		width : "1050px",
		url : 'follow_role_dialog.html?classification=' + 1,
		fun : true,
		myCallBack : function(data) {
			var areaname = data[0].name;
			var areaid = data[0].id;
			$("#areaname").val(areaname);
			$("#areaid").val(areaid);
		}
	}
	nowSearchRole = new jqueryDialog(obj);

	$(".dialog_div_").parent().addClass("wf_top");
}

/**
 * ready
 */
$(function() {
	
	$("#basicEdit").on("click",function(){
		$("#areaSubmit").show(); 
	})
	
	$("#basicCancel,#basicComplete").on("click",function(){
		$("#areaSubmit").hide(); 
	});

			var id = {
				id : getURLParam("id")
			}
			doJsonRequest("/spmsWorkOrder/getById", id, function(data) {
						if (data.result) {
							$("#myid").val(data.data.id);

							$("#areanameS").html(data.data.area.name);
							$("#areaname").val(data.data.area.name);
							$("#areaid").val(data.data.area.id);

							$("#type").select2("val", data.data.type);

							$("#status").select2("val", data.data.status);

							$("#allottype").select2("val", data.data.allottype);

							//$("#duration").val(data.data.duration);
							$("#durationS").html(new Date(data.data.duration).format());
							$("#typeS").html(data.data.typeText);
							$("#typeS").attr("eValue",data.data.type);
							$("#statusS").html(data.data.statusText);
							$("#statusS").attr("eValue",data.data.status);
							$("#allottypeS").html(data.data.allottypeText);
							$("#allottypeS").attr("eValue",data.data.allottype);
							$("#showhref").attr("href","spmsWorkOrderShow.html?id="+data.data.id);
						} else {
							$.alert("查询失败");
						}

					})

		})

/**
 * 点击tab栏，动态过滤设备类型信息
 * 
 * @param type
 */


/**
 * 
 */
function doEdit() {
	// doRequest();
	if ($("#type").val() == "") {
		$.alert("必填信息不能为空");
		return;
	}
	if ($("#status").val() == "") {
		$.alert("必填信息不能为空");
		return;
	}
	if ($("#allottype").val() == "") {
		$.alert("必填信息不能为空");
		return;
	}
	if ($("#duration").val() == "") {
		$.alert("必填信息不能为空");
		return;
	}
	if ($("#duration").val() == null) {
		$.alert("必填信息不能为空");
		return;
	}
	finishEditText($(".edit_complete")[0], 'basic', '/spmsWorkOrder/doEdit');
	var areaname = $("#areaname").val();
	$("#areanameshow").val(areaname);
	/*
	 * var data = $("#inputForm").serialize(); alert(data);
	 * doRequest("/spmsWorkOrder/doEdit", data, function(data) {
	 * if(data.result){ alert('ok'); } },{showWaiting:true})
	 */
}

function doDelete() {
	//alert($("#myid").val());
	var info = {
		id : $("#myid").val()
	}
	doJsonRequest("/spmsWorkOrder/doDelete", info, function(data) {
				if (data.result) {
					if (data.data.success) {
						$.alert(data.data.msg);
						window.location.href = "spmsWorkOrderList.html";
					} else {
						$.alert(data.data.msg);
					}
				}
			}, {
				showWaiting : true
			})
}
