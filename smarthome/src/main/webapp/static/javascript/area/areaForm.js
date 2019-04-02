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
	doReQuery(classification);
}
if(getURLParam("parentid") != null){
	$("#parentname").val(getURLParam("parentname"));
	$("#parentid").val(getURLParam("parentid"));
	$("#parentType").val(getURLParam("parentType"));
	
}

function testAlert() {
	var str = "checkType=multi";

	var obj = {
		title : '区域选择',
		height : "320px",
		width : "1050px",
		url : 'follow_role_dialog.html?classification='+classification,
		fun : true,
		myCallBack : function(data) {
			var parentname = data[0].name;
			var parentid = data[0].id;
			var parentType=data[0].type;
			$("#parentname").val(parentname);
			$("#parentid").val(parentid);
			$("#parentType").val(parentType);
		}
	}
	nowSearchRole = new jqueryDialog(obj);

	$(".dialog_div_").parent().addClass("wf_top");
}

$(document).ready(function() {
	if(getURLParam("classification") == 1){
		$("#li1").addClass("active");
	}else if(getURLParam("classification") == 2){
		$("#li2").addClass("active");
	}
	$("#classification").select2("val",getURLParam("classification"));
	//$("#classification").attr("readonly","true");
	$("#btnSubmit").click(function() {
		$("#inputForm").submit();
	});
	$("#classification").select2("readonly", true);
	$("#name").focus();
	$("#inputForm").validate({
		submitHandler : function(form) {
			var flag=true;
			if($("#parentType").val()!="" && parseInt($("#parentType").val()) >= parseInt($("#type").val())){
				$.alert("当前区域类型不能高于上级区域");
			return false;
			}
			if($("#type").val() == ""){
				$.alert("必填信息不能为空");
				return false;
			}
			if($("#classification").val() ==""){
				$.alert("必填信息不能为空");
				return false;
			}
			if($("#policy").val() ==""){
				$.alert("必填信息不能为空");
				return false;
			}
			var dto = {
					myName : $("#myName").val(),
					myid : $("#myid").val(),
					myParent : $("#parentid").val()
				}
				doJsonRequest("/area/hasName", dto, function(data) {
					if (data.result) {
					} else {
						flag=false;
						$.alert("该区域发生冲突，请验证其唯一性");
						return;
					}
				},{ async : false});
				if(!flag){
				return false;
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
				id :$("#myid").val()
			}
			doJsonRequest("/area/doSave", d, function(data) {
				if (data.result) {
					window.location.href = "areaList.html?classification="+$("#classification").val();
				} else {
					$.alert("保存失败");
				}
			},{showWaiting:true})

		}
	});
});
/**
 * ready
 */
$(function() {
	//doQuery()
	//select2组件注册
	$(".the_select2").addChangeListener(function() {
		doQuery();
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
