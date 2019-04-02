/**
 * 表格操作项
 */
var oppObj = [];
var trun = {
    exception: "异常",
    method: "请求方式",
    params: "请求参数",
    remoteAddr: "请求地址",
    requestUri: "请求URL",
    userAgent: "所在公司",
    type: "类型",
    createDate: "创建时间",
    id: "事件ID"

};
var createBy = {
    userType: "用户类型",
    userCode: "账户",
    createDate: "创建时间",
    companyName: "所在公司",
    loginIp: "登录ip"
};
function testAlert() {
    var str = "checkType=multi";

    var obj = {
        title: '流程信息',
        height: "320px",
        width: "1050px",
        url: 'follow_role_dialog.html',
        fun: true,
        myCallBack: function () {

        }
    }
    nowSearchRole = new jqueryDialog(obj);
    $(".dialog_div_").parent().addClass("wf_top");
}


/**
 * ready
 */
$(function () {
    //select2组件注册
    $(".action_button").hide();
    $(".the_select2").addChangeListener(function () {
        doQuery();
    })

    $("#checkAll").on('click', function () {
        $("[name=checkId]:checkbox").attr("checked", this.checked);
    });

})

/**
 * 点击tab栏，动态过滤设备类型信息
 * @param type
 */
function doReQuery(type) {
    $("#type").val(type);
    $(".active").removeClass("active");
    $("li:even", $(".nav")).eq(type - 1).attr("class", "active");
    doQuery();
}

/**
 * table组件，当row被点击时执行操作
 * @param a
 * @param b
 */
function rowClick(data) {
    console.log((data));
    if (data && data["id"]) {
        var id = data["id"];
        location.href = "detail.html?id=" + id;
        return;
        doJsonRequest("/spmsLog/getDetail", id, function (dat) {
            console.log(dat);
            if (dat.result) {

                var config = {

                    title: "日志详细信息"
                };
                var ml = new harmazing.TanChuK(config);
                var reDiv = ml.init();

                var div = $("<div></div>").css({
                    width: "100%",

                    overflow: "hidden"
                })
                reDiv.append(div)
                var table = $("<table></table>").css({
                    width: "100%"
                });
                var j = 0;
                for (var index in dat["data"]) {
                    if (!dat["data"][index]) {
                        continue
                    }

                    if (index.indexOf("createBy") > -1) {
                        for (var name in dat["data"][index]) {
                            if (!dat["data"][index][name]) {
                                continue
                            }
                            if (name.indexOf("orgDTO") > -1) {
                                var orgDTO = dat["data"][index]["orgDTO"];
                                for (var of in orgDTO) {
                                    if (!orgDTO[of]) {
                                        continue;
                                    }
                                    if (j % 3 == 0) {
                                        var tr = $("<tr></tr>");
                                    }

                                    var td = $("<td></td>").css("width", "14%").html(of);
                                    var td1 = $("<td></td>").css("width", "19.33%").html(orgDTO[of]);
                                    tr.append(td).append(td1);
                                    table.append(tr);
                                    j++;
                                }

                                continue;
                            }

                            if (j % 3 == 0) {
                                var tr = $("<tr></tr>");
                            }
                            var td = $("<td></td>").css("width", "14%").html(createBy[name] || name);
                            var td1 = $("<td></td>").css("width", "19.33%").html(dat["data"][index][name]);
                            tr.append(td).append(td1);
                            table.append(tr);
                            j++;
                        }
                        continue;
                    }
                    if (j % 3 == 0) {
                        var tr = $("<tr></tr>");
                    }
                    var td = $("<td></td>").css("width", "14%").html(trun[index] || index);
                    var td1 = $("<td></td>").css("width", "19.33").html(dat["data"][index]);
                    tr.append(td).append(td1);
                    table.append(tr);
                    j++;
                }
                div.append(table);
                new iScroll("easyDialog_text", {hScrollbar: false, vScrollbar: false});
            }
        }, function () {
            $.alert("获取数据失败");
        });
    }


}


