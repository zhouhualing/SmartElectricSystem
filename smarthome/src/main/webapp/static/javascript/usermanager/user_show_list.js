var emailUrl = "";
var managerUrl  = "";
$(function(){
	doRequest("/user/getEmailInfo",null,function(data){
			if(data.result=true){
				var data = data.data;
				emailUrl = data.url;
				managerUrl = "http://"+emailUrl +"/extman/cgi/index.cgi?"+data.user+"_"+data.password;
				$("#extman").attr("src",managerUrl);
			}
	});
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
	//加载列表
	getPageList(0);
	//停用
	$('.list-con').on('click', '.do-disable', function(){
		var userId = $(this).attr('userId');
		var userCode = $(this).attr('userCode');
		var userName = $(this).attr('userName');
		$.dialog({
			id : 'disable',
			content : '确定要停用该用户么？',
			ok : function(){
				$.ajax({
					type : 'POST',
					url : clickmedBaseUrl+'/user/modifyUserStatus',
					data : {'userId':userId,'status':"0001"}
				}).done(function(){
					//停用邮箱用户
					$("#extman").attr("src","/cmcp/view/email/edit_user.html?code="+userCode+"&name="+userName+"&url="+emailUrl+"&status=0001");
					$('#pagination').trigger('currentPage');
				});
			},
			cancel: true
		});
	});
	//启用
	$('.list-con').on('click', '.do-able', function(){
		var userId = $(this).attr('userId');
		var userCode = $(this).attr('userCode');
		var userName = $(this).attr('userName');
		$.dialog({
			id : 'able',
			content : '确定要启用该用户么？',
			ok : function(){
				$.ajax({
					type : 'POST',
					url : clickmedBaseUrl+'/user/modifyUserStatus',
					data : {'userId':userId,'status':"0002"}
				}).done(function(){
					//启用邮箱用户
					$("#extman").attr("src","/cmcp/view/email/edit_user.html?code="+userCode+"&name="+userName+"&url="+emailUrl+"&status=0002");
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
		url : clickmedBaseUrl+'/user/getUserList',
		data : pagedata,
		dataType:"JSON",
		success : function(msg) {
			var applist = "";
			var totleCount = 0;
			if(msg!=null&&msg.length>0){
				totleCount = msg[0].totleCount;
				for ( var i = 0; i < msg.length; i++) {
					applist = applist
					+"<tr class=\"list-bd\">"
					+"<td style=\"text-align: left; padding-left: 15px\"><span class=\"appname\">"+msg[i].userName+"</span></td>"
					+"<td>"+msg[i].userCode+"</td>"
					+"<td>"+msg[i].orgName+"</td>"
					if(msg[i].status=="0001"){
						applist = applist
						+"<td><a href=\"javascript:;\"class=\"btn btn-mini btn-link do-able\" userId=\""+msg[i].userId+"\" userCode=\""+msg[i].userCode+"\" userName=\""+msg[i].userName+"\" >启用</a></td></tr>"
					}else if(msg[i].status=="0002"){
						applist = applist
						+"<td><a href=\"javascript:openDetailIframe('user_edit.html?userid="+msg[i].userId+"');\" class=\"btn btn-mini btn-link\">编辑</a>"
						+"<a href=\"javascript:;\"class=\"btn btn-mini btn-link do-disable\" userId=\""+msg[i].userId+"\" userCode=\""+msg[i].userCode+"\" userName=\""+msg[i].userName+"\" >停用</a></td></tr>"
					}else{
						applist = applist+"<td>未知状态</td>"
					}
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

