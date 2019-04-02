var WORKFLOW = {
		openService:"openService"
}
var messageType = "0001";
var mailMessage = "";
/**
 * 短信
 */
function editMessage() {
	var obj = {
	    title:'配置短信详细',
	    height:"220px",
	    width:"350px",
	    url:'../users/messageConfig.html?messageType='+messageType+"&mailMessage="+mailMessage,
	    fun:true,
	    myCallBack:function(data){
	    	messageType = data.messageType;
	    	mailMessage = data.mailMessage;
	    }
	}
	var nowSearchRole = new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}


/**
 * 获取启动审批流操作项。
 */
function wf_getOperator(data,callBack) {
	if(!(data instanceof Object)) {
		data = {
				workFlowTypeId:data
		}
	}
	if(data.isStartOpperator == undefined) {
		data.isStartOpperator = true;
	}

	var dto = {
			workFlowTypeId:data.workFlowTypeId,
			isStartOpperator:data.isStartOpperator,
			taskId:data.taskId
	}

	doJsonRequest("/workflow/getWorkFlowOperator", dto, function(data){
		if(data.result) {
			doInitOperater(data.data);
			if(callBack) {
				callBack(data.data)
			}
		} else {
			$.alert("启动操作项获取失败。");
		}
	})
}

var roleCodeCache = {};

/**
 * 初始化操作按钮
 * @param workflowdto
 */
function doInitOperater(workflowdto) {
	var data =  workflowdto.workFlowOperaterDTOs;
	var taskId = workflowdto.taskId;
	var ids = new Array();
	var objs = new Array();
	$("[wf-id]").each(function(){
		ids.push($(this).attr("wf-id"));
		objs.push($(this));
	})
	var workFlowTypeId = workflowdto.workFlowTypeId;
	for(var i=0; i<data.length; i++) {
		var tempLength = $.inArray(data[i].id,ids);
		if(tempLength != -1) {
			var obj = $("<button type='button' val='"+data[i].value+"' class='btn btn-primary'>"+data[i].text+"</button>");
			roleCodeCache[(data[i].id)]=data[i].roleCodes;
			objs[tempLength].empty().append(obj);
			(function(){
				var ii = i;
				var requestUrl = data[i].requestUrl;
				var createDataFun = data[i].createDataFun;
				var callBackDataFun = data[i].callBackDataFun;
				var operaterId = data[i].id;
				var isShowDialog = data[i].isShowDialog;
				var selectType = data[i].selectType;
				var markType = data[i].markType;
				var isShowDate = data[i].isShowDate;
				var printControl = data[i].printControl;
				//启动环节
				if("startEvent" == workflowdto.nodeType) {
					obj.on("click",function(){
						if((typeof(wf_beforeValid) != "undefined")&&(!wf_beforeValid(operaterId))) {
							return false;
						}
						//不显示弹出框，即自动启动
						if(isShowDialog=="0002") {
								var tempMark="";
								if(typeof(wf_getMark) != "undefined") {
									tempMark = wf_getMark(operaterId);
								}
								var data = {
									action:$(this).attr("val"),
									requestUrl:requestUrl,
									createDataFun:createDataFun,
									callBackDataFun:callBackDataFun,
									operaterId:operaterId,
									mark:tempMark,
									selectType:selectType,
									workFlowTypeId:workFlowTypeId
								}
								if(typeof(wf_getRoleCodes) != "undefined") {
									data.roleCodes=wf_getRoleCodes().split(",");
								}
								wf_startWorkFlow(data);
						} else {
							var obj = {
								roleCodes:roleCodeCache[operaterId],
								selectType:selectType,
								markType:markType,
								isShowDate:isShowDate,
								operaterId:operaterId,
								workFlowTypeId:workFlowTypeId,
								action:$(this).attr("val"),
								printControl:printControl
							}
							var _this = this;
							wf_doSearchRole(obj,function(data_,callBack){
								var data = {
									action:$(_this).attr("val"),
									requestUrl:requestUrl,
									createDataFun:createDataFun,
									callBackDataFun:callBackDataFun,
									mark:data_.mark,
									roleCodes:data_.roleCode,
									completeDate:data_.completeDate,
									operaterId:obj.operaterId,
									selectType:selectType,
									workFlowTypeId:workFlowTypeId
								}
								if(typeof(wf_getRoleCodes) != "undefined") {
										data.roleCodes=wf_getRoleCodes().split(",");
								}
								wf_startWorkFlow(data, callBack);
							});
						}
					})
				} else if("userTask" == workflowdto.nodeType) {
					obj.on("click",function(){
                        var _this = this;
                        if(operaterId != "wf_giveup") {
                            if ((typeof(wf_beforeValid) != "undefined") && (!wf_beforeValid(operaterId))) {
                                return false;
                            }
                        }
						if(isShowDialog=="0002") {
							var tempMark = "";
							var ignoreRole = "";
							if(typeof(wf_getMark) != "undefined") {
								tempMark = wf_getMark(operaterId);
							}
							if(typeof(wf_getIgnoreRole) != "undefined") {
								ignoreRole = wf_getIgnoreRole(operaterId);
							}
							var data = {
									roleCodes:roleCodeCache[operaterId],
									taskId:taskId,
									action:$(_this).attr("val"),
									requestUrl:requestUrl,
									createDataFun:createDataFun,
									callBackDataFun:callBackDataFun,
									mark:tempMark,
									operaterId:operaterId,
									workFlowTypeId:workFlowTypeId,
									ignoreRole:ignoreRole

							}
							if(typeof(wf_getRoleCodes) != "undefined") {
									data.roleCodes=wf_getRoleCodes().split(",");
							}
							wf_completeTask(data);
						} else {
							var obj = {
								roleCodes:roleCodeCache[operaterId],
								operaterId:operaterId,
								selectType:selectType,
								markType:markType,
								isShowDate:isShowDate,
								operaterId:operaterId,
								workFlowTypeId:workFlowTypeId,
								action:$(this).attr("val"),
								printControl:printControl
							}
							var ignoreRole = "";
							if(typeof(wf_getIgnoreRole) != "undefined") {
								ignoreRole = wf_getIgnoreRole(operaterId);
							}
							obj.ignoreRole = ignoreRole;
							wf_doSearchRole(obj,function(data_, callBack){
								var data = {
										taskId:taskId,
										action:$(_this).attr("val"),
										requestUrl:requestUrl,
										createDataFun:createDataFun,
										callBackDataFun:callBackDataFun,
										mark:data_.mark,
										roleCodes:data_.roleCode,
										operaterId:obj.operaterId,
										completeDate:data_.completeDate,
										workFlowTypeId:workFlowTypeId,
										printControl:printControl,
										counts:data_.counts,
										ignoreRole:obj.ignoreRole

								}
								if(typeof(wf_getRoleCodes) != "undefined") {
										data.roleCodes=wf_getRoleCodes().split(",");
								}
								wf_completeTask(data, callBack);
							})
					  }
				});
				}
			})()
		}
	}
}

