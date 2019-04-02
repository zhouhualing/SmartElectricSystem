if(getURLParam("classification") == 1) {
    $("#the_name").html("服务区域列表");
} else {
    $("#the_name").html("用电区域列表");
}
/**
 * 表格操作项
 */
var showObj1 = {
		text:"查看",
		fun:function(data){
			window.location.href = "areaShow.html?id="+data.id+"&classification="+classification;
		}
}

var showObj2 = {
		text:"删除",
		fun:function(data){
			$.confirm({
				title: '提示信息',
				msg: '确定要删除么？',
				height: 180,
				confirmClick: function(){
					var info ={
						id:data.id
					}
					doJsonRequest("/area/deleteById", info, function(data) {
						if (data.result) {
							if(data.data.success){
								doReQuery(classification);
							}else{
								$.alert(data.data.msg);
							}

						} else {
							$.alert("操作失败");
						}
					})
				}
			})
		}
}

var showObj3 = {
		text:"添加下级子区域",
		fun:function(data){
			if(data.type != 4){
				window.location.href = "areaForm.html?parentname="+data.name+"&parentid="+data.id+"&classification="+data.classification+"&parentType="+data.type;
			}else{
				$.alert("该区域无法添加下级区域");
			}
		}
}

var showObj4 = {
		text:"预警设置",
		fun:function(data){
			window.location.href = "../device/warning_setting_detail.html?id="+data.id+"&classification="+classification;
		}
}


var oppObj = [showObj1,showObj2,showObj3,showObj4];


function initFun(data,key) {
	if(key == "code") {
		if(classification == 1) {
			clickmedTables.reportInfoQuery.operatorArr[3].text="";
		} else {
			clickmedTables.reportInfoQuery.operatorArr[3].text="预警设置";
		}
		
	} 
}

var classification = 1;
/**
 * 处理url参数
 */


/**
 * ready
 */
$(function(){
	if(getURLParam("classification") != null) {
		classification = getURLParam("classification");
		doReQuery(classification)
	} else {
		doQuery()
	}
	
	//select2组件注册
	$(".the_select2").addChangeListener(function(){
		doQuery();
	})
	
    $("#checkAll").on('click',function(){
        $("[name=checkId]:checkbox").attr("checked",this.checked);
    });

    $("#delBtn").click(function(){
        var checkIds = $("[name=checkId]:checked");
        if(checkIds.length==0){
            alert("请至少选择一条数据");
            return ;
        }
        var ids = [];
        checkIds.each(function(index,e){
            ids.push($(e).val());
        });
        confirmx('确认要删除所选条目吗？', "/spms/a/spms/spmsDevice/delete?id="+ids.join(','));
    });
    
})

/**
 * 点击tab栏，动态过滤设备类型信息
 * @param type
 */
function doReQuery(type) {
	classification = type;
	$("#classification").val(type);
	$(".active").removeClass("active");
	$("li:even",$(".nav")).eq(type-1).attr("class","active");
	doQuery();
}

/**
 * table组件，当row被点击时执行操作
 * @param a
 * @param b
 */
function rowClick(data,tr) {
	//$.alert($.toJSON(data));
	//window.location.href = "areaShow.html?id="+data.id;
}

function createFun(){
	window.location.href="areaForm.html?classification="+classification;
}

function deleteFun() {
	$.alert("删除成功");
}

function queryEnd() {
	$('.treeTable').treeTable();
}
