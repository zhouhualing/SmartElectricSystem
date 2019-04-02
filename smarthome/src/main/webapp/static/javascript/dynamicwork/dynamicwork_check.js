var id = getURLParam("id");
$("#id").val(id);
$(function(){
	//初始化下拉框：
	$("#dynaCate").contents().find("select").append(getOptions());
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userName").val(data.userName);
		$("#isMayor").val(data.isMayor);
	});
	if(id!='' && id!=null){
		var dto = {
				id:id
		}
		doJsonRequest("/dynawork/getDynaWorkIndex",dto,function(data){
			if(data.result) {
				var data = data.data;
				var subitems = data.subitems;//子项
				$("#issue").val(data.issue);
				$("#allIssue").val(data.allIssue);
				$("#title").val(data.title);
				if(data.dyWoDate!=''){
					$("#dyWoDate").html(new Date(data.dyWoDate).format('yyyy年M月d日'));
					$("#year").html(new Date(data.dyWoDate).getFullYear());
				}
				renderSubitem(subitems);
			}
		}); 
	}
})

//渲染子项
function renderSubitem(subitems){
	var keys = new Array();
	var j = 0;
	for(var key in subitems){
		keys[j] = key;
		j++;
	}
	var subitemDIV = "";
	for(var i = 0;i<keys.length;i++){
		var key = keys[i];
		var catName = $("select:first option[value='"+key+"']").html();
		var dynaWorks = subitems[key];
		var dynaCateDom ='<div id="dynaCate" name="dynaCate">'
						+'<div class="main_select">'
						+'<select style="height:31px; float:left; width:90px;display:none;" name="dynaCateCode">'
						+getOptions()+"</select>"
						+'<span id="categorySpan" name="categorySpan" value="'+key+'" class="categorySpan">【'+dynaWorks[0].dynaCateName+'】</span>'
						+'</div>';
		
		var content = "";
		if(dynaWorks.length>0){
			for(var ii = 0;ii<dynaWorks.length;ii++){
				var dw = dynaWorks[ii];
				content += '<div id="content" name="content" index="'+key+'">'
				+'<div class="main_conter"><p class="noPrint">事项内容：</p><textarea class="content textFont" readOnly id="content" name="content" rows=1 cols=40  onfocus="window.activeobj=this;this.clock=setInterval(function(){activeobj.style.height=activeobj.scrollHeight+\'px\';},200);" onblur="clearInterval(this.clock);">'+dw.content+'</textarea></div>'
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

//*****************************************************JS动态效果开始************************************************************

//分类下拉框内容：
function getOptions(){
	return $("select:first").html();
}

//*****************************************************JS动态效果结束************************************************************
