
var showObj1 = {
		text:"再次发送",
		fun:function(data){
			window.location.href = "accountBillDetail.html?id=" + data.id;
		}
};
var oppObj = [showObj1];
var producttypeinfo;
//为页面的id进行赋值。
var status;
$("#id").val(getURLParam("id"));
$("#spmsUserId").val(getURLParam("id"));
$("#userBasicInfo #basicEdit").on("click", function () {
    $("#eleAreaButton").show();
    $("#bizAreaButton").show();
})

$("#userBasicInfo #basicCancel").on("click", function () {
    $("#eleAreaButton").hide();
    $("#bizAreaButton").hide();
})

$("#bizAreaButton").on("click", function () {
    C_doAreaSelect({
        params: {
            classification: 1
        },
        myCallBack: function (data) {
            $("#bizAreaId").val(data.id);
            $("#bizAreaName").val(data.name);
            // doQuery();
        }
    })
})

$("#eleAreaButton").on("click", function () {
    C_doAreaSelect({
        params: {
            classification: 2
        },
        myCallBack: function (data) {
            $("#eleAreaId").val(data.id);
            $("#eleAreaName").val(data.name);
            // doQuery();
        }
    })
})

$(function () {
    // 为设备类型进行赋值
    var dto = {
        id: getURLParam("id")
    };
    doJsonRequest("/spmsUser/getInfo", dto, function (data) {
        if (data.result) {
            var data = data.data;
            $("#status").val(data.status);
            status=$("#status").val();
            if(status==2){
            $("#basicDel").attr("class","frozen_disable_ico");
            }
            var dataKey = [];
            for (var key in data) {
                dataKey.push(key + "S");
            }
            $("div", $("fieldset")).each(function () {
                if ($.inArray($(this).attr("id"), dataKey) != -1) {
                    var nowFieldId = $(this).attr("id").substring(0, $(this).attr("id").length - 1);
                    if ($("#" + nowFieldId).is(".the_select2")) {
                        $(this).html(data[(nowFieldId + "Text")]);
                        $(this).attr("eValue", data[nowFieldId]);
                        $("#" + nowFieldId).select2('val', 1);
                    } else {
                        $(this).html(data[nowFieldId]);
                    }
                }
            })
            $("#mac").val(data.mac);
            $("#macS").on("click", function () {
                window.location.href = "../device/spmsDeviceUpdate.html?id=" + data.spmsDevice + "&type=1";
            })
            if ($("#macS").html().length <= 0) {
                $("#gatewayForm #gatewayEdit").trigger("click");
            }
        } else {
            $.alert("信息获取失败");
        }
    }, {showWaiting: true});

    /*-----取得所有产品类型信息-------*/
    doJsonRequest("/spmsUser/getAllProductTypeInfo", null, function (data) {
        if (data.result) {
            producttypeinfo = data.data;
            for (var i = 0; i < data.data.length; i++) {
                $("#addproducttypeid").append("<option value='" + data.data[i].id + "'>" + data.data[i].names + "</option>");
            }
            $("#addproducttypeid").select2();
            $("#addproducttypeid").select("val", data.data[0].id);
            /*--为初始的添加产品框赋值--*/
            $("#addproductId").val(data.data[0].id);
            $("#addvalidPeriod").html(producttypeinfo[0].validPeriodText);
            $("#addmuluId").html(producttypeinfo[0].mulu);
            $("#addconfigurationInformation").html(producttypeinfo[0].config);
            $("#addareaName").html(producttypeinfo[0].areaName);
            if (producttypeinfo[0].lianDong == 0) {
                $("#addlianDong").html("允许");
            } else {
                $("#addlianDong").html("不允许");
            }
            $("#addkongTiaoCount").html(producttypeinfo[0].kongTiaoCount);
            $("#addchuangGanCount").html(producttypeinfo[0].chuangGanCount);
            $("#addcountRmb").html(producttypeinfo[0].countRmb);
            $("#addzhiLengMix").html(producttypeinfo[0].zhiLengMix);
            $("#addzhiLengMax").html(producttypeinfo[0].zhiLengMax);
            $("#addzhiReMix").html(producttypeinfo[0].zhiReMix);
            $("#addzhiReMax").html(producttypeinfo[0].zhiReMax);
            $("#adddescribes").html(producttypeinfo[0].describes);
        } else {
            $.alert("产品类型获取失败");
        }
    });


    doJsonRequest("/spmsUser/getUserProductBind", dto, function (data) {
        if (data.result) {

            for (var i = 0; i < data.data.length; i++) {
                var dto = {
                    productTypeName: "xxxxx",
                    bind: data.data[i]
                }
                newProduct(dto);
            }
        } else {
            $.alert("订户产品数据获取失败");
        }
    });

})
function queryEnd() {
	clickmedTables["reportInfoQuery"].hideFoot();
}

