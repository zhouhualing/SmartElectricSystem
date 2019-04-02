/**
 * Created by Administrator on 2015/1/9.
 */
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
    type: "类型",
    createDate: "创建时间",
    companyName: "所在公司",
    id:"用户ID",
    loginIp: "登录ip"
};
var getPro = function (key, obj) {
    for (var index in obj) {
        if (index == key) {
            return true;
        }
    }
    return false;
}
$(function () {
    var id = getURLParam("id");
    console.log(id)
    doJsonRequest("/spmsLog/getDetail", parseInt(id), function (dat) {
        console.log(dat);
        if (dat.result) {

            var config = {
                height: 400,
                title: "日志详细信息"
            };
            var ml = new harmazing.TanChuK(config);
            var reDiv = ml.init();

            var table = $("<ul></ul>").css({
                width: "100%",
                overflow: "hidden"
            })
            reDiv.append(table)

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
                            continue;
                            var orgDTO = dat["data"][index]["orgDTO"];
                            for (var of in orgDTO) {
                                if (!orgDTO[of]) {
                                    continue;
                                }
                                if (!getPro(of, trun) && !getPro(of, createBy)) {
                                    continue;
                                }
                                var controlGroup = $("<li class='control-group'></li>")


                                var label = $("<label class='control-label'></label>").html(of + ":");
                                var controls = $("<div class='controls'></div>").html("<div class='input_view'>" + orgDTO[of] + "</div>");
                                controlGroup.append(label).append(controls);
                                table.append(controlGroup);
                                j++;
                            }

                            continue;
                        }
                        if (!getPro(name, trun) && !getPro(name, createBy)) {
                            continue;
                        }
                        var controlGroup = $("<li class='control-group'></li>")
                        var label = $("<label class='control-label'></label>").html((createBy[name] || name) + ":");
                        var controls = $("<div class='controls'></div>").html("<div class='input_view'>" + dat["data"][index][name] + "</div>");

                        controlGroup.append(label).append(controls);
                        table.append(controlGroup);
                        j++;
                    }
                    continue;
                }
                if (!getPro(index, trun) && !getPro(index, createBy)) {
                    continue;
                }
                var controlGroup = $("<li class='control-group'></li>")
                var label = $("<label class='control-label'></label>").html((trun[index] || index) + ":");
                var controls = $("<div class='controls'></div>").html("<div class='input_view'>" + dat["data"][index] + "</div>");

                controlGroup.append(label).append(controls);
                table.append(controlGroup);
                j++;
            }

        //    new iScroll("easyDialog_text", {hScrollbar: false, vScrollbar: false});
        }
    }, function () {
        $.alert("获取数据失败");
    });
});