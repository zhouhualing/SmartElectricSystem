var id = getURLParam("id");

$(function(){
	initData();
})

function initData() {
	$("#id").val(id);
	var data = {
	     id:id
	}
	doJsonRequest("/position/getPositionData", data, function(data, status){
		if(data.result) {
			var data = data.data;
			$("#positionName").val(data.positionName);
			$("#positionCode").val(data.positionCode);
			$('#orgName').val(data.orgName);
			$('#orgCode').val(data.orgCode);
		} else {
		}
	});
}

function goBack() {
	window.history.back();
}

function doSearchOrg() {
	var obj = {
	    title:'选择组织机构',
	    height:"300px",
	    width:"450px",
	    url:'../org/org_org_tree.html?checkType=radio',
	    myCallBack:"initOrgInfo"
	}
	new jqueryDialog(obj);
}

function initOrgInfo(data) {
	$('#orgCode').val(data[0].code);
	$("#orgName").val(data[0].realName);
}

function doSubmit() {
	var form = $('#dataInputForm');
	doRequest("/position/addPosition", form.serialize(), function(data,status){
		if(data.result) {
			var data = data.data;
			if (data) {
				$.alert(data);
			} else {
				window.location.href="position_show_list.html";
			}
		} else {
			$.alert("提交失败。");
		}
	});
}