function doBind(userid, gwid) {
    var d = {
        userid: userid,
        gwid: gwid
    }

    doJsonRequest('/spmsUser/bindGetWay', d, function (data) {
        if (data.result == true) {
            if (data.data.success) {
                $("#gatewayEdit").show();
                $("#gatewayForm #gatewayComplete").hide();
                $("#gatewayForm #gatewayCancel").hide();
                $("#macS").show();
                $("#mac").hide();
                $("#macS").html(data.data.gwMac);
                $("#macS").attr("onclick", "location='../device/spmsDeviceUpdate.html?id=" + data.data.gwId + "&type=1'");
                $.alert(data.data.msg);
            } else {
                $.alert(data.data.msg);
                return;
            }
        } else {
            $.alert(data.data.msg);
            return;
        }
    });
}


function changeProperties(t) {
    var id = $(t).val();
    for (var i = 0; i < producttypeinfo.length; i++) {
        if (producttypeinfo[i].id == id) {

            $("#addproductId").val(id);
            $("#addvalidPeriod").html(producttypeinfo[i].validPeriodText);
            $("#addmuluId").html(producttypeinfo[i].mulu);
            $("#addconfigurationInformation").html(producttypeinfo[i].config);
            $("#addareaName").html(producttypeinfo[i].areaName);
            if (producttypeinfo[i].lianDong == 0) {
                $("#addlianDong").html("允许");
            } else {
                $("#addlianDong").html("不允许");
            }
            $("#addkongTiaoCount").html(producttypeinfo[i].kongTiaoCount);
            $("#addchuangGanCount").html(producttypeinfo[i].chuangGanCount);
            $("#addcountRmb").html(producttypeinfo[i].countRmb);
            $("#addzhiLengMix").html(producttypeinfo[i].zhiLengMix);
            $("#addzhiLengMax").html(producttypeinfo[i].zhiLengMax);
            $("#addzhiReMix").html(producttypeinfo[i].zhiReMix);
            $("#addzhiReMax").html(producttypeinfo[i].zhiReMax);
            $("#adddescribes").html(producttypeinfo[i].describes);
        }
    }
}

/**
 * 新增一个产品
 * @param t
 */
function saveProduct(t) {
    if (!$(t).parent().parent().parent().parent().valid()) {
        return false;
    }
    var devicearray = [];
    $(t).parent().parent().parent().parent().find("input[name=addMac]").each(function (i) {
        devicearray.push($(this).val());
    });
    var d = {
        userid: $("#id").val(),
        producttypeid: $("#addproductId").val(),
        subscribeDate: $("#addsubscribeDate").val(),
        devicemacs: devicearray

    };
    doJsonRequest("/spmsUser/bindProduct", d, function (data) {
        if (data.result) {
            if (data.data.success) {
                var trCount = $("tr", $("#addProductForm")).length;
                $("tr", $("#addProductForm")).each(function (i) {
                    if (i == 0) {

                    } else if (i == trCount - 1) {

                    } else {
                        $(this).remove();
                    }
                })
                $("#addProductForm")[0].reset();
                $("#addProductForm").hide();
                newProduct(data.data);
            } else {
                $.alert(data.data.message);
            }
        } else {
            alert("error");
        }
    });
}

/**
 * 初始化产品信息
 * @param dat
 */
