var dto = {
		id:getURLParam("id")
}
//doJsonRequest("/permission/getMenu",dto,function(data){
doJsonRequest("/permission/getMenuTreeByRole",dto,function(data){
	if(data.result) {
		var str = "";
		var permission = data.data.permission;
		for(var i=0; i<permission.length; i++) {
			str = str + '<li><a href="javascript:void(0)" onclick="clickLink(\'/spms'+permission[i].url+'\')" title="'+permission[i].permissionName+'" target="mainFrame"><i class="icon-circle-arrow-right"></i>&nbsp;'+permission[i].permissionName+'</a></li>';
		}
        $(".icon-chevron-down").after("&nbsp;"+getURLParam("name"));
		$(".nav-list").append(str);
		$(".accordion-heading a").click(function() {
            $(".accordion-body").find(".accordion-inner").toggle();
            $(this).toggleClass("collapsed");
            $('.accordion-toggle i').toggleClass("icon-chevron-down").toggleClass("icon-chevron-right");
			/*$('.accordion-toggle i').removeClass('icon-chevron-down');
			$('.accordion-toggle i').addClass('icon-chevron-right');
			if (!$($(this).attr('href')).hasClass('in')) {
				$(this).children('i').removeClass('icon-chevron-right');
				$(this).children('i').addClass('icon-chevron-down');
			}*/
		});
		$(".accordion-body a").click(function() {
			$("#menu li").removeClass("active");
			$("#menu li i").removeClass("icon-white");
			$(this).parent().addClass("active");
			$(this).children("i").addClass("icon-white");
		});
		$(".accordion-body a:first i").click();
	} else {
		$.alert("菜单获取失败。");
	}
});

function clickLink(url) {
	window.parent.mainFrame.location.href = url;
	//parent.wSize();
}

var leftWidth = "160"; // 左侧窗口大小
$(function () {
    if (!Array.prototype.map) {
        Array.prototype.map = function (fn, scope) {
            var result = [], ri = 0;
            for (var i = 0, n = this.length; i < n; i++) {
                if (i in this) {
                    result[ri++] = fn.call(scope, this[i], i, this);
                }
            }
            return result;
        };
    }
    var getWindowSize = function () {
        return ["Height", "Width"].map(function (name) {
            return window.parent.window["inner" + name] || window.parent.document.compatMode === "CSS1Compat" && window.parent.document.documentElement[ "client" + name ] || window.parent.document.body[ "client" + name ];
            //return window["inner" + name] || document.compatMode === "CSS1Compat" && document.documentElement[ "client" + name ] || document.body[ "client" + name ];
        });
    };

    function wSize() {
        var minHeight = 500, minWidth = 980;
        var strs = getWindowSize().toString().split(",");
        var heightIs = strs[0] < minHeight ? minHeight : strs[0];
        $(".accordion-inner").height( heightIs- $(".accordion-heading").height() - 10);
        $("#menuFrame",parent.document).attr("height",heightIs- $(".accordion-heading").height()-70);
    }

    $(window).resize(function () {
        wSize();
    });
    wSize();
});

$(window).resize(function(){
    wSize();
});