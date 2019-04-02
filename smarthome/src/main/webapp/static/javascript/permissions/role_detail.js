$("#id").val(getURLParam("id"));

$("#basicEdit").on("click", function () {
    /*$("#searchCompanyBtn").show();
     if($("#companyId").val().length > 0) {
     $("#searchOrgBtn").show();
     }*/
})

$("#basicCancel,#basicComplete").on("click", function () {
    /*$("#searchCompanyBtn").hide();
     $("#searchOrgBtn").hide();*/
})


//初始化信息
$(function () {
    //编辑
    if (getURLParam("id") != null) {
        var dto = {
            id: getURLParam("id")
        };
        doJsonRequest("/rolePermissions/getInfo", dto, function (data) {
            if (data.result) {
                /*$("#id").val(data.data.id);
                 $("#roleName").val(data.data.roleName);
                 $("#roleCode").val(data.data.roleCode);
                 $(".roleNameS").html(data.data.roleName);
                 $(".roleCodeS").html(data.data.roleCode);*/
                var data = data.data;
                var rolePermissionsList = data["rolePermissionsList"];
                jqxTree(rolePermissionsList);
                var dataKey = [];
                for (var key in data) {
                    dataKey.push(key + "S");
                }
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
            } else {
                alert(22);
            }
        }, {showWaiting: true});
    } else {
        //新建
        $("#basicEdit").trigger("click");
        $("#basicDel").hide();
        doJsonRequest("/rolePermissions/getAllPermissions", null, function (data) {
        if (data.result) {
        jqxTree(data.data);
        }
        });
    }
})

//validate
$("#inputForm").validate({
    submitHandler: function (form) {
        finishEditText($(".edit_complete")[0], 'basic', '/rolePermissions/doSave', 'inputForm', function () {
            $("#searchCompanyBtn").hide();
            $("#searchOrgBtn").hide();
            window.location.href = "role_list.html";
        });
    }
});

function doEdit() {
var roleName1=$("#roleName").val();
var roleCode1=$("#roleCode").val();
if(roleName1==""||roleCode1==""){
$.alert("角色名称或角色编码不能为空!");
}else{
    //方式一
    //$("#inputForm").submit();
    //方式二
    var dat = {
  		roleName:roleName1,
  		roleCode:roleCode1,
  		id:$("#id").val()
   }
   doJsonRequest("/rolePermissions/doSave", dat, function(data) {
    	if(data.result){
    		$("#id").val(data.data.id);
    		$("#roleNameS").html(data.data.roleName);
    		$("#roleCodeS").html(data.data.roleCode);
    		$("#basicCancel").click();
    		$("#basicDel").show();
    	}
   },{showWaiting:true})
}
}

function doDelete() {
    $.confirm({
        title: '提示信息',
        msg: '您确定要移除么？<br/><font color=\'red\'>将冻结该角色下的所有账户！</font>',
        confirmClick: function () {
            var dto = {
              id:$("#id").val()
            };
            doJsonRequest("/rolePermissions/doDelete", dto, function (data) {
                if (data.result) {
                    history.go(-1)
                } else {

                }
            })
        }
    });
}
function jqxTree(rolePermissionsList) {
    // console.log(rolePermissionsList);
    var dom = d3.select("#jqxTree").style("display", 'block').select("ul");

    for (var i = 0; i < rolePermissionsList.length; i++) {
        var temp = rolePermissionsList[i];
        var li1 = dom.append("li").classed("li1", true).attr("roleId", temp["id"])
            .attr("code", temp["code"]).html(temp["name"]);
        var ul = li1.append("ul");
        if (!temp["children"].length) {
            var isRight = temp["isRight"];
            var str = "<img src =pic.png width='15'>&nbsp&nbsp" + temp["name"];
            if (isRight != "0") {
                str = "<img src =pic1.png width='15'>&nbsp&nbsp" + temp["name"];
                var li = ul.append("li").html(str).attr("roleId", temp["id"]).attr("code", temp["code"]).classed("select",true);
            }else{
                var li = ul.append("li").html(str).attr("roleId", temp["id"]).attr("code", temp["code"]);
            }

            li.on("click", function () {
                $(this).toggleClass("select");
                if ($(this).hasClass("select")) {
                    $(this).find("img").attr("src", "pic1.png");
                } else {
                    $(this).find("img").attr("src", "pic.png");
                }
            });
            continue;
        }
        for (var j = 0; j < temp["children"].length; j++) {
            var child = temp["children"][j];
            var isRight = child["isRight"];
            var str = "<img src =pic.png width='15'>&nbsp&nbsp" + child["name"];
            if (isRight != "0") {
                str = "<img src =pic1.png width='15'>&nbsp&nbsp" + child["name"];
                var li = ul.append("li").html(str).attr("roleId", child["id"]).attr("code", child["code"]).classed("select",true);
            }else{
                var li = ul.append("li").html(str).attr("roleId", child["id"]).attr("code", child["code"]);
            }

            li.on("click", function () {
                $(this).toggleClass("select");
                if ($(this).hasClass("select")) {
                    $(this).find("img").attr("src", "pic1.png");
                } else {
                    $(this).find("img").attr("src", "pic.png");
                }
            });
        }
    }
    $(".li1").each(function () {

        if ($(this).attr("expanded") != "true") {
        	// 默认显示
        	// $(this).find("ul").addClass("hide");
        }
        $(this).on("click", function (e) {
            var target = e.target;
            if (target.className && target.className == "li1")
                $(this).find("ul").toggleClass("hide");
        });
    });
}
function doComplete() {
var id1 =$("#id").val();
if(id1 != null&& id1 !=""){
    var arrayList = {
        id: id1,
        List: []
    };
    $(".li1 li").each(function () {
        if ($(this).hasClass("select")) {
            arrayList["List"].push($(this).attr("roleId"));
            arrayList["List"].push($(this).parent().parent().attr("roleId"));
        }

    });
    arrayList["List"] = unique(arrayList["List"]);
    console.log(arrayList);
    var url = "/rolePermissions/doSavePermissions"
    doJsonRequest(url, arrayList, function (data) {
        if (data.result) {
            var dat = data["data"]
            $.toast(dat["msg"]);
        }

    }, {
        showWaiting: true
    })
}else{
$.alert("请先提交基本信息！")}
}
function unique(arr) {
    var ret = []
    var hash = {}

    for (var i = 0; i < arr.length; i++) {
        var item = arr[i]
        var key = typeof(item) + item
        if (hash[key] !== 1) {
            ret.push(item)
            hash[key] = 1
        }
    }

    return ret
}