function newProduct(dat) {
    var turn = {
        productTypeName: "产品类型",
        validPeriod: "产品期限",
        mulu: "产品目录",
        config: "产品配置",
        subscribeDate: "产品订阅时间",
        area: "产品区域",
        lianDong: "传感器联动",
        acMax: "空调最大绑定数",
        sensorMax: "传感器最大绑定",
        countRmb: "产品总费用",
        id: "不显示(产品的ID)",
        isActivation: "产品状态",
        productTypeId: "不显示（产品类型ID）",
        remarks: "备注",
        rmbType: "付费类型"
    };
    //绑定完的数据
    var data = dat["bind"];
    var newproduct = $("#newproduct").css({
        "margin-top": "20px"
    });
    var productTypeName = data["productTypeName"] || "";
    var form = $("<form></form>").addClass("form-horizontal").css({
        "margin": "0"
    });
    var fieldset = $("<fieldset></fieldset>");
    form.append(fieldset);
    var legend = '<legend>' + productTypeName + '<span><input class="edit_cancel" name="" style="display:none" id="basicCancel" type="button" onclick="cancelContol()"></span></legend>';
    fieldset.append(legend);
    for (var key in turn) {
        if (turn[key].indexOf("不显示") == -1) {
            var control = $("<div></div>").addClass("control-group");
            control.append($("<label></label>").addClass("control-label").attr("id", "fullnameS").html(turn[key] + ":"));
            control.append($("<div></div>").addClass("controls").append($("<div></div>").attr("leixing", key).addClass("input_view").html(data[key])));
            fieldset.append(control)
        }
    }
    newproduct.append(form);
    var spmsDevices = data["spmsDevices"];
    //  spmsDevices = ["12", "13"];
    var table = $("<table>").attr("id", "contentTable1").addClass("table table-striped  table-condensed table_s");
    var tbody = $("<tbody></tbody>");
    var tr = $("<tr>").html("<th width='30.3%'>设备类型</th><th width='30.3%'>设备MAC</th><th align='left'></th>");
    table.append(tbody);
    tbody.append(tr);
    table.attr("productId", data.productId);
    if (spmsDevices && spmsDevices.length) {
        for (var j = 0; j < spmsDevices.length; j++) {
            console.log(spmsDevices[j])
            var tr = $("<tr devicetype='" + spmsDevices[j]["type"] + "'>");
            var td = $("<td>");
            td.append($("<a>").attr("href","../device/spmsDeviceUpdate.html?id="+spmsDevices[j]["id"]+"&type="+spmsDevices[j]["type"])
                .html("<span>" + spmsDevices[j]["typeText"] + "</span>"));

            var select = $("<select>").attr("name", "updateType").attr("ishidden", true)
                .attr("tabindex", -1).addClass("select2-offscreen").css("display", "none");
            //
            select.append("<option value='2'>智能空调")
            select.append("<option value='3'>门窗传感器");
            select.select(2);
            td.append(select);
            tr.append(td);
            var td = ($("<td>"))
            td.append($("<a>").attr("href","../device/spmsDeviceUpdate.html?id="+spmsDevices[j]["id"]+"&type="+spmsDevices[j]["type"])
                .html("<span>" + spmsDevices[j].mac + "</span>"));

            td.append($("<input>").attr("type", "hidden").attr("name", "oldMac").attr("value", spmsDevices[j].mac))
            td.append($("<input>").attr("type", "text").attr("name", "addMac").attr("value", spmsDevices[j].mac).css("display", "none"));
            tr.append(td);
            var td = $("<td>");
            td.append($("<a href='javascript:void(0)' onclick='editDeviceMac(this)' style='display: inline'>").html('<i class="edit_ico"></i>编辑设备'));
            td.append($("<a href='javascript:void(0)' onclick='deleteDevice(this)' style='display: inline'>").html('<i class="delete_ico"></i>回收设备'));
            td.append("<input class='edit_complete' name='' style='display:none;'  type='button' onclick='finishDeviceMac(this)'/>");
            td.append("<input class='edit_cancel' name='' style='display:none;'  type='button' onclick='cancleDeviceMac(this)'/>")
            tr.append(td);
            tbody.append(tr);

        }
    }

    newproduct.append(table);
    var newServiceDeviceTable = ServiceDeviceTable();
    tbody.append(newServiceDeviceTable.show());

}
function ServiceDeviceTable() {
    //var newServiceDeviceTable = $("<div>").addClass("newServiceDeviceTable")
    //    .css({
    //        width: "100%",
    //        "margin-bottom": "20px"
    //    });
    //var table = $("<table>").addClass("table table-striped  table-condensed table_s");
    var tr = $("<tr>");
    var td = $("<td width='30.33%'>");
    var select = $("<select style='height: 30px'>").attr("name", "deviceType").attr("id", "deviceType").addClass("the_select2");
    select.append($("<option>").attr("value", 2).html("智能空调"));
    select.append($("<option>").attr("value", 3).html("门窗传感器"));
    td.append(select);
    tr.append(td);
    var td = $("<td width='30.33%'>");
    td.append($("<input>").attr("name", "deviceMac").attr("id", "deviceMac").attr("type", "text").attr("placeholder", "请输入mac地址"));
    td.append($("<span>").attr("name", "errorInfo"));
    tr.append(td);
    var td = $("<td>");
    td.append($("<input onclick='bindingDevice(this)'>").attr("id", "addDevice").addClass("btn btn-primary").attr("type", "button").attr("value", "添加设备"));
    tr.append(td);
    //newServiceDeviceTable.append(table.append(tr));
    select.select2();
    return tr;
}
//如果id部位
function bindingDevice(_this) {
	if(status != 0){
		$.alert("用户已冻结不能对其设备进行操作");
        return;
	}
    var acmax = $("[leixing='acMax']", $(_this).parent().parent().parent().parent().parent()).html();
    var cgmax = $("[leixing='sensorMax']", $(_this).parent().parent().parent().parent().parent()).html();
    //alert("acmax"+acmax+"  cgmax"+cgmax);
    //总设备数
    var num = ($(_this).parent().parent().parent().children()).length - 2;
    //现有设备数
    var acnum = ($(_this).parent().parent().parent().find("tr[devicetype='2']")).length;
    var cgnum = ($(_this).parent().parent().parent().find("tr[devicetype='3']")).length;
    //alert("acnum"+acnum+"  cgnum"+cgnum);

    var dt = $("[name='deviceType']", $(_this).parent().parent()).val();
    //alert(dt);
    if (dt == 2) {
        if (acnum + 1 > acmax) {
            $.alert("请勿超出空调最大绑定数");
            return;
        }
    } else if (dt == 3) {
        if (cgnum + 1 > cgmax) {
            $.alert("请勿超出门窗感最大绑定数");
            return;
        }
    }
    dtype= $("[name='deviceType']", $(_this).parent().parent()).val();
    var d = {
        productId: $(_this).parents(".table-condensed").attr("productId"),
        devicemac: $("[name='deviceMac']", $(_this).parent().parent()).val(),
        devicetype: $("[name='deviceType']", $(_this).parent().parent()).val()
    };
    doRequest("/spmsUser/addDeviceToProduct", d, function (data) {
        if (data.result) {
            if (data.data.success) {
                $("tr", $(_this).parent().parent().parent()).eq($("tr", $(_this).parent().parent().parent()).length - 2).after("<tr devicetype='"+dtype+"'>" +
                    "<td>" +
                    "<a id='updateTypeurl' href='javascript:void(0)' onclick=\"location=\'../device/spmsDeviceUpdate.html?id=" + data.data.deviceid + "&type=" + data.data.devicetype + "\'\">" + (data.data.devicetype == 2 ? '智能空调' : '门窗传感器') + "</a>" +
                    "<select id='updateType' name='updateType' style='display:none'  class='the_select2'><option value='2'>智能空调</option><option value='3'>门窗传感器</option></select>" +
                    "</td>" +
                    "<td>" +
                    "<a href='javascript:void(0)' onclick=\"location=\'../device/spmsDeviceUpdate.html?id=" + data.data.deviceid + "&type=" + data.data.devicetype + "\'\">" + data.data.devicemac + "</a>" +
                    "<input type='hidden' name='oldMac' value='" + data.data.devicemac + "'/>" +
                    "<input type='text' name='addMac' style='display:none;' value='" + data.data.devicemac + "'/>" +
                    "</td>" +

                    "<td>" +
                    "<a href='javascript:void(0)' onclick='editDeviceMac(this)'><i class='edit_ico'></i>编辑设备</a>" +
                    "<a href='javascript:void(0)'  onclick='deleteDevice(this)'><i class='delete_ico'></i>回收设备</a>" +
                    "<input class='edit_complete' name='' style='display:none;'  type='button' onclick='reBindDevice(this)'/>" +
                    "<input class='edit_cancel' name='' style='display:none;'  type='button' onclick='cancleDeviceMac(this)'/>" +
                    "</td>" +
                    "</tr>");
                $(".the_select2").find('select[name="updateType"]').eq(0).select2();
                $("#addServiceDeviceTable", $(_this).parent().parent().parent().parent()).find('tr:eq(1) td:eq(0) select[name="updateType"]').select2('val', data.data.devicetype);
                $("[name='deviceMac']", $(_this).parent().parent()).val("").blur();
            } else {
                $.alert(data.data.msg);
                $("[name='deviceMac']", $(_this).parent().parent()).val("").blur();
            }
        } else {
            $.alert("添加设备失败");
        }
    });
}
function cancleDeviceMac(opt) {
    //提交取消按钮
    $(opt).hide();
    $(opt).siblings('.edit_complete').hide();
    //表格第一列
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').show();
    $(opt).parents('tr').find('td:eq(1) input[name="addMac"]').val($(opt).parents('tr').find('td:eq(1)').find("input[name=oldMac]").val());
    if ($(opt).parents('tr').find('td:eq(0) a:eq(0)').next().next()[0].nodeName == "SELECT") {
    	$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().next().children().each(function(){
    	if($(this).text()==$(opt).parents('tr').find('td:eq(0) a:eq(0)').text()){
    		$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().val($(this).val());
    		}
    	});
        //$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().next().select2();
    }
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').next().remove();
    
    //表格第二列
    $(opt).parents('tr').find('td:eq(1) input[name="addMac"]').hide();
    $(opt).parents('tr').find('td:eq(1) a:eq(0)').show();
    //表格第三列
    $(opt).parents('tr').find('td:eq(2) a').show();
}
function finishDeviceMac(opt) {
    var deviceName ={
        "2":"智能空调",
        "3":"门窗传感器"
    }
    $(opt).hide();
    $(opt).siblings('.edit_cancel').hide();
    $(opt).parents('tr').find('td:eq(2) a').show();
    //返回数据显示
    //表格第一列
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').show();
   /* if ($(opt).parents('tr').find('td:eq(0) a:eq(0)').next().next()[0].nodeName == "SELECT") {
    	$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().next().children().each(function(){
    	if($(this).text()==$(opt).parents('tr').find('td:eq(0) a:eq(0)').text()){
    		$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().val($(this).val());
    		}
    	});
        //$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().next().select2();
    }*/
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').next().hide();
    //表格第二列
    $(opt).parents('tr').find('td:eq(1) input[name="addMac"]').hide();
    $(opt).parents('tr').find('td:eq(1) a:eq(0)').show();
	//$(opt).parents('tr').find('td:eq(1) input[name="addMac"]').val($(opt).parents('tr').find('td:eq(1)').find("input[name=oldMac]").val());
    //表格第一列
    //$(opt).parents('tr').find('td:eq(0) a:eq(0)').text(deviceName[$(opt).parents('tr').find("select").val()]);
    //$(opt).parents('tr').find('td:eq(0) select[name="updateType"]');
    //表格第二列
    //$(opt).parents('tr').find('td:eq(1) a:eq(0)').text( $(opt).parents('tr').find('td:eq(1)').find("input[name=newMac]").val());

    reBindDevice($(opt));
}



