var id = getURLParam("id");
var fromId = getURLParam("fromId");//通知中心id，当前页面每生成一个督办单，需修改通知中心NoticeCenterEntity.createCardIds字段
if(fromId == null || typeof(fromId) == "undefined") { 
	fromId = 0;
}
var fromPage = getURLParam("fromPage");
var btnSource = "app";
if(fromPage == 'pc'){
	btnSource = fromPage;
}
var mark = getURLParam("mark");
$("#id").val(id);
$(function(){
	//初始化下拉框：
	$("#dynaCate").contents().find("select").append(getOptions());
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userName").val(data.userName);
		$("#isMayor").val(data.isMayor);
		$("#orgCode").val(data.orgCode);
		$("#endDynaWork").show();
		/*if(data.orgCode == '001' || data.orgCode == '002' || data.orgCode == '003' || data.orgCode == '004' || data.orgCode == '005'){
			
		}*/
	});
	if(id!='' && id!=null){
		var dto = {
				id:id,
				noticenterId:fromId
		}
		doJsonRequest("/dynawork/getDynaWorkIndex",dto,function(data){
			if(data.result) {
				var data = data.data;
				var subitems = data.subitems;//子项
				$("#issue").val(data.issue);
				$("#allIssue").val(data.allIssue);
				$("#title").val(data.title);
				$("#cardIds").val(data.createCardIds);
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
	var cardIdItems = new Array();
	var cardIds = $("#cardIds").val();
	if(cardIds!=null && cardIds!=''){
		cardIdItems = cardIds.split(",");	
	}
	var keys = new Array();
	var j = 0;
	for(var key in subitems){
		keys[j] = key;
		j++;
	}
	var subitemDIV = "";
	for(var i = 0;i<keys.length;i++){
		var key = keys[i];
		var dynaWorks = subitems[key];
		var catName = $("select:first option[value='"+key+"']").html();
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
				var btn = "";
				if(cardIdItems.indexOf(''+dw.id)==-1 && ($("#orgCode").val() == '001' || $("#orgCode").val() == '002' || $("#orgCode").val() == '003' || $("#orgCode").val() == '004' || $("#orgCode").val() == '005')){
					btn = '<div class="down_btn"><input type="button" value="批示" class="btn_click" onclick="initSuperviseCard('+dw.id+','+$("#issue").val()+')"/></div>';
				}
				content += '<div id="content" name="content" index="'+key+'">'
				+'<div class="main_conter"><p class="noPrint">事项内容：</p><textarea class="content textFont" readOnly id="content" name="content" rows=1 cols=40  onfocus="window.activeobj=this;this.clock=setInterval(function(){activeobj.style.height=activeobj.scrollHeight+\'px\';},200);" onblur="clearInterval(this.clock);">'+dw.content+'</textarea></div>'
				+ btn
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


//完结本期刊，并在通知中心删除
function endDynaWork(){
	$.confirm({msg:"确定要办结么？",confirmClick:function(){
		doJsonRequest("/noticenter/setEndTodo",fromId, function(data,status){
			if(data.result) {
				window.location.href="../tododocs/docs_down.html?flag=1&fromPage=0002&btnSource="+btnSource+"&mark="+mark;
			} else {
				$.alert("办结失败。");
			}
		},{showWaitting:true})				
	},data:"1"})
}

//生成督办单
function initSuperviseCard(id,issue){
	if($("#orgCode").val() == "001"){
		//市长督办单
		window.location.href = "supervisecard_sz_draft.html?fromList=d&id="+id+"&issue="+issue+"&noticenterId="+fromId;
	}else{
		//副市长督办单
		window.location.href = "supervisecard_draft.html?fromList=d&id="+id+"&issue="+issue+"&noticenterId="+fromId;
	}
}

//*****************************************************JS动态效果开始************************************************************

//分类下拉框内容：
function getOptions(){
	return $("select:first").html();
}

//*****************************************************JS动态效果结束************************************************************
