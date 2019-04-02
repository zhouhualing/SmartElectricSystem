if(getURLParam("classification") == 1) {
    $("#the_name").html("服务区域列表");
} else {
    $("#the_name").html("用电区域列表");
}

/**
 * 表格操作项
 */
var oppObj = [];

var classification = 1;
/**
 * 处理url参数
 */
if (getURLParam("classification") != null) {
	classification = getURLParam("classification");
	doReQuery(classification)
}
function testAlert() {
	var str = "checkType=multi";

	var obj = {
		title : '区域选择',
		height : "320px",
		width : "1050px",
		url : 'follow_role_dialog.html?classification=' + classification,
		fun : true,
		myCallBack : function(data) {
			var parentname = data[0].name;
			var parentid = data[0].id;
			$("#parentname").val(parentname);
			if($("#myid").val()==parentid){
				$("#parentid").val();
			}else{
				$("#parentid").val(parentid);
			}
		}
	}
	nowSearchRole = new jqueryDialog(obj);

	$(".dialog_div_").parent().addClass("wf_top");
}

$(document).ready(function() {

	var id = {
		id : getURLParam("id")
	}
	doJsonRequest("/area/getById", id, function(data) {
		if (data.result) {
			//		alert($.toJSON(data.data));

			if (data.data.classification == 1) {
				$("#li1").addClass("active");
			} else if (data.data.classification == 2) {
				$("#li2").addClass("active");
			}

			if (data.data.parent == null) {
				$("#parentname").val("顶级区域");
				$("#parentid").val();
			} else {
				$("#parentname").val(data.data.parent.name);
				$("#parentid").val(data.data.parent.id);
			}
			$("#myName").val(data.data.name);
			$("#myid").val(data.data.id);
			$("#code").val(data.data.code);

			$("#type").select2("val", data.data.type);
			$("#classification").select2("val", data.data.classification);
			$("#classification").select2("readonly", true);
			$("#policy").select2("val", data.data.policy);
			$("#remarks").val(data.data.remarks);

		} else {
			$.alert("信息获取失败");
		}
	});

	$("#btnSubmit").click(function() {
		$("#inputForm").submit();

	});
	$("#name").focus();
});

$("#inputForm").validate({
	submitHandler : function(form) {
		var dto = {
			myName : $("#myName").val(),
			myid : $("#myid").val(),
			myParent : $("#parentid").val()
		}
		doJsonRequest("/area/hasName", dto, function(data) {
			if (data.result) {
				//alert(1);
			} else {
				alert("该区域发生冲突，请验证其唯一性");
				return;
			}
		})
		if ($("#type").val() == "") {
			$.alert("必填信息不能为空");
			return;
		}
		if ($("#classification").val() == "") {
			$.alert("必填信息不能为空");
			return;
		}
		if ($("#policy").val() == "") {
			$.alert("必填信息不能为空");
			return;
		}
		var d = {
			parent : {
				id : $("#parentid").val()
			},
			name : $("#myName").val(),
			code : $("#code").val(),
			type : $("#type").val(),
			classification : $("#classification").val(),
			policy : $("#policy").val(),
			remarks : $("#remarks").val(),
			id : $("#myid").val()
		}
		doJsonRequest("/area/doSave", d, function(data) {
			if (data.result) {
				//$.alert("保存修改成功");
				window.location.href = "areaList.html?classification=" + data.data.classification;
			} else {
				$.alert("保存修改失败");
			}
		})

	}
});
/**
 * ready
 */
$(function() {
	//doQuery()
	//select2组件注册
	$(".the_select2").addChangeListener(function() {
		//doQuery();
	})

	$("#checkAll").on('click', function() {
		$("[name=checkId]:checkbox").attr("checked", this.checked);
	});

	$("#delBtn").click(
			function() {
				var checkIds = $("[name=checkId]:checked");
				if (checkIds.length == 0) {
					alert("请至少选择一条数据");
					return;
				}
				var ids = [];
				checkIds.each(function(index, e) {
					ids.push($(e).val());
				});
				confirmx('确认要删除所选条目吗？', "/spms/a/spms/spmsDevice/delete?id="
						+ ids.join(','));
			});
})

/**
 * 点击tab栏，动态过滤设备类型信息
 * @param type
 */
function doReQuery(type) {
	$("#classification").val(type);
	$(".active").removeClass("active");
	$("li:even", $(".nav")).eq(type - 1).attr("class", "active");
	//	doQuery(); 	
}

/**
 * table组件，当row被点击时执行操作
 * @param a
 * @param b
 */
function rowClick(data, tr) {
}

function createFun() {
	window.location.href = "areaForm.html?classification=" + classification;
}

function deleteFun() {
	$.alert("删除成功")
}