function wf_startWorkFlow(data_) {
	var data = {
			requestUrl:data_.requestUrl,
			vars:{
				ACTION:data_.action,
				ROLECODE:data_.roleCodes
			},
			mark:data_.mark,
			completeDate:data_.completeDate,
			operaterId:data_.operaterId,
			workFlowTypeId:data_.workFlowTypeId,
			messageType:messageType,
			mailMessage:mailMessage,
			printControl:data_.printControl
	}

	if(data_.roleCodes) {
		data.roleCodes = data_.roleCodes.join(",");
	}
	if(data_.userCodes) {
		data.userCodes = data_.userCodes;
	}
	if(data_.selectType) {
		data.selectType = data_.selectType;
	}
	if(data_.requestUrl.length > 0) {
		eval("var object="+data_.createDataFun+"("+$.toJSON(data_)+")");
		data.object = object;
	}
	if(data_.workFlowTypeId == "jszc"){
		var bxCourse = $("#bxCause").val();
		var jszcdtos = [
		       {iKey:"bxCause",iValue:bxCourse}
		];
		data.jszcdtos = jszcdtos;
	}
	if(data_.workFlowTypeId == "unsubscribe"){
		var tdReason = $("#tdReason").val();
		var jszcdtos = [
		       {iKey:"tdReason",iValue:tdReason},
		       {iKey:"orderProduct",iValue:$("#orderProductID").val(),taskId:getURLParam("taskId"),processId:getURLParam("processInstanceId")},
		];
		data.jszcdtos = jszcdtos;
	}
	if(data_.workFlowTypeId == "businessChange"){		
		var jszcdtos = [
		       {iKey:"orderProduct",iValue:$("#orderProductID").val(),taskId:getURLParam("taskId"),processId:getURLParam("processInstanceId")},
		       {iKey:"changeProduct",iValue:$("#changeProductID").val(),taskId:getURLParam("taskId"),processId:getURLParam("processInstanceId")}
		];
		data.jszcdtos = jszcdtos;
	}
	doJsonRequest("/workflow/doStartWorkFlow",data, function(data){
		if(data.result) {
			eval(data_.callBackDataFun+"("+$.toJSON(data.data)+")");
		} else {
			$.alert("请求失败。");
		}
	},{showWaiting:true,successInfo:'工单已启动',failtureInfo:'工单启动失败'})
}
var nowSearchRole = "";
function wf_doSearchRole(obj,fun) {
	var str = "checkType=multi";
	if(obj.action.lastIndexOf("##") != -1) {
		str = "checkType=single"
	}
	var obj = {
	    title:'流程信息',
	    height:"320px",
	    width:"750px",
	    url:'../dialog/workFlowDialog.html?businessType='+obj.workFlowTypeId+'&printType='+obj.printControl+'&operaterId='+obj.operaterId+'&isShowDate='+obj.isShowDate+'&selectType='+obj.selectType+'&markType='+obj.markType+'&'+str+'&roleCodes='+obj.roleCodes+'&ignoreRole='+obj.ignoreRole,
	    fun:true,
	    myCallBack:fun
	}
	nowSearchRole = new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}

