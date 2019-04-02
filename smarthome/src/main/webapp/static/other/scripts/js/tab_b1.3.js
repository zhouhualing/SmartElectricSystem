// JavaScript Document

$(function () {

    $(".tab li").click(function () {
        var index = $(this).index();
        $(this).addClass("current").siblings().removeClass("current");
        $(".tab_content #tab").eq(index).show().siblings().hide();
    });
    $(".tab2 li").click(function () {
        var index = $(this).index();
        $(this).addClass("current").siblings().removeClass("current");
        $(".tab_content #tab2").eq(index).show().siblings().hide();
    });

});