function editDeviceMac(opt) {
	if(status != 0){
		$.alert("用户已冻结不能对其设备进行操作");
        return;
	}
    // 表格第一列
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').hide();
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').next().css("width","92px");
    $(opt).parents('tr').find('td:eq(0) a:eq(0)').next().show();
    if ($(opt).parents('tr').find('td:eq(0) a:eq(0)').next()[0].nodeName == "SELECT") {
    	$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().children().each(function(){
    	if($(this).text()==$(opt).parents('tr').find('td:eq(0) a:eq(0)').text()){
    		$(opt).parents('tr').find('td:eq(0) a:eq(0)').next().val($(this).val());
    		}
    	});
        $(opt).parents('tr').find('td:eq(0) a:eq(0)').next().select2();
    }
    //表格第二列
    $(opt).parents('tr').find('td:eq(1) input[name="addMac"]').show();
    $(opt).parents('tr').find('td:eq(1) a:eq(0)').hide();
    //提交取消按钮
    $(opt).siblings('.edit_complete').show();
    $(opt).siblings('.edit_cancel').show();
    //编辑删除按钮
    $(opt).hide();
    $(opt).next().hide();
}
//回收设备
function deleteDevice(opt) {
	if(status != 0){
		$.alert("用户已冻结不能对其设备进行操作");
        return;
	}
    var mac = (($(opt).parent().parent().children()).eq(1).children()).eq(0).text();
    var config = {
        msg: "您确定要回收该设备吗？",
        confirmClick: function () {
            doRequest("/spmsUser/cancelDeviceBind", {mac: mac}, function (data) {
                if (data.result) {
                    window.location.href = "spmsUserDetail.html?id=" + $("#id").val();
                } else {

                }
            }, {showWaiting: true, successInfo: '回收成功', failtureInfo: '回收失败'})
        }
    }
    $.confirm(config);
}
//编辑设备
function reBindDevice(opt) {
	if(status != 0){
		$.alert("用户已冻结不能对其设备进行操作");
        return;
	}
    var oldMac = (($(opt).parent().parent().children()).eq(1).children()).eq(1).val();
    var newMac = (($(opt).parent().parent().children()).eq(1).children()).eq(2).val();
    var dtype=(($(opt).parent().parent().children()).eq(0).children()).eq(2).val();
    var productId = $(opt).parents("table:first").attr("productid");
    var config = {
        msg: "您确定要更换设备mac:"+oldMac+"吗？",
        confirmClick: function () {
            doRequest("/spmsUser/reBindDevice1", {oldMac: oldMac, newMac: newMac,dtype: dtype, productId: productId}, function (data) {
                if (data.data) {
                	if(data.data.success){
                   window.location.href = "spmsUserDetail.html?id=" + $("#id").val();
                    }else{
                    $.alert(data.data.msg);
                    }
                } else {
					
                }
            }, {showWaiting: true, successInfo: '回收成功', failtureInfo: '回收失败'})
        }
    }
    $.confirm(config);
}

