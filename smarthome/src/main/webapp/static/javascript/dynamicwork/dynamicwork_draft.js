var id = getURLParam("id");
$("#id").val(id);
var date = new Date();
var dyWoDate =date.format('yyyy年M月d日');
$("#dyWoDate").val(dyWoDate);
$("#dyWoDate1").val(date.format('yyyy-M-d'));
$("#year").html(date.getFullYear());

$("#dyWoDate1").datepicker({
	showSecond: false, //显示秒
	timeFormat: 'HH:mm',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-m-d",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间',
	buttonText:"选择",
	buttonClass:"btn_click"
});

$("#dyWoDate1").next('span').hide();
$("#dyWoDate").click(function(){
	$("#dyWoDate1").next('span').trigger('click');
});

$("#dyWoDate1").change(function(){
	var time = $(this).val().split("-");
	$("#dyWoDate").val(time[0]+"年"+time[1]+"月"+time[2]+"日")
})

$(function(){
	//初始化下拉框：
	$("#dynaCate").contents().find("select").append(getOptions());
	//初始化流程按钮
	wf_getOperator("dynamicwork",function(data){
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userName").val(data.userName);
	});
	if(id!='' && id!=null){
		var dto = {
				id:id
		}
		doJsonRequest("/dynawork/getDynaWorkIndex",dto,function(data){
			if(data.result) {
				var data = data.data;
				$("#issue").val(data.issue);
				$("#allIssue").val(data.allIssue);
				$("#title").val(data.title);
				if(data.dyWoDate!=null && data.dyWoDate!=''){
					$("#dyWoDate").val(new Date(data.dyWoDate).format('yyyy年M月d日'));
					$("#dyWoDate1").val(new Date(data.dyWoDate).format('yyyy-M-d'));
				}
				renderSubitem(data.subitems);
			}
		}); 
	}
})

/**
 * 流程参数
 * @returns {___anonymous796_924}
 */
function getAddData(){
	var dynamicWorkDTOs = new Array();
	$("div[name='content']").each(function(i){
		if($(this).contents().find('textarea').val()!=''){
			var dynamicWorkDTO = {};
			dynamicWorkDTO.dynaCateCode = $(this).attr('index');
			dynamicWorkDTO.content = $(this).contents().find('textarea').val();
			dynamicWorkDTOs[i] = dynamicWorkDTO;
		}
	})
	var dto = {
			id:$("#id").val(),
			issue:$("#issue").val(),
			allIssue:$("#allIssue").val(),
			title:"政府工作动态（"+$("#year").html()+"）第"+$("#issue").val()+"期",
			dyWoDate:$("#dyWoDate1").val(),
			dynamicWorkDTOs:dynamicWorkDTOs,
			status:'0002'
	};
	return dto;
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource=app";
}

function demo(){
	var flag = false;
	$("select[name='dynaCateCode']").each(function(){
		if(!$(this).is(':hidden') && $(this).val()==''){
			flag = true;
		}
	});
	return flag;
}

//校验
function wf_beforeValid(){
	if($("#issue").val() == '' || isNaN($("#issue").val())){
		$.alert("请输入正确的期号。");
		return false;
	}else if($("#allIssue").val() == '' || isNaN($("#allIssue").val())){
		$.alert("请输入正确的期号。");
		return false;
	}else if(demo()){
		$.alert("请输入动态类别。");
		return false;
	}
	return true;
}

//保存到草稿箱
$("#tempSubmitBtn").on("click",function(){
	var dto = getAddData();
	dto.status = "0001";
	doJsonRequest("/dynawork/addDynaWorkIndex",dto,function(data){
		if(data.result) {
			var data = data.data;
			$("#id").val(data.id);
			$.alert("暂存成功。");
		} else {
			$.alert("添加到草稿箱出错。");
		}
	},{showWaiting:true});
});

//*****************************************************JS动态效果开始************************************************************

