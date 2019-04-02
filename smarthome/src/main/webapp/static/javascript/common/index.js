$("#header").load("header.html");
$("#footer").load("footer.html")


function jump(current, url) {
    $("#" + current).addClass("current_page_item_two");
    $("#mainFrame").attr("src", url);
}
function menuJump(url, id) {
    $("#menuFrame").attr("src", url + id);
}

//resize
function wSize() {
    var minHeight = 500, minWidth = 980;
    var strs = getWindowSize().toString().split(",");
    $("#mainFrame").height((window.innerHeight < minHeight ? minHeight : window.innerHeight) - 74 - 40);

    $("#openClose").height((strs[0] < minHeight ? minHeight : strs[0]) - $("#header").height() - $("#footer").height());
    $("#openClose").height($("#openClose").height() - 5);
    $("html,body").css({"overflow": "hidden", "overflow-x": "hidden", "overflow-y": "hidden"});
    var menuFrameWidth = $("#menuFrame").width();
    var closeWidth = $("#openClose").width();
    if ($('#menuFrame').css('display') == 'none')
        menuFrameWidth = 0;
    $("#mainFrame").width($("#footer").width() - menuFrameWidth - closeWidth - 18);

}


function getWindowSize() {
    return ["Height", "Width"].map(function (name) {
        return window["inner" + name] ||
            document.compatMode === "CSS1Compat" && document.documentElement[ "client" + name ] || document.body[ "client" + name ];
    });
};
$(window).resize(function () {
    wSize();
});
wSize();

//
//$("#openClose").click(function(){
//    var menuFrameWidth = $("#menuFrame").width();
//    if($(this).hasClass("close")){
//        $(this).removeClass("close");
//        $(this).addClass("open");
//        $("#left").animate({width:0,opacity:"hide"});
//        $("#right").animate({width:$("#content").width()-$("#openClose").width()-5})
//    }else{
//        $(this).addClass("close");
//        $(this).removeClass("open");
//        $("#left").animate({width:menuFrameWidth,opacity:"show"});
//        $("#right").animate({width:$("#content").width()-$("#openClose").width()-menuFrameWidth-9})
//    }});