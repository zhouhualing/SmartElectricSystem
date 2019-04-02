

var type = 1;
if (getURLParam("type") != null) {
    type = getURLParam("type");
}

var title = getDeviceTitle(type);//TODO add by ZHL
$("#the_name").html(title);
/*
if(type == 1) {
    $("#the_name").html("网关列表");
} else if (type==2) {
    $("#the_name").html("空调列表");
} else if (type==3){
    $("#the_name").html("门窗传感器");
} else if(type==4){
	$("#the_name").html("智能插座");
}
*/
/**
 * 表格操作项
 */
var oppObj = [];


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
    doReQuery(type);
    //select2组件注册
    $(".the_select2").addChangeListener(function () {
        doQuery();
    })

    $("#checkAll").on('click', function () {
        $("[name=checkId]:checkbox").attr("checked", this.checked);
    });

    $("#delBtn").click(function () {
        var checkIds = $("[name=checkId]:checked");
        if (checkIds.length == 0) {
            alert("请至少选择一条数据");
            return;
        }
        var ids = [];
        checkIds.each(function (index, e) {
            ids.push($(e).val());
        });
        confirmx('确认要删除所选条目吗？', "/spms/spmsDevice/doDel?id=" + ids.join(','));
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
    if (type == 1) {
        $(".hm_table_title").find("h2").html("网关列表");
    }
    if (type == 2) {
        $(".hm_table_title").find("h2").html("空调列表");
    }
    if (type == 3) {
        $(".hm_table_title").find("h2").html("门窗传感器列表");
    }
    if (type == 4){
    	$(".hm_table_title").find("h2").html("智能插座列表");
    }
}

/**
 * table组件，当row被点击时执行操作
 * @param a
 * @param b
 */
function rowClick(data, tr) {
    window.location.href = "spmsDeviceUpdate.html?id=" + data.id + "&type=" + data.type;
}

function createFun() {
    window.location.href = "spmsDeviceForm.html?type=" + $("#type").val();
}

function importFun(){
	var obj = {
		title : '选择文件进行导入',
		height : "320px",
		width : "600px",
		url : '../device/spmsDeviceImport.html?type='+type,
		confirmBtn : false,
		cancelText : "关闭",
		myCallBack : function(){
			
		}
	}
	new jqueryDialog(obj);
}

function deleteFun(data, data1) {
    if(data.length <= 0) {
        $.alert("请至少选择一个设备。");
        return false;
    }
    for(var i=0; i<data1.length; i++) {
        if(data1[i].status==2 ) {
            $.alert("mac地址为："+data1[i].mac+"的设备为正常运营状态，不可删除。");
            return false;
        } else if (data[i].status==3) {
            $.alert("mac地址为："+data1[i].mac+"的设备为维修状态，不可删除。");
            return false;
        }
    }
    var config = {
        msg: "您确定要删除么？",
        confirmClick: function () {
            var dto = {'ids': data + ""};
            doRequest('/spmsDevice/doDel', dto, function (data) {
                doQuery();
                if (data.result == true) {
                } else {
                }
            }, {showWaiting: true, successInfo: '删除成功', failtureInfo: '删除失败'});
        }
    }
    $.confirm(config);
}