function wf_confirm(msg,urlorFun,title){
	var tmptitle = "提示信息";
	if(title) {
		tmptitle = title;
	}
	$.confirm({
	    title:tmptitle,
	    msg:msg,
	    height:150,
	    confirmClick:function(){
	    	if(urlorFun instanceof Function) {
	    		urlorFun();
	    	} else {
	    		window.location = urlorFun;
	    	}

	    },
	    cancelBtn:false,
	    closeBtn:false
	});
}

function wf_completeTask(data_) {
	var object = null;
	if((data_.createDataFun != null)&&(data_.createDataFun.length >0)) {
		eval("object="+data_.createDataFun+"("+$.toJSON(data_)+")");
	}
	var roleCodes = data_.roleCodes;
	if(!roleCodes){
		roleCodes = [];
	} else {
		if(typeof(roleCodes) == "string") {
			roleCodes = [roleCodes];
		}
	}
	var data = {
			taskId:data_.taskId,
			vars:{
				ACTION:data_.action
			},
			requestUrl:data_.requestUrl,
			object:object,
			mark:data_.mark,
			roleCodes:roleCodes.join(","),
			operaterId:data_.operaterId,
			completeDate:data_.completeDate,
			workFlowTypeId:data_.workFlowTypeId,
			messageType:messageType,
			mailMessage:mailMessage,
			printControl:data_.printControl
	}
	doJsonRequest("/workflow/doComplete",data, function(data){
		if(data.result) {
			eval(data_.callBackDataFun+"("+$.toJSON(data.data)+")");
		} else {
			$.alert("请求失败。");
		}
	},{showWaiting:true})
}

/**
 * 认领任务。
 * @param data data.taskId待认领的任务id
 * @param callBack 成功认领之后的回调函数。
 */
function wf_claim(data,callBack) {
	var dto = {
		taskId : data.taskId,
		businessKey:data.businessKey
	}
	doJsonRequest("/workflow/doClaim", dto, function(data) {
		if(data.result) {
			callBack();
		} else {
			$.alert("任务认领失败。");
		}
	})
}

(function(){
		$("[wf-tasklist]").each(function(){
			eval("var cfg =" +$(this).attr("wf-tasklist"));
			var title = "";
			var id = "";
			var searchDiv=""
			if(cfg) {
				title = cfg.title
				searchDiv = cfg.searchDiv;
			}
			var div = "<div id='"+$(this).attr("id")+"_wf' class='clickmedCommonTable_wf' queryId='taskListQuery' conf='{title:\""+title+"\",customBtn:true,createDelOpp:false,autoLoad:false,maxLength:20,onQueryEnd:queryEnd_wf,pageMaxCount:5000,searchBtn:false,createBtn:false,deleteBtn:false,showTitle:true}' searchDiv='"+searchDiv+"' >"
					"</div>";
			$(this).append(div)
		})
})()


