var nowTreeTable=""
/*
var deviceTypeAndQueryDic = {
	"1":"gwQuery",
	"2":"acQuery",
	"3":"plugAcQuery",
	"4":"centralAcQuery",
	"5":"onOffPlugQuery",
	"6":"onOffPmPlugQuery",
	"7":"wdQuery",
	"8":"pirQuery",
	"9":"htSensorQuery",
	"10":"onOffLightQuery",
	"11":"lampQuery"
}
*/
var deviceTypeAndQueryDic = {
	"1":{qn:"gwQuery",title:"网关列表"},
	"2":{qn:"acQuery",title:"长虹空调"},
	"3":{qn:"plugAcQuery",title:"智能空调插座"},
	"4":{qn:"centralAcQuery",title:"中央空调面板"},
	"5":{qn:"onOffPlugQuery",title:"通断插座"},
	"6":{qn:"onOffPmPlugQuery",title:"通断计量插座"},
	"7":{qn:"wdQuery",title:"门窗传感器"},
	"8":{qn:"pirQuery",title:"红外传感器"},
	"9":{qn:"htSensorQuery",title:"温湿度传感器"},
	"10":{qn:"onOffLightQuery",title:"灯开关面板"},
	"11":{qn:"lampQuery",title:"智能灯泡"}
}
var CommonTable = function(args) {
	this.title = "详细列表";
	this.pageMaxCount = 10;
	this.metaData = {};
	this.showColumns = {};
	this.columns = [];
	this.retenderDivId = "";
	this.doSearchController = "/query/executeSearch";
	this.datas = {};
	this.clickmedResourceId = "";
	this.defaultPageSize = 10;
	this.operatorColumn = "操作";
	this.allCount = 0;
	this.nowPage = 0;
	this.needRebuild = true;
	this.queryId = "";
	this.searchDiv = "";
	this.createFun = "";
	this.deleteFun = "";
	this.importFun = "";
	this.createDelOpp = true;
	this.showSelect = false;
	this.selectType = "FLAG";
	this.selectText = "";
	this.createBtn = true;
	this.createBtnTxt = "添加设备";
	this.deleteBtn = true;
	this.deleteBtnTxt = "批量删除";
	this.importBtn = false;
	this.importBtnTxt = "批量添加";
	this.customBtn = false;
	this.autoLoad = true;
	this.showNum = false;
	this.showTitle=true;
	// private field no reference
	this.requestData;
	this.mapCache;
	this.initFun;
	this.initFlag;
	this.modifyIds = new Array();
	this.modifyObjs = new Array();
	this.selectIds = new Array();
	this.selectObjs = new Array();
	this.maxLength = 10;
	this.placeholder = "";
	this.showProgressBar = true;
	this.onQueryEnd = "";
	this.handNull = false;
	this.searchFiledName="";
	this.resetSelectId = true;
	this.rowClick=false;
	this.isTree=false
	this.parentIdColumn="";
	this.appendInfo=""
	this.returnInfos=null;
	this.useController="";
	this.del_ico = "del_ico";
	/**
	 * 格式如下: { text:"xxx", fun:function(data) }
	 */
    //    ($(".table_text").attr("placeholder","请输入查询内容"));
	this.operatorArr = [];
	$.extend(this, args);
	var str = (this.clickmedResourceId.length > 0) ? ("clickmedResourceId='"
			+ this.clickmedResourceId + "'") : "";
	var titleStr = "";
	this.table = $('<div class="hm_table" style="padding: 0 0 0;border-bottom:0;">'+
			this.appendInfo+
			'	<div class="hm_table_title table_title '+(this.showTitle?"":'hidden')+'">'+
			'    	<h2 style="color:#FFFFFF;font-family:"Helvetica Neue",Helvetica,Arial,sans-serif">'+
			'        	'+this.title+''+
			'        </h2>'+
			'        <div>'+
			' <div class="action_button">'+(this.createBtn?('<a href="javascript:void(0);"  class="addBtn_table"><i class="add_ico"></i>'+this.createBtnTxt+'</a>'):"")+(this.importBtn?('<a id="importBtn" href="javascript:void(0);"  class="importBtn_table"><i class="add_ico"></i>'+this.importBtnTxt+'</a>'):"")+(this.deleteBtn?('<a href="javascript:void(0);" class="delBtn_table"><i class="' + this.del_ico + '" ></i>'+this.deleteBtnTxt+'</a>'):'')+'<a href="javascript:void(0)" class="common_slide_a table_slider_1" style="cursor:hand;text-decoration:none" onclick="togglebtnText(this)" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>'+"</div>"+
			'        </div>'+
			'    </div>'+
			'<table width="100%"  border="0" cellspacing="0" cellpadding="0"  class="common_table_t table table-striped table-bordered table-condensed table_list treeTable" style="word-wrap:break-word;white-space:nowrap;word-break:break-all;table-layout: fixed;"><thead></thead><tbody></tbody></table>');
	this.dialogDiv = $("<table  width='100%'><tr ><td class='col-sm-6' valign='top'><div class='panel panel-default'><div class='panel-heading'>显示列</div><div class='panel-body'></div></div></td><td><div><div><span class='glyphicon glyphicon-circle-arrow-up'></span></div><div ><span class='glyphicon glyphicon-circle-arrow-left'></span><span class='glyphicon glyphicon-circle-arrow-right'></span></div><div><span class='glyphicon glyphicon-circle-arrow-down'></span></div></div></td><td class='col-sm-6 col-sm-offset-6' valign='top' align='left'><div class='panel panel-default' ><div class='panel-heading'>隐藏列</div><div class='panel-body'></div></div></td></tr></table>");

	var _this = this;
	this.createQusetParam = function() {
		var type = getURLParam("type");
		if(type)
		    _this.queryId = getDeviceQueryName(type); //TODO add by ZHL
		return {
			queryId : _this.queryId,
			nowPage : _this.nowPage,
			pageMaxCount : _this.pageMaxCount,
			allCount : _this.allCount,
			searchFilters : _this.createSearchParam()
		}
	}

	this.createSearchParam = function() {
		var objs = new Array();
		var fieldNames = [];
		$("#" + _this.searchDiv).find("input,select").each(function() {
			var tempPosition = $.inArray($(this).attr("name"),fieldNames);
			if(tempPosition == -1) {
				fieldNames.push($(this).attr("name"));
				var tempObj = {
						fieldName : $(this).attr("name"),
						value : $(this).val()
				}
				objs.push(tempObj);
			} else {
				objs[tempPosition].value=objs[tempPosition].value+","+$(this).val();
			}
			
		});
		
//		var searchObj = {
//				fieldName :_this.table.find(".table_text").attr("name"),
//				value : _this.table.find(".table_text").val()
//		}
//		objs.push(searchObj);
		return objs;
	}

	this.resetBtnStyle = function() {
		if (_this.table.find("label:visible").length == 3) {
			_this.table.find("label:visible").eq(0).addClass("col-md-offset-7");
		} else if (_this.table.find("label:visible").length == 2) {
			_this.table.find("label").each(function() {
				if ($(this).is(":visible")) {
				} else {
					$(this).remove();
				}
			})
			_this.table.find("label:visible").eq(0).addClass("col-md-offset-8");
		} else {
			_this.table.find("label").each(function() {
				if ($(this).is(":visible")) {

				} else {
					$(this).remove();
				}
			})
			_this.table.find("label:visible").eq(0).addClass("col-md-offset-9");

		}

	}

	this.createHeaderDiv = function() {
		if ($("#" + _this.retenderDivId).find(_this.table).length <= 0) {
			$("#" + _this.retenderDivId).append(_this.table);
			var nowId = _this.retenderDivId + "_clickmedTable_" + _this.queryId;
			_this.table.attr("id", nowId);
			if (!_this.createBtn) {
				_this.table.find("label[id='c_t_topbtn_create']").hide();

			}
			if (!_this.searchBtn) {
				_this.table.find(":image").parent().hide();
			}
			if (!_this.customBtn) {
				_this.table.find("label[id='c_t_topbtn_custom']").hide();
			}
			$(".common_slide_a",_this.table).toggle(function(){
				$(this).removeClass("table_slider_1").addClass("table_slider_2");
				$(".common_table_t",_this.table).hide();
				$(_this.table).next("div[id='queryFoot']").hide();
			},function(){
				$(this).removeClass("table_slider_2").addClass("table_slider_1");
				$(".common_table_t",_this.table).show();
				$(_this.table).next("div[id='queryFoot']").show();
			})
		} else {
			if(_this.isTree) {
				$("#" + _this.retenderDivId).empty().append(_this.table);
				var nowId = _this.retenderDivId + "_clickmedTable_" + _this.queryId;
				_this.table.attr("id", nowId);
				if (!_this.createBtn) {
					_this.table.find("label[id='c_t_topbtn_create']").hide();

				}
				if (!_this.searchBtn) {
					_this.table.find(":image").parent().hide();
				}
				if (!_this.customBtn) {
					_this.table.find("label[id='c_t_topbtn_custom']").hide();
				}	
				$(".common_slide_a",_this.table).toggle(function(){
					$(this).removeClass("table_slider_1").addClass("table_slider_2");
					$(".common_table_t",_this.table).hide();
					$(_this.table).next("div[id='queryFoot']").hide();
				},function(){
					$(this).removeClass("table_slider_2").addClass("table_slider_1");
					$(".common_table_t",_this.table).show();
					$(_this.table).next("div[id='queryFoot']").show();
				})
			}
		}
		_this.resetBtnStyle();
	}

	this.appendContent = function(theContent) {
		$(theContent).append(_this.dialogDiv);
		_this.dialogDiv
				.find("div[class='panel-body']")
				.each(
						function(i) {
							$(this).empty();
							if (i == 0) {
								var str = "<ul id='c_t_ul_source'>";
								if ((_this.metaData.column.length > 0)
										&& ($(this).children().length <= 0)) {

									for (var i = 0; i < _this.columns.length; i++) {
										for (var j = 0; j < _this.metaData.column.length; j++) {
											if (_this.metaData.column[j].display == "true") {
												if (_this.columns[i] == _this.metaData.column[j].key) {
													str = str
															+ "<li style='cursor:pointer;' id='"
															+ _this.metaData.column[j].key
															+ "' onclick='clickmedTables."
															+ _this.retenderDivId
															+ ".doChangeStyle(this);'>"
															+ _this.metaData.column[j].header
															+ "</li>";
													break;
												}
											}
										}
									}
								}
								str = str + "</ul>";
								$(this).append(str);
							}
							if (i == 1) {
								var str = "<ul id='c_t_ul_target'>";
								if ((_this.metaData.column.length > 0)
										&& ($(this).children().length <= 0)) {
									for (var i = 0; i < _this.metaData.column.length; i++) {
										if (_this.metaData.column[i].display == "true") {
											if ($
													.inArray(
															_this.metaData.column[i].key,
															_this.columns) == -1) {
												str = str
														+ "<li id='"
														+ _this.metaData.column[i].key
														+ "' onclick='clickmedTables."
														+ _this.retenderDivId
														+ ".doChangeStyle(this);'>"
														+ _this.metaData.column[i].header
														+ "</li>";
											}
										}
									}
								}
								str = str + "</ul>";
								$(this).append(str);
							}
						});
		_this.bindSortBtn();
	}
	this.doSearch = function(resetSelectId) {
		if(_this.isTree) {
			this.createHeaderDiv();
		}
		if(resetSelectId) {
			_this.resetSelectId = false;
		}
		if($("#pageInput",_this.table.next("div")).length > 0) {
			_this.pageMaxCount = $("#pageInput",_this.table.next("div")).val();
		}

		if($(".c_t_topbtn_checkbox_top",_this.table).is(":checked")) {
			$(".c_t_topbtn_checkbox_top",_this.table).attr("checked",false);
		}
		_this.requestData = _this.createQusetParam();
        
		if(this.useController!=""){
			_this.doSearchController = this.useController;
		}
		
		doJsonRequest(_this.doSearchController, _this.requestData, function(
				data, textStatus, XMLHttpRequest) {
			if (data.result) {
				var data = data.data;
				_this.metaData = data.data.queryDTO;
				
				if ((_this.metaData.queryManager== null) &&(_this.metaData.customerDecoration == null)) {
					_this.datas = data.data.page.content;
				} else {
					if(_this.metaData.useEntity) {
						_this.datas = data.data.page.content;
					} else {
						_this.datas = data.data.customDatas;
					}
					
				}
				_this.returnInfos=data.data.returnInfos;
				_this.showColumns = data.data.showColumnDTOs
				_this.allCount = data.data.allCount;
				_this.nowPage = data.data.nowPage;
				_this.mapCache = data.data.dictMap;
				_this.createHeader();
				_this.createItem();
				_this.createFoot();
				_this.bindEvent();
				_this.needRebuild = false;
				_this.reCreateHeader = false;
				if (_this.nowPage == "-1") {
					_this.nowPage = Math.floor(_this.allCount
							/ _this.pageMaxCount);
				}
//				$('.treeTable',_this.table).removeData("treetable").removeClass("treetable");
				if(_this.isTree) {
					$('.treeTable',_this.table).treeTable();
					_this.needRebuild =true;
					_this.reCreateHeader = true;
				}
				if (typeof _this.onQueryEnd == 'function') {
					_this.onQueryEnd(_this.returnInfos,_this.datas);
				}
			} 
		},{showWaiting:_this.showProgressBar});
	}

	this.createHeaderDiv();

	this.autoLoadQuery = function() {

		if (_this.autoLoad) {
			_this.doSearch();
		}
	}

	this.autoLoadQuery();
	this.deleteObj = {
		text : "删除",
		fun : function(data) {

			$.confirm({
				msg : '确定要删除么？',
				confirmClick : "clickmedTables['" + _this.retenderDivId
						+ "'].doDelete",
				data : data
			});
		}
	}

	this.doDelete = function(data) {
		var obj = {
			queryId : _this.queryId,
			entityId : data.id
		}
		doJsonRequest("/query/doDelete", obj, function(data, status) {
			_this.executeSearch();
			if (data.result) {
				$.alert("删除成功!");
			} else {
				$.alert("删除失败!");
			}
		})
	}

	this.createHeader = function() {
		if (_this.needRebuild) {
			_this.table.find("thead").eq(0).empty();

			if (_this.createDelOpp) {
				if (!_this.hasAddBtn) {
					_this.operatorArr.push(_this.deleteObj);
					_this.hasAddBtn = true;
				}
			}
			var radioTh = "";
			if (_this.showSelect) {
                         //   console.log(_this.selectText);
				if (_this.selectType == "CHECKBOX") {
					//TODO multi check handle 
					radioTh = "<th name=\"check\" style=\"line-height:37px;width:30px \"><input type='checkbox' class='c_t_topbtn_checkbox_top' name='selectCheckbox' /></th>";
				} else {
					radioTh = "<th align='center' style='text-align:center;width:30px'>"+_this.selectText+"</th>";
				}
			}
			var headerStr = '<tr style="text-align:center; height:43px; background:#fff; font-size:14px;">' + radioTh;
			if (_this.showNum) {
	
					headerStr = headerStr + '<th style="line-height:43px;text-align:center;width:30px">序号</th>';
			}
			_this.columns = new Array();
			for (var i = 0; i < _this.showColumns.length; i++) {
				_this.columns.push(_this.showColumns[i].key)
				var styleStr = "text-align:"+_this.showColumns[i].align+";";
				var spaceStr = "";
				if(_this.showColumns[i].align=="left") {
					spaceStr = "&nbsp;&nbsp;"
				}
				if (_this.showColumns[i].width != null) {
					styleStr = styleStr + "width:"+_this.showColumns[i].width;
				}	
				headerStr = headerStr + '<th style="'+styleStr+'">'+spaceStr + _this.showColumns[i].header
						+ "</th>";
			}

			// 添加操作列
			if (_this.operatorArr.length > 0) {
				headerStr = headerStr + '<th style="line-height:43px;text-align:center; border-bottom:0px solid #cfd9e2">' + _this.operatorColumn + "</th>";
			}
			headerStr = headerStr + "</tr>";
			_this.table.find("thead").append(headerStr);
		} else {
			$(
					"#" + _this.retenderDivId + "_clickmedTable_"
							+ _this.metaData.queryId).find("tbody").eq(0)
					.empty();
		}
	}

	this.createItem = function() {
		var nowTr = "";
		if (_this.needRebuild) {
			_this.table.find("tbody").eq(0).empty();
		}
		var objvalue = "";
		for (var i = 0; i < _this.datas.length; i++) {
			var str="";
			// 此处默认有数据
//			if(i%2 != 0) {
//				str = "background:#f4f4f4";
//			}
			var pId = "";
			if(_this.isTree) {
				var treeColumnArr = _this.parentIdColumn.split(".");
				var pId = _this.datas[i];
				for (var k = 0; k < treeColumnArr.length; k++) {
					if (pId[eval('treeColumnArr[' + k + ']')] == null) {
						pId = " ";
						break;
					}
					pId = pId[eval('treeColumnArr[' + k + ']')];

				}
			}
			var nowTr = $("<tr  "+(_this.isTree?("id='"+_this.datas[i].id+"' pId='"+pId+"'"):"")+" style=' height:37px;text-align:center;width:100%;\"+str+\"'></tr>")
			_this.table.find("tbody").eq(0).append(nowTr);
			if (_this.showSelect) {
				if (_this.selectType == "RADIO") {
					nowTr
							.append("<td name=\"check\" style=\"line-height:37px;border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2\" width=\"50px\" align='center'><input type='radio' name='selectRadio' value='"
									+ i + "'/></td>");
				} else if (_this.selectType == "CHECKBOX") {
					var checkStr="";
					if(_this.resetSelectId){
						_this.cleanSelectInfo();
					}
					try {
						if($.inArray(_this.datas[i].id,_this.selectIds) != -1) {
							checkStr = "checked"
						}
						} catch(e){
							
						}
					var obj = $("<td name=\"check\" style=\"line-height:37px;border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2\"><input type='checkbox' "+checkStr+" class='c_t_topbtn_checkbox' name='selectCheckbox' /></td>");
					nowTr.append(obj);
					_this.bindCheckboxEvent(_this.datas[i], obj.find("input"),
							_this.retenderDivId);

				} else if(_this.selectType == "FLAG") {
					if (_this.showSelect) {
						var returnObj = _this.initFlag?_this.initFlag(_this.datas[i]):false;
						if (returnObj) {
							nowTr.append("<td style=\"line-height:40px;width:2%;text-align:center;border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2\" ></td>");
							nowTr.find("td").append(returnObj);
						} else {
							
							nowTr.append("<td style=\"line-height:40px;width:2%;text-align:center;border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2\"></td>");
							nowTr.find("td").append(" ");						
						}

					}					
				} else {
					$.alert("不支持的selectType。");
				}

			}
			if (_this.showNum) {
				nowTr.append("<td  style=\"line-height:40px;border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2\">" + (i + 1) + "</td>");

			}
			var columnTD = "";
			for (var j = 0; j < _this.showColumns.length; j++) {
				var filedArr = (_this.showColumns[j].key).split(".");
				var nowData = _this.datas[i];
				var datasNow = _this.datas[i];
				var appendData = "";
				for (var k = 0; k < filedArr.length; k++) {
					if (nowData[eval('filedArr[' + k + ']')] == null) {
						nowData = null;
						break;
					}
					nowData = nowData[eval('filedArr[' + k + ']')];

				}
				if (_this.showColumns[j].type == "java.util.Date") {
					if (nowData != null) {
						nowData = new Date(parseInt(nowData))
								.format(_this.showColumns[j].dateFormat == null ? "yyyy-M-d"
										: _this.showColumns[j].dateFormat);
					}
				}
				
				if (_this.showColumns[j].handleFormat == "0001") {
					if (nowData != null) {
						nowData = intervalFormat(nowData);
					}
				}
				
				
				if (_this.showColumns[j].dict != null) {
					var objArr = _this.mapCache[_this.showColumns[j].dict];
					if (nowData != null) {
						if (!isNaN(nowData)) {
							nowData = nowData.toString();
						}
						var nowKeyArr = nowData.split(",");
						var tempData = "";
						for (var m = 0; m < objArr.length; m++) {
							if ($.inArray(objArr[m].code, nowKeyArr) != -1) {
								tempData = tempData + "," + objArr[m].value
							}
						}
						nowData = tempData.substring(1);
					}
				}
				if (nowData == null) {
					nowData = "";
				}
				var tempData = nowData;

				if (getStrLength(nowData) > _this.showColumns[j].maxLength) {
					tempData = handleLongString(nowData,_this.showColumns[j].maxLength,"...")
				}

				if (_this.initFun) {
					var returnObj = _this.initFun(datasNow,
							_this.showColumns[j].key,nowTr,_this.returnInfos);
					if (returnObj) {
						tempData = returnObj.replace("<input", "<input id='"
								+ datasNow.id + "' filedName='"
								+ _this.showColumns[j].key + "'");
						nowData = "";
					}

				}
				var styleStr = "text-align:"+_this.showColumns[j].align+";";
				if (_this.showColumns[j].width != null) {
					styleStr = styleStr + "width:"+_this.showColumns[j].width;
				}	
				var spaceStr = "";
				if(_this.showColumns[j].align=="left") {
					spaceStr = "&nbsp;&nbsp;"
				}
				columnTD = columnTD + "<td  tdClick="+_this.showColumns[j].rowClick+" style=\"max-width:20%;white-space: nowrap;text-overflow: ellipsis;overflow:hidden;line-height:40px;border-right:1px solid #cfd9e2;white-space: nowrap;text-overflow: ellipsis; border-bottom:1px solid #cfd9e2;"+styleStr+"\" align='center' title='"+nowData+"'>"+spaceStr + tempData
						+ "</td>";
			}
			nowTr.append(columnTD);

			if(_this.rowClick) {
				(function() {
					var ii = i;
					var isTree = _this.isTree;
					$("[tdClick='true']",nowTr).on("click",function(){
							_this.rowClick(_this.datas[ii],this,_this.returnInfos);
					});
				})()
			}
			// 添加操作列
			if (_this.operatorArr.length > 0) {
				var nowTd = $("<td style=\"line-height:30px; text-align:center;border-right:1px solid #cfd9e2; border-bottom:1px solid #cfd9e2;\"></td>");
				nowTr.append(nowTd);
				for (var k = 0; k < _this.operatorArr.length; k++) {
					if(_this.operatorArr[k].text.length > 0) {
						var tempLabel = $("&nbsp;&nbsp;<a class='openThis' style='cursor:pointer;color:blue'>"
								+ _this.operatorArr[k].text
								+ "</a>&nbsp;&nbsp;");
						nowTd.append(tempLabel);
						(function() {
							var kk = k;
							var ii = i;
							tempLabel.on("click", function() {
								_this.operatorArr[kk].fun(_this.datas[ii], this)
							});
						})()
					}
				}
			}
			nowTr = nowTr + "</tr>";
                       
		}
//                 $("a").each(function(){
//                           
//                            if($(this).attr("title")){
//                                if($(this).attr("title").length>34){
//                                    var text =$(this).attr("title");
//                                    $(this).text(text.substr(0,34)+"...");
//                                }
//                                
//                            }
//                         
//                        });
	
		_this.table.find("tbody").find("tr").each(function(){
			$(this).find("input").on(
					"change",
					function() {
						var tempId = $(this).parentsUntil().filter("tr").find("input[filedName='id']").val();
						var tempIndex = $.inArray(tempId,_this.modifyIds);
						
						if (tempIndex != -1) {
							_this.modifyObjs[tempIndex][$(this).attr("filedName")] = $(this).val();
						} else {
							var object = new Object();
							object[$(this).attr("filedName")] = $(this).val();
							object["id"] = tempId;
							_this.modifyObjs.push(object);
							_this.modifyIds.push(tempId);
						}
					})
			$(this).find("textarea").on(
					"change",
					function() {
						var tempId = $(this).parentsUntil().filter("tr").find("input[filedName='id']").val();
						var tempIndex = $.inArray(tempId,_this.modifyIds);
						if (tempIndex != -1) {
							_this.modifyObjs[tempIndex][$(this).attr("filedName")] = $(this).val();
						} else {
							var object = new Object();
							object[$(this).attr("filedName")] = $(this).val();
							object["id"] = tempId;
							_this.modifyObjs.push(object);
							_this.modifyIds.push(tempId);
						}

					})
		});
		
		_this.doValideAcvtion();

		_this.table.find("tbody").find("input").each(
				function() {
					var tempId = $(this).parentsUntil().filter("tr").find("input[filedName='id']").val();
					var tempIndex = $.inArray(tempId,_this.modifyIds);
					if (tempIndex != -1) {
						$(this).val(
								_this.modifyObjs[tempIndex][$(this).attr(
										"filedName")]);
						$(this).trigger("keyup");
					}
				})
				
		_this.table.find("tbody").find("textarea").each(
				function() {
					var tempId = $(this).parentsUntil().filter("tr").find("input[filedName='id']").val();
					var tempIndex = $.inArray(tempId,
							_this.modifyIds);
					if (tempIndex != -1) {
						$(this).val(
								_this.modifyObjs[tempIndex][$(this).attr(
										"filedName")]);
						$(this).trigger("keyup");
					}
				})

	}

	this.createFoot = function() {
		if(_this.isTree) {
			return;
		}
		if(_this.handNull) {
			if(_this.allCount == 0) {
				$("#"+_this.retenderDivId).hide();
			} else {
				$("#"+_this.retenderDivId).show();
			}
		}
		if (_this.needRebuild) {
			$("#queryFoot", _this.table).remove();
			var footStr = '<div id="queryFoot" class="hm_page pagination" >';
			footStr = footStr + '<span style="float:left;margin-left:5px;">显示<input type="number" id="pageInput" style="width:50px!important;height:20px;border-radius:5px;" min="1" value="'+_this.pageMaxCount+'" onkeyup="doValidPageInput(this)" />条</span>';
			footStr = footStr+'<ul style="vertical-align:middle">';
			footStr = footStr + '<li ><a  id="pageFirst"  style="cursor:pointer;border-radius:5px;border:1px solid #dddddd;display: block;padding:7px 14px">首&nbsp;&nbsp;页</a></li>';
			footStr = footStr + '<li><a  id="beforePage" style="cursor:pointer;border-radius:5px;border:1px solid #dddddd;display: block;padding:7px 14px">上一页</a></li>';
			footStr = footStr + '<li><a  id="nextPage" style="cursor:pointer;border-radius:5px;border:1px solid #dddddd;display: block;padding:7px 14px">下一页</a></li>';
			footStr = footStr + '<li ><a  id="pageLast" style="cursor:pointer;border-radius:5px;border:1px solid #dddddd;display: block;padding:7px 14px">末&nbsp;&nbsp;页</a></li></ul>'+
								'<span style="vertical-align:middle;font-size:15px">当前共<span id="common_table_numText">'+
								_this.allCount+
								'</span>条<span></span>'+
								'</div>';
			
			$("#" +  _this.retenderDivId ).append(footStr);
		} else {
			_this.table.next("div").find("#common_table_numText").html(_this.allCount);
		}
		if (_this.datas.length > 0) {
			$(_this.table).next("div[id='queryFoot']")
					.find("span[id='common_table_numText']")
					.next()
					.empty()
					.append(
							"：正显示"
									+ (_this.nowPage * _this.pageMaxCount + 1)
									+ "到"
									+ (((_this.nowPage + 1) * _this.pageMaxCount) > _this.allCount ? _this.allCount
											: ((_this.nowPage + 1) * _this.pageMaxCount))
									+ "条。")
		} else {
			$(_this.table).next("div[id='queryFoot']").find(
					"span[id='common_table_numText']").next().empty();
		}
	}

	this.bindEvent = function() {
		if (_this.needRebuild) {
			_this.bindCreate();
			_this.bindSearch();
			_this.bindCustom();
			_this.bindAllSelect();
		}
		
		_this.activePage($("#" + _this.retenderDivId).find("div[id='queryFoot']"));
		
		if (_this.nowPage == 0) {
			_this.disablePage($("#beforePage",$("#" + _this.retenderDivId).find("div[id='queryFoot']")));
			if (Math.floor(_this.allCount / _this.pageMaxCount) == 0) {
				_this.disablePage($("#nextPage",$("#" + _this.retenderDivId).find("div[id='queryFoot']")));
			}
		} else if ((_this.nowPage == Math.floor(_this.allCount
				/ _this.pageMaxCount))
				|| (_this.nowPage == -1)) {
			
			_this.disablePage($("#nextPage",$("#" + _this.retenderDivId).find("div[id='queryFoot']")));
		} else if ((_this.allCount%_this.pageMaxCount==0)&&(_this.nowPage == (Math.floor(_this.allCount/ _this.pageMaxCount)-1))){
			_this.disablePage($("#nextPage",$("#" + _this.retenderDivId).find("div[id='queryFoot']")));		
		}
	}

	this.disablePage = function(obj) {
		obj.addClass("wf_disabled");
		obj.off("click");
	}

	this.activePage = function(obj) {
		$(".disabled", obj).removeAttr("class");
		obj.find("a[id='pageFirst']").off("click").removeClass("wf_disabled").on("click", function() {
			_this.doPageAction('0001');
		});
		obj.find("a[id='beforePage']").off("click").removeClass("wf_disabled").on("click", function() {
			_this.doPageAction('0002');
		});
		obj.find("a[id='nextPage']").off("click").removeClass("wf_disabled").on("click", function() {
			_this.doPageAction('0003');
		});
		obj.find("a[id='pageLast']").off("click").removeClass("wf_disabled").on("click", function() {
			_this.doPageAction('0004');
		});
	}

	/**
	 * 翻页时触发的操作 type值为【0001：首页，0002：上一页，0003下一页，0004末页】
	 */
	this.doPageAction = function(type) {
		switch (type) {
		case "0001":
			_this.nowPage = 0;
			_this.doSearch("false");
			break;
		case "0002":
			_this.nowPage = _this.nowPage - 1;
			_this.doSearch("false");
			break;
		case "0003":
			_this.nowPage = _this.nowPage + 1;
			_this.doSearch("false");
			break;
		case "0004":
			_this.nowPage = -1;
			_this.doSearch("false");
			break;
		}
	}

	this.bindCreate = function() {
		$(".addBtn_table",_this.table).on("click",function() {
			_this.createFun();
		});
		if(this.importBtn){
			$("#importBtn").on("click",function() {
				_this.importFun();
			});
		}
	}
	
	this.bindAllSelect = function(){
		$(".c_t_topbtn_checkbox_top",_this.table).on("click",function(){
			$(".c_t_topbtn_checkbox").trigger("click")
		})
	}

	this.bindSearch = function() {
		$(".delBtn_table",_this.table).on("click",function() {
			_this.deleteFun(_this.selectIds,_this.selectObjs);
		})
	}

	this.bindCustom = function() {
		_this.table.find("label[id='c_t_topbtn_custom']").on("click",
				function() {
					_this.customAction()
				})
	}

	this.cacheSelectIds = function(_this, objs, divId) {
		var index = $.inArray(objs.id, clickmedTables[divId].selectIds)
		if (index == -1) {
			clickmedTables[divId].selectIds.push(objs.id);
			clickmedTables[divId].selectObjs.push(objs);
		} else {
			clickmedTables[divId].selectIds.splice(index, 1);
			clickmedTables[divId].selectObjs.splice(index,1);
		}
	}

	this.executeSearch = function(resetSelectId) {
		_this.nowPage = 0;
		if(resetSelectId) {
			_this.resetSelectId=false;
		} else {
			_this.resetSelectId=true;
		}
		_this.doSearch();
	}

	this.customAction = function() {
		clickmedDialogs[_this.retenderDivId + "_ct_cd"].show();
	}

	this.doChangeStyle = function(_thisLi) {
		_this.dialogDiv.find("li").removeAttr("style");
		$(_thisLi).attr("style",
				"background-color: #428bca;border-color: #428bca;");
	}

	// 0up 1down 2left 3right
	this.doSortAction = function(_thisBtn, type) {
		var nowObj = _this.dialogDiv.find("li[style]");
		switch (type) {
		case 0: {
			nowObj.after(nowObj.prev());
		}
			break;
		case 1: {
			if (nowObj.parent().attr("id") == "c_t_ul_target") {
				_this.dialogDiv.find("ul[id='c_t_ul_source']").append(nowObj);
			}
		}
			break;
		case 2: {
			if (nowObj.parent().attr("id") == "c_t_ul_source") {
				_this.dialogDiv.find("ul[id='c_t_ul_target']").append(nowObj);
			}
		}
			break;
		case 3: {
			nowObj.before(nowObj.next());
		}
			break;
		default: {

		}
		}

	}

	this.bindSortBtn = function() {
		_this.dialogDiv.find("span").each(function(i) {
			var type = i;
			$(this).on("click", function() {
				_this.doSortAction(this, type);
			});
		});
	};

	this.queryModifySave = function(obj) {
		var showArrs = obj.find("ul[id='c_t_ul_source']").find("li");
		if (showArrs.length <= 0) {
			$.alert("修改失败，至少显示一列。");
			clickmedDialogs[_this.retenderDivId + "_ct_cd"].hide();
		} else {
			var columnNames = "";
			showArrs.each(function() {
				columnNames = columnNames + "," + $(this).attr("id");
			});
			columnNames = columnNames.substring(1);
			var dto = {
				queryId : _this.queryId,
				columnNames : columnNames
			}
			doJsonRequest("/query/queryCustomModify", dto, function(data,
					status) {
				if (data.result) {
					clickmedDialogs[_this.retenderDivId + "_ct_cd"].hide();
					_this.needRebuild = true;
					_this.executeSearch(false);
				} else {
					$.alert("修改失败，数据交互异常。");
					clickmedDialogs[_this.retenderDivId + "_ct_cd"].hide();
				}
			})
		}

	}

	this.getCheckedRadioValue = function() {
		var result = $.toJSON(_this.datas[_this.table.find(
				"input[name='selectRadio']:checked").val()]);
		return result;
	}
	this.hidenCreateBtn = function() {
		_this.table.find("abel[id='c_t_topbtn_create']").remove();
		_this.resetBtnStyle();
	}

	this.doValideAcvtion = function() {
		$('#queryValidateForm_' + _this.retenderDivId).validate(
				{
					onKeyup : true,
					namespace : "table",
					eachValidField : function() {
						$(this).closest('div').removeClass('has-error')
								.addClass('has-success');
					},
					eachInvalidField : function() {
						$(this).closest('div').removeClass('has-success')
								.addClass('has-error');
					}
				});
	}

	this.bindCheckboxEvent = function(data, jqueryObj, divId) {
		jqueryObj.on("click", function(event) {
			_this.cacheSelectIds(jqueryObj, data, divId);
			event.stopPropagation()
		})
	}

	this.getModifyData = function() {
		var obj = new Array();
		_this.table.find("tbody").find("tr").find("input").each(function() {
			var index = $.inArray($(this).attr("id"), _this.modifyIds)
			if (index != -1) {
				if ($(this).parent().attr("class") != "has-error") {
					obj.push(_this.modifyObjs[index]);
				}
			}
		})
		return obj;
	}

	this.getCount = function() {
		return _this.allCount;
	}
	
	this.cleanSelectInfo = function() {
		this.selectIds = new Array();
		this.selectObjs = new Array();
	}
	
	this.hideFoot = function() {
		_this.table.next("div").remove();
	}
}

