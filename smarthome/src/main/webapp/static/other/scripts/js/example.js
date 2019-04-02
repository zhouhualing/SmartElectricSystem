$(function(){
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

});