function zTreeOnClick(event, treeId, treeNode) {
    tempTreeId = treeNode.id;
};

/**
 * 加载区域树
 * @param content
 */
function loadAreaTree(content, datas) {
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        }, callback: {
            onClick: zTreeOnClick
        }
    };

    /*var zNodes =[
     { id:1, pId:0, name:"展开、折叠 自定义图标不同", open:true, iconSkin:"pIcon01"},
     { id:11, pId:1, name:"叶子节点1", iconSkin:"icon01"},
     { id:12, pId:1, name:"叶子节点2", iconSkin:"icon02"},
     { id:13, pId:1, name:"叶子节点3", iconSkin:"icon03"},
     { id:2, pId:0, name:"展开、折叠 自定义图标相同", open:true, iconSkin:"pIcon02"},
     { id:21, pId:2, name:"叶子节点1", iconSkin:"icon04"},
     { id:22, pId:2, name:"叶子节点2", iconSkin:"icon05"},
     { id:23, pId:2, name:"叶子节点3", iconSkin:"icon06"},
     { id:3, pId:0, name:"不使用自定义图标", open:true },
     { id:31, pId:3, name:"叶子节点1"},
     { id:32, pId:3, name:"叶子节点2"},
     { id:33, pId:3, name:"叶子节点3"}
     ];*/
    if(datas!=null && datas.length>0 && !tempTreeId){
    	tempTreeId=datas[0].id;
    	localStorage["tempTreeId"]=tempTreeId;
    }
    var zNodes = [];
    for (var i = 0; i < datas.length; i++) {
        var data = datas[i];       
        localStorage[data.id] = data.name;
        var status = data.status;
        if (status > 0) {
            if (data.children && data.children.length > 0) {
                zNodes.push({
                    id: data.id,
                    pId: data.parentId,
                    name: data.name,
                    url: _ctx + "areaData.html?areaId=" + data.id,
                    target: "dataFrame",
                    open: true,
                    iconSkin: "pIcon01"
                });
            } else {
                zNodes.push({
                    id: data.id,
                    pId: data.parentId,
                    name: data.name,
                    url: _ctx + "areaData.html?areaId=" + data.id,
                    target: "dataFrame",
                    iconSkin: "icon01"
                });
            }
        } else {
            if (data.children && data.children.length > 0) {
                zNodes.push({
                    id: data.id,
                    pId: data.parentId,
                    name: data.name,
                    url: _ctx + "areaData.html?areaId=" + data.id,
                    target: "dataFrame",
                    open: true,
                    iconSkin: "pIcon02"
                });
            } else {
                zNodes.push({
                    id: data.id,
                    pId: data.parentId,
                    name: data.name,
                    url: _ctx + "areaData.html?areaId=" + data.id,
                    target: "dataFrame",
                    iconSkin: "icon02"
                });
            }
        }

    }
 //   console.log(zNodes);
    var zTreeObj = $.fn.zTree.init($("#" + content), setting, zNodes);

    if (!tempTreeId) {
        var node = zTreeObj.getNodeByParam("id", '11', null);
        zTreeObj.selectNode(node, false);
    } else {
        var node = zTreeObj.getNodeByParam("id", tempTreeId, null);
        zTreeObj.selectNode(node, false);
        parent.document.dataFrame.document.location.href="areaData.html?areaId="+tempTreeId;
    }
}

var tempTreeId;
var tempPId;
var refresh1 = null;
$(function () {
    //var areaTreeData = [
    //    {"id": "12", "name": "北配区一", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {
    //        "id": "11", "name": "北京配电区(1/313)", "parentId": "1", "childList": [
    //        {"id": "12", "name": "北配区一", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "13", "name": "北配区二(0/304)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "14", "name": "北配区三(0/300)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "15", "name": "北配区四(0/322)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "16", "name": "北配区五(0/305)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "17", "name": "北配区六(0/302)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "18", "name": "北配区七(0/297)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //        {"id": "19", "name": "北配区八(0/402)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}}
    //    ], "attrs": {"maxLoad": 1.0, "status": 0}
    //    },
    //    {"id": "13", "name": "北配区二(0/304)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {"id": "14", "name": "北配区三(0/300)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {"id": "15", "name": "北配区四(0/322)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {"id": "16", "name": "北配区五(0/305)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {"id": "17", "name": "北配区六(0/302)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {"id": "18", "name": "北配区七(0/297)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}},
    //    {"id": "19", "name": "北配区八(0/402)", "parentId": "11", "attrs": {"maxLoad": 0.0, "status": 0}}
    //];
    // loadAreaTree("areaTree", areaTreeData);

    var URL = "/electro/areaTree";
    doJsonRequest(URL, null, function (dat) {
        console.log(dat);
       // refresh1 = setTimeout("updateAreaTree()", 5000);
        if (dat.result) {
            areaTreeData = dat.data;
            loadAreaTree("areaTree", areaTreeData);
        }
    }, function () {
        console.log("获取数据失败")
    });

});

//定时刷新
function updateAreaTree() {
    clearTimeout(refresh1);
    doJsonRequest("/electro/updateAreaTree", null, function (data) {
       // refresh = setTimeout("updateAreaTree()", 5000);
        if (data.result) {
            	console.log("//****树刷新******//");
      //      console.log(data.data.data);
            //console.log("//****END******//");
            loadAreaTree("areaTree", data.data.data);

        } else {
            console.log("树刷新出错了~");
        }

    });
};
var leftWidth = "160"; // 左侧窗口大小
$(function () {
    if (!Array.prototype.map) {
        Array.prototype.map = function (fn, scope) {
            var result = [], ri = 0;
            for (var i = 0, n = this.length; i < n; i++) {
                if (i in this) {
                    result[ri++] = fn.call(scope, this[i], i, this);
                }
            }
            return result;
        };
    }
    var getWindowSize = function () {
        return ["Height", "Width"].map(function (name) {
            return window["inner" + name] ||
                document.compatMode === "CSS1Compat" && document.documentElement[ "client" + name ] || document.body[ "client" + name ];
        });
    };

    function wSize() {
        var minHeight = 500, minWidth = 980;
        var strs = getWindowSize().toString().split(",");
        $(".accordion-inner").height((strs[0] < minHeight ? minHeight : strs[0]) - $(".accordion-heading").height() - 10);
    }

    $(window).resize(function () {
        wSize();
    });
    wSize();
});
