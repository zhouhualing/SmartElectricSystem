$(function(){
	//配置artDialog全局默认参数
	(function(config){
		config['lock'] = true;
		config['fixed'] = true;
		config['resize'] = false;
		config['background'] = '#000';
		config['opacity'] = 0.5;
	})($.dialog.defaults);
	//加载列表
	getPageList();
	//删除
	$('.list-con').on('click', '.do-del', function(){
		var phraseId = $(this).attr('phraseId');
		$.dialog({
			id : 'del',
			content : '确定要删除该常用语么？',
			ok : function(){
				$.ajax({
					type : 'GET',
					url : clickmedBaseUrl+'/user/deletePhrase',
					data : {'phraseId':phraseId}
				}).done(function(){
					getPageList();
				});
			},
			cancel: true
		});
	});
	//添加
	$('.btn').click(function(){
		var language = $("#phrase").val();
		if(language!=null&&language!=""){
			var data = {'language':language};
			doRequest('/user/addPhrase',data,function(){
				getPageList();
			});
		}
	});
});
function getPageList(){
	ZENG.msgbox.show('正在加载中，请稍后...', 6, 100000);
	doRequest('/user/findPhraseList',null,function(msg) {
		var list = "";
		var data = msg.data;
		if(msg.result==true&&data.length>0){
			for ( var i = 0; i < data.length; i++) {
				var j=i+1;
				list = list 
				+"<tr class=\"list-bd\" id ='"+j+"'>"
				+"<td style=\"text-align: left; padding-left: 15px;word-break:break-all;\">"+data[i].language+"</td>"
				+"<td><a href=\"javascript:;\"class=\"btn btn-mini btn-link do-del\" phraseId=\""+data[i].id+"\">删除</a></td></tr>"
				}
		}
		$('.list-con').html(list);
		ZENG.msgbox._hide();
		$("#phraseList > tbody").tableDnD({
	        onDragClass: "myDragClass",
	        onDrop: function(table, row) {
	        	var rows = table.rows;
	            var sort = '';
	            for (var i=0; i<rows.length; i++) {
	            	sort += rows[i].id+',';
	            }
	            doRequest('/user/changePhraseSort',{'sort':sort},function(data){
	            	getPageList();
	            });
	        },
	        onDragStart: function(table, row) {
	        }
	    });
	});
}

