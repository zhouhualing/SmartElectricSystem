//product 查看页面获取数据方法
$("#id").val(getURLParam("id"));
$(function () {
    var dto = {
        id: getURLParam("id")
    };
    doJsonRequest("/spmsProduct/getInfo", dto, function (data) {
        if (data.result) {
            var data = data.data;
            var dataKey = [];
            for (var key in data) {
                dataKey.push(key + "S");
            }

            $("div", $("fieldset")).each(function () {
                if ($.inArray($(this).attr("id"), dataKey) != -1) {

                    $(this).html(data[$(this).attr("id").substring(0, $(this).attr("id").length - 1)]);
                }
            })

            $("#userNameS").html("<a href='../spmsUser/spmsUserDetail.html?id=" + data.userId + "'>" + data.userName + "</a>");
            $("#spmsTypeNameS").html("<a href='spmsProductTypeDetail.html?id=" + data.spmsProductTypeDTO.id + "'>" + data.spmsTypeName + "</a>");
        } else {
            $.alert("信息获取失败");
        }
        var URL = "/spmsProduct/getProductDeviceList";
        var self = {
            id: getURLParam("id")
        }
        doJsonRequest(URL, self, function (dat) {
            console.log(dat);
            if (dat.result) {
                deviceList(dat["data"]);
            }
        }, {
            showWaiting: true
        })
    })

})

function deviceList(dat) {
    if (!dat || !dat.length) {
        return;
    }
    $("#deviceList").show();
    var listTable = d3.select("#deviceList").select("table").select("tbody");
    for (var i = 0; i < dat.length; i++) {
        var tr = listTable.append("tr");
        var typeText = dat[i]["typeText"] || "";
        var mac = dat[i]["mac"];
        var onOff = dat[i]["operStatus"] ? "在线" : "离线";
        var deviceId = dat[i]["id"];
        var type = dat[i]["type"];
        tr.append("td").append("a").attr("href", "../device/spmsDeviceUpdate.html?id=" + deviceId + "&type=" + type).html(typeText);
        tr.append("td").append("a").attr("href", "../device/spmsDeviceUpdate.html?id=" + deviceId + "&type=" + type).html(mac);
        tr.append("td").append("a").attr("href", "../device/spmsDeviceUpdate.html?id=" + deviceId + "&type=" + type).html(onOff);

    }
}
function doEdit() {

    finishEditText($(".edit_complete")[0], 'basic', '/user/editUser');


}
//产品查看页面 修改后保存方法
function sava() {
    $("#inputForm").submit();
}

$("#inputForm").validate({submitHandler: function () {
    var dto = {
        id: getURLParam("id"),
        spmsProductTypeDTO: {
            id: $("#spmsProductType").val()
        },
        status: $("#status").val(),
        spmsUserDTO: {
            id: $("#spmsUser").val()
        },
        subscribeDate: $("#subscribeDate").val(),
        activateDate: $("#activateDate").val(),
        expireDate: $("#expireDate").val(),
        initialCostRmb: $("#initialCostRmb").val(),
        electricityCostRmb: $("#electricityCostRmb").val(),
        spmsDeviceDTO: {
            id: $("#spmsDevice").val()
        }
    }
    doJsonRequest("/spmsProduct/doDLSave", dto, function (data) {
        if (data.result) {

            window.location.href = "spmsProductList.html";
        } else {

        }
    })
}});
//产品物理删除方法
function deleteDL() {
    var config = {
        msg: "您确定要删除么？",
        confirmClick: confirmClick
    }
    $.confirm(config);
    function confirmClick() {
        var dto = {
            id: getURLParam("id")
        };
        doJsonRequest("/spmsProduct/delete", dto, function (data) {
            if (data.result) {

                window.location.href = "spmsProductList.html";
            } else {
                alert("删除失败");
            }

        });
    }

}









