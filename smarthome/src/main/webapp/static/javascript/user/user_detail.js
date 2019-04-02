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
        doJsonRequest("/user/getInfo", dto, function (data) {
            if (data.result) {
                var data = data.data;
            	status=data.status;
				if(status != 0){
				$("#basicDel").attr("class","frozen_disable_ico");
				}
                var roleId = data["roleIds"].split(",")
                var dataKey = [];
                for (var key in data) {
                    dataKey.push(key + "S");
                }
                doJsonRequest("/user/getAllRole", null, function (dat) {
                    if (dat.result) {
                        getAllRole = dat["data"];
                        var str = null;
                        for (var k in roleId) {
                            if (str) {
                                str += "," + getAllRole[roleId[k]];
                            }

                            else {
                                str = getAllRole[roleId[k]];
                            }
                        }
                        $(".input_view[name='roleIdsS']").html(str);
                    }

                });
                $("div", $("fieldset")).each(function () {
                    if ($.inArray($(this).attr("id"), dataKey) != -1) {
                        if (($(this).attr("id") == "passwordS") || ($(this).attr("id") == "confirmPaswordS")) {
                        } else {
                            var nowFieldId = $(this).attr("id").substring(0, $(this).attr("id").length - 1);
                            if ($("#" + nowFieldId).is(".the_select2")) {
                                $(this).html(data[(nowFieldId + "Text")]);
                                $(this).attr("eValue", data[nowFieldId]);
                                $("#" + nowFieldId).select2('val', 1);
                            } else {
                                $(this).html(data[nowFieldId]);
                            }
                        }
                    }
                })

                $("#companyId").val(data.companyId);
                $("#orgId").val(data.orgId);
//				$("#userTypeS").html(data.userTypeText);
//				$("#userTypeS").attr("eValue",data.userType);
//				$("#userType").select2('val',1)

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
	var mes ="您确定要冻结么？";
	if(status != 0){
		mes ="您确定要解冻么？"
	}
    $.confirm({
        title: '提示信息',
        msg: mes,
        confirmClick: function () {
           var dto = {'ids': getURLParam("id")};
            doJsonRequest("/user/deleteUser", dto, function (data) {
                if (data.result) {
                    history.go(-1)
                } else {

                }
            })
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