function doQueryWF(divId, SearchDivId) {
	clickmedTables[divId+"_wf"].searchDiv = SearchDivId;
	doQuery(divId+"_wf")
}

function queryEnd_wf(other,datas) {
	for(var key in clickmedTables) {
		if(key.lastIndexOf("_wf") == (key.length-3)) {
			clickmedTables[key].hideFoot();
		}
	}
	var dto = {
		processInstanceId:getURLParam("processInstanceId")
	}
	doJsonRequest("/workflow/getTimeLine",dto,function(data){
		if(data.result) {
			var data = data.data;
			var stepCount = 0;
			$.each(data.timeLineNodes,function(index,taskObj){
				if(taskObj.nodeType == 0){
					stepCount++;
				}
			});
			var ol = $('<ol class="ui-step ui-step-' + stepCount + ' timeLineP"></ol>');
            var infoDiv_tl = $('<div class="chart_data tc" style="background:#f5f5f5; margin:10px; border-radius:5px;"></div>');
			$("#common_timeLine").append(ol);
            $("#common_timeLine").append(infoDiv_tl);
			var flagClass="ui-step-done";
			var competingNodeId = data.competingNodeId;
			$.each(data.timeLineNodes,function(index,taskObj){
				if(taskObj.nodeType == 0) {
					if(competingNodeId==taskObj.nodeId) {
						flagClass = "ui-step-active";
					}
                    var str = '<li class="'+flagClass+'" taskNode="'+taskObj.nodeId+'">'+
                              '<div class="ui-step-line">-</div><div class="arrow-right"></div>'+
                              '<div class="ui-step-icon"> <i class="iconfont">&#xf02f;</i> <i class="ui-step-number">'+index+'</i> <span class="ui-step-text">'+taskObj.nodeName+'</span></div>'+
                              '</li>';
					$(".timeLineP",$("#common_timeLine")).append(str);

					var infoStr = '<table class="table_view"  style="display:none" taskNode="'+taskObj.nodeId+'"  width="100%" border="0" cellpadding="0" cellspacing="0">';
					$.each(taskObj.taskInfos,function(index,taskInfo){
						infoStr = infoStr + "<tr>";
						var num = 1;
						for(var key in taskInfo) {
							if(key.lastIndexOf("displaynone_") == 0) {
								infoStr = infoStr+"<input type='hidden' name='"+key.substring(12)+"' id='"+key.substring(12)+"' value='"+taskInfo[key]+"'>";
								continue;
							}
							infoStr = infoStr+"<td class='td_name'>"+key+"</td><td>"+taskInfo[key]+"</td>";
							if(num++%2 == 0) {
								infoStr = infoStr+"</tr>"
							}
						}
						if(infoStr.lastIndexOf("</tr>") ==-1) {
							infoStr = infoStr + "</tr>";
						}
					})
                    infoStr = infoStr + "</table>";
                    $(infoDiv_tl).append(infoStr);
					if(competingNodeId==taskObj.nodeId) {
						flagClass = "ui-step-nodone";
                        $("table",infoDiv_tl).append("<tr><td></td></tr>")
						$("td",$("table:last",infoDiv_tl)).append($(".wf_completing_form"));
					}

				} else if(taskObj.nodeType == 1) {
					//TODO
				} else if(taskObj.nodeType == 2) {
					//TODO
				}
			})

            ol.children(":first").addClass("ui-step-start");
            ol.children(":last").addClass("ui-step-end");
			var tempStartAt = $(".ui-step-done").length+1;
			if($(".ui-step-active").length <= 0) {
				tempStartAt--;
			}
			$("#timeline").timelinr({
				arrowKeys : 'false',
				startAt:tempStartAt
			})
            //处理点击订户

            if($("#wfuserId").length > 0) {
                $("#spmsUserNameS").wrap("<a href='/spms/view/spmsUser/spmsUserDetail.html?id="+$("#wfuserId").val()+"'><a>");
            }
		}
	})
}


