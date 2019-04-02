/*
**  开始菜单
*/
HROS.startmenu = (function(){
	return {
		/*
		**	初始化
		*/
		init : function(){
			$('#startmenu-container').on('mousedown', function(e){
				e.preventDefault();
			});
			$('#startmenu-container .startmenu-nick a, #startmenu-container .startmenu-avatar img').on('click', function(){
				//HROS.startmenu.openAccount();
			});
			$('#startmenu-container .startmenu-exit a').on('click', function(){
				HROS.base.logout();
			});
			$('#startmenu-container .startmenu-lock').on('click', function(){
				HROS.lock.show();
			});
			$('.dock-tool-start').on('click', function(){
				HROS.startmenu.openAccount();
			});
//			$('#startmenu-container .startmenu-feedback').on('click', function(){
//				HROS.window.createTemp({
//					appid : 'hoorayos-feedback',
//					title : '反馈',
//					url : 'http://hoorayos.com/feedback.html',
//					width : 700,
//					height : 500,
//					isflash : false
//				});
//			});
			$('#startmenu-container .startmenu a').on('click', function(){
				switch($(this).attr('class')){
					case 'help':
						HROS.base.help();
						break;
					case 'about':
						HROS.copyright.show();
						break;
				}
			});
		},
		/*
		**  账号设置窗口
		*/
		openAccount : function(){
			if(HROS.CONFIG.memberID != 0){
				HROS.window.createTemp({
					appid : 'zhsz',
					title : '账号设置',
					url : '/cmcp/view/usermanager/account_edit.html',
					width : 550,
					height : 580
				});
			}else{
				HROS.base.login();
			}
		},
		show : function(){
			HROS.popupMenu.hide();
			HROS.folderView.hide();
			HROS.searchbar.hide();
			$('#startmenu-container').css({
				top : 'auto',
				left : 'auto',
				right : 'auto',
				bottom : 'auto'
			}).show();
			switch(HROS.CONFIG.dockPos){
				case 'top':
					$('#startmenu-container').css({
						top : $('#dock-container').height() - 1,
						right : $('#dock-container').offset().left
					});
					break;
				case 'left':
					$('#startmenu-container').css({
						bottom : $('#dock-container').offset().top,
						left : $('#dock-container').width() - 1
					});
					break;
				case 'right':
					$('#startmenu-container').css({
						bottom : $('#dock-container').offset().top,
						right : $('#dock-container').width() - 1
					});
					break;
			}
		},
		hide : function(){
			$('#startmenu-container').hide();
		}
	}
})();