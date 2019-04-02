$(function(){
	doInitMenu();
})

var clickmedBtnPermission = new Array();

function doInitMenu() {
	doJsonRequest("/menu/getCurUserReceiveMenu", null, function(data,status){
		if(data.result) {
			var data = data.data;
			var menu = data.menuDTOs;
			clickmedBtnPermission = data.btnPermission;
			var menuChildern = new Array();
			for(var i=0; i<menu.length; i++) {
				if(menu[i].parentId == null) {
					var tempOneLevel = '<div class="left_bar_tit"  ><span><img status="close" src="../../static/css/comstyle/images/tit_ico.png" width="10" height="9" /></span>'+menu[i].menuName+'</div> <ul class="nav_list" style="display:none"  id="menuli'+menu[i].id+'" ></ul>';
					$(".left_bar").append(tempOneLevel);							  
				} else {
					menuChildern.push(menu[i]);
				}				
			}
			for(var i=0; i<menuChildern.length; i++) {
				var tempTwoLevel = '<a href="../../'+menuChildern[i].url+'" target="mainFrame"><li><span></span>'+menuChildern[i].menuName+'</li></a>'
				$("#menuli"+menuChildern[i].parentId).append(tempTwoLevel);
			}
			$(".left_bar_tit").each(function(){
				$(this).on("click",function(){
					$(this).next("ul").slideToggle("fast",function(){
						if($(this).prev("div").children().find("img").attr("status") == "close") {
							$(this).prev("div").children().find("img").attr("src","../../static/css/comstyle/images/tit_ico_up.png").attr("status","open");
						} else {
							$(this).prev("div").children().find("img").attr("src","../../static/css/comstyle/images/tit_ico.png").attr("status","close");
						}
						
					});
				})
			})
			
			$("li").on("click",function(){
				$(".nav_list_tag").removeClass("nav_list_tag");
				$(this).addClass("nav_list_tag");
			})
			
			if($(".left_bar_tit").length >0) {
				$(".left_bar_tit").eq(0).trigger("click")
			}
			
		}
	})
}