function doDelete() {
	var mes ="您确定要冻结么？";
	if(status != 0){
		mes ="您确定要解冻么？"
	}
    var config = {
        msg: mes,
        confirmClick: function () {
            var id = getURLParam("id");
            doJsonRequest("/spmsUser/doDel", {ids: id}, function (data) {
                if (data.result) {
                    window.location.href = "spmsUserList.html"
                } else {

                }
            }, {showWaiting: true, successInfo: '冻结成功', failtureInfo: '冻结失败'})
        }
    }
    $.confirm(config);
}
function doEdit() {
	if(!$("#userBasicInfo").valid()){
		$.alert("请完善必填信息");
		return false;
	}
    var dat = {
        userid: getURLParam("id"),
        mobile: $("#mobile").val(),
        email:$("#email").val()
    };

    doJsonRequest('/spmsUser/ValidationMobileAndEmail', dat, function (data) {
        if (data.result) {
            if (data.data.success) {
               finishEditText($("[name='edit_complete1']")[0], 'basic', '/spmsUser/doSave');
               $("#eleAreaButton").hide();
    		   $("#bizAreaButton").hide();
            } else {
                $.alert(data.data.msg);
            }
        } else {
            $.alert("保存失败");
        }
    });
}

function deleteSpmsUser() {
    var dto = {'ids': getURLParam("id")};
    doRequest('/spmsUser/doDel', dto, function (data) {
        if (data.result == true) {
            alert("删除成功!");
            window.location.href = "spmsUserList.html";
        } else {
            alert("删除失败！");
        }
    });
}


function bingGw(t) {
	if(status != 0){
		$.alert("用户已冻结不能对其网关进行操作");
        return;
	}

    if ($("#id").val().length <= 0) {
        $.alert("请先保存订户信息");
    }
//    if ($("#mac").val().length <= 0) {
//        $.alert("请输入正确的网关MAC信息");
//        return;
//    } else {
        doBind($("#id").val(), $("#mac").val());
//    }
}

function addProductForm(t) {
	if(status!=0){
		$.alert("用户已冻结不能添加产品");
        return;
	}
    if ($("#id").val().length <= 0) {
        $.alert("请先保存订户信息");
        return;
    }
    var macS=$("#macS").html();
    var gwMac = $("#mac").val();
    if (gwMac == "" || gwMac == null || typeof(gwMac) == "undefined"|| macS == "" || macS == null || typeof(macS) == "undefined") {
        $.alert("请先添加绑定网关");
        return;
    }

    $('#addProductForm').show();
}
function rowClick(data, tr) {
    window.location.href = "accountBillDetail.html?id=" + data.id;

}
