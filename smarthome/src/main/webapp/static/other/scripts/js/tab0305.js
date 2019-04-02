// JavaScript Document
function tabClick(){
        if($(this).hasClass('activeTab')) 
                return;
         $('.oilsc_main_center_second_t_r ul li').removeClass('activeTab');
         $(this).addClass('activeTab');
        var tabId = $(this).attr('tabId');
        $('#content > div').hide();
        $('#' + tabId).show();
}
$(document).ready(function(){
        $('.oilsc_main_center_second_t_r ul li').click(tabClick);
})