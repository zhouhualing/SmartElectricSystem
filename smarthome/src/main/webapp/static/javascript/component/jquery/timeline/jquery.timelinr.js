/* ----------------------------------
jQuery Timelinr 0.9.54
tested with jQuery v1.6+

Copyright 2011, CSSLab.cl
Free under the MIT license.
http://www.opensource.org/licenses/mit-license.php

instructions: http://www.csslab.cl/2011/08/18/jquery-timelinr/
---------------------------------- */

jQuery.fn.timelinr = function(options){
	// default plugin settings
	settings = jQuery.extend({
		containerDiv: 				'#timeline',		// value: any HTML tag or #id, default to #timeline
		datesDiv: 					'#dates',			// value: any HTML tag or #id, default to #dates
		datesSelectedClass: 		'selected',			// value: any class, default to selected
		datesSpeed: 				'normal',			// value: integer between 100 and 1000 (recommended) or 'slow', 'normal' or 'fast'; default to normal
		issuesDiv: 					'#issues',			// value: any HTML tag or #id, default to #issues
		issuesSelectedClass: 		'selected',			// value: any class, default to selected
		issuesSpeed: 				'fast',				// value: integer between 100 and 1000 (recommended) or 'slow', 'normal' or 'fast'; default to fast
		issuesTransparency: 		0.2,				// value: integer between 0 and 1 (recommended), default to 0.2
		issuesTransparencySpeed: 	500,				// value: integer between 100 and 1000 (recommended), default to 500 (normal)
		startAt: 					1					// value: integer, default to 1 (first)
	}, options);

	$(function(){
        $(".timeLineP").children("li").children(".ui-step-icon").children(".iconfont,.ui-step-number").click(function(event){
            if($(this).parent().parent().hasClass("ui-step-nodone")) {
                return false;
            }
			event.preventDefault();
            $(".ui-step-now-iofont").removeClass("ui-step-now-iofont");
            $(".ui-step-now-number").removeClass("ui-step-now-number");
            $(".table_view").hide();
            $(".iconfont",$(this).parent().parent()).addClass("ui-step-now-iofont");
            $(".ui-step-number",$(this).parent().parent()).addClass("ui-step-now-number");
            $(".table_view[taskNode='"+$(this).parents("li").attr("taskNode")+"']").show();
		});
        $(".timeLineP").children("li").children(".ui-step-icon").eq(settings.startAt-1).children(".iconfont,.ui-step-number").trigger('click');
	});
};