var clickmedTables = {};

function doExecuteSearch(divId) {
	doInitSearchParam($("#" + divId))
}

/**
 * scan clickmed's table.
 */
function clickmedCommonTableToScan() {
	clickmedTables = {};
	$("div[class*='clickmedCommonTable']").each(function() {
		doInitSearchParam($(this))
	})
}

function doInitSearchParam(divObj) {
	var configStr = divObj.attr("conf")
	eval("var configStr =" + configStr);
	configStr.queryId = divObj.attr("queryId");
	configStr.searchDiv = divObj.attr("searchDiv");
	configStr.operatorArr =	eval(divObj.attr("operatorArr"));
	configStr.retenderDivId=divObj.attr("id");
	var nowCommonTable  = new CommonTable(eval(configStr));
	clickmedTables[divObj.attr('id')] = nowCommonTable;	
	$("#"+divObj.attr("searchDiv")).keydown(function(event){
		if (event.target.type=='textarea') return;
		switch (event.keyCode) {
			case 13:{
				clickmedTables[$("div[searchDiv='"+$(this).attr("id")+"']").attr("id")].executeSearch();
			} break;
			default:{
				
			}
		}
		
	});
	
//	nowCommonTable.table.find(".table_text").keydown(function(event){
//		switch (event.keyCode) {
//			case 13:{
//				nowCommonTable.executeSearch();
//			} break;
//			default:{
//				
//			}
//		}
//	});
	$("#"+divObj.attr("searchDiv")).find('textarea').keydown(function(event){
	});
}