//function queryEnd_wf(other,datas) {
//    for(var key in clickmedTables) {
//        if(key.lastIndexOf("_wf") == (key.length-3)) {
//            clickmedTables[key].hideFoot();
//        }
//    }
//    var dto = {
//        processInstanceId:getURLParam("processInstanceId")
//    }
//    doJsonRequest("/workflow/getTimeLine",dto,function(data){
//        if(data.result) {
//            var data = data.data;
//            var str = '<div id="timeline" style="width:100%"><ul id="dates"></ul><ul id="issues" style="text-align:center;margin:0!important;min-height: 100px" ></ul></div>';
//            $("#common_timeLine").append(str);
//            var flagClass="completed";
//            var competingNodeId = data.competingNodeId;
//            $.each(data.timeLineNodes,function(index,taskObj){
//                if(taskObj.nodeType == 0) {
//                    if(competingNodeId==taskObj.nodeId) {
//                        flagClass = "completing";
//                    }
//                    $("#dates",$("#common_timeLine")).append('<li class="'+flagClass+'"><a href="#'+taskObj.nodeId+'">'+taskObj.nodeName+'</a></li>');
//                    var infoStr = "";
//                    $.each(taskObj.taskInfos,function(index,taskInfo){
//                        infoStr = infoStr + "<tr>";
//                        var num = 0;
//                        for(var key in taskInfo) {
//                            if(key.lastIndexOf("displaynone_") == 0) {
//                                infoStr = infoStr+"<input type='hidden' name='"+key.substring(12)+"' id='"+key.substring(12)+"' value='"+taskInfo[key]+"'>";
//                                continue;
//                            }
//                            infoStr = infoStr+"<td>"+key+":"+taskInfo[key]+"</td>";
//                            if(num++%2 == 0) {
//                                infoStr = infoStr+"</tr>"
//                            }
//                        }
//                        if(infoStr.lastIndexOf("</tr>") ==-1) {
//                            infoStr = infoStr + "</tr>";
//                        }
//                    })
//                    $("#issues",$("#common_timeLine")).append('<li id="'+taskObj.nodeId+'" style="width:100%;display:none">'+
//                    '<table style="background-color: #999;width:80%;margin:0 auto">'+infoStr+'</table>'
//                    +'</li>');
//                    if(competingNodeId==taskObj.nodeId) {
//                        flagClass = "uncompleted";
//                        $("#issues li:last",$("#common_timeLine")).append($(".wf_completing_form"));
//                    }
//                } else if(taskObj.nodeType == 1) {
//                    //TODO
//                } else if(taskObj.nodeType == 2) {
//                    //TODO
//                }
//            })
//            var tempStartAt = $(".completed").length+1;
//            if($(".completing").length <= 0) {
//                tempStartAt--;
//            }
//            $("#timeline").timelinr({
//                arrowKeys : 'false',
//                startAt:tempStartAt
//            })
//            //处理点击订户
//
//            if($("#wfuserId").length > 0) {
//                $("#spmsUserNameS").wrap("<a href='/spms/view/spmsUser/spmsUserDetail.html?id="+$("#wfuserId").val()+"'><a>");
//            }
//        }
//    })
//}


function wf_showProcessOnTable(divId,text) {
	var obj  = eval($("#"+divId).attr("operatorArr"));
	var texttemp = "查看流程图";
	if(text) {
		texttemp = text
	}
	var nowObj = {
			text:texttemp,
			fun:function(data) {
				var obj = {
						taskId:data.taskId
				}
				wf_doShowImage(obj);
			}
	}
	obj.push(nowObj)
}

function wf_doShowImage(obj) {
	var taskId = "";
	if(obj) {
		taskId = obj.taskId
	}
	var obj = {
	    title:'查看流程图',
	    height:"500px",
	    width:"750px",
	    url:'/cmcp/view/workflow/processImage.html?taskId='+taskId,
	    fun:true,
	    myCallBack:function(){

	    },
	    confirmBtn:false
	}

	new jqueryDialog(obj);
}

