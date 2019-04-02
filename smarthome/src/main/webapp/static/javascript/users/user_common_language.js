var userCode = getURLParam("userCode");
$(function(){
	$("#userEntity").val(userCode);
	doQuery();
	$('.btn').click(function(){
		var language = $("#phrase").val();
		if(language!=null&&language!=""){
			var data = {'language':language};
			doRequest('/user/addPhrase',data,function(){
				doQuery();
			});
		}
	});
});

var ii = 1;
function initFun(data,key,tr) {
	if(key=="language") {
		$(tr).attr("id",ii++);
	}
}

function queryEnd() {
	clickmedTables.commonLanguageInfoDiv.hideFoot();
	ii=1;
	$("#commonLanguageInfoDiv > div > table > tbody").tableDnD({
        onDragClass: "myDragClass",
        onDrop: function(table, row) {
        	var rows = table.rows;
            var sort = '';
            for (var i=0; i<rows.length; i++) {
            	sort += rows[i].id+',';
            }
            doRequest('/user/changePhraseSort',{'sort':sort},function(data){
            	doQuery();
            });
        },
        onDragStart: function(table, row) {
        }
    });
}

//编辑功能，暂时不用，后期优化
function edit(){
	//创建文本框对象 
	var inputobj = $("<input type='text'>");
	//获取当前点击的单元格对象 
	var tdobj = $(event.srcElement);
	//获取单元格中的文本 
	var text = tdobj.html();
	//如果当前单元格中有文本框，就直接跳出方法 
	//注意：一定要在插入文本框前进行判断 
	if(tdobj.children("input").length>0){
		return false;
	} 
	//清空单元格的文本 
	tdobj.html(""); 
	inputobj.css("border","0") 
	.css("font-size",tdobj.css("font-size")) 
	.css("font-family",tdobj.css("font-family")) 
	.css("background-color",tdobj.css("background-color")) 
	.css("color","#C75F3E")
	.css("line-height","normal")
	.width(tdobj.width())
	.val(text);
	tdobj.append(inputobj);
	inputobj.get(0).select(); 
	//阻止文本框的点击事件 
	inputobj.click(function(){
		return false; 
	}); 
	//处理文本框上回车和esc按键的操作 
	inputobj.keyup(function(event){
		//获取当前按键的键值 ,jQuery的event对象上有一个which的属性可以获得键盘按键的键值 
		var keycode = event.which; 
		//处理回车的情况 
		if(keycode==13){
			//获取当前文本框的内容 
			var inputtext = $(this).val(); 
			//将td的内容修改成文本框中的内容 
			tdobj.html(inputtext); 
		} 
		//处理esc的情况 ，将td中的内容还原成text 
		if(keycode == 27){ 
			tdobj.html(text); 
		} 
	});
}

function doCallBack() {
	return clickmedTables.commonLanguageInfoDiv.selectObjs;
}

var deleObj = {
		text:"删除",
		fun:function(data) {
			$.confirm({msg:"确定要删除么？",confirmClick:function(){
				//var dto = {'id':data.id};
				doRequest('/user/deletePhrase',{phraseId:data.id}, function(data){
					doQuery();
					if(data.result) {
						$.alert("删除成功。");
					} else {
						$.alert("删除失败。");
					}
				},{showWaitting:true});
			},data:"1"});
		}
};
var oppObj = [deleObj];




