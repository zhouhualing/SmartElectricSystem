//点击编辑
function editContolText(t,button){
	//获取需要编辑的控件集
	var textLabels = $(t).parents("form").find(".input_view");
	$.each(textLabels,function(index,e){
		var parent = $(e).parent();
		var input = $(e).next();
		if($(parent).get(0).tagName === 'A') {
			input = parent.next();
		}
		if(input.attr("select")){//下拉列表控件
			$("#"+$(input).attr("id")).show();
			//$(input).find("option[text='"+$(e).html()+"']").attr("selected",true);
		}else{
			if(input.hasClass("radioDiv")){//点选
				input.find("input:radio[title='"+$(e).html().trim()+"']").attr("checked","checked");
			}else if(input.hasClass("checkDiv")){//复选
				var checkValue =  $(e).html().trim().split(",");
				$.each(checkValue,function(index,title){
					input.find("input:checkbox[title='"+title+"']").attr("checked","checked");
				});
			}else{
				if($(input).find("input:eq(0)").attr("treeselect") == "true"){//treeselect
					$(input).find("input:eq(1)").css('display','');
					$(input).find("a:eq(0)").css('display','');
				}else
					if(input.attr("id") && input.attr("id").indexOf("s2id")>-1){
						var currSelect = $(input).next();
						$(currSelect).find("option[text='"+$(e).html()+"']").attr("selected",true);
						$(currSelect).select2('val',$(e).attr("eValue"));
					} else {
						input.val($(e).html().trim());
					}
			}
		}
		//隐藏文字显示出组件
		$(e).hide();
		input.show();
	});
	$(t).hide();
	$("#"+button+"Complete").show();
	$("#"+button+"Cancel").show();
}
//完成编辑
function finishEditText(t,button,url,formId,callBack){
	var thisForm ;
	if(formId)
		thisForm = $('#'+formId);
	else
		thisForm = $(t).parents("form");
	var textLabels = thisForm.find(".input_view");
	$(t).attr('disabled','true');
	//ajax调用保存
	doRequest(url,thisForm.serialize(),function(data){
		if(data.result) {
			if(callBack) {
				callBack(data);
			}
			//保存成功
			$.each(textLabels,function(index,e){
				var parent = $(e).parent();
				var input = $(e).next();
				if($(parent).get(0).tagName === 'A')
					input = parent.next();
				if(input.is("select")){//下拉列表
					$(e).html($(input).find("option:selected").text());
				}else if(input.is("div")){//div
					if(input.hasClass("radioDiv")){//单选
						$(e).html(input.find("input:radio:checked").attr("title"));
					}else if(input.hasClass("checkDiv")){//复选
						var checkedBoxs = input.find("input:checkbox:checked");
						var checkText = new Array();
						$.each(checkedBoxs,function(index,checkBox){
							checkText.push($(checkBox).attr("title"));
						});
						$(e).html(checkText.join(","));
					}else if($(input).find("input:eq(0)").attr("treeselect") == "true"){//treeselect
						$(e).html($(input).find("input:eq(1)").val());
					}else{//下拉列表
						$("#s2id_"+$(input).attr("id")).hide();
						var selectVale = $("#"+$(input).attr("id")+" a span").text();
						$(e).html(selectVale);
						var currSelect = $(input).next();
//						$(currSelect).show();
						$(currSelect).find("option[text='"+selectVale+"']").attr("selected",true);
					}
				}else{
					$(e).html(input.val());
				}
				$("[eValue]").each(function(){
					$(this).attr("eValue",data.data[$(this).attr("id").substring(0,$(this).attr("id").length-1)]);
				})
				//显示文字 隐藏组件
				$(e).show();
				input.hide();
			});
			$(t).hide();
			
			$("#"+button+"Cancel").hide();
			$("#"+button+"Edit").show();
		} else {

		}
		$(t).removeAttr("disabled");		
	},{showWaiting:true})
}

/**
 * 取消按钮
 * @param t 当前控件
 * @param button 按钮id字符串共同部分
 */
function cancelContol(t,button){
	var textLabels = $(t).parents("form").find(".input_view");
	$.each(textLabels,function(index,e){
		var parent = $(e).parent();
		var input = $(e).next();
		if($(parent).get(0).tagName === 'A')
			input = parent.next();
		if(input.attr("id") && input.attr("id").indexOf("s2id")>-1){
			var currSelect = $(input).next();
			$(currSelect).find("option[text='"+$(e).html()+"']").attr("selected",true);
			$(currSelect).select2('val',$(e).attr("eValue"));
		}
		$(e).show();
		input.hide();
        parent.find("input[type=button]").toggle();
	});
	$(t).hide();
   // $("input[type=button]").toggle();
	$("#bizAreaButton").hide();
	$("#"+button+"Complete").hide();
	$("#"+button+"Edit").show();
}
/**
 * 隐藏按钮
 * @param t 当前控件
 */
function togglebtnText(t){
	if($(t).hasClass("togglebtn-down")){
		$(t).removeClass("togglebtn-down");
		$(t).addClass("togglebtn-up");
	}else{
		$(t).removeClass("togglebtn-up");
		$(t).addClass("togglebtn-down");
	}
	$(t).parent().parent().find(".control-group").toggle();
    $(t).parent().parent().find("#jqxTree").toggle();
	$(t).parent().parent().find("table").toggle();
}