function doGetMarkInfo(businessKey,workflowId) {
	var dto = {
			businessKey:businessKey,
			workFlowTypeId:workflowId
	}
	var obj = {};
	doJsonRequest("/workflow/getMarkInfo",dto,function(data){
		if(data.result) {
			var markInfo = data.data.markInfo;
			for(var key in markInfo) {
				$("[wf_mark*='"+key+"']").each(function(){
					var str = "";
					for(var i=0; i <markInfo[key].length; i++) {
						if(markInfo[key][i].message == null) {
							continue;
						}
						var strArr = markInfo[key][i].message.split("_");
						str = str+strArr[0]+" "+"["+strArr[3] +" "+new Date(parseInt(strArr[1])).format("yyyy-MM-dd hh:mm")+"]"+"<br/>";
					}
					$(this).html($(this).html()+str).addClass("wf_breakall");
				})
			}
		}
	})
}

/**
 * 获取参与该流程该任务该流水的人和角色【目前只有人】
 * @param workflowTypeId
 * @param taskKey 可以多个逗号分开
 * @param businessKey
 */
function doGetAttendInfo(workflowTypeId,taskKey,businessKey,callBack)  {
	var dto = {
			workFlowTypeId:workflowTypeId,
			userTask:taskKey,
			businessKey:businessKey
	}
	doJsonRequest("/workflow/getIntentPersonRole",dto,function(data){
		if(data.result) {
			if(callBack) {
				callBack(data.data);
			}
		} else {
			$.alert("获取任务信息出错。");
		}
	});
}
/**
 * 初始化工单表单
 */
function appendWorkOrderInfo() {
		if($("#workOrderBaseInfo").length >0) {
			$("#workOrderBaseInfo").append('               <legend>'+
			'                    工单基本信息<a class="togglebtn togglebtn-down" href="javascript:void(0)" onClick="togglebtnText(this)"></a>'+
			'               </legend>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">工单类型:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="typeTextS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">工单编号:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="workOrderCodeS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">工单启动时间:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="createDateS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">开户人:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="spmsUserNameS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">电话:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="spmsUserMobileS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">email:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="emailS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">受理人:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="createUserNameS"></div>'+
			'                    </div>'+
			'               </div>'+
			'               <div class="control-group">'+
			'                    <label class="control-label">受理人工号:</label>'+
			'                    <div class="controls">'+
			'                         <div class="input_view" id="createUserNoS"></div>'+
			'                    </div>'+
			'               </div>')
		}
}

/**
 * 初始化工单表单数据
 */
function initWorkOrderInfo(fun,idName) {
	var tempIdName = "workOrderId";
	if(idName) {
		tempIdName = idName;
	}
	if($("#workOrderBaseInfo").length >0) {
		var dto = {
			id: getURLParam(tempIdName)
		}
		doJsonRequest("/spmsWorkOrder/getById", dto, function (data) {
			if (data.result) {
				var data = data.data;
				var dataKey = [];
				for (var key in data) {
					dataKey.push(key + "S");
				}
				$("div", $("#workOrderInfo")).each(function () {
					if ($.inArray($(this).attr("id"), dataKey) != -1) {
						if (($(this).attr("id") == "passwordS") || ($(this).attr("id") == "confirmPaswordS")) {
						} else {
							var nowFieldId = $(this).attr("id").substring(0, $(this).attr("id").length - 1);
							if ($("#" + nowFieldId).is(".the_select2")) {
								$(this).html(data[(nowFieldId + "Text")]);
								$(this).attr("eValue", data[nowFieldId]);
								$("#" + nowFieldId).select2('val', 1);
							} else {
								$(this).html(data[nowFieldId]);
							}
						}
					}
				})
				$("#createDateS").html(new Date(data.createDate).format("yyyy-MM-dd hh:mm:ss"));
				if (fun) {
					fun(data);
				}
			}
		})
	}
}

appendWorkOrderInfo();

function doAssign(userCode) {
	var dto = {
		taskId:getURLParam("taskId"),
		assignUserCode:userCode,
		businessKey:getURLParam("workOrderId")
	}
	doJsonRequest("/workflow/doAssingee",dto,function(data){
		history.go(-1);
	},{showWaiting:true,successInfo:'已成功指派',failtureInfo:'指派失败'})
}

function doClaim() {
	var dto = {
		taskId:getURLParam("taskId"),
		businessKey:getURLParam("workOrderId")
	}
	doJsonRequest("/workflow/doClaim",dto,function(data){
		history.go(-1);
	},{showWaiting:true,successInfo:'认领成功',failtureInfo:'认领失败'})
}