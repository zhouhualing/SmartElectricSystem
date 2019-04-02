/**
 * 表格操作项
 */
var oppObj = [];
/**
 * 处理url参数
 */

/**
 * ready
 */
$(function() {

	doJsonRequest(
			"/spmsPortalDefault/getDefault",
			null,
			function(data) {
				if (data.result) {
					var temp = data.data;
					for ( var key in temp) {
						var rolename , rolecode;
						var t = temp[key];
						for( var k in t){
							if (k == "roleName") {
								rolename = t[k];
							} 
						}
						rolecode = key;
						/*----------------动态添加DIV------------------*/
						var html = "<div id='"+rolecode+"'>"
						+ "<div class='table_title'>"
						+ "<h2>"+rolename+"默认显示配置</h2>"
						+ "<div class='action_button'>"
						+ "<a href='javascript:void(0);' id='btnSubmit' onClick='doEdit(this)'><i"
						+ "	class='complete_ico'></i>保存</a> "
						+ "	"
						+ "</div>"
						+ "</div>"
						+ "<div>"
						+ "<form id='inputForm' class='form-horizontal form-two form_znydxq'"
						+ "	 method='post'>"
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>角色编码:</label>"
						+ "		<div class='controls'>"
						+ "			<input type='text' value='' maxlength='50' readonly='true'"
						+ "				id='rolecode' class='required'/>"
						+ "		</div>"
						+ "	</div>"
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>用户数量区域统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='userCountByArea' name='userCountByArea' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ "		</div>"
						+ "	</div>"
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>服务类别统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='userCountByRule' name='userCountByRule' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ "		</div>"
						+ "	</div>"
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>在线/异常统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='userCountByOnlineStatus' name='userCountByOnlineStatus' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ ""
						+ "		</div>"
						+ "	</div>"
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>设备库存统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='deviceByModel' name='deviceByModel' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ "		</div>"
						+ "	</div>"
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>设备状态统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='deviceByOptStatus' name='deviceByOptStatus' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ "		</div>"
						+ "	</div>"
						
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>订户产品期限统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='userCountByProductRemaining' name='userCountByProductRemaining' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ "		</div>"
						+ "	</div>"
						
						+ "	<div class='control-group'>"
						+ "		<label class='control-label'>软件本统计信息:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='SoftwareVersionStatistical' name='SoftwareVersionStatistical' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "			</select>"
						+ "		</div>"
						+ "	</div>"
						
						+ "	<div class='control-group' style='width: 1140px; height: 50px'>"
						+ "		<label class='control-label'>开通/退订统计:</label>"
						+ "		<div class='controls'>"
						+ "			<select id='userIncrementByDate' name='userIncrementByDate' style='width: 220px'"
						+ "				class='the_select2 required'>"
						+ "				<option value='1'>--显示--</option>"
						+ "				<option value='0' selected='true'>--不显示--</option>"
						+ "		</select>" + "	</div>" + "	</div>"
						+ "</form>" + "</div>" + "</div>"
						$(document.body).append(html);
						$("select").select2();
						//$("#rolecode").val(key);
						$("div#"+rolecode).find("#rolecode").val(rolecode);
						/*----------------动态添加DIV----END--------------*/
						
						for ( var k in t) {
							if (k == "roleName") {
								//alert(t[k]);
							} else {
								if (t[k] == true) {
									$("div#"+rolecode).find("#" + k + "").select2("val", "1");
								}
							}
						}
					}
				}
			},{showWaiting:true})

	$("#basicEdit").on("click", function() {
		$("#areaSubmit").show();
	})

	$("#basicCancel").on("click", function() {
		$("#areaSubmit").hide();
	});

	// select2组件注册
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
 * table组件，当row被点击时执行操作
 * 
 * @param a
 * @param b
 */
function rowClick(data, tr) {
	// window.location.href = "spmsWorkOrderShow.html?id=" + data.id;
}

function createFun() {
	// window.location.href = "spmsWorkOrderForm.html";
}

function deleteFun() {
	// alert("删除成功")
}

/**
 * 
 */
function doEdit(t) {
	var rolecode = $(t).parent().parent().parent().attr("id");
	var d = {
		rolecode : rolecode,
		userCountByArea : $("div#"+rolecode).find("#userCountByArea").val(),
		userCountByRule : $("div#"+rolecode).find("#userCountByRule").val(),
		userCountByOnlineStatus : $("div#"+rolecode).find("#userCountByOnlineStatus").val(),
		deviceByModel : $("div#"+rolecode).find("#deviceByModel").val(),
		deviceByOptStatus : $("div#"+rolecode).find("#deviceByOptStatus").val(),
		userIncrementByDate : $("div#"+rolecode).find("#userIncrementByDate").val(),
		userCountByProductRemaining:$("div#"+rolecode).find("#userCountByProductRemaining").val(),
		SoftwareVersionStatistical:$("div#"+rolecode).find("#SoftwareVersionStatistical").val()
	};
	doJsonRequest("/spmsPortalDefault/changeDefault", d, function(data) {
		if (data.result) {
			$.alert("保存成功");
		}

	});
}

function doDelete() {
	/*	$.alert("删除按钮");
	 doJsonRequest("/spmsPortalCustom/getCanAddSort", null,
	 function(data) {
	 if(data.result){
	 alert($.toJSON(data.data));
	 }
	 })*/
}
