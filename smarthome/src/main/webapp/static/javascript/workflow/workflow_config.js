var processDefinedId = getURLParam("processDefinedId");

/**
 * init method
 */
$(function(){
	$("#processDefinedId").val(processDefinedId);
	doQuery();
	
});

function initFun(datas, columnKey) {
	
	var checkedStr1="";
	var checkedStr2="";
	var checkedStr3="";
	var checkedStr4="";
	var checkedStr5="";
	var checkedStr6="";
	var checkedStr7="";
	if(datas[columnKey] == '0001') {
		checkedStr1 = "selected"
	}
	if(datas[columnKey] == '0002') {
		checkedStr2 = "selected"
	}
	if(datas[columnKey] == '0003') {
		checkedStr3 = "selected"
	}
	if(datas[columnKey] == '0004') {
		checkedStr4 = "selected"
	}
	if(datas[columnKey] == '0005') {
		checkedStr5 = "selected"
	}
	if(datas[columnKey] == '0006') {
		checkedStr6 = "selected"
	}
	if(datas[columnKey] == '0007') {
		checkedStr7 = "selected"
	}
	if(datas.type == "sequenceFlow") {
		if(columnKey == "text") {
			return "<input type='hidden' name='text' value='"+setValue(datas['text'])+"'/>"+datas['text'];
		}		
		if(columnKey == "requestUrl") {
			return "<input type='hidden' name='type' value='"+setValue(datas['type'])+"'/><input type='hidden' name='workFlowTypeId' value='"+setValue(datas['workFlowTypeId'])+"'/><input type='hidden' name='id' value='"+setValue(datas['id'])+"'/><input type='hidden' name='operaterId' value='"+setValue(datas['operaterId'])+"'/><input type='hidden' name='text' value='"+setValue(datas['text'])+"'/><input type='text' class='form-control' value='"+setValue(datas[columnKey])+"' name='requestUrl'>";
		}
		if(columnKey == "createDataFun") {
			return "<input type='text' class='form-control' value='"+setValue(datas[columnKey])+"' name='createDataFun'>";
		}
		if(columnKey == "callBackDataFun") {
			return "<input type='text' class='form-control' value='"+setValue(datas[columnKey])+"' name='callBackDataFun'>";
		}
		if(columnKey == "markType") {
			return "<select class='form-control'   name='markType'><option value='0001' "+checkedStr1+">显示备注</option><option value='0002' "+checkedStr2+">不显示备注</option></select>";
		}
		if(columnKey == "selectType") {
			return "<select class='form-control'   name='selectType'><option value='0001' "+checkedStr1+">多选角色</option><option value='0002' "+checkedStr2+">不显示选人</option><option value='0003' "+checkedStr3+">单选角色</option><option value='0004' "+checkedStr4+">单选人</option><option value='0005' "+checkedStr5+">多选人</option></select>";
		}
		if(columnKey == "isShowDate") {
			return "<select class='form-control'   name='isShowDate'><option value='0001' "+checkedStr1+">显示日期</option><option value='0002' "+checkedStr2+">不显示日期</option></select>";
		}
		if(columnKey == "isGiveUser") {
			return "<select class='form-control'   name='isGiveUser'><option value='0001' "+checkedStr1+">办理角色</option><option value='0002' "+checkedStr2+">办理人</option><option value='0003' "+checkedStr3+">办理人所属的角色</option></select>";
		}
		if(columnKey == "isShowDialog") {
			return "<select class='form-control'   name='isShowDialog'><option value='0001' "+checkedStr1+">显示弹出框</option><option value='0002' "+checkedStr2+">不显示弹出框</option></select>";
		}
		if(columnKey == "roleCodes") {
			return "<input type='text' class='form-control' value='"+setValue(datas[columnKey])+"' name='roleCodes'>";
		}
		if(columnKey == "haveOperaterStr") {
			return "<input type='text' class='form-control' value='"+setValue(datas[columnKey])+"' name='haveOperaterStr'>";
		}
		if(columnKey == "phoneHaveOperaterStr") {
			return "<input type='text' class='form-control' value='"+setValue(datas[columnKey])+"' name='phoneHaveOperaterStr'>";
		}
		if(columnKey == "phoneMarkType") {
			return "<select class='form-control'   name='phoneMarkType'><option value='0001' "+checkedStr1+">必须输入备注【流程跟踪显示】</option><option value='0002' "+checkedStr2+">不允许输入</option><option value='0003' "+checkedStr3+">默认意见【流程跟踪显示】</option><option value='0004' "+checkedStr4+">必须输入备注【流程跟踪不显示】</option><option value='0005' "+checkedStr5+">默认意见【流程跟踪不显示】</option><option value='0006' "+checkedStr6+">退回意见【流程跟踪显示】</option><option value='0007' "+checkedStr7+">退回意见【流程跟踪不显示】</option></select>";
		}
		if(columnKey == "printControl") {
			return "<select class='form-control'   name='printControl'><option value='0001' "+checkedStr1+">不控制</option><option value='0002' "+checkedStr2+">打印控制</option><option value='0003' "+checkedStr3+">静默控制</option></select>";
		}
		if(columnKey == "defaultAdvice") {
			return "<input type='text' name='defaultAdvice' class='form-control' value='"+(datas[columnKey]==null?"":datas[columnKey])+"'/>";
		}
	}
	
	if(datas.type == "userTask") {
		if(columnKey == "countersign") {
			return "<input type='hidden' name='type' value='"+setValue(datas['type'])+"'/><input type='hidden' name='workFlowTypeId' value='"+setValue(datas['workFlowTypeId'])+"'/><input type='hidden' name='id' value='"+setValue(datas['id'])+"'/><input type='hidden' name='operaterId' value='"+setValue(datas['operaterId'])+"'/><input type='hidden' name='text' value='"+setValue(datas['text'])+"'/><select class='form-control'   name='countersign'><option value='0001' "+checkedStr1+">非会签</option><option value='0002' "+checkedStr2+">角色会签</option><option value='0003' "+checkedStr3+">人会签</option></select>";
		}else if(columnKey == "handleTime") {
            return "<input type='text' name='handleTime' class='form-control' value='"+(datas[columnKey]==null?"":datas[columnKey])+"'/>";
        } else if(columnKey == "userType"){
			return "<select class='form-control'   name='userType'><option value='0001' "+checkedStr1+">市长</option><option value='0002' "+checkedStr2+">副市长</option><option value='0003' "+checkedStr3+">秘书长</option><option value='0004' "+checkedStr4+">副秘书长</option><option value='0005' "+checkedStr5+">法制办</option><option value='0006' "+checkedStr6+">其他</option><option value='0007' "+checkedStr7+">不盖章</option></select>";
		}else if(columnKey == "operaterId"){
			return datas.operaterId;
		} else if(columnKey == "text"){
			return "<input type='hidden' name='text' value='"+setValue(datas['text'])+"'/>"+datas['text'];
		} else {
			return " ";
		}
	}
}

function setValue(data) {
	return (data==undefined)?"":data;
}

function queryEnd() {
	clickmedTables.reportInfoQuery.hideFoot();
}

var scanObj = {
		text:"查看",
		fun:function(data) {
			window.location.href="drafting_reports_show.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}

var approvalObj = {
		text:"保存",
		fun:function(data, _this) {
			var obj = {};
			$(_this).parent().parent().children().find("input").each(function(){
				obj[$(this).attr("name")] = setValue($(this).val())
			})
			$(_this).parent().parent().children().find("select").each(function(){
				obj[$(this).attr("name")] = setValue($(this).val())
			})
			doJsonRequest("/workflow/doSaveOperater",obj, function(data){
				if(data.result) {
					$(_this).parent().parent().children().find("input[name='id']").val(data.data.id);
				} else {
				}
			},{showWaiting:true})
		}
}

var approvalObj1 = {
		text:"办理",
		fun:function(data) {
			window.location.href="new_reqrep_examine.html?id="+data.reportId+"&taskId="+data.taskId;
		}
}


var oppObj = [approvalObj];


