/*; (function ($) {
    $.extend({
        'nicenav': function (con) {
            con = typeof con === 'number' ? con : 400;
            var $lis = $('#nav>li'), $h = $('#buoy')
            $lis.hover(function () {
                $h.stop().animate({
                    'left': $(this).offsetParent().context.offsetLeft
                }, con);
            }, function () {
                $h.stop().animate({
                    'left': '0px'
                }, con);
            })
        }
    });
}(jQuery));*/

$(document).ready(function(){
    $('#nav>li').click(function(){
        var lis = $('#nav>li');
        var h = $('#buoy');
        var marginleft =  $(this).offsetParent().context.offsetLeft;
        $('#nav').attr('data',marginleft);
        h.css('left',marginleft);
        $('.menu_arrow').remove();
        $(this).append('<div class="menu_arrow"></div>');
    })
    $('#nav>li').hover(function(){
        var lis = $('#nav>li');
        var h = $('#buoy');
        var marginleft =  $(this).offsetParent().context.offsetLeft;
        h.stop().css('left',marginleft);
    },function(){
        var h = $('#buoy');
        var nowleft = $('#nav').attr('data');
        nowleft = nowleft ? nowleft : 0;
        h.stop().css('left',nowleft+'px');
    })
})