//渲染子项
function renderSubitem(subitems){
		var keys = new Array();
		var j = 0;
		for(var key in subitems){
			keys[j] = key;
			j++;
		}
		if(j>0){
		$("#dynaCate").remove();
		var subitemDIV = "";
		for(var i = 0;i<keys.length;i++){
			var key = keys[i];
			var catName = $("select:first option[value='"+key+"']").html();
			var dynaWorks = subitems[key];
			var dynaCateDom = "";
			/*if(i == keys.length-1){
				dynaCateDom +='<div id="dynaCate" name="dynaCate">'
							+'<div class="main_select">'
							+'<select style="height:31px; float:left; width:90px;" name="dynaCateCode">'
							+getOptions()+"</select>"
							+'<input type="button" value="添加" class="btn_click" style="margin-left:15px;"  onclick="addDynaCate(this)"/><input type="button" value="删除" class="btn_select" onclick="delDynaCate(this)"/>'
							+'</div>';
			}else{*/
			var dynaCateName = dynaWorks[0].dynaCateName;
			if(key == null || key == ''){
				dynaCateName = "请选择";
			}
				dynaCateDom +='<div id="dynaCate" name="dynaCate">'
							+'<div class="main_select">'
							+'<select style="height:31px; float:left; width:90px;display:none;" name="dynaCateCode">'
							+getOptions()+"</select>"
							+'<span id="categorySpan" name="categorySpan" value="'+key+'" class="categorySpan">【'+dynaCateName+'】</span>'
							+'<input type="button" value="添加" class="btn_click" style="margin-left:15px;"  onclick="addDynaCate(this)" style="display:none;"/><input type="button" value="删除" class="btn_select" onclick="delDynaCate(this)"/>'
							+'</div>';
			/*}*/
			
			
			var content = "";
			if(dynaWorks.length>0){
				for(var n = 0;n<dynaWorks.length;n++){
					var dw = dynaWorks[n];
					content += '<div id="content" name="content" index="'+key+'">'
					+'<div class="main_conter"><p class="noPrint">事项内容：</p><textarea class="content textFont" id="content" name="content" rows=1 cols=40  onfocus="window.activeobj=this;this.clock=setInterval(function(){activeobj.style.height=activeobj.scrollHeight+\'px\';},200);" onblur="clearInterval(this.clock);">'+dw.content+'</textarea></div>'
					+'<div class="down_btn"><input type="button" value="添加" class="btn_click" onclick="addContent(this)"/><input type="button" value="删除" class="btn_select" onclick="delContent(this)"/></div>'
					+'</div>'
				}
			}
			subitemDIV+=dynaCateDom+content+"</div>";
		}
		$("#line").after(subitemDIV);
		
		$('select').each(function(){
			if(!$(this).is(':hidden')){
				$(this).val($(this).parent().next().attr('index'));
				return false;
			}
		})
		
		/**
		 * 内容高度自适应。
		 */
		$('textarea').each(function(){
			var height = $(this)[0].scrollHeight + "px";
			$(this).css('height',height);
		})
	}
}


//分类下拉框内容：
function getOptions(){
	return $("select:first").html();
}

//添加分类
function addDynaCate(dom){
	if($('span[name="categorySpan"]').size() == $("select:first option").size()-2){
		$.alert('已无更多分类。');
		return false;
	}
	if($(dom).parent().children('select').val()!='' || ($(dom).parent().children('span').html()!='' && $(dom).parent().children('span').html()!=undefined)){
		//首先将当前select变为input
		var $_selDom = $(dom).parent().children('select');
		var thisVal = $_selDom.children('option:selected').val();
		var thisHtml = $_selDom.children('option:selected').html();
		var newInput = '<span id="categorySpan" name="categorySpan" value="'+thisVal+'" class="categorySpan">【'+thisHtml+'】</span>'
		$_selDom.hide();
		if(thisVal!=''){
			$(dom).before(newInput);
		}
		var newDiv = '<div id="dynaCate" name="dynaCate"><div class="main_select"><select style="height:31px; float:left; width:90px;" name="dynaCateCode">'
			+getOptions()+"</select>"
	        +'<input type="button" value="添加" style="margin-left:15px;"  class="btn_click" onclick="addDynaCate(this)"/>'
	        +'<input type="button" value="删除" class="btn_select" onclick="delDynaCate(this)"/></div>'
		    +'<div id="content" name="content"><div class="main_conter"><p class="noPrint">事项内容：</p><textarea rows=1 cols=40 class="content textFont" onfocus="window.activeobj=this;this.clock=setInterval(function(){activeobj.style.height=activeobj.scrollHeight+\'px\';},200);" onblur="clearInterval(this.clock);"></textarea></div>'
		    +'<div class="down_btn"><input type="button" value="添加" class="btn_click" onclick="addContent(this)"/><input type="button" value="删除" class="btn_select" onclick="delContent(this)"/></div></div></div>';
		var $_mainDIV = $(dom).parent().parent();
		$_mainDIV.after(newDiv);
		$('span[name="categorySpan"]').each(function(){
			$_mainDIV.next().contents().find('option[value="'+$(this).attr("value")+'"]').attr('disabled',true);
		});
		$(dom).hide();
		/*selDomSort();*/
	}else{
		$.alert('请先选择当前事项类别。');
	}
	
}

