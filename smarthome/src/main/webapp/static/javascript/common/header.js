$(function(){
	getMenu();
})

function getMenu() {
	//doJsonRequest("/permission/getMenu",null,function(data){
	doJsonRequest("/permission/getMenuByuserRole",null,function(data){
		if(data.result) {
			var str = "";
			$("#userName").html(data.data.userInfo.userName)
			var data = data.data.permission;
			var j = 1;
			for(var i=0; i<data.length;i++) {
				str = str + "<li id='"+data[i].id+"' class=''><a onclick='blank()' class='nav_ico nav_"+(j++)+"' rel='#000' target='"+((data[i].permissionType=="1")?"mainFrame":"menuFrame")+"' href='/spms"+data[i].url+"?id="+data[i].id+"&name="+data[i].permissionName+"'><em>"+data[i].permissionName+"</em><i></i></a></li>"
			}
			$("#test").val(str)
			$("#example-two").append(str);
			$("li",$("#example-two")).eq(0).addClass("current_page_item_two");
			$("a[target='menuFrame']",$("#example-two")).on("click",function(){
				$("#menuFrame").show();
				wSize();
			})
			$("a[target='mainFrame']",$("#example-two")).on("click",function(){
				$("#menuFrame").hide();
				wSize();
			})
			doMenuEnhand();
		} else {
			
		}
	})
}

function doLoginOut() {
	var dto = {};
	doJsonRequest("/doLoginOut",dto,function(data) {
		if(data.result) {
			window.location.href="login.html"
		} else {
			$.alert("退出失败。");
		}
	})
}
function blank(){

   document.getElementById("mainFrame").src = "blank.html"
}

/**
 * 菜单滑动处理
 */
function doMenuEnhand() {
    var $el, leftPos, newWidth,
    $mainNav2 = $("#example-two");
    $mainNav2.append("<li id='magic-line-two'></li>");    
    var $magicLineTwo = $("#magic-line-two");
	try{
	    var left = $(".current_page_item_two a").position().left;
		var l=left;
	    $magicLineTwo
	        .width($(".current_page_item_two").width())
	        .height($mainNav2.height())
	        .css("left", $(".current_page_item_two a").position().left)
	        .data("origLeft", $(".current_page_item_two a").position().left)
	        .data("origWidth", $magicLineTwo.width())
	        .data("origColor", $(".current_page_item_two a").attr("rel"));
	      
		 $(function(){
			 var liname = $("#example-two li");
	             liname.each(function(i){
	                $(this).click(function(){
	                       liname.removeClass("current_page_item_two")
	                       $(this).addClass("current_page_item_two");
						   l=left+i*93;
	                       //return false;    //加这句来阻止跳转 可用来调试效果
	                });
	             });
		 })

	            
	    $("#example-two li").find("a").hover(function() {
	        $el = $(this);
	        leftPos = $el.position().left;
	        newWidth = $el.parent().width();
	        $magicLineTwo.stop().animate({
	            left: leftPos,
	            width: newWidth,
	            backgroundColor: $el.attr("rel")
	        })
	    }, function() {
	        $magicLineTwo.stop().animate({
	            left:l,
	            width: $magicLineTwo.data("origWidth"),
	            backgroundColor: $magicLineTwo.data("origColor")
	        });    
	    });
	}catch(e){
		
	}	
}



