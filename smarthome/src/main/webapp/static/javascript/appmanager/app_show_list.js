
$(function(){
	//配置artDialog全局默认参数
	(function(config){
		config['lock'] = true;
		config['fixed'] = true;
		config['resize'] = false;
		config['background'] = '#000';
		config['opacity'] = 0.5;
	})($.dialog.defaults);
	//toolTip
	$('[rel="tooltip"]').tooltip();
	//表单提示
	$("[datatype]").focusin(function(){
		$(this).parent().addClass('info').children('.infomsg').show().siblings('.help-inline').hide();
	}).focusout(function(){
		$(this).parent().removeClass('info').children('.infomsg').hide().siblings('.help-inline').show();
	});
	//detailIframe
	openDetailIframe = function(url){
		ZENG.msgbox.show('正在载入中，请稍后...', 6, 100000);
		$('#detailIframe iframe').attr('src', url).load(function(){
			$('body').css('overflow', 'hidden');
			ZENG.msgbox._hide();
			$('#detailIframe').animate({
				'top' : 0,
				'opacity' : 'show'
			}, 500);
		});
	};
	closeDetailIframe = function(callback){
		$('body').css('overflow', 'auto');
		$('#detailIframe').animate({
			'top' : '-100px',
			'opacity' : 'hide'
		}, 500, function(){
			callback && callback();
		});
	};
});
$(function(){
	//加载列表
	getPageList(0);
	//删除，推荐
	$('.list-con').on('click', '.do-del', function(){
		var appid = $(this).attr('appid');
		var appname = $(this).parents('tr').children('td:first-child').text();
		$.dialog({
			id : 'del',
			content : '确定要删除 “' + appname + '” 该应用么？',
			ok : function(){
				$.ajax({
					type : 'POST',
					url : clickmedBaseUrl+'/app/doDeleteApp',
					data : 'appId=' + appid
				}).done(function(){
					$('#pagination').trigger('currentPage');
				});
			},
			cancel: true
		});
	});
	//搜索
	$('a[menu=search]').click(function(){
		getPageList(0);
	});
});
function initPagination(current_page){
	$('#pagination').pagination(parseInt($('#pagination_setting').attr('count')), {
		current_page : current_page,
		items_per_page : parseInt($('#pagination_setting').attr('per')),
		num_display_entries : 9,
		num_edge_entries : 2,
		callback : getPageList,
		prev_text : '上一页',
		next_text : '下一页'
	});
}
function getPageList(current_page){
	ZENG.msgbox.show('正在加载中，请稍后...', 6, 100000);
	var from = current_page * parseInt($('#pagination_setting').attr('per')), to = parseInt($('#pagination_setting').attr('per'));
	var pagedata={"start":current_page*10,"length":10,name:$('#search_1').val()};
	$.ajax({
		type : 'POST',
		url : clickmedBaseUrl+'/app/getAppList',
		data : pagedata,
		success : function(msg) {
			var applist = "";
			var totleCount = 0;
			if(msg!=null&&msg.length>0){
				totleCount = msg[0].totleCount;
				for ( var i = 0; i < msg.length; i++) {
					applist = applist 
					+"<tr class=\"list-bd\">"
					+"<td style=\"text-align: left; padding-left: 15px\">"
					+"<img src=\""+msg[i].icon+"\" alt=\""+msg[i].name+"\" class=\"appicon\">"
					+"<span class=\"appname\">"+msg[i].name+"</span></td>"
					+"<td>"+msg[i].typeName+"</td>"
					+"<td><a href=\"javascript:openDetailIframe('app_edit.html?appid="+msg[i].tbid+"');\" class=\"btn btn-mini btn-link\">编辑</a>"
					+"<a href=\"javascript:;\"class=\"btn btn-mini btn-link do-del\" appid=\""+msg[i].tbid+"\">删除</a></td></tr>"
					}
			}
			$('#pagination_setting').attr('count', totleCount);
			$('.list-count').text(totleCount);
			$('.list-con').html(applist);
			initPagination(current_page);
			ZENG.msgbox._hide();
		}
	});
	
}
function initPagination(current_page){
	$('#pagination').pagination(parseInt($('#pagination_setting').attr('count')), {
		current_page : current_page,
		items_per_page : parseInt($('#pagination_setting').attr('per')),
		num_display_entries : 9,
		num_edge_entries : 2,
		callback : getPageList,
		prev_text : '上一页',
		next_text : '下一页'
	});
}