//删除分类
function delDynaCate(dom){
	var optValue = $(dom).parent().children('span').attr('value');
	if($('select').length <= 2){
		$(dom).parent().children('span').remove();
		$(dom).parent().children('select').show().val('');
		$(dom).parent().next().children('textarea').val('');
		$(dom).prev().show();
	}else{
		$(dom).parent().parent().remove();
		$('select').each(function(){
			if(!$(this).is(':hidden')){
				$(this).children('option[value="'+optValue+'"]').removeAttr('disabled');
				return false;
			}
		})
	}
}

//添加内容
function addContent(dom){
	if($(dom).parent().parent().contents().find('textarea').val()!=''){
		var newDivs = '<div id="content" name="content"><div class="main_conter"><p class="noPrint">事项内容：</p><textarea rows=1 cols=40 class="content textFont" onfocus="window.activeobj=this;this.clock=setInterval(function(){activeobj.style.height=activeobj.scrollHeight+\'px\';},200);" onblur="clearInterval(this.clock);"></textarea></div>'
            		+ '<div class="down_btn"><input type="button" value="添加" class="btn_click" onclick="addContent(this)"/><input type="button" value="删除" class="btn_select" onclick="delContent(this)"/></div></div>';
		$(dom).parent().parent().after(newDivs);
		var $_thisSel = $(dom).parent().parent().parent().children('.main_select').children('select').children('option:selected').val();
		var $_thisSpan = $(dom).parent().parent().parent().children('.main_select').children('span').attr("value");
		if($_thisSel == ''){
			$_thisSel = $_thisSpan;
		}
		//为同一类别下的所有内容div添加index.
		$(dom).parent().parent().parent().children('div[name="content"]').attr('index',$_thisSel);
	}else{
		$.alert('请先输入当前事项内容。');
	}
	
}

//删除内容
function delContent(dom){
	if($(dom).parent().parent().parent().children('div[name="content"]').size()<=1){
		$(dom).parent().parent().children('.main_conter').children('textarea').val('');
	}else{
		$(dom).parent().parent().remove();
	}
}

$('body').delegate('select','change',function(){
	var optionVal = $(this).children('option:selected').val();
	$(this).parent().next().attr('index',optionVal);
});

///**
//* 给类别下拉框添加change事件，当前下拉框选中的元素其他下拉框不可以重复选择。
//*/
//$('body').delegate('select','click',function(){
//	//获取所有select选中的value值：
//	var $_this = $(this);
//	$("select").each(function(){
//		alert($(this).html());
//		$_this.children('option[value="'+$(this).val()+'"]').attr('disabled',true);
//	});
//});
//$('body').delegate('select','change',function(){
//	var optionVal = $(this).children('option:selected').val();
//	$("select").each(function(){
//		$(this).children('option[value="'+optionVal+'"]').attr('disabled',true);
//		$(this).children('option[value="'+optionVal+'"]').siblings().removeAttr('disabled');
//	});
//	$(this).children('option[value="'+optionVal+'"]').removeAttr('disabled');
//	//选择类别时，为当前类别下的所有内容div编号
//	$(this).parent().next().attr('index','content_'+optionVal);
//});

/*
* 为页面所有的select元素编号
*/
/*function selDomSort(){
	$("select").each(function(){
		$(this).attr('index',"dc_"+$("select").index($(this)));
	});
}*/

//*****************************************************JS动态效果结束************************************************************
