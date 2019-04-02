var task = getURLParam("task");
/**
 * init method
 */
$(function(){
	getCurrenUserInfo(function(data){
		if(task == 1){
			//待通知列表
			$("#collapseOne").append('<input type="hidden" value="'+data.userCode+'" id="userCode" name="createUser.userCode"/>');
			/*$("#reportInfoQuery").attr("queryId","tobecreatedListQuery");
			$("#reportInfoQuery").attr("conf","{title:'参与的公文列表',customBtn:true,createDelOpp:false,autoLoad:false,searchFiledName:'reportTitle'}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();*/
		}else if(task == 2){
			//待创建列表
			$("#collapseOne").append('<input type="hidden" value="'+data.userCode+'" id="userCode" name="userCode"/>');
			$("#reportInfoQuery").attr("queryId","tobecrRolesListQuery");
			$("#reportInfoQuery").attr("conf","{title:'待创建任务',customBtn:true,createDelOpp:false,autoLoad:false,initFun:initFun,selectText:' ',selectType:'FLAG',searchFiledName:'docTitle',showTitle:true}");
			$("#reportInfoQuery").empty();
			clickmedCommonTableToScan();
		}
		doQuery();
	});
});
var tobeCreatedId = 0;

var editObj = {
		text:"通知",
		fun:function(data) {
			tobeCreatedId = data.id;
			//弹出对话框通知相关委办局办理
			/*window.location.href="show_actionlist.html?id="+data.id+"&fromPage=0001"*/
			doRequest("/user/getRoleCodes",null,function(data){
				if(data.result){
					var roleCodes = data.data;
					var obj = {
					    title:'选择处理角色',
					    height:"320px",
					    width:"750px",
					    url:'../users/follow_role_dialog.html?operaterId=&isShowDate=0002&selectType=&markType=&checkType=checkbox&ignoreRole=&roleCodes='+roleCodes,
					    myCallBack:"setRoleCode"
					}
					new jqueryDialog(obj);
				}
			});
		}
}

function setRoleCode(da){
	var dto = {
			roleCodeList:da.roleCode,
			roleNameList:da.roleName,
			content:da.mark,
			status:"0002",
			tobeCreatedId:tobeCreatedId
	}
	doJsonRequest("/actionList/sendToBeCrRoles",dto,function(data){
		if(data.result) {
			if(data.data == 'success'){
				//跳转到成功页面
				$.alert("通知成功。");
			}else{
				$.alert("通知失败。");
			}
		} else {
			$.alert("提交失败。");
		}
	},{showWaiting:true});
}

var downObj = {
		text:"完结",
		fun:function(data) {
			$.confirm({msg:"确定要完结么？",confirmClick:function(){
				doJsonRequest("/actionList/downTobeCreatedItem", data.id, function(data,status){
					doQuery();
					if(data.result) {
						$.alert("成功办结。");
					} else {
						$.alert("操作失败。");
					}
				},{showWaitting:true})				
			},data:"1"})

		}
}


function initFun(data, key) {
	if(key == "docTitle") {
		var title = handleLongString(data.docTitle,240,"...");
		return "<div class='reportTitles' style='cursor:pointer;' onclick='goPage("+$.toJSON(data)+")' title='"+data.docTitle+"'>"+title+"</div>";
	}
}

function goPage(data) {
	window.location.href="draft_actionlist.html?tobeCrId="+data.tobeCreatedId;
}

var creatObj = {
		text:"创建",
		fun:function(data) {
			goPage(data);
		}	
}
var oppObj = null;

if(task == 1){
	oppObj = [editObj,downObj]
}else{
	oppObj = [creatObj];
}