function doQuery(divId) {
	var tempDivId = $("[queryId]").eq(0).attr("id");
	if (divId) {
		tempDivId = divId;
	}
	clickmedTables[tempDivId].executeSearch();
}

function doQueryAfter(time,divId) {
	var tempDivId = $("[queryId]").eq(0).attr("id");
	if (divId) {
		tempDivId = divId;
	}
	var tempTime = 0
	if(time) {
		tempTime = time;
	}
	setTimeout(function(){
		clickmedTables[tempDivId].executeSearch();
	},tempTime)
	
}

function hideCreateBtn(divId) {
	clickmedTables[divId].hidenCreateBtn();
}

function getSelectRadio(divId) {
	if (clickmedTables[divId].getCheckedRadioValue() != null) {
		return JSON.parse(clickmedTables[divId].getCheckedRadioValue());
	}
	else {
		return null;
	}
}

function getQueryCount(divId) {
	var tempDivId = $("[queryId]").eq(0).attr("id");
	if (divId) {
		tempDivId = divId;
	}
	return clickmedTables[tempDivId].getCount();
}

function getDeviceQueryName(type){
	if(type != null){
		var query = deviceTypeAndQueryDic[type];
		return query.qn;
	}
	return "";
}

function getDeviceTitle(type){
	if(type != null){
		var query = deviceTypeAndQueryDic[type];
		return query.title;
	}
	return "";
}

$(function() {
	clickmedCommonTableToScan();
})

function doValidPageInput(_this) {
	$(_this).val($(_this).val().replace(/\D/g,""));
	if($(_this).val() == 0) {
		$(_this).val(1)
	}
}