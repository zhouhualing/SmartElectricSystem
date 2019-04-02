// JavaScript Document
$(document).ready(function(){
	var allHeight = window.innerHeight;
	var allWidth = window.innerWidth;	
	var h = allHeight;
	var w = allWidth;
	$(".login_box").css("height",h+"px");
	$(".ow_main_login_con").css("height",h-74+"px");
	$(".ow_main_login_con_h").css("height",h-74-68+"px");
	/*$(".main_box").css("height",h-74-30+"px");*/
	$('.box_right').css('width',w-210-28-14+'px');
	$('.two_charts').css('width',(w-210-28-14-12)/2+'px');
	$('.block_charts .two_charts').first().css('margin-right','8px');
		
	$(window).resize(function(){
		var allHeight = window.innerHeight;
	var allWidth = window.innerWidth;	
	var h = allHeight;
	var w = allWidth;
	$(".login_box").css("height",h+"px");
	$(".ow_main_login_con").css("height",h-74+"px");
	$(".ow_main_login_con_h").css("height",h-74-68+"px");
	/*$(".main_box").css("height",h-74-30+"px");*/
	$('.box_right').css('width',w-210-28-14+'px');
	$('.two_charts').css('width',(w-210-28-14-12)/2+'px');
	$('.block_charts .two_charts').first().css('margin-right','8px');
	});
})