var flag = false;
$(function() {
    doInitMenu();
    $(".left_time").html(new Date().format('yyyy年M月d日'));
})
var clickmedBtnPermission = new Array();

var key = getURLParam("businessType", true);
function doInitMenu() {
    doJsonRequest("/menu/getCurrentUserMenuByBusinessType", key, function(data, status,XMLHttpRequest) {
        if (data.result) {
            var data = data.data;
            var menu = data.menuDTOs;
            if(menu.length>1){
            	flag = true;
            }
            clickmedBtnPermission = data.btnPermission;
            var menuChildern = new Array();
            for (var i = 0; i < menu.length; i++) {
                if (menu[i].parentId == null) {
                    var tempOneLevel = '<div class="list_one" ><p class="list_two_title oneLevel">' + menu[i].menuName + '</p><ul id="menuli' + menu[i].id + '" ></ul></div>';
                    $(".left_list").append(tempOneLevel);
                } else {
                    menuChildern.push(menu[i]);
                }
            }
            for (var i = 0; i < menuChildern.length; i++) {
                var tempTwoLevel = '<li><a href="../../' + menuChildern[i].url + '" target="mainFrame"><span></span>' + menuChildern[i].menuName + '</a></li>'
                $("#menuli" + menuChildern[i].parentId).append(tempTwoLevel);
            }
            $(".oneLevel").each(function() {
                $(this).on("click", function() {
                   // alert("lalal");
                    if ($(this).hasClass("sel")) {
                        $(this).removeClass("sel");
                      
                    }
                    else {
                        $(this).addClass("sel");
                      
                    }
                    $(this).next("ul").slideToggle("fast", function() {
//						if($(this).prev("div").children().find("img").attr("status") == "close") {
//							$(this).prev("div").children().find("img").attr("src","../../static/css/comstyle/images/tit_ico_up.png").attr("status","open");
//						} else {
//							$(this).prev("div").children().find("img").attr("src","../../static/css/comstyle/images/tit_ico.png").attr("status","close");
//						}

                    });
                })
            })

//			$("li").on("click",function(){
//				$(".nav_list_tag").removeClass("nav_list_tag");
//				$(this).addClass("nav_list_tag");
//			})

            if ($(".oneLevel").length > 0) {
               $(".oneLevel").trigger("click")
            }
            if(flag){
                if(key=="0001"){
                	window.parent.document.getElementById("mainFrame").src="/cmcp/view/workflow/self_todo_list.html?appId=3";
                }else if(key=="0002"){
                	window.parent.document.getElementById("mainFrame").src="/cmcp/view/actionlist/mecreate_actionlist.html?check=1";
                }else if(key=="0003"){
                	window.parent.document.getElementById("mainFrame").src="/cmcp/view/supervise/supervise_actionlist_search_list.html";
                }else if(key=="0010"){
                	window.parent.document.getElementById("mainFrame").src="/cmcp/view/workflow/self_todo_list.html?appId=271";
                }else if(key=="0011"){
                	window.parent.document.getElementById("mainFrame").src="/cmcp/view/workflow/self_todo_list.html?appId=272";
                }else if(key=="0013"){
                	window.parent.document.getElementById("mainFrame").src="/cmcp/view/dynamicwork/check_list.html?task=1";
                }else if(key=="0015"){
                	window.parent.document.getElementById("mainFrame").src="../../"+menuChildern[0].url;;
                }
            }
        }
    });
}