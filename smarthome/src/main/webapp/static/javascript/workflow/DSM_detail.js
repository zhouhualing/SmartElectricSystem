$("#id").val(getURLParam("id"));

$("#basicEdit").on("click", function () {
    $("#searchCompanyBtn").show();
    if ($("#companyId").val().length > 0) {
        $("#searchOrgBtn").show();
    }
})

$("#basicCancel,#basicComplete").on("click", function () {
    $("#searchCompanyBtn").hide();
    $("#searchOrgBtn").hide();
})
var status;
var getAllRole = null;
var roleIds = null;
//初始化信息
$(function () {
    //编辑
    if (getURLParam("id") != null) {
        var dto = {
            id: getURLParam("id")
        };
        doJsonRequest("/dsm/getInfo", dto, function (data) {
            if (data.result) {
                var data = data.data;
				$("#schemeName").html(data.schemeName);
				$("#schemeType").html(data.schemeType);
				
				$("#startDate").html(data.startDateS);
				$("#endDate").html(data.endDateS);
				
				$("#schemePeople").html(data.schemePeople);
				$("#contactPhone").html(data.contactPhone);
				$("#planReduceTargetQ").html(data.planReduceTargetQ);
				$("#promisedReduceTargetQ").html(data.promisedReduceTargetQ);
				$("#actualReduceQ").html(data.actualReduceQ);
				$("#planReduceTargetP").html(data.planReduceTargetP);
				$("#promisedReduceTargetP").html(data.promisedReduceTargetP);
				$("#actualReduceP").html(data.actualReduceP);
            } else {
            }
        }, {showWaiting: true})
    } else {
        //新建
        $("#basicEdit").trigger("click")
    }
})

//validate
$("#inputForm").validate({
    submitHandler: function (form) {
        finishEditText($(".edit_complete")[0], 'basic', '/user/editUser', 'inputForm', function () {
            $("#searchCompanyBtn").hide();
            $("#searchOrgBtn").hide();
        });
    }
});

function doEdit() {
    //方式一
    //$("#inputForm").submit();
    //方式二
	
	/* 验证输入的密码 */
	if($("#password").val() != ""){
		if($("#confirmPassword").val() == ""){
			$.alert("请确认密码");
			return false;
		}
		if($("#confirmPassword").val() != $("#password").val()){
			$.alert("两次所输入的密码不一致");
			return false;
		}
	}
    var data = {};
    $(".required").each(function () {
        data[$(this).attr("name")] = $(this).val();
    });
    data["roleIds"] = roleIds;
    data["id"] = getURLParam("id");
    //  console.log(data);
    doRequest("/user/editUser", data, function (data) {
        location.href = "user_list.html";
    }, {showWaiting: true})

}

function doDelete() {
	var mes ="您确定要删除么？";
    $.confirm({
        title: '提示信息',
        msg: mes,
        confirmClick: function () {
           var dto = {'faId': getURLParam("id")};
           doJsonRequest("/dsm/deleteDSM",dto,function(data){
        	   history.go(-1);
           },{showWaiting:true,successInfo:'删除成功',failtureInfo:'删除失败'})
        }
    });
}

function searchCompany() {
    var nowCompanyId = $("#companyId").val();
    C_doOrgSelect(function (data) {
        $("#companyName").val(data.name);
        $("#companyId").val(data.id);
        if (nowCompanyId != $("#companyId").val()) {
            $("#searchOrgBtn").show();
            $("#orgName").val(null);
            $("#orgId").val(null);
        }
    });
}


function searchOrg() {
    C_doOrgSelect({
        params: {pId: $("#companyId").val()},
        myCallBack: function (data) {
            $("#orgName").val(data.name);
            $("#orgId").val(data.id);
        }
    });
}