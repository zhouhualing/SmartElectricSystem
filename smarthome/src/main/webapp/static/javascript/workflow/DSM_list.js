//表格操作项
var oppObj = [];
var faId = 0;
var adopt = {
		text:"接受",
		fun:function(data){
			if(data.status == "INITIALIZED"){
				faId = data.faId;
				//openSet(data);
				popupBox(faId)
			}else{
				$.alert('当前状态不能进行此操作');
			}
			
			
		}
};

var reject1  = {
		text:"拒绝",
		fun:function(data){
			if(data.status == "INITIALIZED"){
				faId = data.faId;
				reject(data);
			}else{
				$.alert('当前状态不能进行此操作');
			}
			
		}
};

oppObj = [adopt,reject1];
//ready action
$(function(){
	doQuery();
})

function initFun(data,key) {
	
	var statusE = ['DONE','INITIALIZED','REFUSED','APPROVED'];
	var statusC = ['完成的','新响应','已拒绝','运行中'] ;
	if(key =="status") {
		for ( var int = 0; int < statusE.length; int++) {
			if(data[key] == statusE[int]) {
				return statusC[int];
			} 
		}
	}
}
function openSet(data){
	var obj = {
		title : '承诺目标',
		height : "170px",
		width : "350px",
		url : '../workflow/DSM_openBox.html?id='+data.id+
		'&promisedReduceTargetP='+data.promisedReduceTargetP+'&promisedReduceTargetQ='+data.promisedReduceTargetQ+
		'&faId='+faId,
		//confirmBtn : true,
		//confirmText:"确定",
		cancelText : "关闭",
		fun : true,
		myCallBack : function(data) {
			alert();
		}
	}
	new jqueryDialog(obj);
}

//row click submitting(data.id)
function rowClick(data) {
	window.location.href="DSM_ListMinute.html?id="+data.faId;
}

/**
 * 创建用户
 */
function createFun() {
	//window.location.href="user_add.html";
}

/**
 * 批量删除响应记录
 * @param data
 */
function deleteFun(data,data1) {
	if(data1.length==0) {
		$.alert("请选择要删除的记录。");
		return false;
	}
	var config = {
			msg: "您确定要删除么？",
			confirmClick: function () {
			var dto = {id: data1};
			doJsonRequest("/dsm/deleteDSM",dto,function(data){
				doQuery();
			},{showWaiting:true,successInfo:'删除成功',failtureInfo:'删除失败'})
		}
	}	
	$.confirm(config);
}

function doSelect() {
	//方式一
//	C_doOrgSelect({title:'test',myCallBack:function(data){
//		$.alert(data.name)
//	}})
	
	//方式二
	C_doOrgSelect(function(data){
		$.alert(data.name+":"+data.id+":"+data.code)
	})
}

function doASelect() {
	C_doAreaSelect(function(data){
		$.alert(data.name+":"+data.id+":"+data.code)
	})
}
//提交数据  接受
function submitting(faId){
	var promisedReduceTargetP = $("#promisedReduceTargetPForBox").val();
	var promisedReduceTargetQ = $("#promisedReduceTargetQForBox").val();
	if(!/^[0-9]*$/.test(promisedReduceTargetP) || "" == promisedReduceTargetP){ 
		$.alert("请输入数字~!");
		return;
	}
	if(!/^[0-9]*$/.test(promisedReduceTargetQ) || "" == promisedReduceTargetQ){ 
		$.alert("请输入数字~!");
		return;
	}
	var dto = {
		faId:faId,
		promisedReduceTargetP:promisedReduceTargetP,
		promisedReduceTargetQ:promisedReduceTargetQ,
		status:'APPROVED'
	}
	doJsonRequest("/dsm/promiseSet",dto,function(data){
		doQuery();
		$("#anniu3").css("display","none");
		hmUnBlockUI();
	},{showWaiting:true,successInfo:'处理成功',failtureInfo:'处理失败'})
}
//提交数据  拒绝
function reject(){
	var dto = {
		faId: faId,
		status:'REFUSED'
	}
	doJsonRequest("/dsm/promiseSet",dto,function(data){
		doQuery();
		
	},{showWaiting:true,successInfo:'处理成功',failtureInfo:'处理失败'})
}
/**
 * 弹出框
 * @param str
 * @param bool
 */
function popupBox(id){
	hmBlockUI();
	$("#loadingMaskLayerDiv1").css("z-index","98");
	$("#anniu3").remove();
	var tr = 
		"<div id='anniu3' style=\"position:absolute;"+
						"background:#FFFFFF;"+
						"width:360px;"+
						"left:37%;"+
						"top:33%;"+
						"z-index:99;\">"+
		"	<div style=\"margin:55px 0;text-align:center;\">										"+
		"		<div style=\"text-align:center; color:#000;\">										"+
		"<div id=\"collapseOne\">"+
		"<input id=\"type\" name=\"type\" type=\"hidden\" value=\"1\"/>"+
		"<div style=\"margin-top: 8px;\">"+
		"	<label>承诺降负荷: </label>"+
		"	<input id=\"promisedReduceTargetQForBox\" style=\"width:208px\" class=\"required\" type=\"text\" value=\"\" maxlength=\"200\"/>&nbsp;"+
		"	<br/>  "+
		"	<br/>  "+
		"	<label>承诺降电量: </label>"+
		"	<input id=\"promisedReduceTargetPForBox\" style=\"width:208px\" class=\"required\" type=\"text\" value=\"\" maxlength=\"200\"/>&nbsp;"+
		"</div>"+
		"</div>"+
		"		</div>																				"+
		"	</div>																					"+
		"	<div style=\"clear:both\"></div>														"+
		"	<div style=\"width:100%;background:#F2F2F2;padding:8px 0px; float:left;color:#000;\">	"+
		"	<div onclick=\"closeBox()\" onmouseout=\"this.style.backgroundColor='#F2F2F2'\" " +
		"onmousemove=\"this.style.backgroundColor='#d4d9d6'\"  style=\"cursor: pointer;float:right;width:48%;" +
		"border-right:0px solid #999999;text-align:center;\">关闭</div>			"+
		"	<div onclick=\"submitting('"+faId+"')\" onmouseout=\"this.style.backgroundColor='#F2F2F2'\" " +
		"onmousemove=\"this.style.backgroundColor='#d4d9d6'\"  style=\"cursor: pointer;float:right;width:48%;" +
		"border-right:0px solid #999999;text-align:center;\">确定</div>			"+
		"	</div>"+
		"	</div>";
	$("#body").append(tr); 
}
function closeBox(){
	$("#anniu3").css("display","none");
	hmUnBlockUI();
}